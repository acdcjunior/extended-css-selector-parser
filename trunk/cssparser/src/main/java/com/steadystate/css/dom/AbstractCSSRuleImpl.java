/*
 * Created on 05.12.2005
 *
 */
package com.steadystate.css.dom;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * @author koch
 */
public abstract class AbstractCSSRuleImpl extends CSSOMObjectImpl
{

    protected CSSStyleSheetImpl parentStyleSheet = null;
    protected CSSRule parentRule = null;

    protected CSSStyleSheetImpl getParentStyleSheetImpl()
    {
        return this.parentStyleSheet;
    }

    public void setParentStyleSheet(CSSStyleSheetImpl parentStyleSheet)
    {
        this.parentStyleSheet = parentStyleSheet;
    }

    public void setParentRule(CSSRule parentRule)
    {
        this.parentRule = parentRule;
    }


    public AbstractCSSRuleImpl(CSSStyleSheetImpl parentStyleSheet,
        CSSRule parentRule)
    {
        super();
        this.parentStyleSheet = parentStyleSheet;
        this.parentRule = parentRule;
    }

    public AbstractCSSRuleImpl()
    {
    }


    public CSSStyleSheet getParentStyleSheet() {
        return this.parentStyleSheet;
    }

    public CSSRule getParentRule() {
        return this.parentRule;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof CSSRule))
        {
            return false;
        }
        return super.equals(obj);
        // don't use parentRule and parentStyleSheet in equals()
        // recursive loop -> stack overflow!
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        // don't use parentRule and parentStyleSheet in hashCode()
        // recursive loop -> stack overflow!
        return hash;
    }

}
