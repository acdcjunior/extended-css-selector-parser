/*
 * $Id: CSSPageRuleImpl.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
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

import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 * TO DO: Implement setSelectorText()
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSPageRuleImpl.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
 */
public class CSSPageRuleImpl extends AbstractCSSRuleImpl implements CSSPageRule, Serializable {

    private String _ident = null;
    private String _pseudoPage = null;
    private CSSStyleDeclaration _style = null;

    public CSSPageRuleImpl(
            CSSStyleSheetImpl parentStyleSheet,
            CSSRule parentRule,
            String ident,
            String pseudoPage) {
        super(parentStyleSheet, parentRule);
        this._ident = ident;
        this._pseudoPage = pseudoPage;
    }

    public short getType() {
        return PAGE_RULE;
    }

    public String getCssText() {
        String sel = getSelectorText();
        return "@page "
            + sel + ((sel.length() > 0) ? " " : "")
            + getStyle().getCssText();
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

            // The rule must be a page rule
            if (r.getType() == CSSRule.PAGE_RULE) {
                this._ident = ((CSSPageRuleImpl)r)._ident;
                this._pseudoPage = ((CSSPageRuleImpl)r)._pseudoPage;
                this._style = ((CSSPageRuleImpl)r)._style;
            } else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_PAGE_RULE);
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
        return ((this._ident != null) ? this._ident : "")
            + ((this._pseudoPage != null) ? ":" + this._pseudoPage : "");
    }

    public void setSelectorText(String selectorText) throws DOMException {
    }

    public CSSStyleDeclaration getStyle() {
        return this._style;
    }

    protected void setIdent(String ident) {
        this._ident = ident;
    }

    protected void setPseudoPage(String pseudoPage) {
        this._pseudoPage = pseudoPage;
    }

    public void setStyle(CSSStyleDeclarationImpl style) {
        this._style = style;
    }
}
