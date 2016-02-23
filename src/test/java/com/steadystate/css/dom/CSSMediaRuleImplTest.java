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

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.StringReader;

/**
/**
 * Unit tests for {@link CSSMediaRuleImpl}.
 *
 * @author rbri
 */
public class CSSMediaRuleImplTest {

    /**
     * Regression test for bug #56.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleNot() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        final CSSMediaRule mediaRule = (CSSMediaRule) ss.getCssRules().item(0);

        mediaRule.insertRule("li:not(.shiny) { height: 44px }", 0);
        Assert.assertEquals("li:not(.shiny) { height: 44px }", mediaRule.getCssRules().item(0).getCssText());

        try {
            mediaRule.insertRule("li:not(*.shiny) { height: 44px }", 0);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Syntax error"));
            Assert.assertEquals(1, mediaRule.getCssRules().getLength());
        }
    }

}
