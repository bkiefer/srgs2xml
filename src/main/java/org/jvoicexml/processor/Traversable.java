package org.jvoicexml.processor;

public interface Traversable {

  public <T extends TreeWalker> void preorder(T acceptor);

}
