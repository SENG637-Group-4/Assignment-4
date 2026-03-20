package org.jfree.data;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.junit.*;

// Tests for DataUtilities.createNumberArray(double[]).
// No mocking needed — accepts a plain double[], not an interface.
// EC: length (E1 >1, E2 =1, E3 empty, U1 null), values (E4 pos, E5 neg, E6 mixed, U2 NaN, U3 Inf, U4 extremes).
public class DataUtilitiesCreateNumberArrayTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    // E1, E4 NOM: standard positive array
    @Test
    public void createNumberArrayWithPositiveValues() {
        double[] input = {1.0, 2.5, 3.0};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertNotNull("result[1] should not be null", result[1]);
        assertNotNull("result[2] should not be null", result[2]);
        assertEquals(1.0, result[0].doubleValue(), .000000001d);
        assertEquals(2.5, result[1].doubleValue(), .000000001d);
        assertEquals(3.0, result[2].doubleValue(), .000000001d);
    }

    // E1, E5 NOM: standard negative array
    @Test
    public void createNumberArrayWithNegativeValues() {
        double[] input = {-1.0, -2.5, -3.0};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertNotNull("result[1] should not be null", result[1]);
        assertNotNull("result[2] should not be null", result[2]);
        assertEquals(-1.0, result[0].doubleValue(), .000000001d);
        assertEquals(-2.5, result[1].doubleValue(), .000000001d);
        assertEquals(-3.0, result[2].doubleValue(), .000000001d);
    }

    // E1, E6 NOM: mixed values including zero
    @Test
    public void createNumberArrayWithMixedValuesIncludingZero() {
        double[] input = {-1.0, 0.0, 3.0};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertNotNull("result[1] should not be null", result[1]);
        assertNotNull("result[2] should not be null", result[2]);
        assertEquals(-1.0, result[0].doubleValue(), .000000001d);
        assertEquals( 0.0, result[1].doubleValue(), .000000001d);
        assertEquals( 3.0, result[2].doubleValue(), .000000001d);
    }

    // E3 LB: empty array (length = 0)
    @Test
    public void createNumberArrayWithEmptyArray() {
        double[] input = {};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result should not be null for empty input", result);
        assertEquals("Empty input should produce empty output", 0, result.length);
    }

    // E2, E4 ALB: single-element array (length = 1)
    @Test
    public void createNumberArrayWithSingleElement() {
        double[] input = {5.0};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertEquals("Output length should be 1", 1, result.length);
        assertNotNull("result[0] should not be null", result[0]);
        assertEquals(5.0, result[0].doubleValue(), .000000001d);
    }

    // U1: null — must throw InvalidParameterException
    @Test(expected = IllegalArgumentException.class)
    public void createNumberArrayWithNullShouldThrowException() {
        DataUtilities.createNumberArray(null);
    }

    // E2, U2: NaN element — use Double.isNaN() since assertEquals(NaN, ...) always fails in IEEE 754
    @Test
    public void createNumberArrayWithNaNValue() {
        double[] input = {Double.NaN};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertTrue("result[0] should be NaN", Double.isNaN(result[0].doubleValue()));
    }

    // E2, U3: POSITIVE_INFINITY — use Double.isInfinite() since Inf - Inf = NaN in IEEE 754
    @Test
    public void createNumberArrayWithPositiveInfinity() {
        double[] input = {Double.POSITIVE_INFINITY};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertTrue("result[0] should be positive infinity",
                Double.isInfinite(result[0].doubleValue()) && result[0].doubleValue() > 0);
    }

    // E1, U4: MAX_VALUE and MIN_VALUE (UB and LB for element values)
    @Test
    public void createNumberArrayWithMaxAndMinDoubleValues() {
        double[] input = {Double.MAX_VALUE, Double.MIN_VALUE};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull("Result array should not be null", result);
        assertNotNull("result[0] should not be null", result[0]);
        assertNotNull("result[1] should not be null", result[1]);
        assertEquals(Double.MAX_VALUE, result[0].doubleValue(), .000000001d);
        assertEquals(Double.MIN_VALUE, result[1].doubleValue(), .000000001d);
    }

    // E1, E4: output length must equal input length
    @Test
    public void createNumberArrayOutputLengthMatchesInputLength() {
        double[] input = {1.0, 2.5, 3.0};
        Number[] result = DataUtilities.createNumberArray(input);
        assertEquals("Output length must match input length", input.length, result.length);
    }

    // E1, E4: every element must be a Double instance
    @Test
    public void createNumberArrayElementsAreInstancesOfDouble() {
        double[] input = {1.0, 2.5, 3.0};
        Number[] result = DataUtilities.createNumberArray(input);
        for (int i = 0; i < result.length; i++) {
            assertNotNull("result[" + i + "] should not be null", result[i]);
            assertTrue("result[" + i + "] should be a Double", result[i] instanceof Double);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
