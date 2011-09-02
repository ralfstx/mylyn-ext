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

import java.util.Locale;

import org.eclipse.mylyn.tasks.core.ITask;
import org.junit.Test;


public class NameOrIdFilter_Test {

  @Test
  public void testMatches_withEmptyString() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "" );

    assertTrue( filter.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void testMatches_withExactSummary() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "foo" );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "23", "bar" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void testMatches_withSummarySubString() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "bar" );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23", "foobar" ) ) );
  }

  @Test
  public void testMatches_withExactId() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "23" );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertFalse( filter.matches( mockTaskWithSummaryAndId( "42", "" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23", "" ) ) );
  }

  @Test
  public void testMatches_withIdPrefix() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "23" );

    assertFalse( filter.matches( mockTaskWithSummaryAndId( "123", "" ) ) );
    assertTrue( filter.matches( mockTaskWithSummaryAndId( "230", "" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveId() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "23aB" );

    assertTrue( filter.matches( mockTaskWithSummaryAndId( "23Ab", "" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveSummary() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "fooBAR" );

    assertTrue( filter.matches( mockTaskWithSummaryAndId( "", "Foobar" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveId_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      NameOrIdFilter filter = new NameOrIdFilter( "23iI" );
      result = filter.matches( mockTaskWithSummaryAndId( "23Ii", "" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void testMatches_caseInsensitiveSummary_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      NameOrIdFilter filter = new NameOrIdFilter( "Int min" );
      result = filter.matches( mockTaskWithSummaryAndId( "", "int MIN" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void testToString() throws Exception {
    NameOrIdFilter filter = new NameOrIdFilter( "foo" );
    assertEquals( "nameOrId(\"foo\")", filter.toString() );
  }

  private static ITask mockTaskWithSummaryAndId( String id, String summary ) {
    ITask task = mock( ITask.class );
    when( task.getTaskId() ).thenReturn( id );
    when( task.getSummary() ).thenReturn( summary );
    return task;
  }
}
