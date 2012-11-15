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

package com.steadystate.css.dom;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.steadystate.css.parser.LexicalUnitImpl;

/**
/**
 * Unit tests for {@link CSSValueImpl}.
 *
 * @author rbri
 */
public class CSSValueImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attr() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createAttr(null, "attr");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("attr()", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_ATTR, value.getPrimitiveType());
        Assert.assertEquals(0.0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        Assert.assertEquals("", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void cm() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCentimeter(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2cm", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_CM, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void counter() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCounter(null, null);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("counter()", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_COUNTER, value.getPrimitiveType());
        Assert.assertEquals(0.0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deg() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createDegree(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2deg", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_DEG, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimension() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createDimension(null, 1.2f, "lumen");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2lumen", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_DIMENSION, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void em() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createEm(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2em", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_EMS, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emx() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createEx(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2ex", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_EXS, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradian() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createGradian(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2grad", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_GRAD, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void hertz() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createHertz(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2Hz", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_HZ, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ident() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createIdent(null, "id");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("id", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_IDENT, value.getPrimitiveType());
        Assert.assertEquals(0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        Assert.assertEquals("id", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void inch() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createInch(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2in", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_IN, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        try {
            value.getStringValue();
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }
}
