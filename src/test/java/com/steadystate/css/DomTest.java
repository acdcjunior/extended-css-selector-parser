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
package com.steadystate.css;

import com.steadystate.css.parser.CSSOMParser;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.StringReader;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a few operations upon it.
 *
 * @author David Schweinsberg
 * @author rbri
 */
public class DomTest {

    @Test
    public void inheritGetStringValue() throws Exception {
        final String cssText = "p { font-size: 2em } p a:link { font-size: inherit }";
        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSOMParser cssomParser = new CSSOMParser();

        final CSSStyleSheet css = cssomParser.parseStyleSheet(source, null, "http://www.example.org/css/style.css");

        final CSSRuleList rules = css.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        Assert.assertEquals("p { font-size: 2em }", rules.item(0).getCssText());
        Assert.assertEquals("p a:link { font-size: inherit }", rules.item(1).getCssText());
    }
}
