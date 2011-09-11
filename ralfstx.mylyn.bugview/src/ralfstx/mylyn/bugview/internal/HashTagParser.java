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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HashTagParser {

  public List<String> parse( String string ) {
    if( string == null ) {
      throw new NullPointerException( "Null parameter: string" );
    }
    ArrayList<String> result = new ArrayList<String>();
    int index = 0;
    while( index != -1 ) {
      index = string.indexOf( '#', index );
      if( index != -1 ) {
        index++;
        String hashtag = findHashTag( string, index );
        if( hashtag.length() != 0 ) {
          result.add( hashtag );
        }
      }
    }
    return result;
  }

  private static String findHashTag( String string, int start ) {
    int index = start;
    while( index < string.length() && isPermittedCharacter( string.charAt( index ) ) ) {
      index++;
    }
    return string.substring( start, index ).toLowerCase( Locale.ENGLISH );
  }

  private static boolean isPermittedCharacter( char ch ) {
    return Character.isLetter( ch ) || Character.isDigit( ch ) || ch == '_' || ch == '-';
  }

}
