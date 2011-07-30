/*
 * TestSerializable.java
 *
 * Steady State CSS2 Parser
 *
 * Copyright (C) 1999, 2002 Steady State Software Ltd.  All rights reserved.
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
 * To contact the authors of the library, write to Steady State Software Ltd.,
 * 49 Littleworth, Wing, Buckinghamshire, LU7 0JX, England
 *
 * http://www.steadystate.com/css/
 * mailto:css@steadystate.co.uk
 *
 * $Id: TestSerializable.java,v 1.2 2008-03-20 02:49:41 sdanig Exp $
 */

package com.steadystate.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

/**
 * @author David Schweinsberg
 * @author rbri
 */
public class TestSerializable {

    @Test
    public void test() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("html40.css");
        Assert.assertNotNull(is);

        CSSOMParser parser = new CSSOMParser();

        Reader r = new InputStreamReader(is);
        InputSource source = new InputSource(r);

        CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

        // Serialize the style sheet
        File temp = File.createTempFile("temp", "css");
        temp.deleteOnExit();
        FileOutputStream fo = new FileOutputStream(temp);
        ObjectOutput oo = new ObjectOutputStream(fo);
        oo.writeObject(stylesheet);
        oo.flush();

        // Read it back in
        FileInputStream fi = new FileInputStream(temp);
        ObjectInput oi = new ObjectInputStream(fi);
        CSSStyleSheet stylesheet2 = (CSSStyleSheet) oi.readObject();

        CSSRuleList rules = stylesheet2.getCssRules();

        for (int i = 0; i < rules.getLength(); i++) {
            CSSRule rule = rules.item(i);
            System.out.println(rule.getCssText());
        }
    }

}
