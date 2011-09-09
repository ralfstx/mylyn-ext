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

import java.util.Locale;

import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Description;

import ralfstx.mylyn.bugview.TaskMatcher;


public class OwnerMatches extends TaskMatcher {

  private final String searchString;

  public OwnerMatches( String searchString ) {
    this.searchString = searchString.toLowerCase( Locale.ENGLISH );
  }

  @Override
  protected boolean matches( ITask task ) {
    String owner = task.getOwner();
    if( searchString.length() == 0 ) {
      return owner == null || owner.length() == 0;
    } else if( owner != null && owner.toLowerCase( Locale.ENGLISH ).startsWith( searchString ) ) {
      return true;
    }
    return false;
  }

  public void describeTo( Description description ) {
    description.appendText( "ownerMatches(" );
    description.appendValue( searchString );
    description.appendText( ")" );
  }

}
