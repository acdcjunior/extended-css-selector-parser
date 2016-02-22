# Extended CSS Selectors parser used by seleniumQuery

This repo is a fork of [CSS Parser](http://cssparser.sourceforge.net/).

Differences:

- CSS Parser fully parses CSS stylesheets and declarations, including selectors. We only need to parse selectors, so we removed the other parts.
- CSS Parser supports several versions of CSS Selectors. We only use CSS3, so we also removed the other versions.
- Most importantly, though: We use an extended version of CSS3 Selectors, with a modified grammar, so we changed the original CSS3 grammar used by CSS Parser a bit.

