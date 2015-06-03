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
package com.steadystate.css.samples;

import java.io.StringReader;

import org.junit.Test;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * Test cases for the code we have on our 'Get Started' page.
 *
 * @author rbri
 */
public class GettingStartedTest {

    @Test
    public void introduction() throws Exception {
        InputSource source = new InputSource(new StringReader("h1 { background: #ffcc44; }"));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        CSSRuleList rules = sheet.getCssRules();
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);

            System.out.println(rule.getCssText());
        }
    }

    @Test
    public void erroHandling() throws Exception {
        InputSource source = new InputSource(new StringReader("h1 { background: #red; }"));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());

        ErrorHandler errorHandler = new MyErrorHandler();
        parser.setErrorHandler(errorHandler);

        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
    }

    public static class MyErrorHandler implements ErrorHandler {

        public void warning(CSSParseException exception) throws CSSException {
            System.out.println("Warning: " + exception.getMessage());
        }

        public void error(CSSParseException exception) throws CSSException {
            System.out.println("Error: " + exception.getMessage());
        }

        public void fatalError(CSSParseException exception) throws CSSException {
            System.out.println("Fatal: " + exception.getMessage());
        }
    }
}
