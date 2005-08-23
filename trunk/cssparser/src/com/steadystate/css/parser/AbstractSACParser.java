/*
 * Created on 20.08.2005
 *
 */
package com.steadystate.css.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SelectorList;

import com.steadystate.css.parser.selectors.ConditionFactoryImpl;
import com.steadystate.css.parser.selectors.SelectorFactoryImpl;

/**
 * @author koch
 *
 */
abstract class AbstractSACParser implements Parser
{

    private DocumentHandler documentHandler = null;
    private ErrorHandler errorHandler = null;
    private InputSource source = null;
    private Locale locale = null;
    private SelectorFactory selectorFactory = null;
    private ConditionFactory conditionFactory = null;
    private ResourceBundle sacParserMessages;
    private Locator locator;


    protected DocumentHandler getDocumentHandler()
    {
        if (this.documentHandler == null)
        {
            this.setDocumentHandler(new HandlerBase());
        }
        return this.documentHandler;
    }

    public void setDocumentHandler(DocumentHandler handler)
    {
        this.documentHandler = handler;
    }

    protected ErrorHandler getErrorHandler()
    {
        if (this.errorHandler == null)
        {
            this.setErrorHandler(new HandlerBase());
        }
        return this.errorHandler;
    }

    public void setErrorHandler(ErrorHandler eh)
    {
        this.errorHandler = eh;
    }

    protected InputSource getInputSource()
    {
        return this.source;
    }

    public void setLocale(Locale locale)
    {
        if (this.locale != locale)
        {
            this.sacParserMessages = null;
        }
        this.locale = locale;
    }

    private Locale getLocale()
    {
        if (this.locale == null)
        {
            this.setLocale(Locale.getDefault());
        }
        return this.locale;
    }
    
    protected SelectorFactory getSelectorFactory()
    {
        if (this.selectorFactory == null)
        {
            this.selectorFactory = new SelectorFactoryImpl();
        }
        return this.selectorFactory;
    }

    public void setSelectorFactory(SelectorFactory selectorFactory)
    {
        this.selectorFactory = selectorFactory;
    }
    
    protected ConditionFactory getConditionFactory()
    {
        if (this.conditionFactory == null)
        {
            this.conditionFactory = new ConditionFactoryImpl();
        }
        return this.conditionFactory;
    }

    public void setConditionFactory(ConditionFactory conditionFactory)
    {
        this.conditionFactory = conditionFactory;
    }

    protected ResourceBundle getSACParserMessages()
    {
        if (this.sacParserMessages == null)
        {
            this.sacParserMessages =
                ResourceBundle.getBundle(this.getClass().getPackage().getName()
                    + ".SACParserMessages", this.getLocale());
        }
        return this.sacParserMessages;
    }

    public Locator getLocator()
    {
        return this.locator;
    }
    
    protected void setLocator(Locator locator)
    {
        this.locator = locator;
    }
    
    protected String add_escapes(String str)
    {
        StringBuffer retval = new StringBuffer();
        char ch;
        for (int i = 0; i < str.length(); i++)
        {
            switch (str.charAt(i))
            {
                case 0 :
                    continue;
                case '\b':
                    retval.append("\\b");
                    continue;
                case '\t':
                    retval.append("\\t");
                    continue;
                case '\n':
                    retval.append("\\n");
                    continue;
                case '\f':
                    retval.append("\\f");
                    continue;
                case '\r':
                    retval.append("\\r");
                    continue;
                case '\"':
                    retval.append("\\\"");
                    continue;
                case '\'':
                    retval.append("\\\'");
                    continue;
                case '\\':
                    retval.append("\\\\");
                    continue;
                default:
                    if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e)
                    {
                       String s = "0000" + Integer.toString(ch, 16);
                       retval.append("\\u"
                           + s.substring(s.length() - 4, s.length()));
                    }
                    else
                    {
                       retval.append(ch);
                    }
                    continue;
            }
        }
        return retval.toString();
    }

    protected CSSParseException toCSSParseException(ParseException e)
    {
        String messagePattern1 =
            this.getSACParserMessages().getString("invalidExpectingOne");
        String messagePattern2 =
            this.getSACParserMessages().getString("invalidExpectingMore");
        int maxSize = 0;
        StringBuffer expected = new StringBuffer();
        for (int i = 0; i < e.expectedTokenSequences.length; i++)
        {
            if (maxSize < e.expectedTokenSequences[i].length)
            {
                maxSize = e.expectedTokenSequences[i].length;
            }
            for (int j = 0; j < e.expectedTokenSequences[i].length; j++)
            {
                expected.append(e.tokenImage[e.expectedTokenSequences[i][j]]);
            }
            //if (e.expectedTokenSequences[i][e.expectedTokenSequences[i].length - 1] != 0)
            if (i < e.expectedTokenSequences.length - 1)
            {
                expected.append(", ");
            }
        }
        StringBuffer invalid = new StringBuffer();
        Token tok = e.currentToken.next;
        for (int i = 0; i < maxSize; i++)
        {
            if (i != 0)
            {
                invalid.append(" ");
            }
            if (tok.kind == 0)
            {
                invalid.append(e.tokenImage[0]);
                break;
            }
            invalid.append(this.add_escapes(tok.image));
            tok = tok.next;
        }
        StringBuffer message = new StringBuffer();
        if (e.expectedTokenSequences.length == 1)
        {
            message.append(MessageFormat.format(
                messagePattern1, new Object[] {invalid, expected}));
        }
        else
        {
            message.append(MessageFormat.format(
                messagePattern2, new Object[] {invalid, expected}));
        }
        return new CSSParseException(message.toString(),
            this.getInputSource().getURI(), e.currentToken.next.beginLine,
            e.currentToken.next.beginColumn);
    }

    public void parseStyleSheet(InputSource source)
        throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        try
        {
            this.styleSheet();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
    }
    
    public void parseStyleSheet(String uri) throws IOException
    {
        this.parseStyleSheet(new InputSource(uri));
    }

    public void parseStyleDeclaration(InputSource source)
        throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        try
        {
            this.styleDeclaration();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
    }
    
    public void parseRule(InputSource source) throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        try
        {
            this.styleSheetRuleSingle();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
    }
    
    public SelectorList parseSelectors(InputSource source)
        throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        SelectorList sl = null;
        try
        {
            sl = this.selectorList();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
        return sl;
    }
    
    public LexicalUnit parsePropertyValue(InputSource source)
        throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        LexicalUnit lu = null;
        try
        {
            lu = expr();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
        return lu;
    }
    
    public boolean parsePriority(InputSource source)
        throws IOException
    {
        this.source = source;
        this.ReInit(getCharStream(source));
        boolean b = false;
        try
        {
            b = prio();
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
        return b;
    }
    
    public SACMediaList parseMedia(InputSource source) throws IOException
    {
        this.source = source;
        this.ReInit(this.getCharStream(source));
        SACMediaListImpl ml = new SACMediaListImpl();
        try
        {
            this.mediaList(ml);
        }
        catch (ParseException e)
        {
            this.getErrorHandler().error(this.toCSSParseException(e));
        }
        return ml;
    }
    
    private CharStream getCharStream(InputSource source)
        throws IOException
    {
        if (source.getCharacterStream() != null)
        {
            return new ASCII_CharStream(
                source.getCharacterStream(), 1, 1);
        }
        else if (source.getCharacterStream() != null)
        {
            return new ASCII_CharStream(new InputStreamReader(
                source.getByteStream()), 1, 1);
        }
        else if (source.getURI() != null)
        {
            return new ASCII_CharStream(new InputStreamReader(
                new URL(source.getURI()).openStream()), 1, 1);
        }
        return null;
    }

    public abstract String getParserVersion();
    protected abstract void ReInit(CharStream charStream);
    protected abstract void styleSheet() throws ParseException;
    protected abstract void styleDeclaration() throws ParseException;
    protected abstract void styleSheetRuleSingle() throws ParseException;
    protected abstract SelectorList selectorList() throws ParseException;
    protected abstract LexicalUnit expr() throws ParseException;
    protected abstract boolean prio() throws ParseException;
    protected abstract void mediaList(SACMediaListImpl ml) throws ParseException;

}
