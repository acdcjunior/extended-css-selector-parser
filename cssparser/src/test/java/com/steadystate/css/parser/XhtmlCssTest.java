/*
 * $Id: XhtmlCssTest.java,v 1.1 2008-11-28 13:01:29 waldbaer Exp $
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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

import junit.framework.TestCase;

public class XhtmlCssTest extends TestCase implements ErrorHandler
{

    private static final String CSS_CODE = "<!--/*--><![CDATA[/*><!--*/ \n"
        + "body { color: #000000; background-color: #FFFFFF; }\n"
        + "a:link { color: #0000CC; }\n"
        + "p, address {margin-left: 3em;}\n"
        + "span {font-size: smaller;}\n"
        + "/*]]>*/-->";

    public void testXhtmlCssCSS1()
    {
    	this.testXhtmlCss(new SACParserCSS1());
    }

    public void testXhtmlCssCSS2()
    {
    	this.testXhtmlCss(new SACParserCSS2());
    }

    public void testXhtmlCssCSS21()
    {
    	this.testXhtmlCss(new SACParserCSS21());
    }

    public void testXhtmlCssCSSmobileOKBasic1()
    {
    	this.testXhtmlCss(new SACParserCSSmobileOKBasic1());
    }

    private void testXhtmlCss(Parser sacParser)
    {
        Reader r = new StringReader(CSS_CODE);
        InputSource source = new InputSource(r);
        try
        {
            sacParser.parseStyleSheet(source);
        }
        catch (CSSException e)
        {
        	assertFalse(e.getLocalizedMessage(), true);
        }
        catch (IOException e)
        {
        	assertFalse(e.getLocalizedMessage(), true);
        }
    }

    public void error(CSSParseException exception) throws CSSException
    {
        assertFalse(exception.getLocalizedMessage(), true);
    }

    public void fatalError(CSSParseException exception) throws CSSException
    {
        assertFalse(exception.getLocalizedMessage(), true);
    }

    public void warning(CSSParseException exception) throws CSSException
    {
        
    }

}
