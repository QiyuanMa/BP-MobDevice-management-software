package xTool;

public class Input {
	
	
	/**
	 * 判断字符串是否为数字
	 * @param t	输入字符串
	 * @return	bool 布尔逻辑值
	 */
	public static boolean isInteger(String t) {
		return t.matches("[0-9]+");
	}
	
	
	/**
	 * 判断是否为整数或浮点数
	 * @param t
	 * @return
	 */
	public static boolean isFloat(String t) {
		return t.matches("[0-9]*\\.?[0-9]+");
	}
	
	
	/**
	 * 判断是否为整数或为空
	 * @param t
	 * @return
	 */
	public static boolean isEmptyOrInteger(String t) {
		return t.isEmpty() || t.matches("[0-9]+");
	}
	
	
	/**
	 * 判断是否为浮点数或为空
	 * @param t
	 * @return
	 */
	public static boolean isEmptyOrFloat(String t) {
		return t.isEmpty() || t.matches("[0-9]*\\.?[0-9]+");
	}
	
	
	/**
	 * 格式化字符串，构造sql语句使用
	 * @param t
	 * @return
	 */
	public static String sqlStringFormat(String t ) {
		return t == null || t.isEmpty() ? null : String.format("'%s'", t);
	}
	
	/**
	 * 格式化字符串，构造sql语句使用
	 * @param t
	 * @return
	 */
	public static String sqlDigitalFormat(String t ) {
		return t == null || t.isEmpty() ? null : t;
				
	}
	
	
	/**
	 * 判断是否为空或定长整数
	 * @param t 字符串
	 * @param n 长度
	 * @return
	 */
	public static boolean isEmptyOrInteger(String t, int n) {
		return t.isEmpty() || t.matches(String.format("[0-9]{%d}", n));
	}

}
