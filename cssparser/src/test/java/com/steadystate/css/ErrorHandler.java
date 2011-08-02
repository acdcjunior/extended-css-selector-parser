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
    private StringBuilder errorMsg_ = new StringBuilder();

    private int fatalErrorCount_;
    private StringBuilder fatalErrorMsg_ = new StringBuilder();

    private int warningCount_;
    private StringBuilder warningMsg_ = new StringBuilder();

    public void error(final CSSParseException e) throws CSSException {
        errorCount_++;
        errorMsg_.append(e.getMessage()).append(" ");
    }

    public void fatalError(final CSSParseException e) throws CSSException {
        fatalErrorCount_++;
        fatalErrorMsg_.append(e.getMessage()).append(" ");
    }

    public void warning(final CSSParseException e) throws CSSException {
        warningCount_++;
        warningMsg_.append(e.getMessage()).append(" ");
    }

    public int getErrorCount() {
        return errorCount_;
    }

    public String getErrorMessage() {
        return errorMsg_.toString().trim();
    }

    public int getFatalErrorCount() {
        return fatalErrorCount_;
    }

    public String getFatalErrorMessage() {
        return fatalErrorMsg_.toString().trim();
    }

    public int getWarningCount() {
        return warningCount_;
    }

    public String getWarningMessage() {
        return warningMsg_.toString().trim();
    }

}
