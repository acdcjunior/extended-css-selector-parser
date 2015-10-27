/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2015 David Schweinsberg.  All rights reserved.
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
public class SerializableTest {

    @Test
    public void test() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("html40.css");
        Assert.assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

        // Serialize the style sheet
        final File temp = File.createTempFile("temp", "css");
        temp.deleteOnExit();
        final FileOutputStream fo = new FileOutputStream(temp);
        final ObjectOutput oo = new ObjectOutputStream(fo);
        oo.writeObject(stylesheet);
        oo.close();

        // Read it back in
        final FileInputStream fi = new FileInputStream(temp);
        final ObjectInput oi = new ObjectInputStream(fi);
        final CSSStyleSheet stylesheet2 = (CSSStyleSheet) oi.readObject();
        oi.close();

        final CSSRuleList rules = stylesheet2.getCssRules();

        // TODO
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            System.out.println(rule.getCssText());
        }
    }

}
