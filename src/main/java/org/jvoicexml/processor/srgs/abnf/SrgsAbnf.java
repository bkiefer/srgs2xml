/* A Bison parser, made by GNU Bison 3.7.6.  */

/* Skeleton implementation for Bison LALR(1) parsers in Java

   Copyright (C) 2007-2015, 2018-2021 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

package org.jvoicexml.processor.srgs.abnf;



import java.text.MessageFormat;
/* "%code imports" blocks.  */
/* "SrgsAbnf.y":3  */


import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.jvoicexml.processor.srgs.grammar.*;

@SuppressWarnings({"unused", "unchecked"})

/* "SrgsAbnf.java":56  */

/**
 * A Bison parser, automatically generated from <tt>SrgsAbnf.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
public class SrgsAbnf
{
  /** Version number for the Bison executable that generated this parser.  */
  public static final String bisonVersion = "3.7.6";

  /** Name of the skeleton that generated this parser.  */
  public static final String bisonSkeleton = "lalr1.java";



  /**
   * True if verbose error messages are enabled.
   */
  private boolean yyErrorVerbose = true;

  /**
   * Whether verbose error messages are enabled.
   */
  public final boolean getErrorVerbose() { return yyErrorVerbose; }

  /**
   * Set the verbosity of error messages.
   * @param verbose True to request verbose error messages.
   */
  public final void setErrorVerbose(boolean verbose)
  { yyErrorVerbose = verbose; }



  /**
   * A class defining a pair of positions.  Positions, defined by the
   * <code>Position</code> class, denote a point in the input.
   * Locations represent a part of the input through the beginning
   * and ending positions.
   */
  public static class Location {
    /**
     * The first, inclusive, position in the range.
     */
    public Position begin;

    /**
     * The first position beyond the range.
     */
    public Position end;

    /**
     * Create a <code>Location</code> denoting an empty range located at
     * a given point.
     * @param loc The position at which the range is anchored.
     */
    public Location (Position loc) {
      this.begin = this.end = loc;
    }

    /**
     * Create a <code>Location</code> from the endpoints of the range.
     * @param begin The first position included in the range.
     * @param end   The first position beyond the range.
     */
    public Location (Position begin, Position end) {
      this.begin = begin;
      this.end = end;
    }

    /**
     * Print a representation of the location.  For this to be correct,
     * <code>Position</code> should override the <code>equals</code>
     * method.
     */
    public String toString () {
      if (begin.equals (end))
        return begin.toString ();
      else
        return begin.toString () + "-" + end.toString ();
    }
  }

  private Location yylloc(YYStack rhs, int n)
  {
    if (0 < n)
      return new Location(rhs.locationAt(n-1).begin, rhs.locationAt(0).end);
    else
      return new Location(rhs.locationAt(0).end);
  }

  public enum SymbolKind
  {
    S_YYEOF(0),                    /* "end of file"  */
    S_YYerror(1),                  /* error  */
    S_YYUNDEF(2),                  /* "invalid token"  */
    S_ERR(3),                      /* ERR  */
    S_DECL_LANG(4),                /* DECL_LANG  */
    S_DECL_BASE(5),                /* DECL_BASE  */
    S_DECL_ROOT(6),                /* DECL_ROOT  */
    S_DECL_TAG_FORMAT(7),          /* DECL_TAG_FORMAT  */
    S_DECL_MODE(8),                /* DECL_MODE  */
    S_DECL_LEXICON(9),             /* DECL_LEXICON  */
    S_DECL_HTTP_EQUIV(10),         /* DECL_HTTP_EQUIV  */
    S_DECL_META(11),               /* DECL_META  */
    S_DECL_IS(12),                 /* DECL_IS  */
    S_IllegalCharacter(13),        /* IllegalCharacter  */
    S_URI(14),                     /* URI  */
    S_SelfIdentHeader(15),         /* SelfIdentHeader  */
    S_Nmtoken(16),                 /* Nmtoken  */
    S_SlashNum(17),                /* SlashNum  */
    S_Repeat(18),                  /* Repeat  */
    S_QuotedCharacters(19),        /* QuotedCharacters  */
    S_SimpleToken(20),             /* SimpleToken  */
    S_Private(21),                 /* Private  */
    S_Public(22),                  /* Public  */
    S_RuleName(23),                /* RuleName  */
    S_specialRuleReference(24),    /* specialRuleReference  */
    S_TagStart(25),                /* TagStart  */
    S_TagEnd(26),                  /* TagEnd  */
    S_Path(27),                    /* Path  */
    S_Tag(28),                     /* Tag  */
    S_29_(29),                     /* ';'  */
    S_30_(30),                     /* '='  */
    S_31_(31),                     /* '|'  */
    S_32_(32),                     /* '<'  */
    S_33_(33),                     /* '>'  */
    S_34_(34),                     /* '!'  */
    S_35_(35),                     /* '('  */
    S_36_(36),                     /* ')'  */
    S_37_(37),                     /* '['  */
    S_38_(38),                     /* ']'  */
    S_YYACCEPT(39),                /* $accept  */
    S_grammar(40),                 /* grammar  */
    S_declarations(41),            /* declarations  */
    S_declaration(42),             /* declaration  */
    S_baseDecl(43),                /* baseDecl  */
    S_languageDecl(44),            /* languageDecl  */
    S_modeDecl(45),                /* modeDecl  */
    S_rootRuleDecl(46),            /* rootRuleDecl  */
    S_tagFormatDecl(47),           /* tagFormatDecl  */
    S_lexiconDecl(48),             /* lexiconDecl  */
    S_metaDecl(49),                /* metaDecl  */
    S_ruleDefinitions(50),         /* ruleDefinitions  */
    S_ruleDefinition(51),          /* ruleDefinition  */
    S_scope(52),                   /* scope  */
    S_ruleExpansion(53),           /* ruleExpansion  */
    S_sequence(54),                /* sequence  */
    S_sequenceElement(55),         /* sequenceElement  */
    S_repeat(56),                  /* repeat  */
    S_subexpansion(57);            /* subexpansion  */


    private final int yycode_;

    SymbolKind (int n) {
      this.yycode_ = n;
    }

    private static final SymbolKind[] values_ = {
      SymbolKind.S_YYEOF,
      SymbolKind.S_YYerror,
      SymbolKind.S_YYUNDEF,
      SymbolKind.S_ERR,
      SymbolKind.S_DECL_LANG,
      SymbolKind.S_DECL_BASE,
      SymbolKind.S_DECL_ROOT,
      SymbolKind.S_DECL_TAG_FORMAT,
      SymbolKind.S_DECL_MODE,
      SymbolKind.S_DECL_LEXICON,
      SymbolKind.S_DECL_HTTP_EQUIV,
      SymbolKind.S_DECL_META,
      SymbolKind.S_DECL_IS,
      SymbolKind.S_IllegalCharacter,
      SymbolKind.S_URI,
      SymbolKind.S_SelfIdentHeader,
      SymbolKind.S_Nmtoken,
      SymbolKind.S_SlashNum,
      SymbolKind.S_Repeat,
      SymbolKind.S_QuotedCharacters,
      SymbolKind.S_SimpleToken,
      SymbolKind.S_Private,
      SymbolKind.S_Public,
      SymbolKind.S_RuleName,
      SymbolKind.S_specialRuleReference,
      SymbolKind.S_TagStart,
      SymbolKind.S_TagEnd,
      SymbolKind.S_Path,
      SymbolKind.S_Tag,
      SymbolKind.S_29_,
      SymbolKind.S_30_,
      SymbolKind.S_31_,
      SymbolKind.S_32_,
      SymbolKind.S_33_,
      SymbolKind.S_34_,
      SymbolKind.S_35_,
      SymbolKind.S_36_,
      SymbolKind.S_37_,
      SymbolKind.S_38_,
      SymbolKind.S_YYACCEPT,
      SymbolKind.S_grammar,
      SymbolKind.S_declarations,
      SymbolKind.S_declaration,
      SymbolKind.S_baseDecl,
      SymbolKind.S_languageDecl,
      SymbolKind.S_modeDecl,
      SymbolKind.S_rootRuleDecl,
      SymbolKind.S_tagFormatDecl,
      SymbolKind.S_lexiconDecl,
      SymbolKind.S_metaDecl,
      SymbolKind.S_ruleDefinitions,
      SymbolKind.S_ruleDefinition,
      SymbolKind.S_scope,
      SymbolKind.S_ruleExpansion,
      SymbolKind.S_sequence,
      SymbolKind.S_sequenceElement,
      SymbolKind.S_repeat,
      SymbolKind.S_subexpansion
    };

    static final SymbolKind get(int code) {
      return values_[code];
    }

    public final int getCode() {
      return this.yycode_;
    }

    /* Return YYSTR after stripping away unnecessary quotes and
       backslashes, so that it's suitable for yyerror.  The heuristic is
       that double-quoting is unnecessary unless the string contains an
       apostrophe, a comma, or backslash (other than backslash-backslash).
       YYSTR is taken from yytname.  */
    private static String yytnamerr_(String yystr)
    {
      if (yystr.charAt (0) == '"')
        {
          StringBuffer yyr = new StringBuffer();
          strip_quotes: for (int i = 1; i < yystr.length(); i++)
            switch (yystr.charAt(i))
              {
              case '\'':
              case ',':
                break strip_quotes;

              case '\\':
                if (yystr.charAt(++i) != '\\')
                  break strip_quotes;
                /* Fall through.  */
              default:
                yyr.append(yystr.charAt(i));
                break;

              case '"':
                return yyr.toString();
              }
        }
      return yystr;
    }

    /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
       First, the terminals, then, starting at \a YYNTOKENS_, nonterminals.  */
    private static final String[] yytname_ = yytname_init();
  private static final String[] yytname_init()
  {
    return new String[]
    {
  "\"end of file\"", "error", "\"invalid token\"", "ERR", "DECL_LANG",
  "DECL_BASE", "DECL_ROOT", "DECL_TAG_FORMAT", "DECL_MODE", "DECL_LEXICON",
  "DECL_HTTP_EQUIV", "DECL_META", "DECL_IS", "IllegalCharacter", "URI",
  "SelfIdentHeader", "Nmtoken", "SlashNum", "Repeat", "QuotedCharacters",
  "SimpleToken", "Private", "Public", "RuleName", "specialRuleReference",
  "TagStart", "TagEnd", "Path", "Tag", "';'", "'='", "'|'", "'<'", "'>'",
  "'!'", "'('", "')'", "'['", "']'", "$accept", "grammar", "declarations",
  "declaration", "baseDecl", "languageDecl", "modeDecl", "rootRuleDecl",
  "tagFormatDecl", "lexiconDecl", "metaDecl", "ruleDefinitions",
  "ruleDefinition", "scope", "ruleExpansion", "sequence",
  "sequenceElement", "repeat", "subexpansion", null
    };
  }

    /* The user-facing name of this symbol.  */
    public final String getName() {
      return yytnamerr_(yytname_[yycode_]);
    }

  };


  /**
   * Communication interface between the scanner and the Bison-generated
   * parser <tt>SrgsAbnf</tt>.
   */
  public interface Lexer {
    /* Token kinds.  */
    /** Token "end of file", to be returned by the scanner.  */
    static final int YYEOF = 0;
    /** Token error, to be returned by the scanner.  */
    static final int YYerror = 256;
    /** Token "invalid token", to be returned by the scanner.  */
    static final int YYUNDEF = 257;
    /** Token ERR, to be returned by the scanner.  */
    static final int ERR = 258;
    /** Token DECL_LANG, to be returned by the scanner.  */
    static final int DECL_LANG = 259;
    /** Token DECL_BASE, to be returned by the scanner.  */
    static final int DECL_BASE = 260;
    /** Token DECL_ROOT, to be returned by the scanner.  */
    static final int DECL_ROOT = 261;
    /** Token DECL_TAG_FORMAT, to be returned by the scanner.  */
    static final int DECL_TAG_FORMAT = 262;
    /** Token DECL_MODE, to be returned by the scanner.  */
    static final int DECL_MODE = 263;
    /** Token DECL_LEXICON, to be returned by the scanner.  */
    static final int DECL_LEXICON = 264;
    /** Token DECL_HTTP_EQUIV, to be returned by the scanner.  */
    static final int DECL_HTTP_EQUIV = 265;
    /** Token DECL_META, to be returned by the scanner.  */
    static final int DECL_META = 266;
    /** Token DECL_IS, to be returned by the scanner.  */
    static final int DECL_IS = 267;
    /** Token IllegalCharacter, to be returned by the scanner.  */
    static final int IllegalCharacter = 268;
    /** Token URI, to be returned by the scanner.  */
    static final int URI = 269;
    /** Token SelfIdentHeader, to be returned by the scanner.  */
    static final int SelfIdentHeader = 270;
    /** Token Nmtoken, to be returned by the scanner.  */
    static final int Nmtoken = 271;
    /** Token SlashNum, to be returned by the scanner.  */
    static final int SlashNum = 272;
    /** Token Repeat, to be returned by the scanner.  */
    static final int Repeat = 273;
    /** Token QuotedCharacters, to be returned by the scanner.  */
    static final int QuotedCharacters = 274;
    /** Token SimpleToken, to be returned by the scanner.  */
    static final int SimpleToken = 275;
    /** Token Private, to be returned by the scanner.  */
    static final int Private = 276;
    /** Token Public, to be returned by the scanner.  */
    static final int Public = 277;
    /** Token RuleName, to be returned by the scanner.  */
    static final int RuleName = 278;
    /** Token specialRuleReference, to be returned by the scanner.  */
    static final int specialRuleReference = 279;
    /** Token TagStart, to be returned by the scanner.  */
    static final int TagStart = 280;
    /** Token TagEnd, to be returned by the scanner.  */
    static final int TagEnd = 281;
    /** Token Path, to be returned by the scanner.  */
    static final int Path = 282;
    /** Token Tag, to be returned by the scanner.  */
    static final int Tag = 283;

    /** Deprecated, use YYEOF instead.  */
    public static final int EOF = YYEOF;

    /**
     * Method to retrieve the beginning position of the last scanned token.
     * @return the position at which the last scanned token starts.
     */
    Position getStartPos();

    /**
     * Method to retrieve the ending position of the last scanned token.
     * @return the first position beyond the last scanned token.
     */
    Position getEndPos();

    /**
     * Method to retrieve the semantic value of the last scanned token.
     * @return the semantic value of the last scanned token.
     */
    Object getLVal();

    /**
     * Entry point for the scanner.  Returns the token identifier corresponding
     * to the next token and prepares to return the semantic value
     * and beginning/ending positions of the token.
     * @return the token identifier corresponding to the next token.
     */
    int yylex() throws java.io.IOException;

    /**
     * Emit an error referring to the given locationin a user-defined way.
     *
     * @param loc The location of the element to which the
     *                error message is related.
     * @param msg The string for the error message.
     */
     void yyerror(Location loc, String msg);


  }


  /**
   * The object doing lexical analysis for us.
   */
  private Lexer yylexer;





  /**
   * Instantiates the Bison-generated parser.
   * @param yylexer The scanner that will supply tokens to the parser.
   */
  public SrgsAbnf (Lexer yylexer)
  {

    this.yylexer = yylexer;

  }


  private java.io.PrintStream yyDebugStream = System.err;

  /**
   * The <tt>PrintStream</tt> on which the debugging output is printed.
   */
  public final java.io.PrintStream getDebugStream () { return yyDebugStream; }

  /**
   * Set the <tt>PrintStream</tt> on which the debug output is printed.
   * @param s The stream that is used for debugging output.
   */
  public final void setDebugStream (java.io.PrintStream s) { yyDebugStream = s; }

  private int yydebug = 0;

  /**
   * Answer the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   */
  public final int getDebugLevel () { return yydebug; }

  /**
   * Set the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   * @param level The verbosity level for debugging output.
   */
  public final void setDebugLevel (int level) { yydebug = level; }


  private int yynerrs = 0;

  /**
   * The number of syntax errors so far.
   */
  public final int getNumberOfErrors () { return yynerrs; }

  /**
   * Print an error message via the lexer.
   * Use a <code>null</code> location.
   * @param msg The error message.
   */
  public final void yyerror(String msg) {
      yylexer.yyerror((Location)null, msg);
  }

  /**
   * Print an error message via the lexer.
   * @param loc The location associated with the message.
   * @param msg The error message.
   */
  public final void yyerror(Location loc, String msg) {
      yylexer.yyerror(loc, msg);
  }

  /**
   * Print an error message via the lexer.
   * @param pos The position associated with the message.
   * @param msg The error message.
   */
  public final void yyerror(Position pos, String msg) {
      yylexer.yyerror(new Location (pos), msg);
  }

  protected final void yycdebug (String s) {
    if (0 < yydebug)
      yyDebugStream.println (s);
  }

  private final class YYStack {
    private int[] stateStack = new int[16];
    private Location[] locStack = new Location[16];
    private Object[] valueStack = new Object[16];

    public int size = 16;
    public int height = -1;

    public final void push (int state, Object value, Location loc) {
      height++;
      if (size == height)
        {
          int[] newStateStack = new int[size * 2];
          System.arraycopy (stateStack, 0, newStateStack, 0, height);
          stateStack = newStateStack;
          Location[] newLocStack = new Location[size * 2];
          System.arraycopy (locStack, 0, newLocStack, 0, height);
          locStack = newLocStack;

          Object[] newValueStack = new Object[size * 2];
          System.arraycopy (valueStack, 0, newValueStack, 0, height);
          valueStack = newValueStack;

          size *= 2;
        }

      stateStack[height] = state;
      locStack[height] = loc;
      valueStack[height] = value;
    }

    public final void pop () {
      pop (1);
    }

    public final void pop (int num) {
      // Avoid memory leaks... garbage collection is a white lie!
      if (0 < num) {
        java.util.Arrays.fill (valueStack, height - num + 1, height + 1, null);
        java.util.Arrays.fill (locStack, height - num + 1, height + 1, null);
      }
      height -= num;
    }

    public final int stateAt (int i) {
      return stateStack[height - i];
    }


    public final Location locationAt (int i) {
      return locStack[height - i];
    }

    public final Object valueAt (int i) {
      return valueStack[height - i];
    }

    // Print the state stack on the debug stream.
    public void print (java.io.PrintStream out) {
      out.print ("Stack now");

      for (int i = 0; i <= height; i++)
        {
          out.print (' ');
          out.print (stateStack[i]);
        }
      out.println ();
    }
  }

  /**
   * Returned by a Bison action in order to stop the parsing process and
   * return success (<tt>true</tt>).
   */
  public static final int YYACCEPT = 0;

  /**
   * Returned by a Bison action in order to stop the parsing process and
   * return failure (<tt>false</tt>).
   */
  public static final int YYABORT = 1;



  /**
   * Returned by a Bison action in order to start error recovery without
   * printing an error message.
   */
  public static final int YYERROR = 2;

  /**
   * Internal return codes that are not supported for user semantic
   * actions.
   */
  private static final int YYERRLAB = 3;
  private static final int YYNEWSTATE = 4;
  private static final int YYDEFAULT = 5;
  private static final int YYREDUCE = 6;
  private static final int YYERRLAB1 = 7;
  private static final int YYRETURN = 8;


  private int yyerrstatus_ = 0;


  /**
   * Whether error recovery is being done.  In this state, the parser
   * reads token until it reaches a known state, and then restarts normal
   * operation.
   */
  public final boolean recovering ()
  {
    return yyerrstatus_ == 0;
  }

  /** Compute post-reduction state.
   * @param yystate   the current state
   * @param yysym     the nonterminal to push on the stack
   */
  private int yyLRGotoState (int yystate, int yysym)
  {
    int yyr = yypgoto_[yysym - YYNTOKENS_] + yystate;
    if (0 <= yyr && yyr <= YYLAST_ && yycheck_[yyr] == yystate)
      return yytable_[yyr];
    else
      return yydefgoto_[yysym - YYNTOKENS_];
  }

  private int yyaction(int yyn, YYStack yystack, int yylen)
  {
    /* If YYLEN is nonzero, implement the default value of the action:
       '$$ = $1'.  Otherwise, use the top of the stack.

       Otherwise, the following line sets YYVAL to garbage.
       This behavior is undocumented and Bison
       users should not rely upon it.  */
    Object yyval = (0 < yylen) ? yystack.valueAt(yylen - 1) : yystack.valueAt(0);
    Location yyloc = yylloc(yystack, yylen);

    yyReducePrint(yyn, yystack);

    switch (yyn)
      {
          case 12: /* baseDecl: DECL_BASE URI ';'  */
  if (yyn == 12)
    /* "SrgsAbnf.y":186  */
                            { attributes.put("base", ((String[])(yystack.valueAt (1)))[0]);
/* Additional constraints:
   - A base declaration must not appear more than
   once in grammar.
*/
};
  break;


  case 13: /* languageDecl: DECL_LANG Nmtoken ';'  */
  if (yyn == 13)
    /* "SrgsAbnf.y":194  */
                                    { attributes.put("language", ((String)(yystack.valueAt (1))));
  /* Additional constraints:
     - A language declaration must not appear more than
     once in grammar.
     - A language declaration is required if the
     grammar mode is "voice".
  */ };
  break;


  case 14: /* modeDecl: DECL_MODE ';'  */
  if (yyn == 14)
    /* "SrgsAbnf.y":203  */
                        { attributes.put("mode", ((String)(yystack.valueAt (1)))); };
  break;


  case 15: /* rootRuleDecl: DECL_ROOT ';'  */
  if (yyn == 15)
    /* "SrgsAbnf.y":210  */
                            { attributes.put("root", ((String)(yystack.valueAt (1))));
  /*Additional constraints:
          - A root rule declaration must not appear more
            than once in grammar.
          - The root rule must be a rule that is defined
            within the grammar.
        */};
  break;


  case 16: /* tagFormatDecl: DECL_TAG_FORMAT URI ';'  */
  if (yyn == 16)
    /* "SrgsAbnf.y":219  */
                                       { attributes.put("tag_format", ((String[])(yystack.valueAt (1)))[0]); };
  break;


  case 17: /* tagFormatDecl: DECL_TAG_FORMAT QuotedCharacters ';'  */
  if (yyn == 17)
    /* "SrgsAbnf.y":224  */
                                       { attributes.put("tag_format", ((String)(yystack.valueAt (1)))); };
  break;


  case 18: /* lexiconDecl: DECL_LEXICON URI ';'  */
  if (yyn == 18)
    /* "SrgsAbnf.y":228  */
                                  { addLexicon(new lexicon(((String[])(yystack.valueAt (1)))[0])); };
  break;


  case 19: /* metaDecl: DECL_HTTP_EQUIV QuotedCharacters DECL_IS QuotedCharacters ';'  */
  if (yyn == 19)
    /* "SrgsAbnf.y":231  */
                                                                        {
  addMeta(new Meta(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (1))), true));
};
  break;


  case 20: /* metaDecl: DECL_META QuotedCharacters DECL_IS QuotedCharacters ';'  */
  if (yyn == 20)
    /* "SrgsAbnf.y":234  */
                                                          {
  addMeta(new Meta(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (1))), false));
};
  break;


  case 23: /* ruleDefinition: scope RuleName '=' ruleExpansion ';'  */
  if (yyn == 23)
    /* "SrgsAbnf.y":244  */
        { /* Additional constraints:
             - The rule name must be unique within a grammar,
               i.e. no rule must be defined more than once
               within a grammar.
          */
          addRuleDef(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (4))), ((RuleComponent)(yystack.valueAt (1))));
        };
  break;


  case 24: /* scope: %empty  */
  if (yyn == 24)
    /* "SrgsAbnf.y":253  */
                         { yyval = ""; };
  break;


  case 25: /* scope: Private  */
  if (yyn == 25)
    /* "SrgsAbnf.y":254  */
                      { yyval = "private"; };
  break;


  case 26: /* scope: Public  */
  if (yyn == 26)
    /* "SrgsAbnf.y":255  */
                      { yyval = "public"; };
  break;


  case 27: /* ruleExpansion: sequence  */
  if (yyn == 27)
    /* "SrgsAbnf.y":258  */
                        {
  yyval = ((RuleComponent)(yystack.valueAt (0)));
};
  break;


  case 28: /* ruleExpansion: SlashNum sequence  */
  if (yyn == 28)
    /* "SrgsAbnf.y":261  */
                    {
  RuleAlternatives alt = new RuleAlternatives();
  alt.addAlternative(((RuleComponent)(yystack.valueAt (0))), Double.parseDouble(((String)(yystack.valueAt (1)))));
  yyval = alt;
};
  break;


  case 29: /* ruleExpansion: ruleExpansion '|' sequence  */
  if (yyn == 29)
    /* "SrgsAbnf.y":266  */
                             {
  if (((RuleComponent)(yystack.valueAt (2))) instanceof RuleAlternatives) {
    ((RuleAlternatives)((RuleComponent)(yystack.valueAt (2)))).addAlternative(((RuleComponent)(yystack.valueAt (0))));
    yyval = ((RuleComponent)(yystack.valueAt (2)));
  } else {
    RuleAlternatives alt = new RuleAlternatives();
    alt.addAlternative(((RuleComponent)(yystack.valueAt (2))));
    alt.addAlternative(((RuleComponent)(yystack.valueAt (0))));
    yyval = alt;
  }
};
  break;


  case 30: /* ruleExpansion: ruleExpansion '|' SlashNum sequence  */
  if (yyn == 30)
    /* "SrgsAbnf.y":277  */
                                      {
  if (((RuleComponent)(yystack.valueAt (3))) instanceof RuleAlternatives) {
    ((RuleAlternatives)((RuleComponent)(yystack.valueAt (3)))).addAlternative(((RuleComponent)(yystack.valueAt (0))), Double.parseDouble(((String)(yystack.valueAt (1)))));
    yyval = ((RuleComponent)(yystack.valueAt (3)));
  } else {
    RuleAlternatives alt = new RuleAlternatives();
    alt.addAlternative(((RuleComponent)(yystack.valueAt (3))));
    alt.addAlternative(((RuleComponent)(yystack.valueAt (0))), Double.parseDouble(((String)(yystack.valueAt (1)))));
    yyval = alt;
  }
};
  break;


  case 31: /* sequence: sequenceElement  */
  if (yyn == 31)
    /* "SrgsAbnf.y":298  */
                          { yyval = ((RuleComponent)(yystack.valueAt (0))) ; };
  break;


  case 32: /* sequence: sequence sequenceElement  */
  if (yyn == 32)
    /* "SrgsAbnf.y":299  */
                           {
  if (((RuleComponent)(yystack.valueAt (1))) instanceof RuleSequence) {
    ((RuleSequence)((RuleComponent)(yystack.valueAt (1)))).addElement(((RuleComponent)(yystack.valueAt (0))));
    yyval = ((RuleComponent)(yystack.valueAt (1)));
  } else {
    RuleSequence seq = new RuleSequence();
    seq.addElement(((RuleComponent)(yystack.valueAt (1))));
    seq.addElement(((RuleComponent)(yystack.valueAt (0))));
    yyval = seq;
  }
};
  break;


  case 33: /* sequenceElement: subexpansion  */
  if (yyn == 33)
    /* "SrgsAbnf.y":312  */
                              { yyval = ((RuleComponent)(yystack.valueAt (0))); };
  break;


  case 34: /* sequenceElement: subexpansion '<' repeat '>'  */
  if (yyn == 34)
    /* "SrgsAbnf.y":313  */
                              {
  yyval = new RuleCount(((RuleComponent)(yystack.valueAt (3))), ((repeat)(yystack.valueAt (1))).from, ((repeat)(yystack.valueAt (1))).to, ((repeat)(yystack.valueAt (1))).prob);
};
  break;


  case 35: /* repeat: Repeat  */
  if (yyn == 35)
    /* "SrgsAbnf.y":318  */
               { yyval = new repeat(((String)(yystack.valueAt (0)))); };
  break;


  case 36: /* repeat: Repeat SlashNum  */
  if (yyn == 36)
    /* "SrgsAbnf.y":319  */
                  { yyval = new repeat(((String)(yystack.valueAt (1))), ((String)(yystack.valueAt (0)))); };
  break;


  case 37: /* subexpansion: Nmtoken  */
  if (yyn == 37)
    /* "SrgsAbnf.y":334  */
                      {
      RuleToken res = new RuleToken(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 38: /* subexpansion: Nmtoken '!' Nmtoken  */
  if (yyn == 38)
    /* "SrgsAbnf.y":338  */
                          {
      RuleToken res = new RuleToken(((String)(yystack.valueAt (2))), ((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 39: /* subexpansion: QuotedCharacters  */
  if (yyn == 39)
    /* "SrgsAbnf.y":342  */
                       {  // should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 40: /* subexpansion: QuotedCharacters '!' Nmtoken  */
  if (yyn == 40)
    /* "SrgsAbnf.y":346  */
                                   {// should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken(((String)(yystack.valueAt (2))), ((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 41: /* subexpansion: RuleName  */
  if (yyn == 41)
    /* "SrgsAbnf.y":350  */
               {
      yyval = new RuleReference(((String)(yystack.valueAt (0))));
    };
  break;


  case 42: /* subexpansion: RuleName '!' Nmtoken  */
  if (yyn == 42)
    /* "SrgsAbnf.y":353  */
                           {
      RuleReference res = new RuleReference(((String)(yystack.valueAt (2))));
      res.setLanguage(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 43: /* subexpansion: URI  */
  if (yyn == 43)
    /* "SrgsAbnf.y":358  */
          {
      yyval = getRuleReference(((String[])(yystack.valueAt (0))));
    };
  break;


  case 44: /* subexpansion: URI '!' Nmtoken  */
  if (yyn == 44)
    /* "SrgsAbnf.y":361  */
                      {
      RuleReference res = getRuleReference(((String[])(yystack.valueAt (2))));
      res.setLanguage(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;


  case 45: /* subexpansion: specialRuleReference  */
  if (yyn == 45)
    /* "SrgsAbnf.y":366  */
                           {
      yyval = ((RuleComponent)(yystack.valueAt (0)));
    };
  break;


  case 46: /* subexpansion: '(' ruleExpansion ')'  */
  if (yyn == 46)
    /* "SrgsAbnf.y":369  */
                            {
      yyval = ((RuleComponent)(yystack.valueAt (1)));
    };
  break;


  case 47: /* subexpansion: '(' ruleExpansion ')' '!' Nmtoken  */
  if (yyn == 47)
    /* "SrgsAbnf.y":372  */
                                        {
      ((RuleComponent)(yystack.valueAt (3))).setLanguage(((String)(yystack.valueAt (0))));
      yyval = ((RuleComponent)(yystack.valueAt (3)));
    };
  break;


  case 48: /* subexpansion: '[' ruleExpansion ']'  */
  if (yyn == 48)
    /* "SrgsAbnf.y":376  */
                            {
      yyval = setOptional(((RuleComponent)(yystack.valueAt (1))));
    };
  break;


  case 49: /* subexpansion: '[' ruleExpansion ']' '!' Nmtoken  */
  if (yyn == 49)
    /* "SrgsAbnf.y":379  */
                                        {
      ((RuleComponent)(yystack.valueAt (3))).setLanguage(((String)(yystack.valueAt (0))));
      yyval = setOptional(((RuleComponent)(yystack.valueAt (3))));
    };
  break;


  case 50: /* subexpansion: '(' ')'  */
  if (yyn == 50)
    /* "SrgsAbnf.y":383  */
              {
      yyval = RuleSpecial.NULL ;
    };
  break;


  case 51: /* subexpansion: TagStart Tag TagEnd  */
  if (yyn == 51)
    /* "SrgsAbnf.y":387  */
                          { yyval = new RuleTag(((String)(yystack.valueAt (1)))) ; };
  break;


  case 52: /* subexpansion: TagStart TagEnd  */
  if (yyn == 52)
    /* "SrgsAbnf.y":388  */
                      { yyval = new RuleTag("") ; };
  break;



/* "SrgsAbnf.java":1063  */

        default: break;
      }

    yySymbolPrint("-> $$ =", SymbolKind.get(yyr1_[yyn]), yyval, yyloc);

    yystack.pop(yylen);
    yylen = 0;
    /* Shift the result of the reduction.  */
    int yystate = yyLRGotoState(yystack.stateAt(0), yyr1_[yyn]);
    yystack.push(yystate, yyval, yyloc);
    return YYNEWSTATE;
  }


  /*--------------------------------.
  | Print this symbol on YYOUTPUT.  |
  `--------------------------------*/

  private void yySymbolPrint(String s, SymbolKind yykind,
                             Object yyvalue, Location yylocation) {
      if (0 < yydebug) {
          yycdebug(s
                   + (yykind.getCode() < YYNTOKENS_ ? " token " : " nterm ")
                   + yykind.getName() + " ("
                   + yylocation + ": "
                   + (yyvalue == null ? "(null)" : yyvalue.toString()) + ")");
      }
  }


  /**
   * Parse input from the scanner that was specified at object construction
   * time.  Return whether the end of the input was reached successfully.
   *
   * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
   *          imply that there were no syntax errors.
   */
  public boolean parse() throws java.io.IOException

  {
    /* @$.  */
    Location yyloc;


    /* Lookahead token kind.  */
    int yychar = YYEMPTY_;
    /* Lookahead symbol kind.  */
    SymbolKind yytoken = null;

    /* State.  */
    int yyn = 0;
    int yylen = 0;
    int yystate = 0;
    YYStack yystack = new YYStack ();
    int label = YYNEWSTATE;


    /* The location where the error started.  */
    Location yyerrloc = null;

    /* Location. */
    Location yylloc = new Location (null, null);

    /* Semantic value of the lookahead.  */
    Object yylval = null;

    yycdebug ("Starting parse");
    yyerrstatus_ = 0;
    yynerrs = 0;

    /* Initialize the stack.  */
    yystack.push (yystate, yylval, yylloc);



    for (;;)
      switch (label)
      {
        /* New state.  Unlike in the C/C++ skeletons, the state is already
           pushed when we come here.  */
      case YYNEWSTATE:
        yycdebug ("Entering state " + yystate);
        if (0 < yydebug)
          yystack.print (yyDebugStream);

        /* Accept?  */
        if (yystate == YYFINAL_)
          return true;

        /* Take a decision.  First try without lookahead.  */
        yyn = yypact_[yystate];
        if (yyPactValueIsDefault (yyn))
          {
            label = YYDEFAULT;
            break;
          }

        /* Read a lookahead token.  */
        if (yychar == YYEMPTY_)
          {

            yycdebug ("Reading a token");
            yychar = yylexer.yylex ();
            yylval = yylexer.getLVal();
            yylloc = new Location(yylexer.getStartPos(),
                                          yylexer.getEndPos());

          }

        /* Convert token to internal form.  */
        yytoken = yytranslate_ (yychar);
        yySymbolPrint("Next token is", yytoken,
                      yylval, yylloc);

        if (yytoken == SymbolKind.S_YYerror)
          {
            // The scanner already issued an error message, process directly
            // to error recovery.  But do not keep the error token as
            // lookahead, it is too special and may lead us to an endless
            // loop in error recovery. */
            yychar = Lexer.YYUNDEF;
            yytoken = SymbolKind.S_YYUNDEF;
            yyerrloc = yylloc;
            label = YYERRLAB1;
          }
        else
          {
            /* If the proper action on seeing token YYTOKEN is to reduce or to
               detect an error, take that action.  */
            yyn += yytoken.getCode();
            if (yyn < 0 || YYLAST_ < yyn || yycheck_[yyn] != yytoken.getCode())
              label = YYDEFAULT;

            /* <= 0 means reduce or error.  */
            else if ((yyn = yytable_[yyn]) <= 0)
              {
                if (yyTableValueIsError (yyn))
                  label = YYERRLAB;
                else
                  {
                    yyn = -yyn;
                    label = YYREDUCE;
                  }
              }

            else
              {
                /* Shift the lookahead token.  */
                yySymbolPrint("Shifting", yytoken,
                              yylval, yylloc);

                /* Discard the token being shifted.  */
                yychar = YYEMPTY_;

                /* Count tokens shifted since error; after three, turn off error
                   status.  */
                if (yyerrstatus_ > 0)
                  --yyerrstatus_;

                yystate = yyn;
                yystack.push (yystate, yylval, yylloc);
                label = YYNEWSTATE;
              }
          }
        break;

      /*-----------------------------------------------------------.
      | yydefault -- do the default action for the current state.  |
      `-----------------------------------------------------------*/
      case YYDEFAULT:
        yyn = yydefact_[yystate];
        if (yyn == 0)
          label = YYERRLAB;
        else
          label = YYREDUCE;
        break;

      /*-----------------------------.
      | yyreduce -- Do a reduction.  |
      `-----------------------------*/
      case YYREDUCE:
        yylen = yyr2_[yyn];
        label = yyaction(yyn, yystack, yylen);
        yystate = yystack.stateAt (0);
        break;

      /*------------------------------------.
      | yyerrlab -- here on detecting error |
      `------------------------------------*/
      case YYERRLAB:
        /* If not already recovering from an error, report this error.  */
        if (yyerrstatus_ == 0)
          {
            ++yynerrs;
            if (yychar == YYEMPTY_)
              yytoken = null;
            yyreportSyntaxError (new Context (yystack, yytoken, yylloc));
          }

        yyerrloc = yylloc;
        if (yyerrstatus_ == 3)
          {
            /* If just tried and failed to reuse lookahead token after an
               error, discard it.  */

            if (yychar <= Lexer.YYEOF)
              {
                /* Return failure if at end of input.  */
                if (yychar == Lexer.YYEOF)
                  return false;
              }
            else
              yychar = YYEMPTY_;
          }

        /* Else will try to reuse lookahead token after shifting the error
           token.  */
        label = YYERRLAB1;
        break;

      /*-------------------------------------------------.
      | errorlab -- error raised explicitly by YYERROR.  |
      `-------------------------------------------------*/
      case YYERROR:
        yyerrloc = yystack.locationAt (yylen - 1);
        /* Do not reclaim the symbols of the rule which action triggered
           this YYERROR.  */
        yystack.pop (yylen);
        yylen = 0;
        yystate = yystack.stateAt (0);
        label = YYERRLAB1;
        break;

      /*-------------------------------------------------------------.
      | yyerrlab1 -- common code for both syntax error and YYERROR.  |
      `-------------------------------------------------------------*/
      case YYERRLAB1:
        yyerrstatus_ = 3;       /* Each real token shifted decrements this.  */

        // Pop stack until we find a state that shifts the error token.
        for (;;)
          {
            yyn = yypact_[yystate];
            if (!yyPactValueIsDefault (yyn))
              {
                yyn += SymbolKind.S_YYerror.getCode();
                if (0 <= yyn && yyn <= YYLAST_
                    && yycheck_[yyn] == SymbolKind.S_YYerror.getCode())
                  {
                    yyn = yytable_[yyn];
                    if (0 < yyn)
                      break;
                  }
              }

            /* Pop the current state because it cannot handle the
             * error token.  */
            if (yystack.height == 0)
              return false;


            yyerrloc = yystack.locationAt (0);
            yystack.pop ();
            yystate = yystack.stateAt (0);
            if (0 < yydebug)
              yystack.print (yyDebugStream);
          }

        if (label == YYABORT)
          /* Leave the switch.  */
          break;


        /* Muck with the stack to setup for yylloc.  */
        yystack.push (0, null, yylloc);
        yystack.push (0, null, yyerrloc);
        yyloc = yylloc (yystack, 2);
        yystack.pop (2);

        /* Shift the error token.  */
        yySymbolPrint("Shifting", SymbolKind.get(yystos_[yyn]),
                      yylval, yyloc);

        yystate = yyn;
        yystack.push (yyn, yylval, yyloc);
        label = YYNEWSTATE;
        break;

        /* Accept.  */
      case YYACCEPT:
        return true;

        /* Abort.  */
      case YYABORT:
        return false;
      }
}




  /**
   * Information needed to get the list of expected tokens and to forge
   * a syntax error diagnostic.
   */
  public static final class Context
  {
    Context (YYStack stack, SymbolKind token, Location loc)
    {
      yystack = stack;
      yytoken = token;
      yylocation = loc;
    }

    private YYStack yystack;


    /**
     * The symbol kind of the lookahead token.
     */
    public final SymbolKind getToken ()
    {
      return yytoken;
    }

    private SymbolKind yytoken;

    /**
     * The location of the lookahead.
     */
    public final Location getLocation ()
    {
      return yylocation;
    }

    private Location yylocation;
    static final int NTOKENS = SrgsAbnf.YYNTOKENS_;

    /**
     * Put in YYARG at most YYARGN of the expected tokens given the
     * current YYCTX, and return the number of tokens stored in YYARG.  If
     * YYARG is null, return the number of expected tokens (guaranteed to
     * be less than YYNTOKENS).
     */
    int getExpectedTokens (SymbolKind yyarg[], int yyargn)
    {
      return getExpectedTokens (yyarg, 0, yyargn);
    }

    int getExpectedTokens (SymbolKind yyarg[], int yyoffset, int yyargn)
    {
      int yycount = yyoffset;
      int yyn = yypact_[this.yystack.stateAt (0)];
      if (!yyPactValueIsDefault (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative
             indexes in YYCHECK.  In other words, skip the first
             -YYN actions for this state because they are default
             actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST_ - yyn + 1;
          int yyxend = yychecklim < NTOKENS ? yychecklim : NTOKENS;
          for (int yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck_[yyx + yyn] == yyx && yyx != SymbolKind.S_YYerror.getCode()
                && !yyTableValueIsError(yytable_[yyx + yyn]))
              {
                if (yyarg == null)
                  yycount += 1;
                else if (yycount == yyargn)
                  return 0; // FIXME: this is incorrect.
                else
                  yyarg[yycount++] = SymbolKind.get(yyx);
              }
        }
      if (yyarg != null && yycount == yyoffset && yyoffset < yyargn)
        yyarg[yycount] = null;
      return yycount - yyoffset;
    }
  }


  private int yysyntaxErrorArguments (Context yyctx, SymbolKind[] yyarg, int yyargn)
  {
    /* There are many possibilities here to consider:
       - If this state is a consistent state with a default action,
         then the only way this function was invoked is if the
         default action is an error action.  In that case, don't
         check for expected tokens because there are none.
       - The only way there can be no lookahead present (in tok) is
         if this state is a consistent state with a default action.
         Thus, detecting the absence of a lookahead is sufficient to
         determine that there is no unexpected or expected token to
         report.  In that case, just report a simple "syntax error".
       - Don't assume there isn't a lookahead just because this
         state is a consistent state with a default action.  There
         might have been a previous inconsistent state, consistent
         state with a non-default action, or user semantic action
         that manipulated yychar.  (However, yychar is currently out
         of scope during semantic actions.)
       - Of course, the expected token list depends on states to
         have correct lookahead information, and it depends on the
         parser not to perform extra reductions after fetching a
         lookahead from the scanner and before detecting a syntax
         error.  Thus, state merging (from LALR or IELR) and default
         reductions corrupt the expected token list.  However, the
         list is correct for canonical LR with one exception: it
         will still contain any token that will not be accepted due
         to an error action in a later state.
    */
    int yycount = 0;
    if (yyctx.getToken() != null)
      {
        if (yyarg != null)
          yyarg[yycount] = yyctx.getToken();
        yycount += 1;
        yycount += yyctx.getExpectedTokens(yyarg, 1, yyargn);
      }
    return yycount;
  }


  /**
   * Build and emit a "syntax error" message in a user-defined way.
   *
   * @param ctx  The context of the error.
   */
  private void yyreportSyntaxError(Context yyctx) {
      if (yyErrorVerbose) {
          final int argmax = 5;
          SymbolKind[] yyarg = new SymbolKind[argmax];
          int yycount = yysyntaxErrorArguments(yyctx, yyarg, argmax);
          String[] yystr = new String[yycount];
          for (int yyi = 0; yyi < yycount; ++yyi) {
              yystr[yyi] = yyarg[yyi].getName();
          }
          String yyformat;
          switch (yycount) {
              default:
              case 0: yyformat = "syntax error"; break;
              case 1: yyformat = "syntax error, unexpected {0}"; break;
              case 2: yyformat = "syntax error, unexpected {0}, expecting {1}"; break;
              case 3: yyformat = "syntax error, unexpected {0}, expecting {1} or {2}"; break;
              case 4: yyformat = "syntax error, unexpected {0}, expecting {1} or {2} or {3}"; break;
              case 5: yyformat = "syntax error, unexpected {0}, expecting {1} or {2} or {3} or {4}"; break;
          }
          yyerror(yyctx.yylocation, new MessageFormat(yyformat).format(yystr));
      } else {
          yyerror(yyctx.yylocation, "syntax error");
      }
  }

  /**
   * Whether the given <code>yypact_</code> value indicates a defaulted state.
   * @param yyvalue   the value to check
   */
  private static boolean yyPactValueIsDefault (int yyvalue)
  {
    return yyvalue == yypact_ninf_;
  }

  /**
   * Whether the given <code>yytable_</code>
   * value indicates a syntax error.
   * @param yyvalue the value to check
   */
  private static boolean yyTableValueIsError (int yyvalue)
  {
    return yyvalue == yytable_ninf_;
  }

  private static final byte yypact_ninf_ = -58;
  private static final byte yytable_ninf_ = -25;

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
  private static final byte[] yypact_ = yypact_init();
  private static final byte[] yypact_init()
  {
    return new byte[]
    {
      12,   -58,    41,    10,   -58,    30,    33,    20,    15,    23,
      50,    36,    47,   -58,   -58,   -58,   -58,   -58,   -58,   -58,
     -58,    39,    38,    46,   -58,    48,    51,   -58,    52,    58,
      64,   -58,   -58,   -58,    55,   -58,   -58,   -58,   -58,   -58,
      60,    63,    53,    56,    59,    19,   -58,   -58,    57,    61,
      49,    62,    65,   -58,     2,   -12,    19,     8,    49,   -58,
      66,    71,    73,    49,    74,    76,   -58,    67,   -58,     9,
     -28,   -58,    34,   -58,    79,   -58,   -58,   -58,   -58,   -58,
      68,    69,    49,    49,    77,    72,    84,    85,    49,   -58,
     -58,   -58,   -58
    };
  }

/* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE does not specify something else to do.  Zero
   means the default is an error.  */
  private static final byte[] yydefact_ = yydefact_init();
  private static final byte[] yydefact_init()
  {
    return new byte[]
    {
       0,     3,     0,    21,     1,     0,     0,     0,     0,     0,
       0,     0,     0,     4,     5,     6,     7,     8,     9,    10,
      11,     2,     0,     0,    15,     0,     0,    14,     0,     0,
       0,    25,    26,    22,     0,    13,    12,    16,    17,    18,
       0,     0,     0,     0,     0,     0,    19,    20,    43,    37,
       0,    39,    41,    45,     0,     0,     0,     0,    27,    31,
      33,     0,     0,    28,     0,     0,    52,     0,    50,     0,
       0,    23,     0,    32,     0,    44,    38,    40,    42,    51,
      46,    48,     0,    29,    35,     0,     0,     0,    30,    36,
      34,    47,    49
    };
  }

/* YYPGOTO[NTERM-NUM].  */
  private static final byte[] yypgoto_ = yypgoto_init();
  private static final byte[] yypgoto_init()
  {
    return new byte[]
    {
     -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,
     -58,   -58,   -58,   -58,   -47,   -50,   -57,   -58,   -58
    };
  }

/* YYDEFGOTO[NTERM-NUM].  */
  private static final byte[] yydefgoto_ = yydefgoto_init();
  private static final byte[] yydefgoto_init()
  {
    return new byte[]
    {
       0,     2,     3,    13,    14,    15,    16,    17,    18,    19,
      20,    21,    33,    34,    57,    58,    59,    85,    60
    };
  }

/* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule whose
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
  private static final byte[] yytable_ = yytable_init();
  private static final byte[] yytable_init()
  {
    return new byte[]
    {
      63,    73,    48,    72,    49,    50,    73,    51,    69,    70,
      81,    52,    53,    54,     5,     6,     7,     8,     9,    10,
      11,    12,    83,    55,    68,    56,    73,     1,    66,    25,
      67,    73,    88,    48,    26,    49,    50,    71,    51,    72,
      72,     4,    52,    53,    54,    80,    22,    23,    48,    24,
      49,    82,    27,    51,    55,    29,    56,    52,    53,    54,
      31,    32,   -24,    48,    28,    49,    30,    35,    51,    55,
      40,    56,    52,    53,    54,    36,    41,    37,    42,    43,
      38,    39,    44,    45,    55,    46,    56,    75,    47,    76,
      77,    61,    78,    79,    89,    62,    64,    84,    74,    65,
      91,    92,    86,    87,     0,    90
    };
  }

private static final byte[] yycheck_ = yycheck_init();
  private static final byte[] yycheck_init()
  {
    return new byte[]
    {
      50,    58,    14,    31,    16,    17,    63,    19,    55,    56,
      38,    23,    24,    25,     4,     5,     6,     7,     8,     9,
      10,    11,    72,    35,    36,    37,    83,    15,    26,    14,
      28,    88,    82,    14,    19,    16,    17,    29,    19,    31,
      31,     0,    23,    24,    25,    36,    16,    14,    14,    29,
      16,    17,    29,    19,    35,    19,    37,    23,    24,    25,
      21,    22,    23,    14,    14,    16,    19,    29,    19,    35,
      12,    37,    23,    24,    25,    29,    12,    29,    23,    19,
      29,    29,    19,    30,    35,    29,    37,    16,    29,    16,
      16,    34,    16,    26,    17,    34,    34,    18,    32,    34,
      16,    16,    34,    34,    -1,    33
    };
  }

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
  private static final byte[] yystos_ = yystos_init();
  private static final byte[] yystos_init()
  {
    return new byte[]
    {
       0,    15,    40,    41,     0,     4,     5,     6,     7,     8,
       9,    10,    11,    42,    43,    44,    45,    46,    47,    48,
      49,    50,    16,    14,    29,    14,    19,    29,    14,    19,
      19,    21,    22,    51,    52,    29,    29,    29,    29,    29,
      12,    12,    23,    19,    19,    30,    29,    29,    14,    16,
      17,    19,    23,    24,    25,    35,    37,    53,    54,    55,
      57,    34,    34,    54,    34,    34,    26,    28,    36,    53,
      53,    29,    31,    55,    32,    16,    16,    16,    16,    26,
      36,    38,    17,    54,    18,    56,    34,    34,    54,    17,
      33,    16,    16
    };
  }

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
  private static final byte[] yyr1_ = yyr1_init();
  private static final byte[] yyr1_init()
  {
    return new byte[]
    {
       0,    39,    40,    41,    41,    42,    42,    42,    42,    42,
      42,    42,    43,    44,    45,    46,    47,    47,    48,    49,
      49,    50,    50,    51,    52,    52,    52,    53,    53,    53,
      53,    54,    54,    55,    55,    56,    56,    57,    57,    57,
      57,    57,    57,    57,    57,    57,    57,    57,    57,    57,
      57,    57,    57
    };
  }

/* YYR2[YYN] -- Number of symbols on the right hand side of rule YYN.  */
  private static final byte[] yyr2_ = yyr2_init();
  private static final byte[] yyr2_init()
  {
    return new byte[]
    {
       0,     2,     3,     0,     2,     1,     1,     1,     1,     1,
       1,     1,     3,     3,     2,     2,     3,     3,     3,     5,
       5,     0,     2,     5,     0,     1,     1,     1,     2,     3,
       4,     1,     2,     1,     4,     1,     2,     1,     3,     1,
       3,     1,     3,     1,     3,     1,     3,     5,     3,     5,
       2,     3,     2
    };
  }



  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
  private static final short[] yyrline_ = yyrline_init();
  private static final short[] yyrline_init()
  {
    return new short[]
    {
       0,   169,   169,   173,   174,   177,   178,   179,   180,   181,
     182,   183,   186,   194,   203,   210,   219,   224,   228,   231,
     234,   239,   240,   243,   253,   254,   255,   258,   261,   266,
     277,   298,   299,   312,   313,   318,   319,   334,   338,   342,
     346,   350,   353,   358,   361,   366,   369,   372,   376,   379,
     383,   387,   388
    };
  }


  // Report on the debug stream that the rule yyrule is going to be reduced.
  private void yyReducePrint (int yyrule, YYStack yystack)
  {
    if (yydebug == 0)
      return;

    int yylno = yyrline_[yyrule];
    int yynrhs = yyr2_[yyrule];
    /* Print the symbols being reduced, and their result.  */
    yycdebug ("Reducing stack by rule " + (yyrule - 1)
              + " (line " + yylno + "):");

    /* The symbols being reduced.  */
    for (int yyi = 0; yyi < yynrhs; yyi++)
      yySymbolPrint("   $" + (yyi + 1) + " =",
                    SymbolKind.get(yystos_[yystack.stateAt (yynrhs - (yyi + 1))]),
                    yystack.valueAt ((yynrhs) - (yyi + 1)),
                    yystack.locationAt ((yynrhs) - (yyi + 1)));
  }

  /* YYTRANSLATE_(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
     as returned by yylex, with out-of-bounds checking.  */
  private static final SymbolKind yytranslate_(int t)
  {
    // Last valid token kind.
    int code_max = 283;
    if (t <= 0)
      return SymbolKind.S_YYEOF;
    else if (t <= code_max)
      return SymbolKind.get(yytranslate_table_[t]);
    else
      return SymbolKind.S_YYUNDEF;
  }
  private static final byte[] yytranslate_table_ = yytranslate_table_init();
  private static final byte[] yytranslate_table_init()
  {
    return new byte[]
    {
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,    34,     2,     2,     2,     2,     2,     2,
      35,    36,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,    29,
      32,    30,    33,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,    37,     2,    38,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,    31,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28
    };
  }


  private static final int YYLAST_ = 105;
  private static final int YYEMPTY_ = -2;
  private static final int YYFINAL_ = 4;
  private static final int YYNTOKENS_ = 39;

/* Unqualified %code blocks.  */
/* "SrgsAbnf.y":27  */

  List<Rule> rules = new ArrayList<>();
  Map<String, Object> attributes = new HashMap<>();

  public List<Rule> getRules() { return rules; }
  public Map<String, Object> getAttributes() { return attributes; }

  private void addRuleDef(String ref, String scope, RuleComponent body){
    rules.add(new Rule(ref, body,
                       scope.compareToIgnoreCase("public") == 0
                       ? Rule.PUBLIC : Rule.PRIVATE));
  }

  private void addMeta(Meta m) {
    List<Meta> metas;
    metas = (List<Meta>) attributes.get("meta");
    if (metas == null) {
      metas = new ArrayList<>();
      attributes.put("meta", metas);
    }
    metas.add(m);
  }

  private void addLexicon(lexicon m) {
    List<lexicon> lexica;
    lexica = (List<lexicon>) attributes.get("lexicon");
    if (lexica == null) {
      lexica = new ArrayList<>();
      attributes.put("lexicon", lexica);
    }
    lexica.add(m);
  }

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
      hashIndex >= 0 ? uriStr.substring(hashIndex + 1).trim() : "___root";
    final String grammarName =
      hashIndex >= 0 ? uriStr.substring(0, hashIndex) : uriStr;
    //if (grammarName.isEmpty)
    //  return new RuleReference(ruleName); // not legal in ABNF
    try {
      RuleReference res = new RuleReference(new URI(grammarName), ruleName);
      if (uri.length == 2)
        res.setMediaType(uri[1]);
      return res;
    }
    catch (URISyntaxException ex) {
      yyerror("Wrong URI: " + uri[0]);
    }
    return null;
  }

/* "SrgsAbnf.java":1884  */

}
