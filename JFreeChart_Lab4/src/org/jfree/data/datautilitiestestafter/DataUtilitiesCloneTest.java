package org.jfree.data.datautilitiestestafter;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.junit.Test;

public class DataUtilitiesCloneTest {

    /**
     * Normal case: 2x2 array
     */
    @Test
    public void testCloneNormal() {
        double[][] source = {{1.0, 2.0}, {3.0, 4.0}};

        double[][] result = DataUtilities.clone(source);

        assertNotSame(source, result);
        assertArrayEquals(source[0], result[0], 0.0000001);
        assertArrayEquals(source[1], result[1], 0.0000001);
    }

    /**
     * Source contains a null row → should skip that row
     */
    @Test
    public void testCloneWithNullRow() {
        double[][] source = {{1.0, 2.0}, null, {3.0}};

        double[][] result = DataUtilities.clone(source);

        assertNotSame(source, result);
        assertArrayEquals(source[0], result[0], 0.0000001);
        assertNull(result[1]); // null row preserved
        assertArrayEquals(source[2], result[2], 0.0000001);
    }

    /**
     * Source is null → exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullSource() {
        DataUtilities.clone(null);
    }

    /**
     * Empty array → returns empty clone
     */
    @Test
    public void testCloneEmptyArray() {
        double[][] source = new double[0][];

        double[][] result = DataUtilities.clone(source);

        assertEquals(0, result.length);
    }
}
