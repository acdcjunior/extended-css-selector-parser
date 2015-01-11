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
package com.steadystate.css.parser.media;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;

/**
 * Testcases for {@link MediaQuery}.
 */
public class MediaQueryTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void testToString() throws Exception {
        MediaQuery mq = new MediaQuery("test");
        Assert.assertEquals("test", mq.toString());

        mq = new MediaQuery("test", false, false);
        Assert.assertEquals("test", mq.toString());

        mq = new MediaQuery("test", true, false);
        Assert.assertEquals("only test", mq.toString());

        mq = new MediaQuery("test", false, true);
        Assert.assertEquals("not test", mq.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void properties() throws Exception {
        Property prop = new Property("prop", new CSSValueImpl(), false);
        MediaQuery mq = new MediaQuery("test");
        mq.addMediaProperty(prop);
        Assert.assertEquals("test and (prop: )", mq.toString());

        final CSSValue value = new CSSValueImpl();
        value.setCssText("10dpi");
        prop = new Property("prop", value, false);
        mq = new MediaQuery("test", true, false);
        mq.addMediaProperty(prop);
        Assert.assertEquals("only test and (prop: 10dpi)", mq.toString());

        Assert.assertEquals(1, mq.getProperties().size());

        prop = new Property("min-foo", value, false);
        mq.addMediaProperty(prop);
        Assert.assertEquals("only test and (prop: 10dpi) and (min-foo: 10dpi)", mq.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void media() throws Exception {
        final MediaQuery mq = new MediaQuery("test");
        Assert.assertEquals("test", mq.getMedia());

        mq.setMedia("foo");
        Assert.assertEquals("foo", mq.getMedia());
    }
}
