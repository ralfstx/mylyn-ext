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

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;


public class QueryFilterDropDownMenuAction extends Action implements IMenuCreator {

  private Menu dropDownMenu;
  private final BugView bugView;

  public QueryFilterDropDownMenuAction( BugView bugView ) {
    super();
    this.bugView = bugView;
    setText( "Select query ..." );
    setToolTipText( "Select the tasks to show" );
    setMenuCreator( this );
  }

  public Menu getMenu( Control parent ) {
    if( dropDownMenu != null ) {
      dropDownMenu.dispose();
    }
    dropDownMenu = new Menu( parent );
    addActionsToMenu();
    return dropDownMenu;
  }

  public Menu getMenu( Menu parent ) {
    if( dropDownMenu != null ) {
      dropDownMenu.dispose();
    }
    dropDownMenu = new Menu( parent );
    addActionsToMenu();
    return dropDownMenu;
  }

  public void dispose() {
    if( dropDownMenu != null ) {
      dropDownMenu.dispose();
      dropDownMenu = null;
    }
  }

  private void addActionsToMenu() {
    Collection<IRepositoryQuery> queries = MylynBridge.getAllQueries();
    createItemForQuery( "All tasks", null );
    for( final IRepositoryQuery query : queries ) {
      String summary = query.getSummary();
      createItemForQuery( summary, query );
    }
  }

  private void createItemForQuery( String summary, final IRepositoryQuery query ) {
    Action action = new Action( summary, AS_RADIO_BUTTON ) {
      @Override
      public void run() {
        bugView.setActiveQuery( query );
      }
    };
    IRepositoryQuery activeQuery = bugView.getActiveQuery();
    action.setChecked( activeQuery == null ? query == null : activeQuery.equals( query ) );
    ActionContributionItem item = new ActionContributionItem( action );
    item.fill( dropDownMenu, -1 );
  }

}
