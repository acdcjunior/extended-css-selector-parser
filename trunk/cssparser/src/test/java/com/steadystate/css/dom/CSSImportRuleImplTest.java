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
 * Unit tests for {@link CSSImportRuleImpl}.
 *
 * @author rbri
 */
public class CSSImportRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        Assert.assertEquals("@import url(ext.css);", value.getCssText());
        Assert.assertEquals(CSSRule.IMPORT_RULE, value.getType());
        Assert.assertEquals("@import url(ext.css);", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        Assert.assertEquals("@import url(ext.css);", value.getCssText());
        Assert.assertEquals(CSSRule.IMPORT_RULE, value.getType());
        Assert.assertEquals("@import url(ext.css);", value.toString());

        value.setCssText("@import url(cool.css);");
        Assert.assertEquals("@import url(cool.css);", value.getCssText());
        Assert.assertEquals(CSSRule.IMPORT_RULE, value.getType());
        Assert.assertEquals("@import url(cool.css);", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getHref() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");
        Assert.assertEquals("ext.css", value.getHref());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setHref() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");
        Assert.assertEquals("ext.css", value.getHref());

        value.setHref("coOl");
        Assert.assertEquals("coOl", value.getHref());
        Assert.assertEquals("@import url(coOl);", value.getCssText());
        Assert.assertEquals("@import url(coOl);", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSImportRuleImpl value = new CSSImportRuleImpl();

        Assert.assertEquals(CSSRule.IMPORT_RULE, value.getType());
        Assert.assertEquals("@import;", value.toString());
    }

    private CSSImportRuleImpl parseImportRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null, null);

        final CSSImportRuleImpl value = (CSSImportRuleImpl) ss.getCssRules().item(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        Assert.assertEquals("@import url(ext.css);", value.getCssText());
        Assert.assertEquals("@import url(ext.css);", value.getCssText(null));
        Assert.assertEquals("@import url(ext.css);", value.getCssText(new CSSFormat()));
    }
}
