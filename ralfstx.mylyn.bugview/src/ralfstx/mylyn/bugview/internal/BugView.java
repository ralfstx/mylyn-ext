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
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import ralfstx.mylyn.bugview.TaskMatchers;


public class BugView extends ViewPart {

  static final int COL_ID = 0;
  static final int COL_TITLE = 1;

  private TableViewer viewer;
  private Text searchField;
  private IRepositoryQuery activeQuery;
  private Matcher<ITask> toolbarMatcher = CoreMatchers.anything();
  private Matcher<ITask> searchMatcher = CoreMatchers.anything();
  private final SearchQueryParser queryParser = new SearchQueryParser();
  private WordProposalProvider proposalProvider;

  @Override
  public void createPartControl( Composite parent ) {
    parent.setLayout( createMainLayout() );
    createQuickFilterArea( parent );
    createSearchTextField( parent );
    createTableViewer( parent );
    addContentProposalToSearchField();
    makeActions();
    refreshViewer();
  }

  @Override
  public void setFocus() {
    searchField.setFocus();
  }

  void setActiveQuery( IRepositoryQuery query ) {
    activeQuery = query;
    updateIdColumnWidth();
    refreshViewer();
  }

  IRepositoryQuery getActiveQuery() {
    return activeQuery;
  }

  private void createQuickFilterArea( Composite parent ) {
    final QuickFilterArea filterArea = new QuickFilterArea( parent, SWT.NONE );
    filterArea.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    addQuickFilterContributions( filterArea );
    filterArea.setMatcherChangedListener( new Runnable() {
      public void run() {
        toolbarMatcher = filterArea.getMatcher();
        refreshFilter();
      }
    } );
  }

  private static void addQuickFilterContributions( final QuickFilterArea filterArea ) {
    QuickFilterContribution showIncoming = createIncomingContribution();
    QuickFilterContribution showOutgoing = createOutgoingContribution();
    filterArea.createToolBar( showIncoming, showOutgoing );
    QuickFilterContribution showEnhancements = createEnhancementsContribution();
    QuickFilterContribution showDefects = createDefectsContribution();
    filterArea.createToolBar( showEnhancements, showDefects );
    QuickFilterContribution hideCompleted = createHideCompletedContribution();
    filterArea.createToolBar( hideCompleted );
  }

  private static QuickFilterContribution createIncomingContribution() {
    return new QuickFilterContribution( "show only incoming",
                                        Activator.getImageDescriptor( "/icons/incoming.png" ),
                                        TaskMatchers.isIncoming() );
  }

  private static QuickFilterContribution createOutgoingContribution() {
    return new QuickFilterContribution( "show only outgoing",
                                        Activator.getImageDescriptor( "/icons/outgoing.png" ),
                                        TaskMatchers.isOutgoing() );
  }

  private static QuickFilterContribution createEnhancementsContribution() {
    return new QuickFilterContribution( "show only enhancements",
                                        Activator.getImageDescriptor( "/icons/enhancement.png" ),
                                        TaskMatchers.isEnhancement() );
  }

  private static QuickFilterContribution createDefectsContribution() {
    return new QuickFilterContribution( "show only defects",
                                        Activator.getImageDescriptor( "/icons/defect.png" ),
                                        CoreMatchers.not( TaskMatchers.isEnhancement() ) );
  }

  private static QuickFilterContribution createHideCompletedContribution() {
    return new QuickFilterContribution( "hide completed",
                                        Activator.getImageDescriptor( "/icons/hidecompleted.png" ),
                                        CoreMatchers.not( TaskMatchers.isCompleted() ) );
  }

  private void createSearchTextField( Composite parent ) {
    searchField = new Text( parent, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH | SWT.ICON_CANCEL );
    searchField.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    searchField.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetDefaultSelected( SelectionEvent e ) {
        String query = searchField.getText().trim();
        searchMatcher = queryParser.parse( query );
        refreshFilter();
      }
    } );
  }

  private void addContentProposalToSearchField() {
    proposalProvider = new WordProposalProvider();
    TextContentAdapter controlAdapter = new TextContentAdapter();
    ContentProposalAdapter proposalAdapter
      = new ContentProposalAdapter( searchField, controlAdapter, proposalProvider, null, null );
    proposalAdapter.setProposalAcceptanceStyle( ContentProposalAdapter.PROPOSAL_REPLACE );
    proposalProvider.setSuggestions( SearchQueryParser.getSuggestions() );
  }

  private void refreshFilter() {
    viewer.refresh( false );
    updateStatusBar();
  }

  private void createTableViewer( Composite parent ) {
    Table table = new Table( parent, SWT.VIRTUAL | SWT.FULL_SELECTION );
    table.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    table.setLinesVisible( true );
    table.addControlListener( new ControlAdapter() {
      @Override
      public void controlResized( ControlEvent e ) {
        layoutTable();
      }
    } );
    new TableColumn( table, SWT.LEFT ).setWidth( 80 );
    new TableColumn( table, SWT.LEFT ).setWidth( 400 );
    viewer = new TableViewer( table );
    viewer.setLabelProvider( new TaskLabelProvider() );
    viewer.setContentProvider( new ArrayContentProvider() );
    viewer.setComparator( new TaskLastModifiedComparator() );
    viewer.addFilter( new TaskViewerFilter() );
    ColumnViewerToolTipSupport.enableFor( viewer );
    addStrikeThrough();
    addDoubleClickBehavior();
  }

  private void layoutTable() {
    Table table = viewer.getTable();
    int clientWidth = table.getClientArea().width;
    int idWidth = table.getColumn( COL_ID ).getWidth();
    table.getColumn( COL_TITLE ).setWidth( clientWidth - idWidth );
  }

  private void updateIdColumnWidth() {
    Table table = viewer.getTable();
    int iconWidth = 16;
    int padding = 12;
    int idWidth = calculateIdWidth() + iconWidth + padding;
    table.getColumn( COL_ID ).setWidth( idWidth );
  }

  private int calculateIdWidth() {
    int maxWidth = 0;
    Table table = viewer.getTable();
    Font boldFont = JFaceResources.getFontRegistry().getBold( JFaceResources.DEFAULT_FONT );
    GC gc = new GC( table );
    try {
      gc.setFont( boldFont );
      Collection<ITask> tasks = getTasks();
      for( ITask task : tasks ) {
        String id = task.getTaskId();
        int width = gc.stringExtent( id ).x;
        maxWidth = Math.max( maxWidth, width );
      }
    } finally {
      gc.dispose();
    }
    return maxWidth;
  }

  private void addStrikeThrough() {
    StrikeThroughProvider strikeThroughProvider = new StrikeThroughProvider() {
      public boolean getStrikeThrough( Object element ) {
        if( element instanceof ITask ) {
          ITask task = (ITask)element;
          return task.isCompleted();
        }
        return false;
      }
    };
    TableViewerStrikeThroughUtil.attach( strikeThroughProvider, viewer );
  }

  private void addDoubleClickBehavior() {
    viewer.addDoubleClickListener( new IDoubleClickListener() {
      public void doubleClick( DoubleClickEvent event ) {
        ITask selectedTask = getSelectedTask();
        if( selectedTask != null ) {
          MylynBridge.openTaskInEditor( selectedTask );
        }
      }
    } );
  }

  private void makeActions() {
    ImageDescriptor refreshImage = Activator.getImageDescriptor( "/icons/refresh.gif" );
    IAction refreshViewAction = new Action( "Refresh View", refreshImage ) {
      @Override
      public void run() {
        refreshViewer();
      }
    };
    ImageDescriptor refreshAllImage
      = Activator.getImageDescriptor( "/icons/repository-synchronize.gif" );
    IAction refreshAllAction = new Action( "Refresh all Repositories", refreshAllImage ) {
      @Override
      public void run() {
        MylynBridge.synchronizeAllRepositories();
      }
    };
    IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
    toolBarManager.add( refreshViewAction );
    toolBarManager.add( refreshAllAction );
    IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
    menuManager.add( new QueryFilterDropDownMenuAction( this ) );
  }

  private void refreshViewer() {
    Collection<ITask> tasks = getTasks();
    viewer.setInput( tasks );
    updateStatusBar();
  }

  private Collection<ITask> getTasks() {
    if( activeQuery != null ) {
      return MylynBridge.getAllTasks( activeQuery );
    }
    return MylynBridge.getAllTasks();
  }

  private ITask getSelectedTask() {
    ISelection selection = viewer.getSelection();
    if( !selection.isEmpty() && selection instanceof StructuredSelection ) {
      StructuredSelection structuredSelection = (StructuredSelection)selection;
      Object element = structuredSelection.getFirstElement();
      if( element instanceof ITask ) {
        return (ITask)element;
      }
    }
    return null;
  }

  private void updateStatusBar() {
    String message = null;
    int items = viewer.getTable().getItemCount();
    message = items + " bugs";
    IStatusLineManager statusLineManager = getViewSite().getActionBars().getStatusLineManager();
    statusLineManager.setMessage( message );
  }

  private static GridLayout createMainLayout() {
    GridLayout mainLayout = new GridLayout();
    mainLayout.marginWidth = 0;
    mainLayout.marginHeight = 0;
    mainLayout.verticalSpacing = 0;
    return mainLayout;
  }

  private static final class TaskLastModifiedComparator extends ViewerComparator {
    @Override
    public int compare( Viewer viewer, Object element1, Object element2 ) {
      int result = 0;
      if( element1 instanceof ITask && element2 instanceof ITask ) {
        result = compareModificationDate( (ITask)element1, (ITask)element2 ) * -1;
      }
      return result;
    }

    private static int compareModificationDate( ITask task1, ITask task2 ) {
      int result;
      Date modDate1 = task1.getModificationDate();
      Date modDate2 = task2.getModificationDate();
      if( modDate1 == null ) {
        result = modDate2 == null ? 0 : -1;
      } else {
        result = modDate2 == null ? 1 : modDate1.compareTo( modDate2 );
      }
      return result;
    }
  }

  private final class TaskViewerFilter extends ViewerFilter {
    @Override
    public boolean select( Viewer viewer, Object parentElement, Object element ) {
      if( element instanceof ITask ) {
        ITask task = (ITask)element;
        return toolbarMatcher.matches( task ) && searchMatcher.matches( task );
      }
      return false;
    }
  }

}
