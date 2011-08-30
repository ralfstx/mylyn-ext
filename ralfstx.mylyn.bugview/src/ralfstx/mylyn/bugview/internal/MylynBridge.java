package ralfstx.mylyn.bugview.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.mylyn.internal.tasks.core.AbstractTask;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.ITask;


@SuppressWarnings( "restriction" )
class MylynBridge {

  static Collection<ITask> getAllTasks() {
    Collection<AbstractTask> tasks = TasksUiPlugin.getTaskList().getAllTasks();
    return new ArrayList<ITask>( tasks );
  }

}
