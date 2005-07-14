/*
 * $Id: LocatorImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
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

import org.w3c.css.sac.Locator;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: LocatorImpl.java,v 1.2 2005-07-14 00:25:05 davidsch Exp $
 */
public class LocatorImpl implements Locator {

    private String _uri;
    private int _line;
    private int _column;

    /** Creates new LocatorImpl */
    public LocatorImpl() {
    }

    /**
     * Return the line number where the current document event ends.
     * Note that this is the line position of the first character
     * after the text associated with the document event.
     * @return The line number, or -1 if none is available.
     * @see #getColumnNumber
     */
    public int getLineNumber() {
        return _line;
    }
    
    /**
     * Return the URI for the current document event.
     *
     * <p>The parser must resolve the URI fully before passing it to the
     * application.</p>
     *
     * @return A string containing the URI, or null
     *        if none is available.
     */
    public String getURI() {
        return _uri;
    }
    
    /**
     * Return the column number where the current document event ends.
     * Note that this is the column number of the first
     * character after the text associated with the document
     * event.  The first column in a line is position 1.
     * @return The column number, or -1 if none is available.
     * @see #getLineNumber
     */
    public int getColumnNumber() {
        return _column;
    }
    
}
