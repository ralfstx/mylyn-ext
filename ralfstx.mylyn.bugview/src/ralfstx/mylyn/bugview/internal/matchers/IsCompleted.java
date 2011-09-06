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
package ralfstx.mylyn.bugview.internal.matchers;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Description;

import ralfstx.mylyn.bugview.TaskMatcher;

public class IsCompleted extends TaskMatcher {

  @Override
  protected boolean matches( ITask task ) {
    return task.isCompleted();
  }

  public void describeTo( Description description ) {
    description.appendText( "isCompleted" );
  }

}
