/*******************************************************************************
 * Copyright (c) 2011 Ralf Sternberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package ralfstx.mylyn.bugview;

import ralfstx.mylyn.bugview.internal.matchers.ProductMatches;
import ralfstx.mylyn.bugview.internal.matchers.IsCompleted;
import ralfstx.mylyn.bugview.internal.matchers.IsEnhancement;
import ralfstx.mylyn.bugview.internal.matchers.IsIncoming;
import ralfstx.mylyn.bugview.internal.matchers.IsOutgoing;


public final class TaskMatchers {

  private TaskMatchers() {
    // prevent instantiation
  }

  public static TaskMatcher isIncoming() {
    return new IsIncoming();
  }

  public static TaskMatcher isOutgoing() {
    return new IsOutgoing();
  }

  public static TaskMatcher isCompleted() {
    return new IsCompleted();
  }

  public static TaskMatcher isEnhancement() {
    return new IsEnhancement();
  }

  public static TaskMatcher productMatches( String name ) {
    return new ProductMatches( name );
  }

}
