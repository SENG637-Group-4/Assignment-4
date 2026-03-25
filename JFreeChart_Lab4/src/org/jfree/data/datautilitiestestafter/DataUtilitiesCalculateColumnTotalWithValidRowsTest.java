package org.jfree.data.datautilitiestestafter;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues2D;
import org.junit.*;

/**
 * White-box tests for DataUtilities.calculateColumnTotal(Values2D, int, int[]).
 *
 * Concrete implementation note: jmock 2.5.1 requires hamcrest-collection which is
 * not bundled in the A3 artifact set. Per the assignment guideline ("Replace your
 * test that uses a mock with a test case that uses the actual dependent-on component"),
 * these tests use DefaultKeyedValues2D — a concrete Values2D implementation that is
 * part of the JFreeChart library itself.
 *
 * Source code (SUT bug annotated):
 *
 *   public static double calculateColumnTotal(Values2D data, int column, int[] validRows) {
 *       ParamChecks.nullNotPermitted(data, "data");   // Node 1
 *       double total = 0.0;
 *       if (total > 0) {        // *** BUG *** Branch A is ALWAYS FALSE (dead code).
 *           total = 100;        // total is 0.0 at this point; 0.0 > 0 is false.
 *       }
 *       int rowCount = data.getRowCount();
 *       for (int v = 0; v < validRows.length; v++) {   // Branch B: loop runs?
 *           int row = validRows[v];
 *           if (row < rowCount) {                       // Branch C: row index in range?
 *               Number n = data.getValue(row, column);
 *               if (n != null) {                        // Branch D: value non-null?
 *                   total += n.doubleValue();
 *               }
 *           }
 *       }
 *       return total;
 *   }
 *
 * All reachable branch outcomes are exercised:
 *   Null data  → IllegalArgumentException             → test 1
 *   Branch A (true) = INFEASIBLE (dead code)          → documented as bug
 *   Branch B: empty loop (validRows.length = 0)       → test 2
 *   Branch B: loop runs                               → tests 3–8
 *   Branch C=true  (row in range)                     → tests 3, 5, 6, 7, 8
 *   Branch C=false (row out of range → skip)          → test 4
 *   Branch D=true  (value non-null → add)             → tests 3, 4, 5, 6, 7, 8
 *   Branch D=false (value null → skip)                → test 5
 *
 * Spec: "Returns the total of the values in one column of the data table,
 *        considering only the specified valid row indices."
 */
public class DataUtilitiesCalculateColumnTotalWithValidRowsTest {

    // Helper: build a DefaultKeyedValues2D with integer row/column keys (0-based)
    private static DefaultKeyedValues2D buildTable(double[][] data) {
        DefaultKeyedValues2D table = new DefaultKeyedValues2D();
        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[r].length; c++) {
                table.addValue(data[r][c], r, c);
            }
        }
        return table;
    }

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @Before      public void setUp()              throws Exception {}
    @After       public void tearDown()           throws Exception {}
    @AfterClass  public static void tearDownAfterClass() throws Exception {}

    // Node 1: null data → ParamChecks.nullNotPermitted throws IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void calculateColumnTotalWithNullDataThrowsException() {
        DataUtilities.calculateColumnTotal(null, 0, new int[]{0});
    }

    // Branch B: validRows.length = 0 → loop never executes → total stays 0.0
    @Test
    public void calculateColumnTotalWithEmptyValidRowsReturnsZero() {
        DefaultKeyedValues2D table = buildTable(new double[][]{{1.0}, {2.0}, {3.0}});
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{});
        assertEquals("Empty validRows → no rows read → total = 0.0", 0.0, result, 1e-9);
    }

    // Branch B: loop runs; Branch C=true; Branch D=true
    // NOM: all specified row indices are valid, all values non-null
    @Test
    public void calculateColumnTotalWithAllValidRowsAndNonNullValues() {
        DefaultKeyedValues2D table = buildTable(new double[][]{{1.0}, {2.0}, {3.0}});
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{0, 1, 2});
        assertEquals("Rows 0,1,2 → 1.0+2.0+3.0 = 6.0", 6.0, result, 1e-9);
    }

    // Branch C=false: row index >= rowCount → silently skipped (not added)
    // BVA: one out-of-range index mixed with valid ones
    @Test
    public void calculateColumnTotalSkipsOutOfRangeRowIndices() {
        DefaultKeyedValues2D table = buildTable(new double[][]{{5.0}, {3.0}});
        // rowCount = 2; index 5 is out of range and must be skipped
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{0, 1, 5});
        assertEquals("Row 5 out of range (rowCount=2) — skipped; total = 5.0+3.0 = 8.0",
                8.0, result, 1e-9);
    }

    // Branch D=false: null value in a valid row → skipped
    // Use setValue(null, ...) to put a null in the table
    @Test
    public void calculateColumnTotalSkipsNullValuesInValidRows() {
        DefaultKeyedValues2D table = new DefaultKeyedValues2D();
        table.addValue(4.0,  0, 0);
        table.addValue(null, 1, 0); // null value in row 1
        table.addValue(3.0,  2, 0);
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{0, 1, 2});
        assertEquals("Null in row 1 is skipped; total = 4.0+3.0 = 7.0", 7.0, result, 1e-9);
    }

    // NOM: single valid row in a non-zero column
    @Test
    public void calculateColumnTotalWithSingleValidRow() {
        DefaultKeyedValues2D table = buildTable(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 9.5},
                {0.0, 0.0, 0.0}});
        double result = DataUtilities.calculateColumnTotal(table, 2, new int[]{1});
        assertEquals("Single valid row 1, column 2: getValue(1,2)=9.5 → total = 9.5",
                9.5, result, 1e-9);
    }

    // EC: negative values in valid rows
    @Test
    public void calculateColumnTotalWithNegativeValues() {
        DefaultKeyedValues2D table = buildTable(new double[][]{{-3.0}, {-7.0}});
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{0, 1});
        assertEquals("-3.0 + -7.0 = -10.0", -10.0, result, 1e-9);
    }

    // EC: subset of rows — only rows 0 and 2 included in validRows
    @Test
    public void calculateColumnTotalWithSubsetOfRows() {
        DefaultKeyedValues2D table = buildTable(new double[][]{{10.0}, {5.0}, {3.0}});
        // Only rows 0 and 2 are specified; row 1 (5.0) should be excluded
        double result = DataUtilities.calculateColumnTotal(table, 0, new int[]{0, 2});
        assertEquals("Only rows 0 and 2 specified; row 1 excluded; total = 10.0+3.0 = 13.0",
                13.0, result, 1e-9);
    }
}
