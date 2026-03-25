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
}