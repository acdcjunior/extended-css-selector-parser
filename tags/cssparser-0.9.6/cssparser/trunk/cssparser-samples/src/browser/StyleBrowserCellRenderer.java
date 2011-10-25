/*
 * $Id: StyleBrowserCellRenderer.java,v 1.1 2005-07-29 17:14:05 davidsch Exp $
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
import javax.swing.*;
import javax.swing.tree.*;

/**
 * @author <a href="mailto:davidsch@users.sourceforge.net">David Schweinsberg</a>
 * @version $Id: StyleBrowserCellRenderer.java,v 1.1 2005-07-29 17:14:05 davidsch Exp $
 */
public class StyleBrowserCellRenderer extends JLabel implements TreeCellRenderer {

  private static ImageIcon _pageIcon;
  private static ImageIcon _charsetIcon;
  private static ImageIcon _unknownIcon;
  private static ImageIcon _styleIcon;
  private static ImageIcon _importIcon;
  private static ImageIcon _fontFaceIcon;
  private static ImageIcon _styleSheetIcon;
  private static ImageIcon _listIcon;
  private static ImageIcon _styleDeclarationIcon;
  private static ImageIcon _mediaIcon;

  static {
    try {
      _pageIcon = new ImageIcon("images/PageRule.jpg");
      _charsetIcon = new ImageIcon("images/CharsetRule.jpg");
      _unknownIcon = new ImageIcon("images/UnknownRule.jpg");
      _styleIcon = new ImageIcon("images/StyleRule.jpg");
      _importIcon = new ImageIcon("images/ImportRule.jpg");
      _fontFaceIcon = new ImageIcon("images/FontFaceRule.jpg");
      _styleSheetIcon = new ImageIcon("images/StyleSheet.jpg");
      _listIcon = new ImageIcon("images/List.jpg");
      _styleDeclarationIcon = new ImageIcon("images/StyleDeclaration.jpg");
      _mediaIcon = new ImageIcon("images/MediaRule.jpg");
    }
    catch(Exception e) {
      System.out.println("Couldn't load images: " + e);
    }
  }

  private boolean _selected;

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

    if (leaf)
      setIcon(null);
    else {
//      StyleData sd =
//        (StyleData)((DefaultMutableTreeNode)value).getUserObject();

      // This is hardly efficient, but it'll do for now
      if (str.equals("CSSPageRule"))
        setIcon(_pageIcon);
      else if (str.equals("CSSCharsetRule"))
        setIcon(_charsetIcon);
      else if (str.equals("CSSUnknownRule"))
        setIcon(_unknownIcon);
      else if (str.equals("CSSStyleRule"))
        setIcon(_styleIcon);
      else if (str.equals("CSSImportRule"))
        setIcon (_importIcon);
      else if (str.equals("CSSFontFaceRule"))
        setIcon(_fontFaceIcon);
      else if (str.equals("CSSStyleSheet"))
        setIcon(_styleSheetIcon);
      else if (str.equals("MediaList") || str.equals("CSSRuleList"))
        setIcon(_listIcon);
      else if (str.equals("CSSStyleDeclaration"))
        setIcon(_styleDeclarationIcon);
      else if (str.equals("CSSMediaRule"))
        setIcon(_mediaIcon);
      else
        setIcon(null);
    }

    setForeground(selected ? SystemColor.textHighlightText : SystemColor.textText);

    _selected = selected;

    return this;
  }

  public void paint(Graphics g) {
    if (_selected)
      g.setColor(SystemColor.textHighlight);
    else if (getParent() != null)
      g.setColor(getParent().getBackground());
    else
      g.setColor(getBackground());

    Icon icon = getIcon();
    int offset = 0;

    if (icon != null && getText() != null)
      offset = icon.getIconWidth() + getIconTextGap();

    g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

    super.paint(g);
  }
}
