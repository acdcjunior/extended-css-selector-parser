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

package com.steadystate.css.format;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.parser.AbstractSACParserTest;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * @author rbri
 */
public class CSSFormatTest  extends AbstractSACParserTest {

    @Override
    protected SACParserCSS3 sacParser() {
        return new SACParserCSS3();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simple() throws Exception {
        final String css = "p { background: green; }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.item(0);
        Assert.assertEquals("p { background: green }", rule.getCssText());
        Assert.assertEquals("p { background: green }", rule.getCssText(new CSSFormat()));
        Assert.assertEquals("p { background: green }", rule.getCssText(new CSSFormat().setRgbAsHex(true)));
        Assert.assertEquals("p { background: green }",
                rule.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void classSelector() throws Exception {
        final String css = ".info { display: none; }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.item(0);
        Assert.assertEquals("*.info { display: none }", rule.getCssText());
        Assert.assertEquals("*.info { display: none }", rule.getCssText(new CSSFormat()));
        Assert.assertEquals("*.info { display: none }", rule.getCssText(new CSSFormat().setRgbAsHex(true)));
        Assert.assertEquals(".info { display: none }",
                rule.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }
}
