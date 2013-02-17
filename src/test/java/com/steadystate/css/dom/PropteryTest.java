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

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.LexicalUnit;

import com.steadystate.css.parser.LexicalUnitImpl;

/**
/**
 * Unit tests for {@link Property}.
 *
 * @author rbri
 */
public class PropteryTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final Property prop = new Property();
        Assert.assertEquals("null:", prop.toString());
        Assert.assertNull(prop.getName());
        Assert.assertNull(prop.getValue());
        Assert.assertFalse(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());

        prop.setName("MyName");
        Assert.assertEquals("MyName:", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertNull(prop.getValue());
        Assert.assertFalse(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());

        LexicalUnit lu = LexicalUnitImpl.createString(null, "MyValue");
        prop.setValue(new CSSValueImpl(lu, true));
        Assert.assertEquals("MyName: \"MyValue\"", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("\"MyValue\"", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());

        lu = LexicalUnitImpl.createPixel(null, 11f);
        prop.setValue(new CSSValueImpl(lu, true));
        Assert.assertEquals("MyName: 11px", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("11px", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());

        prop.setImportant(true);
        Assert.assertEquals("MyName: 11px !important", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("11px", prop.getValue().toString());
        Assert.assertTrue(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructWithParams() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCentimeter(null, 13.2f);
        final Property prop = new Property("MyName", new CSSValueImpl(lu, true), false);
        Assert.assertEquals("MyName: 13.2cm", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("13.2cm", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());
        Assert.assertTrue(prop.getUserDataMap().isEmpty());
    }

    public void equals() throws Exception {
        LexicalUnit lu = LexicalUnitImpl.createCentimeter(null, 13.2f);
        final Property prop = new Property("MyName", new CSSValueImpl(lu, true), false);

        lu = LexicalUnitImpl.createCentimeter(null, 13.2f);
        Property prop2 = new Property("MyName", new CSSValueImpl(lu, true), false);

        Assert.assertEquals(prop, prop2);

        // different name
        lu = LexicalUnitImpl.createCentimeter(null, 13.2f);
        prop2 = new Property("MyName2", new CSSValueImpl(lu, true), false);
        Assert.assertFalse(prop.equals(prop2));

        // different value
        lu = LexicalUnitImpl.createMillimeter(null, 13.2f);
        prop2 = new Property("MyName", new CSSValueImpl(lu, true), false);
        Assert.assertFalse(prop.equals(prop2));

        // different importance
        lu = LexicalUnitImpl.createCentimeter(null, 13.2f);
        prop2 = new Property("MyName", new CSSValueImpl(lu, true), true);
        Assert.assertFalse(prop.equals(prop2));
    }
}
