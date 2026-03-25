package org.jfree.data.datautilitiestest;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.Values2D;
import org.junit.Test;

public class DataUtilitiesCalculateRowTotalTest {

    // Simple stub class for Values2D
    static class MyValues2D implements Values2D {
        private final Number[][] data;

        public MyValues2D(Number[][] data) {
            this.data = data;
        }

        @Override
        public Number getValue(int row, int column) {
            return data[row][column];
        }

        @Override
        public int getColumnCount() {
            return data.length > 0 ? data[0].length : 0;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }
    }

    /**
     * Normal case: all values present
     */
    @Test
    public void testNormalCase() {
        Number[][] data = {
            {1.0, 2.0, 3.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(6.0, total, 0.0000001);
    }

    /**
     * Some values null → skipped
     */
    @Test
    public void testSomeNullValues() {
        Number[][] data = {
            {null, 2.0, null}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(2.0, total, 0.0000001);
    }

    /**
     * Some columns out of range → skipped
     */
    @Test
    public void testColsOutOfRange() {
        Number[][] data = {
            {1.0, 2.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {0,1,2}; // column 2 out of range

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(3.0, total, 0.0000001);
    }

    /**
     * Empty validCols → total 0
     */
    @Test
    public void testEmptyValidCols() {
        Number[][] data = {
            {1.0, 2.0, 3.0}
        };
        MyValues2D myData = new MyValues2D(data);
        int[] validCols = {};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(0.0, total, 0.0000001);
    }

    /**
     * Null data → exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        DataUtilities.calculateRowTotal(null, 0, new int[]{0,1});
    }

    /**
     * colCount negative → should return 0
     */
    @Test
    public void testNegativeColCount() {
        Number[][] data = {};
        MyValues2D myData = new MyValues2D(data) {
            @Override
            public int getColumnCount() {
                return -1;
            }
        };
        int[] validCols = {0,1};

        double total = DataUtilities.calculateRowTotal(myData, 0, validCols);
        assertEquals(0.0, total, 0.0000001);
    }
    
	 // Kills line 220: all SURVIVED — dead branch "if (colCount < 0) total = 0.0"
	 // Mutants that make this condition true would reset total to 0 mid-execution.
	 // We verify result is correct (non-zero) even though colCount is positive.
	 @Test
	 public void testDeadIfBranch_colCountNotNegative_doesNotResetTotal() {
	     Number[][] data = {{5.0, 6.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {0, 1};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("Dead if(colCount<0) must not fire; 5+6=11.0", 11.0, result, 1e-9);
	     assertTrue("Result must be > 0 — dead branch did not reset total to 0.0",
	             result > 0.0);
	 }
	
	 // Kills line 223: Less than to not equal
	 // Three valid columns with distinct values — loop must run all 3 iterations.
	 @Test
	 public void testValidCols_loopRunsExactlyThreeTimes() {
	     Number[][] data = {{1.0, 2.0, 3.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {0, 1, 2};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("All 3 valid cols: 1+2+3 = 6.0", 6.0, result, 1e-9);
	 }
	
	 // Kills line 224: Incremented (a++) integer array field, Decremented (a--) integer array field
	 // validCols[0] = 1 must fetch column 1 (value=20.0), not column 0 (10.0) or column 2 (30.0).
	 @Test
	 public void testValidCols_correctColIndexReadFromArray() {
	     Number[][] data = {{10.0, 20.0, 30.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {1};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("validCols[0]=1 must fetch col 1 (20.0), not col 0 or col 2",
	             20.0, result, 1e-9);
	 }
	
	 // Kills line 226: Negated integer local variable (row), Incremented (a++) / Decremented (a--) on col
	 // Row must be passed through correctly; col off-by-one would fetch wrong value.
	 @Test
	 public void testValidCols_correctColPassedToGetValue() {
	     Number[][] data = {{5.0, 77.0, 9.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {1};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("getValue must be called with col=1 (value=77.0), not col 0 or 2",
	             77.0, result, 1e-9);
	 }
	
	 // Kills line 228: Incremented (a++) / Decremented (a--) on total accumulator
	 @Test
	 public void testValidCols_accumulatorExact_twoColumns() {
	     Number[][] data = {{1.0, 1.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {0, 1};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("1+1 must be exactly 2.0", 2.0, result, 1e-9);
	     assertTrue("a++ mutant gives 1.0 — must be >= 2.0", result >= 2.0 - 1e-9);
	     assertTrue("a-- mutant gives 3.0 — must be <= 2.0", result <= 2.0 + 1e-9);
	 }
	
	 // Kills line 232: Incremented (a++) / Decremented (a--) on return value
	 @Test
	 public void testValidCols_returnValueIsExactAccumulatedSum() {
	     Number[][] data = {{4.0, 6.0}};
	     MyValues2D myData = new MyValues2D(data);
	     int[] validCols = {0, 1};
	     double result = DataUtilities.calculateRowTotal(myData, 0, validCols);
	     assertEquals("Return must be 4+6=10 exactly", 10.0, result, 1e-9);
	     assertTrue("a++ mutant returns 11.0", result < 10.5);
	     assertTrue("a-- mutant returns 9.0",  result > 9.5);
	 }
}
