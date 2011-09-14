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
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;


public class WordProposalProvider implements IContentProposalProvider {

  private List<String> suggestions = Collections.emptyList();

  public IContentProposal[] getProposals( String contents, int position ) {
    ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
    int wordStart = findWordStart( contents, position );
    int wordEnd = findWordEnd( contents, position );
    if( wordEnd > wordStart ) {
      String prefix = contents.substring( wordStart, position );
      for( String suggestion : suggestions ) {
        if( startsWithIgnoreCase( suggestion, prefix ) ) {
          String replacement = replaceRange( contents, wordStart, wordEnd, suggestion );
          if( wordEnd == contents.length() ) {
            replacement += " ";
          }
          int cursorPosition = wordStart + suggestion.length() + 1;
          list.add( new ContentProposal( replacement, suggestion, null, cursorPosition ) );
        }
      }
    }
    return list.toArray( new IContentProposal[ list.size() ] );
  }

  public void setSuggestions( List<String> suggestions ) {
    this.suggestions = new ArrayList<String>( suggestions );
  }

  private static int findWordStart( String string, int position ) {
    for( int index = position - 1; index >= 0; index-- ) {
      char ch = string.charAt( index );
      if( Character.isWhitespace( ch ) ) {
        return index + 1;
      }
    }
    return 0;
  }

  private static int findWordEnd( String string, int position ) {
    int length = string.length();
    for( int index = position; index < length; index++ ) {
      char ch = string.charAt( index );
      if( Character.isWhitespace( ch ) ) {
        return index;
      }
    }
    return length;
  }

  private static boolean startsWithIgnoreCase( String string, String prefix ) {
    return    string.length() >= prefix.length()
           && string.substring( 0, prefix.length() ).equalsIgnoreCase( prefix );
  }

  private static String replaceRange( String string, int start, int end, String replacement ) {
    StringBuilder buffer = new StringBuilder( string );
    buffer.replace( start, end, replacement );
    return buffer.toString();
  }

}
