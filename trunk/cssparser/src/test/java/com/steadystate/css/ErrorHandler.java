/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2008 David Schweinsberg.  All rights reserved.
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
package com.steadystate.css;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

public class ErrorHandler implements org.w3c.css.sac.ErrorHandler {

    private int errorCount;
    private int fatalErrorCount;
    private int warningCount;

    public void error(CSSParseException e) throws CSSException {
        System.out.println(e.toString());
        errorCount++;
    }

    public void fatalError(CSSParseException e) throws CSSException	{
        System.out.println(e.toString());
        fatalErrorCount++;
    }

    public void warning(CSSParseException e) throws CSSException {
        System.out.println(e.toString());
        warningCount++;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getFatalErrorCount() {
        return fatalErrorCount;
    }

    public int getWarningCount() {
        return warningCount;
    }
}
