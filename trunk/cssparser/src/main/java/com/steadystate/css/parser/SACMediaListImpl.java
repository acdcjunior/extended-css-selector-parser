/*
 * $Id: SACMediaListImpl.java,v 1.2 2008-03-26 01:35:33 sdanig Exp $
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

package com.steadystate.css.parser;

import java.util.*;
import org.w3c.css.sac.*;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: SACMediaListImpl.java,v 1.2 2008-03-26 01:35:33 sdanig Exp $
 */
public class SACMediaListImpl implements SACMediaList {

    private Vector _selectors = new Vector(10, 10);

    public int getLength() {
        return this._selectors.size();
    }

    public String item(int index) {
        return (String) this._selectors.elementAt(index);
    }

    public void add(String s) {
        this._selectors.addElement(s);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int len = getLength();
        for (int i = 0; i < len; i++) {
            sb.append(item(i));
            if (i < len - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
