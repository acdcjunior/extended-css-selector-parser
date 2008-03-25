package com.steadystate.css.dom;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Unit tests for {@link CSSStyleDeclarationImpl}.
 * @author Daniel Gredler
 */
public class CSSStyleDeclarationImplTest extends TestCase {

    /**
     * Regression test for bug 1874800.
     * @throws Exception if any error occurs
     */
    public void testCssTextHasNoCurlyBraces() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        assertNotNull(is);

        CSSOMParser parser = new CSSOMParser();

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);

        assertFalse(style.getCssText().contains("{"));
        assertFalse(style.getCssText().contains("}"));

        style.setCssText("color: red;");
        assertEquals("color: red", style.getCssText());
    }

}
