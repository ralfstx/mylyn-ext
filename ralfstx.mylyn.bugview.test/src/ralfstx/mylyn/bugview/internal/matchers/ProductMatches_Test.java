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


public class ProductMatches_Test {

  @Test
  public void matches_onlyExactName() throws Exception {
    ProductMatches matcher = new ProductMatches( "foo" );

    assertTrue( matcher.matches( mockTaskWithProduct( "foo" ) ) );
    assertFalse( matcher.matches( mockTaskWithProduct( "bar" ) ) );
    assertFalse( matcher.matches( mockTaskWithProduct( "foobar" ) ) );
    assertFalse( matcher.matches( mockTaskWithProduct( "barfoo" ) ) );
  }

  @Test
  public void matches_withoutName() throws Exception {
    ProductMatches matcher = new ProductMatches( "" );

    assertTrue( matcher.matches( mockTaskWithProduct( "" ) ) );
    assertTrue( matcher.matches( mockTaskWithProduct( null ) ) );
    assertFalse( matcher.matches( mockTaskWithProduct( "foo" ) ) );
  }

  @Test
  public void matches_whenProductIsNull() throws Exception {
    ProductMatches matcher = new ProductMatches( "foo" );

    assertFalse( matcher.matches( mockTaskWithProduct( null ) ) );
  }

  @Test
  public void matches_caseInsensitive() throws Exception {
    ProductMatches matcher = new ProductMatches( "Foo" );

    assertTrue( matcher.matches( mockTaskWithProduct( "foO" ) ) );
  }

  @Test
  public void matches_caseInsensitiveId_ignoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    boolean result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      ProductMatches matcher = new ProductMatches( "iI" );
      result = matcher.matches( mockTaskWithProduct( "Ii" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result );
  }

  @Test
  public void description() throws Exception {
    assertEquals( "product(\"\")", StringDescription.toString( new ProductMatches( "" ) ) );
    assertEquals( "product(\"foo\")", StringDescription.toString( new ProductMatches( "foo" ) ) );
  }

}
