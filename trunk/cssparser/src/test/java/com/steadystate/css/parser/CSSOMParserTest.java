/*
 * $Id: CSSOMParserTest.java,v 1.1 2008-03-20 01:20:18 sdanig Exp $
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

import junit.framework.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.Properties;
import java.util.Stack;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import org.w3c.css.sac.helpers.ParserFactory;

import com.steadystate.css.dom.CSSFontFaceRuleImpl;
import com.steadystate.css.dom.CSSImportRuleImpl;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSPageRuleImpl;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSUnknownRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;

/**
 *
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: CSSOMParserTest.java,v 1.1 2008-03-20 01:20:18 sdanig Exp $
 */
public class CSSOMParserTest extends TestCase {

    String _testSelector = "FOO";
    String _testItem = "color";
    String _testValue = "rgb(1, 2, 3)";
    String _testString = _testSelector + "{ " + _testItem + ": " + _testValue + " }";
    CSSOMParser _parser = new CSSOMParser();
    
    public CSSOMParserTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CSSOMParserTest.class);
        
        return suite;
    }

    /**
     * Test of parseStyleSheet method, of class com.steadystate.css.parser.CSSOMParser.
     */
    public void testParseStyleSheet() throws IOException {
        System.out.println("testParseStyleSheet");
        
        Reader r = new StringReader(_testString);
        InputSource is = new InputSource(r);
        CSSStyleSheet ss = _parser.parseStyleSheet(is, null, null);
        CSSRuleList rl = ss.getCssRules();
        CSSRule rule = rl.item(0);
        if (rule.getType() == CSSRule.STYLE_RULE) {
            CSSStyleRule sr = (CSSStyleRule) rule;
            assertEquals(sr.getSelectorText(), _testSelector);
            
            CSSStyleDeclaration style = sr.getStyle();
            assertEquals(style.item(0), _testItem);

            CSSValue value = style.getPropertyCSSValue(style.item(0));
            assertEquals(value.getCssText(), _testValue);
        } else {
            fail();
        }
    }

    /**
     * Test of parseStyleDeclaration method, of class com.steadystate.css.parser.CSSOMParser.
     */
//    public void testParseStyleDeclaration() {
//        System.out.println("testParseStyleDeclaration");
        
        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of parsePropertyValue method, of class com.steadystate.css.parser.CSSOMParser.
     */
//    public void testParsePropertyValue() {
//        System.out.println("testParsePropertyValue");
        
        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of parseRule method, of class com.steadystate.css.parser.CSSOMParser.
     */
//    public void testParseRule() {
//        System.out.println("testParseRule");
        
        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of parseSelectors method, of class com.steadystate.css.parser.CSSOMParser.
     */
    public void testParseSelectors() throws IOException {
        System.out.println("testParseSelectors");
        
        Reader r = new StringReader(_testSelector);
        InputSource is = new InputSource(r);
        SelectorList sl = _parser.parseSelectors(is);
        assertEquals(sl.item(0).toString(), _testSelector);
    }
}
