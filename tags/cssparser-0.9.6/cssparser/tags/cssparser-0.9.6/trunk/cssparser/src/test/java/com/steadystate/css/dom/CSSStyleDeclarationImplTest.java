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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Unit tests for {@link CSSStyleDeclarationImpl}.
 *
 * @author Daniel Gredler
 * @author waldbaer
 * @author rbri
 */
public class CSSStyleDeclarationImplTest {

    /**
     * Regression test for bug 1874800.
     *
     * @throws Exception
     *             if any error occurs
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
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void emptyUrl() throws Exception {
        final CSSOMParser parser = new CSSOMParser();

        final Reader r = new StringReader("background: url()");
        final InputSource source = new InputSource(r);
        final CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);

        Assert.assertEquals("background: url()", style.getCssText());
        Assert.assertEquals(1, style.getLength());
        Assert.assertEquals("background", style.item(0));
        Assert.assertEquals("url()", style.getPropertyValue("background"));
    }

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
}
