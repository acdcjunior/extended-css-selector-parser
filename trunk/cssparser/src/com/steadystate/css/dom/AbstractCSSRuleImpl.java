/*
 * Created on 05.12.2005
 *
 */
package com.steadystate.css.dom;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * @author koch
 *
 */
public class AbstractCSSRuleImpl extends CSSOMObjectImpl
{

    protected CSSStyleSheetImpl _parentStyleSheet = null;
    protected CSSRule _parentRule = null;
    
    public AbstractCSSRuleImpl(CSSStyleSheetImpl parentStyleSheet,
        CSSRule parentRule)
    {
        super();
        this._parentStyleSheet = parentStyleSheet;
        this._parentRule = parentRule;
    }

    public CSSStyleSheet getParentStyleSheet() {
        return this._parentStyleSheet;
    }

    public CSSRule getParentRule() {
        return this._parentRule;
    }

}
