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

package com.steadystate.css.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rbri
 */
public class ParserUtilsTest {

    @Test
    public void trimBy() {
        Assert.assertEquals("test", ParserUtils.trimBy(new StringBuilder("test"), 0, 0));

        Assert.assertEquals("est", ParserUtils.trimBy(new StringBuilder("test"), 1, 0));
        Assert.assertEquals("st", ParserUtils.trimBy(new StringBuilder("test"), 2, 0));

        Assert.assertEquals("tes", ParserUtils.trimBy(new StringBuilder("test"), 0, 1));
        Assert.assertEquals("te", ParserUtils.trimBy(new StringBuilder("test"), 0, 2));

        Assert.assertEquals("e", ParserUtils.trimBy(new StringBuilder("test"), 1, 2));
    }

    @Test
    public void trimUrl() {
        Assert.assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(test)")));
        Assert.assertEquals("", ParserUtils.trimUrl(new StringBuilder("url()")));

        Assert.assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url('test')")));
        Assert.assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(\"test\")")));

        Assert.assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(   test \t )")));
    }
}