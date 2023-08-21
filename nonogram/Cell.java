package nonogram;

/**
 * A cell in a Nonogram puzzle.
 * 
 * @author Dr Mark C. Sinclair
 * @version September 2022
 */
public class Cell {
	/**
	 * Constructor, the state is set to UNKNOWN
	 * 
	 * @param ng the nonogram puzzle this cell is part of
	 * @param row the cell row in the grid
	 * @param col the cell column in the grid
	 */
	public Cell(Nonogram ng, int row, int col) {
		if (ng == null)
			throw new IllegalArgumentException("ng cannot be null");
		if ((row < 0)  || (row >= ng.getNumRows()))
			throw new IllegalArgumentException("row invalid, must be 0 <= row < " + ng.getNumRows());
		if ((col < 0)  || (col >= ng.getNumCols()))
			throw new IllegalArgumentException("col invalid, must be 0 <= col < " + ng.getNumCols());
		this.ng    = ng;
		this.row   = row;
		this.col   = col;
		this.state = Nonogram.UNKNOWN;
	}
	
	/**
	 * Constructor
	 * 
	 * @param ng the nonogram puzzle this cell is part of
	 * @param row the cell row in the grid
	 * @param col the cell column in the grid
	 * @param state the initial Cell state
	 */
	public Cell(Nonogram ng, int row, int col, int state) {
		this(ng, row, col);
		if (!isValidState(state))
			throw new IllegalArgumentException("invalid state (" + state + ")");
		this.state = state;
	}
	
  /**
   * Retrieve the cell row
   * 
   * @return the row
   */
	public int getRow() {
		return row;
	}
	
  /**
   * Retrieve the cell column
   * 
   * @return the column
   */
	public int getCol() {
		return col;
	}
	
	/**
	 * Is the cell state FULL?
	 * 
	 * @return true if the cell state is FULL, otherwise false
	 */
	public boolean isFull() {
		if (!checkState())
			throw new NonogramException("invalid cell state (" + state + ")");
		return state == Nonogram.FULL;
	}
	
	/**
	 * Is the cell state EMPTY?
	 * 
	 * @return true if the cell state is EMPTY, otherwise false
	 */
	public boolean isEmpty() {
		if (!checkState())
			throw new NonogramException("invalid cell state (" + state + ")");
		return state == Nonogram.EMPTY;
	}
	
	/**
	 * Is the cell state UNKNOWN?
	 * 
	 * @return true if the cell state is UNKNOWN, otherwise false
	 */
	public boolean isUnknown() {
		if (!checkState())
			throw new NonogramException("invalid cell state (" + state + ")");
		return state == Nonogram.UNKNOWN;
	}
	
	/**
	 * Retrieve the cell state
	 * 
	 * @return the cell state (FULL, EMPY or UNKNOWN)
	 */
	public int getState() {
		if (!checkState())
			throw new NonogramException("invalid cell state (" + state + ")");
		return state;
	}
	
	/**
	 * Set the cell state to FULL
	 */
	public void setFull() {
		state = Nonogram.FULL;
	}
	
	/**
	 * Set the cell state to EMPTY
	 */
	public void setEmpty() {
		state = Nonogram.EMPTY;
	}
	
	/**
	 * Set the cell state to UNKNOWN
	 */
	public void setUnknown() {
		state = Nonogram.UNKNOWN;
	}
	
	/**
	 * Set the cell state
	 * 
	 * @param state the desired state (FULL, EMPY or UNKNOWN)
	 */
	void setState(int state) {
		if (!isValidState(state))
			throw new NonogramException("invalid state (" + state + ")");
		this.state = state;
	}
	
  /**
   * String representation of the assignment
   * 
   * @return the string representation (the cell state)
   */
	@Override
	public String toString() {
		return "" + getState();
	}
	
  /**
   * String representation of the assignment (useful for debugging)
   * 
   * @return the string representation
   */
	public String toStringFull() {
		StringBuffer sb = new StringBuffer("Cell(");
		sb.append(row);
		sb.append(",");
		sb.append(col);
		sb.append(",");
		sb.append(getState());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Check the cell state is valid (FULL, EMPY or UNKNOWN)
	 * 
	 * @return true if the state is valid, otherwise false
	 */
	private boolean checkState() {
		return isValidState(state);
	}
	
	/**
	 * Check if an integer is a valid cell state (FULL, EMPY or UNKNOWN)
	 * 
	 * @param state the integer to check
	 * @return true if the state is valid, otherwise false
	 */
	public static boolean isValidState(int state) {
		if ((state == Nonogram.FULL)  ||
				(state == Nonogram.EMPTY) ||
				(state == Nonogram.UNKNOWN))
			return true;
		else
			return false;
	}
	
	/**
	 * Check that all the cells in an array are from the same nonogram
	 * 
	 * @param cells the array of cells to check
	 * @return true if all the cells are from the same nonogram, otherwise false
	 */
	public static boolean checkSameNonogram(Cell[] cells) {
		if (cells == null)
			throw new IllegalArgumentException("cells cannot be null");
		if (cells.length < Nonogram.MIN_SIZE)
			throw new IllegalArgumentException("cells cannot be shorter than " + Nonogram.MIN_SIZE);
		Nonogram ng = cells[0].ng;
		for (int i=1; i<cells.length; i++)
			if (cells[i].ng != ng)
				return false;
		return true;
	}
		
	private int      state;
	private int      row;
	private int      col;
  private Nonogram ng = null;	
}
