/*
 * $Id: CSSOMParserTest.java,v 1.7 2008-03-26 01:27:18 sdanig Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSOMParserTest.java,v 1.7 2008-03-26 01:27:18 sdanig Exp $
 */
public class CSSOMParserTest extends TestCase {

    private String _testSelector = "FOO";

    private String _testItem = "color";

    private String _testValue = "rgb(1, 2, 3)";

    private String _testString = _testSelector + "{ " + _testItem + ": " + _testValue + " }";

    private CSSOMParser _parser = new CSSOMParser();

    public void testParseStyleSheet() throws IOException {

        Reader r = new StringReader(_testString);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = _parser.parseStyleSheet(is, null, null);

        CSSRuleList rl = ss.getCssRules();
        CSSRule rule = rl.item(0);
        if (rule.getType() == CSSRule.STYLE_RULE) {
            CSSStyleRule sr = (CSSStyleRule) rule;
            assertEquals(sr.getSelectorText(), _testSelector);

            CSSStyleDeclaration style = sr.getStyle();
            assertEquals(style.item(0), _testItem);

            CSSValue value = style.getPropertyCSSValue(style.item(0));
            assertEquals(value.getCssText(), _testValue);
        } else {
            fail();
        }
    }

    public void testParseSelectors() throws Exception {
        Reader r = new StringReader(_testSelector);
        InputSource is = new InputSource(r);
        SelectorList sl = _parser.parseSelectors(is);
        assertEquals(sl.item(0).toString(), _testSelector);
    }

    /**
     * Regression test for bug 1191376.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testParseStyleDeclaration() throws Exception {
        Reader r = new StringReader("background-color: white");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration declaration = _parser.parseStyleDeclaration(is);
        assertEquals(declaration.getLength(), 1);
    }

    /**
     * Regression test for bug 1183734.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testColorFirst() throws Exception {

        Reader r = new StringReader("background: #e8eff5 url(images/bottom-angle.png) no-repeat");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration d = _parser.parseStyleDeclaration(is);
        assertEquals("background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat", d.getCssText());

        Reader r2 = new StringReader("background: red url(images/bottom-angle.png) no-repeat");
        InputSource is2 = new InputSource(r2);
        CSSStyleDeclaration d2 = _parser.parseStyleDeclaration(is2);
        assertEquals("background: red url(images/bottom-angle.png) no-repeat", d2.getCssText());

        Reader r3 = new StringReader("background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat");
        InputSource is3 = new InputSource(r3);
        CSSStyleDeclaration d3 = _parser.parseStyleDeclaration(is3);
        assertEquals("background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat", d3.getCssText());
    }

    /**
     * Regression test for bug 1226128.
     * 
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#escaped-characters">CSS Spec</a>
     * @throws Exception
     *             if an error occurs
     */
    public void testEscapedChars() throws Exception {
        testEscapedChars(new SACParserCSS2());
        testEscapedChars(new SACParserCSS21());
    }

    private void testEscapedChars(Parser p) throws Exception {
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\rbc'"));
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\nbc'"));
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\fbc'"));
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'abc\\\r\n'"));
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\r\nbc'"));
        assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\\62x'"));
        assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: '\\61\\62\\63'"));
        assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61 \\62x'"));
        assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\t\\62x'"));
        assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\n\\62x'"));
        assertEquals("bogus: \"a'bc\"", getCssTextFromDeclaration(p, "bogus: 'a\\'bc'"));
        assertEquals("bogus: \"a'bc\"", getCssTextFromDeclaration(p, "bogus: \"a\\'bc\""));
        assertEquals("bogus: \"a\"bc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\"bc'"));
        assertEquals("bogus: \"a\"bc\"", getCssTextFromDeclaration(p, "bogus: \"a\\\"bc\""));
    }

    private String getCssTextFromDeclaration(Parser p, String s) throws Exception {
        CSSOMParser parser = new CSSOMParser(p);
        Reader r = new StringReader(s);
        InputSource is = new InputSource(r);
        CSSStyleDeclaration d = parser.parseStyleDeclaration(is);
        return d.getCssText();
    }

    public void testMisc() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("page_test.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser();
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
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
    }

}
