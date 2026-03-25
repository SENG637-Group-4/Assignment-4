package org.jfree.data.datautilitiestestafter;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class CalculateColumnTotalValidRowsTest {

    private Mockery context;
    private Values2D values;

    @Before
    public void setUp() {
        context = new Mockery();
        values = context.mock(Values2D.class);
    }

    // ------------------------------------------------------------------
    // Normal valid rows — all rows in validRows are within rowCount
    // ------------------------------------------------------------------

    @Test
    public void testSingleValidRow() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(5.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0});
        assertEquals(5.0, result, 1e-9);
    }

    @Test
    public void testTwoValidRows() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(2.0));
            allowing(values).getValue(1, 0); will(returnValue(3.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
        assertEquals(5.0, result, 1e-9);
    }

    @Test
    public void testAllRowsValid() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(2.0));
            allowing(values).getValue(2, 0); will(returnValue(3.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1, 2});
        assertEquals(6.0, result, 1e-9);
    }

    @Test
    public void testLastRowOnly() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(2, 0); will(returnValue(9.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{2});
        assertEquals(9.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Rows in validRows that are >= rowCount are skipped
    // ------------------------------------------------------------------

    @Test
    public void testRowIndexEqualToRowCountIsSkipped() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(4.0));
        }});
        // row 3 == rowCount (3), so it is skipped
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 3});
        assertEquals(4.0, result, 1e-9);
    }

    @Test
    public void testAllRowsAboveRowCountSkipped() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{5, 6, 7});
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testMixOfValidAndInvalidRows() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(2.0));
            allowing(values).getValue(2, 0); will(returnValue(3.0));
        }});
        // row 5 is out of range and skipped
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 2, 5});
        assertEquals(5.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Null values in table are skipped
    // ------------------------------------------------------------------

    @Test
    public void testNullValueInTableIsSkipped() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(4.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
        assertEquals(4.0, result, 1e-9);
    }

    @Test
    public void testAllNullValuesReturnZero() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(null));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
        assertEquals(0.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Empty validRows array → returns 0.0
    // ------------------------------------------------------------------

    @Test
    public void testEmptyValidRowsArray() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{});
        assertEquals(0.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Negative values
    // ------------------------------------------------------------------

    @Test
    public void testNegativeValues() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(-3.0));
            allowing(values).getValue(1, 0); will(returnValue(-2.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
        assertEquals(-5.0, result, 1e-9);
    }

    @Test
    public void testMixedPositiveNegativeValues() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(5.0));
            allowing(values).getValue(1, 0); will(returnValue(-5.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
        assertEquals(0.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Different column index
    // ------------------------------------------------------------------

    @Test
    public void testSecondColumn() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 1); will(returnValue(7.0));
            allowing(values).getValue(1, 1); will(returnValue(3.0));
        }});
        double result = DataUtilities.calculateColumnTotal(values, 1, new int[]{0, 1});
        assertEquals(10.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Bug: total starts at 0.0 so "if (total > 0)" is never true
    // → the total = 100 line is dead code and never executes
    // This test confirms total is NOT affected by that dead branch
    // ------------------------------------------------------------------

    @Test
    public void testDeadBranchDoesNotAffectTotal() {
        context.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(2));
            allowing(values).getValue(0, 0); will(returnValue(3.0));
        }});
        // If the dead branch fired, result would be 103.0 — confirm it is 3.0
        double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0});
        assertEquals(3.0, result, 1e-9);
    }

    // ------------------------------------------------------------------
    // Null data → IllegalArgumentException
    // ------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testNullDataThrowsException() {
        DataUtilities.calculateColumnTotal(null, 0, new int[]{0});
    }
    
	 // Kills line 158: dead branch "if (total > 0) total = 100" — SURVIVED mutants
	 // If any mutant activates this branch, total would be ~103 instead of 3.
	 @Test
	 public void testDeadIfBranch_doesNotSetTotalTo100() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(1));
	         allowing(values).getValue(0, 0); will(returnValue(3.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0});
	     assertEquals("Dead if(total>0) must never fire; result must be 3.0 not 103.0",
	             3.0, result, 1e-9);
	     assertTrue("Result must be < 10 — dead branch did not corrupt total",
	             result < 10.0);
	 }
	
	 // Kills line 163: Less than to not equal
	 // Three valid rows with distinct values — loop must run all 3 iterations.
	 @Test
	 public void testValidRows_loopRunsExactlyThreeTimes() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(3));
	         allowing(values).getValue(0, 0); will(returnValue(1.0));
	         allowing(values).getValue(1, 0); will(returnValue(2.0));
	         allowing(values).getValue(2, 0); will(returnValue(3.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1, 2});
	     assertEquals("All 3 valid rows: 1+2+3 = 6.0", 6.0, result, 1e-9);
	 }
	
	 // Kills line 164: Incremented (a++) integer array field, Decremented (a--) integer array field
	 // The array element validRows[v] is read; a post-increment mutant would corrupt the index.
	 // Row 1 holds a unique value (20.0) — off-by-one index would fetch a different row.
	 @Test
	 public void testValidRows_correctRowIndexReadFromArray() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(3));
	         allowing(values).getValue(0, 0); will(returnValue(10.0));
	         allowing(values).getValue(1, 0); will(returnValue(20.0));
	         allowing(values).getValue(2, 0); will(returnValue(30.0));
	     }});
	     // validRows = {1} — must fetch exactly row 1, not row 0 or row 2
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{1});
	     assertEquals("validRows[0]=1 must fetch row 1 (value=20.0), not row 0 or 2",
	             20.0, result, 1e-9);
	 }
	
	 // Kills line 166: Incremented (a++) / Decremented (a--) on row index in getValue call
	 // validRows = {2} — off-by-one would call getValue(1,0)=5 or getValue(3,0) instead.
	 @Test
	 public void testValidRows_rowIndexPassedToGetValueIsCorrect() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(4));
	         allowing(values).getValue(1, 0); will(returnValue(5.0));
	         allowing(values).getValue(2, 0); will(returnValue(77.0));
	         allowing(values).getValue(3, 0); will(returnValue(9.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{2});
	     assertEquals("getValue must be called with row=2 (value=77.0), not row 1 or 3",
	             77.0, result, 1e-9);
	 }
	
	 // Kills line 168: Incremented (a++) / Decremented (a--) on total accumulator
	 @Test
	 public void testValidRows_accumulatorExact_twoRows() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(1.0));
	         allowing(values).getValue(1, 0); will(returnValue(1.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
	     assertEquals("1+1 must be exactly 2.0", 2.0, result, 1e-9);
	     assertTrue("a++ mutant gives 1.0 — must be >= 2.0", result >= 2.0 - 1e-9);
	     assertTrue("a-- mutant gives 3.0 — must be <= 2.0", result <= 2.0 + 1e-9);
	 }
	
	 // Kills line 172: Incremented (a++) / Decremented (a--) on return value
	 @Test
	 public void testValidRows_returnValueIsExactAccumulatedSum() {
	     context.checking(new Expectations() {{
	         allowing(values).getRowCount(); will(returnValue(2));
	         allowing(values).getValue(0, 0); will(returnValue(4.0));
	         allowing(values).getValue(1, 0); will(returnValue(6.0));
	     }});
	     double result = DataUtilities.calculateColumnTotal(values, 0, new int[]{0, 1});
	     assertEquals("Return must be 4+6=10 exactly", 10.0, result, 1e-9);
	     assertTrue("a++ mutant returns 11.0", result < 10.5);
	     assertTrue("a-- mutant returns 9.0",  result > 9.5);
	 }
}