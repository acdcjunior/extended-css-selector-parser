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
 * Testcases for {@link ChildSelectorImpl}.
 * @author rbri
 */
public class ChildSelectorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutParentSimple() throws Exception {
        final ChildSelectorImpl s = new ChildSelectorImpl(null, null);
        Assert.assertEquals(Selector.SAC_CHILD_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getAncestorSelector());
        Assert.assertNull(s.getSimpleSelector());

        Assert.assertEquals(" > ", s.toString());

        Assert.assertEquals(" > ", s.getCssText(null));
        Assert.assertEquals(" > ", s.getCssText(new CSSFormat()));
        Assert.assertEquals(" > ", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parentOnly() throws Exception {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ChildSelectorImpl s = new ChildSelectorImpl(parent, null);
        Assert.assertEquals(Selector.SAC_CHILD_SELECTOR, s.getSelectorType());
        Assert.assertEquals(parent, s.getAncestorSelector());
        Assert.assertNull(s.getSimpleSelector());

        Assert.assertEquals("p > ", s.toString());

        Assert.assertEquals("p > ", s.getCssText(null));
        Assert.assertEquals("p > ", s.getCssText(new CSSFormat()));
        Assert.assertEquals("p > ", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleOnly() throws Exception {
        final ElementSelectorImpl simple = new ElementSelectorImpl("c");
        final ChildSelectorImpl s = new ChildSelectorImpl(null, simple);
        Assert.assertEquals(Selector.SAC_CHILD_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getAncestorSelector());
        Assert.assertEquals(simple, s.getSimpleSelector());

        Assert.assertEquals(" > c", s.toString());

        Assert.assertEquals(" > c", s.getCssText(null));
        Assert.assertEquals(" > c", s.getCssText(new CSSFormat()));
        Assert.assertEquals(" > c", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl simple = new ElementSelectorImpl("c");
        final ChildSelectorImpl s = new ChildSelectorImpl(parent, simple);
        Assert.assertEquals(Selector.SAC_CHILD_SELECTOR, s.getSelectorType());
        Assert.assertEquals(parent, s.getAncestorSelector());
        Assert.assertEquals(simple, s.getSimpleSelector());

        Assert.assertEquals("p > c", s.toString());

        Assert.assertEquals("p > c", s.getCssText(null));
        Assert.assertEquals("p > c", s.getCssText(new CSSFormat()));
        Assert.assertEquals("p > c", s.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }
}
