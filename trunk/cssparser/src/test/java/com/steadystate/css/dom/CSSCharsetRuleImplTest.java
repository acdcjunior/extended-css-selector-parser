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
 * Unit tests for {@link CSSCharsetRuleImpl}.
 *
 * @author rbri
 */
public class CSSCharsetRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"utf-8\";");

        Assert.assertEquals("@charset \"utf-8\";", value.getCssText());
        Assert.assertEquals(CSSRule.CHARSET_RULE, value.getType());
        Assert.assertEquals("@charset \"utf-8\";", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"utf-8\";");

        Assert.assertEquals("@charset \"utf-8\";", value.getCssText());
        Assert.assertEquals(CSSRule.CHARSET_RULE, value.getType());
        Assert.assertEquals("@charset \"utf-8\";", value.toString());

        value.setCssText("@charset \"ASCII\";");
        Assert.assertEquals("@charset \"ASCII\";", value.getCssText());
        Assert.assertEquals(CSSRule.CHARSET_RULE, value.getType());
        Assert.assertEquals("@charset \"ASCII\";", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"Deutsch\";");
        Assert.assertEquals("Deutsch", value.getEncoding());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setEncoding() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"UTF-8\";");
        Assert.assertEquals("UTF-8", value.getEncoding());

        value.setEncoding("EBCDIC");
        Assert.assertEquals("EBCDIC", value.getEncoding());
        Assert.assertEquals("@charset \"EBCDIC\";", value.getCssText());
        Assert.assertEquals("@charset \"EBCDIC\";", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSCharsetRuleImpl value = new CSSCharsetRuleImpl();

        Assert.assertEquals(CSSRule.CHARSET_RULE, value.getType());
        Assert.assertEquals("@charset \"\";", value.toString());
    }

    private CSSCharsetRuleImpl parseCharsetRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null, null);

        final CSSCharsetRuleImpl value = (CSSCharsetRuleImpl) ss.getCssRules().item(0);
        return value;
    }
}
