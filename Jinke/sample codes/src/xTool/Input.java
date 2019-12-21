package xTool;

public class Input {
	
	
	/**
	 * Determines whether the string is a number
	 * @param t	Input string
	 * @return	bool logic value
	 */
	public static boolean isInteger(String t) {
		return t.matches("[0-9]+");
	}
	
	
	/**
	 * Determines whether it is an int or float
	 * @param t
	 * @return
	 */
	public static boolean isFloat(String t) {
		return t.matches("[0-9]*\\.?[0-9]+");
	}
	
	
	/**
	 * Determines whether it is an int or null
	 * @param t
	 * @return
	 */
	public static boolean isEmptyOrInteger(String t) {
		return t.isEmpty() || t.matches("[0-9]+");
	}
	
	
	/**
	 * Determines whether it is a flout or null
	 * @param t
	 * @return
	 */
	public static boolean isEmptyOrFloat(String t) {
		return t.isEmpty() || t.matches("[0-9]*\\.?[0-9]+");
	}
	
	
	/**
	 * Format string, construct sql statement to use
	 * @param t
	 * @return
	 */
	public static String sqlStringFormat(String t ) {
		return t == null || t.isEmpty() ? null : String.format("'%s'", t);
	}
	
	/**
	 * Format string, construct sql statement to use
	 * @param t
	 * @return
	 */
	public static String sqlDigitalFormat(String t ) {
		return t == null || t.isEmpty() ? null : t;
				
	}
	
	
	/**
	 * Determine if it is an null or fixed-length int
	 * @param t string
	 * @param n length
	 * @return
	 */
	public static boolean isEmptyOrInteger(String t, int n) {
		return t.isEmpty() || t.matches(String.format("[0-9]{%d}", n));
	}

}
