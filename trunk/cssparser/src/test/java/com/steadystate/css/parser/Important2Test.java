/*
 * $Id: Important2Test.java,v 1.2 2008-11-28 13:02:18 waldbaer Exp $
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

public class Important2Test implements ErrorHandler {

	private Parser sacParser;

	@Test
	public void css1() {
        css(new SACParserCSS1());
    }

	@Test
    public void css2() {
        css(new SACParserCSS2());
    }

	@Test
    public void css21() {
        css(new SACParserCSS21());
    }


    private void css(Parser sacParser) {
    	this.sacParser = sacParser;
    	sacParser.setErrorHandler(this);
        InputStream is = getClass().getClassLoader().getResourceAsStream("important2.css");
        Assert.assertNotNull(is);
        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        try {
            sacParser.parseStyleSheet(source);
		}
        catch (CSSException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
		}
        catch (IOException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
		}
    }

	public void error(CSSParseException e) throws CSSException {
		if ("http://www.w3.org/TR/CSS21/".equals(this.sacParser.getParserVersion())) {
		    Assert.assertFalse(e.getLocalizedMessage(), true);
		}
	}

	public void fatalError(CSSParseException e) throws CSSException {
	    Assert.assertFalse(e.getLocalizedMessage(), true);
	}

	public void warning(CSSParseException e) throws CSSException {
		if (! "http://www.w3.org/TR/REC-CSS2/".equals(sacParser.getParserVersion())) {
		    Assert.assertFalse(e.getLocalizedMessage(), true);
		}
	}
}
