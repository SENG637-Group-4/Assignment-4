package org.jfree.data.datautilitiestestbefore;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;

import org.jfree.data.DataUtilities;
import org.junit.*;

// Tests for DataUtilities.createNumberArray2D(double[][]).
// No mocking needed — accepts a plain double[][], not an interface.
// EC: outer length (E1 >1, E2 =1, E3 empty, U1 null), inner length (E4 rect, E5 jagged, E6 col=1, E7 col=0), values (E8, U2, U3).
public class DataUtilitiesCreateNumberArray2DTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // E1, E4, E8 NOM: rectangular 2x2 array
    @Test
    public void createNumberArray2DWithRectangularArray() {
        double[][] input = {{1.0, 2.0}, {3.0, 4.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[0][1] should not be null", result[0][1]);
        assertNotNull("result[1][0] should not be null", result[1][0]);
        assertNotNull("result[1][1] should not be null", result[1][1]);
        assertEquals(1.0, result[0][0].doubleValue(), .000000001d);
        assertEquals(2.0, result[0][1].doubleValue(), .000000001d);
        assertEquals(3.0, result[1][0].doubleValue(), .000000001d);
        assertEquals(4.0, result[1][1].doubleValue(), .000000001d);
    }

    // E1, E5: jagged array (unequal row lengths)
    @Test
    public void createNumberArray2DWithJaggedArray() {
        double[][] input = {{1.0}, {2.0, 3.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertEquals("Row 0 should have length 1", 1, result[0].length);
        assertEquals("Row 1 should have length 2", 2, result[1].length);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[1][0] should not be null", result[1][0]);
        assertNotNull("result[1][1] should not be null", result[1][1]);
        assertEquals(1.0, result[0][0].doubleValue(), .000000001d);
        assertEquals(2.0, result[1][0].doubleValue(), .000000001d);
        assertEquals(3.0, result[1][1].doubleValue(), .000000001d);
    }

    // E2, E4 ALB outer=1: single row with 3 elements
    @Test
    public void createNumberArray2DWithSingleRow() {
        double[][] input = {{1.0, 2.0, 3.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 row", 1, result.length);
        assertEquals("Row should have 3 elements", 3, result[0].length);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[0][2] should not be null", result[0][2]);
        assertEquals(1.0, result[0][0].doubleValue(), .000000001d);
        assertEquals(3.0, result[0][2].doubleValue(), .000000001d);
    }

    // E1, E6 ALB inner=1: single-column array (3 rows, 1 col each)
    @Test
    public void createNumberArray2DWithSingleColumn() {
        double[][] input = {{1.0}, {2.0}, {3.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 3 rows", 3, result.length);
        assertEquals("Each row should have 1 element", 1, result[0].length);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[2][0] should not be null", result[2][0]);
        assertEquals(1.0, result[0][0].doubleValue(), .000000001d);
        assertEquals(3.0, result[2][0].doubleValue(), .000000001d);
    }

    // E3 LB outer=0: empty outer array
    @Test
    public void createNumberArray2DWithEmptyOuterArray() {
        double[][] input = {};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null for empty input", result);
        assertEquals("Empty input should give length 0", 0, result.length);
    }

    // E2, E7 LB inner=0: single empty row
    @Test
    public void createNumberArray2DWithEmptyInnerRow() {
        double[][] input = {{}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 row", 1, result.length);
        assertEquals("That row should have length 0", 0, result[0].length);
    }

    // U1: null — must throw InvalidParameterException
    @Test(expected = IllegalArgumentException.class)
    public void createNumberArray2DWithNullShouldThrowException() {
        DataUtilities.createNumberArray2D(null);
    }

    // E1, E4, U2: NaN and POSITIVE_INFINITY elements
    // Use isNaN()/isInfinite() — assertEquals with NaN or Inf always fails due to IEEE 754
    @Test
    public void createNumberArray2DWithNaNAndInfinityValues() {
        double[][] input = {{Double.NaN, 1.0}, {2.0, Double.POSITIVE_INFINITY}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[1][1] should not be null", result[1][1]);
        assertTrue("result[0][0] should be NaN",
                Double.isNaN(result[0][0].doubleValue()));
        assertTrue("result[1][1] should be positive infinity",
                Double.isInfinite(result[1][1].doubleValue()) && result[1][1].doubleValue() > 0);
    }

    // E1, E6, U3: MAX_VALUE and MIN_VALUE (UB and LB for element values)
    @Test
    public void createNumberArray2DWithMaxAndMinDoubleValues() {
        double[][] input = {{Double.MAX_VALUE}, {Double.MIN_VALUE}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull("Result should not be null", result);
        assertNotNull("result[0][0] should not be null", result[0][0]);
        assertNotNull("result[1][0] should not be null", result[1][0]);
        assertEquals(Double.MAX_VALUE, result[0][0].doubleValue(), .000000001d);
        assertEquals(Double.MIN_VALUE, result[1][0].doubleValue(), .000000001d);
    }

    // E1, E4: outer length must match input
    @Test
    public void createNumberArray2DOuterLengthMatchesInput() {
        double[][] input = {{1.0, 2.0}, {3.0, 4.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertEquals("Outer length should match input", input.length, result.length);
    }

    // E1, E4: each inner row length must match input
    @Test
    public void createNumberArray2DInnerLengthMatchesInput() {
        double[][] input = {{1.0, 2.0}, {3.0, 4.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertEquals("Row 0 length should match", input[0].length, result[0].length);
        assertEquals("Row 1 length should match", input[1].length, result[1].length);
    }

    // E1, E4, E8: all elements must be Double instances
    @Test
    public void createNumberArray2DElementsAreInstancesOfDouble() {
        double[][] input = {{1.0, 2.0}, {3.0, 4.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertNotNull("result[" + i + "][" + j + "] should not be null", result[i][j]);
                assertTrue("result[" + i + "][" + j + "] should be a Double",
                        result[i][j] instanceof Double);
            }
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
