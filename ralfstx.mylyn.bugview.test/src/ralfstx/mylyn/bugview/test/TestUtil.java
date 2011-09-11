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
package ralfstx.mylyn.bugview.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.mylyn.internal.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.SynchronizationState;
import org.hamcrest.Matcher;


@SuppressWarnings( "restriction" )
public final class TestUtil {

  private TestUtil() {
    // prevent instantiation
  }

  public static ITask mockTaskWithSummaryAndId( String id, String summary ) {
    ITask task = mock( ITask.class );
    when( task.getTaskId() ).thenReturn( id );
    when( task.getSummary() ).thenReturn( summary );
    return task;
  }

  public static ITask mockTaskWithSyncState( SynchronizationState syncState ) {
    ITask task = mock( ITask.class );
    when( task.getSynchronizationState() ).thenReturn( syncState );
    return task;
  }

  public static ITask mockCompletedTask() {
    ITask task = mock( ITask.class );
    when( Boolean.valueOf( task.isCompleted() ) ).thenReturn( Boolean.TRUE );
    return task;
  }

  public static ITask mockUncompletedTask() {
    ITask task = mock( ITask.class );
    when( Boolean.valueOf( task.isCompleted() ) ).thenReturn( Boolean.FALSE );
    return task;
  }

  public static ITask mockTaskWithSeverity( String severity ) {
    ITask task = mock( ITask.class );
    when( task.getAttribute( "bug_severity" ) ).thenReturn( severity );
    return task;
  }

  public static ITask mockTaskWithProduct( String name ) {
    ITask task = mock( ITask.class );
    when( task.getAttribute( "product" ) ).thenReturn( name );
    return task;
  }

  public static ITask mockTaskWithOwner( String name ) {
    ITask task = mock( ITask.class );
    when( task.getOwner() ).thenReturn( name );
    return task;
  }

  public static ITask mockTaskWithNotes( String text ) {
    AbstractTask task = mock( AbstractTask.class );
    when( task.getNotes() ).thenReturn( text );
    return task;
  }

  public static void assertMatcherEquals( Matcher<ITask> expected, Matcher<ITask> actual ) {
    assertEquals( expected.toString(), actual.toString() );
  }

}
