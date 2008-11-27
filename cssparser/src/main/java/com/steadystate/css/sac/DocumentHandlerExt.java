/*
 * Created on 04.12.2007
 *
 */
package com.steadystate.css.sac;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

public interface DocumentHandlerExt extends DocumentHandler
{

    /**
     * Receive notification of a charset at-rule.
     * 
     * @param characterEncoding the character encoding
     * @param locator the SAC locator
     * @throws CSSException Any CSS exception, possibly wrapping another
     *  exception.
     */
    public void charset(String characterEncoding, Locator locator)
        throws CSSException;

    /**
     * Receive notification of a import statement in the style sheet.
     *
     * @param uri The URI of the imported style sheet.
     * @param media The intended destination media for style information.
     * @param defaultNamepaceURI The default namespace URI for the imported
     *  style sheet.
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.
     */
    public void importStyle(String uri, SACMediaList media, 
        String defaultNamespaceURI, Locator locator)
	    throws CSSException;

    /**
     * Receive notification of an unknown rule t-rule not supported by this
     * parser.
     *
     * @param at-rule The complete ignored at-rule.
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.  
     */
    public void ignorableAtRule(String atRule, Locator locator)
        throws CSSException;

    /**
     * Receive notification of the beginning of a font face statement.
     *
     * The Parser will invoke this method at the beginning of every font face
     * statement in the style sheet. there will be a corresponding endFontFace()
     * event for every startFontFace() event.
     *
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.
     */
    public void startFontFace(Locator locator) throws CSSException;

    /**
     * Receive notification of the beginning of a page statement.
     *
     * The Parser will invoke this method at the beginning of every page
     * statement in the style sheet. there will be a corresponding endPage()
     * event for every startPage() event.
     *
     * @param name the name of the page (if any, null otherwise)
     * @param pseudo_page the pseudo page (if any, null otherwise)
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.  
     */    
    public void startPage(String name, String pseudo_page, Locator locator)
        throws CSSException;

    /**
     * Receive notification of the beginning of a media statement.
     *
     * The Parser will invoke this method at the beginning of every media
     * statement in the style sheet. there will be a corresponding endMedia()
     * event for every startElement() event.
     *
     * @param media The intended destination media for style information.
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.  
     */
    public void startMedia(SACMediaList media, Locator locator)
        throws CSSException;

    /**
     * Receive notification of the beginning of a rule statement.
     *
     * @param selectors All intended selectors for all declarations.
     * @param locator the SAC locator
     * @exception CSSException Any CSS exception, possibly wrapping another
     *  exception.
     */
    public void startSelector(SelectorList selectors, Locator locator)
        throws CSSException;

    /**
     * Receive notification of a declaration.
     * 
     * @param name the name of the property.
     * @param value the value of the property. All whitespace are stripped.
     * @param important is this property important ?
     * @param locator the SAC locator
     */
    public void property(String name, LexicalUnit value,
        boolean important, Locator locator);

}
