/* A Bison parser, made by GNU Bison 3.5.1.  */

/* Skeleton implementation for Bison LALR(1) parsers in Java

   Copyright (C) 2007-2015, 2018-2020 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

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

package org.jvoicexml.processor.srgs.abnf;



/* "%code imports" blocks.  */
/* "SrgsAbnf.y":3  */


import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.jvoicexml.processor.srgs.grammar.*;

@SuppressWarnings({"unused", "unchecked"})

/* "SrgsAbnf.java":51  */

/**
 * A Bison parser, automatically generated from <tt>SrgsAbnf.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
public class SrgsAbnf
{
    /** Version number for the Bison executable that generated this parser.  */
  public static final String bisonVersion = "3.5.1";

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
  public class Location {
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




  private Location yylloc (YYStack rhs, int n)
  {
    if (0 < n)
      return new Location (rhs.locationAt (n-1).begin, rhs.locationAt (0).end);
    else
      return new Location (rhs.locationAt (0).end);
  }

  /**
   * Communication interface between the scanner and the Bison-generated
   * parser <tt>SrgsAbnf</tt>.
   */
  public interface Lexer {
    /** Token returned by the scanner to signal the end of its input.  */
    public static final int EOF = 0;

/* Tokens.  */
    /** Token number,to be returned by the scanner.  */
    static final int ERR = 258;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_LANG = 259;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_BASE = 260;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_ROOT = 261;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_TAG_FORMAT = 262;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_MODE = 263;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_LEXICON = 264;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_HTTP_EQUIV = 265;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_META = 266;
    /** Token number,to be returned by the scanner.  */
    static final int DECL_IS = 267;
    /** Token number,to be returned by the scanner.  */
    static final int IllegalCharacter = 268;
    /** Token number,to be returned by the scanner.  */
    static final int URI = 269;
    /** Token number,to be returned by the scanner.  */
    static final int SelfIdentHeader = 270;
    /** Token number,to be returned by the scanner.  */
    static final int Nmtoken = 271;
    /** Token number,to be returned by the scanner.  */
    static final int SlashNum = 272;
    /** Token number,to be returned by the scanner.  */
    static final int Repeat = 273;
    /** Token number,to be returned by the scanner.  */
    static final int QuotedCharacters = 274;
    /** Token number,to be returned by the scanner.  */
    static final int SimpleToken = 275;
    /** Token number,to be returned by the scanner.  */
    static final int Private = 276;
    /** Token number,to be returned by the scanner.  */
    static final int Public = 277;
    /** Token number,to be returned by the scanner.  */
    static final int RuleName = 278;
    /** Token number,to be returned by the scanner.  */
    static final int specialRuleReference = 279;
    /** Token number,to be returned by the scanner.  */
    static final int TagStart = 280;
    /** Token number,to be returned by the scanner.  */
    static final int TagEnd = 281;
    /** Token number,to be returned by the scanner.  */
    static final int Path = 282;
    /** Token number,to be returned by the scanner.  */
    static final int Tag = 283;


    /**
     * Method to retrieve the beginning position of the last scanned token.
     * @return the position at which the last scanned token starts.
     */
    Position getStartPos ();

    /**
     * Method to retrieve the ending position of the last scanned token.
     * @return the first position beyond the last scanned token.
     */
    Position getEndPos ();

    /**
     * Method to retrieve the semantic value of the last scanned token.
     * @return the semantic value of the last scanned token.
     */
    Object getLVal ();

    /**
     * Entry point for the scanner.  Returns the token identifier corresponding
     * to the next token and prepares to return the semantic value
     * and beginning/ending positions of the token.
     * @return the token identifier corresponding to the next token.
     */
    int yylex () throws java.io.IOException;

    /**
     * Entry point for error reporting.  Emits an error
     * referring to the given location in a user-defined way.
     *
     * @param loc The location of the element to which the
     *                error message is related
     * @param msg The string for the error message.
     */
     void yyerror (Location loc, String msg);
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
  public final void setDebugStream(java.io.PrintStream s) { yyDebugStream = s; }

  private int yydebug = 0;

  /**
   * Answer the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   */
  public final int getDebugLevel() { return yydebug; }

  /**
   * Set the verbosity of the debugging output; 0 means that all kinds of
   * output from the parser are suppressed.
   * @param level The verbosity level for debugging output.
   */
  public final void setDebugLevel(int level) { yydebug = level; }


  /**
   * Print an error message via the lexer.
   * Use a <code>null</code> location.
   * @param msg The error message.
   */
  public final void yyerror (String msg)
  {
    yylexer.yyerror ((Location)null, msg);
  }

  /**
   * Print an error message via the lexer.
   * @param loc The location associated with the message.
   * @param msg The error message.
   */
  public final void yyerror (Location loc, String msg)
  {
    yylexer.yyerror (loc, msg);
  }

  /**
   * Print an error message via the lexer.
   * @param pos The position associated with the message.
   * @param msg The error message.
   */
  public final void yyerror (Position pos, String msg)
  {
    yylexer.yyerror (new Location (pos), msg);
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

    public final void push (int state, Object value                            , Location loc) {
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
    int yyr = yypgoto_[yysym - yyntokens_] + yystate;
    if (0 <= yyr && yyr <= yylast_ && yycheck_[yyr] == yystate)
      return yytable_[yyr];
    else
      return yydefgoto_[yysym - yyntokens_];
  }

  private int yyaction (int yyn, YYStack yystack, int yylen) 
  {
    /* If YYLEN is nonzero, implement the default value of the action:
       '$$ = $1'.  Otherwise, use the top of the stack.

       Otherwise, the following line sets YYVAL to garbage.
       This behavior is undocumented and Bison
       users should not rely upon it.  */
    Object yyval = (0 < yylen) ? yystack.valueAt (yylen - 1) : yystack.valueAt (0);
    Location yyloc = yylloc (yystack, yylen);

    yyReducePrint (yyn, yystack);

    switch (yyn)
      {
          case 12:
  if (yyn == 12)
    /* "SrgsAbnf.y":187  */
                            { attributes.put("base", ((String[])(yystack.valueAt (1)))[0]);
/* Additional constraints:
   - A base declaration must not appear more than
   once in grammar.
*/
};
  break;
    

  case 13:
  if (yyn == 13)
    /* "SrgsAbnf.y":195  */
                                    { attributes.put("language", ((String)(yystack.valueAt (1))));
  /* Additional constraints:
     - A language declaration must not appear more than
     once in grammar.
     - A language declaration is required if the
     grammar mode is "voice".
  */ };
  break;
    

  case 14:
  if (yyn == 14)
    /* "SrgsAbnf.y":204  */
                        { attributes.put("mode", ((String)(yystack.valueAt (1)))); };
  break;
    

  case 15:
  if (yyn == 15)
    /* "SrgsAbnf.y":211  */
                            { attributes.put("root", ((String)(yystack.valueAt (1))));
  /*Additional constraints:
          - A root rule declaration must not appear more
            than once in grammar.
          - The root rule must be a rule that is defined
            within the grammar.
        */};
  break;
    

  case 16:
  if (yyn == 16)
    /* "SrgsAbnf.y":220  */
                                       { attributes.put("tag_format", ((String[])(yystack.valueAt (1)))[0]); };
  break;
    

  case 17:
  if (yyn == 17)
    /* "SrgsAbnf.y":225  */
                                       { attributes.put("tag_format", ((String)(yystack.valueAt (1)))); };
  break;
    

  case 18:
  if (yyn == 18)
    /* "SrgsAbnf.y":229  */
                                  { addLexicon(new lexicon(((String[])(yystack.valueAt (1)))[0])); };
  break;
    

  case 19:
  if (yyn == 19)
    /* "SrgsAbnf.y":232  */
                                                                        {
  addMeta(new Meta(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (1))), true));
};
  break;
    

  case 20:
  if (yyn == 20)
    /* "SrgsAbnf.y":235  */
                                                          {
  addMeta(new Meta(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (1))), false));
};
  break;
    

  case 23:
  if (yyn == 23)
    /* "SrgsAbnf.y":245  */
        { /* Additional constraints:
             - The rule name must be unique within a grammar,
               i.e. no rule must be defined more than once
               within a grammar.
          */
          addRuleDef(((String)(yystack.valueAt (3))), ((String)(yystack.valueAt (4))), ((RuleComponent)(yystack.valueAt (1))));
        };
  break;
    

  case 24:
  if (yyn == 24)
    /* "SrgsAbnf.y":254  */
                         { yyval = ""; };
  break;
    

  case 25:
  if (yyn == 25)
    /* "SrgsAbnf.y":255  */
                      { yyval = "private"; };
  break;
    

  case 26:
  if (yyn == 26)
    /* "SrgsAbnf.y":256  */
                      { yyval = "public"; };
  break;
    

  case 27:
  if (yyn == 27)
    /* "SrgsAbnf.y":259  */
                        {
  yyval = ((RuleComponent)(yystack.valueAt (0)));
};
  break;
    

  case 28:
  if (yyn == 28)
    /* "SrgsAbnf.y":262  */
                    {
  RuleAlternatives alt = new RuleAlternatives();
  alt.addAlternative(((RuleComponent)(yystack.valueAt (0))), Double.parseDouble(((String)(yystack.valueAt (1)))));
  yyval = alt;
};
  break;
    

  case 29:
  if (yyn == 29)
    /* "SrgsAbnf.y":267  */
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
    

  case 30:
  if (yyn == 30)
    /* "SrgsAbnf.y":278  */
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
    

  case 31:
  if (yyn == 31)
    /* "SrgsAbnf.y":299  */
                          { yyval = ((RuleComponent)(yystack.valueAt (0))) ; };
  break;
    

  case 32:
  if (yyn == 32)
    /* "SrgsAbnf.y":300  */
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
    

  case 33:
  if (yyn == 33)
    /* "SrgsAbnf.y":313  */
                              { yyval = ((RuleComponent)(yystack.valueAt (0))); };
  break;
    

  case 34:
  if (yyn == 34)
    /* "SrgsAbnf.y":314  */
                              {
  yyval = ((repeat)(yystack.valueAt (1))).prob < 0 ? new RuleCount(((RuleComponent)(yystack.valueAt (3))), ((repeat)(yystack.valueAt (1))).from, ((repeat)(yystack.valueAt (1))).to)
                   : new RuleCount(((RuleComponent)(yystack.valueAt (3))), ((repeat)(yystack.valueAt (1))).from, ((repeat)(yystack.valueAt (1))).to, ((repeat)(yystack.valueAt (1))).prob);
};
  break;
    

  case 35:
  if (yyn == 35)
    /* "SrgsAbnf.y":320  */
               { yyval = new repeat(((String)(yystack.valueAt (0)))); };
  break;
    

  case 36:
  if (yyn == 36)
    /* "SrgsAbnf.y":321  */
                  { yyval = new repeat(((String)(yystack.valueAt (1))), ((String)(yystack.valueAt (0)))); };
  break;
    

  case 37:
  if (yyn == 37)
    /* "SrgsAbnf.y":336  */
                      {
      RuleToken res = new RuleToken(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 38:
  if (yyn == 38)
    /* "SrgsAbnf.y":340  */
                          {
      RuleToken res = new RuleToken(((String)(yystack.valueAt (2))), ((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 39:
  if (yyn == 39)
    /* "SrgsAbnf.y":344  */
                       {  // should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 40:
  if (yyn == 40)
    /* "SrgsAbnf.y":348  */
                                   {// should that be DoubleQuotedCharacters ??
      RuleToken res = new RuleToken(((String)(yystack.valueAt (2))), ((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 41:
  if (yyn == 41)
    /* "SrgsAbnf.y":352  */
               {
      yyval = new RuleReference(((String)(yystack.valueAt (0))));
    };
  break;
    

  case 42:
  if (yyn == 42)
    /* "SrgsAbnf.y":355  */
                           {
      RuleReference res = new RuleReference(((String)(yystack.valueAt (2))));
      res.setLanguage(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 43:
  if (yyn == 43)
    /* "SrgsAbnf.y":360  */
          {
      yyval = getRuleReference(((String[])(yystack.valueAt (0))));
    };
  break;
    

  case 44:
  if (yyn == 44)
    /* "SrgsAbnf.y":363  */
                      {
      RuleReference res = getRuleReference(((String[])(yystack.valueAt (2))));
      res.setLanguage(((String)(yystack.valueAt (0))));
      yyval = res;
    };
  break;
    

  case 45:
  if (yyn == 45)
    /* "SrgsAbnf.y":368  */
                           {
      yyval = ((RuleComponent)(yystack.valueAt (0)));
    };
  break;
    

  case 46:
  if (yyn == 46)
    /* "SrgsAbnf.y":371  */
                            {
      yyval = ((RuleComponent)(yystack.valueAt (1)));
    };
  break;
    

  case 47:
  if (yyn == 47)
    /* "SrgsAbnf.y":374  */
                                        {
      ((RuleComponent)(yystack.valueAt (3))).setLanguage(((String)(yystack.valueAt (0))));
      yyval = ((RuleComponent)(yystack.valueAt (3)));
    };
  break;
    

  case 48:
  if (yyn == 48)
    /* "SrgsAbnf.y":378  */
                            {
      yyval = setOptional(((RuleComponent)(yystack.valueAt (1))));
    };
  break;
    

  case 49:
  if (yyn == 49)
    /* "SrgsAbnf.y":381  */
                                        {
      RuleComponent opt = setOptional(((RuleComponent)(yystack.valueAt (3))));
      opt.setLanguage(((String)(yystack.valueAt (0))));
      yyval = opt;
    };
  break;
    

  case 50:
  if (yyn == 50)
    /* "SrgsAbnf.y":386  */
              {
      yyval = RuleSpecial.NULL ;
    };
  break;
    

  case 51:
  if (yyn == 51)
    /* "SrgsAbnf.y":390  */
                          { yyval = new RuleTag(((String)(yystack.valueAt (1)))) ; };
  break;
    

  case 52:
  if (yyn == 52)
    /* "SrgsAbnf.y":391  */
                      { yyval = new RuleTag("") ; };
  break;
    


/* "SrgsAbnf.java":853  */

        default: break;
      }

    yySymbolPrint ("-> $$ =", yyr1_[yyn], yyval, yyloc);

    yystack.pop (yylen);
    yylen = 0;

    /* Shift the result of the reduction.  */
    int yystate = yyLRGotoState (yystack.stateAt (0), yyr1_[yyn]);
    yystack.push (yystate, yyval, yyloc);
    return YYNEWSTATE;
  }


  /* Return YYSTR after stripping away unnecessary quotes and
     backslashes, so that it's suitable for yyerror.  The heuristic is
     that double-quoting is unnecessary unless the string contains an
     apostrophe, a comma, or backslash (other than backslash-backslash).
     YYSTR is taken from yytname.  */
  private final String yytnamerr_ (String yystr)
  {
    if (yystr.charAt (0) == '"')
      {
        StringBuffer yyr = new StringBuffer ();
        strip_quotes: for (int i = 1; i < yystr.length (); i++)
          switch (yystr.charAt (i))
            {
            case '\'':
            case ',':
              break strip_quotes;

            case '\\':
              if (yystr.charAt(++i) != '\\')
                break strip_quotes;
              /* Fall through.  */
            default:
              yyr.append (yystr.charAt (i));
              break;

            case '"':
              return yyr.toString ();
            }
      }
    else if (yystr.equals ("$end"))
      return "end of input";

    return yystr;
  }


  /*--------------------------------.
  | Print this symbol on YYOUTPUT.  |
  `--------------------------------*/

  private void yySymbolPrint (String s, int yytype,
                             Object yyvaluep                              , Object yylocationp)
  {
    yycdebug (s + (yytype < yyntokens_ ? " token " : " nterm ")
              + yytname_[yytype] + " ("
              + yylocationp + ": "
              + (yyvaluep == null ? "(null)" : yyvaluep.toString ()) + ")");
  }


  /**
   * Parse input from the scanner that was specified at object construction
   * time.  Return whether the end of the input was reached successfully.
   *
   * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
   *          imply that there were no syntax errors.
   */
  public boolean parse () throws java.io.IOException

  {
    /* @$.  */
    Location yyloc;


    /* Lookahead and lookahead in internal form.  */
    int yychar = yyempty_;
    int yytoken = 0;

    /* State.  */
    int yyn = 0;
    int yylen = 0;
    int yystate = 0;
    YYStack yystack = new YYStack ();
    int label = YYNEWSTATE;

    /* Error handling.  */
    int yynerrs_ = 0;
    /* The location where the error started.  */
    Location yyerrloc = null;

    /* Location. */
    Location yylloc = new Location (null, null);

    /* Semantic value of the lookahead.  */
    Object yylval = null;

    yycdebug ("Starting parse\n");
    yyerrstatus_ = 0;

    /* Initialize the stack.  */
    yystack.push (yystate, yylval , yylloc);



    for (;;)
      switch (label)
      {
        /* New state.  Unlike in the C/C++ skeletons, the state is already
           pushed when we come here.  */
      case YYNEWSTATE:
        yycdebug ("Entering state " + yystate + "\n");
        if (0 < yydebug)
          yystack.print (yyDebugStream);

        /* Accept?  */
        if (yystate == yyfinal_)
          return true;

        /* Take a decision.  First try without lookahead.  */
        yyn = yypact_[yystate];
        if (yyPactValueIsDefault (yyn))
          {
            label = YYDEFAULT;
            break;
          }

        /* Read a lookahead token.  */
        if (yychar == yyempty_)
          {

            yycdebug ("Reading a token: ");
            yychar = yylexer.yylex ();
            yylval = yylexer.getLVal ();
            yylloc = new Location (yylexer.getStartPos (),
                            yylexer.getEndPos ());

          }

        /* Convert token to internal form.  */
        yytoken = yytranslate_ (yychar);
        yySymbolPrint ("Next token is", yytoken,
                       yylval, yylloc);

        /* If the proper action on seeing token YYTOKEN is to reduce or to
           detect an error, take that action.  */
        yyn += yytoken;
        if (yyn < 0 || yylast_ < yyn || yycheck_[yyn] != yytoken)
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
            yySymbolPrint ("Shifting", yytoken,
                           yylval, yylloc);

            /* Discard the token being shifted.  */
            yychar = yyempty_;

            /* Count tokens shifted since error; after three, turn off error
               status.  */
            if (yyerrstatus_ > 0)
              --yyerrstatus_;

            yystate = yyn;
            yystack.push (yystate, yylval, yylloc);
            label = YYNEWSTATE;
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
        label = yyaction (yyn, yystack, yylen);
        yystate = yystack.stateAt (0);
        break;

      /*------------------------------------.
      | yyerrlab -- here on detecting error |
      `------------------------------------*/
      case YYERRLAB:
        /* If not already recovering from an error, report this error.  */
        if (yyerrstatus_ == 0)
          {
            ++yynerrs_;
            if (yychar == yyempty_)
              yytoken = yyempty_;
            yyerror (yylloc, yysyntax_error (yystate, yytoken));
          }

        yyerrloc = yylloc;
        if (yyerrstatus_ == 3)
          {
            /* If just tried and failed to reuse lookahead token after an
               error, discard it.  */

            if (yychar <= Lexer.EOF)
              {
                /* Return failure if at end of input.  */
                if (yychar == Lexer.EOF)
                  return false;
              }
            else
              yychar = yyempty_;
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

        for (;;)
          {
            yyn = yypact_[yystate];
            if (!yyPactValueIsDefault (yyn))
              {
                yyn += yy_error_token_;
                if (0 <= yyn && yyn <= yylast_ && yycheck_[yyn] == yy_error_token_)
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
        yySymbolPrint ("Shifting", yystos_[yyn],
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




  // Generate an error message.
  private String yysyntax_error (int yystate, int tok)
  {
    if (yyErrorVerbose)
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
        if (tok != yyempty_)
          {
            /* FIXME: This method of building the message is not compatible
               with internationalization.  */
            StringBuffer res =
              new StringBuffer ("syntax error, unexpected ");
            res.append (yytnamerr_ (yytname_[tok]));
            int yyn = yypact_[yystate];
            if (!yyPactValueIsDefault (yyn))
              {
                /* Start YYX at -YYN if negative to avoid negative
                   indexes in YYCHECK.  In other words, skip the first
                   -YYN actions for this state because they are default
                   actions.  */
                int yyxbegin = yyn < 0 ? -yyn : 0;
                /* Stay within bounds of both yycheck and yytname.  */
                int yychecklim = yylast_ - yyn + 1;
                int yyxend = yychecklim < yyntokens_ ? yychecklim : yyntokens_;
                int count = 0;
                for (int x = yyxbegin; x < yyxend; ++x)
                  if (yycheck_[x + yyn] == x && x != yy_error_token_
                      && !yyTableValueIsError (yytable_[x + yyn]))
                    ++count;
                if (count < 5)
                  {
                    count = 0;
                    for (int x = yyxbegin; x < yyxend; ++x)
                      if (yycheck_[x + yyn] == x && x != yy_error_token_
                          && !yyTableValueIsError (yytable_[x + yyn]))
                        {
                          res.append (count++ == 0 ? ", expecting " : " or ");
                          res.append (yytnamerr_ (yytname_[x]));
                        }
                  }
              }
            return res.toString ();
          }
      }

    return "syntax error";
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
  private static final byte yypact_[] = yypact_init();
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
  private static final byte yydefact_[] = yydefact_init();
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
  private static final byte yypgoto_[] = yypgoto_init();
  private static final byte[] yypgoto_init()
  {
    return new byte[]
    {
     -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,   -58,
     -58,   -58,   -58,   -58,   -47,   -50,   -57,   -58,   -58
    };
  }

/* YYDEFGOTO[NTERM-NUM].  */
  private static final byte yydefgoto_[] = yydefgoto_init();
  private static final byte[] yydefgoto_init()
  {
    return new byte[]
    {
      -1,     2,     3,    13,    14,    15,    16,    17,    18,    19,
      20,    21,    33,    34,    57,    58,    59,    85,    60
    };
  }

/* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule whose
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
  private static final byte yytable_[] = yytable_init();
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

private static final byte yycheck_[] = yycheck_init();
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
  private static final byte yystos_[] = yystos_init();
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
  private static final byte yyr1_[] = yyr1_init();
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
  private static final byte yyr2_[] = yyr2_init();
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


  /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
     First, the terminals, then, starting at \a yyntokens_, nonterminals.  */
  private static final String yytname_[] = yytname_init();
  private static final String[] yytname_init()
  {
    return new String[]
    {
  "$end", "error", "$undefined", "ERR", "DECL_LANG", "DECL_BASE",
  "DECL_ROOT", "DECL_TAG_FORMAT", "DECL_MODE", "DECL_LEXICON",
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


  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
  private static final short yyrline_[] = yyrline_init();
  private static final short[] yyrline_init()
  {
    return new short[]
    {
       0,   170,   170,   174,   175,   178,   179,   180,   181,   182,
     183,   184,   187,   195,   204,   211,   220,   225,   229,   232,
     235,   240,   241,   244,   254,   255,   256,   259,   262,   267,
     278,   299,   300,   313,   314,   320,   321,   336,   340,   344,
     348,   352,   355,   360,   363,   368,   371,   374,   378,   381,
     386,   390,   391
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
              + " (line " + yylno + "), ");

    /* The symbols being reduced.  */
    for (int yyi = 0; yyi < yynrhs; yyi++)
      yySymbolPrint ("   $" + (yyi + 1) + " =",
                     yystos_[yystack.stateAt(yynrhs - (yyi + 1))],
                     yystack.valueAt ((yynrhs) - (yyi + 1)),
                     yystack.locationAt ((yynrhs) - (yyi + 1)));
  }

  /* YYTRANSLATE_(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
     as returned by yylex, with out-of-bounds checking.  */
  private static final byte yytranslate_ (int t)
  {
    int user_token_number_max_ = 283;
    byte undef_token_ = 2;

    if (t <= 0)
      return Lexer.EOF;
    else if (t <= user_token_number_max_)
      return yytranslate_table_[t];
    else
      return undef_token_;
  }
  private static final byte yytranslate_table_[] = yytranslate_table_init();
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


  private static final byte yy_error_token_ = 1;

  private static final int yylast_ = 105;
  private static final int yynnts_ = 19;
  private static final int yyempty_ = -2;
  private static final int yyfinal_ = 4;
  private static final int yyntokens_ = 39;

/* User implementation code.  */
/* Unqualified %code blocks.  */
/* "SrgsAbnf.y":28  */

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
    public double prob = -1;

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

/* "SrgsAbnf.java":1639  */

}

