/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2014 David Schweinsberg.  All rights reserved.
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

package com.steadystate.css.dom;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.Rect;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.LexicalUnitImpl;
import com.steadystate.css.parser.SACParserCSS21;

/**
/**
 * Unit tests for {@link RectImpl}.
 *
 * @author rbri
 */
public class RectTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final RectImpl rect = new RectImpl();
        Assert.assertEquals("rect(null, null, null, null)", rect.toString());
        Assert.assertNull(rect.getTop());
        Assert.assertNull(rect.getLeft());
        Assert.assertNull(rect.getBottom());
        Assert.assertNull(rect.getRight());

        LexicalUnit lu = LexicalUnitImpl.createNumber(null, 10);
        rect.setTop(new CSSValueImpl(lu, true));
        Assert.assertEquals("rect(10, null, null, null)", rect.toString());
        Assert.assertEquals("10", rect.getTop().getCssText());

        lu = LexicalUnitImpl.createNumber(null, 20);
        rect.setRight(new CSSValueImpl(lu, true));
        Assert.assertEquals("rect(10, 20, null, null)", rect.toString());
        Assert.assertEquals("20", rect.getRight().getCssText());

        lu = LexicalUnitImpl.createNumber(null, 30);
        rect.setBottom(new CSSValueImpl(lu, true));
        Assert.assertEquals("rect(10, 20, 30, null)", rect.toString());
        Assert.assertEquals("30", rect.getBottom().getCssText());

        lu = LexicalUnitImpl.createNumber(null, 40);
        rect.setLeft(new CSSValueImpl(lu, true));
        Assert.assertEquals("rect(10, 20, 30, 40)", rect.toString());
        Assert.assertEquals("40", rect.getLeft().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        final RectImpl rect = new RectImpl(rectLU);
        Assert.assertEquals("rect(10, 20, 30, 40)", rect.toString());
        Assert.assertEquals("10", rect.getTop().getCssText());
        Assert.assertEquals("20", rect.getRight().getCssText());
        Assert.assertEquals("30", rect.getBottom().getCssText());
        Assert.assertEquals("40", rect.getLeft().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(rectLU, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RectImpl(rectLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("Rect parameters must be separated by ','.", e.getMessage());
        }

        rectLU = LexicalUnitImpl.createNumber(null, 10);
        lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RectImpl(rectLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("Rect parameters must be separated by ','.", e.getMessage());
        }

        rectLU = LexicalUnitImpl.createNumber(null, 10);
        lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        try {
            new RectImpl(rectLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("Rect parameters must be separated by ','.", e.getMessage());
        }

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 40);
        lu = LexicalUnitImpl.createComma(lu);

        try {
            new RectImpl(rectLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("Too many parameters for rect function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRule() throws Exception {
        final String testRule = "img { clip: rect(1px, 2px, -3px, 4px) }";
        final Reader r = new StringReader(testRule);
        final InputSource is = new InputSource(r);
        final CSSRule rule = new CSSOMParser(new SACParserCSS21()).parseRule(is);

        Assert.assertEquals(testRule, rule.getCssText());

        final CSSStyleDeclaration style = ((CSSStyleRuleImpl) rule).getStyle();
        final Property prop = ((CSSStyleDeclarationImpl) style).getPropertyDeclaration("clip");
        final Rect rect = ((CSSValueImpl) prop.getValue()).getRectValue();

        Assert.assertEquals("1px", rect.getTop().toString());
        Assert.assertEquals("2px", rect.getRight().toString());
        Assert.assertEquals("-3px", rect.getBottom().toString());
        Assert.assertEquals("4px", rect.getLeft().toString());
    }
}
