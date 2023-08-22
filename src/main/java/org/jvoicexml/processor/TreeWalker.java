package org.jvoicexml.processor;

interface TreeWalker<T> {
  void enter(T node, boolean leaf);

  void leave(T node, boolean leaf);
}