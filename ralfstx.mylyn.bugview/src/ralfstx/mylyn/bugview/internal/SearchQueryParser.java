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


public class SearchQueryParser {

  public TaskMatcher parse( String query ) {
    if( query == null ) {
      throw new NullPointerException( "null parameter: query" );
    }
    ArrayList<TaskMatcher> result = new ArrayList<TaskMatcher>();
    String[] parts = query.split( "\\s+" );
    for( String part : parts ) {
      if( part.length() > 0 ) {
        result.add( new NameOrIdMatcher( part ) );
      }
    }
    TaskMatcher[] components = new TaskMatcher[ result.size() ];
    result.toArray( components );
    return new AndMatcher( components );
  }

}
