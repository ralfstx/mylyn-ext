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

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.BaseMatcher;


public abstract class TaskMatcher extends BaseMatcher<ITask> {

  public boolean matches( Object item ) {
    if( item instanceof ITask ) {
      return matches( (ITask)item );
    }
    return false;
  }

  protected abstract boolean matches( ITask task );

}
