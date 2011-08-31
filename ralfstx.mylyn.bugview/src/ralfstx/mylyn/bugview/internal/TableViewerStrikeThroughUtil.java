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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


public class TableViewerStrikeThroughUtil {

  public static void attach( StrikeThroughProvider provider, TableViewer viewer ) {
    Listener customDrawer = new StrikeThroughListener( provider );
    Table table = viewer.getTable();
    table.addListener( SWT.EraseItem, customDrawer );
    table.addListener( SWT.PaintItem, customDrawer );
  }

  static void strikeThroughTableItem( TableItem item, GC gc ) {
    int columnCount = item.getParent().getColumnCount();
    for( int column = 0; column < columnCount; column++ ) {
      String text = item.getText( column );
      Point extent = gc.textExtent( text );
      Rectangle bounds = item.getTextBounds( column );
      int lineY = bounds.y + bounds.height / 2;
      gc.drawLine( bounds.x, lineY, bounds.x + extent.x, lineY );
    }
  }

  private static class StrikeThroughListener implements Listener {
    private final StrikeThroughProvider provider;

    public StrikeThroughListener( StrikeThroughProvider provider ) {
      this.provider = provider;
    }

    public void handleEvent( Event event ) {
      if( provider.getStrikeThrough( event.item.getData() ) ) {
        strikeThroughTableItem( (TableItem)event.item, event.gc );
      }
    }
  }

}
