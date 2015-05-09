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

package com.steadystate.css.dom;

import org.junit.Assert;
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
        final LexicalUnit lu = LexicalUnitImpl.createAttr(null, "attrValue");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("attr(attrValue)", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_ATTR, value.getPrimitiveType());
        Assert.assertEquals(0.0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        Assert.assertEquals("attrValue", value.getStringValue());
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

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void kiloHertz() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createKiloHertz(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2kHz", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_KHZ, value.getPrimitiveType());
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
    public void millimeter() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createMillimeter(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2mm", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_MM, value.getPrimitiveType());
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
    public void millisecond() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createMillisecond(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2ms", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_MS, value.getPrimitiveType());
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
    public void numberFloat() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createNumber(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_NUMBER, value.getPrimitiveType());
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
    public void numberInt() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createNumber(null, 12);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("12", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_NUMBER, value.getPrimitiveType());
        Assert.assertEquals(12, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
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
    public void pica() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPica(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2pc", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_PC, value.getPrimitiveType());
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
    public void percentage() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2%", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_PERCENTAGE, value.getPrimitiveType());
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
    public void point() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPoint(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2pt", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_PT, value.getPrimitiveType());
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
    public void pixel() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPixel(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2px", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_PX, value.getPrimitiveType());
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
    public void radian() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createRadian(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2rad", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_RAD, value.getPrimitiveType());
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
    public void rect() throws Exception {
        final LexicalUnit rect = LexicalUnitImpl.createNumber(null, 1);
        LexicalUnit param = LexicalUnitImpl.createComma(rect);
        param = LexicalUnitImpl.createNumber(param, 2);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 3);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 4);

        final LexicalUnit lu = LexicalUnitImpl.createRect(null, rect);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("rect(1, 2, 3, 4)", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_RECT, value.getPrimitiveType());
        try {
            value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
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
    public void rgbColor() throws Exception {
        final LexicalUnit color = LexicalUnitImpl.createNumber(null, 255);
        LexicalUnit param = LexicalUnitImpl.createComma(color);
        param = LexicalUnitImpl.createNumber(param, 128);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 0);

        final LexicalUnit lu = LexicalUnitImpl.createRgbColor(null, color);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("rgb(255, 128, 0)", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_RGBCOLOR, value.getPrimitiveType());
        try {
            value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
            Assert.fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
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
    public void second() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createSecond(null, 1.2f);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("1.2s", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_S, value.getPrimitiveType());
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
    public void string() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createString(null, "value");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("\"value\"", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_STRING, value.getPrimitiveType());
        Assert.assertEquals(0.0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001); // TODO is this correct?
        Assert.assertEquals("value", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void uri() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createURI(null, "cssparser");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("url(cssparser)", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_URI, value.getPrimitiveType());
        Assert.assertEquals(0.0, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
        Assert.assertEquals("cssparser", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createURI(null, "cssparser");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        value.setCssText("1.2s");
        Assert.assertEquals("1.2s", value.getCssText());
        Assert.assertEquals(CSSPrimitiveValue.CSS_S, value.getPrimitiveType());
        Assert.assertEquals(1.2, value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), 0.00001);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createString(null, "value");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        Assert.assertEquals("\"value\"", value.getCssText());
        Assert.assertEquals("\"value\"", value.getCssText(null));
        Assert.assertEquals("\"value\"", value.getCssText(new CSSFormat()));
    }
}
