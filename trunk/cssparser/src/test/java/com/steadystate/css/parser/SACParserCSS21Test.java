/*
 * $Id: SACParserCSS21Test.java,v 1.3 2008-11-28 13:02:18 waldbaer Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2008 David Schweinsberg.  All rights reserved.
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

public class SACParserCSS21Test extends TestCase {

    public void testDojoCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        SACParserCSS21 sp = new SACParserCSS21();
        CSSOMParser parser = new CSSOMParser(sp);
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
        assertEquals(17, sheet.getCssRules().getLength());
    }

    public void testTestCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("test.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
//        assertEquals(21, sheet.getCssRules().getLength());
        assertEquals(78, sheet.getCssRules().getLength());
    }

}
