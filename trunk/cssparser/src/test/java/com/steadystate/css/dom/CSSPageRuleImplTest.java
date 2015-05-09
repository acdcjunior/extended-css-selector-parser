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
 * Unit tests for {@link CSSPageRuleImpl}.
 *
 * @author rbri
 */
public class CSSPageRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.getCssText());
        Assert.assertEquals(CSSRule.PAGE_RULE, value.getType());
        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.getCssText());
        Assert.assertEquals(CSSRule.PAGE_RULE, value.getType());
        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.toString());

        value.setCssText("@page :pseudo { color: blue }");
        Assert.assertEquals("@page :pseudo {color: blue}", value.getCssText());
        Assert.assertEquals(CSSRule.PAGE_RULE, value.getType());
        Assert.assertEquals("@page :pseudo {color: blue}", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        Assert.assertEquals("", value.getSelectorText());

        value = parsePageRule("@page :pseudo {color: blue}");
        Assert.assertEquals(":pseudo", value.getSelectorText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPseudoPage() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        Assert.assertEquals("", value.getSelectorText());

        value.setPseudoPage("test");
        Assert.assertEquals(":test", value.getSelectorText());
        Assert.assertEquals("@page :test {size: 21cm 29.7cm}", value.getCssText());
        Assert.assertEquals("@page :test {size: 21cm 29.7cm}", value.toString());

        value = parsePageRule("@page :pseudo {color: blue}");
        Assert.assertEquals(":pseudo", value.getSelectorText());

        value.setPseudoPage("test");
        Assert.assertEquals(":test", value.getSelectorText());
        Assert.assertEquals("@page :test {color: blue}", value.getCssText());
        Assert.assertEquals("@page :test {color: blue}", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getStyle() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        Assert.assertEquals("size: 21cm 29.7cm", value.getStyle().toString());

        value = parsePageRule("@page :pseudo {color: blue}");
        Assert.assertEquals("color: blue", value.getStyle().toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSPageRuleImpl value = new CSSPageRuleImpl();

        Assert.assertEquals(CSSRule.PAGE_RULE, value.getType());
        Assert.assertEquals("@page {}", value.toString());
    }

    private CSSPageRuleImpl parsePageRule(final String pageRule) throws Exception {
        final InputSource is = new InputSource(new StringReader(pageRule));
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null, null);

        final CSSPageRuleImpl value = (CSSPageRuleImpl) ss.getCssRules().item(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.getCssText());
        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.getCssText(null));
        Assert.assertEquals("@page {size: 21cm 29.7cm}", value.getCssText(new CSSFormat()));
    }
}
