package org.jvoicexml.processor.grammar;

public class Meta {
  public String key, value;
  boolean http_equiv;

  public Meta(String s1, String s2, boolean http_equiv){
    this.key = s1;
    this.value = s2;
    this.http_equiv = http_equiv;
  }
  
  public String toString() {
    return "[ " + this.key + " : " + this.value + " ]" + (http_equiv ? "*" : ""); 
  }
}
