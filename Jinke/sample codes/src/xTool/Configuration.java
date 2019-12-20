package xTool;

import java.util.Calendar;
import java.util.Date;

import database.H2_DB;

public class Configuration {
	
	private String title = "动态血压检查报告";
	private String[] customitem = new String[2];
	private String[] filenamesplit = new String[2];
	private String prefix = "jinke";
	private String reportitem = "0,1,4";
	private String filenameitem = "1,2,3";
	private int width = 5, counter = 0;
	
	private Calendar dayStart = Calendar.getInstance(), nightStart = Calendar.getInstance();
	
	private int daysystolic 	= 145,
				daydiastolic 	= 95,
				nightsystolic 	= 125,
				nightdiastolic	= 85;
	private Double standMBPS = 23.6;
	
	public Configuration(String fnmitm, String repitm, String tit, String pre, int w, int c, Date dst, Date nst, int ds, int dd, int ns, int nd, Double standmbps, String cusite1, String cusite2, String fnmspl1, String fnmspl2) {
		filenameitem	= fnmitm;
		reportitem		= repitm;
		title 			= tit;
		prefix 			= pre;
		width			= w;
		counter			= c;
		daysystolic 	= ds;
		daydiastolic 	= dd;
		nightsystolic 	= ns;
		nightdiastolic 	= nd;
		standMBPS		= standmbps;
		customitem[0]	= cusite1;
		customitem[1]	= cusite2;
		filenamesplit[0]= fnmspl1;
		filenamesplit[1]= fnmspl2;
		
		dayStart.setTime(dst);
		nightStart.setTime(nst);
	}
	
	public Configuration() {
		
		filenamesplit[0]= "_";
		filenamesplit[1]= "_";
		customitem[0] = "医师签名";
		customitem[1] = "";
		dayStart.set(Calendar.HOUR, 6);
		dayStart.set(Calendar.MINUTE, 0);
		nightStart.set(Calendar.HOUR, 22);
		nightStart.set(Calendar.MINUTE, 0);
		
	}
	
	public String getFileName() {
		return filenameitem;
	}
	public String[] getFileNameItem() {	
		String[] items = filenameitem.split(",");
		H2_DB h2 = new H2_DB();
		for (int i = 0; i < items.length; i++) {
			switch (items[i]) {
				case "0":				//空
					items[i] = "";
					break;
				case "1":				//测量日期
					items[i] = h2.getReviewTime(Cache.getDate());
					break;
				case "2":				//当前日期
					Calendar calendar = Calendar.getInstance();
					items[i] = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
					break;
				case "3":				//测量类型
					items[i] = "24hABPM";
					break;
				case "4":				//姓名
					items[i] = Cache.getName();
					break;
				case "5":				//ID
					items[i] = Cache.getCode();
					break;
				default:
					break;
			}
		}
		h2.close();
		return items;
	}
	
	public String getReportItem() {
		return reportitem;
	}
	public String getTitle() {
		return title;
	}
	public String getPrefix() {
		return prefix;
	}
	public int getWidth() {
		return width;
	}
	public int getCounter() {
		return counter;
	}
	public Calendar getDayStart() {
		return dayStart;
	}
	public Calendar getNightStart() {
		return nightStart;
	}
	public int getDaySBP() {
		return daysystolic;
	}
	public int getNightSBP() {
		return nightsystolic;
	}
	public int getDayDBP() {
		return daydiastolic;
	}
	public int getNightDBP() {
		return nightdiastolic;
	}
	public Double getStandMBPS() {
		return standMBPS;
	}
	public String[] getCustomItems() {
		return customitem;
	}
	public String[] getFileNameSplit() {
		return filenamesplit;
	}
}
