package com.steadystate.css.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.Parser;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.stylesheets.MediaList;

import com.steadystate.css.dom.CSSOMObject;
import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import com.steadystate.css.userdata.UserDataConstants;

import junit.framework.TestCase;

public class LocatorTest extends TestCase
{

	private static final String CHARSET_RULE = "@charset \"utf-8\";\n";
	private static final String SIMPLE_IMPORT_RULE =
		"@import url('./import.css');\n";
	private static final String IMPORT_RULE =
		"@import url('./import.css') screen, projection;\n";
	private static final String UNKNOWN_AT_RULE = "@foo bar;\n";
	private static final String FONT_FACE_RULE = "@font-face {\n"
        + "  font-family: \"Robson Celtic\";\n"
        + "  src: url('http://site/fonts/rob-celt')\n"
        + "}\n";
	private static final String PAGE_RULE = "@page :left {\n" +
		"  margin: 3cm\n" +
		"}\n";
	private static final String MEDIA_RULE_START = "@media handheld {\n";
	private static final String STYLE_RULE =
		"h1, h2, .foo, h3#bar {\n"
		+ "  font-weight: bold;\n"
		+ "  padding-left: -1px;\n"
		+ "  border-right: medium solid #00f\n"
		+ "}\n";

	public void testLocationsCSS1()
    {
        String cssCode = SIMPLE_IMPORT_RULE
        	+ UNKNOWN_AT_RULE
        	+ STYLE_RULE;
        Map<Character, List<Integer[]>> positions =
        	new Hashtable<Character, List<Integer[]>>();
        List<Integer[]> rPos = new ArrayList<Integer[]>();
        rPos.add(new Integer[] {1,1});
        rPos.add(new Integer[] {2,1});
        rPos.add(new Integer[] {3,1});
        positions.put('R', rPos);
        List<Integer[]> pPos = new ArrayList<Integer[]>();
        pPos.add(new Integer[] {4,3});
        pPos.add(new Integer[] {5,3});
        pPos.add(new Integer[] {6,3});
        positions.put('P', pPos);
        List<Integer[]> vPos = new ArrayList<Integer[]>();
        vPos.add(new Integer[] {4,16});
        vPos.add(new Integer[] {5,17});
        vPos.add(new Integer[] {6,17});
        vPos.add(new Integer[] {6,17});
        vPos.add(new Integer[] {6,24});
        vPos.add(new Integer[] {6,30});
        positions.put('V', vPos);
        this.testLocations(new SACParserCSS1(), cssCode, positions);
    }

    public void testLocationsCSS2()
    {
        String cssCode = CHARSET_RULE
        	+ IMPORT_RULE
        	+ UNKNOWN_AT_RULE
        	+ PAGE_RULE
        	+ FONT_FACE_RULE
        	+ MEDIA_RULE_START
        	+ STYLE_RULE
        	+ "}\n";
        Map<Character, List<Integer[]>> positions =
        	new Hashtable<Character, List<Integer[]>>();
        List<Integer[]> rPos = new ArrayList<Integer[]>();
        rPos.add(new Integer[] {1,1});
        rPos.add(new Integer[] {2,1});
        rPos.add(new Integer[] {3,1});
        rPos.add(new Integer[] {4,1});
        rPos.add(new Integer[] {7,1});
        rPos.add(new Integer[] {11,1});
        rPos.add(new Integer[] {12,1});
        positions.put('R', rPos);
        List<Integer[]> mPos = new ArrayList<Integer[]>();
        mPos.add(new Integer[] {2,29});
        mPos.add(new Integer[] {11,16});
        positions.put('M', mPos);
        List<Integer[]> pPos = new ArrayList<Integer[]>();
        pPos.add(new Integer[] {5,3});
        pPos.add(new Integer[] {8,3});
        pPos.add(new Integer[] {9,3});
        pPos.add(new Integer[] {13,3});
        pPos.add(new Integer[] {14,3});
        pPos.add(new Integer[] {15,3});
        positions.put('P', pPos);
        List<Integer[]> vPos = new ArrayList<Integer[]>();
        vPos.add(new Integer[] {5,11});
        vPos.add(new Integer[] {8,16});
        vPos.add(new Integer[] {9,8});
        vPos.add(new Integer[] {13,16});
        vPos.add(new Integer[] {14,17});
        vPos.add(new Integer[] {15,17});
        vPos.add(new Integer[] {15,17});
        vPos.add(new Integer[] {15,24});
        vPos.add(new Integer[] {15,30});
        positions.put('V', vPos);
        this.testLocations(new SACParserCSS2(), cssCode, positions);
    }

    public void testLocationsCSS21()
    {
        String cssCode = CHARSET_RULE
        	+ IMPORT_RULE
        	+ UNKNOWN_AT_RULE
        	+ PAGE_RULE
        	+ FONT_FACE_RULE
        	+ MEDIA_RULE_START
        	+ STYLE_RULE
        	+ "}\n";
        Map<Character, List<Integer[]>> positions =
        	new Hashtable<Character, List<Integer[]>>();
        List<Integer[]> rPos = new ArrayList<Integer[]>();
        rPos.add(new Integer[] {1,1});
        rPos.add(new Integer[] {2,1});
        rPos.add(new Integer[] {3,1});
        rPos.add(new Integer[] {4,1});
        rPos.add(new Integer[] {7,1});
        rPos.add(new Integer[] {11,1});
        rPos.add(new Integer[] {12,1});
        positions.put('R', rPos);
        List<Integer[]> mPos = new ArrayList<Integer[]>();
        mPos.add(new Integer[] {2,29});
        mPos.add(new Integer[] {11,8});
        positions.put('M', mPos);
        List<Integer[]> pPos = new ArrayList<Integer[]>();
        pPos.add(new Integer[] {5,3});
        pPos.add(new Integer[] {13,3});
        pPos.add(new Integer[] {14,3});
        pPos.add(new Integer[] {15,3});
        positions.put('P', pPos);
        List<Integer[]> vPos = new ArrayList<Integer[]>();
        vPos.add(new Integer[] {5,11});
        vPos.add(new Integer[] {13,16});
        vPos.add(new Integer[] {14,17});
        vPos.add(new Integer[] {15,17});
        vPos.add(new Integer[] {15,17});
        vPos.add(new Integer[] {15,24});
        vPos.add(new Integer[] {15,30});
        positions.put('V', vPos);
        this.testLocations(new SACParserCSS21(), cssCode, positions);
    }

    public void testLocationsCSSmobileOKbasic1()
    {
        String cssCode = IMPORT_RULE
	    	+ UNKNOWN_AT_RULE
	    	+ PAGE_RULE
	    	+ FONT_FACE_RULE
	    	+ MEDIA_RULE_START
	    	+ STYLE_RULE
	    	+ "}\n";
	    Map<Character, List<Integer[]>> positions =
	    	new Hashtable<Character, List<Integer[]>>();
	    List<Integer[]> rPos = new ArrayList<Integer[]>();
	    rPos.add(new Integer[] {1,1});
	    rPos.add(new Integer[] {2,1});
	    rPos.add(new Integer[] {3,1});
	    rPos.add(new Integer[] {6,1});
	    rPos.add(new Integer[] {10,1});
	    rPos.add(new Integer[] {11,1});
	    rPos.add(new Integer[] {12,1});
	    positions.put('R', rPos);
	    List<Integer[]> mPos = new ArrayList<Integer[]>();
	    mPos.add(new Integer[] {1,29});
	    mPos.add(new Integer[] {10,16});
	    positions.put('M', mPos);
	    List<Integer[]> pPos = new ArrayList<Integer[]>();
	    pPos.add(new Integer[] {12,3});
	    pPos.add(new Integer[] {13,3});
	    pPos.add(new Integer[] {14,3});
	    positions.put('P', pPos);
	    List<Integer[]> vPos = new ArrayList<Integer[]>();
	    vPos.add(new Integer[] {12,16});
	    vPos.add(new Integer[] {13,17});
	    vPos.add(new Integer[] {14,17});
	    vPos.add(new Integer[] {14,17});
	    vPos.add(new Integer[] {14,24});
	    vPos.add(new Integer[] {14,30});
	    positions.put('V', vPos);
        this.testLocations(new SACParserCSSmobileOKBasic1(), cssCode, positions);
    }

    private void testLocations(Parser sacParser, String cssCode,
		Map<Character, List<Integer[]>> positions)
    {
//    	System.out.println(cssCode);
        Reader r = new StringReader(cssCode);
        InputSource source = new InputSource(r);
        CSSOMParser cssomParser = new CSSOMParser(sacParser);
        Map<Character, Integer> counts = new Hashtable<Character, Integer>();
        counts.put('R', 0);
        counts.put('M', 0);
        counts.put('P', 0);
        counts.put('V', 0);
        try
        {
        	CSSStyleSheet cssStyleSheet =
        		cssomParser.parseStyleSheet(source, null, null);
        	CSSRuleList cssRules = cssStyleSheet.getCssRules();
        	this.testCSSRules(cssRules, positions, counts);
//        	System.out.println("----------");
		}
        catch (IOException e)
        {
            assertFalse(e.getLocalizedMessage(), true);
		}
    }

    private void testCSSRules(CSSRuleList cssRules,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
    {
        for (int i = 0; i < cssRules.getLength(); i++)
        {
            this.testCSSRule(cssRules.item(i), positions, counts);
        }
    }

    private void testCSSRule(CSSRule cssRule,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
    {
        if (cssRule instanceof CSSOMObject)
        {
            Locator locator = this.getLocator((CSSOMObject) cssRule);
//            System.out.println(locator.toString() + " R " + cssRule);
            Integer[] expected = positions.get('R').get(counts.get('R'));
            int expectedLine = expected[0];
            int expectedColumn = expected[1];
            assertEquals(expectedLine, locator.getLineNumber());
            assertEquals(expectedColumn, locator.getColumnNumber());
            counts.put('R', counts.get('R') + 1);
        }
        switch (cssRule.getType())
        {
        case CSSRule.IMPORT_RULE:
            CSSImportRule cssImportRule = (CSSImportRule) cssRule;
            this.testMediaList(cssImportRule.getMedia(), positions, counts);
            break;
        case CSSRule.MEDIA_RULE:
            CSSMediaRule cssMediaRule = (CSSMediaRule) cssRule;
            this.testMediaList(cssMediaRule.getMedia(), positions, counts);
            this.testCSSRules(cssMediaRule.getCssRules(), positions, counts);
            break;
        case CSSRule.PAGE_RULE:
        	CSSPageRule cssPageRule = (CSSPageRule) cssRule;
        	this.testCSSStyleDeclaration(cssPageRule.getStyle(), positions,
    			counts);
        	break;
        case CSSRule.FONT_FACE_RULE:
        	CSSFontFaceRule cssFontFaceRule = (CSSFontFaceRule) cssRule;
        	this.testCSSStyleDeclaration(cssFontFaceRule.getStyle(), positions,
    			counts);
            break;
        case CSSRule.STYLE_RULE:
            CSSStyleRule cssStyleRule = (CSSStyleRule) cssRule;
            this.testCSSStyleDeclaration(cssStyleRule.getStyle(), positions,
    			counts);
            break;
        }
    }

	private void testCSSStyleDeclaration(CSSStyleDeclaration style,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
	{
		if (style instanceof CSSStyleDeclarationImpl)
		{
		    CSSStyleDeclarationImpl csdi = (CSSStyleDeclarationImpl) style;
		    Iterator<Property> it = csdi.getProperties().iterator();
		    while (it.hasNext())
		    {
		        this.testProperty(it.next(), positions, counts);
		    }
		}
	}

    private void testMediaList(MediaList mediaList,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
    {
    	if ((mediaList.getLength() > 0) && (mediaList instanceof CSSOMObject))
    	{
            Locator locator = this.getLocator((CSSOMObject) mediaList);
//            System.out.println("" + locator + " M " + mediaList);
            Integer[] expected = positions.get('M').get(counts.get('M'));
            int expectedLine = expected[0];
            int expectedColumn = expected[1];
            assertEquals(expectedLine, locator.getLineNumber());
            assertEquals(expectedColumn, locator.getColumnNumber());
            counts.put('M', counts.get('M') + 1);
    	}
    }

    private void testProperty(Property property,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
    {
        Locator locator = this.getLocator(property);
//        System.out.println(locator.toString() + " P " + property);
        Integer[] expected = positions.get('P').get(counts.get('P'));
        int expectedLine = expected[0];
        int expectedColumn = expected[1];
        assertEquals(expectedLine, locator.getLineNumber());
        assertEquals(expectedColumn, locator.getColumnNumber());
        counts.put('P', counts.get('P') + 1);
        this.testCSSValue(property.getValue(), positions, counts);
    }

    private void testCSSValue(CSSValue cssValue,
		Map<Character, List<Integer[]>> positions,
		Map<Character, Integer> counts)
    {
        if (cssValue instanceof CSSValueImpl)
        {
        	Locator locator = this.getLocator((CSSValueImpl) cssValue);
//            System.out.println(locator.toString() + " V " + cssValue);
            Integer[] expected = positions.get('V').get(counts.get('V'));
            int expectedLine = expected[0];
            int expectedColumn = expected[1];
            assertEquals(expectedLine, locator.getLineNumber());
            assertEquals(expectedColumn, locator.getColumnNumber());
	        counts.put('V', counts.get('V') + 1);
        }
        if (cssValue.getCssValueType() == CSSValue.CSS_VALUE_LIST)
        {
        	CSSValueList cssValueList = (CSSValueList) cssValue;
        	for (int i = 0; i < cssValueList.getLength(); i++)
        	{
        		this.testCSSValue(cssValueList.item(i), positions, counts);
        	}
        }
    }

    private Locator getLocator(CSSOMObject cssomObject)
    {
    	return (Locator) cssomObject.getUserData(UserDataConstants.KEY_LOCATOR);
    }
}
