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
import org.w3c.css.sac.Locator;

/**
 * Testcases.
 */
public class LocatorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final Locator locator = new LocatorImpl("uri", 1, 2);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(locator);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();

        Assert.assertEquals(locator, o);
    }
}
