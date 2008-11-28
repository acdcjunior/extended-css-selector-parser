/*
 * $Id: ConditionalSelectorImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
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

import java.io.Serializable;
import org.w3c.css.sac.*;

import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: ConditionalSelectorImpl.java,v 1.2 2008-11-28 13:05:17 waldbaer Exp $
 */
public class ConditionalSelectorImpl extends LocatableImpl implements ConditionalSelector, Serializable {

    private static final long serialVersionUID = 7217145899707580586L;

    private SimpleSelector simpleSelector;
    private Condition condition;

    public void setSimpleSelector(SimpleSelector simpleSelector)
    {
        this.simpleSelector = simpleSelector;
        if (simpleSelector instanceof Locatable)
        {
        	this.setLocator(((Locatable) simpleSelector).getLocator());
        }
        else if (simpleSelector == null)
        {
        	this.setLocator(null);
        }
    }

    public void setCondition(Condition condition)
    {
        this.condition = condition;
        if (this.getLocator() == null)
        {
            if (condition instanceof Locatable)
            {
            	this.setLocator(((Locatable) condition).getLocator());
            }
            else if (condition == null)
            {
            	this.setLocator(null);
            }
        }
    }


    public ConditionalSelectorImpl(
        SimpleSelector simpleSelector,
        Condition condition) {
        this.setSimpleSelector(simpleSelector);
        this.setCondition(condition);
    }

    public ConditionalSelectorImpl()
    {
    }


    public short getSelectorType() {
        return Selector.SAC_CONDITIONAL_SELECTOR;
    }

    public SimpleSelector getSimpleSelector() {
        return this.simpleSelector;
    }

    public Condition getCondition() {
        return this.condition;
    }
    
    public String toString() {
        return this.simpleSelector.toString() + this.condition.toString();
    }
}
