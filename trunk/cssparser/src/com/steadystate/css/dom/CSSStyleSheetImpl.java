/*
 * $Id: CSSStyleSheetImpl.java,v 1.4 2006-04-11 08:15:19 waldbaer Exp $
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
import org.w3c.dom.Node;

import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.stylesheets.StyleSheet;

import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.SACMediaList;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS2;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSStyleSheetImpl.java,v 1.4 2006-04-11 08:15:19 waldbaer Exp $
 */
public class CSSStyleSheetImpl implements CSSStyleSheet, Serializable {

    private boolean _disabled = false;
    private Node _ownerNode = null;
    private StyleSheet _parentStyleSheet = null;
    private String _href = null;
    private String _title = null;
    private MediaList _media = null;
    private CSSRule _ownerRule = null;
    private boolean _readOnly = false;
    private CSSRuleListImpl _rules = null;

    public CSSStyleSheetImpl() {
    }

    public String getType() {
        return "text/css";
    }

    public boolean getDisabled() {
        return this._disabled;
    }

    /**
     * We will need to respond more fully if a stylesheet is disabled, probably
     * by generating an event for the main application.
     */
    public void setDisabled(boolean disabled) {
        this._disabled = disabled;
    }

    public Node getOwnerNode() {
        return this._ownerNode;
    }

    public StyleSheet getParentStyleSheet() {
        return this._parentStyleSheet;
    }

    public String getHref() {
        return this._href;
    }

    public String getTitle() {
        return this._title;
    }

    public MediaList getMedia() {
        return this._media;
    }

    public CSSRule getOwnerRule() {
        return this._ownerRule;
    }

    public CSSRuleList getCssRules() {
        if (this._rules == null)
        {
            this._rules = new CSSRuleListImpl();
        }
        return this._rules;
    }

    public int insertRule(String rule, int index) throws DOMException {
        if (this._readOnly) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }

        try {
            InputSource is = new InputSource(new StringReader(rule));
            CSSOMParser parser = new CSSOMParser();
            parser.setParentStyleSheet(this);
            CSSRule r = parser.parseRule(is);

            if (getCssRules().getLength() > 0) {

                // We need to check that this type of rule can legally go into
                // the requested position.
                int msg = -1;
                if (r.getType() == CSSRule.CHARSET_RULE) {

                    // Index must be 0, and there can be only one charset rule
                    if (index != 0) {
                        msg = DOMExceptionImpl.CHARSET_NOT_FIRST;
                    } else if (getCssRules().item(0).getType()
                            == CSSRule.CHARSET_RULE) {
                        msg = DOMExceptionImpl.CHARSET_NOT_UNIQUE;
                    }
                } else if (r.getType() == CSSRule.IMPORT_RULE) {

                    // Import rules must preceed all other rules (except
                    // charset rules)
                    if (index <= getCssRules().getLength()) {
                        for (int i = 0; i < index; i++) {
                            int rt = getCssRules().item(i).getType();
                            if ((rt != CSSRule.CHARSET_RULE)
                                    || (rt != CSSRule.IMPORT_RULE)) {
                                msg = DOMExceptionImpl.IMPORT_NOT_FIRST;
                                break;
                            }
                        }
                    }
                }

                if (msg > -1) {
                    throw new DOMExceptionImpl(
                        DOMException.HIERARCHY_REQUEST_ERR,
                        msg);
                }
            }

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
        if (this._readOnly) {
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

    public boolean isReadOnly() {
        return this._readOnly;
    }

    public void setReadOnly(boolean b) {
        this._readOnly = b;
    }

    public void setOwnerNode(Node ownerNode) {
        this._ownerNode = ownerNode;
    }

    public void setParentStyleSheet(StyleSheet parentStyleSheet) {
        this._parentStyleSheet = parentStyleSheet;
    }

    public void setHref(String href) {
        this._href = href;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public void setMedia(String mediaText) {
        InputSource source = new InputSource(new StringReader(mediaText));
        try
        {
            // TODO get SAC Parser version from System property?
            SACMediaList sml = new SACParserCSS2().parseMedia(source);
            this._media = new MediaListImpl(sml);
        }
        catch (IOException e)
        {
            // TODO handle exception
        }
    }

    public void setOwnerRule(CSSRule ownerRule) {
        this._ownerRule = ownerRule;
    }
    
    public void setRuleList(CSSRuleListImpl rules) {
        this._rules = rules;
    }
    
    public String toString() {
        return this.getCssRules().toString();
    }
    
    /**
     * Imports referenced CSSStyleSheets.
     *
     * @param recursive <code>true</code> if the import should be done
     *   recursively, <code>false</code> otherwise
     */
    public void importImports(boolean recursive)
        throws DOMException
    {
        for (int i = 0; i < this.getCssRules().getLength(); i++)
        {
            CSSRule cssRule = this.getCssRules().item(i);
            if (cssRule.getType() == CSSRule.IMPORT_RULE)
            {
                CSSImportRule cssImportRule = (CSSImportRule) cssRule;
                try
                {
                    java.net.URI importURI = new java.net.URI(this.getHref())
                        .resolve(cssImportRule.getHref());
                    CSSStyleSheetImpl importedCSS = (CSSStyleSheetImpl)
                        new CSSOMParser().parseStyleSheet(new InputSource(
                            importURI.toString()));
                    if (recursive)
                    {
                        importedCSS.importImports(recursive);
                    }
                    MediaList mediaList = cssImportRule.getMedia();
                    if (mediaList.getLength() == 0)
                    {
                        mediaList.appendMedium("all");
                    }
                    CSSMediaRuleImpl cssMediaRule =
                        new CSSMediaRuleImpl(this, null, mediaList);
                    cssMediaRule.setRuleList(
                        (CSSRuleListImpl) importedCSS.getCssRules());
                    this.deleteRule(i);
                    ((CSSRuleListImpl) this.getCssRules()).insert(cssMediaRule, i);
                }
                catch (java.net.URISyntaxException e)
                {
                    // TODO handle exception
                    throw new DOMException(DOMException.SYNTAX_ERR,
                        e.getLocalizedMessage());
                }
                catch (IOException e)
                {
                    // TODO handle exception
                }
            }
        }
    }
}
