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

package com.steadystate.css.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.ErrorHandler;

/**
 * @author rbri
 */
public abstract class AbstractSACParserTest {

    private Locale systemLocale_;

    /**
     * Set up
     */
    @Before
    public void setUp() {
        systemLocale_ = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Tear down
     */
    @After
    public void tearDown() {
        Locale.setDefault(systemLocale_);
    }

    protected CSSStyleSheet parse(final String css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    protected CSSStyleSheet parse(final InputStream css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    protected CSSStyleSheet parse(final String css, final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheet parse(final InputStream css, final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new InputStreamReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheet parse(final InputSource source, final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(err, errorHandler.getErrorCount());
        Assert.assertEquals(fatal, errorHandler.getFatalErrorCount());
        Assert.assertEquals(warn, errorHandler.getWarningCount());

        return sheet;
    }

    protected abstract CSSOMParser parser();
}