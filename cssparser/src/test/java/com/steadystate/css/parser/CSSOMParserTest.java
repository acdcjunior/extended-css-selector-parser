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
import org.w3c.css.sac.SACMediaList;
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

    private String _testStyleDeclaration = "align: right"; 
    private String _testParseMedia = "print, screen";
    private String _testParseRule = "p { " + _testStyleDeclaration + " }";
    private String _testSelector = "FOO";
    private String _testItem = "color";
    private String _testValue = "rgb(1, 2, 3)";
    private String _testString = _testSelector + "{ " + _testItem + ": " + _testValue + " }";
    private String _testPropertyValue = "sans-serif";
    private CSSOMParser _parser = new CSSOMParser();

    public void testDefaultConstructor() {
        CSSOMParser parser = new CSSOMParser();
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    public void testDefineParserClass() {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS21");
        CSSOMParser parser = new CSSOMParser();
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));

        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS2");
        parser = new CSSOMParser();
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS2", System.getProperty("org.w3c.css.sac.parser"));

        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS1");
        parser = new CSSOMParser();
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS1", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    public void testDefineParserInstance() {
        CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));

        parser = new CSSOMParser(new SACParserCSS2());
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS2", System.getProperty("org.w3c.css.sac.parser"));

        parser = new CSSOMParser(new SACParserCSS1());
        assertNotNull(parser);
        assertEquals("com.steadystate.css.parser.SACParserCSS1", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    public void testParseStyleSheet() throws IOException {
        Reader r = new StringReader(_testString);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = _parser.parseStyleSheet(is, null, null);

        CSSRuleList rl = ss.getCssRules();
        CSSRule rule = rl.item(0);
        if (rule.getType() == CSSRule.STYLE_RULE) {
            CSSStyleRule sr = (CSSStyleRule) rule;
            assertEquals(_testSelector, sr.getSelectorText());

            CSSStyleDeclaration style = sr.getStyle();
            assertEquals(_testItem, style.item(0));

            CSSValue value = style.getPropertyCSSValue(style.item(0));
            assertEquals(_testValue, value.getCssText());
        } else {
            fail();
        }
    }

    
    public void testParseStyleSheetIgnoreProblem() throws IOException {
        String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        Reader r = new StringReader(test);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = _parser.parseStyleSheet(is, null, null);
        CSSRuleList rl = ss.getCssRules();
        assertEquals(2, rl.getLength());

        CSSRule rule = rl.item(0);
        CSSStyleRule sr = (CSSStyleRule) rule;
        assertEquals("p {  }", sr.getCssText());

        rule = rl.item(1);
        sr = (CSSStyleRule) rule;
        assertEquals("a { color: rgb(18, 52, 86) }", sr.getCssText());
    }
    
    
    
    public void testParseSelectors() throws Exception {
        Reader r = new StringReader(_testSelector);
        InputSource is = new InputSource(r);
        SelectorList sl = _parser.parseSelectors(is);
        assertEquals(_testSelector, sl.item(0).toString());
    }

    public void testParseSelectorsParseException() throws Exception {
        Reader r = new StringReader("table:bogus(1) td");
        InputSource is = new InputSource(r);
        SelectorList sl = _parser.parseSelectors(is);

        assertNull(sl);
    }

    public void testParsePropertyValue() throws Exception {
        Reader r = new StringReader(_testPropertyValue);
        InputSource is = new InputSource(r);
        CSSValue pv = _parser.parsePropertyValue(is);
        assertEquals(_testPropertyValue, pv.toString());
    }

    public void testParsePropertyValueParseException() throws Exception {
        Reader r = new StringReader("@a");
        InputSource is = new InputSource(r);
        CSSValue pv = _parser.parsePropertyValue(is);

        assertNull(pv);
    }
    
    public void testParseMedia() throws Exception {
        Reader r = new StringReader(_testParseMedia);
        InputSource is = new InputSource(r);
        SACMediaList ml = _parser.parseMedia(is);

        assertEquals(2, ml.getLength());
    }

    public void testParseMediaParseException() throws Exception {
        Reader r = new StringReader("~xx");
        InputSource is = new InputSource(r);
        SACMediaList ml = _parser.parseMedia(is);

        assertEquals(0, ml.getLength());
    }

    public void testParseRule() throws Exception {
        Reader r = new StringReader(_testParseRule);
        InputSource is = new InputSource(r);
        CSSRule rule = _parser.parseRule(is);

        assertEquals(_testParseRule, rule.getCssText());
    }

    public void testParseRuleParseException() throws Exception {
        Reader r = new StringReader("~xx");
        InputSource is = new InputSource(r);
        CSSRule rule = _parser.parseRule(is);

        assertNull(rule);
    }

    public void testParseStyleDeclaration() throws Exception {
        Reader r = new StringReader(_testStyleDeclaration);
        InputSource is = new InputSource(r);
        CSSStyleDeclaration sd = _parser.parseStyleDeclaration(is);
        assertEquals(_testStyleDeclaration, sd.toString());
    }

    public void testParseStyleDeclarationParseException() throws Exception {
        Reader r = new StringReader("@abc");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration sd = _parser.parseStyleDeclaration(is);

        assertEquals(sd.getLength(), 0);
    }
    
    /**
     * Regression test for bug 1191376.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testParseStyleDeclarationWithoutBrace() throws Exception {
        Reader r = new StringReader("background-color: white");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration declaration = _parser.parseStyleDeclaration(is);
        assertEquals(1, declaration.getLength());
    }

    /**
     * Regression test for bug 3293695.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testUrlGreedy() throws Exception {
        assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));

        assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));
    }

    /**
     * Regression test for bug 2042900.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testCommaList() throws Exception {
        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));

        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));
    }
    
    /**
     * Regression test for bug 1183734.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testColorFirst() throws Exception {
        assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: red url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));

        assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: red url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));
    }

    public void testSpeciaChars() throws Exception {
        assertEquals(
            "content: \"•\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '•';"));
        assertEquals(
            "content: \"\u0122\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0122';"));
        assertEquals(
            "content: \"\u0422\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0422';"));

        assertEquals(
            "content: \"•\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '•';"));
        assertEquals(
            "content: \"\u0122\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0122';"));
        assertEquals(
            "content: \"\u0422\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0422';"));
    }
    
    /**
     * Regression test for bug 1659992.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testDoubleDotSelector() throws Exception {
        testDoubleDotSelector(new SACParserCSS2());
        testDoubleDotSelector(new SACParserCSS21());
    }
    
    private void testDoubleDotSelector(Parser p) throws Exception {
        Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 2796824.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testImportEOF() throws Exception {
        testImportEOF(new SACParserCSS2());
        testImportEOF(new SACParserCSS21());
    }
    
    private void testImportEOF(Parser p) throws Exception {
        Reader r = new StringReader("@import http://www.wetator.org");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);
        assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 3198584.
     * 
     * @throws Exception
     *             if any error occurs
     */
    public void testImportWithoutClosingSemicolon() throws Exception {
        testImportWithoutClosingSemicolon(new SACParserCSS2());
        testImportWithoutClosingSemicolon(new SACParserCSS21());
    }
    
    private void testImportWithoutClosingSemicolon(Parser p) throws Exception {
        Reader r = new StringReader("@import url('a.css'); @import url('c.css')");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        // second rule is not detected, because the closing semicolon is missed
        assertEquals(1, ss.getCssRules().getLength());
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
        assertEquals("bogus: \"a\\\"bc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\"bc'"));

        // regression for 2891851
        assertEquals("bogus: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus: 'NNNNN\\-NNNN'"));
        assertEquals("bogus: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus: \"NNNNN\\-NNNN\""));
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
                        // System.out.println(">> " + pv.toString());
                        if (pv.getPrimitiveType() == CSSPrimitiveValue.CSS_COUNTER) {
                            // System.out.println("CSS_COUNTER(" + pv.toString() + ")");
                        }
                    }
                }
            }
        }
    }

}
