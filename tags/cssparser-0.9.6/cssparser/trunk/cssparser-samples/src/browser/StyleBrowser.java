/*
 * $Id: StyleBrowser.java,v 1.1 2005-07-29 17:14:05 davidsch Exp $
 *
 * CSS Parser Project
 *
 * Copyright (C) 1999-2005 David Schweinsberg.  All rights reserved.
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
 */

package browser;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import com.steadystate.css.parser.CSSOMParser;
import org.w3c.dom.css.*;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: StyleBrowser.java,v 1.1 2005-07-29 17:14:05 davidsch Exp $
 */
public class StyleBrowser {

    private JFrame _frame;
    private JTree _tree;
    private DefaultTreeModel _treeModel;

    public StyleBrowser() {
        _frame = new JFrame("Style Browser");

        JPanel panel = new JPanel(true);
        _frame.getContentPane().add("Center", panel);

        _frame.setJMenuBar(createMenuBar());
        //_frame.setBackground(Color.lightGray);

        _tree = new JTree((TreeModel)null);

        // Enable tool tips for the tree, without this tool tips will not
        // be picked up
        ToolTipManager.sharedInstance().registerComponent(_tree);

        // Make the tree use an instance of StyleBrowserCellRenderer for
        // drawing
        _tree.setCellRenderer(new StyleBrowserCellRenderer());

        // Put the Tree in a scroller
        JScrollPane sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(500, 500));
        sp.getViewport().add(_tree);

        // Show it
        panel.setLayout(new BorderLayout());
        panel.add("Center", sp);

        _frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        _frame.pack();
        _frame.show();
    }

    private JMenuBar createMenuBar() {
        JMenu menu;
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;

        menu = new JMenu("File");
        menuBar.add(menu);

        menuItem = menu.add(new JMenuItem("Open..."));
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openStyleSheet();
                    }
                }
        );

        menuItem = menu.add(new JMenuItem("Exit"));
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        menu = new JMenu("Help");
        menuBar.add(menu);

        menuItem = menu.add(new JMenuItem("About"));
        menuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showAbout();
                    }
                }
        );

        return menuBar;
    }

    private void loadStyleSheet(String pathName) {
        try {
            _frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Parse the style sheet
            // (the old way)
//            InputStream is = new FileInputStream(pathName);
//            CSS2Parser parser = new CSS2Parser(is);
//            CSSStyleSheet stylesheet = parser.styleSheet();
            
            // (the new way)
            Reader r = new FileReader(pathName);
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);
            CSSStyleSheet stylesheet = parser.parseStyleSheet(is);

            // Create the tree to put the information in
            _treeModel = (DefaultTreeModel)
            StyleBrowserTreeBuilder.createStyleSheetTree(stylesheet);

            _tree.setModel(_treeModel);

            _frame.setTitle("Style Browser - " + pathName);
        }
        catch(CSSException e) {
            JOptionPane.showMessageDialog(
            null,
            e.getMessage(),
            "CSS Exception",
            JOptionPane.ERROR_MESSAGE);
        }
        catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(
            null,
            e.getMessage(),
            "File Not Found",
            JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(
            null,
            e.getMessage(),
            "I/O Exception",
            JOptionPane.ERROR_MESSAGE);
        }
        finally {
            _frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void openStyleSheet() {
        JFileChooser chooser = new JFileChooser();

        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("css");
        filter.setDescription("Cascading Style Sheets");

        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(_frame) == JFileChooser.APPROVE_OPTION)
        loadStyleSheet(chooser.getSelectedFile().getPath());
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
        null,
        "DOM CSS Style Browser\n" +
        "Copyright (C) 1999, 2002 Steady State Software Ltd.\n" +
        "http://www.steadystate.com/css/",
        "About Style Browser",
        JOptionPane.INFORMATION_MESSAGE);
    }

    static public void main(String[] args) {
        new StyleBrowser();
    }
}
