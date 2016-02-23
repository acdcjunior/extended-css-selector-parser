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
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Testcases.
 */
public class ImportantTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void css21() throws Exception {
        css(new SACParserCSS3());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void css21Error() throws Exception {
        final ErrorHandler errorHandler = parserError(new SACParserCSS3());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());
    }

    private void css(final Parser sacParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        sacParser.setErrorHandler(errorHandler);

        final InputStream is = getClass().getClassLoader().getResourceAsStream("important.css");
        Assert.assertNotNull(is);
        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSOMParser parser = new CSSOMParser(sacParser);
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals(".sel1 { padding: 0 !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(1);
        Assert.assertEquals(".sel2 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(2);
        Assert.assertEquals(".sel3 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(3);
        Assert.assertEquals(".sel4 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(4);
        Assert.assertEquals(".important { font-weight: bold }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
    }

    private ErrorHandler parserError(final Parser sacParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        sacParser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(
                new StringReader(".foo { font-weight: normal !/* comment */important; }"));

        sacParser.parseStyleSheet(source);
        return errorHandler;
    }
}
