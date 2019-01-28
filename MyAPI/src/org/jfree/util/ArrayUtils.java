/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 * ---------------
 * ArrayUtils.java
 * ---------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ArrayUtils.java,v 1.6 2004/04/26 19:15:45 taqua Exp $
 *
 * Changes
 * -------
 * 21-Aug-2003 : Version 1 (DG);
 *
 */

package org.jfree.util;

import java.util.Arrays;

/**
 * Utility methods for working with arrays.
 * 
 */
public abstract class ArrayUtils {
    
    /**
     * Clones a two dimensional array of floats.
     * 
     * @param array  the array.
     * 
     * @return A clone of the array.
     */
    public static float[][] clone(final float[][] array) {
    
        if (array == null) {
            return null;
        }
        final float[][] result = new float[array.length][];
        System.arraycopy(array, 0, result, 0, array.length);

        for (int i = 0; i < array.length; i++) {
            final float[] child = array[i];
            final float[] copychild = new float[child.length];
            System.arraycopy(child, 0, copychild, 0, child.length);
            result[i] = copychild;
        }

        return result;
    
    }
    
    /**
     * Tests two float arrays for equality.
     * 
     * @param array1  the first array.
     * @param array2  the second arrray.
     * 
     * @return A boolean.
     */
    public static boolean equal(final float[][] array1, final float[][] array2) {
        if (array1 == null) {
            return (array2 == null);
        }

        if (array2 == null) {
            return false;
        }

        if (array1.length != array2.length) {
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            if (!Arrays.equals(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }
    
}
