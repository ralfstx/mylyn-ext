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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Locale;

import org.junit.Test;


public class SearchQueryParser_Test {

  @Test( expected = NullPointerException.class )
  public void testParse_withNull() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    parser.parse( null );
  }

  @Test
  public void testParse_withEmptyString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<String> result = parser.parse( "" );

    assertEquals( 0, result.size() );
  }

  @Test
  public void testParse_withSingleString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<String> result = parser.parse( "foo" );

    assertEquals( 1, result.size() );
    assertTrue( result.contains( "foo" ) );
  }

  @Test
  public void testParse_withTwoStrings() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<String> result = parser.parse( "foo bar" );

    assertEquals( 2, result.size() );
    assertTrue( result.contains( "foo" ) );
    assertTrue( result.contains( "bar" ) );
  }

  @Test
  public void testParse_stripsWhitespace() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<String> result = parser.parse( " foo\t  " );

    assertTrue( result.contains( "foo" ) );
  }

  @Test
  public void testParse_convertsStringsToLowerCase() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<String> result = parser.parse( "Foo" );

    assertTrue( result.contains( "foo" ) );
  }

  @Test
  public void testParse_lowerCaseIgnoresDefaultLocale() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();
    Locale defaultLocale = Locale.getDefault();
    Collection<String> result;

    try {
      Locale.setDefault( new Locale( "tr" ) );
      result = parser.parse( "I" );
    } finally {
      Locale.setDefault( defaultLocale );
    }

    assertTrue( result.contains( "i" ) );
  }

}
