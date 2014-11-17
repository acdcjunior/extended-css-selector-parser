/*
 * CSS Parser Project
 *
 * Copyright (C) 1999-2014 David Schweinsberg.  All rights reserved.
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
package net.sourceforge.cssparser.browser;

import com.steadystate.css.parser.CSSOMParser;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David
 * Schweinsberg</a>
 */
public class StyleBrowser {

    private final JFrame frame_;
    private final JTree tree_;
    private DefaultTreeModel treeModel_;

    public StyleBrowser() {
        frame_ = new JFrame("Style Browser");

        JPanel panel = new JPanel(true);
        frame_.getContentPane().add("Center", panel);

        frame_.setJMenuBar(createMenuBar());

        tree_ = new JTree((TreeModel) null);

        // Enable tool tips for the tree, without this tool tips will not
        // be picked up
        ToolTipManager.sharedInstance().registerComponent(tree_);

        // Make the tree use an instance of StyleBrowserCellRenderer for
        // drawing
        tree_.setCellRenderer(new StyleBrowserCellRenderer());

        // Put the Tree in a scroller
        JScrollPane sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(500, 500));
        sp.getViewport().add(tree_);

        // Show it
        panel.setLayout(new BorderLayout());
        panel.add("Center", sp);

        frame_.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        frame_.pack();
        frame_.show();
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
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openStyleSheet();
                    }
                }
        );

        menuItem = menu.add(new JMenuItem("Exit"));
        menuItem.addActionListener(
                new ActionListener() {
                    @Override
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
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showAbout();
                    }
                }
        );

        return menuBar;
    }

    private void loadStyleSheet(String pathName) {
        try {
            frame_.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Parse the style sheet
            Reader r = new FileReader(pathName);
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);
            CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);

            // Create the tree to put the information in
            treeModel_ = (DefaultTreeModel) StyleBrowserTreeBuilder.createStyleSheetTree(stylesheet);

            tree_.setModel(treeModel_);

            frame_.setTitle("Style Browser - " + pathName);
        } catch (CSSException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "CSS Exception",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "I/O Exception",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            frame_.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void openStyleSheet() {
        JFileChooser chooser = new JFileChooser();

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getPath().endsWith(".css");
            }

            @Override
            public String getDescription() {
                return "Cascading Style Sheets";
            }
        };
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(frame_) == JFileChooser.APPROVE_OPTION) {
            loadStyleSheet(chooser.getSelectedFile().getPath());
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                null,
                "DOM CSS Style Browser\n"
                + "Copyright (C) 1999, 2014 David Schweinsberg\n"
                + "http://cssparser.sourceforge.net/",
                "About Style Browser",
                JOptionPane.INFORMATION_MESSAGE);
    }

    static public void main(String[] args) {
        new StyleBrowser();
    }
}
