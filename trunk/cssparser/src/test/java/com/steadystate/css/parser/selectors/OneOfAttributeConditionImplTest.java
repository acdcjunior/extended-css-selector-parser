/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2014 David Schweinsberg.  All rights reserved.
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

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.Condition;

/**
 * Testcases for {@link OneOfAttributeConditionImpl}.
 */
public class OneOfAttributeConditionImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", null, false);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
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
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "", false);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());
        Assert.assertFalse(ac.getSpecified());
        Assert.assertEquals("[test~=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "value", false);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());
        Assert.assertFalse(ac.getSpecified());
        Assert.assertEquals("[test~=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValueAndSpecified() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", null, true);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
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
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "", true);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());
        Assert.assertTrue(ac.getSpecified());
        Assert.assertEquals("[test~=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecified() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "value", true);
        Assert.assertNull(ac.getNamespaceURI());
        Assert.assertEquals(Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());
        Assert.assertTrue(ac.getSpecified());
        Assert.assertEquals("[test~=\"value\"]", ac.toString());
    }
}
