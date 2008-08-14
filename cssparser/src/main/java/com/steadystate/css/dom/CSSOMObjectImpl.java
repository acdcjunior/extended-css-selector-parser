/*
 * $Id: CSSOMObjectImpl.java,v 1.3 2008-08-14 08:17:55 waldbaer Exp $
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
import java.util.Hashtable;
import java.util.Map;

import com.steadystate.css.util.LangUtils;

/**
 * Implementation of {@link CSSOMObject}.
 * 
 * @author koch
 */
public class CSSOMObjectImpl implements CSSOMObject, Serializable
{

    private static final long serialVersionUID = 0L;

    private Map<String, Object> userDataMap;

    public Map<String, Object> getUserDataMap()
    {
        if (this.userDataMap == null)
        {
            this.userDataMap = new Hashtable<String, Object>();
        }
        return this.userDataMap;
    }

    public void setUserDataMap(Map<String, Object> userDataMap)
    {
        this.userDataMap = userDataMap;
    }


    public CSSOMObjectImpl()
    {
        super();
    }


    public Object getUserData(String key)
    {
        return this.getUserDataMap().get(key);
    }

    public Object setUserData(String key, Object data)
    {
        return this.getUserDataMap().put(key, data);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof CSSOMObjectImpl))
        {
            return false;
        }
        CSSOMObjectImpl coi = (CSSOMObjectImpl) obj;
        return LangUtils.equals(this.userDataMap, coi.userDataMap);
    }

    @Override
    public int hashCode()
    {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.userDataMap);
        return hash;
    }
}
