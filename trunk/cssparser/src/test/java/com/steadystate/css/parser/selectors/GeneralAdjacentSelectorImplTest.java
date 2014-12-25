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
import org.w3c.css.sac.Selector;

/**
 * Test cases for {@link GeneralAdjacentSelectorImpl}.
 * @author Ahmed Ashour
 */
public class GeneralAdjacentSelectorImplTest {

    @Test
    public void simpleSelector() {
        final ElementSelectorImpl parent = new ElementSelectorImpl("p");
        final ElementSelectorImpl descendant = new ElementSelectorImpl("a");
        final GeneralAdjacentSelectorImpl selector =
                new GeneralAdjacentSelectorImpl(Selector.SAC_ANY_NODE_SELECTOR, parent, descendant);
        Assert.assertEquals(parent, selector.getSelector());
        Assert.assertEquals(descendant, selector.getSiblingSelector());
        Assert.assertEquals("p ~ a", selector.toString());
    }

}
