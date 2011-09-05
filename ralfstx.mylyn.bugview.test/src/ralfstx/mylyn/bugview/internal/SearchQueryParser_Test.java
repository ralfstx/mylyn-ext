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

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;


@SuppressWarnings( "unchecked" )
public class SearchQueryParser_Test {

  @Test( expected = NullPointerException.class )
  public void testParse_withNull() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    parser.parse( null );
  }

  @Test
  public void testParse_withEmptyString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( "" );

    Matcher<ITask> expected = CoreMatchers.anything();
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_withSingleString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( "foo" );

    Matcher<ITask> expected = CoreMatchers.allOf( new NameOrIdMatcher( "foo" ) );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_withTwoStrings() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( "foo bar" );

    Matcher<ITask> expected = CoreMatchers.allOf( new NameOrIdMatcher( "foo" ),
                                                  new NameOrIdMatcher( "bar" ) );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_stripsWhitespace() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( " foo\t  " );

    Matcher<ITask> expected = CoreMatchers.allOf( new NameOrIdMatcher( "foo" ) );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_incoming() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( ":incoming" );

    Matcher<ITask> expected = CoreMatchers.allOf( SyncStateMatchers.isIncoming() );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_outgoing() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( ":outgoing" );

    Matcher<ITask> expected = CoreMatchers.allOf( SyncStateMatchers.isOutgoing() );
    assertMatcherEquals( expected, result );
  }

  @Test
  public void testParse_mixed() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Matcher<ITask> result = parser.parse( "foo :incoming" );

    Matcher<ITask> expected = CoreMatchers.allOf( new NameOrIdMatcher( "foo" ),
                                                  SyncStateMatchers.isIncoming() );
    assertMatcherEquals( expected, result );
  }

  private static void assertMatcherEquals( Matcher<ITask> expected, Matcher<ITask> actual ) {
    assertEquals( expected.toString(), actual.toString() );
  }
}
