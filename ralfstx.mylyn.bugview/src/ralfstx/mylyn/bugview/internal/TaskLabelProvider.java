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

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import ralfstx.mylyn.bugview.TaskMatcher;
import ralfstx.mylyn.bugview.TaskMatchers;


class TaskLabelProvider extends ColumnLabelProvider {

  private static final String DEFECT_ICON = "defect-icon";
  private static final String ENHANCEMENT_ICON = "enhancement-icon";
  private static final String COLOR_P1 = TaskLabelProvider.class.getName() + ".prio1";
  private static final String COLOR_P2 = TaskLabelProvider.class.getName() + ".prio2";
  private static final String COLOR_P4 = TaskLabelProvider.class.getName() + ".prio4";
  private static final String COLOR_P5 = TaskLabelProvider.class.getName() + ".prio5";
  private final TaskMatcher enhancementMatcher;
  private final TaskMatcher incomingMatcher;
  private final TaskMatcher outgoingMatcher;

  public TaskLabelProvider() {
    enhancementMatcher = TaskMatchers.isEnhancement();
    incomingMatcher = TaskMatchers.isIncoming();
    outgoingMatcher = TaskMatchers.isOutgoing();
    initializeImages();
    initializeColors();
  }

  public Image getColumnImage( Object element, int columnIndex ) {
    Image result = null;
    if( element instanceof ITask ) {
      if( columnIndex == BugView.COL_ID ) {
        ITask task = (ITask)element;
        if( enhancementMatcher.matches( task ) ) {
          result = JFaceResources.getImageRegistry().get( ENHANCEMENT_ICON );
        } else {
          result = JFaceResources.getImageRegistry().get( DEFECT_ICON );
        }
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

  @Override
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

  @Override
  public Font getFont( Object element ) {
    if( element instanceof ITask ) {
      ITask task = (ITask)element;
      if( incomingMatcher.matches( task ) ) {
        return JFaceResources.getFontRegistry().getBold( JFaceResources.DEFAULT_FONT );
      }
      if( outgoingMatcher.matches( task ) ) {
        return JFaceResources.getFontRegistry().getItalic( JFaceResources.DEFAULT_FONT );
      }
    }
    return null;
  }

  @Override
  public String getToolTipText( Object element ) {
    if( element instanceof ITask ) {
      ITask task = (ITask)element;
      return task.getSummary();
    }
    return null;
  }

  @Override
  public void update( ViewerCell cell ) {
    Object element = cell.getElement();
    int columnIndex = cell.getColumnIndex();
    cell.setText( getColumnText( element, columnIndex ) );
    cell.setImage( getColumnImage( element, columnIndex ) );
    cell.setBackground( getBackground( element ) );
    cell.setForeground( getForeground( element ) );
    cell.setFont( getFont( element ) );
  }

  private static void initializeColors() {
    ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
    colorRegistry.put( COLOR_P1, new RGB( 0xFF, 0, 0 ) );
    colorRegistry.put( COLOR_P2, new RGB( 0x80, 0, 0 ) );
    colorRegistry.put( COLOR_P4, new RGB( 0x80, 0x80, 0x80 ) );
    colorRegistry.put( COLOR_P5, new RGB( 0xC0, 0xC0, 0xC0 ) );
  }

  private static void initializeImages() {
    ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
    imageRegistry.put( DEFECT_ICON, Activator.getImageDescriptor( "/icons/defect.png" ) );
    imageRegistry.put( ENHANCEMENT_ICON, Activator.getImageDescriptor( "/icons/enhancement.png" ) );
  }

}
