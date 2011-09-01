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

import java.util.Locale;

import org.eclipse.mylyn.tasks.core.ITask;


public class NameOrIdFilter implements TaskFilter {

  private final String searchString;

  public NameOrIdFilter( String searchString ) {
    this.searchString = searchString.toLowerCase( Locale.ENGLISH );
  }

  public boolean matches( ITask task ) {
    String id = task.getTaskId().toLowerCase( Locale.ENGLISH );
    if( id.startsWith( searchString ) ) {
      return true;
    }
    String summary = task.getSummary().toLowerCase( Locale.ENGLISH );
    if( summary.contains( searchString ) ) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "nameOrId:" + searchString;
  }
}
