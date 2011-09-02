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


public class NameOrIdMatcher_Test {

  @Test
  public void testMatches_withEmptyString() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void testMatches_withExactSummary() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "foo" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "23", "bar" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void testMatches_withSummarySubString() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "bar" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foobar" ) ) );
  }

  @Test
  public void testMatches_withExactId() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "23" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "42", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "" ) ) );
  }

  @Test
  public void testMatches_withIdPrefix() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "23" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "123", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "230", "" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveId() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "23aB" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23Ab", "" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveSummary() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "fooBAR" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "", "Foobar" ) ) );
  }

  @Test
  public void testMatches_caseInsensitiveId_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      NameOrIdMatcher matcher = new NameOrIdMatcher( "23iI" );
      result = matcher.matches( mockTaskWithSummaryAndId( "23Ii", "" ) );
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
      NameOrIdMatcher matcher = new NameOrIdMatcher( "Int min" );
      result = matcher.matches( mockTaskWithSummaryAndId( "", "int MIN" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void testToString() throws Exception {
    NameOrIdMatcher matcher = new NameOrIdMatcher( "foo" );
    assertEquals( "nameOrId(\"foo\")", matcher.toString() );
  }

  private static ITask mockTaskWithSummaryAndId( String id, String summary ) {
    ITask task = mock( ITask.class );
    when( task.getTaskId() ).thenReturn( id );
    when( task.getSummary() ).thenReturn( summary );
    return task;
  }
}
