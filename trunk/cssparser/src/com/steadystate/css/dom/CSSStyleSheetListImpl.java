/*
 * CSSStyleSheetListImpl.java
 *
 * Created on 22. Februar 2005, 22:04
 */

package com.steadystate.css.dom;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

/**
 *
 * @author  koch
 * @version $Release$
 */
public class CSSStyleSheetListImpl implements StyleSheetList
{
    
    private List cssStyleSheets;    // List of CSSStyleSheet
    
    private List getCSSStyleSheets()
    {
        if (this.cssStyleSheets == null)
        {
            this.cssStyleSheets = new Vector();
        }
        return this.cssStyleSheets;
    }
    

    /** Creates a new instance of CSSStyleSheetListImpl */
    public CSSStyleSheetListImpl()
    {
    }
    

    // start StyleSheetList
    public int getLength()
    {
        return this.getCSSStyleSheets().size();
    }
    
    public StyleSheet item(int index)
    {
        return (StyleSheet) this.getCSSStyleSheets().get(index);
    }
    
    /**
     * Adds a CSSStyleSheet.
     *
     * @param cssStyleSheet the CSSStyleSheet
     */
    public void add(CSSStyleSheet cssStyleSheet)
    {
        this.getCSSStyleSheets().add(cssStyleSheet);
    }
    // end StyleSheetList
    
    /**
     * Merges all StyleSheets in this list into one.
     *
     * @return the new (merged) StyleSheet
     */
    public StyleSheet merge()
    {
        CSSStyleSheetImpl merged = new CSSStyleSheetImpl();
        CSSRuleListImpl cssRuleList = new CSSRuleListImpl();
        Iterator it = this.getCSSStyleSheets().iterator();
        while (it.hasNext())
        {
            CSSStyleSheetImpl cssStyleSheet = (CSSStyleSheetImpl) it.next();
            CSSMediaRuleImpl cssMediaRule =
                new CSSMediaRuleImpl(merged, null, cssStyleSheet.getMedia());
            cssMediaRule.setRuleList(
                (CSSRuleListImpl) cssStyleSheet.getCssRules());
            cssRuleList.add(cssMediaRule);
        }
        merged.setRuleList(cssRuleList);
        merged.setMedia("all");
        return merged;
    }
    
}
