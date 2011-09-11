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

import java.util.List;
import java.util.Locale;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Description;

import ralfstx.mylyn.bugview.TaskMatcher;
import ralfstx.mylyn.bugview.internal.HashTagParser;
import ralfstx.mylyn.bugview.internal.MylynBridge;


public class ContainsHashTag extends TaskMatcher {

  private final String searchString;

  public ContainsHashTag( String searchString ) {
    this.searchString = searchString.toLowerCase( Locale.ENGLISH );
  }

  @Override
  protected boolean matches( ITask task ) {
    String notes = MylynBridge.getNotes( task );
    if( notes != null ) {
      HashTagParser parser = new HashTagParser();
      List<String> tags = parser.parse( notes );
      return tags.contains( searchString );
    }
    return false;
  }

  public void describeTo( Description description ) {
    description.appendText( "containsHashTag(" );
    description.appendValue( searchString );
    description.appendText( ")" );
  }

}
