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
import org.w3c.css.sac.Selector;

import com.steadystate.css.format.CSSFormat;

/**
 * Testcases for {@link ConditionalSelectorImpl}.
 * @author rbri
 */
public class ConditionalSelectorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutSelectorCondition() throws Exception {
        final ConditionalSelectorImpl s = new ConditionalSelectorImpl(null, null);
        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getSimpleSelector());
        Assert.assertNull(s.getCondition());

        Assert.assertEquals("", s.toString());

        Assert.assertEquals("", s.getCssText(null));
        Assert.assertEquals("", s.getCssText(new CSSFormat()));
        Assert.assertEquals("", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorOnly() throws Exception {
        final ElementSelectorImpl selector = new ElementSelectorImpl("p");
        final ConditionalSelectorImpl s = new ConditionalSelectorImpl(selector, null);
        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, s.getSelectorType());
        Assert.assertEquals(selector, s.getSimpleSelector());
        Assert.assertNull(s.getCondition());

        Assert.assertEquals("p", s.toString());

        Assert.assertEquals("p", s.getCssText(null));
        Assert.assertEquals("p", s.getCssText(new CSSFormat()));
        Assert.assertEquals("p", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void conditionOnly() throws Exception {
        final IdConditionImpl condition = new IdConditionImpl("id");
        final ConditionalSelectorImpl s = new ConditionalSelectorImpl(null, condition);
        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getSimpleSelector());
        Assert.assertEquals(condition, s.getCondition());

        Assert.assertEquals("#id", s.toString());

        Assert.assertEquals("#id", s.getCssText(null));
        Assert.assertEquals("#id", s.getCssText(new CSSFormat()));
        Assert.assertEquals("#id", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final ElementSelectorImpl selector = new ElementSelectorImpl("p");
        final IdConditionImpl condition = new IdConditionImpl("id");
        final ConditionalSelectorImpl s = new ConditionalSelectorImpl(selector, condition);
        Assert.assertEquals(Selector.SAC_CONDITIONAL_SELECTOR, s.getSelectorType());
        Assert.assertEquals(selector, s.getSimpleSelector());
        Assert.assertEquals(condition, s.getCondition());

        Assert.assertEquals("p#id", s.toString());

        Assert.assertEquals("p#id", s.getCssText(null));
        Assert.assertEquals("p#id", s.getCssText(new CSSFormat()));
        Assert.assertEquals("p#id", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }
}
