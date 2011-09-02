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

import java.util.ArrayList;
import java.util.Collection;

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

    TaskFilter result = parser.parse( "" );

    assertFilterEquals( new AndFilter(), result );
  }

  @Test
  public void testParse_withSingleString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskFilter result = parser.parse( "foo" );

    AndFilter expected = new AndFilter( new NameOrIdFilter( "foo" ) );
    assertFilterEquals( expected, result );
  }

  @Test
  public void testParse_withTwoStrings() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskFilter result = parser.parse( "foo bar" );

    AndFilter expected = new AndFilter( new NameOrIdFilter( "foo" ), new NameOrIdFilter( "bar" ) );
    assertFilterEquals( expected, result );
  }

  @Test
  public void testParse_stripsWhitespace() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    TaskFilter result = parser.parse( " foo\t  " );

    AndFilter expected = new AndFilter( new NameOrIdFilter( "foo" ) );
    assertFilterEquals( expected, result );
  }

  private static Collection<TaskFilter> expectFilters( TaskFilter... expected ) {
    ArrayList<TaskFilter> result = new ArrayList<TaskFilter>();
    for( TaskFilter filter : expected ) {
      result.add( filter );
    }
    return result;
  }

  private static void assertFilterEquals( TaskFilter expected, TaskFilter actual ) {
    assertEquals( expected.toString(), actual.toString() );
  }
}
