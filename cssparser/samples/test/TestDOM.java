/*
 * TestDOM.java
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
 * $Id: TestDOM.java,v 1.1.1.1 2003-12-28 21:22:41 davidsch Exp $
 */

package test;

import java.io.*;
import com.steadystate.css.*;
import org.w3c.dom.*;
import org.w3c.dom.css.*;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a
 * few operations upon it.
 *
 * @author David Schweinsberg
 * @version $Release$
 */
public class TestDOM {

    public static void main(String[] args) throws ParseException {
        try {
            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\basic.css");
            CSS2Parser parser = new CSS2Parser(r);
/*
            CSSStyleSheet stylesheet = parser.styleSheet();
            CSSRuleList rules = stylesheet.getCssRules();

            for (int i = 0; i < rules.getLength(); i++) {
                CSSRule rule = rules.item(i);
                System.out.println(rule.getCssText());
            }
*/
            CSSStyleDeclaration n = parser.styleDeclaration();

            // Enumerate the properties and retrieve their values
            System.out.println("No. of properties: " + n.getLength());

            for (int i = 0; i < n.getLength(); i++) {
                String name = n.item(i);
                System.out.println(name + " : " + n.getPropertyValue(name));
            }

            // Get the style declaration as a single lump of text
            System.out.println("\ngetCssText");
            System.out.println(n.getCssText());

            // Directly set the CSS style declaration
            // NOTE: We must include the braces!
            System.out.println("\nsetCssText");
            n.setCssText("{ alpha: 2; beta: 20px; gamma: 40em; delta: 1mm; epsilon: 24pt }");

            System.out.println(n.getCssText());

            // Remove some properties, from the middle, beginning, and end
            System.out.println();
            System.out.println("Removing 'gamma'");
            System.out.println(n.removeProperty("gamma"));
            System.out.println("Removing 'alpha'");
            System.out.println(n.removeProperty("alpha"));
            System.out.println("Removing 'epsilon'");
            System.out.println(n.removeProperty("epsilon"));

            // Let's see what remains
            System.out.println(n.getCssText());

            // Use the setProperty method to modify an existing property,
            // and add a new one.
            System.out.println();
            System.out.println("setting 'beta' to 40px");
            n.setProperty("beta", "40px", null);
            System.out.println("setting 'omega' to 1 with 'important' priority");
            n.setProperty("omega", "1", "important");

            // Let's look at the changes
            System.out.println(n.getCssText());

            // Work with CSSValues
            System.out.println();
            System.out.println("Retrieving 'beta' as a CSSPrimitiveValue");
            CSSPrimitiveValue value = (CSSPrimitiveValue) n.getPropertyCSSValue("beta");
            System.out.println("getCssText: 'beta' = " + value.getCssText());
            System.out.println("getFloatValue: 'beta' = " + value.getFloatValue(CSSPrimitiveValue.CSS_PX));
            System.out.println("Setting 'beta' to 100px");
            value.setFloatValue(CSSPrimitiveValue.CSS_PX, 100);

            System.out.println("Adding a new value, which should end-up as a CSSValueList.\nSetting 'list-test' to 100 200 300");
            n.setProperty("list-test", "100 200 300", null);
            value = (CSSPrimitiveValue) n.getPropertyCSSValue("list-test");
            System.out.println("getValueType: 'list-test' = " + value.getCssValueType());

            CSSValueList vl = (CSSValueList) n.getPropertyCSSValue("list-test");
            for (int i = 0; i < vl.getLength(); i++) {
                System.out.println(
                "getFloatValue: 'list-test[" +
                String.valueOf( i ) +
                "]' = " +
                ((CSSPrimitiveValue)vl.item(i)).getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
            }

            // When a CSSValue is modified, it modifies the declaration
            System.out.println("Let's see the change within the entire declaration");
            System.out.println(n.getCssText());

            // Using the setCssText method, we can change the type of value
            System.out.println("Setting 'list-test' to something completely different, the string 'bogus'.");
            vl.setCssText("bogus");

            System.out.println("What happens...");
            System.out.println("getValueType: 'list-test' = " + value.getCssValueType());
            System.out.println(n.getCssText());
        } catch (Exception e) {
            System.out.println("Error.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
