package org.jfree.data.datautilitiestest;

import static org.junit.Assert.*;
import org.jfree.data.KeyedValues;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DataUtilitiesGetCumulativePercentagesTest {

    private static final double DELTA = 1e-9;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

     
    // TC1: Multiple entries, all positive, standard integer keys
    // Input:  {0→5, 1→9, 2→2}
    // Expect: {0→0.3125, 1→0.875, 2→1.0}
    @Test
    public void testTC1_multiplePositiveEntries() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), 5.0);
        data.addValue(Integer.valueOf(1), 9.0);
        data.addValue(Integer.valueOf(2), 2.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 3", 3, result.getItemCount());

        assertEquals("Key 0 cumulative %", 0.3125, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
        assertEquals("Key 1 cumulative %", 0.875,  result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
        assertEquals("Key 2 cumulative %", 1.0,    result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);

        assertEquals("Key at index 0", Integer.valueOf(0), result.getKey(0));
        assertEquals("Key at index 1", Integer.valueOf(1), result.getKey(1));
        assertEquals("Key at index 2", Integer.valueOf(2), result.getKey(2));

        assertNotNull("Value at index 0 should not be null", result.getValue(0));
        assertTrue("Value at index 0 should be a Number", result.getValue(0) instanceof Number);
    }

    // TC2: Single entry
    // Input:  {0→10}
    // Expect: {0→1.0}
    @Test
    public void testTC2_singleEntry() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), 10.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 1", 1, result.getItemCount());
        assertEquals("Single entry cumulative % should be 1.0",
                1.0, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);

        assertEquals("Key at index 0", Integer.valueOf(0), result.getKey(0));
        assertTrue("Value should be a Number", result.getValue(0) instanceof Number);
    }

    
    // TC3: Empty KeyedValues
    // Input:  {}
    // Expect: empty result, itemCount = 0
    @Test
    public void testTC3_emptyKeyedValues() {
        DefaultKeyedValues data = new DefaultKeyedValues();

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull("Result should not be null", result);
        assertEquals("Item count should be 0", 0, result.getItemCount());
    }

    // TC4: null input
    // Expect: InvalidParameterException
    @Test(expected = IllegalArgumentException.class)
    public void testTC4_nullInput_throwsInvalidParameterException() {
        DataUtilities.getCumulativePercentages(null);
    }
    
     
    // TC5: Mixed positive and negative values
    // Input:  {0→5, 1→-2, 2→3}  total = 6
    // Expect: {0→5/6≈0.8333, 1→3/6=0.5, 2→6/6=1.0}    
    @Test
    public void testTC5_mixedPositiveAndNegativeValues() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0),  5.0);
        data.addValue(Integer.valueOf(1), -2.0);
        data.addValue(Integer.valueOf(2),  3.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 3", 3, result.getItemCount());
        assertEquals("Key 0 cumulative %", 5.0 / 6.0, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
        assertEquals("Key 1 cumulative %", 3.0 / 6.0, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
        assertEquals("Key 2 cumulative %", 1.0,        result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
    }

     
    // TC6: All values equal
    // Input:  {0→4, 1→4, 2→4}  total = 12
    // Expect: {0→0.3333…, 1→0.6666…, 2→1.0}     
    @Test
    public void testTC6_allValuesEqual() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), 4.0);
        data.addValue(Integer.valueOf(1), 4.0);
        data.addValue(Integer.valueOf(2), 4.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 3", 3, result.getItemCount());
        assertEquals("Key 0 cumulative %", 1.0 / 3.0, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
        assertEquals("Key 1 cumulative %", 2.0 / 3.0, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
        assertEquals("Key 2 cumulative %", 1.0,        result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
    }

     
    // TC7: One value is zero
    // Input:  {0→5, 1→0, 2→3}  total = 8
    // Expect: {0→0.625, 1→0.625, 2→1.0}    
    @Test
    public void testTC7_oneValueIsZero() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), 5.0);
        data.addValue(Integer.valueOf(1), 0.0);
        data.addValue(Integer.valueOf(2), 3.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 3", 3, result.getItemCount());
        assertEquals("Key 0 cumulative %", 0.625, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
        assertEquals("Key 1 cumulative %", 0.625, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
        assertEquals("Key 2 cumulative %", 1.0,   result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
    }

     
    // TC8: All values are zero — division-by-zero risk
    // Input:  {0→0, 1→0, 2→0}  total = 0
    // Expect: all returned values are NaN  (0.0 / 0.0 = NaN per IEEE 754)     
    @Test
    public void testTC8_allValuesZero_expectNaN() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), 0.0);
        data.addValue(Integer.valueOf(1), 0.0);
        data.addValue(Integer.valueOf(2), 0.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 3", 3, result.getItemCount());
        assertTrue("Key 0 should be NaN", Double.isNaN(result.getValue(Integer.valueOf(0)).doubleValue()));
        assertTrue("Key 1 should be NaN", Double.isNaN(result.getValue(Integer.valueOf(1)).doubleValue()));
        assertTrue("Key 2 should be NaN", Double.isNaN(result.getValue(Integer.valueOf(2)).doubleValue()));
    }

     
    // TC9: Non-integer (String) keys
    // Input:  {"a"→6, "b"→4}  total = 10
    // Expect: {"a"→0.6, "b"→1.0}; keys preserved     
    @Test
    public void testTC9_stringKeys() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue("a", 6.0);
        data.addValue("b", 4.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 2", 2, result.getItemCount());
        assertEquals("Key 'a' cumulative %", 0.6, result.getValue("a").doubleValue(), DELTA);
        assertEquals("Key 'b' cumulative %", 1.0, result.getValue("b").doubleValue(), DELTA);

        assertEquals("Key at index 0 should be 'a'", "a", result.getKey(0));
        assertEquals("Key at index 1 should be 'b'", "b", result.getKey(1));

        assertTrue("Value at 'a' should be a Number", result.getValue("a") instanceof Number);
        assertTrue("Value at 'b' should be a Number", result.getValue("b") instanceof Number);
    }

     
    // TC10: null value within the dataset
//    @Test(expected = IllegalArgumentException.class)
//    public void testTC10_nullValueInDataset_throwsNPE() {
//        DefaultKeyedValues data = new DefaultKeyedValues();
//        data.addValue(Integer.valueOf(0), null);
//        data.addValue(Integer.valueOf(1), 4.0);
//
//        DataUtilities.getCumulativePercentages(data);
//    }

     
    // TC11: NaN value — IEEE 754 propagation
    // Input:  {0→NaN, 1→5}
    // Expect: all cumulative values are NaN (observe and document)
    @Test
    public void testTC11_nanValue_propagates() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), Double.NaN);
        data.addValue(Integer.valueOf(1), 5.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 2", 2, result.getItemCount());

        assertTrue("Key 0 should be NaN", Double.isNaN(result.getValue(Integer.valueOf(0)).doubleValue()));
        assertTrue("Key 1 should be NaN", Double.isNaN(result.getValue(Integer.valueOf(1)).doubleValue()));
    }

     
    // TC12: Positive Infinity value — IEEE 754 behaviour
    // Input:  {0→Infinity, 1→5}
    // Expect: total = Infinity; first = NaN (Inf/Inf); propagation follows IEEE    
    @Test
    public void testTC12_infinityValue_ieeeRules() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), Double.POSITIVE_INFINITY);
        data.addValue(Integer.valueOf(1), 5.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 2", 2, result.getItemCount());

        double v0 = result.getValue(Integer.valueOf(0)).doubleValue();
        double v1 = result.getValue(Integer.valueOf(1)).doubleValue();

        assertTrue("Key 0 should be NaN (Inf/Inf)", Double.isNaN(v0));

        assertTrue("Key 1 should be NaN", Double.isNaN(v1));
    }

     
    // TC13: Double.MAX_VALUE — floating-point precision dominance
    // Input:  {0→Double.MAX_VALUE, 1→1.0}
    // Expect: first ≈ 1.0 (MAX_VALUE dominates the total); second = 1.0     
    @Test
    public void testTC13_doubleMaxValue_precisionDominance() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue(Integer.valueOf(0), Double.MAX_VALUE);
        data.addValue(Integer.valueOf(1), 1.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Item count should be 2", 2, result.getItemCount());

        double v0 = result.getValue(Integer.valueOf(0)).doubleValue();
        double v1 = result.getValue(Integer.valueOf(1)).doubleValue();

        assertEquals("Key 0 cumulative % should be ≈ 1.0", 1.0, v0, 1e-10);
        assertEquals("Key 1 cumulative % should be 1.0",   1.0, v1, DELTA);
    }
    
	 // TC14: Dataset containing a null value that should be skipped in total calculation
	 // Input: {0→5, 1→null, 2→5}
	 // Expected behaviour: total = 10 (null skipped)
	 // cumulative: {0→0.5, 1→0.5, 2→1.0}
	 @Test
	 public void testTC14_nullValueSkippedInTotal() {
	
	     DefaultKeyedValues data = new DefaultKeyedValues();
	     data.addValue(Integer.valueOf(0), 5.0);
	     data.addValue(Integer.valueOf(1), null);   // triggers v == null branch
	     data.addValue(Integer.valueOf(2), 5.0);
	
	     KeyedValues result = DataUtilities.getCumulativePercentages(data);
	
	     assertEquals(3, result.getItemCount());
	
	     assertEquals(0.5, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	
	     // cumulative should not change because value is null
	     assertEquals(0.5, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
	
	     assertEquals(1.0, result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
	 }
	 
	// TC15: Verify loop executes exactly itemCount times by checking all intermediate
	// cumulative values precisely — kills "Less than to not equal" survivors on lines 284 & 297
	// and post-increment/decrement survivors on lines 287 & 300.
	// Input: {0→1, 1→2, 2→3, 3→4}  total = 10
	// Expect: {0→0.1, 1→0.3, 2→0.6, 3→1.0}
	@Test
	public void testTC15_allIntermediateCumulativeValues_preciseCheck() {
	    DefaultKeyedValues data = new DefaultKeyedValues();
	    data.addValue(Integer.valueOf(0), 1.0);
	    data.addValue(Integer.valueOf(1), 2.0);
	    data.addValue(Integer.valueOf(2), 3.0);
	    data.addValue(Integer.valueOf(3), 4.0);

	    KeyedValues result = DataUtilities.getCumulativePercentages(data);

	    assertEquals("Item count should be 4", 4, result.getItemCount());

	    // Each intermediate value must be exact — post-increment/decrement mutations
	    // on total or runningTotal would shift these values and be caught here
	    assertEquals("Key 0 cumulative %", 0.1, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	    assertEquals("Key 1 cumulative %", 0.3, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
	    assertEquals("Key 2 cumulative %", 0.6, result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
	    assertEquals("Key 3 cumulative %", 1.0, result.getValue(Integer.valueOf(3)).doubleValue(), DELTA);
	}

	// TC16: Two-element dataset with unequal values — verifies the first cumulative entry
	// is strictly between 0 and 1, killing "Less than to not equal" survivors on lines 284 & 297.
	// If the loop ran 0 times (i != count mutant skipping when i==0 immediately), the
	// result would be empty or have wrong values.
	// Input: {0→3, 1→7}  total = 10
	// Expect: {0→0.3, 1→1.0}
	@Test
	public void testTC16_twoElements_firstEntryStrictlyIntermediate() {
	    DefaultKeyedValues data = new DefaultKeyedValues();
	    data.addValue(Integer.valueOf(0), 3.0);
	    data.addValue(Integer.valueOf(1), 7.0);

	    KeyedValues result = DataUtilities.getCumulativePercentages(data);

	    assertEquals("Item count should be 2", 2, result.getItemCount());
	    assertEquals("Key 0 cumulative %", 0.3, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	    assertEquals("Key 1 cumulative %", 1.0, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);

	    // Confirm first value is strictly between 0 and 1 — if loop body was skipped,
	    // runningTotal/total would be 0.0/10.0 = 0.0, not 0.3
	    assertTrue("First cumulative % must be > 0.0",
	            result.getValue(Integer.valueOf(0)).doubleValue() > 0.0);
	    assertTrue("First cumulative % must be < 1.0",
	            result.getValue(Integer.valueOf(0)).doubleValue() < 1.0);
	}

	// TC17: Validates total accumulation is exact by using values whose sum is sensitive
	// to post-increment/decrement mutations on the accumulator variable (line 287 survivors).
	// A mutant applying a++ or a-- to the running total BEFORE the division would produce
	// a value off by 1 unit, which differs detectably when total is small.
	// Input: {0→1, 1→1}  total = 2
	// Expect: {0→0.5, 1→1.0}
	// If runningTotal were post-incremented (a++) after addition, slot 0 would yield
	// 1.0/2.0=0.5 still — but if the accumulator itself is mutated the total changes,
	// making the first result != 0.5.
	@Test
	public void testTC17_smallTotalSensitiveToAccumulatorMutation() {
	    DefaultKeyedValues data = new DefaultKeyedValues();
	    data.addValue(Integer.valueOf(0), 1.0);
	    data.addValue(Integer.valueOf(1), 1.0);

	    KeyedValues result = DataUtilities.getCumulativePercentages(data);

	    assertEquals("Item count should be 2", 2, result.getItemCount());
	    assertEquals("Key 0 cumulative % must be exactly 0.5",
	            0.5, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	    assertEquals("Key 1 cumulative % must be exactly 1.0",
	            1.0, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);

	    // Explicit delta-tight assertion — any off-by-one in accumulator is caught
	    assertTrue("Key 0 must not be > 0.5",
	            result.getValue(Integer.valueOf(0)).doubleValue() <= 0.5 + DELTA);
	    assertTrue("Key 0 must not be < 0.5",
	            result.getValue(Integer.valueOf(0)).doubleValue() >= 0.5 - DELTA);
	}

	// TC18: Five-element dataset — ensures the second loop's dead condition (i2 > count)
	// kills surviving mutations on line 290 by providing enough items that any accidental
	// execution of the second loop would double-count the total, halving all results.
	// Input: {0→2, 1→2, 2→2, 3→2, 4→2}  total = 10
	// Expect: {0→0.2, 1→0.4, 2→0.6, 3→0.8, 4→1.0}
	// If the dead second loop ran (doubling total to 20), all values would be halved.
	@Test
	public void testTC18_fiveEqualElements_secondLoopMustNotExecute() {
	    DefaultKeyedValues data = new DefaultKeyedValues();
	    data.addValue(Integer.valueOf(0), 2.0);
	    data.addValue(Integer.valueOf(1), 2.0);
	    data.addValue(Integer.valueOf(2), 2.0);
	    data.addValue(Integer.valueOf(3), 2.0);
	    data.addValue(Integer.valueOf(4), 2.0);

	    KeyedValues result = DataUtilities.getCumulativePercentages(data);

	    assertEquals("Item count should be 5", 5, result.getItemCount());

	    // If the dead second loop ever ran, total = 20 and these would all be halved
	    assertEquals("Key 0 cumulative %", 0.2, result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	    assertEquals("Key 1 cumulative %", 0.4, result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
	    assertEquals("Key 2 cumulative %", 0.6, result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);
	    assertEquals("Key 3 cumulative %", 0.8, result.getValue(Integer.valueOf(3)).doubleValue(), DELTA);
	    assertEquals("Key 4 cumulative %", 1.0, result.getValue(Integer.valueOf(4)).doubleValue(), DELTA);
	}

	// TC19: Asymmetric values with a strictly non-trivial intermediate — designed to
	// detect post-increment/decrement survivors on runningTotal (line 300, mutations 10 & 11).
	// The intermediate at index 1 is 0.25, which would shift to an incorrect value if
	// runningTotal were incremented or decremented by 1 after the addition step.
	// Input: {0→1, 1→3, 2→12}  total = 16
	// Expect: {0→0.0625, 1→0.25, 2→1.0}
	@Test
	public void testTC19_runningTotalPostMutation_intermediateDetection() {
	    DefaultKeyedValues data = new DefaultKeyedValues();
	    data.addValue(Integer.valueOf(0), 1.0);
	    data.addValue(Integer.valueOf(1), 3.0);
	    data.addValue(Integer.valueOf(2), 12.0);

	    KeyedValues result = DataUtilities.getCumulativePercentages(data);

	    assertEquals("Item count should be 3", 3, result.getItemCount());

	    assertEquals("Key 0 cumulative %", 1.0 / 16.0,
	            result.getValue(Integer.valueOf(0)).doubleValue(), DELTA);
	    assertEquals("Key 1 cumulative %", 4.0 / 16.0,
	            result.getValue(Integer.valueOf(1)).doubleValue(), DELTA);
	    assertEquals("Key 2 cumulative %", 1.0,
	            result.getValue(Integer.valueOf(2)).doubleValue(), DELTA);

	    // Tight range check on the intermediate — a mutated runningTotal of 5.0 instead
	    // of 4.0 would yield 5/16 = 0.3125, which fails the 0.25 assertion above
	    double v1 = result.getValue(Integer.valueOf(1)).doubleValue();
	    assertTrue("Key 1 must be strictly less than 0.3", v1 < 0.3);
	    assertTrue("Key 1 must be strictly greater than 0.2", v1 > 0.2);
	}
    
    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}