/*
 * $Id: CSSRuleListImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
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
 * @version $Id: CSSRuleListImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 */
public class CSSRuleListImpl implements CSSRuleList, Serializable {
    
    private Vector _rules = null;

    public CSSRuleListImpl() {
    }

    public int getLength() {
        return (_rules != null) ? _rules.size() : 0;
    }

    public CSSRule item(int index) {
        return (_rules != null) ? (CSSRule) _rules.elementAt(index) : null;
    }

    public void add(CSSRule rule) {
        if (_rules == null) {
            _rules = new Vector();
        }
        _rules.addElement(rule);
    }
    
    public void insert(CSSRule rule, int index) {
        if (_rules == null) {
            _rules = new Vector();
        }
        _rules.insertElementAt(rule, index);
    }
    
    public void delete(int index) {
        if (_rules == null) {
            _rules = new Vector();
        }
        _rules.removeElementAt(index);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < getLength(); i++ ) {
            sb.append(item(i).toString()).append("\r\n");
        }
        return sb.toString();
    }
}