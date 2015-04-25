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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

/**
/**
 * Unit tests for {@link CSSPageRuleImpl}.
 *
 * @author rbri
 */
public class CSSFontFaceRuleImplTest {

    private String stdParser_;

    @Before
    public void before() {
        stdParser_ = System.getProperty("org.w3c.css.sac.parser");
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");
    }

    @After
    public void after() {
        if (null == stdParser_) {
            System.clearProperty("org.w3c.css.sac.parser");
        }
        else {
            System.setProperty("org.w3c.css.sac.parser", stdParser_);
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");

        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.getCssText());
        Assert.assertEquals(CSSRule.FONT_FACE_RULE, value.getType());
        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");

        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.getCssText());
        Assert.assertEquals(CSSRule.FONT_FACE_RULE, value.getType());
        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.toString());

        value.setCssText("@font-face { font-family: 'Ariel'; font-style: 'cute'; }");
        Assert.assertEquals("@font-face {font-family: \"Ariel\"; font-style: \"cute\"}", value.getCssText());
        Assert.assertEquals(CSSRule.FONT_FACE_RULE, value.getType());
        Assert.assertEquals("@font-face {font-family: \"Ariel\"; font-style: \"cute\"}", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getStyle() throws Exception {
        CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");
        Assert.assertEquals("font-family: \"Scarface\"", value.getStyle().toString());

        value = parseFontFaceRule("@font-face { font-style: cute; }");
        Assert.assertEquals("font-style: cute", value.getStyle().toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSFontFaceRuleImpl value = new CSSFontFaceRuleImpl();

        Assert.assertEquals(CSSRule.FONT_FACE_RULE, value.getType());
        Assert.assertEquals("@font-face {}", value.toString());
    }

    private CSSFontFaceRuleImpl parseFontFaceRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null, null);

        final CSSFontFaceRuleImpl value = (CSSFontFaceRuleImpl) ss.getCssRules().item(0);
        return value;
    }
}
