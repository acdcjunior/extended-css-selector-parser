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
package com.steadystate.css.parser.selectors;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testcases for OneOfAttributeConditionImpl.
 */
public class BeginHyphenAttributeConditionImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", null, false);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertNull(ac.getValue());
        Assert.assertFalse(ac.getSpecified());
        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", "", false);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());
        Assert.assertFalse(ac.getSpecified());
        Assert.assertEquals("[test|=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", "value", false);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());
        Assert.assertFalse(ac.getSpecified());
        Assert.assertEquals("[test|=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValueAndSpecified() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", null, true);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertNull(ac.getValue());
        Assert.assertTrue(ac.getSpecified());
        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValueAndSpecified() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", "", true);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());
        Assert.assertTrue(ac.getSpecified());
        Assert.assertEquals("[test|=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecified() throws Exception {
        final BeginHyphenAttributeConditionImpl ac = new BeginHyphenAttributeConditionImpl("test", "value", true);
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());
        Assert.assertTrue(ac.getSpecified());
        Assert.assertEquals("[test|=\"value\"]", ac.toString());
    }
}
