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

package com.steadystate.css.dom;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.LexicalUnit;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.LexicalUnitImpl;

/**
/**
 * Unit tests for {@link CounterImpl}.
 *
 * @author rbri
 */
public class CounterImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CounterImpl counter = new CounterImpl();
        Assert.assertEquals("counter(null)", counter.toString());
        Assert.assertNull(counter.getIdentifier());
        Assert.assertNull(counter.getSeparator());
        Assert.assertNull(counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");

        CounterImpl counter = new CounterImpl(false, counterLu);
        Assert.assertEquals("counter(ident)", counter.toString());
        Assert.assertEquals("ident", counter.getIdentifier());
        Assert.assertNull(counter.getSeparator());
        Assert.assertNull(counter.getListStyle());

        counter = new CounterImpl(true, counterLu);
        Assert.assertEquals("counter(ident)", counter.toString());
        Assert.assertEquals("ident", counter.getIdentifier());
        Assert.assertNull(counter.getSeparator());
        Assert.assertNull(counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUSeparator() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");

        CounterImpl counter = new CounterImpl(false, counterLu);
        Assert.assertEquals("counter(ident, sep)", counter.toString());
        Assert.assertEquals("ident", counter.getIdentifier());
        Assert.assertNull(counter.getSeparator());
        Assert.assertEquals("sep", counter.getListStyle());

        counter = new CounterImpl(true, counterLu);
        Assert.assertEquals("counters(ident, \"sep\")", counter.toString());
        Assert.assertEquals("ident", counter.getIdentifier());
        Assert.assertEquals("sep", counter.getSeparator());
        Assert.assertNull(counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUSeparatorList() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createString(lu, "list");

        final CounterImpl counter = new CounterImpl(true, counterLu);
        Assert.assertEquals("counters(ident, \"sep\", list)", counter.toString());
        Assert.assertEquals("ident", counter.getIdentifier());
        Assert.assertEquals("sep", counter.getSeparator());
        Assert.assertEquals("list", counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createString(lu, "list");

        final CounterImpl counter = new CounterImpl(true, counterLu);

        Assert.assertEquals("counters(ident, \"sep\", list)", counter.getCssText());
        Assert.assertEquals("counters(ident, \"sep\", list)", counter.getCssText(null));
        Assert.assertEquals("counters(ident, \"sep\", list)", counter.getCssText(new CSSFormat()));
    }
}
