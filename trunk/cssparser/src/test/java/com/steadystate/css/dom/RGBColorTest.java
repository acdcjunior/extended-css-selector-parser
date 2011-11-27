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

import com.steadystate.css.parser.LexicalUnitImpl;

/**
/**
 * Unit tests for {@link RGBColorImpl}.
 *
 * @author rbri
 */
public class RGBColorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final RGBColorImpl rgb = new RGBColorImpl();
        Assert.assertEquals("rgb(null, null, null)", rgb.toString());
        Assert.assertNull(rgb.getRed());
        Assert.assertNull(rgb.getGreen());
        Assert.assertNull(rgb.getBlue());

        LexicalUnit lu = LexicalUnitImpl.createNumber(null, 10);
        rgb.setRed(new CSSValueImpl(lu, true));
        Assert.assertEquals("rgb(10, null, null)", rgb.toString());
        Assert.assertEquals("10", rgb.getRed().getCssText());

        lu = LexicalUnitImpl.createNumber(null, 20);
        rgb.setGreen(new CSSValueImpl(lu, true));
        Assert.assertEquals("rgb(10, 20, null)", rgb.toString());
        Assert.assertEquals("20", rgb.getGreen().getCssText());

        lu = LexicalUnitImpl.createNumber(null, 30);
        rgb.setBlue(new CSSValueImpl(lu, true));
        Assert.assertEquals("rgb(10, 20, 30)", rgb.toString());
        Assert.assertEquals("30", rgb.getBlue().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        final RGBColorImpl rgb = new RGBColorImpl(rgbLU);
        Assert.assertEquals("rgb(10, 20, 30)", rgb.toString());
        Assert.assertEquals("10", rgb.getRed().getCssText());
        Assert.assertEquals("20", rgb.getGreen().getCssText());
        Assert.assertEquals("30", rgb.getBlue().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(rgbLU, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RGBColorImpl(rgbLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("rgb parameters must be separated by ','.", e.getMessage());
        }

        rgbLU = LexicalUnitImpl.createNumber(null, 10);
        lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RGBColorImpl(rgbLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("rgb parameters must be separated by ','.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);

        try {
            new RGBColorImpl(rgbLU);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertEquals("Too many parameters for rgb function.", e.getMessage());
        }
    }
}
