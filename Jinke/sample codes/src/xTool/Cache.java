package xTool;

public class Cache {
	
	// 记录盒端口号
	private static String com = null;
	
	
	/**
	 * 设置端口号
	 * @param tString
	 */
	public static void setCom(String tString) {
		com = tString;
	}
	
	
	/**
	 * 获得端口号
	 * @return
	 */
	public static String getCom() {
		return com;
	}
	
	// 患者id
	private static String code = null;
	
	
	/**
	 * 设置ID
	 * @param key
	 */
	public static void setCode(String key) {
		code = key;
	}
	
	
	/**
	 * 返回ID
	 * @return
	 */
	public static String getCode() {
		return code;
	}
	
	
	// 患者年龄以及姓名
	private static String name = null, sex = null;
	private static int age = 0;
	
	
	/**
	 * 设置姓名年龄
	 * @param new_age
	 * @param new_name
	 */
	public static void setAgeName(int new_age, String new_name) {
		age = new_age;
		name = new_name;
	}
	public static void setSex(int gender) {
		switch (gender) {
		case 0:
			sex = "--";
			break;
		case 1:
			sex = "男";
			break;
		case 2:
			sex = "女";
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 获得年龄
	 * @return
	 */
	public static int getAge() {
		return age;
	}
	
	
	/**
	 * 获得年龄
	 * @return
	 */
	public static String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getSex() {
		return sex;
	}
	
	private static String date = null;
	
	
	/**
	 * 设置日期
	 * @param new_date
	 */
	public static void setDate(String new_date) {
		date = new_date;
	}
	
	/**
	 * 获得日期
	 * @return
	 */
	public static String getDate() {
		return date;
	}
	public static boolean flagForEmpty = true;
	public static void setEmpty(boolean flag) {
		flagForEmpty = flag;
	}
	public static boolean isEmpty() {
		return flagForEmpty;
	}

}
