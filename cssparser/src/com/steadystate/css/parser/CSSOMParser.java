/*
 * $Id: CSSOMParser.java,v 1.5 2005-07-15 00:35:39 davidsch Exp $
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

package com.steadystate.css.parser;

import java.io.IOException;

import java.util.Properties;
import java.util.Stack;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import org.w3c.css.sac.helpers.ParserFactory;

import com.steadystate.css.dom.CSSFontFaceRuleImpl;
import com.steadystate.css.dom.CSSImportRuleImpl;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSPageRuleImpl;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSUnknownRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSOMParser.java,v 1.5 2005-07-15 00:35:39 davidsch Exp $
 */
public class CSSOMParser {
    
    private static final String PARSER = "com.steadystate.css.parser.SACParser";
    
    private static boolean use_internal = false;

    private Parser _parser = null;
    private CSSStyleSheetImpl _parentStyleSheet = null;
    // TODO what is this _parentRule for? It is not read locally.
    //private CSSRule _parentRule = null;

    /** Creates new CSSOMParser */
    public CSSOMParser() {
        try {
            // use the direct method if we already failed once before
            if(use_internal) {
                _parser = new SACParser();
            } else {
                setProperty("org.w3c.css.sac.parser", PARSER);
                ParserFactory factory = new ParserFactory();
                _parser = factory.makeParser();
            }
        } catch (Exception e) {
            use_internal = true;
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("using the default parser instead");
            _parser = new SACParser();
        }
    }

    /**
     * Creates new CSSOMParser
     *
     * @param parser the SAC Parser
     */
    public CSSOMParser(Parser parser)
    {
        this._parser = parser;
    }

    public CSSStyleSheet parseStyleSheet(InputSource source) throws IOException {
        CSSOMHandler handler = new CSSOMHandler();
        _parser.setDocumentHandler(handler);
        _parser.parseStyleSheet(source);
        return (CSSStyleSheet) handler.getRoot();
    }
    
    public CSSStyleDeclaration parseStyleDeclaration(InputSource source)
            throws IOException {
        CSSStyleDeclarationImpl sd = new CSSStyleDeclarationImpl(null);
        parseStyleDeclaration(sd, source);
        return sd;
    }
    
    public void parseStyleDeclaration(CSSStyleDeclaration sd, InputSource source)
            throws IOException {
        Stack nodeStack = new Stack();
        nodeStack.push(sd);
        CSSOMHandler handler = new CSSOMHandler(nodeStack);
        _parser.setDocumentHandler(handler);
        _parser.parseStyleDeclaration(source);
    }
    
    public CSSValue parsePropertyValue(InputSource source) throws IOException {
        CSSOMHandler handler = new CSSOMHandler();
        _parser.setDocumentHandler(handler);
        return new CSSValueImpl(_parser.parsePropertyValue(source));
    }
    
    public CSSRule parseRule(InputSource source) throws IOException {
        CSSOMHandler handler = new CSSOMHandler();
        _parser.setDocumentHandler(handler);
        _parser.parseRule(source);
        return (CSSRule) handler.getRoot();
    }
    
    public SelectorList parseSelectors(InputSource source) throws IOException {
        HandlerBase handler = new HandlerBase();
        _parser.setDocumentHandler(handler);
        return _parser.parseSelectors(source);
    }

    public void setParentStyleSheet(CSSStyleSheetImpl parentStyleSheet) {
        _parentStyleSheet = parentStyleSheet;
    }

    // See _parentRule
    /*
    public void setParentRule(CSSRule parentRule) {
        _parentRule = parentRule;
    }
    */
    
    class CSSOMHandler implements DocumentHandler {
        
        private Stack _nodeStack;
        private Object _root = null;

        public CSSOMHandler(Stack nodeStack) {
            _nodeStack = nodeStack;
        }
        
        public CSSOMHandler() {
            _nodeStack = new Stack();
        }
        
        public Object getRoot() {
            return _root;
        }
        
        public void startDocument(InputSource source) throws CSSException {
            if (_nodeStack.empty()) {
                CSSStyleSheetImpl ss = new CSSStyleSheetImpl();
                _parentStyleSheet = ss;
                ss.setHref(source.getURI());
                ss.setMedia(source.getMedia());
                ss.setTitle(source.getTitle());
                // Create the rule list
                CSSRuleListImpl rules = new CSSRuleListImpl();
                ss.setRuleList(rules);
                _nodeStack.push(ss);
                _nodeStack.push(rules);
            } else {
                // Error
            }
        }

        public void endDocument(InputSource source) throws CSSException {

            // Pop the rule list and style sheet nodes
            _nodeStack.pop();
            _root = _nodeStack.pop();
        }

        public void comment(String text) throws CSSException {
        }

        public void ignorableAtRule(String atRule) throws CSSException {

            // Create the unknown rule and add it to the rule list
            CSSUnknownRuleImpl ir = new CSSUnknownRuleImpl(
                _parentStyleSheet,
                null,
                atRule);
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(ir);
            } else {
//                _nodeStack.push(ir);
                _root = ir;
            }
        }

        public void namespaceDeclaration(String prefix, String uri)
                throws CSSException {
        }

        public void importStyle(
                String uri,
                SACMediaList media, 
                String defaultNamespaceURI) throws CSSException {

            // Create the import rule and add it to the rule list
            CSSImportRuleImpl ir = new CSSImportRuleImpl(
                _parentStyleSheet,
                null,
                uri,
                new MediaListImpl(media));
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(ir);
            } else {
//                _nodeStack.push(ir);
                _root = ir;
            }
        }

        public void startMedia(SACMediaList media) throws CSSException {

            // Create the media rule and add it to the rule list
            CSSMediaRuleImpl mr = new CSSMediaRuleImpl(
                _parentStyleSheet,
                null,
                new MediaListImpl(media));
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(mr);
            }

            // Create the rule list
            CSSRuleListImpl rules = new CSSRuleListImpl();
            mr.setRuleList(rules);
            _nodeStack.push(mr);
            _nodeStack.push(rules);
        }

        public void endMedia(SACMediaList media) throws CSSException {

            // Pop the rule list and media rule nodes
            _nodeStack.pop();
            _root = _nodeStack.pop();
        }

        public void startPage(String name, String pseudo_page) throws CSSException {

            // Create the page rule and add it to the rule list
            CSSPageRuleImpl pr = new CSSPageRuleImpl(_parentStyleSheet, null, name, pseudo_page);
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(pr);
            }

            // Create the style declaration
            CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(pr);
            pr.setStyle(decl);
            _nodeStack.push(pr);
            _nodeStack.push(decl);
        }

        public void endPage(String name, String pseudo_page) throws CSSException {

            // Pop both the style declaration and the page rule nodes
            _nodeStack.pop();
            _root = _nodeStack.pop();
        }

        public void startFontFace() throws CSSException {

            // Create the font face rule and add it to the rule list
            CSSFontFaceRuleImpl ffr = new CSSFontFaceRuleImpl(_parentStyleSheet, null);
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(ffr);
            }

            // Create the style declaration
            CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(ffr);
            ffr.setStyle(decl);
            _nodeStack.push(ffr);
            _nodeStack.push(decl);
        }

        public void endFontFace() throws CSSException {

            // Pop both the style declaration and the font face rule nodes
            _nodeStack.pop();
            _root = _nodeStack.pop();
        }

        public void startSelector(SelectorList selectors) throws CSSException {

            // Create the style rule and add it to the rule list
            CSSStyleRuleImpl sr = new CSSStyleRuleImpl(_parentStyleSheet, null, selectors);
            if (!_nodeStack.empty()) {
                ((CSSRuleListImpl)_nodeStack.peek()).add(sr);
            }
            
            // Create the style declaration
            CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(sr);
            sr.setStyle(decl);
            _nodeStack.push(sr);
            _nodeStack.push(decl);
        }

        public void endSelector(SelectorList selectors) throws CSSException {

            // Pop both the style declaration and the style rule nodes
            _nodeStack.pop();
            _root = _nodeStack.pop();
        }

        public void property(String name, LexicalUnit value, boolean important)
                throws CSSException {
            CSSStyleDeclarationImpl decl =
                (CSSStyleDeclarationImpl) _nodeStack.peek();
            decl.addProperty(
                new Property(name, new CSSValueImpl(value), important));
        }
    }

    public static void setProperty(String key, String val) {
        Properties props = System.getProperties();
        props.put(key, val);
        System.setProperties(props);
    }
}
