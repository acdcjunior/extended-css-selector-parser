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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS21;

/**
/**
 * Unit tests for {@link CSSStyleSheetImpl}.
 *
 * @author exxws
 */
public class CSSStyleSheetImplTest {

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void insertRuleWithLeadingWhitespaceTest() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader(""));
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        final String expected = "*.testStyleDef { height: 42px }";

        ss.insertRule(" .testStyleDef { height: 42px; }", 0);
        Assert.assertEquals(expected, ss.getCssRules().item(0).getCssText());

        ss.insertRule("      .testStyleDef { height: 42px;}   ", 0);
        Assert.assertEquals(expected, ss.getCssRules().item(1).getCssText());

        ss.insertRule("\t.testStyleDef { height: 42px; }\r\n", 0);
        Assert.assertEquals(expected, ss.getCssRules().item(2).getCssText());
    }

    @Test
    public void serializeTest() {
        final String cssText =
            "h1 {\n"
            + "  font-size: 2em\n"
            + "}\n"
            + "\n"
            + "@media handheld {\n"
            + "  h1 {\n"
            + "    font-size: 1.5em\n"
            + "  }\n"
            + "}";
        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSOMParser cssomParser = new CSSOMParser();
        try {
            final CSSStyleSheet css = cssomParser.parseStyleSheet(source, null,
                "http://www.example.org/css/style.css");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(css);
            oos.flush();
            oos.close();
            final byte[] bytes = baos.toByteArray();
            final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            final Object o = ois.readObject();
            Assert.assertEquals(css, o);
        }
        catch (final IOException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
        catch (final ClassNotFoundException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }
}
