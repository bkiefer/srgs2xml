package org.jvoicexml.processor.srgs.abnf;

public class Token {
  private String s;
  private Position start, end;

  public Token(String s, Position start, Position  end) {
    this.s = s;
    this.start = start;
    this.end = end;
  }

  public String getText() { return this.s; }

  public Position getStart() { return start; }

  public Position getEnd() { return end; }

  public String toString() {
    return '<' + s + '>' + '[' + start + '|' + end + ']';
  }
}