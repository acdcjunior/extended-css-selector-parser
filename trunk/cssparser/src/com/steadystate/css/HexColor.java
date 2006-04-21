/*
 * $Id: HexColor.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
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

package com.steadystate.css;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: HexColor.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 */
public class HexColor {

    private int r = 0;
    private int g = 0;
    private int b = 0;

    public HexColor(String hex) {

        // Step past the hash at the beginning
        int i = 0;
        if (hex.charAt( 0 ) == '#') {
            i++;
        }

        int len = hex.length() - i;
        if (len == 3) {
            r = Integer.parseInt(hex.substring(i + 0, i + 1), 16);
            g = Integer.parseInt(hex.substring(i + 1, i + 2), 16);
            b = Integer.parseInt(hex.substring(i + 2, i + 3), 16);
            r = (r << 4) | r;
            g = (g << 4) | g;
            b = (b << 4) | b;
        } else if(len == 6) {
            r = Integer.parseInt(hex.substring(i + 0, i + 2), 16);
            g = Integer.parseInt(hex.substring(i + 2, i + 4), 16);
            b = Integer.parseInt(hex.substring(i + 4, i + 6), 16);
        }
    }

    int getRed() {
        return r;
    }

    int getGreen() {
        return g;
    }

    int getBlue() {
        return b;
    }
}