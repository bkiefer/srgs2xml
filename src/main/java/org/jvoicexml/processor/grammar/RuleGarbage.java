/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 63 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.processor.grammar;

//Comp. 2.0.6

/** A $GARBAGE token behaves like "$$.*"<0->
 *
 *  It's weight is by default bigger that all other items to suppress garbage
 *  where possible.
 */
public class RuleGarbage extends RuleJunk {

  protected RuleGarbage() {
    super("GARBAGE");
  }

  /** Behaves like a count(0, infinity) */
  @Override
  public Boolean isActive(int dot) {
    return true;
  }

  /**
   * dot is the number of repetitions already covered
   */
  @Override
  public int nextSlot(int dot) {
    return ++dot;
  }

}
