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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.LexicalUnit;

/**
 * Unit tests for {@link LexicalUnitImpl}.
 *
 * @author rbri
 */
public class LexicalUnitImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();

        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals(",", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setLexicalUnitType() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_OPERATOR_GT);

        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_GT, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals(">", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setFloatValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_KILOHERTZ);
        unit.setFloatValue(7.1234f);

        Assert.assertEquals(LexicalUnit.SAC_KILOHERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(7.1234f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(7, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("kHz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("7.1234kHz", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setDimension() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_DIMENSION);
        unit.setDimension("Watt");

        Assert.assertEquals(LexicalUnit.SAC_DIMENSION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("Watt", unit.getDimension());
        Assert.assertEquals("Watt", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("0Watt", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setFunctionName() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_FUNCTION);
        unit.setFunctionName("sqrt");

        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("sqrt", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("sqrt()", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setParameters() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_FUNCTION);
        unit.setFunctionName("sqrt");
        unit.setParameters(LexicalUnitImpl.createCentimeter(null, 14f));

        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("sqrt", unit.getFunctionName());
        Assert.assertEquals("14cm", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("sqrt(14cm)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_UNICODERANGE);
        unit.setStringValue("testValue");

        Assert.assertEquals(LexicalUnit.SAC_UNICODERANGE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("testValue", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("testValue", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValueNull() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        unit.setLexicalUnitType(LexicalUnit.SAC_UNICODERANGE);
        unit.setStringValue(null);

        Assert.assertEquals(LexicalUnit.SAC_UNICODERANGE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nextLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        Assert.assertNull(unit.getNextLexicalUnit());

        final LexicalUnitImpl next = new LexicalUnitImpl();
        unit.setNextLexicalUnit(next);
        Assert.assertEquals(next, unit.getNextLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void previousLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        Assert.assertNull(unit.getPreviousLexicalUnit());

        final LexicalUnitImpl prev = new LexicalUnitImpl();
        unit.setPreviousLexicalUnit(prev);
        Assert.assertEquals(prev, unit.getPreviousLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void refreshToStringCache() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();
        Assert.assertEquals(",", unit.toString());

        unit.setLexicalUnitType(LexicalUnit.SAC_KILOHERTZ);
        Assert.assertEquals("0kHz", unit.toString());

        unit.setFloatValue(7.1234f);
        Assert.assertEquals("7.1234kHz", unit.toString());

        unit.setLexicalUnitType(LexicalUnit.SAC_DIMENSION);
        Assert.assertEquals("7.1234", unit.toString());
        unit.setDimension("Ohm");
        Assert.assertEquals("7.1234Ohm", unit.toString());

        unit.setLexicalUnitType(LexicalUnit.SAC_FUNCTION);
        Assert.assertEquals("()", unit.toString());
        unit.setFunctionName("pow");
        Assert.assertEquals("pow()", unit.toString());

        unit.setParameters(LexicalUnitImpl.createPixel(null, 4f));
        Assert.assertEquals("pow(4px)", unit.toString());

        unit.setLexicalUnitType(LexicalUnit.SAC_SUB_EXPRESSION);
        Assert.assertEquals("", unit.toString());
        unit.setStringValue("value");
        Assert.assertEquals("value", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getDimensionUnitText() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl();

        unit.setLexicalUnitType(LexicalUnit.SAC_EM);
        Assert.assertEquals("em", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_EX);
        Assert.assertEquals("ex", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_PIXEL);
        Assert.assertEquals("px", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_INCH);
        Assert.assertEquals("in", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_CENTIMETER);
        Assert.assertEquals("cm", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_MILLIMETER);
        Assert.assertEquals("mm", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_POINT);
        Assert.assertEquals("pt", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_PICA);
        Assert.assertEquals("pc", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_PERCENTAGE);
        Assert.assertEquals("%", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_DEGREE);
        Assert.assertEquals("deg", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_GRADIAN);
        Assert.assertEquals("grad", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_RADIAN);
        Assert.assertEquals("rad", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_MILLISECOND);
        Assert.assertEquals("ms", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_SECOND);
        Assert.assertEquals("s", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_HERTZ);
        Assert.assertEquals("Hz", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_KILOHERTZ);
        Assert.assertEquals("kHz", unit.getDimensionUnitText());

        unit.setLexicalUnitType(LexicalUnit.SAC_DIMENSION);
        unit.setDimension("test");
        Assert.assertEquals("test", unit.getDimensionUnitText());

        unit.setLexicalUnitType((short) 119);
        Assert.assertEquals("", unit.getDimensionUnitText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromInt() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 17);

        Assert.assertEquals(LexicalUnit.SAC_INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(17f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(17, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("17", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromFloat() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_REAL, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPercentage() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPercentage(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_PERCENTAGE, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("%", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7%", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPixel() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPixel(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("px", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7px", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCentimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCentimeter(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_CENTIMETER, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("cm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7cm", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillimeter(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_MILLIMETER, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("mm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7mm", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createInch() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createInch(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_INCH, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("in", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7in", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPoint() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPoint(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_POINT, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("pt", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7pt", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPica() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPica(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_PICA, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("pc", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7pc", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEm() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEm(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_EM, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("em", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7em", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEx() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEx(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_EX, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("ex", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7ex", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDegree() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDegree(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_DEGREE, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("deg", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7deg", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRadian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createRadian(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_RADIAN, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("rad", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7rad", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createGradian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createGradian(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_GRADIAN, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("grad", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7grad", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillisecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillisecond(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_MILLISECOND, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("ms", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7ms", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createSecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_SECOND, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("s", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7s", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createHertz(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_HERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("Hz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7Hz", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDimension() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDimension(null, 1.7f, "Ohm");

        Assert.assertEquals(LexicalUnit.SAC_DIMENSION, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("Ohm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7Ohm", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createKiloHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createKiloHertz(null, 1.7f);

        Assert.assertEquals(LexicalUnit.SAC_KILOHERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("kHz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7kHz", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounter() throws Exception {
        final LexicalUnit value = LexicalUnitImpl.createIdent(null, "CounterValue");
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, value);

        Assert.assertEquals(LexicalUnit.SAC_COUNTER_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counter", unit.getFunctionName());
        Assert.assertEquals("CounterValue", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counter(CounterValue)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounterNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, null);

        Assert.assertEquals(LexicalUnit.SAC_COUNTER_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counter", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counter()", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounters() throws Exception {
        final LexicalUnit value1 = LexicalUnitImpl.createIdent(null, "CounterValue");
        LexicalUnitImpl.createIdent(LexicalUnitImpl.createComma(value1), "Second");
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, value1);

        Assert.assertEquals(LexicalUnit.SAC_COUNTERS_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counters", unit.getFunctionName());
        Assert.assertEquals("CounterValue", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counters(CounterValue, Second)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCountersNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, null);

        Assert.assertEquals(LexicalUnit.SAC_COUNTERS_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counters", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counters()", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createAttr() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createAttr(null, "attrValue");

        Assert.assertEquals(LexicalUnit.SAC_ATTR, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("name", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("attrValue", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("attr(attrValue)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRect() throws Exception {
        // rect() function impl assumes the commas are part of the declaration
        final LexicalUnit x1 = LexicalUnitImpl.createNumber(null, 1);
        final LexicalUnit y1 = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(x1), 2);
        final LexicalUnit x2 = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(y1), 3);
        LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(x2), 4);

        final LexicalUnit unit = LexicalUnitImpl.createRect(null, x1);

        Assert.assertEquals(LexicalUnit.SAC_RECT_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("rect", unit.getFunctionName());
        Assert.assertEquals("1", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("rect(1, 2, 3, 4)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRgbColor() throws Exception {
        final LexicalUnit r = LexicalUnitImpl.createNumber(null, 255);
        final LexicalUnit g = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(r), 128);
        LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(g), 0);
        final LexicalUnit unit = LexicalUnitImpl.createRgbColor(null, r);

        Assert.assertEquals(LexicalUnit.SAC_RGBCOLOR, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("rgb", unit.getFunctionName());
        Assert.assertEquals("255", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("rgb(255, 128, 0)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createFunction() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createFunction(null, "foo",
                LexicalUnitImpl.createString(null, "param"));

        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("foo", unit.getFunctionName());
        Assert.assertEquals("\"param\"", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("foo(\"param\")", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createFunctionTwoParams() throws Exception {
        final LexicalUnit x = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit c = LexicalUnitImpl.createComma(x);
        LexicalUnitImpl.createNumber(c, 11);

        final LexicalUnit unit = LexicalUnitImpl.createFunction(null, "foo", x);

        Assert.assertEquals(LexicalUnit.SAC_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("foo", unit.getFunctionName());
        Assert.assertEquals("10", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("foo(10, 11)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createString() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createString(null, "RBRi");

        Assert.assertEquals(LexicalUnit.SAC_STRING_VALUE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("RBRi", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("\"RBRi\"", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createIdent() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createIdent(null, "css");

        Assert.assertEquals(LexicalUnit.SAC_IDENT, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("css", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("css", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createURI() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createURI(null, "www.wetator.org");

        Assert.assertEquals(LexicalUnit.SAC_URI, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("www.wetator.org", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("url(www.wetator.org)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createComma() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createComma(null);

        Assert.assertEquals(LexicalUnit.SAC_OPERATOR_COMMA, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getFloatValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals(",", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 10f);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(unit);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();

        Assert.assertEquals(unit.toString(), o.toString());
    }
}
