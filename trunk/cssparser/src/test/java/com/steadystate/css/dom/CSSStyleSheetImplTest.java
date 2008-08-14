/*
 * Created on 01.08.2008
 *
 */
package com.steadystate.css.dom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

public class CSSStyleSheetImplTest
{

    @Test
    public void serializeTest()
    {
        String cssText =
            "h1 {\n" +
            "  font-size: 2em\n" +
            "}\n" +
            "\n" +
            "@media handheld {\n" +
            "  h1 {\n" +
            "    font-size: 1.5em\n" +
            "  }\n" +
            "}";
        InputSource source = new InputSource(new StringReader(cssText));
        CSSOMParser cssomParser = new CSSOMParser();
        try
        {
            CSSStyleSheet css = cssomParser.parseStyleSheet(source, null,
                "http://www.example.org/css/style.css");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(css);
            oos.flush();
            oos.close();
            byte[] bytes = baos.toByteArray();
            ObjectInputStream ois =
                new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object o = ois.readObject();
            Assert.assertEquals(css, o);
        }
        catch (IOException e)
        {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
        catch (ClassNotFoundException e)
        {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

}
