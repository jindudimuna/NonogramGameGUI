package nonogram;

import java.util.Arrays;

/**
 * A cell constraint (either row or column) in a Nonogram puzzle.
 * 
 * @author Dr Mark C. Sinclair
 * @version September 2022
 */
public class Constraint {
	/**
	 * Constructor
	 * 
	 * @param pat the pattern of contiguous full cells that the cells should match
	 * @param cells the cells to which the pattern applies
	 */
	public Constraint(NGPattern pat, Cell[] cells) {
		if (pat == null)
			throw new IllegalArgumentException("pat cannot be null");
		if (cells == null)
			throw new IllegalArgumentException("cells cannot be null");
		if (cells.length < Nonogram.MIN_SIZE)
			throw new IllegalArgumentException("cells cannot be shorter than " + Nonogram.MIN_SIZE);
		if (!Cell.checkSameNonogram(cells))
			throw new IllegalArgumentException("cells must all be from the same Nonogram");
		if (pat.getMaxLen() != cells.length)
			throw new IllegalArgumentException("pat maxLen ("+pat.getMaxLen()+") must match length of cells array ("+cells.length+")");
		this.pat   = pat;
		this.cells = Arrays.copyOf(cells, cells.length);
	}
	
	/**
	 * Are the cells valid for this pattern?
	 * 
	 * @return true if valid, otherwise false
	 */
	public boolean isValid() {
		return pat.isValid(getSequence());
	}
	
	/**
	 * Are the cells a solution to the pattern?
	 * 
	 * @return true if the cells solve the pattern, otherwise false
	 */
	public boolean isSolved() {
		return pat.isSolved(getSequence());
	}
	
	/**
	 * Retrieve the regular expression used to check the cells for validity
	 * 
	 * @return the validity regular expression as a string
	 */
	public String getRegExValid() {
		return pat.getRegExValid();
	}
	
	/**
	 * Retrieve the regular expression used to check the cells solve the pattern
	 * 
	 * @return the solution regular expression as a string
	 */
	public String getRegExSolved() {
		return pat.getRegExSolved();
	}
	
	/**
	 * Retrieve the pattern of contiguous full cells as an integer array 
	 * 
	 * @return the integer array representation
	 */
	public int[] getNums() {
		return pat.getNums();
	}
	
	/**
	 * Retrieve the pattern of contiguous full cells as a string suitable for .non file use
	 * 
	 * @return the pattern as a string for a .non file
	 */
	public String getNumsForNon() {
		return pat.toStringForNon();
	}
	
	/**
	 * Retrieve the sequence of state values from the cells as a string
	 * 
	 * @return the sequence of cell state values
	 */
	public String getSequence() {
		// seq could be cached, but is this worthwhile?
		StringBuffer sb = new StringBuffer();
		for (Cell c : cells)
			sb.append(c);
		String seq = sb.toString();
		if (pat.getMaxLen() != seq.length())
			throw new NonogramException("cells sequence length ("+seq.length()+") must match pat maxLen ("+pat.getMaxLen()+")");
		return seq;
	}
	
	/**
	 * Set the cell state values from a sequence string
	 * 
	 * @param seq the cell sequence string
	 */
	public void setSequence(String seq) {
		if (seq == null)
			throw new IllegalArgumentException("seq cannot be null");
		if (seq.isEmpty())
			throw new IllegalArgumentException("seq cannot be empty");
		if (seq.length() != cells.length)
			throw new IllegalArgumentException("seq length ("+seq.length()+") must match length of cells array ("+cells.length+")");
		for (int i=0; i<cells.length; i++) {
			int state = Nonogram.UNKNOWN;
			try {
				state = Integer.parseInt(seq.substring(i, i+1));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("seq contains non number (" + seq.charAt(i) + ")");
			}
			if (!Cell.isValidState(state))
				throw new IllegalArgumentException("invalid state (" + state + ") in s["+ i +"]");
			cells[i].setState(state);
		}
	}
	
  /**
   * String representation of the constraint, consisting of just the sequence of cell states
   * 
   * @return the string representation (the cell state sequence)
   */
	@Override
	public String toString() {
		return getSequence();
	}
	
  /**
   * String representation of the constraint (useful for debugging)
   * 
   * @return the string representation
   */
	public String toStringFull() {
		StringBuffer sb = new StringBuffer("Constraint(");
		sb.append(pat);
		sb.append(",\"");
		sb.append(this);
		sb.append("\")");
		return sb.toString();
	}
	
	private NGPattern pat   = null;
	private Cell[]    cells = null;
}
