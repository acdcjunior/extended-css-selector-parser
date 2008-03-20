/*
 * TestDOM_1.java
 *
 * Steady State CSS2 Parser
 *
 * Copyright (C) 1999, 2002 Steady State Software Ltd.  All rights reserved.
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
 * To contact the authors of the library, write to Steady State Software Ltd.,
 * 49 Littleworth, Wing, Buckinghamshire, LU7 0JX, England
 *
 * http://www.steadystate.com/css/
 * mailto:css@steadystate.co.uk
 *
 * $Id: TestDOM2.java,v 1.2 2008-03-20 02:49:41 sdanig Exp $
 */

package com.steadystate.css;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a few operations upon it.
 * 
 * @author David Schweinsberg
 * @version $Release$
 */
public class TestDOM2 extends TestCase {

    public void test() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("test.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        CSSOMParser parser = new CSSOMParser();
        CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = stylesheet.getCssRules();

        for (int i = 0; i < rules.getLength(); i++) {
            CSSRule rule = rules.item(i);
            System.out.println(rule.getCssText());
        }

        // Do some modifications and output the results

        // Style Rules
        rules.item(9).setCssText("apple { color: green }"); // GOOD
        // rules.item(9).setCssText("@font-face { src: url(null) }"); // BAD

        CSSRule rule = rules.item(9);
        System.out.println(rule.getCssText());

        ((CSSStyleRule) rules.item(9)).setSelectorText("banana"); // GOOD

        System.out.println(rule.getCssText());

        ((CSSStyleRule) rules.item(9)).setSelectorText("banana, orange tangerine, grapefruit"); // GOOD

        System.out.println(rule.getCssText());

        ((CSSStyleRule) rules.item(9)).getStyle().setCssText(
            "{ color: red green brown; smell: sweet, sour; taste: sweet/tart }"); // GOOD

        System.out.println(rule.getCssText());

        // Import rules
        stylesheet.insertRule("@import \"thing.css\";", 0); // GOOD
        // stylesheet.insertRule("@import \"thing.css\";", 10); // BAD

        rule = rules.item(0);
        System.out.println(rule.getCssText());

        ((CSSImportRule) rules.item(0)).setCssText("@import \"thing-hack.css\";");

        System.out.println(rule.getCssText());

        // Font-face rules
        stylesheet.insertRule("@font-face { src: \"#foo\" }", 10); // GOOD

        rule = rules.item(10);
        System.out.println(rule.getCssText());

        ((CSSFontFaceRule) rules.item(10)).setCssText("@font-face { src: \"#bar\" }"); // GOOD
        // ((CSSFontFaceRule)rules.item(10)).setCssText("@import \"thing-hack.css\";"); // BAD

        System.out.println(rule.getCssText());

        // Media rules

    }
}
