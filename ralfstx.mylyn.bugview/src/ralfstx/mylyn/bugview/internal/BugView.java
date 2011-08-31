package ralfstx.mylyn.bugview.internal;

import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;


public class BugView extends ViewPart {

  static final int COL_ID = 0;
  static final int COL_TITLE = 1;

  private TableViewer viewer;

  @Override
  public void createPartControl( Composite parent ) {
    parent.setLayout( GridLayoutFactory.fillDefaults().create() );
    createTableViewer( parent );
    makeActions();
    refreshViewer();
  }

  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  private void createTableViewer( Composite parent ) {
    Table table = new Table( parent, SWT.VIRTUAL );
    table.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    table.setLinesVisible( true );
    new TableColumn( table, SWT.LEFT ).setWidth( 80 );
    new TableColumn( table, SWT.LEFT ).setWidth( 400 );
    viewer = new TableViewer( table );
    viewer.setLabelProvider( new TaskLabelProvider() );
    viewer.setContentProvider( new ArrayContentProvider() );
    viewer.setComparator( new TaskLastModifiedComparator() );
    addStrikeThrough();
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

  private void makeActions() {
    ImageDescriptor refreshImage = Activator.getImageDescriptor( "/icons/refresh.gif" );
    IAction refreshAction = new Action( "Refresh", refreshImage ) {
      @Override
      public void run() {
        refreshViewer();
      }
    };
    getViewSite().getActionBars().getToolBarManager().add( refreshAction );
  }

  private void refreshViewer() {
    Collection<ITask> tasks = MylynBridge.getAllTasks();
    viewer.setInput( tasks );
    updateStatusBar();
  }

  private void updateStatusBar() {
    String message = null;
    Object input = viewer.getInput();
    if( input instanceof Collection ) {
      Collection<?> collection = (Collection<?>)input;
      message = collection.size() + " bugs";
    }
    IStatusLineManager statusLineManager = getViewSite().getActionBars().getStatusLineManager();
    statusLineManager.setMessage( message );
  }

  static class TaskLabelProvider extends LabelProvider implements ITableLabelProvider,
      IFontProvider
  {

    private static final ImageDescriptor TASK_ICON = Activator.getImageDescriptor( "/icons/task.gif" );

    public Image getColumnImage( Object element, int columnIndex ) {
      Image result = null;
      if( element instanceof ITask ) {
        if( columnIndex == BugView.COL_ID ) {
          result = TASK_ICON.createImage();
        }
      }
      return result;
    }

    public String getColumnText( Object element, int columnIndex ) {
      String result = null;
      if( element instanceof ITask ) {
        ITask task = (ITask)element;
        if( columnIndex == BugView.COL_ID ) {
          result = task.getTaskId();
        } else if( columnIndex == BugView.COL_TITLE ) {
          result = task.getSummary();
        }
      }
      return result;
    }

    public Font getFont( Object element ) {
      if( element instanceof ITask ) {
        ITask task = (ITask)element;
        if( task.getSynchronizationState().isIncoming() ) {
          return JFaceResources.getFontRegistry().getBold( JFaceResources.DEFAULT_FONT );
        }
      }
      return null;
    }

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

    private int compareModificationDate( ITask task1, ITask task2 ) {
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

}
