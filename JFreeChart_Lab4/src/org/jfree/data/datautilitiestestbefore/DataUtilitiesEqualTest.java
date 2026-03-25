package org.jfree.data.datautilitiestestbefore;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.junit.Test;
import java.util.Arrays;

public class DataUtilitiesEqualTest {

    /**
     * Both arrays null → should return true
     */
    @Test
    public void testBothNull() {
        double[][] a = null;
        double[][] b = null;

        assertTrue(DataUtilities.equal(a, b));
    }

    /**
     * a null, b non-null → false
     */
    @Test
    public void testANullBNotNull() {
        double[][] a = null;
        double[][] b = {{1.0, 2.0}};

        assertFalse(DataUtilities.equal(a, b));
    }

    /**
     * b null, a non-null → false
     */
    @Test
    public void testBNullANotNull() {
        double[][] a = {{1.0}};
        double[][] b = null;

        assertFalse(DataUtilities.equal(a, b));
    }

    /**
     * Different lengths → false
     */
    @Test
    public void testDifferentLengths() {
        double[][] a = {{1.0, 2.0}};
        double[][] b = {{1.0, 2.0}, {3.0}};

        assertFalse(DataUtilities.equal(a, b));
    }

    /**
     * One row different → false
     */
    @Test
    public void testRowDifferent() {
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] b = {{1.0, 2.0}, {4.0, 3.0}};

        assertFalse(DataUtilities.equal(a, b));
    }

    /**
     * All rows same → true
     */
    @Test
    public void testAllRowsSame() {
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] b = {{1.0, 2.0}, {3.0, 4.0}};

        assertTrue(DataUtilities.equal(a, b));
    }

    /**
     * Includes a null row → arrays equal if both null at same index
     */
    @Test
    public void testNullRows() {
        double[][] a = {{1.0}, null};
        double[][] b = {{1.0}, null};

        assertTrue(DataUtilities.equal(a, b));
    }

    /**
     * Null in one array only → false
     */
    @Test
    public void testOneRowNullOnly() {
        double[][] a = {{1.0}, null};
        double[][] b = {{1.0}, {2.0}};

        assertFalse(DataUtilities.equal(a, b));
    }
}
