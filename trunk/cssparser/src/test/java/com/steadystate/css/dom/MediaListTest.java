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
import org.w3c.dom.stylesheets.MediaList;

/**
/**
 * Unit tests for {@link MediaListImpl}.
 *
 * @author rbri
 */
public class MediaListTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final MediaList ml = new MediaListImpl();
        Assert.assertEquals("", ml.toString());
        Assert.assertEquals("", ml.getMediaText());
        Assert.assertEquals(0, ml.getLength());
        Assert.assertNull(ml.item(0));

        ml.setMediaText("MyMediaText");
        Assert.assertEquals("MyMediaText", ml.toString());
        Assert.assertEquals("MyMediaText", ml.getMediaText());
        Assert.assertEquals(1, ml.getLength());
        Assert.assertEquals("MyMediaText", ml.item(0));
        Assert.assertNull(ml.item(1));
    }
}
