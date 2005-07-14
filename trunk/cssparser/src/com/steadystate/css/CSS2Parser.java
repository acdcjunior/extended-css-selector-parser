/*
 * $Id: CSS2Parser.java,v 1.3 2005-07-14 00:25:05 davidsch Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2005 David Schweinsberg.  All rights reserved.
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
 */

package com.steadystate.css;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import org.w3c.dom.Node;

import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSUnknownRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSS2Parser.java,v 1.3 2005-07-14 00:25:05 davidsch Exp $
 * @deprecated As of 0.9.0, replaced by
 * {@link com.steadystate.css.parser.CSSOMParser}
 */
public class CSS2Parser {

    private CSSOMParser _parser = null;
    private InputSource _is = null;
    
    public CSS2Parser(
            Reader stream,
            Node ownerNode,
            String href,
            String title,
            String media) {
        _parser = new CSSOMParser();
        _is = new InputSource(stream);
    }

    public CSS2Parser(
            InputStream stream,
            Node ownerNode,
            String href,
            String title,
            String media) {
        this(new InputStreamReader(stream), ownerNode, href, title, media);
    }

    public CSS2Parser(Reader stream) {
        this(stream, null, null, null, null);
    }

    public CSS2Parser(InputStream stream) {
        this(stream, null, null, null, null);
    }

    public CSSStyleSheet styleSheet() {
        try {
            return _parser.parseStyleSheet(_is);
        } catch (IOException e) {
            return null;
        }
    }

    public CSSRuleList styleSheetRuleList() {
        return null;
    }

    public CSSCharsetRule charsetRule() {
        return null;
    }

    public CSSUnknownRule unknownRule() {
        return null;
    }

    public CSSImportRule importRule() {
        return null;
    }

    public CSSMediaRule mediaRule() {
        return null;
    }

    public CSSPageRule pageRule() {
        return null;
    }

    public CSSFontFaceRule fontFaceRule() {
        return null;
    }

    public CSSStyleRule styleRule() {
        return null;
    }
    
    public CSSStyleDeclaration styleDeclaration() {
        try {
            return _parser.parseStyleDeclaration(_is);
        } catch (IOException e) {
            return null;
        }
    }

    public CSSValue expr() {
        try {
            return _parser.parsePropertyValue(_is);
        } catch (IOException e) {
            return null;
        }
    }
}