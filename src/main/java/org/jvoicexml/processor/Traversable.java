package org.jvoicexml.processor;

public interface Traversable<S> {

  public void preorder(TreeWalker<S> acceptor);

}
