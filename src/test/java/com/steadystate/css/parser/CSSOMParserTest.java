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

package com.steadystate.css.parser;

import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.*;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @author sdanig
 * @author rbri
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

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defineParserClass() throws Exception {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS21");
        CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defineParserClassWrongClass() throws Exception {
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS0");
        final CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS0", System.getProperty("org.w3c.css.sac.parser"));

        // this creates a working parser
        final String test = "p { color:#123456; }";
        final Reader r = new StringReader(test);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = parser.parseStyleSheet(is, null, null);
        final CSSRuleList rl = ss.getCssRules();
        Assert.assertEquals(1, rl.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defineParserInstance() throws Exception {
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS3", System.getProperty("org.w3c.css.sac.parser"));

        parser = new CSSOMParser(new SACParserCSS21());
        Assert.assertNotNull(parser);
        Assert.assertEquals("com.steadystate.css.parser.SACParserCSS21", System.getProperty("org.w3c.css.sac.parser"));
    }

    /**
     * @throws Exception if any error occurs
     */
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

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectors() throws Exception {
        final Reader r = new StringReader(testSelector_);
        final InputSource is = new InputSource(r);
        final SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertEquals(testSelector_, sl.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsEscapedChars() throws Exception {
        final Reader r = new StringReader("#id\\:withColon");
        final InputSource is = new InputSource(r);
        final SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertEquals("#id:withColon", sl.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsParseException() throws Exception {
        final Reader r = new StringReader("table:bogus(1) td");
        final InputSource is = new InputSource(r);
        final SelectorList sl = getCss21Parser().parseSelectors(is);

        Assert.assertNull(sl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValue() throws Exception {
        final Reader r = new StringReader(testPropertyValue_);
        final InputSource is = new InputSource(r);
        final CSSValue pv = getCss21Parser().parsePropertyValue(is);

        Assert.assertEquals(testPropertyValue_, pv.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValueParseException() throws Exception {
        final Reader r = new StringReader("@a");
        final InputSource is = new InputSource(r);
        final CSSValue pv = getCss21Parser().parsePropertyValue(is);

        Assert.assertNull(pv);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMedia() throws Exception {
        final Reader r = new StringReader(testParseMedia_);
        final InputSource is = new InputSource(r);
        final SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(2, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMediaParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final SACMediaList ml = getCss21Parser().parseMedia(is);

        Assert.assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRule() throws Exception {
        final Reader r = new StringReader(testParseRule_);
        final InputSource is = new InputSource(r);
        final CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertEquals(testParseRule_, rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRuleParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final CSSRule rule = getCss21Parser().parseRule(is);

        Assert.assertNull(rule);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleDeclaration() throws Exception {
        final Reader r = new StringReader(testStyleDeclaration_);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration sd = getCss21Parser().parseStyleDeclaration(is);

        Assert.assertEquals(testStyleDeclaration_, sd.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
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
     * @throws Exception if any error occurs
     */
    @Test
    public void speciaChars() throws Exception {
        Assert.assertEquals("content: \"�\"",
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '�';"));
        Assert.assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0122';"));
        Assert.assertEquals("content: \"\u0422\"",
            getCssTextFromDeclaration(new SACParserCSS21(), "content: '\u0422';"));

        Assert.assertEquals("content: \"�\"",
                getCssTextFromDeclaration(new SACParserCSS3(), "content: '�';"));
        Assert.assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new SACParserCSS3(), "content: '\u0122';"));
        Assert.assertEquals("content: \"\u0422\"",
            getCssTextFromDeclaration(new SACParserCSS3(), "content: '\u0422';"));
    }

    /**
     * Regression test for bug 1659992.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void doubleDotSelector() throws Exception {
        doubleDotSelector(new SACParserCSS21());
        doubleDotSelector(new SACParserCSS3());
    }

    private void doubleDotSelector(final Parser p) throws Exception {
        final Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser(p);
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, ss.getCssRules().getLength());
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
        escapedChars(new SACParserCSS21());
        escapedChars(new SACParserCSS3());
    }

    private void escapedChars(final Parser p) throws Exception {
        Assert.assertEquals("bogus0: \"abc\"", getCssTextFromDeclaration(p, "bogus0: 'a\\\rbc'"));
        Assert.assertEquals("bogus1: \"abc\"", getCssTextFromDeclaration(p, "bogus1: 'a\\\nbc'"));
        Assert.assertEquals("bogus2: \"abc\"", getCssTextFromDeclaration(p, "bogus2: 'a\\\fbc'"));
        Assert.assertEquals("bogus3: \"abc\"", getCssTextFromDeclaration(p, "bogus3: 'abc\\\r\n'"));
        Assert.assertEquals("bogus4: \"abc\"", getCssTextFromDeclaration(p, "bogus4: 'a\\\r\nbc'"));
        Assert.assertEquals("bogus5: \"abx\"", getCssTextFromDeclaration(p, "bogus5: '\\61\\62x'"));
        Assert.assertEquals("bogus6: \"abc\"", getCssTextFromDeclaration(p, "bogus6: '\\61\\62\\63'"));
        Assert.assertEquals("bogus7: \"abx\"", getCssTextFromDeclaration(p, "bogus7: '\\61 \\62x'"));
        Assert.assertEquals("bogus8: \"abx\"", getCssTextFromDeclaration(p, "bogus8: '\\61\t\\62x'"));
        Assert.assertEquals("bogus9: \"abx\"", getCssTextFromDeclaration(p, "bogus9: '\\61\n\\62x'"));
        Assert.assertEquals("bogus10: \"a'bc\"", getCssTextFromDeclaration(p, "bogus10: 'a\\'bc'"));
        Assert.assertEquals("bogus11: \"a'bc\"", getCssTextFromDeclaration(p, "bogus11: \"a\\'bc\""));
        Assert.assertEquals("bogus12: \"a\\\"bc\"", getCssTextFromDeclaration(p, "bogus12: 'a\\\"bc'"));

        // regression for 2891851
        // double backslashes are needed
        // see http://www.developershome.com/wap/wcss/wcss_tutorial.asp?page=inputExtension2
        Assert.assertEquals("bogus13: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus13: 'NNNNN\\\\-NNNN'"));
        Assert.assertEquals("bogus14: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus14: \"NNNNN\\\\-NNNN\""));
    }

    private String getCssTextFromDeclaration(final Parser p, final String s) throws Exception {
        final CSSOMParser parser = new CSSOMParser(p);
        final Reader r = new StringReader(s);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration d = parser.parseStyleDeclaration(is);
        return d.getCssText();
    }

    /**
     * @throws Exception if any error occurs
     */
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

    /**
     * @throws Exception if any error occurs
     */
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

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void overwriteProperties() throws Exception {
        final Reader r = new StringReader(
                "p {"
                + "background: rgb(0, 0, 0);"
                + "background-repeat: repeat-y;"
                + "background: url(img/test.png) no-repeat;"
                + "background-size: 190px 48px;"
                + "}");
        final InputSource is = new InputSource(r);
        final CSSStyleSheet sheet = getCss21Parser().parseStyleSheet(is, null, null);

        Assert.assertEquals("p { background: rgb(0, 0, 0); "
                + "background-repeat: repeat-y; "
                + "background: url(img/test.png) no-repeat; "
                + "background-size: 190px 48px }",
                sheet.toString().trim());

        final CSSRuleList rules = sheet.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = (CSSStyleDeclarationImpl) ruleImpl.getStyle();

        Assert.assertEquals(4, declImpl.getLength());

        Assert.assertEquals("background", declImpl.item(0));
        Assert.assertEquals("url(img/test.png) no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        Assert.assertEquals("background-repeat", declImpl.item(1));
        Assert.assertEquals("repeat-y", declImpl.getPropertyCSSValue("background-repeat").getCssText());

        Assert.assertEquals("background", declImpl.item(2));
        Assert.assertEquals("url(img/test.png) no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        Assert.assertEquals("background-size", declImpl.item(3));
        Assert.assertEquals("190px 48px", declImpl.getPropertyCSSValue("background-size").getCssText());

        // now check the core results
        Assert.assertEquals(4, declImpl.getProperties().size());

        Property prop = declImpl.getProperties().get(0);
        Assert.assertEquals("background", prop.getName());
        Assert.assertEquals("rgb(0, 0, 0)", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(1);
        Assert.assertEquals("background-repeat", prop.getName());
        Assert.assertEquals("repeat-y", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(2);
        Assert.assertEquals("background", prop.getName());
        Assert.assertEquals("url(img/test.png) no-repeat", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(3);
        Assert.assertEquals("background-size", prop.getName());
        Assert.assertEquals("190px 48px", prop.getValue().getCssText());
    }
}
