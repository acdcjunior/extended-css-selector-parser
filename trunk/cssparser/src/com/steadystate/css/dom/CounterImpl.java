/*
 * $Id: CounterImpl.java,v 1.3 2005-08-23 14:47:30 waldbaer Exp $
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

import org.w3c.dom.css.Counter;

import org.w3c.css.sac.LexicalUnit;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CounterImpl.java,v 1.3 2005-08-23 14:47:30 waldbaer Exp $
 */
public class CounterImpl implements Counter, Serializable {

    private String _identifier;
    private String _listStyle;
    private String _separator;
    
    /** Creates new CounterImpl */
    public CounterImpl(boolean separatorSpecified, LexicalUnit lu) {
        LexicalUnit next = lu;
        this._identifier = next.getStringValue();
        next = next.getNextLexicalUnit();   // ','
        if (next != null)
        {
            next = next.getNextLexicalUnit();
            if (separatorSpecified && (next != null)) {
                this._separator = next.getStringValue();
                next = next.getNextLexicalUnit();   // ','
                if (next != null)
                {
                    next = next.getNextLexicalUnit();
                }
            }
            if (next != null) {
                this._listStyle = next.getStringValue();
            }
        }
    }

    public String getIdentifier() {
        return _identifier;
    }

    public String getListStyle() {
        return _listStyle;
    }

    public String getSeparator() {
        return _separator;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (_separator == null) {
            // This is a 'counter()' function
            sb.append("counter(");
        } else {
            // This is a 'counters()' function
            sb.append("counters(");
        }
        sb.append(_identifier);
        if (_separator != null) {
            sb.append(", \"").append(_separator).append("\"");
        }
        if (_listStyle != null) {
            sb.append(", ").append(_listStyle);
        }
        sb.append(")");
        return sb.toString();
    }
}