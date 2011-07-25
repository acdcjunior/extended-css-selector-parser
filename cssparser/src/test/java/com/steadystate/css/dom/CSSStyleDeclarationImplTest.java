package com.steadystate.css.dom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Unit tests for {@link CSSStyleDeclarationImpl}.
 * 
 * @author Daniel Gredler
 * @author waldbaer
 * @author rbri
 */
public class CSSStyleDeclarationImplTest {

    /**
     * Regression test for bug 1874800.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void cssTextHasNoCurlyBraces() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        Assert.assertNotNull(is);

        CSSOMParser parser = new CSSOMParser();

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);

        Assert.assertFalse(style.getCssText().contains("{"));
        Assert.assertFalse(style.getCssText().contains("}"));

        style.setCssText("color: red;");
        Assert.assertEquals("color: red", style.getCssText());
    }

    /**
     * Regression test for bug 1691221.
     * 
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void emptyUrl() throws Exception {
        CSSOMParser parser = new CSSOMParser();

        Reader r = new StringReader("{ background: url() }");
        InputSource source = new InputSource(r);
        CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);

        Assert.assertEquals("", style.getCssText());
    }

    @Test
    public void serialize() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        Assert.assertNotNull(is);

        CSSOMParser parser = new CSSOMParser();

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        CSSStyleDeclarationImpl style = (CSSStyleDeclarationImpl) parser.parseStyleDeclaration(source);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(style);
        oos.flush();
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(baos.toByteArray()));
        Object o = ois.readObject();
        ois.close();
        Assert.assertEquals(style, o);
    }
}
