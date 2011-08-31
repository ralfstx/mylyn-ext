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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;


class TaskLabelProvider extends LabelProvider implements ITableLabelProvider,
    IFontProvider, IColorProvider
{

  private static final ImageDescriptor TASK_ICON = Activator.getImageDescriptor( "/icons/task.gif" );
  private static final String COLOR_P1 = TaskLabelProvider.class.getName() + ".prio1";
  private static final String COLOR_P2 = TaskLabelProvider.class.getName() + ".prio2";
  private static final String COLOR_P4 = TaskLabelProvider.class.getName() + ".prio4";
  private static final String COLOR_P5 = TaskLabelProvider.class.getName() + ".prio5";

  public TaskLabelProvider() {
    JFaceResources.getColorRegistry().put( COLOR_P1, new RGB( 0xFF, 0, 0 ) );
    JFaceResources.getColorRegistry().put( COLOR_P2, new RGB( 0x80, 0, 0 ) );
    JFaceResources.getColorRegistry().put( COLOR_P4, new RGB( 0x80, 0x80, 0x80 ) );
    JFaceResources.getColorRegistry().put( COLOR_P5, new RGB( 0xC0, 0xC0, 0xC0 ) );
  }

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

  public Color getForeground( Object element ) {
    if( element instanceof ITask ) {
      ITask task = (ITask)element;
      String priority = task.getPriority();
      if( "P1".equals( priority ) ) {
        return JFaceResources.getColorRegistry().get( COLOR_P1 );
      }
      if( "P2".equals( priority ) ) {
        return JFaceResources.getColorRegistry().get( COLOR_P2 );
      }
      if( "P4".equals( priority ) ) {
        return JFaceResources.getColorRegistry().get( COLOR_P4 );
      }
      if( "P5".equals( priority ) ) {
        return JFaceResources.getColorRegistry().get( COLOR_P5 );
      }
    }
    return null;
  }

  public Color getBackground( Object element ) {
    return null;
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
