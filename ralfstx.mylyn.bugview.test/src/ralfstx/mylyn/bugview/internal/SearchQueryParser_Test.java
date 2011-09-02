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

    TaskMatcher result = parser.parse( "" );

    assertMatcherEquals( new AndMatcher(), result );
  }

  @Test
  public void testParse_withSingleString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskMatcher result = parser.parse( "foo" );

    AndMatcher expected = new AndMatcher( new NameOrIdMatcher( "foo" ) );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_withTwoStrings() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskMatcher result = parser.parse( "foo bar" );

    AndMatcher expected = new AndMatcher( new NameOrIdMatcher( "foo" ),
                                          new NameOrIdMatcher( "bar" ) );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_stripsWhitespace() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskMatcher result = parser.parse( " foo\t  " );

    AndMatcher expected = new AndMatcher( new NameOrIdMatcher( "foo" ) );
    assertMatcherEquals( expected, result );
  }

  private static void assertMatcherEquals( TaskMatcher expected, TaskMatcher actual ) {
    assertEquals( expected.toString(), actual.toString() );
  }
}
