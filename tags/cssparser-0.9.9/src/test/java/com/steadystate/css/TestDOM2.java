/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2011 David Schweinsberg.  All rights reserved.
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
package com.steadystate.css;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS2;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a few operations upon it.
 *
 * @author David Schweinsberg
 * @author rbri
 */
public class TestDOM2 {

    @Test
    public void test() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("test.css");
        Assert.assertNotNull(is);

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        // set CSS2 parser because this test contains test for features
        // no longer supported by CSS21
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS2());
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        final CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(3, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(3, errorHandler.getWarningCount());

        final CSSRuleList rules = stylesheet.getCssRules();
        Assert.assertEquals(78, rules.getLength());

        CSSRule rule = rules.item(9);
        Assert.assertEquals("H1, H2 { color: green; background-color: blue }", rule.getCssText());

        // Do some modifications and output the results

        // Style Rules
        rule = rules.item(9);
        rule.setCssText("apple { color: green }");
        Assert.assertEquals("apple { color: green }", rule.getCssText());

        ((CSSStyleRule) rule).setSelectorText("banana");
        Assert.assertEquals("banana { color: green }", rule.getCssText());

        ((CSSStyleRule) rule).setSelectorText("orange tangerine, grapefruit");
        Assert.assertEquals("orange tangerine, grapefruit { color: green }", rule.getCssText());

        ((CSSStyleRule) rule).getStyle().setCssText(
            "color: red green brown; smell: sweet, sour; taste: sweet/tart");
        Assert.assertEquals(
                "orange tangerine, grapefruit { color: red green brown; smell: sweet, sour; taste: sweet/ tart }",
                rule.getCssText());

        rule = rules.item(0);
        Assert.assertEquals("@import url(foo.css);", rule.getCssText());

        // Import rules
        stylesheet.insertRule("@import \"thing.css\";", 0);
        Assert.assertEquals(79, rules.getLength());
        rule = rules.item(0);
        Assert.assertEquals("@import url(thing.css);", rule.getCssText());

        ((CSSImportRule) rule).setCssText("@import \"thing-hack.css\";");
        rule = rules.item(0);
        Assert.assertEquals("@import url(thing-hack.css);", rule.getCssText());

        // Font-face rules
        stylesheet.insertRule("@font-face { src: \"#foo\" }", 10);
        Assert.assertEquals(80, rules.getLength());
        rule = rules.item(10);
        Assert.assertEquals("@font-face {src: \"#foo\"}", rule.getCssText());

        ((CSSFontFaceRule) rules.item(10)).setCssText("@font-face { src: \"#bar\" }");
        Assert.assertEquals("@font-face {src: \"#bar\"}", rule.getCssText());
    }
}
