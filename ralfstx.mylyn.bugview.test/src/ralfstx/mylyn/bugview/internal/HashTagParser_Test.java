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
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


public class HashTagParser_Test {

  private HashTagParser parser;

  @Before
  public void setup() {
    parser = new HashTagParser();
  }

  @Test( expected = NullPointerException.class )
  public void parse_withNull() throws Exception {
    parser.parse( null );
  }

  @Test
  public void parse_withEmptyString() throws Exception {
    assertEquals( 0, parser.parse( "" ).size() );
  }

  @Test
  public void parse_withSingleString() throws Exception {
    assertEquals( createList( "foo" ), parser.parse( "#foo" ) );
  }

 @Test
  public void parse_withTwoStrings() throws Exception {
    assertEquals( createList( "foo", "bar" ), parser.parse( "#foo #bar" ) );
  }

  @Test
  public void parse_stripsWhitespace() throws Exception {
    assertEquals( createList( "foo" ), parser.parse( " #foo\t  " ) );
  }

  @Test
  public void parse_tagWithNonAsciiCharacters() throws Exception {
    assertEquals( createList( "föö" ), parser.parse( "#föö" ) );
  }

  @Test
  public void parse_tagWithLetters() throws Exception {
    assertEquals( createList( "f00" ), parser.parse( "#f00" ) );
    assertEquals( createList( "23" ), parser.parse( "#23" ) );
  }

  @Test
  public void parse_permittedCharacters() throws Exception {
    assertEquals( createList( "foo_bar" ), parser.parse( "#foo_bar" ) );
    assertEquals( createList( "foo-bar" ), parser.parse( "#foo-bar" ) );
  }

  @Test
  public void parse_forbiddenCharacters() throws Exception {
    assertEquals( createList( "foo" ), parser.parse( "#foo.bar" ) );
    assertEquals( createList( "foo" ), parser.parse( "#foo,bar" ) );
    assertEquals( createList( "foo" ), parser.parse( "#foo!bar" ) );
    assertEquals( createList( "foo" ), parser.parse( "#foo?bar" ) );
    assertEquals( createList( "foo" ), parser.parse( "#foo|bar" ) );
    assertEquals( createList( "foo" ), parser.parse( "#foo/bar" ) );
  }

  @Test
  public void parse_singleHashCharacters() throws Exception {
    assertEquals( createList(), parser.parse( "#" ) );
    assertEquals( createList( "foo" ), parser.parse( "# #foo #" ) );
  }

  @Test
  public void parse_returnsLowerCase() throws Exception {
    assertEquals( createList( "foo" ), parser.parse( "#Foo" ) );
  }

  @Test
  public void parse_lowerCaseIgnoresDefaultLocale() throws Exception {
    Locale defaultLocale = Locale.getDefault();
    try {
      Locale.setDefault( new Locale( "tr" ) );
      assertEquals( createList( "i" ), parser.parse( "#I" ) );
    } finally {
      Locale.setDefault( defaultLocale );
    }
  }

  private static List<String> createList( String... strings ) {
    ArrayList<String> result = new ArrayList<String>();
    for( String string : strings ) {
      result.add( string );
    }
    return result;
  }

}
