/* -*- Mode: Java -*- */

%code imports {

import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.jvoicexml.processor.srgs.grammar.*;

@SuppressWarnings({"unused"})
}

%locations

%define package "org.jvoicexml.processor.srgs.abnf"

%define public

%define parser_class_name {SrgsAbnf}

%define parse.error verbose

%code {

  public class Grammar {
    public String base;
    public String mode;
    public String language;
    public String root;
    public String tag_format;
    public List<lexicon> lexis = new ArrayList<>();
    public List<meta> metas = new ArrayList<>();
    public List<Rule> rules = new ArrayList<>();

    void addRuleDef(String ref, String scope, RuleComponent body){
      rules.add(new Rule(ref, body,
                         scope.compareToIgnoreCase("public") == 0
                         ? Rule.PUBLIC : Rule.PRIVATE));
    }

    void addMeta(meta m) { metas.add(m); }

    //void parseHeader(String h) {}
  }

  Grammar gram = new Grammar();

  public Grammar getGrammar() { return gram; }

  class lexicon {
    public String uri;
    public String uri_media;

    public lexicon(String u) {
      this(u, null);
    }

    public lexicon(String u, String media) {
      uri = u;
      uri_media = media;
    }
  }

  class meta {
    public String s1, s2;
    boolean http_equiv;

    public meta(String s1, String s2, boolean http_equiv){
      this.s1 = s1;
      this.s2 = s2;
      this.http_equiv = http_equiv;
    }
  }

  public static class repeat {
    public int from, to;
    public double prob;

    public repeat(String repeat) {
      int index = repeat.indexOf("-");
      if (index < 0) {
        from = Integer.parseInt(repeat);
        to = from;
        return;
      }
      from = Integer.parseInt(repeat.substring(0, index));
      to = index + 1 == repeat.length()
        ? RuleCount.REPEAT_INDEFINITELY
        : Integer.parseInt(repeat.substring(index + 1));
    }

    public repeat(String repeat, String p) {
      this(repeat); prob = Double.parseDouble(p);
    }
  }

  RuleComponent setOptional(RuleComponent r) {
    return new RuleCount(r, 0, 1);
  }

  private RuleReference getRuleReference(String[] uri) {
    String uriStr = uri[0];
    int hashIndex = uriStr.indexOf("#");
    // if no #, reference to root rule of grammar
    final String ruleName =
      hashIndex >= 0 ? uriStr.substring(hashIndex + 1).trim() : null;
    final String grammarName =
      hashIndex >= 0 ? uriStr.substring(0, hashIndex) : uriStr;
    //if (grammarName.isEmpty)
    //  return new RuleReference(ruleName); // not legal in ABNF
    try {
      RuleReference res = new RuleReference(new URI(grammarName), ruleName);
      if (uri.length == 2)
        res.setMediaType(uri[2]);
      return res;
    }
    catch (URISyntaxException ex) {
      yyerror("Wrong URI: " + uri[0]);
    }
    return null;
  }
}

%language "Java"


%token ERR

 //%token <String> SelfIdentHeader

%token  DECL_LANG
%token  DECL_BASE
%token<String> DECL_ROOT
%token  DECL_TAG_FORMAT
%token<String> DECL_MODE
%token  DECL_LEXICON
%token  DECL_HTTP_EQUIV
%token  DECL_META
%token  DECL_IS

%token <String[]> URI
%token SelfIdentHeader
%token <String> Nmtoken
%token <String> SlashNum
%token <String> Repeat
%token <String> QuotedCharacters
%token <String> SimpleToken

%token Private
%token Public
%token <String> RuleName
%token <RuleComponent> specialRuleReference

%token TagStart
%token TagEnd
%token <String> Path
/* %token TagTokens */

%type <RuleComponent> subexpansion
%type <RuleComponent> sequence
%type <RuleComponent> sequenceElement
%type <repeat> repeat
%type <RuleComponent> ruleExpansion
%type <String> scope
%token <String> Tag
 //%type <fs> Conj
%%

grammar:    SelfIdentHeader // { gram.parseHeader($1); } // checked before
            declarations
            ruleDefinitions;

declarations: declaration
              | declarations declaration
;

declaration: baseDecl
             | languageDecl
             | modeDecl
             | rootRuleDecl
             | tagFormatDecl
             | lexiconDecl
             | metaDecl
;

baseDecl: DECL_BASE URI ';' { gram.base = $2[0];
/* Additional constraints:
   - A base declaration must not appear more than
   once in grammar.
*/
}
;

languageDecl: DECL_LANG Nmtoken ';' { gram.language = $2;
  /* Additional constraints:
     - A language declaration must not appear more than
     once in grammar.
     - A language declaration is required if the
     grammar mode is "voice".
  */ }
;

modeDecl: DECL_MODE ';' { gram.mode = $1 ; }
          /* Additional constraints:
             - A mode declaration must not appear more than
             once in grammar.
          */
;

rootRuleDecl: DECL_ROOT ';' { gram.root = $1;
  /*Additional constraints:
          - A root rule declaration must not appear more
            than once in grammar.
          - The root rule must be a rule that is defined
            within the grammar.
        */}
;

tagFormatDecl: DECL_TAG_FORMAT URI ';' { gram.tag_format = $2[0]; }
/*Additional constraints:
  - A tag-format declaration must not appear more
  than once in grammar.
*/
| DECL_TAG_FORMAT QuotedCharacters ';' { gram.tag_format = $2; }

;

lexiconDecl: DECL_LEXICON URI ';' { gram.lexis.add(new lexicon($2[0])); }
;

metaDecl: DECL_HTTP_EQUIV QuotedCharacters DECL_IS QuotedCharacters ';' {
  gram.addMeta(new meta($2, $4, true));
}
| DECL_META QuotedCharacters DECL_IS QuotedCharacters ';' {
  gram.addMeta(new meta($2, $4, false));
}
;

ruleDefinitions: %empty
                 | ruleDefinitions ruleDefinition
;

ruleDefinition: scope RuleName '=' ruleExpansion ';'
        { /* Additional constraints:
             - The rule name must be unique within a grammar,
               i.e. no rule must be defined more than once
               within a grammar.
          */
          gram.addRuleDef($2, $1, $4);
        }
;

scope:      %empty       { $$ = ""; }
            | Private { $$ = "private"; }
            | Public  { $$ = "public"; }
            ;

ruleExpansion: sequence {
  $$ = $1;
}
| SlashNum sequence {
  RuleAlternatives alt = new RuleAlternatives();
  alt.addAlternative($2, Integer.parseInt($1));
  $$ = alt;
}
| ruleExpansion '|' sequence {
  if ($1 instanceof RuleAlternatives) {
    ((RuleAlternatives)$1).addAlternative($3);
    $$ = $1;
  } else {
    RuleAlternatives alt = new RuleAlternatives();
    alt.addAlternative($1);
    alt.addAlternative($3);
    $$ = alt;
  }
}
| ruleExpansion '|' SlashNum sequence {
  if ($1 instanceof RuleAlternatives) {
    ((RuleAlternatives)$1).addAlternative($4, Integer.parseInt($3));
    $$ = $1;
  } else {
    RuleAlternatives alt = new RuleAlternatives();
    alt.addAlternative($1);
    alt.addAlternative($4, Integer.parseInt($3));
    $$ = alt;
  }
}
;

/*
ruleAlternative: sequence { $$ = $1; }   // sequence returns an itemseq object
| SlashNum sequence {
  $2.set_weight($1);
  $$ = $2;
}
;*/

sequence: sequenceElement { $$ = $1 ; }
| sequence sequenceElement {
  if ($1 instanceof RuleSequence) {
    ((RuleSequence)$1).addElement($2);
    $$ = $1;
  } else {
    RuleSequence seq = new RuleSequence();
    seq.addElement($1);
    seq.addElement($2);
    $$ = seq;
  }
}
;

sequenceElement: subexpansion { $$ = $1; } // subexpansion returns ruleexp obj
| subexpansion '<' repeat '>' {
  $$ = new RuleCount($1, $3.from, $3.to, $3.prob);
}
;

repeat: Repeat { $$ = new repeat($1); }
| Repeat SlashNum { $$ = new repeat($1, $2); }
;

/*
                 | subexpansion '+' {
                      $1.set_optional($2);
                      $$ = $1
                 }
                 | subexpansion '*' {
                      $1.set_kleene($2);
                      $$ = $1
                 }
*/


subexpansion: Nmtoken {
      RuleToken res = new RuleToken($1);
      $$ = res;
    }
    | Nmtoken '!' Nmtoken {
      RuleToken res = new RuleToken($1, $3);
      $$ = res;
    }
    | QuotedCharacters {  // should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken($1);
      $$ = res;
    }
    | QuotedCharacters '!' Nmtoken {// should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken($1, $3);
      $$ = res;
    }
    | RuleName {
      $$ = new RuleReference($1);
    }
    | RuleName '!' Nmtoken {
      RuleReference res = new RuleReference($1);
      res.setLanguage($3);
      $$ = res;
    }
    | URI {
      $$ = getRuleReference($1);
    }
    | URI '!' Nmtoken {
      RuleReference res = getRuleReference($1);
      res.setLanguage($3);
      $$ = res;
    }
    | specialRuleReference {
      $$ = $1;
    }
    | '(' ruleExpansion ')' {
      $$ = $2;
    }
    | '(' ruleExpansion ')' '!' Nmtoken {
      $2.setLanguage($5);
      $$ = $2;
    }
    | '[' ruleExpansion ']' {
      $$ = setOptional($2);
    }
    | '[' ruleExpansion ']' '!' Nmtoken {
      $2.setLanguage($5);
      $$ = setOptional($2);
    }
    | '(' ')' {
      $$ = RuleSpecial.NULL ;
    }
    // hier kommt unsere Spezialsyntax hin
    | TagStart Tag TagEnd { $$ = new RuleTag($2) ; }
    | TagStart TagEnd { $$ = new RuleTag("") ; }
;

/*
Tag: Path { $$ = new tagnode($1); }
     | Conj { $$ = new tagnode($1); }
     // first one extracts root name of this item
     | Path '=' Path {
       // this differs from the next alternative because it sets the "base"
       // member during creation of the new tagnode
       dag_node *end = (new dag_node());
       tagnode *res = new tagnode((new dag_node()).build_path($1,end));
       dag_node *two = (new dag_node()).build_path($3,end);
       $$ = res.unify(two);
       delete[] $1;
       delete[] $3;
       // delete two;
     }
     | Tag Path '=' Path {
       dag_node *one = (new dag_node()).build_path($2,NULL);
       dag_node *two = (new dag_node()).build_path($4,NULL);
       $$ = $1.unify(one.build_coref(two));
       delete[] $2;
       delete[] $4;
       // delete one;
       // delete two;
     }
     | Tag Conj {
       $$ = $1.unify($2);
       // delete $2;
     } // unify all conjunctions
;

Conj: Path '=' QuotedCharacters {
          $$ = (new dag_node()).build_path($1, new dag_node($3));
          delete[] $1;
          delete[] $3;
      }
      | Path '=' SimpleToken {
          $$ = (new dag_node()).build_path($1, new dag_node($3)) ;
          delete[] $1;
          delete[] $3;
      }
  /*
      | Path '=' TagTokens {
          $$ = (new dag_node()).build_path($1, new dag_node("%tokens")) ;
          delete[] $1;
      }
      | Path '(' ConjSeq ')'{
          $$ = (new dag_node()).build_path($1, $3) ;
          delete[] $1;
      }
;

ConjSeq: Conj { $$ = $1; }
         | ConjSeq Conj { $$ = $1.unify($2);
                          // delete $2;
         }
  */
;