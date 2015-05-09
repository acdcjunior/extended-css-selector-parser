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

package com.steadystate.css.dom;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS21;

/**
/**
 * Unit tests for {@link CSSMediaRuleImpl}.
 *
 * @author rbri
 */
public class CSSMediaRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        Assert.assertEquals("@media print {body { font-size: 10pt } }", mediaRule.getCssText());
        Assert.assertEquals(CSSRule.MEDIA_RULE, mediaRule.getType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRule() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        mediaRule.insertRule(".testStyle { height: 42px; }", 0);
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().item(0).getCssText());

        mediaRule.insertRule(".testStyle { height: 43px; }", 0);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().item(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().item(1).getCssText());

        mediaRule.insertRule(".testStyle { height: 44px; }", 2);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().item(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().item(1).getCssText());
        Assert.assertEquals("*.testStyle { height: 44px }", mediaRule.getCssRules().item(2).getCssText());

        mediaRule.insertRule(".testStyle { height: 45px; }", 2);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().item(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().item(1).getCssText());
        Assert.assertEquals("*.testStyle { height: 45px }", mediaRule.getCssRules().item(2).getCssText());
        Assert.assertEquals("*.testStyle { height: 44px }", mediaRule.getCssRules().item(3).getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithLeadingWhitespace() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        mediaRule.insertRule(" .testStyleDef { height: 42px; }", 0);
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().item(0).getCssText());

        mediaRule.insertRule("      .testStyleDef { height: 43px;}   ", 0);
        Assert.assertEquals("*.testStyleDef { height: 43px }", mediaRule.getCssRules().item(0).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().item(1).getCssText());

        mediaRule.insertRule("\t.testStyleDef { height: 44px; }\r\n", 0);
        Assert.assertEquals("*.testStyleDef { height: 44px }", mediaRule.getCssRules().item(0).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 43px }", mediaRule.getCssRules().item(1).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().item(2).getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithoutDeclaration() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        try {
            mediaRule.insertRule(".testStyleDef", 0);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Syntax error"));
            Assert.assertEquals(0, mediaRule.getCssRules().getLength());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRule() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        mediaRule.deleteRule(0);
        Assert.assertEquals(0, mediaRule.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRuleWrongIndex() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        try {
            mediaRule.deleteRule(7);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Index out of bounds error"));
            Assert.assertEquals(0, mediaRule.getCssRules().getLength());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void asString() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().item(0);

        Assert.assertEquals("@media print {body { font-size: 10pt } }", mediaRule.toString());
    }
}
