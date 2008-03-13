/*
 * $Id: ConditionFactoryImpl.java,v 1.2 2005-07-14 00:25:06 davidsch Exp $
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

package com.steadystate.css.parser.selectors;

import org.w3c.css.sac.*;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: ConditionFactoryImpl.java,v 1.2 2005-07-14 00:25:06 davidsch Exp $
 */
public class ConditionFactoryImpl implements ConditionFactory {

    public CombinatorCondition createAndCondition(
        Condition first, 
        Condition second) throws CSSException {
        return new AndConditionImpl(first, second);
    }

    public CombinatorCondition createOrCondition(
        Condition first, 
        Condition second) throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }

    public NegativeCondition createNegativeCondition(Condition condition)
        throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }

    public PositionalCondition createPositionalCondition(
        int position,
        boolean typeNode, 
        boolean type) throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }

    public AttributeCondition createAttributeCondition(
        String localName,
        String namespaceURI,
        boolean specified,
        String value) throws CSSException {
//        if ((namespaceURI != null) || !specified) {
//            throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
//        } else {
            return new AttributeConditionImpl(localName, value);
//        }
    }

    public AttributeCondition createIdCondition(String value)
        throws CSSException {
        return new IdConditionImpl(value);
    }

    public LangCondition createLangCondition(String lang)
	    throws CSSException {
    	return new LangConditionImpl(lang);
    }

    public AttributeCondition createOneOfAttributeCondition(
        String localName,
        String namespaceURI,
        boolean specified,
        String value) throws CSSException {
//        if ((namespaceURI != null) || !specified) {
//            throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
//        } else {
            return new OneOfAttributeConditionImpl(localName, value);
//        }
    }

    public AttributeCondition createBeginHyphenAttributeCondition(
        String localName,
        String namespaceURI,
        boolean specified,
        String value) throws CSSException {
//        if ((namespaceURI != null) || !specified) {
//            throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
//        } else {
            return new BeginHyphenAttributeConditionImpl(localName, value);
//        }
    }

    public AttributeCondition createClassCondition(
        String namespaceURI,
        String value) throws CSSException {
        return new ClassConditionImpl(value);
    }

    public AttributeCondition createPseudoClassCondition(
        String namespaceURI,
        String value) throws CSSException {
        return new PseudoClassConditionImpl(value);
    }

    public Condition createOnlyChildCondition() throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }

    public Condition createOnlyTypeCondition() throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }

    public ContentCondition createContentCondition(String data)
        throws CSSException {
        throw new CSSException(CSSException.SAC_NOT_SUPPORTED_ERR);
    }    
}
