/*
 * (c) COPYRIGHT 1999 World Wide Web Consortium
 * (Massachusetts Institute of Technology, Institut National de Recherche
 *  en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
 *
 * $Id: NegativeSelector.java,v 1.1.1.1 2003-12-28 21:23:51 davidsch Exp $
 */
package org.w3c.css.sac;

/**
 * @version $Revision: 1.1.1.1 $
 * @author  Philippe Le Hegaret
 * @see Selector#SAC_NEGATIVE_SELECTOR
 */
public interface NegativeSelector extends SimpleSelector {

    /**
     * Returns the simple selector.
     */    
    public SimpleSelector getSimpleSelector();
}
