/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2019-  Bernd Kiefer <kiefer@dfki.de>                      *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: CC 4.0                                                         *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package org.jvoicexml.processor.srgs.abnf;

import java.io.PrintStream;
import java.io.Reader;
import java.util.LinkedList;

import org.jvoicexml.processor.srgs.grammar.RuleSpecial;

%%

%class SrgsLexer
%implements SrgsAbnf.Lexer
%function yylex_internal

%unicode

%char
%line
%column

%byaccj

%{
  private String origin;

  private Object yylval;

  private LinkedList<Token> commentTokens = new LinkedList<>();

  private LinkedList<Token> tokens = new LinkedList<>();

  private PrintStream err;

  public SrgsLexer(Reader r, PrintStream ps) {
    this(r);
    err = ps;
  }

  /**
   * Method to retrieve the beginning position of the last scanned token.
   * @return the position at which the last scanned token starts.
   */
  public Position getStartPos() {
    return new Position(yyline, yycolumn, yychar, origin);
  }

  /**
   * Method to retrieve the ending position of the last scanned token.
   * @return the first position beyond the last scanned token.
   */
  public Position getEndPos() {
    int len = yylength();
    return new Position(yyline, yycolumn + len, yychar + len, origin);
  }

  /**
   * Method to retrieve the semantic value of the last scanned token.
   * @return the semantic value of the last scanned token.
   */
  public Object getLVal() {
    Object result = yylval;
    yylval = null;
    return result;
  }

  /**
   * Entry point for the scanner.  Returns the token identifier corresponding
   * to the next token and prepares to return the semantic value
   * and beginning/ending positions of the token.
   *
   * This is a wrapper around the internal yylex method to collect tokens such
   * as comments, whitespace, etc. to use them later on in the compiler's
   * output. Also, other necessary functionality can be put her (extracting
   * the full input text?)
   *
   * @return the token identifier corresponding to the next token.
   */
  public int yylex() throws java.io.IOException {
    int result = yylex_internal();
    return result;
  }

  /**
   * Entry point for error reporting.  Emits an error
   * referring to the given location in a user-defined way.
   *
   * @param loc The location of the element to which the
   *                error message is related
   * @param msg The string for the error message.
   */
  public void yyerror (SrgsAbnf.Location loc, String msg) {
    err.println(loc.begin + ": error: " + msg);
  }

  public void setOrigin(String s) { origin = s; }

  /** Return the collected tokens */
  public LinkedList<Token> getTokens() { return tokens; }

  /** Return the collected comment tokens */
  public LinkedList<Token> getCommentTokens() { return commentTokens; }

  /** Add a non-comment and non-whitespace token */
  public int token(int token) {
    tokens.add(new Token(yytext(), getStartPos(), getEndPos()));
    return token;
  }

  /** Add a comment or whitespace token */
  public void addComment(String comment) {
    commentTokens.add(new Token(comment, getStartPos(), getEndPos()));
  }

  public void begin_repeat() { yybegin(repeat); }

  public void begin_header() { yybegin(header); }
  
  // RuleReference with URI with media
  private void treatUriWithMedia() {
    String uri_with_media = yytext();
    int uriend = uri_with_media.indexOf(">~<");
    String base = uri_with_media.substring(1, uriend);
    String type = uri_with_media.substring(uriend + 3, uri_with_media.length() - 1);
    yylval = new String[]{ base, type };
  }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = ({LineTerminator} | [ \t\f])+

Letter = [:letter:]
Digit = [:digit:]
NameChar = ({Letter}|{Digit}|[-._:])
ConstrainedChar = ({Letter}|{Digit}|[_])

Number = ({Digit}+|{Digit}+"."{Digit}*|{Digit}*"."{Digit}+)

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} |
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"
//"
%s header
%s ruleref
%s tag
%s tag2
%s repeat

%%

/* Done before */
"\xef\xbb\xbf"  { //fputc('\xef', out); fputc('\xbb', out); fputc('\xbf', out);
                  // UTF8 byte order mark
                }

\#ABNF\x201.0(\x20{NameChar}+)?;\r?\n  {
  yybegin(header);
  yylval = yytext().substring(yytext().indexOf(' ') + 1);
  return SelfIdentHeader;
}
/* */

{Comment} { addComment(yytext()); }

<header> {
"language"   { return DECL_LANG; }
"root" " "+ "$" ({Letter}|'_'){ConstrainedChar}* {
  String s = yytext();
  int dollar = s.indexOf("$");
  yylval = yytext().substring(dollar+1);
  return DECL_ROOT;
}
"base"         { return DECL_BASE; }
"tag-format"   { return DECL_TAG_FORMAT ; }
"lexicon"      { return DECL_LEXICON ; }
"mode" " "+ ("voice"|"dtmf") {
  String[] chunks = yytext().split("  *");
  yylval=chunks[1];
  return DECL_MODE ;
}

"http-equiv"   { return DECL_HTTP_EQUIV ; }
"meta"         { return DECL_META ; }
"is"             { return DECL_IS ; }
;                { return ';' ; }
"$"              { yybegin(ruleref); }
}

<ruleref> {
("NULL"|"VOID"|"GARBAGE") {
  switch (yytext()) {
    case "NULL": yylval = RuleSpecial.NULL; break;
    case "VOID": yylval = RuleSpecial.VOID; break;
    case "GARBAGE": yylval = RuleSpecial.GARBAGE; break;
  }
  yybegin(YYINITIAL);
  return specialRuleReference;
}

({Letter}|'_'){ConstrainedChar}* {
  yylval = yytext();
  yybegin(YYINITIAL);
  return RuleName;
}
} // end <ruleref>

<YYINITIAL> "$" { yybegin(ruleref); }

<header>{
"<"[^>]+">" {
  yylval = new String[]{ yytext().substring(1, yylength() - 1)};
  // RuleReference with URI without media
  return URI;
}

"<"[^>]+">~<"[^>]+">" {
  treatUriWithMedia();
  return URI;
}
}

<ruleref>{
"<"[^>]+">" {
  yylval = new String[]{ yytext().substring(1, yylength() - 1)};
  // RuleReference with URI without media
  yybegin(YYINITIAL);
  return URI;
}

"<"[^>]+">~<"[^>]+">" {
  treatUriWithMedia();
  yybegin(YYINITIAL);
  return URI;
}
}


"/"{Number}"/" {
  yylval = yytext().substring(1, yylength() - 1);
  return SlashNum ;
}

"<" { yybegin(repeat); return '<'; }

<repeat> [0-9]+("-"[0-9]*)? {
  yybegin(YYINITIAL);
  yylval = yytext();
  return Repeat;
}

"{"           {yybegin(tag) ; return TagStart; }
<tag>[^}]*    { yylval = yytext(); return Tag; }
<tag>"}"      { yybegin(YYINITIAL) ; return TagEnd; }

"{!{"                            { yybegin(tag2); return TagStart; }
<tag2>([^}]*|[^}]*"}"[^!]|[^}]*"}!"[^}])*  { yylval = yytext(); return Tag; }
<tag2>"}!}"                      { yybegin(YYINITIAL); return TagEnd; }

/*
<tag,tag2>\$({ConstrainedChar}+)("."({ConstrainedChar}+))*  { // PathSpec
  yylval = yytext().substring(1);
  return Path;
}

<tag,tag2>{ConstrainedChar}+ {
  yylval = yytext();
  return SimpleToken;
}
*/

("private"|"public")/{WhiteSpace}+"$"  {
  yybegin(YYINITIAL);
  return ((yytext().charAt(1) == 'r') ? Private : Public) ;
}

<header,YYINITIAL>({NameChar})+ {
  yylval = yytext();
  return Nmtoken;
}

<tag,tag2,YYINITIAL>[=;\(\)\[\]!|<>] { return yytext().charAt(0); }

<YYINITIAL,header,tag,tag2>{
  {WhiteSpace} { addComment(yytext()); }

  (\'[^']*\')|(\"[^\"]*\") { // 'for syntax highlighting''
    yylval = yytext().substring(1, yylength()-1) ;
    return QuotedCharacters ;
  }
}

/* error fallback */
[^] {
    return IllegalCharacter;
}

<<EOF>>  { return SrgsAbnf.Lexer.EOF; }
