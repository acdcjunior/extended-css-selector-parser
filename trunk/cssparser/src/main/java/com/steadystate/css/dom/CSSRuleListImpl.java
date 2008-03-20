/*
 * $Id: CSSRuleListImpl.java,v 1.1 2008-03-20 01:20:16 sdanig Exp $
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

import java.util.Vector;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSRuleListImpl.java,v 1.1 2008-03-20 01:20:16 sdanig Exp $
 */
public class CSSRuleListImpl implements CSSRuleList, Serializable {
    
    private static final long serialVersionUID = -1269068897476453290L;

    private Vector rules = null;

    public Vector getRules()
    {
        if (this.rules == null)
        {
            this.rules = new Vector();
        }
        return this.rules;
    }

    public void setRules(Vector rules)
    {
        this.rules = rules;
    }


    public CSSRuleListImpl() {
    }

    public int getLength() {
        return this.getRules().size();
    }

    public CSSRule item(int index) {
        return (CSSRule) this.getRules().elementAt(index);
    }

    public void add(CSSRule rule) {
        this.getRules().addElement(rule);
    }
    
    public void insert(CSSRule rule, int index) {
        this.getRules().insertElementAt(rule, index);
    }
    
    public void delete(int index) {
        this.getRules().removeElementAt(index);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.getLength(); i++ ) {
            sb.append(this.item(i).toString()).append("\r\n");
        }
        return sb.toString();
    }
}
