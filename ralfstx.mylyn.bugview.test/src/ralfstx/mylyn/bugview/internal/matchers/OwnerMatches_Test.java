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


public class OwnerMatches_Test {

  @Test
  public void matches() throws Exception {
    OwnerMatches matcher = new OwnerMatches( "foo" );

    assertTrue( matcher.matches( mockTaskWithOwner( "foo" ) ) );
    assertFalse( matcher.matches( mockTaskWithOwner( "bar" ) ) );
  }

  @Test
  public void matches_prefix() throws Exception {
    OwnerMatches matcher = new OwnerMatches( "foo" );

    assertTrue( matcher.matches( mockTaskWithOwner( "foobar" ) ) );
    assertFalse( matcher.matches( mockTaskWithOwner( "barfoo" ) ) );
  }

  @Test
  public void matches_withoutName() throws Exception {
    OwnerMatches matcher = new OwnerMatches( "" );

    assertTrue( matcher.matches( mockTaskWithOwner( "" ) ) );
    assertTrue( matcher.matches( mockTaskWithOwner( null ) ) );
    assertFalse( matcher.matches( mockTaskWithOwner( "foo" ) ) );
  }

  @Test
  public void matches_whenOwnerIsNull() throws Exception {
    OwnerMatches matcher = new OwnerMatches( "foo" );

    assertFalse( matcher.matches( mockTaskWithOwner( null ) ) );
  }

  @Test
  public void matches_caseInsensitive() throws Exception {
    OwnerMatches matcher = new OwnerMatches( "Foo" );

    assertTrue( matcher.matches( mockTaskWithOwner( "foO" ) ) );
  }

  @Test
  public void matches_caseInsensitiveId_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      OwnerMatches matcher = new OwnerMatches( "iI" );
      result = matcher.matches( mockTaskWithOwner( "Ii" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void description() throws Exception {
    assertEquals( "ownerMatches(\"foo\")", StringDescription.toString( new OwnerMatches( "foo" ) ) );
  }

}
