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

package com.steadystate.css.parser;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.SelectorList;

import com.steadystate.css.parser.selectors.PrefixAttributeConditionImpl;
import com.steadystate.css.parser.selectors.SubstringAttributeConditionImpl;
import com.steadystate.css.parser.selectors.SuffixAttributeConditionImpl;

/**
 * @author Ahmed Ashour
 */
public class SACParserCSS3Test {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void prefixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel^=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof PrefixAttributeConditionImpl);
    }
    
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void suffixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel$=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof SuffixAttributeConditionImpl);
    }
    
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void substringAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel*=val]");
        final ConditionalSelector selector = (ConditionalSelector) selectors.item(0);
        Assert.assertTrue(selector.getCondition() instanceof SubstringAttributeConditionImpl);
    }
    
    private SelectorList createSelectors(final String cssText) throws Exception {
        final InputSource source = new InputSource(new StringReader(cssText));
        return new SACParserCSS3().parseSelectors(source);
    }

}
