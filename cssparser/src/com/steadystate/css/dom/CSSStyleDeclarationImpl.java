/*
 * $Id: CSSStyleDeclarationImpl.java,v 1.6 2008-01-14 11:14:24 waldbaer Exp $
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
import java.io.StringReader;

import java.util.Vector;

import org.w3c.css.sac.InputSource;

import org.w3c.dom.DOMException;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.parser.CSSOMParser;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSStyleDeclarationImpl.java,v 1.6 2008-01-14 11:14:24 waldbaer Exp $
 */
public class CSSStyleDeclarationImpl implements CSSStyleDeclaration, Serializable
{

    private static final long serialVersionUID = -2373755821317100189L;

    private CSSRule parentRule;
    private Vector properties = new Vector();

    public void setParentRule(CSSRule parentRule)
    {
        this.parentRule = parentRule;
    }

    public Vector getProperties()
    {
        return this.properties;
    }

    public void setProperties(Vector properties)
    {
        this.properties = properties;
    }


    public CSSStyleDeclarationImpl(CSSRule parentRule) {
        this.parentRule = parentRule;
    }

    public CSSStyleDeclarationImpl()
    {
    }


    public String getCssText() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        //if newlines requested in text
        //sb.append("\n");
        for (int i = 0; i < this.properties.size(); ++i) {
            Property p = (Property) this.properties.elementAt(i);
            if (p != null) {
                sb.append(p.toString());
            }
            if (i < this.properties.size() - 1) {
                sb.append("; ");
            }
            //if newlines requested in text
            //sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public void setCssText(String cssText) throws DOMException {
        try {
            InputSource is = new InputSource(new StringReader(cssText));
            CSSOMParser parser = new CSSOMParser();
            this.properties.removeAllElements();
            parser.parseStyleDeclaration(this, is);
        } catch (Exception e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
    }

    public String getPropertyValue(String propertyName) {
        Property p = this.getPropertyDeclaration(propertyName);
        return (p != null) ? p.getValue().toString() : "";
    }

    public CSSValue getPropertyCSSValue(String propertyName) {
        Property p = this.getPropertyDeclaration(propertyName);
        return (p != null) ? p.getValue() : null;
    }

    public String removeProperty(String propertyName) throws DOMException {
        for (int i = 0; i < this.properties.size(); i++) {
            Property p = (Property) this.properties.elementAt(i);
            if (p.getName().equalsIgnoreCase(propertyName)) {
                this.properties.removeElementAt(i);
                return p.getValue().toString();
            }
        }
        return "";
    }

    public String getPropertyPriority(String propertyName) {
        Property p = this.getPropertyDeclaration(propertyName);
        if (p != null) {
            return p.isImportant() ? "important" : "";
        }
        return "";
    }

    public void setProperty(
            String propertyName,
            String value,
            String priority ) throws DOMException {
        try {
            InputSource is = new InputSource(new StringReader(value));
            CSSOMParser parser = new CSSOMParser();
            CSSValue expr = parser.parsePropertyValue(is);
            Property p = this.getPropertyDeclaration(propertyName);
            boolean important = (priority != null)
                ? priority.equalsIgnoreCase("important")
                : false;
            if (p == null) {
                p = new Property(propertyName, expr, important);
                addProperty(p);
            } else {
                p.setValue(expr);
                p.setImportant(important);
            }
        } catch (Exception e) {
            throw new DOMExceptionImpl(
            DOMException.SYNTAX_ERR,
            DOMExceptionImpl.SYNTAX_ERROR,
            e.getMessage());
        }
    }
    
    public int getLength() {
        return this.properties.size();
    }

    public String item(int index) {
        Property p = (Property) this.properties.elementAt(index);
        return (p != null) ? p.getName() : "";
    }

    public CSSRule getParentRule() {
        return this.parentRule;
    }

    public void addProperty(Property p) {
        this.properties.addElement(p);
    }

    public Property getPropertyDeclaration(String name) {
        for (int i = 0; i < this.properties.size(); i++) {
            Property p = (Property) this.properties.elementAt(i);
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public String toString() {
        return this.getCssText();
    }
}
