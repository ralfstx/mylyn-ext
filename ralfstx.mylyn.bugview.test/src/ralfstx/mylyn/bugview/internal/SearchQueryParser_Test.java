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

    Collection<TaskFilter> result = parser.parse( "" );

    assertEquals( 0, result.size() );
  }

  @Test
  public void testParse_withSingleString() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<TaskFilter> result = parser.parse( "foo" );

    ArrayList<String> expected = expectFilters( "nameOrId:foo" );
    assertFiltersEquals( expected, result );
  }

  @Test
  public void testParse_withTwoStrings() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<TaskFilter> result = parser.parse( "foo bar" );

    ArrayList<String> expected = expectFilters( "nameOrId:foo", "nameOrId:bar" );
    assertFiltersEquals( expected, result );
  }

  @Test
  public void testParse_stripsWhitespace() throws Exception {
    SearchQueryParser parser = new SearchQueryParser();

    Collection<TaskFilter> result = parser.parse( " foo\t  " );

    ArrayList<String> expected = expectFilters( "nameOrId:foo" );
    assertFiltersEquals( expected, result );
  }

  private static ArrayList<String> expectFilters( String... expected ) {
    ArrayList<String> result = new ArrayList<String>();
    for( String string : expected ) {
      result.add( string );
    }
    return result;
  }

  private static void assertFiltersEquals( Collection<String> expected,
      Collection<TaskFilter> actual )
  {
    ArrayList<String> actualStrings = new ArrayList<String>();
    for( TaskFilter filter : actual ) {
      actualStrings.add( filter.toString() );
    }
    for( String expectedString : expected ) {
      if( !actualStrings.contains( expectedString ) ) {
        String joinedActual = "[ " + join( actualStrings ) + " ]";
        fail( "Expected \"" + expectedString + "\" not contained in " + joinedActual );
      }
    }
    for( String actualString : actualStrings ) {
      if( !expected.contains( actualString ) ) {
        String joinedActual = "[ " + join( actualStrings ) + " ]";
        fail( "Unexpected \"" + actualString + "\" contained in " + joinedActual );
      }
    }
  }

  private static String join( ArrayList<String> strings ) {
    StringBuilder buffer = new StringBuilder();
    for( String string : strings ) {
      if( buffer.length() > 0 ) {
        buffer.append( ", " );
      }
      buffer.append( '"' );
      buffer.append( string );
      buffer.append( '"' );
    }
    return buffer.toString();
  }
}
