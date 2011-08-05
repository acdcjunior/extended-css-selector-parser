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

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;

/**
 *
 * @author rbri
 */
public class SACParserCSSmobileOKBasic1Test {

    private static final Parser SAC_PARSER = new SACParserCSSmobileOKBasic1();

    @Test
    public void selectorList() throws Exception {
        selectorList("h1:first-line", 1);
        selectorList("h1", 1);
        selectorList("h1, h2", 2);
        selectorList("h1:first-line, h2", 2);
        selectorList("h1, h2, h3", 3);
        selectorList("h1, h2,\nh3", 3);
        selectorList("h1, h2, h3#id", 3);
        selectorList("h1.class, h2, h3", 3);
    }

    @Test
    public void selector() throws Exception {
        selectorType("a#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a", Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("#id.class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id.class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("#id", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class:link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(".class", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType(":link", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:visited", Selector.SAC_CONDITIONAL_SELECTOR);
        selectorType("a:active", Selector.SAC_CONDITIONAL_SELECTOR);

        selectorType("h1:first-line",
                Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:first-letter",
                Selector.SAC_DESCENDANT_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR,
                Selector.SAC_PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1 a", Selector.SAC_DESCENDANT_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR);
        selectorType("h1  a", Selector.SAC_DESCENDANT_SELECTOR,
                Selector.SAC_ELEMENT_NODE_SELECTOR, Selector.SAC_ELEMENT_NODE_SELECTOR);
    }

    @Test
    public void condition() throws Exception {
        conditionType("a#id.class:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id.class", Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION);
        conditionType("a#id:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a#id", Condition.SAC_ID_CONDITION);
        conditionType("a.class:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("a.class", Condition.SAC_CLASS_CONDITION);
        conditionType("a:link", Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_AND_CONDITION, Condition.SAC_ID_CONDITION,
                Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id.class", Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_CLASS_CONDITION);
        conditionType("#id:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_ID_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType("#id", Condition.SAC_ID_CONDITION);
        conditionType(".class:link", Condition.SAC_AND_CONDITION,
                Condition.SAC_CLASS_CONDITION, Condition.SAC_PSEUDO_CLASS_CONDITION);
        conditionType(".class", Condition.SAC_CLASS_CONDITION);
        conditionType(":link", Condition.SAC_PSEUDO_CLASS_CONDITION);
    }

    @Test
    public void attributeCondition() throws Exception {
        attributeConditionValue(".class", "class");
        attributeConditionValue("#id", "id");
        attributeConditionValue("h1.class", "class");
        attributeConditionValue("h1#id", "id");
        attributeConditionValue("a:link", "link");
        attributeConditionValue(":link", "link");
        attributeConditionValue("a:visited", "visited");
        attributeConditionValue(":visited", "visited");
        attributeConditionValue("a:active", "active");
        attributeConditionValue(":active", "active");
    }

    private void selectorList(final String cssText, final int length) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(length, selectors.getLength());
    }

    private void selectorType(final String cssText, final int... selectorTypes) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
        Assert.assertEquals(selectorTypes[0], selector.getSelectorType());

        if (selectorTypes[0] == Selector.SAC_DESCENDANT_SELECTOR) {
            final DescendantSelector descendantSelector = (DescendantSelector) selector;
            final Selector ancestor = descendantSelector.getAncestorSelector();
            Assert.assertEquals(selectorTypes[1], ancestor.getSelectorType());

            final SimpleSelector simple = descendantSelector.getSimpleSelector();
            Assert.assertEquals(selectorTypes[2], simple.getSelectorType());
        }
    }

    private void conditionType(final String cssText, final int... conditionTypes) throws Exception {
        final Condition condition = createCondition(cssText);
        conditionType(condition, 0, conditionTypes);
    }

    private int conditionType(final Condition condition, int initial, final int... conditionTypes) {
        Assert.assertEquals(conditionTypes[initial], condition.getConditionType());
        if (conditionTypes[initial] == Condition.SAC_AND_CONDITION) {
            final CombinatorCondition combinatorCondition = (CombinatorCondition) condition;
            final Condition first = combinatorCondition.getFirstCondition();
            final Condition second = combinatorCondition.getSecondCondition();
            initial = conditionType(first, ++initial, conditionTypes);
            initial = conditionType(second, ++initial, conditionTypes);
        }
        return initial;
    }

    private void attributeConditionValue(final String cssText, final String value) throws Exception {
        final Condition condition = createCondition(cssText);
        final AttributeCondition attributeCondition = (AttributeCondition) condition;

        Assert.assertEquals(value, attributeCondition.getValue());
    }

    private SelectorList createSelectors(final String cssText) throws Exception {
        final InputSource source = new InputSource(new StringReader(cssText));
        return SAC_PARSER.parseSelectors(source);
    }

    private Condition createCondition(final String cssText) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
        final ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
        return conditionalSelector.getCondition();
    }
}
