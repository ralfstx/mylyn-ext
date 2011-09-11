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
package ralfstx.mylyn.bugview.internal.matchers;

import static org.junit.Assert.*;
import static ralfstx.mylyn.bugview.test.TestUtil.*;

import org.hamcrest.StringDescription;
import org.junit.Test;


public class ContainsHashTag_Test {

  @Test
  public void matches_hashTag() throws Exception {
    ContainsHashTag matcher = new ContainsHashTag( "foo" );

    assertTrue( matcher.matches( mockTaskWithNotes( "#foo" ) ) );
    assertFalse( matcher.matches( mockTaskWithNotes( "#bar" ) ) );
    assertFalse( matcher.matches( mockTaskWithNotes( "foo" ) ) );
  }

  @Test
  public void matches_withoutName() throws Exception {
    ContainsHashTag matcher = new ContainsHashTag( "" );

    assertFalse( matcher.matches( mockTaskWithNotes( "" ) ) );
    assertFalse( matcher.matches( mockTaskWithNotes( "#" ) ) );
    assertFalse( matcher.matches( mockTaskWithNotes( "foo" ) ) );
  }

  @Test
  public void matches_whenNotesAreNull() throws Exception {
    ContainsHashTag matcher = new ContainsHashTag( "foo" );

    assertFalse( matcher.matches( mockTaskWithNotes( null ) ) );
  }

  @Test
  public void matches_caseInsensitive() throws Exception {
    ContainsHashTag matcher = new ContainsHashTag( "Foo" );

    assertTrue( matcher.matches( mockTaskWithNotes( "#foO" ) ) );
  }

  @Test
  public void description() throws Exception {
    assertEquals( "containsHashTag(\"\")", StringDescription.toString( new ContainsHashTag( "" ) ) );
    assertEquals( "containsHashTag(\"foo\")",
                  StringDescription.toString( new ContainsHashTag( "foo" ) ) );
  }

}
