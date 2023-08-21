package nonogram;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A pattern for a cell constraint (either row or column) in a Nonogram puzzle.
 * 
 * @author Dr Mark C. Sinclair
 * @version September 2022
 */
public class NGPattern {
	/**
	 * Constructor
	 * 
	 * @param nums the pattern of contiguous full cells as an integer array
	 * @param maxLen the maximum allowed length of the pattern
	 */
	public NGPattern(int[] nums, int maxLen) {
		if (!checkNums(nums))
			throw new IllegalArgumentException("nums invalid");
		this.nums   = Arrays.copyOf(nums, nums.length);
		this.minLen = calcMinLen(nums);
		if (maxLen < Nonogram.MIN_SIZE)
			throw new IllegalArgumentException("nums cannot be shorter than " + Nonogram.MIN_SIZE);
		this.maxLen = maxLen;
		if (minLen > maxLen)
			throw new IllegalArgumentException("minimum length of nums (" + minLen + ") exceeds maxLen");
	}
	
	/**
	 * Retrieve the minimum number of cells this pattern can represent
	 * 
	 * @return the minimum length (number of cells)
	 */
	public int getMinLen() {
		return minLen;
	}
	
	/**
	 * Retrieve the maximum number of cells it has been decided this pattern can represent
	 * 
	 * @return the maximum length (number of cells)
	 */
	public int getMaxLen() {
		return maxLen;
	}
	
	/**
	 * Retrieve the pattern of contiguous full cells as an integer array 
	 * 
	 * @return the integer array representation
	 */
	public int[] getNums() {
		return Arrays.copyOf(nums, nums.length);
	}
	
	/**
	 * Retrieve a regular expression that can be used to check a cell sequence for validity
	 * 
	 * @return the validity regular expression as a string
	 */
	public String getRegExValid() {
		if (valid == null)
			compileRegExValid();  // causes regex to be compiled and assigned
		return valid.pattern();
	}
	
	/**
	 * Retrieve a regular expression that can be used to check if a cell sequence solves the pattern
	 * 
	 * @return the solution regular expression as a string
	 */
	public String getRegExSolved() {
		if (solved == null)
			compileRegExSolved();  // causes regex to be compiled and assigned
		return solved.pattern();
	}
	
	/**
	 * Compile and cache the validity regular expression
	 */
	private void compileRegExValid() {
		StringBuffer sb = new StringBuffer();
		sb.append(REGEX_EMPTY + "*?"); // optional empty chars at start
		for (int i=0; i < (nums.length-1); i++) {
			sb.append(REGEX_FULL + "{" + nums[i] + "}");
			sb.append(REGEX_EMPTY + "+?"); // non optional empty chars
		}
		sb.append(REGEX_FULL + "{" + nums[nums.length-1] + "}"); // last full chars
		sb.append(REGEX_EMPTY + "*?"); // optional empty chars at end
		valid = Pattern.compile(sb.toString());
	}
	
	/**
	 * Compile and cache the solution regular expression
	 */
	private void compileRegExSolved() {
		StringBuffer sb = new StringBuffer();
		sb.append(REGEX_EMPTY + "*?"); // optional empty chars at start
		for (int i=0; i < (nums.length-1); i++) {
			sb.append(Nonogram.FULL + "{" + nums[i] + "}");
			sb.append(REGEX_EMPTY + "+?"); // non optional empty chars
		}
		sb.append(Nonogram.FULL + "{" + nums[nums.length-1] + "}"); // last full chars
		sb.append(REGEX_EMPTY + "*?"); // optional empty chars at end
		solved = Pattern.compile(sb.toString());
	}
	
	/**
	 * Check if a cell state sequence (of maxLen) is valid for the pattern
	 * 
	 * @param seq a sequence of maxLen cell states
	 * @return true if the sequence is valid, otherwise false
	 */
	public boolean isValid(String seq) {
		if (seq == null)
			throw new IllegalArgumentException("seq cannot be null");
		if (seq.length() != maxLen)
			throw new IllegalArgumentException("seq is incorrect length for pattern (" + seq.length() + "!=" + maxLen+")");
		if (valid == null)
			compileRegExValid(); // causes valid Pattern to be compiled and assigned
		return valid.matcher(seq).matches();
	}
	
	/**
	 * Check if a cell state sequence (of maxLen) solves the pattern
	 * 
	 * @param seq a sequence of maxLen cell states
	 * @return true if the sequence is a solution, otherwise false
	 */
	public boolean isSolved(String seq) {
		if (seq == null)
			throw new IllegalArgumentException("seq cannot be null");
		if (seq.length() != maxLen)
			throw new IllegalArgumentException("seq is incorrect length for pattern (" + seq.length() + "!=" + maxLen+")");
		if (solved == null)
			compileRegExSolved(); // causes solved Pattern to be compiled and assigned
		return solved.matcher(seq).matches();
	}
	
  /**
   * String representation of the pattern (the array of the numbers of contiguous full cells as a string)
   * 
   * @return the string representation
   */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(toStringForNon());
		sb.append("]");
		return sb.toString();
	}
	
  /**
   * String representation of the pattern (the array of the numbers of contiguous full cells as a string) in a form suitable for a .non file
   * 
   * @return the string representation
   */
	public String toStringForNon() {
		StringBuffer sb = new StringBuffer();
		sb.append(nums[0]);
		for (int i=1; i<nums.length; i++)
			sb.append(","+nums[i]);
		return sb.toString();
	}
	
  /**
   * String representation of the pattern (useful for debugging)
   * 
   * @return the string representation
   */
	public String toStringFull() {
		StringBuffer sb = new StringBuffer("NGPattern(");
		sb.append(toString());
		sb.append(",");
		sb.append(minLen);
		sb.append(",");
		sb.append(maxLen);
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * Checks if an integer array could represent a pattern of the numbers of contiguous full cells in a nonogram row/column
	 * 
	 * @return true if passes the check, otherwise false
	 */
	public static boolean checkNums(int[] nums) {
		if (nums == null)
			return false;
		if (nums.length == 0)
			return false;
		for (int i=0; i<nums.length; i++)
			if (nums[i] <= 0)
				return false;
		return true;
	}
	
	/**
	 * Calculate the minimum length (number of cells) that an integer array representing a pattern of the numbers of contiguous
	 * full cells in a nonogram row/column could occupy
	 * 
	 * @param nums a pattern of contiguous full cells as an integer array
	 * @return the minimum length (number of cells)
	 */
	public static int calcMinLen(int[] nums) {
		if (!checkNums(nums))
			throw new IllegalArgumentException("nums invalid");
		int min = nums[0];
		for (int i=1; i<nums.length; i++)
			min += nums[i] + 1; // the extra 1 is for the necessary gap between filled blocks
		return min;
	}
	
	private static final String REGEX_EMPTY = "[" + Nonogram.EMPTY + Nonogram.UNKNOWN + "]";
	private static final String REGEX_FULL  = "[" + Nonogram.FULL  + Nonogram.UNKNOWN + "]";
	
	private int[]   nums   = null;
	private int     minLen = -1;   // minimum length of the pattern
	private int     maxLen = -1;   // maximum length of the pattern (supplied)
	private Pattern valid  = null; // regex to test sequence is valid
	private Pattern solved = null; // regex to test sequence is solved
}
