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
import org.junit.Test;


public class AndFilter_Test {

  @Test( expected = NullPointerException.class )
  public void testMatches_null() throws Exception {
    AndFilter filter = new AndFilter();

    filter.matches( null );
  }

  @Test
  public void testMatches_withoutComponents() throws Exception {
    AndFilter filter = new AndFilter();

    assertTrue( filter.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void testMatches_withSingleComponent() throws Exception {
    AndFilter filter = new AndFilter( new NameOrIdFilter( "foo" ) );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "", "bar" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "", "foo" ) ) );
  }

  @Test
  public void testMatches_withMultipleComponents() throws Exception {
    AndFilter filter = new AndFilter( new NameOrIdFilter( "foo" ), new NameOrIdFilter( "bar" ) );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "", "bar" ) ) );
    assertFalse( filter.matches( mockTaskWithSummaryAndId( "", "foo" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "", "foo bar" ) ) );
  }

  @Test
  public void testToString() throws Exception {
    AndFilter filter = new AndFilter( new NameOrIdFilter( "foo" ), new NameOrIdFilter( "bar" ) );

    assertEquals( "and(nameOrId(\"foo\"),nameOrId(\"bar\"))", filter.toString() );
  }

  private static ITask mockTaskWithSummaryAndId( String id, String summary ) {
    ITask task = mock( ITask.class );
    when( task.getTaskId() ).thenReturn( id );
    when( task.getSummary() ).thenReturn( summary );
    return task;
  }
}
