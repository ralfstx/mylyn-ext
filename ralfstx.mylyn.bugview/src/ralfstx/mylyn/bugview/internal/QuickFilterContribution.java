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
import org.eclipse.mylyn.tasks.core.ITask;
import org.hamcrest.Matcher;


public class QuickFilterContribution {

  public final String text;
  public final ImageDescriptor image;
  public final Matcher<ITask> matcher;

  public QuickFilterContribution( String text, ImageDescriptor image, Matcher<ITask> matcher ) {
    this.text = text;
    this.image = image;
    this.matcher = matcher;
  }

}
