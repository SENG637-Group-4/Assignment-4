package org.jfree.data;

import static org.junit.Assert.*;
import org.jfree.data.KeyedValues;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class GetCumulativePercentagesTest {

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
    
    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}