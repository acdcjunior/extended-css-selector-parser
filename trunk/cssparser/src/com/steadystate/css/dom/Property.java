/*
 * $Id: Property.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
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

import org.w3c.dom.css.CSSValue;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: Property.java,v 1.3 2006-04-11 08:15:19 waldbaer Exp $
 */
public class Property extends CSSOMObjectImpl implements Serializable {

    private String _name;
    private CSSValue _value;
    private boolean _important;

    /** Creates new Property */
    public Property(String name, CSSValue value, boolean important) {
        this._name = name;
        this._value = value;
        this._important = important;
    }

    public String getName() {
        return this._name;
    }

    public CSSValue getValue() {
        return this._value;
    }

    public boolean isImportant() {
        return this._important;
    }

    public void setValue(CSSValue value) {
        this._value = value;
    }
    
    public void setImportant(boolean important) {
        this._important = important;
    }
    
    public String toString() {
        return this._name + ": "
            + this._value.toString()
            + (this._important ? " !important" : "");
    }
}