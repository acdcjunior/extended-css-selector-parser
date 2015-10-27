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
 * Testcases for {@link LangConditionImpl}.
 */
public class LangConditionImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final LangConditionImpl c = new LangConditionImpl(null);
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, c.getConditionType());
        Assert.assertNull(c.getLang());

        Assert.assertEquals(":lang()", c.toString());

        Assert.assertEquals(":lang()", c.getCssText(null));
        Assert.assertEquals(":lang()", c.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final LangConditionImpl c = new LangConditionImpl("");
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, c.getConditionType());
        Assert.assertEquals("", c.getLang());

        Assert.assertEquals(":lang()", c.toString());

        Assert.assertEquals(":lang()", c.getCssText(null));
        Assert.assertEquals(":lang()", c.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final LangConditionImpl c = new LangConditionImpl("value");
        Assert.assertEquals(Condition.SAC_LANG_CONDITION, c.getConditionType());
        Assert.assertEquals("value", c.getLang());

        Assert.assertEquals(":lang(value)", c.toString());

        Assert.assertEquals(":lang(value)", c.getCssText(null));
        Assert.assertEquals(":lang(value)", c.getCssText(new CSSFormat()));
    }
}
