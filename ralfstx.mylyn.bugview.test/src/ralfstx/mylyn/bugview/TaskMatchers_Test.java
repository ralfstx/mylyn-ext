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

import static ralfstx.mylyn.bugview.test.TestUtil.*;

import org.junit.Test;

import ralfstx.mylyn.bugview.internal.matchers.IsCompleted;
import ralfstx.mylyn.bugview.internal.matchers.IsEnhancement;
import ralfstx.mylyn.bugview.internal.matchers.IsIncoming;
import ralfstx.mylyn.bugview.internal.matchers.IsOutgoing;


public class TaskMatchers_Test {

  @Test
  public void isIncoming() throws Exception {
    assertMatcherEquals( new IsIncoming(), TaskMatchers.isIncoming() );
  }

  @Test
  public void isOutgoing() throws Exception {
    assertMatcherEquals( new IsOutgoing(), TaskMatchers.isOutgoing() );
  }

  @Test
  public void isCompleted() throws Exception {
    assertMatcherEquals( new IsCompleted(), TaskMatchers.isCompleted() );
  }

  @Test
  public void isEnhancement() throws Exception {
    assertMatcherEquals( new IsEnhancement(), TaskMatchers.isEnhancement() );
  }

}
