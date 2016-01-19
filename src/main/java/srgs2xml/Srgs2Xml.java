package srgs2xml;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jvoicexml.processor.srgs.BacktrackingGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.GrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.RuleGraphContext;
import org.jvoicexml.processor.srgs.SrgsRuleGrammarParser;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleComponent;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;

public class Srgs2Xml {
  private static final String TYPE_DATE = "Date";

  /**
   * Look at the id's of a native array and try to determine whether it's
   * actually an Array or a Hashmap
   *
   * @param ids
   *          id's of the native array
   * @return boolean true if it's an array, false otherwise (ie it's a map)
   */
  private static boolean isArray(Object[] ids) {
    boolean result = true;
    for (Object id : ids) {
      if (id instanceof Integer == false) {
        result = false;
        break;
      }
    }
    return result;
  }

  /**
   * Convert an object from any script wrapper value to a valid repository
   * serializable value. This includes converting JavaScript Array objects to
   * Lists of valid objects.
   *
   * @param value
   *          Value to convert from script wrapper object to repo serializable
   *          value
   *
   * @return valid repo value
   *
  public Serializable convertValueForRepo(Serializable value) {
    Object converted = convertValueForJava((Object) value);
    return converted instanceof Serializable ? (Serializable) converted : value;
  }

  public static final Object convertValueForJava(Object value) {
    if (value == null) {
      return null;
    }

    else if (value instanceof ScriptNode) { // convert back to NodeRef
      value = ((ScriptNode)value).getNodeRef();
    } else if (value instanceof ChildAssociation) {
      value = ((ChildAssociation)value).getChildAssociationRef();
    } else if (value instanceof Association) {
      value = ((Association)value).getAssociationRef();
    }

    else if (value instanceof Wrapper) {
      // unwrap a Java object from a JavaScript wrapper
      // recursively call this method to convert the unwrapped value
      value = convertValueForJava(((Wrapper) value).unwrap());
    } else if (value instanceof Scriptable) {
      // a scriptable object will probably indicate a multi-value property
      // set using a JavaScript Array object
      Scriptable values = (Scriptable) value;

      if (value instanceof IdScriptableObject) {
        // TODO: add code here to use the dictionary and convert to correct
        // value type
        if (TYPE_DATE.equals(((IdScriptableObject) value).getClassName())) {
          value = Context.jsToJava(value, Date.class);
        } else if (value instanceof NativeArray) {
          // convert JavaScript array of values to a List of objects
          Object[] propIds = values.getIds();
          if (isArray(propIds) == true) {
            List<Object> propValues = new ArrayList<Object>(propIds.length);
            for (int i = 0; i < propIds.length; i++) {
              // work on each key in turn
              Object propId = propIds[i];

              // we are only interested in keys that indicate a list of values
              if (propId instanceof Integer) {
                // get the value out for the specified key
                Object val = values.get((Integer) propId, values);
                // recursively call this method to convert the value
                propValues.add(convertValueForJava(val));
              }
            }

            value = propValues;
          } else {
            Map<Object, Object> propValues = new HashMap<Object, Object>(propIds.length);
            for (Object propId : propIds) {
              // Get the value and add to the map
              Object val = values.get(propId.toString(), values);
              propValues.put(convertValueForJava(propId), convertValueForJava(val));
            }

            value = propValues;
          }
        } else {
          // convert Scriptable object of values to a Map of objects
          Object[] propIds = values.getIds();
          Map<String, Object> propValues = new HashMap<String, Object>(propIds.length);
          for (int i = 0; i < propIds.length; i++) {
            // work on each key in turn
            Object propId = propIds[i];

            // we are only interested in keys that indicate a list of values
            if (propId instanceof String) {
              // get the value out for the specified key
              Object val = values.get((String) propId, values);
              // recursively call this method to convert the value
              propValues.put((String) propId, convertValueForJava(val));
            }
          }
          value = propValues;
        }
      } else {
        // convert Scriptable object of values to a Map of objects
        Object[] propIds = values.getIds();
        Map<String, Object> propValues = new HashMap<String, Object>(propIds.length);
        for (int i = 0; i < propIds.length; i++) {
          // work on each key in turn
          Object propId = propIds[i];

          // we are only interested in keys that indicate a list of values
          if (propId instanceof String) {
            // get the value out for the specified key
            Object val = values.get((String) propId, values);
            // recursively call this method to convert the value
            propValues.put((String) propId, convertValueForJava(val));
          }
        }
        value = propValues;
      }
    } else if (value.getClass().isArray()) {
      // convert back a list of Java values
      int length = Array.getLength(value);
      ArrayList<Object> list = new ArrayList<Object>(length);
      for (int i = 0; i < length; i++) {
        list.add(convertValueForJava(Array.get(value, i)));
      }
      value = list;
    } else if (value instanceof CharSequence) {
      // Rhino has some interesting internal classes such as ConsString which
      // cannot be cast to String
      // but fortunately are instanceof CharSequence so we can toString() them.
      value = value.toString();
    }
    return value;
  }
*/

  public static void main(String[] args) throws Exception {
    BasicConfigurator.resetConfiguration();
    //Logger.getRootLogger().setLevel(Level.TRACE);
    //Logger.getLogger(GrammarChecker.class).setLevel(Level.TRACE);
    /*
    SrgsSisrGrammar parsedGrammar = Utils.loadDocument(args[0]);

    Object o = parsedGrammar.getSemanticInterpretation(" i want a small pizza with Ham please");

    Object obj = convertValueForJava(o);
    // Object o = mc.executeSisr();
    //System.out.println(o);
     */

    String[] inputs = {
        "small pizza",
        "medium pizza",
        "large pizza",
        "a small pizza",
        "a medium pizza",
        "a large pizza",
        "I want a small pizza",
        "I want a medium pizza",
        "I want a large pizza",
        "I want small pizza",
        "I want medium pizza",
        "I want large pizza",
        "I want a small pizza please",
        "I want a medium pizza please",
        "I want a large pizza please",
        "I want small pizza please",
        "I want medium pizza please",
        "I want large pizza please",
        "pizza with salami",
        "pizza with ham",
        "pizza with mushrooms",
        "a pizza with salami",
        "a pizza with ham",
        "a pizza with mushrooms",
        "I want a pizza with salami",
        "I want a pizza with ham",
        "I want a pizza with mushrooms",
        "I want pizza with salami",
        "I want pizza with ham",
        "I want pizza with mushrooms",
        "I want a pizza with salami please",
        "I want a pizza with ham please",
        "I want a pizza with mushrooms please",
        "I want pizza with salami please",
        "I want pizza with ham please",
        "I want pizza with mushrooms please",
        "small pizza with salami",
        "medium pizza with ham",
        "large pizza with mushrooms",
        "a small pizza with salami",
        "a medium pizza with ham",
        "a large pizza with mushrooms",
        "I want a small pizza with salami",
        "I want a medium pizza with ham",
        "I want a large pizza with mushrooms",
        "I want small pizza with salami",
        "I want medium pizza with ham",
        "I want large pizza with mushrooms",
        "I want a small pizza with salami please",
        "I want a medium pizza with ham please",
        "I want a large pizza with mushrooms please",
        "I want small pizza with salami please",
        "I want medium pizza with ham please",
        "I want large pizza with mushrooms please",
    };


    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final RuleGrammar ruleGrammar = (RuleGrammar) manager.loadGrammar( new
        File(args[0]).toURI());
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule = checker.parse(ruleGrammar, tokens);
      //Assert.assertTrue(validRule instanceof RuleToken);
      // final RuleToken token = (RuleToken) validRule;
      if (validRule == null) {
        for (String ss : tokens) {
          System.out.print(ss + " ");
        }
        System.out.println();
      } else {
        System.out.println(validRule);
      }
    }
    //Assert.assertEquals("this is a test", token.getText());

    /*
    SrgsRuleGrammarParser in = new SrgsRuleGrammarParser();
    Rule[] rules = in.load(new FileReader(args[0]));
    for (Rule rule : rules) {
     System.out.println(rule);
    }
    */

  }

}
