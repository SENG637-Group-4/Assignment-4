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
    
	 // =====================================================================
	 // NEW TESTS — clone(double[][])
	 // =====================================================================
	
	 // Kills line 104: Less than to not equal
	 // Two-row clone — both rows must be present and correct.
	 @Test
	 public void clone_twoRows_bothCopiedCorrectly() {
	     double[][] source = {{1.0, 2.0}, {3.0, 4.0}};
	     double[][] result = DataUtilities.clone(source);
	     assertNotNull(result);
	     assertEquals(2, result.length);
	     assertArrayEquals(source[0], result[0], 1e-9);
	     assertArrayEquals(source[1], result[1], 1e-9);
	 }
	
	 // Kills line 104: Substituted 0 with 1 on loop init — row 0 would be skipped
	 @Test
	 public void clone_firstRowHasCorrectValues() {
	     double[][] source = {{42.0, 43.0}, {1.0, 2.0}};
	     double[][] result = DataUtilities.clone(source);
	     assertEquals("Row 0 element 0 must be 42.0", 42.0, result[0][0], 1e-9);
	     assertEquals("Row 0 element 1 must be 43.0", 43.0, result[0][1], 1e-9);
	 }
	
	 // Kills line 104: Less than to not equal with 3-row array
	 @Test
	 public void clone_threeRows_allCopiedCorrectly() {
	     double[][] source = {{1.0}, {2.0}, {3.0}};
	     double[][] result = DataUtilities.clone(source);
	     assertEquals(3, result.length);
	     assertEquals(1.0, result[0][0], 1e-9);
	     assertEquals(2.0, result[1][0], 1e-9);
	     assertEquals(3.0, result[2][0], 1e-9);
	 }
	
	 // Confirms deep copy — mutating source must not affect clone
	 @Test
	 public void clone_isDeepCopy_sourceChangeDoesNotAffectClone() {
	     double[][] source = {{1.0, 2.0}, {3.0, 4.0}};
	     double[][] result = DataUtilities.clone(source);
	     source[0][0] = 99.0;
	     assertEquals("Clone must not reflect source mutation", 1.0, result[0][0], 1e-9);
	 }
}
