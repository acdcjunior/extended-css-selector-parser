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
 * $Id: TestDOM_1.java,v 1.2 2007-08-14 09:43:30 waldbaer Exp $
 */

package test;

import java.io.*;
import org.w3c.dom.css.*;
import org.w3c.css.sac.*;
import com.steadystate.css.parser.*;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a
 * few operations upon it.
 *
 * @author David Schweinsberg
 * @version $Release$
 */
public class TestDOM_1 {

    public static void main(String[] args) {
        try {
            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\test.css");
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);

            CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);
            CSSRuleList rules = stylesheet.getCssRules();

            for (int i = 0; i < rules.getLength(); i++) {
                CSSRule rule = rules.item(i);
                System.out.println(rule.getCssText());
            }
            
            // Do some modifications and output the results
            
            // Style Rules
            rules.item(9).setCssText("apple { color: green }"); // GOOD
//            rules.item(9).setCssText("@font-face { src: url(null) }"); // BAD

            CSSRule rule = rules.item(9);
            System.out.println(rule.getCssText());

            ((CSSStyleRule)rules.item(9)).setSelectorText("banana"); // GOOD

            System.out.println(rule.getCssText());

            ((CSSStyleRule)rules.item(9)).setSelectorText("banana, orange tangerine, grapefruit"); // GOOD

            System.out.println(rule.getCssText());

            ((CSSStyleRule)rules.item(9)).getStyle().setCssText("{ color: red green brown; smell: sweet, sour; taste: sweet/tart }"); // GOOD

            System.out.println(rule.getCssText());
            
            // Import rules
            stylesheet.insertRule("@import \"thing.css\";", 0); // GOOD
//            stylesheet.insertRule("@import \"thing.css\";", 10); // BAD

            rule = rules.item(0);
            System.out.println(rule.getCssText());
            
            ((CSSImportRule)rules.item(0)).setCssText("@import \"thing-hack.css\";");

            System.out.println(rule.getCssText());

            // Font-face rules
            stylesheet.insertRule("@font-face { src: \"#foo\" }", 10); // GOOD

            rule = rules.item(10);
            System.out.println(rule.getCssText());

            ((CSSFontFaceRule)rules.item(10)).setCssText("@font-face { src: \"#bar\" }"); // GOOD
//            ((CSSFontFaceRule)rules.item(10)).setCssText("@import \"thing-hack.css\";"); // BAD

            System.out.println(rule.getCssText());
            
            // Media rules

        } catch (Exception e) {
            System.out.println("Error.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
