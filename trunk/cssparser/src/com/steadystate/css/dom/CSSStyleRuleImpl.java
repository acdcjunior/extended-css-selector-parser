/*
 * $Id: CSSStyleRuleImpl.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
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

package com.steadystate.css.dom;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.w3c.dom.DOMException;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.SelectorList;

import com.steadystate.css.parser.CSSOMParser;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSStyleRuleImpl.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
 */
public class CSSStyleRuleImpl extends AbstractCSSRuleImpl implements CSSStyleRule, Serializable {

    private SelectorList _selectors = null;
    private CSSStyleDeclaration _style = null;

    public CSSStyleRuleImpl(CSSStyleSheetImpl parentStyleSheet,
        CSSRule parentRule, SelectorList selectors) {
        super(parentStyleSheet, parentRule);
        this._selectors = selectors;
    }

    public short getType() {
        return STYLE_RULE;
    }

    public String getCssText() {
        return this.getSelectorText() + " " + this.getStyle().toString();
    }

    public void setCssText(String cssText) throws DOMException {
        if (this._parentStyleSheet != null && this._parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }

        try {
            InputSource is = new InputSource(new StringReader(cssText));
            CSSOMParser parser = new CSSOMParser();
            CSSRule r = parser.parseRule(is);

            // The rule must be a style rule
            if (r.getType() == CSSRule.STYLE_RULE) {
                this._selectors = ((CSSStyleRuleImpl)r)._selectors;
                this._style = ((CSSStyleRuleImpl)r)._style;
            } else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_STYLE_RULE);
            }
        } catch (CSSException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        } catch (IOException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
    }

    public String getSelectorText() {
        return this._selectors.toString();
    }

    public void setSelectorText(String selectorText) throws DOMException {
        if (this._parentStyleSheet != null && this._parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET );
        }

        try {
            InputSource is = new InputSource(new StringReader(selectorText));
            CSSOMParser parser = new CSSOMParser();
            this._selectors = parser.parseSelectors(is);
        } catch (CSSException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        } catch (IOException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
    }

    public CSSStyleDeclaration getStyle() {
        return this._style;
    }

    public void setStyle(CSSStyleDeclarationImpl style) {
        this._style = style;
    }
    
    public String toString() {
        return this.getCssText();
    }
}
