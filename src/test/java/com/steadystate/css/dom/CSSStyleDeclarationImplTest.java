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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Unit tests for {@link CSSStyleDeclarationImpl}.
 *
 * @author Daniel Gredler
 * @author waldbaer
 * @author rbri
 * @author Ahmed Ashour
 */
public class CSSStyleDeclarationImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CSSStyleDeclaration style = new CSSStyleDeclarationImpl();

        Assert.assertEquals("", style.getCssText());
        Assert.assertEquals(0, style.getLength());

        Assert.assertNull(style.getParentRule());
        Assert.assertEquals("", style.getPropertyValue("unknown"));
        Assert.assertEquals("", style.getPropertyPriority("unknown"));
        Assert.assertNull(style.getPropertyCSSValue("unknown"));

        Assert.assertEquals("", style.getPropertyValue(null));
        Assert.assertEquals("", style.getPropertyPriority(null));
        Assert.assertNull(style.getPropertyCSSValue(null));

        // remove
        Assert.assertEquals("", style.removeProperty("unknown"));
        Assert.assertEquals(0, style.getLength());

        Assert.assertEquals("", style.removeProperty(null));
        Assert.assertEquals(0, style.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyRule() throws Exception {
        final CSSStyleDeclaration style = parseStyleDeclaration("");

        Assert.assertEquals("", style.getCssText());
        Assert.assertEquals(0, style.getLength());

        Assert.assertNull(style.getParentRule());
        Assert.assertEquals("", style.getPropertyValue("unknown"));
        Assert.assertEquals("", style.getPropertyPriority("unknown"));
        Assert.assertNull(style.getPropertyCSSValue("unknown"));

        Assert.assertEquals("", style.getPropertyValue(null));
        Assert.assertEquals("", style.getPropertyPriority(null));
        Assert.assertNull(style.getPropertyCSSValue(null));

        // remove
        Assert.assertEquals("", style.removeProperty("unknown"));
        Assert.assertEquals(0, style.getLength());

        Assert.assertEquals("", style.removeProperty(null));
        Assert.assertEquals(0, style.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleRule() throws Exception {
        final CSSStyleDeclaration style = parseStyleDeclaration("prop: value");

        Assert.assertEquals("prop: value", style.getCssText());
        Assert.assertEquals(1, style.getLength());

        Assert.assertNull(style.getParentRule());
        Assert.assertEquals("value", style.getPropertyValue("prop"));
        Assert.assertEquals("", style.getPropertyPriority("prop"));
        Assert.assertEquals("value", style.getPropertyCSSValue("prop").getCssText());

        Assert.assertEquals("", style.getPropertyValue("unknown"));
        Assert.assertEquals("", style.getPropertyPriority("unknown"));
        Assert.assertNull(style.getPropertyCSSValue("unknown"));

        Assert.assertEquals("", style.getPropertyValue(null));
        Assert.assertEquals("", style.getPropertyPriority(null));
        Assert.assertNull(style.getPropertyCSSValue(null));

        // remove
        Assert.assertEquals("", style.removeProperty("unknown"));
        Assert.assertEquals(1, style.getLength());

        Assert.assertEquals("", style.removeProperty(null));
        Assert.assertEquals(1, style.getLength());

        Assert.assertEquals("value", style.removeProperty("prop"));
        Assert.assertEquals(0, style.getLength());
        Assert.assertEquals("", style.getPropertyValue("prop"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void twoRules() throws Exception {
        final CSSStyleDeclaration style = parseStyleDeclaration("prop: value; color: red !important;");

        Assert.assertEquals("prop: value; color: red !important", style.getCssText());
        Assert.assertEquals(2, style.getLength());

        Assert.assertNull(style.getParentRule());
        Assert.assertEquals("value", style.getPropertyValue("prop"));
        Assert.assertEquals("", style.getPropertyPriority("prop"));
        Assert.assertEquals("value", style.getPropertyCSSValue("prop").getCssText());

        Assert.assertEquals("red", style.getPropertyValue("color"));
        Assert.assertEquals("important", style.getPropertyPriority("color"));
        Assert.assertEquals("red", style.getPropertyCSSValue("color").getCssText());

        Assert.assertEquals("", style.getPropertyValue("unknown"));
        Assert.assertEquals("", style.getPropertyPriority("unknown"));
        Assert.assertNull(style.getPropertyCSSValue("unknown"));

        Assert.assertEquals("", style.getPropertyValue(null));
        Assert.assertEquals("", style.getPropertyPriority(null));
        Assert.assertNull(style.getPropertyCSSValue(null));

        // remove
        Assert.assertEquals("", style.removeProperty("unknown"));
        Assert.assertEquals(2, style.getLength());

        Assert.assertEquals("", style.removeProperty(null));
        Assert.assertEquals(2, style.getLength());

        Assert.assertEquals("value", style.removeProperty("prop"));
        Assert.assertEquals(1, style.getLength());
        Assert.assertEquals("", style.getPropertyValue("prop"));

        Assert.assertEquals("red", style.removeProperty("color"));
        Assert.assertEquals(0, style.getLength());
        Assert.assertEquals("", style.getPropertyValue("color"));
    }

    /**
     * Regression test for bug 1874800.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void cssTextHasNoCurlyBraces() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        Assert.assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);
        final CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);

        Assert.assertFalse(style.getCssText().contains("{"));
        Assert.assertFalse(style.getCssText().contains("}"));

        style.setCssText("color: red;");
        Assert.assertEquals("color: red", style.getCssText());
    }

    /**
     * Regression test for bug 1691221.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyUrl() throws Exception {
        final CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parseStyleDeclaration("background: url()");

        Assert.assertEquals("background: url()", style.getCssText());
        Assert.assertEquals(1, style.getLength());
        Assert.assertEquals("background", style.item(0));
        Assert.assertEquals("url()", style.getPropertyValue("background"));
    }

    /**
     * Test serialization.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void serialize() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        Assert.assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);
        final CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(style);
        oos.flush();
        oos.close();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Object o = ois.readObject();
        ois.close();
        Assert.assertEquals(style, o);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setProperty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        style.setProperty("display", "value", "false");
        Assert.assertEquals("value", style.getPropertyValue("display"));
        Assert.assertEquals("", style.getPropertyPriority("display"));

        style.setProperty("display", "value2", "imPOrTant");
        Assert.assertEquals("value2", style.getPropertyValue("display"));
        Assert.assertEquals("important", style.getPropertyPriority("display"));

        style.setProperty("display", "value2", "TrUE");
        Assert.assertEquals("value2", style.getPropertyValue("display"));
        Assert.assertEquals("", style.getPropertyPriority("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyNull() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        style.setProperty("display", "value", null);
        Assert.assertEquals("value", style.getPropertyValue("display"));
        Assert.assertEquals("", style.getPropertyPriority("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValue() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", "newValue", "false");
        Assert.assertEquals("newValue", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValueToEmpty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", "", "false");
        Assert.assertEquals("", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValueToBlank() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", " \t \r \n", "false");
        Assert.assertEquals("", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removeProperty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        Assert.assertEquals("none", style.removeProperty("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removePropertyEmpty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("");
        style.addProperty(new Property("display", value, false));
        Assert.assertEquals("", style.removeProperty("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removePropertyBlank() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl();
        final CSSValue value = parsePropertyValue("  \t  ");
        style.addProperty(new Property("display", value, false));
        Assert.assertEquals("", style.removeProperty("display"));
    }

    private CSSStyleDeclaration parseStyleDeclaration(final String value) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource is = new InputSource(new StringReader(value));
        return parser.parseStyleDeclaration(is);
    }

    private CSSValue parsePropertyValue(final String value) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource is = new InputSource(new StringReader(value));
        return parser.parsePropertyValue(is);
    }
}
