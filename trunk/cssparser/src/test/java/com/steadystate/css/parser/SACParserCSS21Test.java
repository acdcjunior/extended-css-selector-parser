package com.steadystate.css.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

public class SACParserCSS21Test extends TestCase {

    public void testDojoCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        SACParserCSS21 sp = new SACParserCSS21();
        CSSOMParser parser = new CSSOMParser(sp);
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
        assertEquals(17, sheet.getCssRules().getLength());
    }

    public void testTestCSS() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("test.css");
        assertNotNull(is);

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

//        System.out.println(sheet);
//        assertEquals(21, sheet.getCssRules().getLength());
        assertEquals(78, sheet.getCssRules().getLength());
    }

}
