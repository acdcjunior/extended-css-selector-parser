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
package com.steadystate.css;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

/**
 * @author RBRi
 */
public class ErrorHandler implements org.w3c.css.sac.ErrorHandler {

    private int errorCount_;
    private int fatalErrorCount_;
    private int warningCount_;

    public void error(final CSSParseException e) throws CSSException {
        System.out.println(e.toString());
        errorCount_++;
    }

    public void fatalError(final CSSParseException e) throws CSSException {
        System.out.println(e.toString());
        fatalErrorCount_++;
    }

    public void warning(final CSSParseException e) throws CSSException {
        System.out.println(e.toString());
        warningCount_++;
    }

    public int getErrorCount() {
        return errorCount_;
    }

    public int getFatalErrorCount() {
        return fatalErrorCount_;
    }

    public int getWarningCount() {
        return warningCount_;
    }
}
