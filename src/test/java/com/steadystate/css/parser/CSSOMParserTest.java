/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2011 David Schweinsberg.  All rights reserved.
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

    private String testStyleDeclaration_ = "align: right";
    private String testParseMedia_ = "print, screen";
    private String testParseRule_ = "p { " + testStyleDeclaration_ + " }";
    private String testSelector_ = "FOO";
    private String testItem_ = "color";
    private String testValue_ = "rgb(1, 2, 3)";
    private String testString_ = testSelector_ + "{ " + testItem_ + ": " + testValue_ + " }";
    private String testPropertyValue_ = "sans-serif";

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
        final CSSOMParser parser = new CSSOMParser();
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
        final Reader r = new StringReader(testString_);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        final CSSRuleList rl = ss.getCssRules();
        final CSSRule rule = rl.item(0);

        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        final CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals(testSelector_, sr.getSelectorText());

        final CSSStyleDeclaration style = sr.getStyle();
        Assert.assertEquals(testItem_, style.item(0));

        final CSSValue value = style.getPropertyCSSValue(style.item(0));
        Assert.assertEquals(testValue_, value.getCssText());
    }

    @Test
    public void parseStyleSheetIgnoreProblemCss2() throws Exception {
        final String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        final Reader r = new StringReader(test);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = getCss2Parser().parseStyleSheet(is, null, null);
        final CSSRuleList rl = ss.getCssRules();
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
        final String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        final Reader r = new StringReader(test);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);
        final CSSRuleList rl = ss.getCssRules();
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
        final Reader r = new StringReader(testSelector_);
        final InputSource is = new InputSource(r);
        final SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertEquals(testSelector_, sl.item(0).toString());
    }

    @Test
    public void parseSelectorsParseException() throws Exception {
        final Reader r = new StringReader("table:bogus(1) td");
        final InputSource is = new InputSource(r);
        final SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertNull(sl);
    }

    @Test
    public void parsePropertyValue() throws Exception {
        final Reader r = new StringReader(testPropertyValue_);
        final InputSource is = new InputSource(r);
        final CSSValue pv = getCss21Parser().parsePropertyValue(is);

        Assert.assertEquals(testPropertyValue_, pv.toString());
    }

    @Test
    public void parsePropertyValueParseException() throws Exception {
        final Reader r = new StringReader("@a");
        final InputSource is = new InputSource(r);
        final CSSValue pv = getCss21Parser().parsePropertyValue(is);

        Assert.assertNull(pv);
    }

    @Test
    public void parseMedia() throws Exception {
        final Reader r = new StringReader(testParseMedia_);
        final InputSource is = new InputSource(r);
        final SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(2, ml.getLength());
    }

    @Test
    public void parseMediaParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(0, ml.getLength());
    }

    @Test
    public void parseRule() throws Exception {
        final Reader r = new StringReader(testParseRule_);
        final InputSource is = new InputSource(r);
        final CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertEquals(testParseRule_, rule.getCssText());
    }

    @Test
    public void parseRuleParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertNull(rule);
    }

    @Test
    public void parseStyleDeclaration() throws Exception {
        final Reader r = new StringReader(testStyleDeclaration_);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration sd = getCss21Parser().parseStyleDeclaration(is);

        Assert.assertEquals(testStyleDeclaration_, sd.toString());
    }

    @Test
    public void parseStyleDeclarationParseException() throws Exception {
        final Reader r = new StringReader("@abc");
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration sd = getCss21Parser().parseStyleDeclaration(is);

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
        final Reader r = new StringReader("background-color: white");
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration declaration = getCss21Parser().parseStyleDeclaration(is);

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
            getCssTextFromDeclaration(
                    new SACParserCSS2(),
                    "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(
                    new SACParserCSS2(),
                    "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        Assert.assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; "
            + "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(
                    new SACParserCSS2(),
                    "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;"
                    + "font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));

        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(
                    new SACParserCSS21(),
                    "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(
                    new SACParserCSS21(),
                    "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        Assert.assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; "
            + "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(
                    new SACParserCSS21(),
                    "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;"
                    + "font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));
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
            getCssTextFromDeclaration(new SACParserCSS2(),
                    "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));

        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(),
                    "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new SACParserCSS21(),
                    "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));
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
            getCssTextFromDeclaration(new SACParserCSS2(),
                    "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new SACParserCSS2(), "background: red url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new SACParserCSS2(),
                    "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));

        Assert.assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new SACParserCSS21(),
                    "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new SACParserCSS21(), "background: red url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new SACParserCSS21(),
                    "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));
    }

    @Test
    public void speciaChars() throws Exception {
        Assert.assertEquals("content: \"�\"",
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '�';"));
        Assert.assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0122';"));
        Assert.assertEquals("content: \"\u0422\"",
            getCssTextFromDeclaration(new SACParserCSS2(), "content: '\u0422';"));

        Assert.assertEquals("content: \"�\"",
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '�';"));
        Assert.assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0122';"));
        Assert.assertEquals("content: \"\u0422\"",
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

    private void doubleDotSelector(final Parser p) throws Exception {
        final Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser(p);
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

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

    private void importEOF(final Parser p) throws Exception {
        final Reader r = new StringReader("@import http://www.wetator.org");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser(p);
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

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

    private void importWithoutClosingSemicolon(final Parser p) throws Exception {
        final Reader r = new StringReader("@import url('a.css'); @import url('c.css')");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser(p);
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

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

    private void escapedChars(final Parser p) throws Exception {
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

    private String getCssTextFromDeclaration(final Parser p, final String s) throws Exception {
        final CSSOMParser parser = new CSSOMParser(p);
        final Reader r = new StringReader(s);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration d = parser.parseStyleDeclaration(is);
        return d.getCssText();
    }

    @Test
    public void parsePageDeclaration() throws Exception {
        final Reader r = new StringReader("@page :pageStyle { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", ss.toString().trim());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());

        final CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals(":pageStyle", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }

    @Test
    public void parsePageDeclaration2() throws Exception {
        final Reader r = new StringReader("@page { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = getCss21Parser().parseStyleSheet(is, null, null);

        Assert.assertEquals("@page {size: 21cm 29.7cm}", ss.toString().trim());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page {size: 21cm 29.7cm}", rule.getCssText());

        final CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals("", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }
}
