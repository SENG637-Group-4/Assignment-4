package org.jfree.data.datautilitiestestafter;

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
    
	 // =====================================================================
	 // NEW TESTS — equal(double[][], double[][])
	 // =====================================================================
	
	 // Kills line 76: Substituted 1 with -1
	 // The return (b == null) would evaluate as -1 (false) if mutated.
	 // We verify true is actually returned when both are null.
	 @Test
	 public void equal_bothNull_returnsTrue() {
	     assertTrue(DataUtilities.equal(null, null));
	 }
	
	 // Kills line 76: complementary branch — (b == null) → false
	 @Test
	 public void equal_aNullBNonNull_returnsFalse() {
	     assertFalse(DataUtilities.equal(null, new double[][]{{1.0}}));
	 }
	
	 // Kills line 81: equal to greater or equal
	 // Mutant replaces a.length != b.length with a.length >= b.length.
	 // When lengths are equal and content matches, must still return true.
	 @Test
	 public void equal_sameLengthMatchingContent_returnsTrue() {
	     double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
	     double[][] b = {{1.0, 2.0}, {3.0, 4.0}};
	     assertTrue(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 81: different outer lengths must return false
	 @Test
	 public void equal_differentOuterLengths_returnsFalse() {
	     double[][] a = {{1.0}};
	     double[][] b = {{1.0}, {2.0}};
	     assertFalse(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 84: Less than to not equal (loop init / boundary survivors)
	 // Difference is in row 0 — if loop started at i=1 it would be missed.
	 @Test
	 public void equal_differenceInFirstRow_returnsFalse() {
	     double[][] a = {{99.0, 2.0}, {3.0, 4.0}};
	     double[][] b = {{1.0,  2.0}, {3.0, 4.0}};
	     assertFalse(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 84: Substituted 0 with 1 survivors + Less than to not equal
	 // Three rows — exact iteration count must be 3.
	 @Test
	 public void equal_threeIdenticalRows_returnsTrue() {
	     double[][] a = {{1.0}, {2.0}, {3.0}};
	     double[][] b = {{1.0}, {2.0}, {3.0}};
	     assertTrue(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 85: not equal to greater than
	 // Row 0 matches, row 1 differs. The mutant would not detect this correctly.
	 @Test
	 public void equal_firstRowMatchSecondDiffers_returnsFalse() {
	     double[][] a = {{1.0}, {2.0}};
	     double[][] b = {{1.0}, {3.0}};
	     assertFalse(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 85: Incremented (++a) on loop variable
	 // All rows match — loop must complete without premature exit.
	 @Test
	 public void equal_threeRowsAllMatch_returnsTrue() {
	     double[][] a = {{1.0}, {2.0}, {3.0}};
	     double[][] b = {{1.0}, {2.0}, {3.0}};
	     assertTrue(DataUtilities.equal(a, b));
	 }
	
	 // Kills line 89: Substituted 1 with -1
	 // Final return true is mutated to -1 (false). Must confirm true is returned.
	 @Test
	 public void equal_identicalArrays_finalReturnIsTrue() {
	     double[][] a = {{5.0, 6.0}, {7.0, 8.0}};
	     double[][] b = {{5.0, 6.0}, {7.0, 8.0}};
	     assertTrue(DataUtilities.equal(a, b));
	 }
}
