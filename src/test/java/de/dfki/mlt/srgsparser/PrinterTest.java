package de.dfki.mlt.srgsparser;


import static org.junit.Assert.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvoicexml.processor.srgs.*;
import org.jvoicexml.processor.srgs.grammar.*;

@RunWith(Parameterized.class)
public class PrinterTest {
  public static final String ABNF_HEADER =
      "#ABNF 1.0 UTF-8;\n\n"
      + "language en-EN;\n"
      + "root $order;\n"
      + "mode voice;\n"
      + "tag-format \"semantics/1.0\";\n";
  
  public static final String XML_HEADER = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<grammar version=\"1.0\" root=\"order\" xml:lang=\"en\"\n"
      + "    xmlns=\"http://www.w3.org/2001/06/grammar\" mode=\"voice\"\n"
      + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
      + "    xsi:schemaLocation=\"http://www.w3.org/2001/06/grammar\n"
      + "                        http://www.w3.org/TR/speech-grammar/grammar.xsd\"\n"
      + "    tag-format=\"semantics/1.0\">\n";
  public static final String XML_FOOTER = "</grammar>\n";
  
  public static final String RESOURCE_DIR = "src/test/resources/";
  public static URI testURI(String name) {
    return new File(RESOURCE_DIR, name).toURI();
  }
  
  private Path path;
  
  public PrinterTest(Path p) {
    this.path = p;
  }
  
  @Parameterized.Parameters
  public static Collection<Path> official() throws IOException {
    return Files.walk(Path.of(RESOURCE_DIR + "/unixlf/test"), 1)
        .filter(p -> p.toFile().isFile()).collect(Collectors.toList()); 
  }
  
  boolean accepted(String name) {
    return ! (OfficialTest.toReject.contains(name)
        || name.contains("duplicated-rulenames"));
  }
  
  @Test
  public void printTest() throws URISyntaxException, IOException {
    String name = path.getFileName().toString();
    if (name.endsWith("grxml")) {
      if (accepted(name.replace("grxml", "gram"))) {
        printTestABNF(path);
      }
    } else {
      if (accepted(name)) {
        printTestXML(path);
      }
    }
  }
  
  private void checkRules(List<Rule> inrules, List<Rule> outrules) {
    for (Rule out : outrules) {
      Rule inrule = null;
      for (Rule in : inrules) {
        if (out.getRuleName().equals(in.getRuleName())) {
          inrule = in;
          break;
        }
      }
      assertNotNull(inrule);
      assertEquals(inrule.getRuleName(), inrule.toString(), out.toString());
    }
  }
  
  public void printTestXML(Path name) throws URISyntaxException, IOException {
    URI grammarReference = name.toUri();
    URL url = grammarReference.toURL();
    System.out.println(name);
    AbnfRuleGrammarParser s = new AbnfRuleGrammarParser(url.toString());
    List<Rule> inrules = s.load(url.openStream());
    StringBuffer sb = new StringBuffer();

    sb.append(XML_HEADER);
    for (Rule r : inrules) {
      sb.append('\n').append(r.toStringXML());
    }
    sb.append(XML_FOOTER);

    String xml = sb.toString();
    System.out.println(xml);
    
    InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    String fileName = name.getFileName().toString().replace("gram", "grxml");
    Path out = name.getParent().resolve(fileName);
    SrgsRuleGrammarParser p = new SrgsRuleGrammarParser();
    List<Rule> outrules = p.load(in);
    checkRules(inrules, outrules);
  }
  
  public void printTestABNF(Path name) throws URISyntaxException, IOException {
    URI grammarReference = name.toUri();
    URL url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    List<Rule> inrules = s.load(url.openStream());
    StringBuffer sb = new StringBuffer();
    
    sb.append(ABNF_HEADER);
    for (Rule r : inrules) {
      sb.append('\n').append(r.toStringABNF());
    }

    String abnf = sb.toString();
    //System.out.println(abnf);
    
    InputStream in = new ByteArrayInputStream(abnf.getBytes(StandardCharsets.UTF_8));
    String fileName = name.getFileName().toString().replace("grxml", "gram");
    Path out = name.getParent().resolve(fileName);
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(out.toUri().toString());
    List<Rule> outrules = p.load(in);
    checkRules(inrules, outrules);
  }
}
