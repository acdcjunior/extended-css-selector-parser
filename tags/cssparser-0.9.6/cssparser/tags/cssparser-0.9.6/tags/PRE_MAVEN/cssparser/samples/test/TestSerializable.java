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
 * $Id: TestSerializable.java,v 1.2 2007-08-14 09:43:30 waldbaer Exp $
 */

package test;

import com.steadystate.css.parser.CSSOMParser;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

/**
 *
 * @author  David Schweinsberg
 * @version $Release$
 */
public class TestSerializable {

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        try {
//            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\test.css");
            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\html40.css");
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);

            CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);
            
            // Serialize the style sheet
            FileOutputStream fo = new FileOutputStream("c:\\working\\css2parser\\stylesheets\\tmp");
            ObjectOutput oo = new ObjectOutputStream(fo);
            oo.writeObject(stylesheet);
            oo.flush();
            
            // Read it back in
            FileInputStream fi = new FileInputStream("c:\\working\\css2parser\\stylesheets\\tmp");
            ObjectInput oi = new ObjectInputStream(fi);
            CSSStyleSheet stylesheet2 = (CSSStyleSheet) oi.readObject();
            
            CSSRuleList rules = stylesheet2.getCssRules();

            for (int i = 0; i < rules.getLength(); i++) {
                CSSRule rule = rules.item(i);
                System.out.println(rule.getCssText());
            }
        } catch (Exception e) {
            System.out.println("Error.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
