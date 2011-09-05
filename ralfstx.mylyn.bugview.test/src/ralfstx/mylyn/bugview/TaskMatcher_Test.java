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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Description;
import org.junit.Test;

import ralfstx.mylyn.bugview.TaskMatcher;


public class TaskMatcher_Test {

  @Test
  public void matchesOnlyITask() throws Exception {
    TaskMatcher matcher = new TaskMatcher() {

      public void describeTo( Description description ) {
        description.appendText( "TRUE" );
      }

      @Override
      protected boolean matches( ITask task ) {
        return true;
      }
    };

    assertFalse( matcher.matches( new Object() ) );
    assertTrue( matcher.matches( mock( ITask.class ) ) );
  }

}
