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
 * Test cases for {@link GeneralAdjacentSelectorImpl}.
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class GeneralAdjacentSelectorImplTest {

    @Test
    public void withoutParentDescendant() {
        final GeneralAdjacentSelectorImpl selector =
                new GeneralAdjacentSelectorImpl(Selector.SAC_ANY_NODE_SELECTOR, null, null);
        Assert.assertNull(selector.getSelector());
        Assert.assertNull(selector.getSiblingSelector());

        Assert.assertEquals(" ~ ", selector.toString());

        Assert.assertEquals(" ~ ", selector.getCssText(null));
        Assert.assertEquals(" ~ ", selector.getCssText(new CSSFormat()));
        Assert.assertEquals(" ~ ", selector.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    @Test
    public void withoutParent() {
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final GeneralAdjacentSelectorImpl selector =
                new GeneralAdjacentSelectorImpl(Selector.SAC_ANY_NODE_SELECTOR, null, descendant);
        Assert.assertNull(selector.getSelector());
        Assert.assertEquals(descendant, selector.getSiblingSelector());

        Assert.assertEquals(" ~ a", selector.toString());

        Assert.assertEquals(" ~ a", selector.getCssText(null));
        Assert.assertEquals(" ~ a", selector.getCssText(new CSSFormat()));
        Assert.assertEquals(" ~ a", selector.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    @Test
    public void withoutDescendant() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final GeneralAdjacentSelectorImpl selector =
                new GeneralAdjacentSelectorImpl(Selector.SAC_ANY_NODE_SELECTOR, parent, null);
        Assert.assertEquals(parent, selector.getSelector());
        Assert.assertNull(null, selector.getSiblingSelector());

        Assert.assertEquals("p ~ ", selector.toString());

        Assert.assertEquals("p ~ ", selector.getCssText(null));
        Assert.assertEquals("p ~ ", selector.getCssText(new CSSFormat()));
        Assert.assertEquals("p ~ ", selector.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }

    @Test
    public void both() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final GeneralAdjacentSelectorImpl selector =
                new GeneralAdjacentSelectorImpl(Selector.SAC_ANY_NODE_SELECTOR, parent, descendant);
        Assert.assertEquals(parent, selector.getSelector());
        Assert.assertEquals(descendant, selector.getSiblingSelector());

        Assert.assertEquals("p ~ a", selector.toString());

        Assert.assertEquals("p ~ a", selector.getCssText(null));
        Assert.assertEquals("p ~ a", selector.getCssText(new CSSFormat()));
        Assert.assertEquals("p ~ a", selector.getCssText(new CSSFormat().setSuppressUniversalSelector(true)));
    }
}
