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
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

/**
/**
 * Unit tests for {@link CSSStyleRuleImpl}.
 *
 * @author rbri
 */
public class CSSStyleRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");

        Assert.assertEquals("h1 { color: blue }", value.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, value.getType());
        Assert.assertEquals("h1 { color: blue }", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");

        Assert.assertEquals("h1 { color: blue }", value.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, value.getType());
        Assert.assertEquals("h1 { color: blue }", value.toString());

        value.setCssText("p { width: 10px };");
        Assert.assertEquals("p { width: 10px }", value.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, value.getType());
        Assert.assertEquals("p { width: 10px }", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");
        Assert.assertEquals("h1", value.getSelectorText());

        value = parseStyleRule("h1, h2,\r\nb { color: blue }");
        Assert.assertEquals("h1, h2, b", value.getSelectorText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSStyleRuleImpl value = new CSSStyleRuleImpl();

        Assert.assertEquals(CSSRule.STYLE_RULE, value.getType());
        Assert.assertEquals("", value.toString());
    }

    private CSSStyleRuleImpl parseStyleRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null, null);

        final CSSStyleRuleImpl value = (CSSStyleRuleImpl) ss.getCssRules().item(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1{color:blue}");

        Assert.assertEquals("h1 { color: blue }", value.getCssText());
        Assert.assertEquals("h1 { color: blue }", value.getCssText(null));
        Assert.assertEquals("h1 { color: blue }", value.getCssText(new CSSFormat()));
    }
}
