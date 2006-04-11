/*
 * Created on 05.12.2005
 *
 */
package com.steadystate.css.dom;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author koch
 *
 */
public class CSSOMObjectImpl implements CSSOMObject
{

    private Map userDataMap;
    
    private Map getUserDataMap()
    {
        if (this.userDataMap == null)
        {
            this.userDataMap = new Hashtable();
        }
        return this.userDataMap;
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

}
