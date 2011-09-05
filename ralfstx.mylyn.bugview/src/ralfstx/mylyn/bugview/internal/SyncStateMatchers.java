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
package ralfstx.mylyn.bugview.internal;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Description;


public abstract class SyncStateMatchers {

  public static TaskMatcher isIncoming() {
    return new TaskMatcher() {

      @Override
      protected boolean matches( ITask task ) {
        return task.getSynchronizationState().isIncoming();
      }

      public void describeTo( Description description ) {
        description.appendText( "INCOMING" );
      }

    };
  }

  public static TaskMatcher isOutgoing() {
    return new TaskMatcher() {

      @Override
      protected boolean matches( ITask task ) {
        return task.getSynchronizationState().isOutgoing();
      }

      public void describeTo( Description description ) {
        description.appendText(  "OUTGOING"  );
      }

    };
  }

}
