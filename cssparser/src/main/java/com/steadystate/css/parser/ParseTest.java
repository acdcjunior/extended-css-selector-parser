/*
 * $Id: ParseTest.java,v 1.1 2008-03-20 01:20:17 sdanig Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2005 David Schweinsberg.  All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * To contact the authors of the library:
 *
 * http://cssparser.sourceforge.net/
 * mailto:davidsch@users.sourceforge.net
 */

package com.steadystate.css.parser;

import java.io.*;
import org.w3c.css.sac.*;
import org.w3c.css.sac.helpers.ParserFactory;

/** 
 * TODO Move this to a test suite
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: ParseTest.java,v 1.1 2008-03-20 01:20:17 sdanig Exp $
 */
public class ParseTest extends HandlerBase {

    private static final String PARSER = "com.steadystate.css.parser.SACParser";

    private int _propertyCounter = 0;
    private int _indentSize = 0;
  
    public ParseTest() {
        try {
            CSSOMParser.setProperty("org.w3c.css.sac.parser", PARSER);
            ParserFactory factory = new ParserFactory();
            Parser parser = factory.makeParser();
            parser.setDocumentHandler(this);

            Reader r = new FileReader("stylesheets/test.css");
            
            InputSource is = new InputSource(r);
            parser.parseStyleSheet(is);

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ParseTest();
    }

    public void startDocument(InputSource source)
        throws CSSException {
        System.out.println("startDocument");
    }
    
    public void endDocument(InputSource source) throws CSSException {
        System.out.println("endDocument");
    }

    public void comment(String text) throws CSSException {
    }

    public void ignorableAtRule(String atRule) throws CSSException {
        System.out.println(atRule);
    }

    public void namespaceDeclaration(String prefix, String uri)
	    throws CSSException {
    }

    public void importStyle(String uri, SACMediaList media, 
			String defaultNamespaceURI)
	    throws CSSException {
        System.out.print("@import url(" + uri + ")");
        if (media.getLength() > 0) {
          System.out.println(" " + media.toString() + ";");
        } else {
          System.out.println(";");
        }
    }

    public void startMedia(SACMediaList media) throws CSSException {
        System.out.println(indent() + "@media " + media.toString() + " {");
        incIndent();
    }

    public void endMedia(SACMediaList media) throws CSSException {
        decIndent();
        System.out.println(indent() + "}");
    }

    public void startPage(String name, String pseudo_page) throws CSSException {
        System.out.print(indent() + "@page");
        if (name != null) {
            System.out.print(" " + name);
        }
        if (pseudo_page != null) {
            System.out.println(" " + pseudo_page);
        }
        System.out.println(" {");
        this._propertyCounter = 0;
        incIndent();
    }

    public void endPage(String name, String pseudo_page) throws CSSException {
        System.out.println();
        decIndent();
        System.out.println(indent() + "}");
    }

    public void startFontFace() throws CSSException {
        System.out.println(indent() + "@font-face {");
        this._propertyCounter = 0;
        incIndent();
    }

    public void endFontFace() throws CSSException {
        System.out.println();
        decIndent();
        System.out.println(indent() + "}");
    }

    public void startSelector(SelectorList selectors) throws CSSException {
        System.out.println(indent() + selectors.toString() + " {");
        this._propertyCounter = 0;
        incIndent();
    }

    public void endSelector(SelectorList selectors) throws CSSException {
        System.out.println();
        decIndent();
        System.out.println(indent() + "}");
    }

    public void property(String name, LexicalUnit value, boolean important)
            throws CSSException {
        if (this._propertyCounter++ > 0) {
            System.out.println(";");
        }
        System.out.print(indent() + name + ":");

        // Iterate through the chain of lexical units
        LexicalUnit nextVal = value;
        while (nextVal != null) {
//            System.out.print(" " + nextVal.toString());
            System.out.print(" " + ((LexicalUnitImpl)nextVal).toDebugString());
            nextVal = nextVal.getNextLexicalUnit();
        }

        // Is it important?
        if (important) {
            System.out.print(" !important");
        }
    }
    
    private String indent() {
        StringBuffer sb = new StringBuffer(16);
        for (int i = 0; i < this._indentSize; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    private void incIndent() {
        this._indentSize += 4;
    }

    private void decIndent() {
        this._indentSize -= 4;
    }
}
