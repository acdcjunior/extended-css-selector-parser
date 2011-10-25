/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2011 David Schweinsberg.  All rights reserved.
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
 *
 */

package com.steadystate.css.dom;

import java.io.Serializable;

import org.w3c.dom.css.CSSValue;

import com.steadystate.css.util.LangUtils;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 */
public class Property extends CSSOMObjectImpl implements Serializable {

    private static final long serialVersionUID = 8720637891949104989L;
    private String name_;
    private CSSValue value_;
    private boolean important_;

    public void setName(final String name) {
        name_ = name;
    }

    /**
     * Creates new Property
     */
    public Property(final String name, final CSSValue value, final boolean important) {
        name_ = name;
        value_ = value;
        important_ = important;
    }

    public Property() {
        super();
    }

    public String getName() {
        return name_;
    }

    public CSSValue getValue() {
        return value_;
    }

    public boolean isImportant() {
        return important_;
    }

    public void setValue(final CSSValue value) {
        value_ = value;
    }

    public void setImportant(final boolean important) {
        important_ = important;
    }

    @Override
    public String toString() {
        return name_ + ": "
            + value_.toString()
            + (important_ ? " !important" : "");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Property)) {
            return false;
        }
        final Property p = (Property) obj;
        return super.equals(obj)
            && (important_ == p.important_)
            && LangUtils.equals(name_, p.name_)
            && LangUtils.equals(value_, p.value_);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = LangUtils.hashCode(hash, important_);
        hash = LangUtils.hashCode(hash, name_);
        hash = LangUtils.hashCode(hash, value_);
        return hash;
    }
}