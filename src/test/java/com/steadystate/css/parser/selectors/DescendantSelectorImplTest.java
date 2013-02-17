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

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.Selector;

/**
 * Testcases for {@link DescendantSelectorImpl}.
 * @author rbri
 */
public class DescendantSelectorImplTest {

    @Test
    public void ancestorSelector() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final DescendantSelectorImpl selector = new DescendantSelectorImpl(parent, descendant);
        Assert.assertEquals(parent, selector.getAncestorSelector());
    }

    @Test
    public void simpleSelector() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final DescendantSelectorImpl selector = new DescendantSelectorImpl(parent, descendant);
        Assert.assertEquals(descendant, selector.getSimpleSelector());
    }

    @Test
    public void selectorType() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final DescendantSelectorImpl selector = new DescendantSelectorImpl(parent, descendant);
        Assert.assertEquals(Selector.SAC_DESCENDANT_SELECTOR, selector.getSelectorType());
    }

    @Test
    public void elementDescendant() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final DescendantSelectorImpl selector = new DescendantSelectorImpl(parent, descendant);
        Assert.assertEquals("p a", selector.toString());
    }

    @Test
    public void pseudoElementDescendant() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("a");
        final PseudoElementSelectorImpl descendant = new PseudoElementSelectorImpl("after");
        final DescendantSelectorImpl selector = new DescendantSelectorImpl(parent, descendant);
        Assert.assertEquals("a:after", selector.toString());
    }
}
