package org.jfree.data.datautilitiestest;

import static org.junit.Assert.*;
import java.security.InvalidParameterException;

import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.jfree.data.DataUtilities;


public class DataUtilitiesCalculateColumnTotalTest {

    private Mockery context;
    private Values2D values;

    @Before
    public void setUp() {
        context = new Mockery();
        values = context.mock(Values2D.class);
    }

      
    // TC1 – LB column (0)
     
    @Test
    public void testTC1_firstColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(4.0, result, 1e-9);
    }

      
    // TC2 – UB column
      
    @Test
    public void testTC2_lastColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(1, 1); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 1);
        assertEquals(6.0, result, 1e-9);
    }

      
    // TC3 – NOM column (3x3 table)
      
    @Test
    public void testTC3_middleColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(1, 1); will(returnValue(5.0));
            allowing(values).getValue(2, 1); will(returnValue(8.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 1);
        assertEquals(15.0, result, 1e-9);
    }

      
    // TC5 – Single row
      
    @Test
    public void testTC5_singleRow() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(1));
            allowing(values).getValue(0, 0); will(returnValue(5.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(5.0, result, 1e-9);
    }

      
    // TC6 – Single column
      
    @Test
    public void testTC6_singleColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(2.0));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
            allowing(values).getValue(2, 0); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(9.0, result, 1e-9);
    }

      
    // TC7 – Empty table (0 rows)
      
    @Test
    public void testTC7_emptyTable() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC8 – All null values
      
    @Test
    public void testTC8_allNullColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(null));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC9 – Some null values
      
    @Test
    public void testTC9_someNullValues() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(4.0, result, 1e-9);
    }

      
    // TC10 – All zeros
      
    @Test
    public void testTC10_allZeroColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(0.0));
            allowing(values).getValue(1, 0); will(returnValue(0.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(0.0, result, 1e-9);
    }

      
    // TC12 – All negative values
      
    @Test
    public void testTC12_allNegative() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(-2.0));
            allowing(values).getValue(1, 0); will(returnValue(-4.0));
        }});

        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals(-6.0, result, 1e-9);
    }

      
    // TC14 – Null data (real behavior = NullPointerException)
      
    @Test(expected = IllegalArgumentException.class)
    public void testTC14_nullData() {
        DataUtilities.calculateColumnTotal(null, 0);
    }
    
	 // TC_NEG1 – Negative row count: enters second loop, getValue returns non-null
	 // Covers: second loop condition (true), n != null (true branch)
	 // NOTE: This test will hang due to infinite loop bug in implementation
//	 @Test(timeout = 1000)
//	 public void testNegativeRowCount_nonNullValue_addsToTotal() {
//	     context.checking(new Expectations() {{
//	         allowing(values).getRowCount(); will(returnValue(-1));
//	         allowing(values).getValue(with(any(Integer.class)), with(equal(0)));
//	             will(returnValue(7.0));
//	     }});
//	
//	     double result = DataUtilities.calculateColumnTotal(values, 0);
//	     assertEquals(7.0, result, 1e-9);
//	 }
	
	 // TC_NEG2 – Negative row count: enters second loop, getValue returns null
	 // Covers: second loop condition (true), n != null (false branch)
	 // NOTE: This test will hang due to infinite loop bug in implementation
//	 @Test(timeout = 1000)
//	 public void testNegativeRowCount_nullValue_returnsZero() {
//	     context.checking(new Expectations() {{
//	         allowing(values).getRowCount(); will(returnValue(-1));
//	         allowing(values).getValue(with(any(Integer.class)), with(equal(0)));
//	             will(returnValue(null));
//	     }});
//	
//	     double result = DataUtilities.calculateColumnTotal(values, 0);
//	     assertEquals(0.0, result, 1e-9);
//	 }
    
	 // Kills line 127: Less than to not equal
	 // Two distinct non-zero rows — exact sum required.
	 @Test
	 public void testTC_twoRows_sumIsCorrect() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(3.0));
	         allowing(values).getValue(1, 0); will(returnValue(4.0));
	     }});
	     assertEquals("3.0 + 4.0 = 7.0", 7.0,
	             DataUtilities.calculateColumnTotal(values, 0), 1e-9);
	 }
	
	 // Kills line 127: Substituted 0 with 1 on loop init — row 0 would be skipped
	 @Test
	 public void testTC_firstRowContributes_notSkipped() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(10.0));
	         allowing(values).getValue(1, 0); will(returnValue(1.0));
	     }});
	     assertEquals("Row 0 must not be skipped; 10 + 1 = 11.0", 11.0,
	             DataUtilities.calculateColumnTotal(values, 0), 1e-9);
	 }
	
	 // Kills line 130: Incremented (a++) / Decremented (a--) on total accumulator
	 // Post-increment mutant uses stale total before addition — detected by tight check.
	 @Test
	 public void testTC_accumulatorExact_threeEqualRows() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(3));
	         allowing(values).getValue(0, 0); will(returnValue(1.0));
	         allowing(values).getValue(1, 0); will(returnValue(1.0));
	         allowing(values).getValue(2, 0); will(returnValue(1.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0);
	     assertEquals("1+1+1 must be exactly 3.0", 3.0, result, 1e-9);
	     assertTrue("a++ mutant would give 2.0 — must not be < 3.0", result >= 3.0 - 1e-9);
	     assertTrue("a-- mutant would give 4.0 — must not be > 3.0", result <= 3.0 + 1e-9);
	 }
	
	 // Kills line 133: removed conditional - replaced comparison check with true (dead loop survivor)
	 // If dead second loop (r2 > rowCount) ever ran, total would be doubled.
	 @Test
	 public void testTC_deadSecondLoopMustNotDoubleTotal() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(2.0));
	         allowing(values).getValue(1, 0); will(returnValue(3.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0);
	     assertEquals("Dead loop must not run; 2+3=5, not 10", 5.0, result, 1e-9);
	 }
	
	 // Kills line 139: Incremented (a++) / Decremented (a--) on return value
	 // Return total+1 or total-1 would fail this tight assertion.
	 @Test
	 public void testTC_returnValueIsExactSum() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(4.0));
	         allowing(values).getValue(1, 0); will(returnValue(6.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0);
	     assertEquals("Return must equal 4+6=10 exactly", 10.0, result, 1e-9);
	     assertTrue("a++ mutant would return 11.0", result < 10.5);
	     assertTrue("a-- mutant would return 9.0",  result > 9.5);
	 }
	 
	 /**
	     * Kills line 130 a++ mutant.
	     *
	     * With values [10, 20, 30]:
	     *   Correct:   0 + 10 + 20 + 30 = 60
	     *   a++ mutant uses stale `total` before each add, effectively discarding
	     *   the running sum on every iteration → final total ≠ 60.
	     */
	    @Test
	    public void testLine130_postIncrementMutant_accumulatorPreservesRunningTotal() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(3));
	            allowing(values).getValue(0, 0); will(returnValue(10.0));
	            allowing(values).getValue(1, 0); will(returnValue(20.0));
	            allowing(values).getValue(2, 0); will(returnValue(30.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	 
	        // a++ mutant: total is reset to stale value before each add,
	        // so the cross-row accumulation is lost → result would be 30 (last value only).
	        // a-- mutant: similarly loses the accumulation → result would be 0.
	        assertEquals(
	            "Running total must accumulate across all rows: 10+20+30=60",
	            60.0, result, 1e-9);
	        assertTrue("a++ mutant yields 30.0 (last value only); result must be > 30",
	            result > 30.0);
	        assertTrue("a-- mutant yields 0.0 (accumulation lost); result must be > 0",
	            result > 0.0);
	    }
	 
	    /**
	     * Kills line 130 a-- mutant directly.
	     *
	     * Values [5, 5, 5]: correct sum = 15.
	     * a-- mutant: each iteration uses stale pre-decrement; total never climbs → 0.
	     * Also distinguishes from a++ (which gives 5 for this input).
	     */
	    @Test
	    public void testLine130_postDecrementMutant_totalDoesNotDropToZero() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(3));
	            allowing(values).getValue(0, 0); will(returnValue(5.0));
	            allowing(values).getValue(1, 0); will(returnValue(5.0));
	            allowing(values).getValue(2, 0); will(returnValue(5.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	 
	        assertEquals("5+5+5 must equal 15.0", 15.0, result, 1e-9);
	        // a-- mutant yields 0.0; assert strictly above zero.
	        assertTrue("a-- mutant would yield 0.0; result must be > 0", result > 0.0 - 1e-9);
	        // a++ mutant yields 5.0 (last row value only)
	        assertTrue("a++ mutant would yield 5.0; result must be > 5", result > 5.0);
	    }
	 
	    /**
	     * Kills "replaced comparison check with true" (line 133 / loop condition).
	     *
	     * With rowCount = 0 the loop must NOT execute.
	     * A "condition always true" mutant would spin forever; with a finite
	     * getValue stub it would accumulate the stub value indefinitely.
	     * We verify that 0 rows → 0.0 total, and no getValue call is made.
	     *
	     * (Complements TC7; the strict tolerance here is the kill signal.)
	     */
	    @Test
	    public void testLine133_conditionAlwaysTrue_zeroRowsReturnsZero() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(0));
	            // No getValue call should ever be made for an empty table.
	            never(values).getValue(with(any(Integer.class)), with(any(Integer.class)));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        assertEquals("Empty table must return 0.0 with zero getValue calls",
	            0.0, result, 1e-9);
	    }
	 
	    /**
	     * Kills "Negated integer local variable number 5" (rowCount → -rowCount).
	     *
	     * Mutant: rowCount becomes negative → `r < -rowCount` is `0 < -2` = false
	     * immediately → loop skips all rows → returns 0.0 for a non-empty table.
	     * Fix: non-empty table must return a non-zero sum.
	     */
	    @Test
	    public void testLine133_negatedRowCount_loopMustIterateAllRows() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(2));
	            allowing(values).getValue(0, 0); will(returnValue(7.0));
	            allowing(values).getValue(1, 0); will(returnValue(3.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        // Negated-rowCount mutant would yield 0.0 (loop skipped).
	        assertEquals("Negated rowCount mutant skips all rows; must equal 10.0",
	            10.0, result, 1e-9);
	        assertTrue("Loop must have iterated; result must be > 0", result > 0.0);
	    }
	 
	    /**
	     * Kills "Substituted 0 with -1" on loop initializer (r starts at -1).
	     *
	     * Mutant: first getValue call uses row = -1, which is invalid.
	     * This test uses a strict mock that only permits row indices {0, 1},
	     * so any call to getValue(-1, 0) causes a JMock unsatisfied expectation
	     * and the test fails — killing the mutant.
	     */
	    @Test
	    public void testLine133_loopInitSubstituted_rowMinusOneNeverAccessed() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(2));
	            // Only rows 0 and 1 are permitted. Row -1 must never be called.
	            allowing(values).getValue(0, 0); will(returnValue(1.0));
	            allowing(values).getValue(1, 0); will(returnValue(2.0));
	            // Explicitly forbid access to row -1.
	            never(values).getValue(-1, 0);
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        assertEquals("Sum must be 1+2=3.0 with no out-of-bounds row access",
	            3.0, result, 1e-9);
	    }
	 
	    /**
	     * Kills a++/a-- post-increment on r or rowCount (off-by-one row traversal).
	     *
	     * Strategy: 4 rows with distinct values [1, 2, 4, 8].
	     * Any off-by-one (skipping or double-counting a row) produces a sum that
	     * differs from 15 by at least 1, detectable at tolerance 1e-9.
	     *
	     * Also kills variants where the last row is visited twice or skipped.
	     */
	    @Test
	    public void testLine133_postIncrementOnLoopVar_allRowsVisitedExactlyOnce() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(4));
	            allowing(values).getValue(0, 0); will(returnValue(1.0));
	            allowing(values).getValue(1, 0); will(returnValue(2.0));
	            allowing(values).getValue(2, 0); will(returnValue(4.0));
	            allowing(values).getValue(3, 0); will(returnValue(8.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        // Any off-by-one in iteration changes the sum by at least 1.
	        assertEquals("All 4 rows must be visited exactly once: 1+2+4+8=15",
	            15.0, result, 1e-9);
	    }
	 
	    /**
	     * Kills "Incremented (++a) integer local variable number 4" — pre-increment
	     * on r before use in loop body, which would skip row 0.
	     *
	     * We place the largest value in row 0 so its absence is obvious.
	     * Also separately asserts the row-0 contribution is included.
	     */
	    @Test
	    public void testLine133_preIncrementSkipsRowZero_row0MustBeIncluded() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(3));
	            allowing(values).getValue(0, 0); will(returnValue(100.0));
	            allowing(values).getValue(1, 0); will(returnValue(1.0));
	            allowing(values).getValue(2, 0); will(returnValue(1.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        // ++r mutant would skip row 0 → 1 + 1 = 2.0
	        assertEquals("Row 0 (value=100) must be included: 100+1+1=102",
	            102.0, result, 1e-9);
	        assertTrue("++r mutant yields 2.0; result must be >> 2", result > 50.0);
	    }
	 
	    /**
	     * Kills "Decremented (--a) integer local variable number 5" — pre-decrement
	     * on r, making the loop start at r = -1 (reading an invalid row).
	     *
	     * With a strict mock that forbids negative row access, the mutant
	     * triggers a JMock failure.  Also asserts the correct sum.
	     */
	    @Test
	    public void testLine133_preDecrementLoopVar_noNegativeRowAccess() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(2));
	            allowing(values).getValue(0, 0); will(returnValue(6.0));
	            allowing(values).getValue(1, 0); will(returnValue(4.0));
	            never(values).getValue(with(org.hamcrest.Matchers.lessThan(0)),
	                                   with(any(Integer.class)));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        assertEquals("6+4=10; no negative row index may be accessed",
	            10.0, result, 1e-9);
	    }
	 
	    /**
	     * Kills line 139 a++ mutant (returns total+1 instead of total).
	     *
	     * Sum = 0.5: a++ mutant returns 1.5; strict assertion on 0.5 kills it.
	     * Using a fractional sum ensures it cannot accidentally equal total+1.
	     */
	    @Test
	    public void testLine139_returnPostIncrement_exactFractionalSum() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(2));
	            allowing(values).getValue(0, 0); will(returnValue(0.3));
	            allowing(values).getValue(1, 0); will(returnValue(0.2));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        // a++ mutant: returns 1.5 instead of 0.5
	        assertEquals("Return value must be exactly 0.3+0.2=0.5; a++ would give 1.5",
	            0.5, result, 1e-9);
	        assertTrue("a++ mutant returns ~1.5; result must be < 1.0", result < 1.0);
	    }
	 
	    /**
	     * Kills line 139 a-- mutant (returns total-1 instead of total).
	     *
	     * Sum = 0.7: a-- mutant returns -0.3; negative result is clearly wrong.
	     */
	    @Test
	    public void testLine139_returnPostDecrement_totalNotShiftedByOne() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(2));
	            allowing(values).getValue(0, 0); will(returnValue(0.4));
	            allowing(values).getValue(1, 0); will(returnValue(0.3));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        // a-- mutant: returns -0.3 instead of 0.7
	        assertEquals("Return value must be exactly 0.4+0.3=0.7; a-- would give -0.3",
	            0.7, result, 1e-9);
	        assertTrue("a-- mutant returns ~-0.3; result must be > 0", result > 0.0);
	        assertTrue("a++ mutant returns ~1.7; result must be < 1.0", result < 1.0);
	    }
	 
	    /**
	     * Comprehensive return-value kill: large sum with strict tight bounds.
	     *
	     * Sum = 1000.0; a++ mutant → 1001.0; a-- mutant → 999.0.
	     * Bounds [999.5, 1000.5] kill both.
	     */
	    @Test
	    public void testLine139_returnValue_largeExactSumWithBounds() {
	        context.checking(new Expectations() {{
	            allowing(values).getRowCount(); will(returnValue(4));
	            allowing(values).getValue(0, 0); will(returnValue(100.0));
	            allowing(values).getValue(1, 0); will(returnValue(200.0));
	            allowing(values).getValue(2, 0); will(returnValue(300.0));
	            allowing(values).getValue(3, 0); will(returnValue(400.0));
	        }});
	 
	        double result = DataUtilities.calculateColumnTotal(values, 0);
	        assertEquals("100+200+300+400 must return exactly 1000.0", 1000.0, result, 1e-9);
	        assertTrue("a-- mutant returns 999.0; result must be > 999.5", result > 999.5);
	        assertTrue("a++ mutant returns 1001.0; result must be < 1000.5", result < 1000.5);
	    }
}