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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;
import org.w3c.dom.stylesheets.MediaList;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David
 * Schweinsberg</a>
 */
class StyleBrowserTreeBuilder {

    private static DefaultMutableTreeNode createNode(String name) {
        return new DefaultMutableTreeNode(new StyleData(name));
    }

    private static void addStyleDeclaration(
            DefaultMutableTreeNode parent,
            CSSStyleDeclaration decl) {

        DefaultMutableTreeNode node = createNode("CSSStyleDeclaration");
        parent.add(node);

        // Add each style declaration
        for (int i = 0; i < decl.getLength(); i++) {
            String name = decl.item(i);
            String value = decl.getPropertyValue(name);
            String prio = decl.getPropertyPriority(name);

            if (prio.equals("")) {
                node.add(createNode(name + " : " + value));
            } else {
                node.add(createNode(name + " : " + value + " ! " + prio));
            }
        }
    }

    private static void addMediaList(
            DefaultMutableTreeNode parent,
            MediaList mediaList) {

        // WORKAROUND
        // CSSImportRule.media shouldn't ever be null, but is if the media list
        // is empty - this is a bug.
        if (mediaList == null) {
            return;
        }

        DefaultMutableTreeNode node = createNode("MediaList");
        parent.add(node);

        // Add each media type
        for (int i = 0; i < mediaList.getLength(); i++) {
            node.add(createNode(mediaList.item(i)));
        }
    }

    private static void addUnknownRule(
            DefaultMutableTreeNode parent,
            CSSUnknownRule rule) {

        DefaultMutableTreeNode node = createNode("CSSUnknownRule");
        parent.add(node);

        // Add the href
        node.add(createNode(rule.getCssText()));
    }

    private static void addStyleRule(
            DefaultMutableTreeNode parent,
            CSSStyleRule rule) {

        DefaultMutableTreeNode node = createNode("CSSStyleRule");
        parent.add(node);

        // Add the selector text
        node.add(createNode(rule.getSelectorText()));

        addStyleDeclaration(node, rule.getStyle());
    }

    private static void addCharsetRule(
            DefaultMutableTreeNode parent,
            CSSCharsetRule rule) {

        DefaultMutableTreeNode node = createNode("CSSCharsetRule");
        parent.add(node);

        // Add the encoding
        node.add(createNode(rule.getEncoding()));
    }

    private static void addImportRule(
            DefaultMutableTreeNode parent,
            CSSImportRule rule) {

        DefaultMutableTreeNode node = createNode("CSSImportRule");
        parent.add(node);

        // Add the href
        node.add(createNode(rule.getHref()));

        addMediaList(node, rule.getMedia());
    }

    private static void addMediaRule(
            DefaultMutableTreeNode parent,
            CSSMediaRule rule) {

        DefaultMutableTreeNode node = createNode("CSSMediaRule");
        parent.add(node);

        addMediaList(node, rule.getMedia());
        addRuleList(node, rule.getCssRules());
    }

    private static void addFontFaceRule(
            DefaultMutableTreeNode parent,
            CSSFontFaceRule rule) {

        DefaultMutableTreeNode node = createNode("CSSFontFaceRule");
        parent.add(node);

        addStyleDeclaration(node, rule.getStyle());
    }

    private static void addPageRule(
            DefaultMutableTreeNode parent,
            CSSPageRule rule) {

        DefaultMutableTreeNode node = createNode("CSSPageRule");
        parent.add(node);

        // Add the selector text
        node.add(createNode(rule.getSelectorText()));

        addStyleDeclaration(node, rule.getStyle());
    }

    private static void addRuleList(
            DefaultMutableTreeNode parent,
            CSSRuleList rules) {

        DefaultMutableTreeNode node = createNode("CSSRuleList");
        parent.add(node);

        for (int i = 0; i < rules.getLength(); i++) {
            CSSRule rule = rules.item(i);

            switch (rule.getType()) {
                case CSSRule.UNKNOWN_RULE:
                    addUnknownRule(node, (CSSUnknownRule) rule);
                    break;
                case CSSRule.STYLE_RULE:
                    addStyleRule(node, (CSSStyleRule) rule);
                    break;
                case CSSRule.CHARSET_RULE:
                    addCharsetRule(node, (CSSCharsetRule) rule);
                    break;
                case CSSRule.IMPORT_RULE:
                    addImportRule(node, (CSSImportRule) rule);
                    break;
                case CSSRule.MEDIA_RULE:
                    addMediaRule(node, (CSSMediaRule) rule);
                    break;
                case CSSRule.FONT_FACE_RULE:
                    addFontFaceRule(node, (CSSFontFaceRule) rule);
                    break;
                case CSSRule.PAGE_RULE:
                    addPageRule(node, (CSSPageRule) rule);
                    break;
            }
        }
    }

    public static TreeModel createStyleSheetTree(CSSStyleSheet stylesheet) {
        DefaultMutableTreeNode node = createNode("CSSStyleSheet");
        TreeModel treeModel = new DefaultTreeModel(node);

        addRuleList(node, stylesheet.getCssRules());

        return treeModel;
    }
}
