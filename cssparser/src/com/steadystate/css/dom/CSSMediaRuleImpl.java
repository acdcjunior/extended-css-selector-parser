/*
 * $Id: CSSMediaRuleImpl.java,v 1.4 2006-04-11 08:15:19 waldbaer Exp $
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

import org.w3c.dom.stylesheets.MediaList;

import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSMediaRuleImpl.java,v 1.4 2006-04-11 08:15:19 waldbaer Exp $
 */
public class CSSMediaRuleImpl extends AbstractCSSRuleImpl implements CSSMediaRule, Serializable {

    private MediaList _media = null;
    private CSSRuleList _rules = null;

    public CSSMediaRuleImpl(
            CSSStyleSheetImpl parentStyleSheet,
            CSSRule parentRule,
            MediaList media) {
        super(parentStyleSheet, parentRule);
        this._media = media;
    }

    public short getType() {
        return MEDIA_RULE;
    }

    public String getCssText() {
        StringBuffer sb = new StringBuffer("@media ");
        sb.append(getMedia().toString()).append(" {");
        for (int i = 0; i < getCssRules().getLength(); i++) {
            CSSRule rule = getCssRules().item(i);
            sb.append(rule.getCssText()).append(" ");
        }
        sb.append("}");
        return sb.toString();
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

            // The rule must be a media rule
            if (r.getType() == CSSRule.MEDIA_RULE) {
                this._media = ((CSSMediaRuleImpl)r)._media;
                this._rules = ((CSSMediaRuleImpl)r)._rules;
            } else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_MEDIA_RULE);
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

    public MediaList getMedia() {
        return this._media;
    }

    public CSSRuleList getCssRules() {
        return this._rules;
    }

    public int insertRule(String rule, int index) throws DOMException {
        if (this._parentStyleSheet != null && this._parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }

        try {
            InputSource is = new InputSource(new StringReader(rule));
            CSSOMParser parser = new CSSOMParser();
            parser.setParentStyleSheet(this._parentStyleSheet);
            // parser._parentRule is never read
            //parser.setParentRule(_parentRule);
            CSSRule r = parser.parseRule(is);

            // Insert the rule into the list of rules
            ((CSSRuleListImpl)getCssRules()).insert(r, index);

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DOMExceptionImpl(
                DOMException.INDEX_SIZE_ERR,
                DOMExceptionImpl.ARRAY_OUT_OF_BOUNDS,
                e.getMessage());
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
        return index;
    }

    public void deleteRule(int index) throws DOMException {
        if (this._parentStyleSheet != null && this._parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }
        try {
            ((CSSRuleListImpl)getCssRules()).delete(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DOMExceptionImpl(
                DOMException.INDEX_SIZE_ERR,
                DOMExceptionImpl.ARRAY_OUT_OF_BOUNDS,
                e.getMessage());
        }
    }

    public void setRuleList(CSSRuleListImpl rules) {
        this._rules = rules;
    }
    
    public String toString() {
        return this.getCssText();
    }
}
