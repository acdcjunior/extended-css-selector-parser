/*
 * $Id: CSSRuleListImpl.java,v 1.4 2008-08-14 08:17:54 waldbaer Exp $
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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

import com.steadystate.css.util.LangUtils;

/**
 * Implementation of {@link CSSRuleList}.
 * 
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSRuleListImpl.java,v 1.4 2008-08-14 08:17:54 waldbaer Exp $
 */
public class CSSRuleListImpl implements CSSRuleList, Serializable {
    
    private static final long serialVersionUID = -1269068897476453290L;

    private List<CSSRule> rules = null;

    public List<CSSRule> getRules()
    {
        if (this.rules == null)
        {
            this.rules = new ArrayList<CSSRule>();
        }
        return this.rules;
    }

    public void setRules(List<CSSRule> rules)
    {
        this.rules = rules;
    }


    public CSSRuleListImpl() {
    }

    public int getLength() {
        return this.getRules().size();
    }

    public CSSRule item(int index) {
        return this.getRules().get(index);
    }

    public void add(CSSRule rule) {
        this.getRules().add(rule);
    }
    
    public void insert(CSSRule rule, int index) {
        this.getRules().add(index, rule);
    }
    
    public void delete(int index) {
        this.getRules().remove(index);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getLength(); i++ ) {
            sb.append(this.item(i).toString()).append("\r\n");
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof CSSRuleList))
        {
            return false;
        }
        CSSRuleList crl = (CSSRuleList) obj;
        return this.equalsRules(crl);
    }

    private boolean equalsRules(CSSRuleList crl)
    {
        if ((crl == null) || (this.getLength() != crl.getLength()))
        {
            return false;
        }
        for (int i = 0; i < this.getLength(); i++)
        {
            CSSRule cssRule1 = this.item(i);
            CSSRule cssRule2 = crl.item(i);
            if (!LangUtils.equals(cssRule1, cssRule2))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.rules);
        return hash;
    }

}
