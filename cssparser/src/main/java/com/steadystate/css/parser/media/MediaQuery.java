/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2015 David Schweinsberg.  All rights reserved.
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

package com.steadystate.css.parser.media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.steadystate.css.dom.Property;
import com.steadystate.css.parser.LocatableImpl;

/**
 * @author rbri
 */
public class MediaQuery extends LocatableImpl implements Serializable {

    private static final long serialVersionUID = 456776383828897471L;

    private String media_;
    private List<Property> properties_;

    public MediaQuery(final String media) {
        setMedia(media);
        properties_ = new ArrayList<Property>(10);
    }

    public String getMedia() {
        return media_;
    }

    public void setMedia(final String media) {
        media_ = media;
    }

    public List<Property> getProperties() {
        return properties_;
    }

    public void addMediaProperty(final Property mp) {
        properties_.add(mp);
    }

    @Override
    public String toString() {
        return getMedia();
    }
}
