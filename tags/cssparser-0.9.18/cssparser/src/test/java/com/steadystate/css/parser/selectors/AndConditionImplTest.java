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
package com.steadystate.css.parser.selectors;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.Condition;

import com.steadystate.css.format.CSSFormat;

/**
 * Testcases for {@link AndConditionImpl}.
 */
public class AndConditionImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutFirstSecond() throws Exception {
        final AndConditionImpl ac = new AndConditionImpl(null, null);
        Assert.assertEquals(Condition.SAC_AND_CONDITION, ac.getConditionType());
        Assert.assertNull(ac.getFirstCondition());
        Assert.assertNull(ac.getSecondCondition());

        Assert.assertEquals("", ac.toString());

        Assert.assertEquals("", ac.getCssText(null));
        Assert.assertEquals("", ac.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void firstOnly() throws Exception {
        final AttributeConditionImpl first = new AttributeConditionImpl("test", null, false);
        final AndConditionImpl ac = new AndConditionImpl(first, null);
        Assert.assertEquals(Condition.SAC_AND_CONDITION, ac.getConditionType());
        Assert.assertEquals(first, ac.getFirstCondition());
        Assert.assertNull(ac.getSecondCondition());

        Assert.assertEquals("[test]", ac.toString());

        Assert.assertEquals("[test]", ac.getCssText(null));
        Assert.assertEquals("[test]", ac.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void secondOnly() throws Exception {
        final AttributeConditionImpl second = new AttributeConditionImpl("test", null, false);
        final AndConditionImpl ac = new AndConditionImpl(null, second);
        Assert.assertEquals(Condition.SAC_AND_CONDITION, ac.getConditionType());
        Assert.assertNull(ac.getFirstCondition());
        Assert.assertEquals(second, ac.getSecondCondition());

        Assert.assertEquals("[test]", ac.toString());

        Assert.assertEquals("[test]", ac.getCssText(null));
        Assert.assertEquals("[test]", ac.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final IdConditionImpl first = new IdConditionImpl("value");
        final AttributeConditionImpl second = new AttributeConditionImpl("test", null, false);
        final AndConditionImpl ac = new AndConditionImpl(first, second);
        Assert.assertEquals(Condition.SAC_AND_CONDITION, ac.getConditionType());
        Assert.assertEquals(first, ac.getFirstCondition());
        Assert.assertEquals(second, ac.getSecondCondition());

        Assert.assertEquals("#value[test]", ac.toString());

        Assert.assertEquals("#value[test]", ac.getCssText(null));
        Assert.assertEquals("#value[test]", ac.getCssText(new CSSFormat()));
    }
}
