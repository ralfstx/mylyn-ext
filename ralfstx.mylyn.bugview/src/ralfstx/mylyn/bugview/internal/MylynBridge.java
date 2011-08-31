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
import java.util.Collection;

import org.eclipse.mylyn.internal.tasks.core.AbstractTask;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.ITask;


@SuppressWarnings( "restriction" )
class MylynBridge {

  static Collection<ITask> getAllTasks() {
    Collection<AbstractTask> tasks = TasksUiPlugin.getTaskList().getAllTasks();
    return new ArrayList<ITask>( tasks );
  }

  static void openTaskInEditor( ITask selectedTask ) {
    TasksUiInternal.openTask( selectedTask, selectedTask.getTaskId() );
  }

}
