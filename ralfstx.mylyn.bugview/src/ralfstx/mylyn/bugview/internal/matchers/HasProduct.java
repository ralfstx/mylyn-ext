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


public class HasProduct extends TaskMatcher {

  private final String searchString;

  public HasProduct( String searchString ) {
    this.searchString = searchString.toLowerCase( Locale.ENGLISH );
  }

  @Override
  protected boolean matches( ITask task ) {
    String product = task.getAttribute( "product" );
    if( product != null && product.toLowerCase( Locale.ENGLISH ).equals( searchString ) ) {
      return true;
    }
    if( product == null && searchString.length() == 0 ) {
      return true;
    }
    return false;
  }

  public void describeTo( Description description ) {
    description.appendText( "hasProduct(" );
    description.appendValue( searchString );
    description.appendText( ")" );
  }

}
