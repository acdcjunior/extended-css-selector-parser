package com.steadystate.css.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;

import junit.framework.TestCase;

public class ImportantTest extends TestCase implements ErrorHandler
{

    public void testCSS1()
    {
        this.testCSS(new SACParserCSS1());
    }

    public void testCSS2()
    {
        this.testCSS(new SACParserCSS2());
    }

    public void testCSS21()
    {
        this.testCSS(new SACParserCSS21());
    }


    private void testCSS(Parser sacParser)
    {
    	sacParser.setErrorHandler(this);
        InputStream is = getClass().getClassLoader().getResourceAsStream("important.css");
        assertNotNull(is);
        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);
        try
        {
            sacParser.parseStyleSheet(source);
		}
        catch (CSSException e)
        {
            assertFalse(e.getLocalizedMessage(), true);
		}
        catch (IOException e)
        {
            assertFalse(e.getLocalizedMessage(), true);
		}
    }

	public void error(CSSParseException e) throws CSSException
	{
        assertFalse(e.getLocalizedMessage(), true);
	}

	public void fatalError(CSSParseException e) throws CSSException
	{
        assertFalse(e.getLocalizedMessage(), true);
	}

	public void warning(CSSParseException e) throws CSSException
	{
        assertFalse(e.getLocalizedMessage(), true);
	}

}
