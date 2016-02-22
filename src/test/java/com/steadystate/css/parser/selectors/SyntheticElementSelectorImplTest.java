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
 * Testcases for {@link SyntheticElementSelectorImpl}.
 */
public class SyntheticElementSelectorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ctor() throws Exception {
        final SyntheticElementSelectorImpl s = new SyntheticElementSelectorImpl();
        Assert.assertNull(s.getNamespaceURI());
        Assert.assertNull(s.getLocalName());
        Assert.assertEquals(Selector.SAC_ELEMENT_NODE_SELECTOR, s.getSelectorType());

        Assert.assertEquals("", s.toString());

        Assert.assertEquals("", s.getCssText(null));
        Assert.assertEquals("", s.getCssText(new CSSFormat()));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final SyntheticElementSelectorImpl s = new SyntheticElementSelectorImpl();
        try {
            s.setLocalName("test");
            Assert.fail("RuntimeException expected");
        }
        catch (final RuntimeException e) {
            Assert.assertEquals("Method setLocalName is not supported for SyntheticElementSelectorImpl.",
                                    e.getMessage());
        }
    }
}
