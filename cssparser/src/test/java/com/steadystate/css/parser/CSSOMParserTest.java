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

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @author sdanig
 * @author exxws
 */
public class CSSOMParserTest {

    private String _testStyleDeclaration = "align: right"; 
    private String _testParseMedia = "print, screen";
    private String _testParseRule = "p { " + _testStyleDeclaration + " }";
    private String _testSelector = "FOO";
    private String _testItem = "color";
    private String _testValue = "rgb(1, 2, 3)";
    private String _testString = _testSelector + "{ " + _testItem + ": " + _testValue + " }";
    private String _testPropertyValue = "sans-serif";

    private CSSOMParser getCss21Parser() {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS21");
        return new CSSOMParser();
    }
    
    private CSSOMParser getCss2Parser() {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS20");
        return new CSSOMParser();
    }
    
    @Test
    public void defaultConstructor() {
        CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    @Test
    public void defineParserClass() {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS21");
        CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));

        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS2");
        parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS2", System.getProperty("org.w3c.css.sac.parser"));

        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS1");
        parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS1", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    @Test
    public void defineParserInstance() {
        CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));

        parser = new CSSOMParser(new SACParserCSS2());
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS2", System.getProperty("org.w3c.css.sac.parser"));

        parser = new CSSOMParser(new SACParserCSS1());
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS1", System.getProperty("org.w3c.css.sac.parser"));
    }
    
    @Test
    public void parseStyleSheet() throws Exception {
        Reader r = new StringReader(_testString);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        CSSRuleList rl = ss.getCssRules();
        CSSRule rule = rl.item(0);

        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals(_testSelector, sr.getSelectorText());

        CSSStyleDeclaration style = sr.getStyle();
        Assert.assertEquals(_testItem, style.item(0));

        CSSValue value = style.getPropertyCSSValue(style.item(0));
        Assert.assertEquals(_testValue, value.getCssText());
    }

    @Test
    public void parseStyleSheetIgnoreProblemCss2() throws Exception {
        String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        Reader r = new StringReader(test);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = getCss2Parser().parseStyleSheet(is, null, null);
        CSSRuleList rl = ss.getCssRules();
        Assert.assertEquals(2, rl.getLength());

        CSSRule rule = rl.item(0);
        CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals("p {  }", sr.getCssText());

        rule = rl.item(1);
        sr = (CSSStyleRule) rule;
        Assert.assertEquals("a { color: rgb(18, 52, 86) }", sr.getCssText());
    }
    
    @Test
    public void parseStyleSheetIgnoreProblemCss21() throws Exception {
        String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";
        
        Reader r = new StringReader(test);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);
        CSSRuleList rl = ss.getCssRules();
        Assert.assertEquals(2, rl.getLength());

        CSSRule rule = rl.item(0);
        CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals("p {  }", sr.getCssText());

        rule = rl.item(1);
        sr = (CSSStyleRule) rule;
        Assert.assertEquals("a { color: rgb(18, 52, 86) }", sr.getCssText());
    }

    @Test
    public void parseSelectors() throws Exception {
        Reader r = new StringReader(_testSelector);
        InputSource is = new InputSource(r);
        SelectorList sl = getCss21Parser().parseSelectors(is);
        Assert.assertEquals(_testSelector, sl.item(0).toString());
    }

    @Test
    public void parseSelectorsParseException() throws Exception {
        Reader r = new StringReader("table:bogus(1) td");
        InputSource is = new InputSource(r);
        SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertNull(sl);
    }

    @Test
    public void parsePropertyValue() throws Exception {
        Reader r = new StringReader(_testPropertyValue);
        InputSource is = new InputSource(r);
        CSSValue pv = getCss21Parser().parsePropertyValue(is);
        Assert.assertEquals(_testPropertyValue, pv.toString());
    }

    @Test
    public void parsePropertyValueParseException() throws Exception {
        Reader r = new StringReader("@a");
        InputSource is = new InputSource(r);
        CSSValue pv = getCss21Parser().parsePropertyValue(is);

        Assert.assertNull(pv);
    }
    
    @Test
    public void parseMedia() throws Exception {
        Reader r = new StringReader(_testParseMedia);
        InputSource is = new InputSource(r);
        SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(2, ml.getLength());
    }

    @Test
    public void parseMediaParseException() throws Exception {
        Reader r = new StringReader("~xx");
        InputSource is = new InputSource(r);
        SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(0, ml.getLength());
    }

    @Test
    public void parseRule() throws Exception {
        Reader r = new StringReader(_testParseRule);
        InputSource is = new InputSource(r);
        CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertEquals(_testParseRule, rule.getCssText());
    }

    @Test
    public void parseRuleParseException() throws Exception {
        Reader r = new StringReader("~xx");
        InputSource is = new InputSource(r);
        CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertNull(rule);
    }

    @Test
    public void parseStyleDeclaration() throws Exception {
        Reader r = new StringReader(_testStyleDeclaration);
        InputSource is = new InputSource(r);
        CSSStyleDeclaration sd = getCss21Parser().parseStyleDeclaration(is);

        Assert.assertEquals(_testStyleDeclaration, sd.toString());
    }

    @Test
    public void parseStyleDeclarationParseException() throws Exception {
        Reader r = new StringReader("@abc");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration sd = getCss21Parser().parseStyleDeclaration(is);

        Assert.assertEquals(sd.getLength(), 0);
    }
    
    /**
     * Regression test for bug 1191376.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void parseStyleDeclarationWithoutBrace() throws Exception {
        Reader r = new StringReader("background-color: white");
        InputSource is = new InputSource(r);
        CSSStyleDeclaration declaration = getCss21Parser().parseStyleDeclaration(is);

        Assert.assertEquals(1, declaration.getLength());
    }

    /**
     * Regression test for bug 3293695.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void urlGreedy() throws Exception {
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        Assert.assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));

        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        Assert.assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));
    }

    /**
     * Regression test for bug 2042900.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void commaList() throws Exception {
        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS2(), "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));

        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(), "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));
    }
    
    /**
     * Regression test for bug 1183734.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void colorFirst() throws Exception {
        Assert.assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: red url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS2(), "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));

        Assert.assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: red url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat", 
            getCssTextFromDeclaration(new SACParserCSS21(), "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));
    }

    @Test
    public void speciaChars() throws Exception {
        Assert.assertEquals(
            "content: \"•\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '•';"));
        Assert.assertEquals(
            "content: \"\u0122\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0122';"));
        Assert.assertEquals(
            "content: \"\u0422\"", 
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0422';"));

        Assert.assertEquals(
            "content: \"•\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '•';"));
        Assert.assertEquals(
            "content: \"\u0122\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0122';"));
        Assert.assertEquals(
            "content: \"\u0422\"", 
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0422';"));
    }
    
    /**
     * Regression test for bug 1659992.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void doubleDotSelector() throws Exception {
        doubleDotSelector(new SACParserCSS2());
        doubleDotSelector(new SACParserCSS21());
    }
    
    private void doubleDotSelector(Parser p) throws Exception {
        Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 2796824.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void importEOF() throws Exception {
        importEOF(new SACParserCSS2());
        importEOF(new SACParserCSS21());
    }
    
    private void importEOF(Parser p) throws Exception {
        Reader r = new StringReader("@import http://www.wetator.org");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 3198584.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void importWithoutClosingSemicolon() throws Exception {
        importWithoutClosingSemicolon(new SACParserCSS2());
        importWithoutClosingSemicolon(new SACParserCSS21());
    }
    
    private void importWithoutClosingSemicolon(Parser p) throws Exception {
        Reader r = new StringReader("@import url('a.css'); @import url('c.css')");
        InputSource source = new InputSource(r);
        CSSOMParser parser = new CSSOMParser(p);
        CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        // second rule is not detected, because the closing semicolon is missed
        Assert.assertEquals(1, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 1226128.
     * 
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#escaped-characters">CSS Spec</a>
     * @throws Exception
     *             if an error occurs
     */
    @Test
    public void escapedChars() throws Exception {
        escapedChars(new SACParserCSS2());
        escapedChars(new SACParserCSS21());
    }

    private void escapedChars(Parser p) throws Exception {
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\rbc'"));
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\nbc'"));
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\fbc'"));
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'abc\\\r\n'"));
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\r\nbc'"));
        Assert.assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\\62x'"));
        Assert.assertEquals("bogus: \"abc\"", getCssTextFromDeclaration(p, "bogus: '\\61\\62\\63'"));
        Assert.assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61 \\62x'"));
        Assert.assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\t\\62x'"));
        Assert.assertEquals("bogus: \"abx\"", getCssTextFromDeclaration(p, "bogus: '\\61\n\\62x'"));
        Assert.assertEquals("bogus: \"a'bc\"", getCssTextFromDeclaration(p, "bogus: 'a\\'bc'"));
        Assert.assertEquals("bogus: \"a'bc\"", getCssTextFromDeclaration(p, "bogus: \"a\\'bc\""));
        Assert.assertEquals("bogus: \"a\\\"bc\"", getCssTextFromDeclaration(p, "bogus: 'a\\\"bc'"));

        // regression for 2891851
        Assert.assertEquals("bogus: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus: 'NNNNN\\-NNNN'"));
        Assert.assertEquals("bogus: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus: \"NNNNN\\-NNNN\""));
    }

    private String getCssTextFromDeclaration(Parser p, String s) throws Exception {
        CSSOMParser parser = new CSSOMParser(p);
        Reader r = new StringReader(s);
        InputSource is = new InputSource(r);
        CSSStyleDeclaration d = parser.parseStyleDeclaration(is);
        return d.getCssText();
    }

    @Test
    public void parsePageDeclaration() throws Exception {
        Reader r = new StringReader("@page :pageStyle { size: 21.0cm 29.7cm; }");
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", ss.toString().trim());
        
        CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());
        
        CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());

        CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals(":pageStyle", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }

    @Test
    public void parsePageDeclaration2() throws Exception {
        Reader r = new StringReader("@page { size: 21.0cm 29.7cm; }");
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        Assert.assertEquals("@page {size: 21cm 29.7cm}", ss.toString().trim());
        
        CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());
        
        CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page {size: 21cm 29.7cm}", rule.getCssText());

        CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals("", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }
}
