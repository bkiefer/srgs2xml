package org.jvoicexml.processor;

public interface TreeStrategy {

  public <T extends TreeWalker> void preorder(T acceptor);

}
