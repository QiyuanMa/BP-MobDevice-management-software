package xTool;

public class Cache {
	
	// Recording Box Port Number
	private static String com = null;
	
	
	/**
	 * Set the port number
	 * @param tString
	 */
	public static void setCom(String tString) {
		com = tString;
	}
	
	
	/**
	 * Get port number
	 * @return
	 */
	public static String getCom() {
		return com;
	}
	
	// patient id
	private static String code = null;
	
	
	/**
	 * Set ID
	 * @param key
	 */
	public static void setCode(String key) {
		code = key;
	}
	
	
	/**
	 * return ID
	 * @return
	 */
	public static String getCode() {
		return code;
	}
	
	
	// Patient age and name
	private static String name = null, sex = null;
	private static int age = 0;
	
	
	/**
	 * Set name age
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
	 * return age
	 * @return
	 */
	public static int getAge() {
		return age;
	}
	
	
	/**
	 * return age
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
	 * set date
	 * @param new_date
	 */
	public static void setDate(String new_date) {
		date = new_date;
	}
	
	/**
	 * return date
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
