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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.junit.Before;
import org.junit.Test;


public class WordProposalProvider_Test {

  private WordProposalProvider provider;

  @Before
  public void setup() {
    provider = new WordProposalProvider();
  }

  @Test
  public void getProposals_whenEmpty() throws Exception {
    assertEquals( 0, provider.getProposals( "", 0 ).length );
    assertEquals( 0, provider.getProposals( "f", 1 ).length );
  }

  @Test
  public void getProposals_afterFirstChar() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "f", 1 );

    assertEquals( 1, proposals.length );
    assertEquals( "foo", proposals[0].getLabel() );
    assertEquals( "foo ", proposals[0].getContent() );
    assertEquals( 4, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_afterSecondChar() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "fo", 2 );

    assertEquals( 1, proposals.length );
    assertEquals( "foo", proposals[0].getLabel() );
    assertEquals( "foo ", proposals[0].getContent() );
    assertEquals( 4, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_afterLastChar() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "foo", 3 );

    assertEquals( 1, proposals.length );
    assertEquals( "foo", proposals[0].getLabel() );
    assertEquals( "foo ", proposals[0].getContent() );
    assertEquals( 4, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_inWord() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "for", 2 );

    assertEquals( 1, proposals.length );
    assertEquals( "foo", proposals[0].getLabel() );
    assertEquals( "foo ", proposals[0].getContent() );
    assertEquals( 4, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_withPreceedingWord() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "bar f", 5 );

    assertEquals( 1, proposals.length );
    assertEquals( "foo", proposals[0].getLabel() );
    assertEquals( "bar foo ", proposals[0].getContent() );
    assertEquals( 8, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_withPreceedingAndSubsequentWord() throws Exception {
    provider.setSuggestions( createList( "bar" ) );

    IContentProposal[] proposals = provider.getProposals( "foo bao baz", 5 );

    assertEquals( 1, proposals.length );
    assertEquals( "bar", proposals[0].getLabel() );
    assertEquals( "foo bar baz", proposals[0].getContent() );
    assertEquals( 8, proposals[0].getCursorPosition() );
  }

  @Test
  public void getProposals_withMultipleSuggestions() throws Exception {
    provider.setSuggestions( createList( "foo", "bar", "baz" ) );

    IContentProposal[] proposals = provider.getProposals( "ba", 2 );

    assertEquals( 2, proposals.length );
    assertEquals( "bar ", proposals[0].getContent() );
    assertEquals( "baz ", proposals[1].getContent() );
  }

  @Test
  public void getProposals_afterWhitespace() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( " ", 1 );

    assertEquals( 0, proposals.length );
  }

  @Test
  public void getProposals_afterWhitespaceAfterWord() throws Exception {
    provider.setSuggestions( createList( "foo" ) );

    IContentProposal[] proposals = provider.getProposals( "foo ", 4 );

    assertEquals( 0, proposals.length );
  }

  private static List<String> createList( String... strings ) {
    ArrayList<String> result = new ArrayList<String>();
    for( String string : strings ) {
      result.add( string );
    }
    return result;
  }

}
