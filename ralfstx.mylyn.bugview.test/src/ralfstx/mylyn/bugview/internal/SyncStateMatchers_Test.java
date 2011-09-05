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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.SynchronizationState;
import org.hamcrest.StringDescription;
import org.junit.Test;


public class SyncStateMatchers_Test {

  @Test
  public void testIncomingMatches() throws Exception {
    TaskMatcher matcher = SyncStateMatchers.isIncoming();

    assertTrue( matcher.matches( mockTaskWithSyncState( SynchronizationState.INCOMING ) ) );
    assertTrue( matcher.matches( mockTaskWithSyncState( SynchronizationState.CONFLICT ) ) );
    assertFalse( matcher.matches( mockTaskWithSyncState( SynchronizationState.OUTGOING ) ) );
    assertFalse( matcher.matches( mockTaskWithSyncState( SynchronizationState.SYNCHRONIZED ) ) );
  }

  @Test
  public void testOutgoingMatches() throws Exception {
    TaskMatcher matcher = SyncStateMatchers.isOutgoing();

    assertTrue( matcher.matches( mockTaskWithSyncState( SynchronizationState.OUTGOING ) ) );
    assertTrue( matcher.matches( mockTaskWithSyncState( SynchronizationState.CONFLICT ) ) );
    assertFalse( matcher.matches( mockTaskWithSyncState( SynchronizationState.INCOMING ) ) );
    assertFalse( matcher.matches( mockTaskWithSyncState( SynchronizationState.SYNCHRONIZED ) ) );
  }

  @Test
  public void testDescription() throws Exception {
    assertEquals( "INCOMING", StringDescription.toString( SyncStateMatchers.isIncoming() ) );
    assertEquals( "OUTGOING", StringDescription.toString( SyncStateMatchers.isOutgoing() ) );
  }

  private static ITask mockTaskWithSyncState( SynchronizationState syncState ) {
    ITask task = mock( ITask.class );
    when( task.getSynchronizationState() ).thenReturn( syncState );
    return task;
  }

}
