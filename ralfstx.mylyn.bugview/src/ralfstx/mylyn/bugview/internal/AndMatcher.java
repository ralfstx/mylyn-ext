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

import org.eclipse.mylyn.tasks.core.ITask;


public class AndMatcher implements TaskMatcher {

  private final TaskMatcher[] components;

  public AndMatcher( TaskMatcher... components ) {
    this.components = components;
  }

  public boolean matches( ITask task ) {
    if( task == null ) {
      throw new NullPointerException( "parameter is null: task" );
    }
    for( TaskMatcher component : components ) {
      if( !component.matches( task ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append( "and(" );
    for( int i = 0; i < components.length; i++ ) {
      if( i > 0 ) {
        buffer.append( ',' );
      }
      buffer.append( components[i].toString() );
    }
    buffer.append( ")" );
    return buffer.toString();
  }

}
