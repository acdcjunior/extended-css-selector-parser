/*
 * $Id: RectImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
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

import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

import org.w3c.css.sac.LexicalUnit;

/** 
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: RectImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 */
public class RectImpl implements Rect, Serializable {
    
    private CSSPrimitiveValue _left;
    private CSSPrimitiveValue _top;
    private CSSPrimitiveValue _right;
    private CSSPrimitiveValue _bottom;

    /** Creates new RectImpl */
    public RectImpl(LexicalUnit lu) {
        LexicalUnit next = lu;
        _left = new CSSValueImpl(next, true);
        next = next.getNextLexicalUnit();
        next = next.getNextLexicalUnit();
        _top = new CSSValueImpl(next, true);
        next = next.getNextLexicalUnit();
        next = next.getNextLexicalUnit();
        _right = new CSSValueImpl(next, true);
        next = next.getNextLexicalUnit();
        next = next.getNextLexicalUnit();
        _bottom = new CSSValueImpl(next, true);
    }
  
    public CSSPrimitiveValue getTop() {
        return _top;
    }

    public CSSPrimitiveValue getRight() {
        return _right;
    }

    public CSSPrimitiveValue getBottom() {
        return _bottom;
    }

    public CSSPrimitiveValue getLeft() {
        return _left;
    }
    
    public String toString() {
        return new StringBuffer()
            .append("rect(")
            .append(_left.toString())
            .append(", ")
            .append(_top.toString())
            .append(", ")
            .append(_right.toString())
            .append(", ")
            .append(_bottom.toString())
            .append(")")
            .toString();
    }
}