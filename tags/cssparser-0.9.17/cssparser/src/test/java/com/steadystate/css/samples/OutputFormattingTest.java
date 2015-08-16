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
package com.steadystate.css.samples;

import java.io.StringReader;

import org.junit.Test;
import org.w3c.css.sac.InputSource;

import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * Test cases for the code we have on our 'Get Started' page.
 *
 * @author rbri
 */
public class OutputFormattingTest {

    @Test
    public void outputFormatting() throws Exception {
        InputSource source = new InputSource(new StringReader("h1{background:rgb(7,42,0)}"));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheetImpl sheet = (CSSStyleSheetImpl) parser.parseStyleSheet(source, null, null);

        CSSFormat format = new CSSFormat();
        format.setRgbAsHex(true);

        System.out.println(sheet.getCssText(format));
    }
}
