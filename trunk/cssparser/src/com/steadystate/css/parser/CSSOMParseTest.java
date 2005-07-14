/*
 * $Id: CSSOMParseTest.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2005 David Schweinsberg.  All rights reserved.
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
 */
 
package com.steadystate.css.parser;

import java.io.*;
import org.w3c.css.sac.*;
import org.w3c.dom.css.*;

/** 
 * TODO Move this to a test suite
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSOMParseTest.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 */
public class CSSOMParseTest extends Object {

    /** Creates new CSSOMParseTest */
    public CSSOMParseTest() {
        try {
            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\page_test.css");
//            Reader r = new StringReader("FOO { color: rgb(1,2,3) }");
            InputSource is = new InputSource(r);
            CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet ss = parser.parseStyleSheet(is);
            System.out.println(ss.toString());
            
            CSSRuleList rl = ss.getCssRules();
            for (int i = 0; i < rl.getLength(); i++) {
                CSSRule rule = rl.item(i);
                if (rule.getType() == CSSRule.STYLE_RULE) {
                    CSSStyleRule sr = (CSSStyleRule) rule;
                    CSSStyleDeclaration style = sr.getStyle();
                    for (int j = 0; j < style.getLength(); j++) {
                        CSSValue value = style.getPropertyCSSValue(style.item(j));
                        if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
                            CSSPrimitiveValue pv = (CSSPrimitiveValue) value;
                            System.out.println(">> " + pv.toString());
                            if (pv.getPrimitiveType() == CSSPrimitiveValue.CSS_COUNTER) {
                                System.out.println("CSS_COUNTER(" + pv.toString() + ")");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        new CSSOMParseTest();
    }
}