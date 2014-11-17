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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David
 * Schweinsberg</a>
 */
public class StyleBrowserCellRenderer extends JLabel implements TreeCellRenderer {

    private static ImageIcon pageIcon_;
    private static ImageIcon charsetIcon_;
    private static ImageIcon unknownIcon_;
    private static ImageIcon styleIcon_;
    private static ImageIcon importIcon_;
    private static ImageIcon fontFaceIcon_;
    private static ImageIcon styleSheetIcon_;
    private static ImageIcon listIcon_;
    private static ImageIcon styleDeclarationIcon_;
    private static ImageIcon mediaIcon_;

    static {
        try {
            pageIcon_ = createImageIcon("images/PageRule.jpg");
            charsetIcon_ = createImageIcon("images/CharsetRule.jpg");
            unknownIcon_ = createImageIcon("images/UnknownRule.jpg");
            styleIcon_ = createImageIcon("images/StyleRule.jpg");
            importIcon_ = createImageIcon("images/ImportRule.jpg");
            fontFaceIcon_ = createImageIcon("images/FontFaceRule.jpg");
            styleSheetIcon_ = createImageIcon("images/StyleSheet.jpg");
            listIcon_ = createImageIcon("images/List.jpg");
            styleDeclarationIcon_ = createImageIcon("images/StyleDeclaration.jpg");
            mediaIcon_ = createImageIcon("images/MediaRule.jpg");
        } catch (Exception e) {
            System.err.println("Couldn't load images: " + e);
        }
    }

    static ImageIcon createImageIcon(String path) throws FileNotFoundException {
        URL imgURL = StyleBrowserCellRenderer.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            throw new FileNotFoundException(path);
        }
    }

    private boolean _selected;

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        String str = tree.convertValueToText(
                value,
                selected,
                expanded,
                leaf,
                row,
                hasFocus);

        setText(str);
        setToolTipText(str);

        if (leaf) {
            setIcon(null);
        } else {

            // This is hardly efficient, but it'll do for now
            if (str.equals("CSSPageRule")) {
                setIcon(pageIcon_);
            } else if (str.equals("CSSCharsetRule")) {
                setIcon(charsetIcon_);
            } else if (str.equals("CSSUnknownRule")) {
                setIcon(unknownIcon_);
            } else if (str.equals("CSSStyleRule")) {
                setIcon(styleIcon_);
            } else if (str.equals("CSSImportRule")) {
                setIcon(importIcon_);
            } else if (str.equals("CSSFontFaceRule")) {
                setIcon(fontFaceIcon_);
            } else if (str.equals("CSSStyleSheet")) {
                setIcon(styleSheetIcon_);
            } else if (str.equals("MediaList") || str.equals("CSSRuleList")) {
                setIcon(listIcon_);
            } else if (str.equals("CSSStyleDeclaration")) {
                setIcon(styleDeclarationIcon_);
            } else if (str.equals("CSSMediaRule")) {
                setIcon(mediaIcon_);
            } else {
                setIcon(null);
            }
        }

        setForeground(selected ? SystemColor.textHighlightText : SystemColor.textText);

        _selected = selected;

        return this;
    }

    @Override
    public void paint(Graphics g) {
        if (_selected) {
            g.setColor(SystemColor.textHighlight);
        } else if (getParent() != null) {
            g.setColor(getParent().getBackground());
        } else {
            g.setColor(getBackground());
        }

        Icon icon = getIcon();
        int offset = 0;

        if (icon != null && getText() != null) {
            offset = icon.getIconWidth() + getIconTextGap();
        }

        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

        super.paint(g);
    }
}
