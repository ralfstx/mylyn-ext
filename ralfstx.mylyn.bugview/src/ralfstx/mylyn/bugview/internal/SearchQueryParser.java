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
import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import ralfstx.mylyn.bugview.TaskMatchers;
import ralfstx.mylyn.bugview.internal.matchers.NameOrId;


public class SearchQueryParser {

  public Matcher<ITask> parse( String query ) {
    if( query == null ) {
      throw new NullPointerException( "null parameter: query" );
    }
    ArrayList<Matcher<? extends ITask>> result = new ArrayList<Matcher<? extends ITask>>();
    String[] parts = query.split( "\\s+" );
    for( String part : parts ) {
      Matcher<ITask> matcher = getMatcherForPart( part );
      if( matcher != null ) {
        result.add( matcher );
      }
    }
    if( result.size() == 0 ) {
      return CoreMatchers.anything();
    }
    return CoreMatchers.allOf( result );
  }

  public static List<String> getSuggestions() {
    return Arrays.asList( new String[] { ":incoming", ":outgoing", ":open", ":defect",
        ":enhancement", "product:", "assigned:" } );
  }

  private static Matcher<ITask> getMatcherForPart( String part ) {
    if( part.length() == 0 ) {
      return null;
    }
    if( part.startsWith( "!" ) ) {
      return CoreMatchers.not( getMatcherForPart( part.substring( 1 ) ) );
    }
    if( ":incoming".equals( part ) ) {
      return TaskMatchers.isIncoming();
    }
    if( ":outgoing".equals( part ) ) {
      return TaskMatchers.isOutgoing();
    }
    if( ":open".equals( part ) ) {
      return CoreMatchers.not( TaskMatchers.isCompleted() );
    }
    if( ":defect".equals( part ) ) {
      return CoreMatchers.not( TaskMatchers.isEnhancement() );
    }
    if( ":enhancement".equals( part ) ) {
      return TaskMatchers.isEnhancement();
    }
    if( part.startsWith( "product:" ) ) {
      return TaskMatchers.productMatches( part.substring( "product:".length() ) );
    }
    if( part.startsWith( "assigned:" ) ) {
      return TaskMatchers.ownerMatches( part.substring( "assigned:".length() ) );
    }
    if( part.startsWith( "#" ) ) {
      return TaskMatchers.containsHashTag( part.substring( 1 ) );
    }
    return new NameOrId( part );
  }

}
