/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2015 David Schweinsberg.  All rights reserved.
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
 *
 */
package com.steadystate.css.parser;

import com.steadystate.css.ErrorHandler;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

import java.io.StringReader;

/**
 * @author rbri
 */
public class XhtmlCssTest {

    private static final String CSS_CODE = "<!--/*--><![CDATA[/*><!--*/ \n"
        + "body { color: #000000; background-color: #FFFFFF; }\n"
        + "a:link { color: #0000CC; }\n"
        + "p, address {margin-left: 3em;}\n"
        + "span {font-size: smaller;}\n"
        + "/*]]>*/-->";

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void xhtmlCssCSS2() throws Exception {
        xhtmlCss(new SACParserCSS2());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void xhtmlCssCSS21() throws Exception {
        xhtmlCss(new SACParserCSS21());
    }

    private void xhtmlCss(final Parser sacParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        sacParser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(CSS_CODE));

        sacParser.parseStyleSheet(source);

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());
    }
}
