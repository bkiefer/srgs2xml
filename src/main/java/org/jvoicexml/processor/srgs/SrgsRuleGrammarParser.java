/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.processor.srgs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleAlternatives;
import org.jvoicexml.processor.srgs.grammar.RuleComponent;
import org.jvoicexml.processor.srgs.grammar.RuleCount;
import org.jvoicexml.processor.srgs.grammar.RuleReference;
import org.jvoicexml.processor.srgs.grammar.RuleSequence;
import org.jvoicexml.processor.srgs.grammar.RuleSpecial;
import org.jvoicexml.processor.srgs.grammar.RuleTag;
import org.jvoicexml.processor.srgs.grammar.RuleToken;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A parser for SRGS grammars.
 *
 * @author Renato Cassaca
 * @author Dirk Schnelle-Walka
 * @version $Revision: 1370 $
 */
public class SrgsRuleGrammarParser implements RuleGrammarParser {

    private static EntityResolver entityResolver = new EmptyEntityResolver();
    private Map<String, Object> attributes;

    public static class EmptyEntityResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }

    public SrgsRuleGrammarParser() {
        attributes = new java.util.HashMap<>();
    }

    public List<Rule> load(final Reader reader) throws URISyntaxException, GrammarException {
        final InputSource source = new InputSource(reader);
        return load(source);
    }

    public List<Rule> load(final InputStream stream) throws URISyntaxException, GrammarException {
        final InputSource source = new InputSource(stream);
        return load(source);
    }

    public List<Rule> loadRule(final Reader reader) {
        try {
            final DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            final InputSource source = new InputSource(reader);
            return parseGrammar(builder.parse(source));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Rule> loadRule(InputStream stream) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            return parseGrammar(builder.parse(new InputSource(stream)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Rule> load(final InputSource inputSource)
            throws URISyntaxException, GrammarException {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);

            final Document document = builder.parse(inputSource);
            Node grammarNode = document.getFirstChild();
            while(! grammarNode.getNodeName().equalsIgnoreCase("grammar")
                || ! grammarNode.hasAttributes()
                || grammarNode.getAttributes().getNamedItem("version") == null) {
              grammarNode = grammarNode.getNextSibling();
            }

            // version attribute has been checked, now comes mode etc.
            String mode = null;
            Node modeNode = grammarNode.getAttributes().getNamedItem("mode");
            if (modeNode == null || (mode = modeNode.getTextContent()).equals("voice")) {
              mode = "voice";
              if (grammarNode.getAttributes().getNamedItem("xml:lang") == null) {
                throw new GrammarException("No language for mode voice specified.");
              }
            }
            /*
            if (grammarNode.getAttributes().getNamedItem("root") == null) {
              throw new GrammarException("No root rule specified.");
            }
            */
            if (grammarNode.getAttributes().getNamedItem("xmlns") == null) {
              throw new GrammarException("No namespace specified.");
            }

            final List<Rule> rules = parseGrammar(grammarNode);

            // Extract header from grammar
            final NamedNodeMap docAttributes = grammarNode.getAttributes();
            for (int i = 0; docAttributes != null && i < docAttributes.getLength(); i++) {
                final Node node = docAttributes.item(i);
                attributes.put(node.getNodeName(), node.getNodeValue());
            }

            return rules;
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (SAXException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Rule> parseGrammar(Node grammarNode) throws URISyntaxException {
        List<Rule> rules = new ArrayList<Rule>();
        NodeList childNodes = grammarNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node child = childNodes.item(i);
            final String nodeName = child.getNodeName();
            if (!nodeName.equalsIgnoreCase("rule")) {
                continue;
            }
            final NamedNodeMap attributes = child.getAttributes();
            final String ruleId = getAttribute(attributes, "id");
            int scope = Rule.PRIVATE;
            final String scopeStr = getAttribute(attributes, "scope");
            if (scopeStr != null) {
                if (scopeStr.equalsIgnoreCase("public")) {
                    scope = Rule.PUBLIC;
                }
            }

            final List<RuleComponent> components = evalChildNodes(child);
            if (components.size() == 1) {
                final Rule rule = new Rule(ruleId, components.get(0), scope);
                rules.add(rule);
            } else if (components.size() > 1) {
                final RuleSequence rs =
                    new RuleSequence(new ArrayList<RuleComponent>(components));
                Rule rule = new Rule(ruleId, rs, scope);
                rules.add(rule);
            }
        }
        return rules;
    }

    private String getAttribute(NamedNodeMap attributes, String name) {
        Node attribute = attributes.getNamedItem(name);
        if (attribute == null) {
            return null;
        }
        return attribute.getNodeValue();
    }

    private RuleComponent evalNode(final Text node) {
        final String text = node.getWholeText().trim();
        if (text.length() == 0) {
            return null;
        }
        if (text.charAt(0) == '"') {
          return new RuleToken(text);
        }
        String[] tokens = text.split("\\s+");
        if (tokens.length == 1) {
          return new RuleToken(text);
        }
        List<RuleComponent> toks = new ArrayList<>();
        for (String t : tokens) {
          toks.add(new RuleToken(t));
        }
        return new RuleSequence(toks);
    }

    private RuleComponent evalOneOf(final Node node) throws URISyntaxException {
        final RuleAlternatives res = new RuleAlternatives();
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            final RuleComponent component = evalNode(child);
            if (component != null) {
                final String weightStr = getAttribute(child.getAttributes(), "weight");
                if (weightStr == null) {
                    res.addAlternative(component);
                } else {
                    res.addAlternative(component, Double.parseDouble(weightStr));
                }
            }
        }

        return setLanguage(res, node.getAttributes());
    }

    private RuleComponent setLanguage(RuleComponent c, final NamedNodeMap attributes) {
      final String langStr = getAttribute(attributes, "xml:lang");
      if (langStr != null)
        c.setLanguage(langStr);
      return c;
    }


    private RuleComponent evalItem(final Node node) throws URISyntaxException {
        final NamedNodeMap attributes = node.getAttributes();

        final List<RuleComponent> components = evalChildNodes(node);
        RuleComponent component;
        if (components.size() == 1) {
            component = components.get(0);
        } else {
            final List<RuleComponent> sequenceComponents =
                new ArrayList<RuleComponent>(components);
            component = new RuleSequence(sequenceComponents);
        }

        int repeatMin = -1;
        int repeatMax = -1;
        double repeatProb = -1;
        final String repeatStr = getAttribute(attributes, "repeat");
        if (repeatStr != null) {
            int toIndex = repeatStr.indexOf('-');
            if (toIndex < 0) {
                repeatMin = Integer.parseInt(repeatStr);
                repeatMax = repeatMin;
            } else {
                String minStr = repeatStr.substring(0, toIndex);
                String maxStr = repeatStr.substring(toIndex + 1);
                if (minStr.trim().length() > 0) {
                    repeatMin = Integer.parseInt(minStr);
                }
                if (maxStr.trim().length() > 0) {
                    repeatMax = Integer.parseInt(maxStr);
                }
            }
        }

        final String repeatProbStr = getAttribute(attributes, "repeat-prob");
        if (repeatProbStr != null) {
            repeatProb = Double.parseDouble(repeatProbStr);
        }

        if ((repeatMin != -1) && (repeatMax != -1) && (repeatProb != -1)) {
            component = new RuleCount(component, repeatMin, repeatMax, repeatProb);
        } else if ((repeatMin != -1) && (repeatMax != -1)) {
            component = new RuleCount(component, repeatMin, repeatMax);
        } else if (repeatMin != -1) {
            if (repeatProb != -1) {
                component = new RuleCount(component, repeatMin,
                    RuleCount.REPEAT_INDEFINITELY, repeatProb);
            } else {
                component = new RuleCount(component, repeatMin);
            }
        }

        return setLanguage(component, attributes);
    }

    private RuleComponent evalReference(final Node node) throws URISyntaxException {
        final NamedNodeMap attributes = node.getAttributes();
        final String specialStr = getAttribute(attributes, "special");
        if (specialStr != null) {
            if (specialStr.equalsIgnoreCase("NULL")) {
                return RuleSpecial.NULL;
            } else if (specialStr.equalsIgnoreCase("VOID")) {
                return RuleSpecial.VOID;
            } else if (specialStr.equalsIgnoreCase("GARBAGE")) {
                return RuleSpecial.GARBAGE;
            }
        } else {
            final String uriStr = getAttribute(attributes, "uri");
            if (uriStr != null && uriStr.indexOf("#") == -1) {
                return new RuleReference(new URI(uriStr));
            } else if (uriStr != null) {
                final String ruleName =
                    uriStr.substring(uriStr.indexOf("#") + 1).trim();
                final String grammarName = uriStr.substring(0, uriStr.indexOf("#"));
                final String typeStr = getAttribute(attributes, "type");
                if (grammarName.isEmpty()) {
                    return new RuleReference(ruleName);
                } else if (typeStr == null) {
                    final URI uri = new URI(grammarName);
                    return new RuleReference(uri, ruleName);
                } else {
                    final URI uri = new URI(grammarName);
                    RuleReference res = new RuleReference(uri, ruleName);
                    res.setMediaType(typeStr.trim());
                    return res;
                }
            }
        }
        return null;
    }

    private RuleComponent evalNode(final Node node) throws URISyntaxException {
        final String nodeName = node.getNodeName();
        if (nodeName.equalsIgnoreCase("#text")) {
            final Text textNode = (Text) node;
            return evalNode(textNode);
        } else if (nodeName.equalsIgnoreCase("one-of")) {
            return evalOneOf(node);
        } else if (nodeName.equalsIgnoreCase("item")) {
            return evalItem(node);
        } else if (nodeName.equalsIgnoreCase("ruleref")) {
            return evalReference(node);
        } else if (nodeName.equalsIgnoreCase("token")) {
            String tokenText = node.getTextContent();
            return setLanguage(new RuleToken(tokenText), node.getAttributes());
        } else if (nodeName.equalsIgnoreCase("tag")) {
            Object tagObject = node.getTextContent();
            return new RuleTag(tagObject);
        } else if (nodeName.equalsIgnoreCase("example")) {
            // Ignore
        }
        return null;
    }

    private List<RuleComponent> evalChildNodes(Node node)
            throws URISyntaxException {
        final List<RuleComponent> ruleComponents = new ArrayList<RuleComponent>();
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            final RuleComponent component = evalNode(child);
            if (component != null) {
                ruleComponents.add(component);
            }
        }

        return ruleComponents;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
