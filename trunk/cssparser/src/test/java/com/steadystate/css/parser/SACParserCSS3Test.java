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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.ErrorHandler;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;
import com.steadystate.css.parser.selectors.ChildSelectorImpl;
import com.steadystate.css.parser.selectors.ConditionalSelectorImpl;
import com.steadystate.css.parser.selectors.LangConditionImpl;
import com.steadystate.css.parser.selectors.PrefixAttributeConditionImpl;
import com.steadystate.css.parser.selectors.PseudoClassConditionImpl;
import com.steadystate.css.parser.selectors.SubstringAttributeConditionImpl;
import com.steadystate.css.parser.selectors.SuffixAttributeConditionImpl;

/**
 * @author Ahmed Ashour
 * @author rbri
 */
public class SACParserCSS3Test  extends AbstractSACParserTest {

    @Override
    protected AbstractSACParser sacParser() {
        return new SACParserCSS3();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorList() throws Exception {
        selectorList("h1:first-line", 1);
        selectorList("h1", 1);
        selectorList("h1, h2", 2);
        selectorList("h1:first-line, h2", 2);
        selectorList("h1, h2, h3", 3);
        selectorList("h1, h2,\nh3", 3);
        selectorList("h1, h2, h3#id", 3);
        selectorList("h1.class, h2, h3", 3);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selector() throws Exception {
        selectorType("a#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a", Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(":link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:visited", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:active", Selector.SAC_CONDITIONAL_SELECTOR);

        selectorType("h1 a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("h1  a", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorPseudo() throws Exception {
        selectorType("h1:first-line", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:first-letter", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:before", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:after", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1:lang(en)", Selector.SAC_CONDITIONAL_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorPseudoDoubleColon() throws Exception {
        selectorType("h1::first-line", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::first-letter", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::before", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::after", Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1::lang(en)", Selector.SAC_CONDITIONAL_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/selector.html#lang">http://www.w3.org/TR/CSS21/selector.html#lang</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorLang() throws Exception {
        final String css = "html:lang(fr-ca) { }\n"
                + "html:lang(de) { }\n"
                + ":lang(fr) > Q { }\n"
                + ":lang(de) > Q { }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("html:lang(fr-ca) { }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        ConditionalSelectorImpl selector = (ConditionalSelectorImpl) ((CSSStyleRuleImpl) rule).getSelectors().item(0);
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, selector.getCondition().getConditionType());
        Assert.assertEquals("fr-ca", ((LangConditionImpl) selector.getCondition()).getLang());

        rule = rules.item(1);
        Assert.assertEquals("html:lang(de) { }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        selector = (ConditionalSelectorImpl) ((CSSStyleRuleImpl) rule).getSelectors().item(0);
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, selector.getCondition().getConditionType());
        Assert.assertEquals("de", ((LangConditionImpl) selector.getCondition()).getLang());

        rule = rules.item(2);
        Assert.assertEquals("*:lang(fr) > Q { }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        ChildSelectorImpl childSelector = (ChildSelectorImpl) ((CSSStyleRuleImpl) rule).getSelectors().item(0);
        selector = (ConditionalSelectorImpl) childSelector.getAncestorSelector();
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, selector.getCondition().getConditionType());
        Assert.assertEquals("fr", ((LangConditionImpl) selector.getCondition()).getLang());

        rule = rules.item(3);
        Assert.assertEquals("*:lang(de) > Q { }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        childSelector = (ChildSelectorImpl) ((CSSStyleRuleImpl) rule).getSelectors().item(0);
        selector = (ConditionalSelectorImpl) childSelector.getAncestorSelector();
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, selector.getCondition().getConditionType());
        Assert.assertEquals("de", ((LangConditionImpl) selector.getCondition()).getLang());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/selector.html#lang">http://www.w3.org/TR/CSS21/selector.html#lang</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorLangInvalid() throws Exception {
        final String css = "html:lang() { background: red }\n"
                    + "p { color:green; }";

        final CSSStyleSheet sheet = parse(css, 1, 0, 1);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        Assert.assertEquals("p { color: green }", rules.toString().trim());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void condition() throws Exception {
        conditionType("a#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION);
        conditionType("a#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id", Condition.SAC_ID_CONDITION);
        conditionType("a.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a.class", Condition.SAC_CLASS_CONDITION);
        conditionType("a:link", Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class:link", Condition.SAC_AND_CONDITION, Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION);
        conditionType("#id:link", Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id", Condition.SAC_ID_CONDITION);
        conditionType(".class:link", Condition.SAC_AND_CONDITION, Condition.SAC_CLASS_CONDITION,
                Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType(".class", Condition.SAC_CLASS_CONDITION);
        conditionType(":link", Condition.SAC_PSEUDO_CLASS_CONDITION);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void classCondition() throws Exception {
        conditionAssert(".class", null, "class", true);
        conditionAssert("h1.class", null, "class", true);
        Assert.assertNull(createSelectors("."));
        Assert.assertNull(createSelectors("h1."));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void idCondition() throws Exception {
        conditionAssert("#id", null, "id", true);
        conditionAssert("h1#id", null, "id", true);
        Assert.assertNull(createSelectors("#"));
        Assert.assertNull(createSelectors("h1.#"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoCondition() throws Exception {
        conditionAssert(":link", null, "link", true);
        conditionAssert("a:link", null, "link", true);
        conditionAssert("a:visited", null, "visited", true);
        conditionAssert(":visited", null, "visited", true);
        conditionAssert("a:active", null, "active", true);
        conditionAssert(":active", null, "active", true);
        Assert.assertNull(createSelectors(":"));
        Assert.assertNull(createSelectors("a:"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attributeCondition() throws Exception {
        conditionAssert("[rel]", "rel", null, false);
        conditionAssert("[ rel ]", "rel", null, false);

        conditionAssert("[rel=val]", "rel", "val", true);
        conditionAssert("[ rel = val ]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel=]")); // invalid rule

        conditionAssert("[rel~=val]", "rel", "val", true);
        conditionAssert("[ rel ~= val ]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel~=]")); // invalid rule

        conditionAssert("[rel|=val]", "rel", "val", true);
        conditionAssert("[ rel |= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel|=]")); // invalid rule

        conditionAssert("[rel^=val]", "rel", "val", true);
        conditionAssert("[ rel ^= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel^=]")); // invalid rule

        conditionAssert("[rel$=val]", "rel", "val", true);
        conditionAssert("[ rel $= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel$=]")); // invalid rule

        conditionAssert("[rel*=val]", "rel", "val", true);
        conditionAssert("[ rel *= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel*=]")); // invalid rule
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dojoCSS() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        Assert.assertNotNull(is);

        final CSSStyleSheet sheet = parse(is);
        Assert.assertEquals(17, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyCSS() throws Exception {
        final CSSStyleSheet sheet = parse("");
        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespaceOnlyCSS() throws Exception {
        final CSSStyleSheet sheet = parse("  \t \r\n \n");
        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charset() throws Exception {
        final String css = "@charset 'UTF-8';\n"
            + "h1 { color: blue }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetWhitespaceBefore() throws Exception {
        final String css = "/* comment */ \n @charset 'UTF-8';\n"
            + "h1 { color: blue }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetWhitespaceAfter() throws Exception {
        final String css = "@charset 'UTF-8';\n"
            + " \t \n "
            + "h1 { color: blue }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRuleOnly() throws Exception {
        final String css = "@import 'subs.css';";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRulesOnly() throws Exception {
        final String css = "@import 'subs.css'; @import 'subs1.css'; @import 'subs2.css';";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@import url(subs1.css);", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("@import url(subs2.css);", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mediaRulePrint() throws Exception {
        final String css = "@media print { h1 { color: red } }";
        final CSSStyleSheet sheet = parse(css);

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final CSSRule cssRule = rules.item(0);
        Assert.assertEquals("@media print {h1 { color: red } }", cssRule.getCssText());

        final MediaListImpl mediaList = (MediaListImpl) ((CSSMediaRuleImpl) cssRule).getMedia();

        Assert.assertEquals(1, mediaList.getLength());
        Assert.assertEquals("print", mediaList.getMediaText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mediaRulePrintAndScreen() throws Exception {
        final String css = "@media print,screen { h1 { color: red } }";
        final CSSStyleSheet sheet = parse(css);

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final CSSRule cssRule = rules.item(0);
        Assert.assertEquals("@media print, screen {h1 { color: red } }", cssRule.getCssText());

        final MediaListImpl mediaList = (MediaListImpl) ((CSSMediaRuleImpl) cssRule).getMedia();

        Assert.assertEquals(2, mediaList.getLength());
        Assert.assertEquals("print, screen", mediaList.getMediaText());
    }

    /**
     * @see <a href="http://dev.w3.org/csswg/css3-fonts/#font-face-rule">
     *          http://dev.w3.org/csswg/css3-fonts/#font-face-rule</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRuleFontFace() throws Exception {
        final String css = "@font-face { font-family: Gentium; src: url(http://example.com/fonts/Gentium.ttf); }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@font-face {font-family: Gentium; src: url(http://example.com/fonts/Gentium.ttf)}",
                rule.getCssText());
    }

    /**
     * @see <a href="http://dev.w3.org/csswg/css3-fonts/#font-face-rule">
     *          http://dev.w3.org/csswg/css3-fonts/#font-face-rule</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRuleFontFaceComplex() throws Exception {
        final String css = "@font-face {\n"
                + "font-family: Headline;\n"
                + "src: local(Futura-Medium), url(fonts.svg#MyGeometricModern) format(\"svg\");}";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@font-face {font-family: Headline; "
                + "src: local(Futura-Medium), url(fonts.svg#MyGeometricModern) format(\"svg\")}",
                rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#at-rules">
     *          http://www.w3.org/TR/CSS21/syndata.html#at-rules</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRules1() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "h1 { color: blue }\n"
            + "@import 'list.css';\n"
            + "h2 { color: red }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting one of: <S>, \"<!--\", \"-->\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("1", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h2 { color: red }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#at-rules">
     *  http://www.w3.org/TR/CSS21/syndata.html#at-rules</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRules2() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  @import 'print-main.css';\n"
            + "  body { font-size: 10pt }\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * Test for {@literal @}import after a rule
     * @throws Exception
     */
    @Test
    public void atRules2b() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  body { font-size: 10pt }\n"
            + "  @import 'print-main.css';\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("4", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    @Test
    public void hexColor() throws Exception {
        final String cssText = "color: #ccc; background: #1c1d00;";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(2, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("color : rgb(204, 204, 204)", name + " : " + style.getPropertyValue(name));

        name = style.item(1);
        name = style.item(1);
        Assert.assertEquals("background : rgb(28, 29, 0)", name + " : " + style.getPropertyValue(name));
    }

    @Test
    public void rgb() throws Exception {
        final String cssText = "foreground: rgb( 10, 20, 30 )";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("foreground : rgb(10, 20, 30)", name + " : " + style.getPropertyValue(name));
    }

    @Test
    public void rgbInsidefunction() throws Exception {
        final String cssText = "color: foo(#cd4);";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("color: foo(rgb(204, 221, 68))", name + ": " + style.getPropertyValue(name));
    }

    @Test
    public void funct() throws Exception {
        final String cssText = "clip: foo(rect( 10px, 20em, 30px, max(40, blue(rgb(1,2,3))) ) )";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("clip : foo(rect(10px, 20em, 30px, max(40, blue(rgb(1, 2, 3)))))",
                name + " : " + style.getPropertyValue(name));

        final CSSValueImpl value = (CSSValueImpl) style.getPropertyCSSValue(name);
        LexicalUnitImpl unit  = (LexicalUnitImpl) value.getValue();
        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("foo", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnit.SAC_RECT_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("rect", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnit.SAC_PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(10f, unit.getFloatValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_EM, unit.getLexicalUnitType());
        Assert.assertEquals(20f, unit.getFloatValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(30f, unit.getFloatValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("max", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnit.SAC_INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(40, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("blue", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnit.SAC_RGBCOLOR, unit.getLexicalUnitType());
        Assert.assertEquals("rgb", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnit.SAC_INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(1, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(2, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnit.SAC_INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(3, unit.getIntegerValue());

        Assert.assertNull(unit.getNextLexicalUnit());
    }

    @Test
    public void beforeAfter() throws Exception {
        final String cssText = "heading:before { content: attr(test) \"testData\" }";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.item(0);
        Assert.assertEquals("heading:before { content: attr(test) \"testData\" }", rule.getCssText());

        final CSSStyleDeclaration style = rule.getStyle();

        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("content : attr(test) \"testData\"", name + " : " + style.getPropertyValue(name));
    }

    @Test
    public void rect() throws Exception {
        final String cssText = "clip: rect( 10px, 20px, 30px, 40px )";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("clip : rect(10px, 20px, 30px, 40px)", name + " : " + style.getPropertyValue(name));
    }

    @Test
    public void attr() throws Exception {
        final String cssText = "content: attr( data-foo )";

        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleDeclaration style = parser.parseStyleDeclaration(source);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        String name = style.item(0);
        name = style.item(0);
        Assert.assertEquals("content : attr(data-foo)", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/generate.html#counters">
     *          http://www.w3.org/TR/CSS21/generate.html#counters</a>
     * @throws Exception if the test fails
     */
    @Test
    public void counter() throws Exception {
        final String css =
                  "H1:before        { content: counter(chno, upper-latin) \". \" }\n"
                + "H2:before        { content: counter(section, upper-roman) \" - \" }\n"
                + "BLOCKQUOTE:after { content: \" [\" counter(bq, lower-greek) \"]\" }\n"
                + "DIV.note:before  { content: counter(notecntr, disc) \" \" }\n"
                + "P:before         { content: counter(p, none) }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("H1:before { content: counter(chno, upper-latin) \". \" }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(chno, upper-latin)", ((CSSValueImpl) value.item(0)).getCounterValue().toString());

        rule = rules.item(1);
        Assert.assertEquals("H2:before { content: counter(section, upper-roman) \" - \" }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(section, upper-roman)",
                ((CSSValueImpl) value.item(0)).getCounterValue().toString());

        rule = rules.item(2);
        Assert.assertEquals("BLOCKQUOTE:after { content: \" [\" counter(bq, lower-greek) \"]\" }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(bq, lower-greek)", ((CSSValueImpl) value.item(1)).getCounterValue().toString());

        rule = rules.item(3);
        Assert.assertEquals("DIV.note:before { content: counter(notecntr, disc) \" \" }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(notecntr, disc)", ((CSSValueImpl) value.item(0)).getCounterValue().toString());

        rule = rules.item(4);
        Assert.assertEquals("P:before { content: counter(p, none) }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(p, none)", (value.getCounterValue().toString()));
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/generate.html#counters">
     *          http://www.w3.org/TR/CSS21/generate.html#counters</a>
     * @throws Exception if the test fails
     */
    @Test
    public void counters() throws Exception {
        final String css = "LI:before { content: counters(item, \".\") \" \"; counter-increment: item }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("LI:before { content: counters(item, \".\") \" \"; counter-increment: item }",
                rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
        final CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counters(item, \".\")", ((CSSValueImpl) value.item(0)).getCounterValue().toString());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void unknownProperty() throws Exception {
        final String css = "h1 { color: red; rotation: 70minutes }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        // parser accepts this
        Assert.assertEquals("h1 { color: red; rotation: 70minutes }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void illegalValues() throws Exception {
        final String css = "img { float: left } /* correct CSS 2.1 */\n"
                    + "img { float: left here } /* 'here' is not a value of 'float' */\n"
                    + "img { background: \"red\" } /* keywords cannot be quoted */\n"
                    + "img { background: \'red\' } /* keywords cannot be quoted */\n"
                    + "img { border-width: 3 } /* a unit must be specified for length values */\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("img { float: left }", rule.getCssText());

        // parser accepts this
        rule = rules.item(1);
        Assert.assertEquals("img { float: left here }", rule.getCssText());
        rule = rules.item(2);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.item(3);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.item(4);
        Assert.assertEquals("img { border-width: 3 }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void malformedDeclaration() throws Exception {
        final String css = "p { color:green }\n"
                    + "p { color:green; color } /* malformed declaration missing ':', value */\n"
                    + "p { color:red;   color; color:green } /* same with expected recovery */\n"
                    + "p { color:green; color: } /* malformed declaration missing value */\n"
                    + "p { color:red;   color:; color:green } /* same with expected recovery */\n"
                    + "p { color:green; color{;color:maroon} } /* unexpected tokens { } */\n"
                    + "p { color:red;   color{;color:maroon}; color:green } /* same with recovery */\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(7, errorHandler.getErrorCount());
        final String expected = "Error in declaration. (Invalid token \"}\". Was expecting one of: <S>, \":\".)"
                + " Error in declaration. (Invalid token \";\". Was expecting one of: <S>, \":\".)"
                + " Error in expression. (Invalid token \"}\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <URI>, <FUNCTION>.)"
                + " Error in expression. (Invalid token \";\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <URI>, <FUNCTION>.)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)"
                + " Error in style rule. (Invalid token \" \". Was expecting one of: <EOF>, \"}\", \";\".)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2 3 4 5 6 6 7", errorHandler.getErrorLines());
        Assert.assertEquals("24 23 25 24 23 38 23", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertTrue(errorHandler.getWarningMessage(),
                errorHandler.getWarningMessage().startsWith("Ignoring the following declarations in this rule."));
        Assert.assertEquals("6", errorHandler.getWarningLines());
        Assert.assertEquals("38", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(7, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        // parser accepts this
        rule = rules.item(1);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(2);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
        rule = rules.item(3);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(4);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
        rule = rules.item(5);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.item(6);
        Assert.assertEquals("p { color: red; color: green }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void malformedStatements() throws Exception {
        final String css = "p { color:green }\n"
                    + "p @here {color:red} /* ruleset with unexpected at-keyword '@here' */\n"
                    + "@foo @bar; /* at-rule with unexpected at-keyword '@bar' */\n"
                    // TODO + "}} {{ - }} /* ruleset with unexpected right brace */\n"
                    // TODO + ") ( {} ) p {color: red } /* ruleset with unexpected right parenthesis */\n"
                    + "p { color:blue; }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in style rule. "
                + "(Invalid token \"@here\". Was expecting one of: <S>, <LBRACE>, <COMMA>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the following declarations in this rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("3", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("@foo @bar;", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("p { color: blue }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRulesWithUnknownAtKeywords() throws Exception {
        final String css = "@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30deg;\n"
                            + "    elevation: 190deg;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }\n"
                            + "  h1 { color: blue }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30;\n"
                            + "    elevation: 190;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void unexpectedEndOfStyleSheet() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: ";
        Assert.assertTrue(errorHandler.getErrorMessage(), errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("27", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("27", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@media screen {p:before { content: Hello } }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unexpectedEndOfMediaRule() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: ";
        Assert.assertTrue(errorHandler.getErrorMessage(), errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("29", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("29", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@media screen {p:before { content: Hello } }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unexpectedEndOfPageRule() throws Exception {
        final String css = "@page :pageStyle { size: 21.0cm 29.7cm;";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @page rule. "
                + "(Invalid token \"<EOF>\". Was expecting one of: <S>, <IDENT>, \"}\", \";\", \"*\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("1", errorHandler.getErrorLines());
        Assert.assertEquals("39", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("1", errorHandler.getWarningLines());
        Assert.assertEquals("39", errorHandler.getWarningColumns());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     *
     * @throws Exception in case of failure
     */
    @Test
    public void unexpectedEndOfString() throws Exception {
        final String css = "p {\n"
                            + "  color: green;\n"
                            + "  font-family: 'Courier New Times\n"
                            + "  color: red;\n"
                            + "  color: green;\n"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in expression. "
                + "(Invalid token \"\\'\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <URI>, <FUNCTION>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("16", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: green; color: green }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#strings">
     *          http://www.w3.org/TR/CSS21/syndata.html#strings</a>
     *
     * @throws Exception in case of failure
     */
    @Test
    public void strings() throws Exception {
        final String css = "h1 { background: url(\"this is a 'string'\") }\n"
                            + "h2 { background: url(\"this is a \\\"string\\\"\") }\n"
                            + "h4 { background: url('this is a \"string\"') }\n"
                            + "h5 { background: url('this is a \\'string\\'') }"
                            + "h6 { background: url('this is a \\\r\n string') }"
                            + "h1:before { content: 'chapter\\A hoofdstuk\\00000a chapitre' }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(6, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("h1 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("h2 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("h4 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.item(3);
        Assert.assertEquals("h5 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.item(4);
        Assert.assertEquals("h6 { background: url(this is a  string) }", rule.getCssText());

        rule = rules.item(5);
        Assert.assertEquals("h1:before { content: \"chapter\nhoofdstuk\n chapitre\" }", rule.getCssText());
    }

    /**
     * Regression test for bug 1420893.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidCommaInDef() throws Exception {
        final String css = ".a, .b, { test: 1; }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in simple selector."));
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
    }

    /**
     * Regression test for bug 1420893.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void missingValue() throws Exception {
        final String css = ".a { test; }\n"
                           + "p { color: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("*.a { }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("p { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void skipDeklarationsErrorBefore() throws Exception {
        final String css = ".a { test; color: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("*.a { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void skipDeklarationsErrorBetween() throws Exception {
        final String css = ".a { color: blue; test; color: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("*.a { color: blue; color: green }", rule.getCssText());
    }

    /**
     * see https://issues.jboss.org/browse/RF-11741
     * @throws Exception if any error occurs
     */
    @Test
    public void skipJBossIssue() throws Exception {
        final String css = ".shadow {\n"
                + " -webkit-box-shadow: 1px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + " -moz-box-shadow: 2px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + " box-shadow: 3px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = (CSSStyleDeclarationImpl) ruleImpl.getStyle();

        Property prop = declImpl.getPropertyDeclaration("-webkit-box-shadow");
        CSSValueImpl valueImpl = (CSSValueImpl) prop.getValue();
        Assert.assertEquals("1px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());

        prop = declImpl.getPropertyDeclaration("-moz-box-shadow");
        valueImpl = (CSSValueImpl) prop.getValue();
        Assert.assertEquals("2px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());

        prop = declImpl.getPropertyDeclaration("box-shadow");
        valueImpl = (CSSValueImpl) prop.getValue();
        Assert.assertEquals("3px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPercent() throws Exception {
        final CSSValueImpl value = dimension("2%");
        Assert.assertEquals(CSSPrimitiveValue.CSS_PERCENTAGE, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPX() throws Exception {
        final CSSValueImpl value = dimension("3px");
        Assert.assertEquals(CSSPrimitiveValue.CSS_PX, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionCM() throws Exception {
        final CSSValueImpl value = dimension("5cm");
        Assert.assertEquals(CSSPrimitiveValue.CSS_CM, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionMM() throws Exception {
        final CSSValueImpl value = dimension("7mm");
        Assert.assertEquals(CSSPrimitiveValue.CSS_MM, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionIN() throws Exception {
        final CSSValueImpl value = dimension("11in");
        Assert.assertEquals(CSSPrimitiveValue.CSS_IN, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPT() throws Exception {
        final CSSValueImpl value = dimension("13pt");
        Assert.assertEquals(CSSPrimitiveValue.CSS_PT, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionEMS() throws Exception {
        final CSSValueImpl value = dimension("17em");
        Assert.assertEquals(CSSPrimitiveValue.CSS_EMS, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionEXS() throws Exception {
        final CSSValueImpl value = dimension("19ex");
        Assert.assertEquals(CSSPrimitiveValue.CSS_EXS, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionDEG() throws Exception {
        final CSSValueImpl value = dimension("13deg");
        Assert.assertEquals(CSSPrimitiveValue.CSS_DEG, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionRAD() throws Exception {
        final CSSValueImpl value = dimension("99rad");
        Assert.assertEquals(CSSPrimitiveValue.CSS_RAD, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionGRAD() throws Exception {
        final CSSValueImpl value = dimension("31grad");
        Assert.assertEquals(CSSPrimitiveValue.CSS_GRAD, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionMS() throws Exception {
        final CSSValueImpl value = dimension("37ms");
        Assert.assertEquals(CSSPrimitiveValue.CSS_MS, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionS() throws Exception {
        final CSSValueImpl value = dimension("41s");
        Assert.assertEquals(CSSPrimitiveValue.CSS_S, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionHZ() throws Exception {
        final CSSValueImpl value = dimension("43Hz");
        Assert.assertEquals(CSSPrimitiveValue.CSS_HZ, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionKHZ() throws Exception {
        final CSSValueImpl value = dimension("47kHz");
        Assert.assertEquals(CSSPrimitiveValue.CSS_KHZ, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPC() throws Exception {
        dimension("5pc");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void illegalDimension() throws Exception {
        final String css = ".a { top: 0\\9; }"
                + ".b { top: -01.234newDim; }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("*.a { top: 0\\9 }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("*.b { top: -1.234newDim }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void opacity() throws Exception {
        final String css = ".a {\n"
                            + "-ms-filter: \"progid:DXImageTransform.Microsoft.Alpha(Opacity=90)\";\n"
                            + "filter: alpha(opacity=90);\n"
                            + "-moz-opacity: 0.9;\n"
                            + "opacity: 0.9;\n"
                            + "}";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);

        CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("-ms-filter");
        Assert.assertEquals("\"progid:DXImageTransform.Microsoft.Alpha(Opacity=90)\"", value.getCssText());

        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("alpha(opacity = 90)", value.getCssText());

        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("-moz-opacity");
        Assert.assertEquals("0.9", value.getCssText());

        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("opacity");
        Assert.assertEquals("0.9", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradientIE6Style() throws Exception {
        final String css = ".a {\n"
                            + "filter: progid:DXImageTransform.Microsoft."
                                + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9);\n"
                            + "color: green;"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().
                startsWith("Error in expression; ':' found after identifier \"progid\"."));

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);

        CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertNull(value);

        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("color");
        Assert.assertEquals("green", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void evalIEStyle() throws Exception {
        final String css = ".a {\n"
                            + "top: expression((eval(document.documentElement||document.body).scrollTop));\n"
                            + "color: green;"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in expression. (Invalid token \"(\"."));

        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("*.a { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradientIE8Style() throws Exception {
        final String css = ".a {\n"
                            + "-ms-filter: \"progid:DXImageTransform.Microsoft."
                                + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9)\";\n"
                            + "color: green;"
                            + "}";
        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);

        CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("-ms-filter");
        Assert.assertEquals("\"progid:DXImageTransform.Microsoft."
                + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9)\"", value.getCssText());

        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("color");
        Assert.assertEquals("green", value.getCssText());
    }

    @Test
    public void transformRotate() throws Exception {
        final String css = ".flipped {\n"
                + "  transform: rotateY(180deg);\n"
                + "}";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);

        final CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("transform");
        Assert.assertEquals("rotateY(180deg)", value.getCssText());
    }

    @Test
    public void rgba() throws Exception {
        final String css = "p {\n"
                + "  background-color: rgba(0,0,0,0.2);\n"
                + "}";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);

        final CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().
                            getPropertyCSSValue("background-color");
        Assert.assertEquals("rgba(0, 0, 0, 0.2)", value.getCssText());
    }

    @Test
    public void linearGradient() throws Exception {
        final String css = "h1 { background: linear-gradient(top, #fff, #f2f2f2); }\n"
                + "h2 { background: linear-gradient( 45deg, blue, red ); }\n"
                + "h3 { background: linear-gradient( to left top, #00f, red); }\n"
                + "h4 { background: linear-gradient( 0 deg, blue, green 40%, red ); }\n";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        CSSRule rule = rules.item(0);
        CSSValueImpl value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(top, rgb(255, 255, 255), rgb(242, 242, 242))", value.getCssText());

        rule = rules.item(1);
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(45deg, blue, red)", value.getCssText());

        rule = rules.item(2);
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(to left top, rgb(0, 0, 255), red)", value.getCssText());

        rule = rules.item(3);
        value = (CSSValueImpl) ((CSSStyleRule) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(0 deg, blue, green 40%, red)", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void prefixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel^=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof PrefixAttributeConditionImpl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void suffixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel$=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof SuffixAttributeConditionImpl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void substringAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel*=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof SubstringAttributeConditionImpl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_child() throws Exception {
        String cssText = "div:nth-child(0)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-child(2n+1)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-child(2n-1)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-child(odd)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-child(even)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_last_child() throws Exception {
        String cssText = "div:nth-last-child(-n+2)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-last-child(odd)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_of_type() throws Exception {
        String cssText = "div:nth-of-type(2n+1)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());

        cssText = "div:nth-of-type(2n)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_preserveInnerWhitespace() throws Exception {
        SelectorList selectors = createSelectors("div:nth-child( 0 )");
        Assert.assertEquals("div:nth-child(0)", selectors.item(0).toString());

        selectors = createSelectors("div:nth-child( + 4 n )");
        Assert.assertEquals("div:nth-child(+ 4 n)", selectors.item(0).toString());

        selectors = createSelectors("div:nth-child( - 5 n + 2 )");
        Assert.assertEquals("div:nth-child(- 5 n + 2)", selectors.item(0).toString());

        selectors = createSelectors("div:nth-child( - 5     n\t\t+ \t 2 )");
        Assert.assertEquals("div:nth-child(- 5     n\t\t+ \t 2)", selectors.item(0).toString());

        selectors = createSelectors("div:nth-child( odd )");
        Assert.assertEquals("div:nth-child(odd)", selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void generalAdjacentSelector() throws Exception {
        final String cssText = "div ~ hi";
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorTrimWhitespace() throws Exception {
        final String cssText = "  \t\r\n  div > hi  \t\r\n  ";
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText.trim(), selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not() throws Exception {
        // element name
        SelectorList selectors = createSelectors("input:not(abc)");
        Assert.assertEquals("input:not(abc)", selectors.item(0).toString());

        selectors = createSelectors("input:not(*)");
        Assert.assertEquals("input:not(*)", selectors.item(0).toString());

        // hash
        selectors = createSelectors("input:not(#test)");
        Assert.assertEquals("input:not(*#test)", selectors.item(0).toString());

        // class
        selectors = createSelectors("input:not(.home)");
        Assert.assertEquals("input:not(*.home)", selectors.item(0).toString());

        // attrib
        selectors = createSelectors("input:not([title])");
        Assert.assertEquals("input:not(*[title])", selectors.item(0).toString());

        selectors = createSelectors("input:not([type = 'file'])");
        Assert.assertEquals("input:not(*[type=\"file\"])", selectors.item(0).toString());

        selectors = createSelectors("input:not([type ~= 'file'])");
        Assert.assertEquals("input:not(*[type~=\"file\"])", selectors.item(0).toString());

        // pseudo
        selectors = createSelectors("input:not(:last)");
        Assert.assertEquals("input:not(*:last)", selectors.item(0).toString());

        // whitespace
        selectors = createSelectors("input:not( .hi \t)");
        Assert.assertEquals("input:not(*.hi)", selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_elementName() throws Exception {
        final SelectorList selectors = createSelectors("p:not(abc)");
        Assert.assertEquals("p:not(abc)", selectors.item(0).toString());

        Assert.assertEquals(1, selectors.getLength());
        final Selector selector = selectors.item(0);

        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, selector.getSelectorType());

        final ConditionalSelector condSel = (ConditionalSelector) selector;
        final Condition condition = condSel.getCondition();

        Assert.assertEquals(Condition.SAC_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassConditionImpl pseudo = (PseudoClassConditionImpl) condition;
        Assert.assertEquals("not(abc)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_hash() throws Exception {
        final SelectorList selectors = createSelectors("p:not( #test)");
        Assert.assertEquals("p:not(*#test)", selectors.item(0).toString());

        Assert.assertEquals(1, selectors.getLength());
        final Selector selector = selectors.item(0);

        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, selector.getSelectorType());

        final ConditionalSelector condSel = (ConditionalSelector) selector;
        final Condition condition = condSel.getCondition();

        Assert.assertEquals(Condition.SAC_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassConditionImpl pseudo = (PseudoClassConditionImpl) condition;
        Assert.assertEquals("not(*#test)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_class() throws Exception {
        // element name
        final SelectorList selectors = createSelectors("p:not(.klass)");
        Assert.assertEquals("p:not(*.klass)", selectors.item(0).toString());

        Assert.assertEquals(1, selectors.getLength());
        final Selector selector = selectors.item(0);

        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, selector.getSelectorType());

        final ConditionalSelector condSel = (ConditionalSelector) selector;
        final Condition condition = condSel.getCondition();

        Assert.assertEquals(Condition.SAC_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassConditionImpl pseudo = (PseudoClassConditionImpl) condition;
        Assert.assertEquals("not(*.klass)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_attrib() throws Exception {
        final SelectorList selectors = createSelectors("p:not([type='file'])");
        Assert.assertEquals("p:not(*[type=\"file\"])", selectors.item(0).toString());

        Assert.assertEquals(1, selectors.getLength());
        final Selector selector = selectors.item(0);

        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, selector.getSelectorType());

        final ConditionalSelector condSel = (ConditionalSelector) selector;
        final Condition condition = condSel.getCondition();

        Assert.assertEquals(Condition.SAC_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassConditionImpl pseudo = (PseudoClassConditionImpl) condition;
        Assert.assertEquals("not(*[type=\"file\"])", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_pseudo() throws Exception {
        final SelectorList selectors = createSelectors("p:not(:first)");
        Assert.assertEquals("p:not(*:first)", selectors.item(0).toString());

        Assert.assertEquals(1, selectors.getLength());
        final Selector selector = selectors.item(0);

        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, selector.getSelectorType());

        final ConditionalSelector condSel = (ConditionalSelector) selector;
        final Condition condition = condSel.getCondition();

        Assert.assertEquals(Condition.SAC_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassConditionImpl pseudo = (PseudoClassConditionImpl) condition;
        Assert.assertEquals("not(*:first)", pseudo.getValue());
    }
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void invalid_not() throws Exception {
        checkError("input:not(.home:visited)",
                "Error in pseudo class or element. (Invalid token \":\". Was expecting one of: <S>, \")\".)");

        checkError("input:not(.home p)",
                "Error in pseudo class or element. (Invalid token \"p\". Was expecting one of: <S>, \")\".)");

        checkError("input:not()",
                "Error in pseudo class or element. (Invalid token \")\"."
                + " Was expecting one of: <S>, <IDENT>, \".\", \":\", \"*\", \"[\", <HASH>.)");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void twoPseudo() throws Exception {
        SelectorList selectors = createSelectors("input:lang(en):lang(de)");
        Assert.assertEquals("input:lang(en):lang(de)", selectors.item(0).toString());

        selectors = createSelectors("input:foo(test):foo(rest)");
        Assert.assertEquals("input:foo(test):foo(rest)", selectors.item(0).toString());

        selectors = createSelectors("input:foo(test):before");
        Assert.assertEquals("input:foo(test):before", selectors.item(0).toString());

        selectors = createSelectors("input:not(#test):not(#rest)");
        Assert.assertEquals("input:not(*#test):not(*#rest)", selectors.item(0).toString());

        selectors = createSelectors("input:not(#test):nth-child(even)");
        Assert.assertEquals("input:not(*#test):nth-child(even)", selectors.item(0).toString());

        selectors = createSelectors("input:not(#test):before");
        Assert.assertEquals("input:not(*#test):before", selectors.item(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementsErrors() throws Exception {
        // two pseudo elements
        checkError("input:before:after", "Duplicate pseudo class \":after\" or pseudo class \":after\" not at end.");
        checkError("input::before::after", "Duplicate pseudo class \":after\" or pseudo class \":after\" not at end.");

        checkError("input:before:lang(de)",
                "Duplicate pseudo class \":lang(de)\" or pseudo class \":lang(de)\" not at end.");
        checkError("input:before:foo(ab)",
                "Duplicate pseudo class \":foo(ab)\" or pseudo class \":foo(ab)\" not at end.");
        checkError("input:before:",
                "Error in pseudo class or element. (Invalid token \"<EOF>\". "
                + "Was expecting one of: <IDENT>, \":\", <FUNCTION_NOT>, <FUNCTION_LANG>, <FUNCTION>.)");

        // pseudo element not at end
        checkError("input:before:not(#test)",
                "Duplicate pseudo class \":not(*#test)\" or pseudo class \":not(*#test)\" not at end.");
        checkError("input:before[type='file']",
                "Error in attribute selector. (Invalid token \"type\". Was expecting: <S>.)");
        checkError("input:before.styleClass", "Error in class selector. (Invalid token \"\". Was expecting one of: .)");
        checkError("input:before#hash", "Error in hash. (Invalid token \"\". Was expecting one of: .)");
    }

    /**
     * The CDO (<!--) and CDC (-->) symbols may appear in certain locations of a stylesheet.
     * In other locations, they should cause parts of the stylesheet to be ignored.
     * @see <a href="http://www.hixie.ch/tests/evil/mixed/cdocdc.html">
     *          http://www.hixie.ch/tests/evil/mixed/cdocdc.html</a>
     * @see <a href="https://test.csswg.org/suites/css2.1/20101027/html4/sgml-comments-002.htm">
     *          https://test.csswg.org/suites/css2.1/20101027/html4/sgml-comments-002.htm</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void cdoCdc() throws Exception {
        final String css = "\n"
                + "    OL { list-style-type: lower-alpha; }\n"
                + "\n"
                + "<!--\n"
                + "\n"
                + "    .a { color: green; background: white none; }\n"
                + "<!--.b { color: green; background: white none; } --> <!-- --> <!--\n"
                + "    .c { color: green; background: white none; }\n"
                + "\n"
                + "<!--\n"
                + ".d { color: green; background: white none; }\n"
                + "-->\n"
                + "\n"
                + "    .e { color: green; background: white none; }\n"
                + "\n"
                + "\n"
                + "    <!--    .f { color: green; background: white none; }-->\n"
                + "-->.g { color: green; background: white none; }<!--\n"
                + "    .h { color: green; background: white none; }\n"
                + "-->-->-->-->-->-->.i { color: green; background: white none; }-->-->-->-->\n"
                + "\n"
                + "<!-- .j { color: green; background: white none; } -->\n"
                + "\n"
                + "<!--\n"
                + "     .k { color: green; background: white none; }\n"
                + "-->\n"
                + "\n"
                + "    .xa <!-- { color: yellow; background: red none; }\n"
                + "\n"
                + "    .xb { color: yellow -->; background: red none <!--; }\n"
                + "\n"
                + "    .xc { <!-- color: yellow; --> background: red none; }\n"
                + "\n"
                + "    .xd { <!-- color: yellow; background: red none -->; }\n"
                + "\n"
                + " <! -- .xe { color: yellow; background: red none; }\n"
                + "\n"
                + "--> <!--       --> <!-- -- >\n"
                + "\n"
                + "  .xf { color: yellow; background: red none; }\n";
        final CSSStyleSheet sheet = parse(css, 5, 0, 4);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(15, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("OL { list-style-type: lower-alpha }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("*.a { color: green; background: white none }", rule.getCssText());

        rule = rules.item(1);
        Assert.assertEquals("*.a { color: green; background: white none }", rule.getCssText());

        rule = rules.item(2);
        Assert.assertEquals("*.b { color: green; background: white none }", rule.getCssText());

        rule = rules.item(3);
        Assert.assertEquals("*.c { color: green; background: white none }", rule.getCssText());

        rule = rules.item(4);
        Assert.assertEquals("*.d { color: green; background: white none }", rule.getCssText());

        rule = rules.item(5);
        Assert.assertEquals("*.e { color: green; background: white none }", rule.getCssText());

        rule = rules.item(6);
        Assert.assertEquals("*.f { color: green; background: white none }", rule.getCssText());

        rule = rules.item(7);
        Assert.assertEquals("*.g { color: green; background: white none }", rule.getCssText());

        rule = rules.item(8);
        Assert.assertEquals("*.h { color: green; background: white none }", rule.getCssText());

        rule = rules.item(9);
        Assert.assertEquals("*.i { color: green; background: white none }", rule.getCssText());

        rule = rules.item(10);
        Assert.assertEquals("*.j { color: green; background: white none }", rule.getCssText());

        rule = rules.item(11);
        Assert.assertEquals("*.k { color: green; background: white none }", rule.getCssText());

        rule = rules.item(12);
//         Assert.assertEquals("*.xb { }", rule.getCssText());

        rule = rules.item(13);
        Assert.assertEquals("*.xc { }", rule.getCssText());

        rule = rules.item(14);
        Assert.assertEquals("*.xd { }", rule.getCssText());
    }

    /**
     * Comments
     * @throws Exception if any error occurs
     */
    @Test
    public void comment() throws Exception {
        final String css = "p { color: red; /* background: white; */ background: green; }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: red; background: green }", rule.getCssText());
    }

    /**
     * Empty declaration
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclaration() throws Exception {
        final String css = "p {  }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only semicolon
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationSemicolon() throws Exception {
        final String css = "p { ; }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only some semicolon
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationManySemicolon() throws Exception {
        final String css = "p { ; ; \t     ; }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only comment
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationComment() throws Exception {
        final String css = "p { /* background: white; */ }";
        final CSSStyleSheet sheet = parse(css, 0, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Comments
     * @throws Exception if any error occurs
     */
    @Test
    public void commentNotClosed() throws Exception {
        final String css = "p { color: red; /* background: white; }"
                + "h1 { color: blue; }";
        final CSSStyleSheet sheet = parse(css, 1, 0, 0);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals("p { color: red }", rule.getCssText());
    }

    /**
     * Handle the famous star hack as smart as possible.
     * @throws Exception if any error occurs
     */
    @Test
    public void starHackFirst() throws Exception {
        String css = "p { *color: red; background: white; }"
                + "h1 { color: blue; }";
        CSSStyleSheet sheet = parse(css, 1, 0, 0);

        CSSRuleList rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("p { background: white }", rule.getCssText());

        css = "p { color: red; *background: white }"
                + "h1 { color: blue; }";
        sheet = parse(css, 1, 0, 0);

        rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        rule = rules.item(0);
        Assert.assertEquals("p { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unicode() throws Exception {
        unicode("@p\\41ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\0041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\00041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\000041ge :pageStyle {}", "@page :pageStyle {}");

        // \\0000041 - fails
        unicode("@p\\0000041ge :pageStyle {}", "@p\\0000041ge :pageStyle {}");

        // terminated by whitespace
        unicode("@\\0070 age :pageStyle {}", "@page :pageStyle {}");
        unicode("@\\0070\tage :pageStyle {}", "@page :pageStyle {}");
        unicode("@\\0070\r\nage :pageStyle {}", "@page :pageStyle {}");

        // terminated by lenght
        unicode("@\\000070age :pageStyle {}", "@page :pageStyle {}");

        // backslash ignored
        unicode("@\\page :pageStyle {}", "@page :pageStyle {}");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unicodeEscaping() throws Exception {
        unicode("@media paper\\7b { }", "@media paper{ {}");
        unicode(".class\\7b { color: blue }", "*.class{ { color: blue }");
    }

    private void unicode(final String css, final String expected) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = parser();

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final CSSRule rule = rules.item(0);
        Assert.assertEquals(expected, rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeBackslash() throws Exception {
        Assert.assertEquals("abuv", sacParser().unescape("ab\\uv", false));
        Assert.assertEquals("ab\\ab", sacParser().unescape("ab\\\\ab", false));
        Assert.assertEquals("ab\\", sacParser().unescape("ab\\", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeOneHexDigit() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\9x", false));
        Assert.assertEquals("ab\u0009", sacParser().unescape("ab\\9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeTwoHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\09x", false));
        Assert.assertEquals("ab\u00e9x", sacParser().unescape("ab\\e9x", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeThreeHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\009x", false));
        Assert.assertEquals("ab\u00e9x", sacParser().unescape("ab\\0e9x", false));
        Assert.assertEquals("ab\u0ce9x", sacParser().unescape("ab\\ce9x", false));
        Assert.assertEquals("ab\u0ce9", sacParser().unescape("ab\\ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeFourHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\0009x", false));
        Assert.assertEquals("ab\u00e9x", sacParser().unescape("ab\\00e9x", false));
        Assert.assertEquals("ab\u0ce9x", sacParser().unescape("ab\\0ce9x", false));
        Assert.assertEquals("ab\u1ce9x", sacParser().unescape("ab\\1ce9x", false));
        Assert.assertEquals("ab\u1ce9", sacParser().unescape("ab\\1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeFiveHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\00009x", false));
        Assert.assertEquals("ab\u00e9x", sacParser().unescape("ab\\000e9x", false));
        Assert.assertEquals("ab\u0ce9x", sacParser().unescape("ab\\00ce9x", false));
        Assert.assertEquals("ab\u1ce9x", sacParser().unescape("ab\\01ce9x", false));
        Assert.assertEquals("ab\ufffdx", sacParser().unescape("ab\\a1ce9x", false));
        Assert.assertEquals("ab\ufffd", sacParser().unescape("ab\\a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeSixHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", sacParser().unescape("ab\\000009x", false));
        Assert.assertEquals("ab\u00e9x", sacParser().unescape("ab\\0000e9x", false));
        Assert.assertEquals("ab\u0ce9x", sacParser().unescape("ab\\000ce9x", false));
        Assert.assertEquals("ab\u1ce9x", sacParser().unescape("ab\\001ce9x", false));
        Assert.assertEquals("ab\ufffdx", sacParser().unescape("ab\\0a1ce9x", false));
        Assert.assertEquals("ab\ufffdx", sacParser().unescape("ab\\3a1ce9x", false));
        Assert.assertEquals("ab\ufffd", sacParser().unescape("ab\\3a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeSevenHexDigits() throws Exception {
        Assert.assertEquals("ab\ufffd9x", sacParser().unescape("ab\\0000009x", false));
        Assert.assertEquals("ab\u000e9x", sacParser().unescape("ab\\00000e9x", false));
        Assert.assertEquals("ab\u00ce9x", sacParser().unescape("ab\\0000ce9x", false));
        Assert.assertEquals("ab\u01ce9x", sacParser().unescape("ab\\0001ce9x", false));
        Assert.assertEquals("ab\ua1ce9x", sacParser().unescape("ab\\00a1ce9x", false));
        Assert.assertEquals("ab\ufffd9x", sacParser().unescape("ab\\03a1ce9x", false));
        Assert.assertEquals("ab\ufffd9x", sacParser().unescape("ab\\73a1ce9x", false));
        Assert.assertEquals("ab\ufffd9", sacParser().unescape("ab\\73a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeAutoterminate() throws Exception {
        Assert.assertEquals("ab\uabcd", sacParser().unescape("ab\\abcd", false));

        Assert.assertEquals("ab\u00e9a", sacParser().unescape("ab\\e9 a", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9 ", false));
        Assert.assertEquals("ab\u00e9 a", sacParser().unescape("ab\\e9  a", false));
        Assert.assertEquals("ab\u00e9 a", sacParser().unescape("ab\\0000e9 a", false));

        Assert.assertEquals("ab\u00e9a", sacParser().unescape("ab\\e9\ta", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9\t", false));
        Assert.assertEquals("ab\u00e9\ta", sacParser().unescape("ab\\e9\t\ta", false));
        Assert.assertEquals("ab\u00e9\ta", sacParser().unescape("ab\\0000e9\ta", false));

        Assert.assertEquals("ab\u00e9a", sacParser().unescape("ab\\e9\ra", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9\r", false));
        Assert.assertEquals("ab\u00e9\ra", sacParser().unescape("ab\\e9\r\ra", false));
        Assert.assertEquals("ab\u00e9\ra", sacParser().unescape("ab\\0000e9\ra", false));
        Assert.assertEquals("ab\u00e9a", sacParser().unescape("ab\\e9\r\na", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9\r\n", false));
        Assert.assertEquals("ab\u00e9\ra", sacParser().unescape("ab\\e9\r\n\ra", false));
        Assert.assertEquals("ab\u00e9\r", sacParser().unescape("ab\\e9\r\n\r", false));
        Assert.assertEquals("ab\u00e9\na", sacParser().unescape("ab\\e9\r\n\na", false));
        Assert.assertEquals("ab\u00e9\n", sacParser().unescape("ab\\e9\r\n\n", false));

        Assert.assertEquals("ab\u00e9a", sacParser().unescape("ab\\e9\na", false));
        Assert.assertEquals("ab\u00e9", sacParser().unescape("ab\\e9\n", false));
        Assert.assertEquals("ab\u00e9\na", sacParser().unescape("ab\\e9\n\na", false));
        Assert.assertEquals("ab\u00e9\na", sacParser().unescape("ab\\0000e9\na", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldMicrosoft() throws Exception {
        realWorld("realworld//style.csx.css", 701,
                "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 480px);"
                + "screen and (max-width: 539px);"
                + "screen and (max-width: 667px) and (min-width: 485px);"
                + "screen and (max-width: 679px);"
                + "screen and (max-width: 680px);"
                + "screen and (max-width: 900px);"
                + "screen and (min-width: 0) and (max-width: 899px);"
                + "screen and (min-width: 1024px);"
                + "screen and (min-width: 1025px);"
                + "screen and (min-width: 1025px) and (min-height: 900px);"
                + "screen and (min-width: 33.75em);"
                + "screen and (min-width: 42.5em);"
                + "screen and (min-width: 53.5em);"
                + "screen and (min-width: 540px);"
                + "screen and (min-width: 540px) and (max-width: 679px);"
                + "screen and (min-width: 560px);"
                + "screen and (min-width: 600px);"
                + "screen and (min-width: 64.0625em);"
                + "screen and (min-width: 64.0625em) and (min-height: 768px);"
                + "screen and (min-width: 64.0625em) and (min-height: 900px);"
                + "screen and (min-width: 668px);"
                + "screen and (min-width: 668px) and (max-width: 1024px);"
                + "screen and (min-width: 680px);"
                + "screen and (min-width: 680px) and (max-width: 899px);"
                + "screen and (min-width: 70em);"
                + "screen and (min-width: 70em) and (min-height: 768px);"
                + "screen and (min-width: 70em) and (min-height: 900px);"
                + "screen and (min-width: 900px);", 145, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldOracle() throws Exception {
        realWorld("realworld//compass-homestyle.css", 735,
                "all and (max-height: 600px);"
                + "all and (max-width: 600px);"
                + "all and (max-width: 770px);"
                + "all and (min-width: 0) and (max-width: 1012px);"
                + "all and (min-width: 0) and (max-width: 1018px);"
                + "all and (min-width: 0) and (max-width: 1111px);"
                + "all and (min-width: 0) and (max-width: 1312px);"
                + "all and (min-width: 0) and (max-width: 390px);"
                + "all and (min-width: 0) and (max-width: 400px);"
                + "all and (min-width: 0) and (max-width: 410px);"
                + "all and (min-width: 0) and (max-width: 450px);"
                + "all and (min-width: 0) and (max-width: 600px);"
                + "all and (min-width: 0) and (max-width: 640px);"
                + "all and (min-width: 0) and (max-width: 680px);"
                + "all and (min-width: 0) and (max-width: 720px);"
                + "all and (min-width: 0) and (max-width: 770px);"
                + "all and (min-width: 0) and (max-width: 870px);"
                + "all and (min-width: 0) and (max-width: 974px);"
                + "all and (min-width: 601px);"
                + "all and (min-width: 771px) and (max-width: 990px);"
                + "only screen and (max-width: 974px);"
                + "only screen and (min-width: 0) and (max-width: 1024px);"
                + "only screen and (min-width: 0) and (max-width: 320px);"
                + "only screen and (min-width: 0) and (max-width: 500px);"
                + "only screen and (min-width: 0) and (max-width: 600px);"
                + "only screen and (min-width: 0) and (max-width: 770px);"
                + "only screen and (min-width: 0) and (max-width: 880px);"
                + "only screen and (min-width: 0) and (max-width: 974px);"
                + "only screen and (min-width: 1024px) and (max-width: 1360px);"
                + "only screen and (min-width: 1360px);"
                + "only screen and (min-width: 601px) and (max-width: 974px);"
                + "only screen and (min-width: 771px) and (max-width: 974px);"
                + "only screen and (min-width: 880px);"
                + "only screen and (min-width: 974px);"
                + "only screen and (min-width: 975px) and (max-width: 1040px);"
                + "\ufffdscreen,screen\t;", 51, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldIBM() throws Exception {
        realWorld("realworld//www.css", 493,
                "only screen and (min-device-width: 768px) and (max-device-width: 1024px);print;screen;", 34, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldApple() throws Exception {
        realWorld("realworld//home.built.css", 675,
                "only screen and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-device-width: 767px);"
                + "only screen and (max-height: 970px);"
                + "only screen and (max-width: 1023px);"
                + "only screen and (max-width: 1024px);"
                + "only screen and (max-width: 1024px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 28em) and (max-device-width: 735px);"
                + "only screen and (max-width: 320px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) "
                        + "and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (orientation: portrait);"
                + "only screen and (min-device-width: 768px);"
                + "only screen and (min-width: 1442px);"
                + "only screen and (min-width: 1442px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (min-width: 1442px) and (min-height: 1251px);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dpi);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dppx);"
                + "print;"
                + "screen and (min-resolution: 144dpi);"
                + "screen and (min-resolution: 144dppx);", 2, 0);
    }
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldWikipedia() throws Exception {
        realWorld("realworld//load.php.css", 90, "print;screen;screen and (min-width: 982px);", 44, 36);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldSpiegel() throws Exception {
        realWorld("realworld//style-V5-11.css", 2088, "screen and (min-width: 1030px);", 74, 0);
    }

    private void realWorld(final String resourceName, final int rules, final String media,
                final int err, final int warn) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        Assert.assertNotNull(is);

        final InputSource css = new InputSource(new InputStreamReader(is, "UTF-8"));
        final CSSStyleSheet sheet = parse(css, err, 0, warn);
        Assert.assertEquals(rules, sheet.getCssRules().getLength());

        final Set<String> mediaQ = new TreeSet<String>();
        for (int i = 0; i < sheet.getCssRules().getLength(); i++) {
            final CSSRule cssRule = sheet.getCssRules().item(i);
            if (cssRule instanceof CSSMediaRule) {
                final MediaListImpl mediaList = (MediaListImpl) ((CSSMediaRuleImpl) cssRule).getMedia();
                for (int j = 0; j < mediaList.getLength(); j++) {
                    mediaQ.add(mediaList.mediaQuery(j).toString());
                }
            }
        }
        final StringBuilder queries = new StringBuilder();
        for (String string : mediaQ) {
            queries.append(string);
            queries.append(";");
        }
        Assert.assertEquals(media, queries.toString());
    }
}
