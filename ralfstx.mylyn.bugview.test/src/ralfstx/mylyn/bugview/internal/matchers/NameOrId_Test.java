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

import static org.junit.Assert.*;
import static ralfstx.mylyn.bugview.test.TestUtil.*;

import java.util.Locale;

import org.hamcrest.StringDescription;
import org.junit.Test;


public class NameOrId_Test {

  @Test
  public void matches_withEmptyString() throws Exception {
    NameOrId matcher = new NameOrId( "" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void matches_withExactSummary() throws Exception {
    NameOrId matcher = new NameOrId( "foo" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "23", "bar" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
  }

  @Test
  public void matches_withSummarySubString() throws Exception {
    NameOrId matcher = new NameOrId( "bar" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "23", "foo" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "foobar" ) ) );
  }

  @Test
  public void matches_withExactId() throws Exception {
    NameOrId matcher = new NameOrId( "23" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "", "" ) ) );
    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "42", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23", "" ) ) );
  }

  @Test
  public void matches_withIdPrefix() throws Exception {
    NameOrId matcher = new NameOrId( "23" );

    assertFalse( matcher.matches( mockTaskWithSummaryAndId( "123", "" ) ) );
    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "230", "" ) ) );
  }

  @Test
  public void matches_caseInsensitiveId() throws Exception {
    NameOrId matcher = new NameOrId( "23aB" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "23Ab", "" ) ) );
  }

  @Test
  public void matches_caseInsensitiveSummary() throws Exception {
    NameOrId matcher = new NameOrId( "fooBAR" );

    assertTrue( matcher.matches( mockTaskWithSummaryAndId( "", "Foobar" ) ) );
  }

  @Test
  public void matches_caseInsensitiveId_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      NameOrId matcher = new NameOrId( "23iI" );
      result = matcher.matches( mockTaskWithSummaryAndId( "23Ii", "" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void matches_caseInsensitiveSummary_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      NameOrId matcher = new NameOrId( "Int min" );
      result = matcher.matches( mockTaskWithSummaryAndId( "", "int MIN" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void description() throws Exception {
    NameOrId matcher = new NameOrId( "foo" );

    assertEquals( "nameOrId(\"foo\")", StringDescription.toString( matcher ) );
  }

}
