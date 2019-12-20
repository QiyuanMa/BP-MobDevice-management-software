package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import dialog.MessageDialog;
import xTool.Analyse;
import xTool.Cache;
import xTool.Configuration;
import xTool.Input;
import xTool.PatientInitStruct;
import xTool.RecordInitStruct;

public class H2_DB {
	
	private static String url = "jdbc:h2:file:";
	private static final String user = "root";
	private static final String pw = "123456";
	
	private Connection connection = null;
	private Statement statement = null;
	
	private boolean is_Connected = false;
	
	
	/**
	 * H2数据库构造函数
	 */
	public H2_DB() {
		if(url == "jdbc:h2:file:") {
			url = url + System.getProperty("user.dir") + "\\Database\\JincoMed";
		}
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(url, user, pw);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			is_Connected = true;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			if (e.toString().contains("Database may be already in use")){
				MessageDialog.showError(null, "数据库被占用。您是否重复打开本程序？");
				System.exit(0);
			}
			else {
				MessageDialog.showError(null, e.toString());
			}
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 获取某一患者信息
	 * @param code
	 * @return
	 */
	public PatientInitStruct getPatient(String code) {
		
		String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", code);
		PatientInitStruct p = new PatientInitStruct();
		
		try {
			
			ResultSet r = statement.executeQuery(sql);
			r.next();
			p.patient_id = r.getString("p_id");
			p.patient_name = r.getString("p_name");
			p.patient_age = r.getString("p_age");
			p.patient_sex = r.getInt("p_sex");
			p.patient_height = r.getString("p_height");
			p.patient_weight = r.getString("p_weight");
			p.patient_phone = r.getString("p_phone");
			p.patient_postcode = r.getString("p_postcode");
			p.patient_address = r.getString("p_address");
			p.patient_birth = r.getString("p_birthday");
			p.patient_department = r.getString("p_department");
			p.patient_bed = r.getString("p_bed");
			p.patient_medins = r.getString("p_medins");
			p.patient_clinic = r.getString("p_clinic");
			p.patient_hospital = r.getString("p_hospital");
			p.patient_medrecord = r.getString("p_medrecord");
			return p;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 检验ID是否可用
	 * @param id 待检验ID
	 * @return
	 */
	public boolean checkPatientID(String id) {
		String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", id);
		try {
			ResultSet r = statement.executeQuery(sql);
			r.last();
			if (r.getRow() > 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean insertPatient(PatientInitStruct p) {
		String sql = String.format(
				"INSERT INTO patient VALUES('%s', '%s', %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
				p.patient_id,
				p.patient_name,
				p.patient_age,
				p.patient_sex,
				Input.sqlDigitalFormat(p.patient_height),
				Input.sqlDigitalFormat(p.patient_weight), 
				Input.sqlStringFormat(p.patient_phone), 
				Input.sqlStringFormat(p.patient_postcode), 
				Input.sqlStringFormat(p.patient_address),
				Input.sqlStringFormat(p.patient_birth),
				Input.sqlStringFormat(p.patient_department),
				Input.sqlStringFormat(p.patient_bed),
				Input.sqlStringFormat(p.patient_medins),
				Input.sqlStringFormat(p.patient_clinic),
				Input.sqlStringFormat(p.patient_hospital),
				Input.sqlStringFormat(p.patient_medrecord));
		try {
			statement.execute(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updatePatient(PatientInitStruct p, String preID) {
		
		String sql = String.format("UPDATE patient SET p_id='%s', p_name='%s', p_age=%s, p_sex=%d, p_height=%s, p_weight=%s, p_phone=%s, p_postcode=%s, p_address=%s, p_birthday=%s, p_department=%s, p_bed=%s, p_medins=%s, p_clinic=%s, p_hospital=%s, p_medrecord=%s WHERE p_id='%s';",
				p.patient_id,
				p.patient_name,
				p.patient_age,
				p.patient_sex,
				Input.sqlDigitalFormat(p.patient_height),
				Input.sqlDigitalFormat(p.patient_weight), 
				Input.sqlStringFormat(p.patient_phone), 
				Input.sqlStringFormat(p.patient_postcode), 
				Input.sqlStringFormat(p.patient_address),
				Input.sqlStringFormat(p.patient_birth),
				Input.sqlStringFormat(p.patient_department),
				Input.sqlStringFormat(p.patient_bed),
				Input.sqlStringFormat(p.patient_medins),
				Input.sqlStringFormat(p.patient_clinic),
				Input.sqlStringFormat(p.patient_hospital),
				Input.sqlStringFormat(p.patient_medrecord),
				preID);
		
		try {
			statement.execute(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
	}


	/**
	 * 删除患者信息及其数据
	 * @param code
	 */
	public void deletePatient(String code) {
		String sql = String.format("DELETE FROM record WHERE p_id = '%1$s';DELETE FROM report WHERE p_id = '%1$s';DELETE FROM patient WHERE p_id = '%1$s'", code);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 导出患者信息到xml
	 * @param code	患者id
	 * @param file	文件路径
	 */
	public void exportPatient(String code[], String file) {
		Document document = DocumentHelper.createDocument();
		Element patients = document.addElement("patinets_list");
		try {
			for (String c : code) {
				String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", c);
				ResultSet r = statement.executeQuery(sql);
				
				r.next();
				
				Element patient = patients.addElement("patient");
				
				Element	key = patient.addElement("key"),
						basic_info = patient.addElement("basic_info"),
						contact_info = patient.addElement("contact_info"),
						moreinfo = patient.addElement("moreinfo");
				
				key.setText(r.getString("p_id"));
				
				Element name = basic_info.addElement("name"),
						age = basic_info.addElement("age"),
						sex = basic_info.addElement("gender"),
						height = basic_info.addElement("height"),
						weight = basic_info.addElement("weight");
				name.setText(r.getString("p_name"));
				age.setText(r.getString("p_age"));
				sex.setText(r.getString("p_sex"));
				
				if (r.getString("p_height") != null)
					height.setText(r.getString("p_height"));
				if (r.getString("p_weight") != null)
					weight.setText(r.getString("p_weight"));
				
				Element phone = contact_info.addElement("phone"),
						postcode = contact_info.addElement("postcode"),
						addr = contact_info.addElement("address");
				if (r.getString("p_phone") != null)
					phone.setText(r.getString("p_phone"));
				if (r.getString("p_phone") != null)
					postcode.setText(r.getString("p_postcode"));
				if (r.getString("p_address") != null)
					addr.setText(r.getString("p_address"));
				
				Element department = moreinfo.addElement("department"),
						bed = moreinfo.addElement("bed"),
						medins = moreinfo.addElement("medicalinsurance"),
						clinic = moreinfo.addElement("clinic"),
						hospital = moreinfo.addElement("hospital"),
						medrecord = moreinfo.addElement("medicalrecird");
				if (r.getString("p_department") != null) {
					department.setText(r.getString("p_department"));
				}
				if (r.getString("p_bed") != null) {
					bed.setText(r.getString("p_bed"));
				}
				if (r.getString("p_medins") != null) {
					medins.setText(r.getString("p_medins"));
				}
				if (r.getString("p_clinic") != null) {
					clinic.setText(r.getString("p_clinic"));
				}
				if (r.getString("p_hospital") != null) {
					hospital.setText(r.getString("p_hospital"));
				}
				if (r.getString("p_medrecord") != null) {
					medrecord.setText(r.getString("p_medrecord"));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer;
		try {
			if (file.matches(".+\\.xml"))
				writer = new XMLWriter(new FileOutputStream(new File(file)), format);
			else
				writer = new XMLWriter(new FileOutputStream(new File(file + ".xml")), format);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void exportPatientAndRecord(String patientID, String file, JTable jtable) throws ParseException {
		Document document = DocumentHelper.createDocument();
		Element data = document.addElement("Data");
		Element patinent 	= data.addElement("Patient"),
				records		= data.addElement("Records"),
				report		= data.addElement("Report");
		String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", patientID);
		try {
			ResultSet r = statement.executeQuery(sql);
			r.next();
			Element patient = patinent.addElement("patient");
			
			Element	key = patient.addElement("key"),
					basic_info = patient.addElement("basic_info"),
					contact_info = patient.addElement("contact_info"),
					moreinfo = patient.addElement("moreinfo");
			
			key.setText(r.getString("p_id"));
			
			Element name = basic_info.addElement("name"),
					age = basic_info.addElement("age"),
					sex = basic_info.addElement("gender"),
					height = basic_info.addElement("height"),
					weight = basic_info.addElement("weight");
			name.setText(r.getString("p_name"));
			age.setText(r.getString("p_age"));
			sex.setText(r.getString("p_sex"));
			
			if (r.getString("p_height") != null)
				height.setText(r.getString("p_height"));
			if (r.getString("p_weight") != null)
				weight.setText(r.getString("p_weight"));
			
			Element phone = contact_info.addElement("phone"),
					postcode = contact_info.addElement("postcode"),
					addr = contact_info.addElement("address");
			if (r.getString("p_phone") != null)
				phone.setText(r.getString("p_phone"));
			if (r.getString("p_phone") != null)
				postcode.setText(r.getString("p_postcode"));
			if (r.getString("p_address") != null)
				addr.setText(r.getString("p_address"));
			
			Element department = moreinfo.addElement("department"),
					bed = moreinfo.addElement("bed"),
					medins = moreinfo.addElement("medicalinsurance"),
					clinic = moreinfo.addElement("clinic"),
					hospital = moreinfo.addElement("hospital"),
					medrecord = moreinfo.addElement("medicalrecird");
			if (r.getString("p_department") != null) {
				department.setText(r.getString("p_department"));
			}
			if (r.getString("p_bed") != null) {
				bed.setText(r.getString("p_bed"));
			}
			if (r.getString("p_medins") != null) {
				medins.setText(r.getString("p_medins"));
			}
			if (r.getString("p_clinic") != null) {
				clinic.setText(r.getString("p_clinic"));
			}
			if (r.getString("p_hospital") != null) {
				hospital.setText(r.getString("p_hospital"));
			}
			if (r.getString("p_medrecord") != null) {
				medrecord.setText(r.getString("p_medrecord"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = String.format("SELECT * FROM record WHERE p_id = '%s'", patientID);
		try {
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				Element record = records.addElement("record");
				record.addAttribute("id", String.valueOf(r.getRow()));
				Element d_id 	= record.addElement("d_id"),
						d_date	= record.addElement("d_date"),
						d_time 	= record.addElement("d_time"),
						d_startday	= record.addElement("d_startday"),
						d_counter	= record.addElement("d_counter"),
						d_sbp	= record.addElement("d_sbp"),
						d_abp	= record.addElement("d_abp"),
						d_dbp 	= record.addElement("d_dbp"),
						d_hb	= record.addElement("d_hb"),
						d_code	= record.addElement("d_code"),
						d_voltage	= record.addElement("d_voltage");
				d_id.setText(r.getString(1));
				d_date.setText(r.getString(3));
				d_time.setText(r.getString(4));
				if (r.getString(5) != null)
					d_startday.setText(r.getString(5));
				if (r.getString(6) != null)
					d_counter.setText(r.getString(6));
				if (r.getString(7) != null)
					d_sbp.setText(r.getString(7));
				if (r.getString(8) != null)
					d_abp.setText(r.getString(8));
				if (r.getString(9) != null)
					d_dbp.setText(r.getString(9));
				if (r.getString(10) != null)
					d_hb.setText(r.getString(10));
				if (r.getString(11) != null)
					d_code.setText(r.getString(11));
				if (r.getString(12) != null)
					d_voltage.setText(r.getString(12));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Element header = report.addElement("Header");
		Configuration conf = getConfiguration();
		header.setText(conf.getTitle());
		Element recording = report.addElement("Content");
		Analyse analyse = new Analyse(jtable);
		if(analyse.getValidNum(Analyse.ALL) <= 0) {
			Element error = recording.addElement("Error");
			error.setText("没有足够的测量值用于评估。");
			OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding("UTF-8");
	        XMLWriter writer;
			try {
				if (file.matches(".+\\.xml"))
					writer = new XMLWriter(new FileOutputStream(new File(file)), format);
				else
					writer = new XMLWriter(new FileOutputStream(new File(file + ".xml")), format);
				writer.write(document);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if(analyse.getValidNum(Analyse.DAY) > 0) {
			Element day = recording.addElement("Day");
			DecimalFormat df = new DecimalFormat("0.0");//设置保留位数
			newElement("AvgDias", "白天舒张压平均值", analyse.getDataAvg(Analyse.DBP, Analyse.DAY)+"", day);
			newElement("AvgHR", "白天心率平均值", analyse.getDataAvg(Analyse.HR, Analyse.DAY)+"", day);
			newElement("AvgPulsPress", "白天脉压差平均值", (analyse.getDataAvg(Analyse.SBP, Analyse.DAY) - analyse.getDataAvg(Analyse.DBP, Analyse.DAY))+"", day);
			newElement("AvgSys", "白天收缩压平均值", analyse.getDataAvg(Analyse.SBP, Analyse.DAY)+"", day);
			newElement("DiasAboveLimit", "白天舒张压超过标准值次数", analyse.getOverNum(Analyse.DBP, Analyse.DAY)+"", day);
			int dayindex = analyse.getDataMax(Analyse.DBP, Analyse.DAY);
			newElement("DiasMaxTime", "白天最高舒张压发生时间", jtable.getValueAt(dayindex, 1) + " " + jtable.getValueAt(dayindex, 2), day);
			newElement("DiasMaxVal", "白天最高舒张压数值", jtable.getValueAt(dayindex, 5)+"", day);
			dayindex = analyse.getDataMin(Analyse.DBP, Analyse.DAY);
			newElement("DiasMinTime", "白天最低舒张压发生时间", jtable.getValueAt(dayindex, 1) + " " + jtable.getValueAt(dayindex, 2), day);
			newElement("DiasMinVal", "白天最低舒张压数值", jtable.getValueAt(dayindex, 5)+"", day);
			newElement("StdDerivDias", "白天舒张压标准差", df.format(analyse.getDataStd(Analyse.DBP, Analyse.DAY)), day);
			newElement("StdDerivHR", "白天心率标准差", df.format(analyse.getDataStd(Analyse.HR, Analyse.DAY)), day);
			newElement("StdDerivSys", "白天收缩压标准差", df.format(analyse.getDataStd(Analyse.SBP, Analyse.DAY)), day);
			newElement("SysAboveLimit", "白天收缩压超过标准值次数", analyse.getOverNum(Analyse.SBP, Analyse.DAY)+"", day);
			dayindex = analyse.getDataMax(Analyse.SBP, Analyse.DAY);
			newElement("SysMaxTime", "白天最高收缩压发生时间", jtable.getValueAt(dayindex, 1) + " " + jtable.getValueAt(dayindex, 2), day);
			newElement("SysMaxVal", "白天最高收缩压数值", jtable.getValueAt(dayindex, 3)+"", day);
			dayindex = analyse.getDataMin(Analyse.SBP, Analyse.DAY);
			newElement("SysMinTime", "白天最低收缩压发生时间", jtable.getValueAt(dayindex, 1) + " " + jtable.getValueAt(dayindex, 2), day);
			newElement("SysMinVal", "白天最低收缩压数值", jtable.getValueAt(dayindex, 3)+"", day);
		}
		if(analyse.getValidNum(Analyse.NIGHT) > 0) {
			Element night = recording.addElement("Night");
			DecimalFormat df = new DecimalFormat("0.0");//设置保留位数
			newElement("AvgDias", "夜间舒张压平均值", analyse.getDataAvg(Analyse.DBP, Analyse.NIGHT)+"", night);
			newElement("AvgHR", "夜间心率平均值", analyse.getDataAvg(Analyse.HR, Analyse.NIGHT)+"", night);
			newElement("AvgPulsPress", "夜间脉压差平均值", (analyse.getDataAvg(Analyse.SBP, Analyse.NIGHT) - analyse.getDataAvg(Analyse.DBP, Analyse.NIGHT))+"", night);
			newElement("AvgSys", "夜间收缩压平均值", analyse.getDataAvg(Analyse.SBP, Analyse.NIGHT)+"", night);
			newElement("DiasAboveLimit", "夜间舒张压超过标准值次数", analyse.getOverNum(Analyse.DBP, Analyse.NIGHT)+"", night);
			int nightindex = analyse.getDataMax(Analyse.DBP, Analyse.NIGHT);
			newElement("DiasMaxTime", "夜间最高舒张压发生时间", jtable.getValueAt(nightindex, 1) + " " + jtable.getValueAt(nightindex, 2), night);
			newElement("DiasMaxVal", "夜间最高舒张压数值", jtable.getValueAt(nightindex, 5)+"", night);
			nightindex = analyse.getDataMin(Analyse.DBP, Analyse.NIGHT);
			newElement("DiasMinTime", "夜间最低舒张压发生时间", jtable.getValueAt(nightindex, 1) + " " + jtable.getValueAt(nightindex, 2), night);
			newElement("DiasMinVal", "夜间最低舒张压数值", jtable.getValueAt(nightindex, 5)+"", night);
			newElement("StdDerivDias", "夜间舒张压标准差", df.format(analyse.getDataStd(Analyse.DBP, Analyse.NIGHT)), night);
			newElement("StdDerivHR", "夜间心率标准差", df.format(analyse.getDataStd(Analyse.HR, Analyse.NIGHT)), night);
			newElement("StdDerivSys", "夜间收缩压标准差", df.format(analyse.getDataStd(Analyse.SBP, Analyse.NIGHT)), night);
			newElement("SysAboveLimit", "夜间收缩压超过标准值次数", analyse.getOverNum(Analyse.SBP, Analyse.NIGHT)+"", night);
			nightindex = analyse.getDataMax(Analyse.SBP, Analyse.NIGHT);
			newElement("SysMaxTime", "夜间最高收缩压发生时间", jtable.getValueAt(nightindex, 1) + " " + jtable.getValueAt(nightindex, 2), night);
			newElement("SysMaxVal", "夜间最高收缩压数值", jtable.getValueAt(nightindex, 3)+"", night);
			nightindex = analyse.getDataMin(Analyse.SBP, Analyse.NIGHT);
			newElement("SysMinTime", "夜间最低收缩压发生时间", jtable.getValueAt(nightindex, 1) + " " + jtable.getValueAt(nightindex, 2), night);
			newElement("SysMinVal", "夜间最低收缩压数值", jtable.getValueAt(nightindex, 3)+"", night);
		}
		if(analyse.getValidNum(Analyse.DAY) > 0 && analyse.getValidNum(Analyse.NIGHT) > 0) {
			Element all = recording.addElement("All");
			DecimalFormat df = new DecimalFormat("0.0");//设置保留位数
			newElement("bloodPressureMorningAverageDiastolic", "晨峰血压-舒张压平均值", df.format(analyse.getMPBSAvg(Analyse.DBP)), all);
			newElement("bloodPressureMorningAverageSystolic", "晨峰血压-收缩压平均值", df.format(analyse.getMPBSAvg(Analyse.SBP)), all);
			ArrayList<Integer> indexList = analyse.getMPBS();
			Element MBPSDetail = all.addElement("bloodPressureMorningDetail");
			MBPSDetail.addAttribute("name", "晨峰血压-数据");
			for (int j = 0; j < indexList.size(); j++) {
				Element e = MBPSDetail.addElement("record");
				e.addAttribute("id", (j + 1)+"");
				Element time = e.addElement("time");
				Element sbp = e.addElement("sbp");
				Element dbp = e.addElement("dbp");
				Element hr = e.addElement("hr");
				time.setText(jtable.getValueAt(indexList.get(j), 2).toString());
				sbp.addAttribute("name", "收缩压");
				dbp.addAttribute("name", "舒张压");
				hr.addAttribute("name", "心率");
				sbp.setText(jtable.getValueAt(indexList.get(j), 3).toString());
				dbp.setText(jtable.getValueAt(indexList.get(j), 5).toString());
				hr.setText(jtable.getValueAt(indexList.get(j), 6).toString());
			}
			newElement("bloodPressureNightMinAverageDiastolic", "夜间最低血压-舒张压平均值", df.format(analyse.getMPBSNightAvg(Analyse.DBP)), all);
			newElement("bloodPressureNightMinAverageSystolic", "夜间最低血压-收缩压平均值", df.format(analyse.getMPBSNightAvg(Analyse.SBP)), all);
			ArrayList<Integer> SBPArrayList = analyse.getMPBSNightMin(Analyse.SBP);
	        ArrayList<Integer> DBPArrayList = analyse.getMPBSNightMin(Analyse.DBP);
	        Element MBPSNightDBPDetail = all.addElement("bloodPressureNightMinDiastolicDetail");
	        MBPSNightDBPDetail.addAttribute("name", "夜间最低血压-舒张压数据");
	        for (int j = 0; j < DBPArrayList.size(); j++) {
				Element e = MBPSNightDBPDetail.addElement("record");
				e.addAttribute("id", (j + 1)+"");
				Element time = e.addElement("time");
				Element value = e.addElement("value");
				time.setText(jtable.getValueAt(DBPArrayList.get(j), 2).toString());
				value.setText(jtable.getValueAt(DBPArrayList.get(j), 5).toString());
			}
	        Element MBPSNightSBPDetail = all.addElement("bloodPressureNightMinSystolicDetail");
	        MBPSNightSBPDetail.addAttribute("name", "夜间最低血压-收缩压数据");
	        for (int j = 0; j < SBPArrayList.size(); j++) {
				Element e = MBPSNightSBPDetail.addElement("record");
				e.addAttribute("id", (j + 1)+"");
				Element time = e.addElement("time");
				Element value = e.addElement("value");
				time.setText(jtable.getValueAt(SBPArrayList.get(j), 2).toString());
				value.setText(jtable.getValueAt(SBPArrayList.get(j), 5).toString());
			}
	        newElement("IMBPDiastolic", "清晨血压增高值-舒张压", df.format(analyse.getMPBSAvg(Analyse.DBP) - analyse.getMPBSNightAvg(Analyse.DBP)), all);
	        newElement("IMBPSystolic", "清晨血压增高值-收缩压", df.format(analyse.getMPBSAvg(Analyse.SBP) - analyse.getMPBSNightAvg(Analyse.SBP)), all);
	        String SBPCR = df.format((double)(analyse.getDataAvg(Analyse.SBP, Analyse.DAY) - analyse.getDataAvg(Analyse.SBP, Analyse.NIGHT)) / (double)analyse.getDataAvg(Analyse.SBP, Analyse.DAY) * 100.0);
	        String DBPCR = df.format((double)(analyse.getDataAvg(Analyse.DBP, Analyse.DAY) - analyse.getDataAvg(Analyse.DBP, Analyse.NIGHT)) / (double)analyse.getDataAvg(Analyse.DBP, Analyse.DAY) * 100.0);
	        newElement("changeRhythmDiastolic", "昼夜变化节律-舒张压", DBPCR, all);
	        newElement("changeRhythmSystolic", "昼夜变化节律-收缩压", SBPCR, all);
	        newElement("coefficientOfVariationDiastolicDay", "白天舒张压变异系数", df.format(analyse.getDataStd(Analyse.DBP, Analyse.DAY) / analyse.getDataAvg(Analyse.DBP, Analyse.DAY) * 100), all);
	        newElement("coefficientOfVariationDiastolicNight", "夜间舒张压变异系数", df.format(analyse.getDataStd(Analyse.DBP, Analyse.NIGHT) / analyse.getDataAvg(Analyse.DBP, Analyse.NIGHT) * 100), all);
	        newElement("coefficientOfVariationDiastolicTotal", "全天舒张压变异系数", df.format(analyse.getDataStd(Analyse.DBP, Analyse.ALL) / analyse.getDataAvg(Analyse.DBP, Analyse.ALL) * 100), all);
	        newElement("coefficientOfVariationSystolicDay", "白天收缩压变异系数", df.format(analyse.getDataStd(Analyse.SBP, Analyse.DAY) / analyse.getDataAvg(Analyse.SBP, Analyse.DAY) * 100), all);
	        newElement("coefficientOfVariationSystolicNight", "夜间收缩压变异系数", df.format(analyse.getDataStd(Analyse.SBP, Analyse.NIGHT) / analyse.getDataAvg(Analyse.SBP, Analyse.NIGHT) * 100), all);
	        newElement("coefficientOfVariationSystolicTotal", "全天收缩压变异系数", df.format(analyse.getDataStd(Analyse.SBP, Analyse.ALL) / analyse.getDataAvg(Analyse.SBP, Analyse.ALL) * 100), all);
	        newElement("loadValueDiastolicDay", "白天舒张压负荷值", df.format((double)analyse.getOverNum(Analyse.DBP, Analyse.DAY) / (double)analyse.getValidNum(Analyse.DAY) * 100.0), all);
	        newElement("loadValueDiastolicNight", "夜间舒张压负荷值", df.format((double)analyse.getOverNum(Analyse.DBP, Analyse.NIGHT) / (double)analyse.getValidNum(Analyse.NIGHT) * 100.0), all);
	        newElement("loadValueDiastolicTotal", "全天舒张压负荷值", df.format((double)analyse.getOverNum(Analyse.DBP, Analyse.ALL) / (double)analyse.getValidNum(Analyse.ALL) * 100.0), all);
	        newElement("loadValueSystolicDay", "白天收缩压负荷值", df.format((double)analyse.getOverNum(Analyse.SBP, Analyse.DAY) / (double)analyse.getValidNum(Analyse.DAY) * 100.0), all);
	        newElement("loadValueSystolicNight", "夜间收缩压负荷值", df.format((double)analyse.getOverNum(Analyse.SBP, Analyse.NIGHT) / (double)analyse.getValidNum(Analyse.NIGHT) * 100.0), all);
	        newElement("loadValueSystolicTotal", "全天收缩压负荷值", df.format((double)analyse.getOverNum(Analyse.SBP, Analyse.ALL) / (double)analyse.getValidNum(Analyse.ALL) * 100.0), all);
	        int dayindex = analyse.getDataMax(Analyse.HR, Analyse.DAY);
			newElement("maxHeartbeatDayTime", "白天最高心率发生时间", jtable.getValueAt(dayindex, 1) + " " + jtable.getValueAt(dayindex, 2), all);
			newElement("maxHeartbeatDay", "白天最高心率数值", jtable.getValueAt(dayindex, 6)+"", all);
			int nightindex = analyse.getDataMax(Analyse.HR, Analyse.NIGHT);
			newElement("maxHeartbeatNightTime", "夜间最高心率发生时间", jtable.getValueAt(nightindex, 1) + " " + jtable.getValueAt(nightindex, 2), all);
			newElement("maxHeartbeatNight", "夜间最高心率数值", jtable.getValueAt(nightindex, 6)+"", all);
			int allindex = analyse.getDataMax(Analyse.HR, Analyse.ALL);
			newElement("maxHeartbeatTotalTime", "全天最高心率发生时间", jtable.getValueAt(allindex, 1) + " " + jtable.getValueAt(allindex, 2), all);
			newElement("maxHeartbeatTotal", "全天最高心率数值", jtable.getValueAt(allindex, 6)+"", all);
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer;
		try {
			if (file.matches(".+\\.xml"))
				writer = new XMLWriter(new FileOutputStream(new File(file)), format);
			else
				writer = new XMLWriter(new FileOutputStream(new File(file + ".xml")), format);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void newElement(String name, String cname, String value, Element e) {
		Element element = e.addElement(name);
		element.addAttribute("name", cname);
		element.setText(value);
	}
	
	public void store(String file) {
		Document document = DocumentHelper.createDocument();
		Element data = document.addElement("Data");
		Element patients 	= data.addElement("Patients"),
				records		= data.addElement("Records"),
				reports		= data.addElement("Reports");
		
		String sql = "SELECT * FROM PATIENT";
		try {
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				Element patient = patients.addElement("patinet");
				Element	key = patient.addElement("key"),
						basic_info = patient.addElement("basic_info"),
						contact_info = patient.addElement("contact_info");

				patient.addAttribute("id", String.valueOf(r.getRow()));
				key.setText(r.getString("p_id"));
				
				Element name = basic_info.addElement("name"),
						age = basic_info.addElement("age"),
						sex = basic_info.addElement("gender"),
						height = basic_info.addElement("height"),
						weight = basic_info.addElement("weight");
				name.setText(r.getString("p_name"));
				age.setText(r.getString("p_age"));
				sex.setText(r.getString("p_sex"));
				
				if (r.getString("p_height") != null)
					height.setText(r.getString("p_height"));
				if (r.getString("p_weight") != null)
					weight.setText(r.getString("p_weight"));
				
				Element phone = contact_info.addElement("phone"),
						postcode = contact_info.addElement("postcode"),
						addr = contact_info.addElement("address");
				if (r.getString("p_phone") != null)
					phone.setText(r.getString("p_phone").toString());
				if (r.getString("p_phone") != null)
					postcode.setText(r.getString("p_postcode"));
				if (r.getString("p_address") != null) {
					addr.setText(r.getString("p_address"));
				}
				else {
					addr.setText("---");
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = "SELECT * FROM RECORD";
		try {
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				Element record = records.addElement("record");
				record.addAttribute("id", String.valueOf(r.getRow()));
				
				Element d_id 	= record.addElement("d_id"),
						p_id	= record.addElement("p_id"),
						d_date	= record.addElement("d_date"),
						d_time 	= record.addElement("d_time"),
						d_startday	= record.addElement("d_startday"),
						d_counter	= record.addElement("d_counter"),
						d_sbp	= record.addElement("d_sbp"),
						d_abp	= record.addElement("d_abp"),
						d_dbp 	= record.addElement("d_dbp"),
						d_hb	= record.addElement("d_hb"),
						d_code	= record.addElement("d_code"),
						d_voltage	= record.addElement("d_voltage");
				d_id.setText(r.getString(1));
				p_id.setText(r.getString(2));
				d_date.setText(r.getString(3));
				d_time.setText(r.getString(4));
				if (r.getString(5) != null)
					d_startday.setText(r.getString(5));
				if (r.getString(6) != null)
					d_counter.setText(r.getString(6));
				if (r.getString(7) != null)
					d_sbp.setText(r.getString(7));
				if (r.getString(8) != null)
					d_abp.setText(r.getString(8));
				if (r.getString(9) != null)
					d_dbp.setText(r.getString(9));
				if (r.getString(10) != null)
					d_hb.setText(r.getString(10));
				if (r.getString(11) != null)
					d_code.setText(r.getString(11));
				if (r.getString(12) != null)
					d_voltage.setText(r.getString(12));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		sql = "SELECT * FROM REPORT ";
		try {
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				Element report = reports.addElement("report");
				report.addAttribute("id", String.valueOf(r.getRow()));
				Element key 		= report.addElement("key"),
						p_report	= report.addElement("p_report");
				key.setText(r.getString(1));
				p_report.setText(r.getString(2));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer;
		try {
			if (file.matches(".+\\.xml"))
				writer = new XMLWriter(new FileOutputStream(new File(file)), format);
			else
				writer = new XMLWriter(new FileOutputStream(new File(file + ".xml")), format);
			writer.write(document);
			MessageDialog.showMessage(null, "导出成功！");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	public void restore(File f) {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(f);
			Element data = document.getRootElement();
			Element patients	= data.element("Patients"),
					records		= data.element("Records"),
					reports		= data.element("Reports");
			for (Element e : patients.elements()) {
				String key = e.elementText("key");
				Element basic_info = e.element("basic_info"),
						contact_info = e.element("contact_info");
				
				ResultSet r = statement.executeQuery(String.format("select * from patient where p_id=%s", key));
				r.last();
				if (r.getRow() != 0)
					MessageDialog.showError(null, String.format("患者：\r\n%s\r\n已存在！", r.getString("p_name")));
				else {
					String sql = String.format("INSERT INTO patient VALUES('%s', '%s', %s, %s, %s, %s, %s, %s, '%s');", 
							key,
							basic_info.elementText("name"),
							basic_info.elementText("age"),
							basic_info.elementText("gender"),
							Input.sqlDigitalFormat(basic_info.elementText("height")),
							Input.sqlDigitalFormat(basic_info.elementText("weight")),
							Input.sqlStringFormat(contact_info.elementText("phone")),
							Input.sqlStringFormat(contact_info.elementText("postcode")),
							Input.sqlDigitalFormat(contact_info.elementText("address")));
					statement.execute(sql);
				}

			}
			for (Element e: records.elements()) {
				ResultSet r = statement.executeQuery(String.format("select * from record where d_id='%s' and p_id='%s' and d_date='%s' and d_time='%s'", 
						e.elementText("d_id"),e.elementText("p_id"), e.elementText("d_date"), e.elementText("d_time")));
				r.last();
				if (r.getRow() == 0) {
					String sql = String.format("INSERT INTO record VALUES(%s,'%s', '%s','%s', '%s', %s, %s, %s, %s, %s, %s, %s)", 
							e.elementText("d_id"),
							e.elementText("p_id"),
							e.elementText("d_date"),
							e.elementText("d_time"),
							e.elementText("d_startday"),
							Input.sqlStringFormat(e.elementText("d_counter")),
							Input.sqlStringFormat(e.elementText("d_sbp")),
							Input.sqlStringFormat(e.elementText("d_abp")),
							Input.sqlStringFormat(e.elementText("d_dbp")),
							Input.sqlStringFormat(e.elementText("d_hb")),
							e.elementText("d_code"),
							e.elementText("d_voltage"));
					statement.execute(sql);
				}
			}
			for (Element e : reports.elements()) {
				ResultSet r = statement.executeQuery(String.format("SELECT * FROM REPORT WHERE p_id ='%s'", e.elementText("key")));
				r.last();
				if (r.getRow() == 0) {
					String sql = String.format("insert into report values('%s','%s')", e.elementText("key"), e.elementText("p_report"));
					statement.execute(sql);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
	
	
	/**
	 * 导入患者信息
	 * @param f	xml文件路径
	 */
	public int importPatient(File f) {
		SAXReader reader = new SAXReader();
		int flag = -1;
		try {
			Document document = reader.read(f);
			Element patients = document.getRootElement();
			for (Element e : patients.elements()) {
				String key = e.elementText("key");
				Element basic_info = e.element("basic_info"),
						contact_info = e.element("contact_info"),
						moreinfo = e.element("moreinfo");
				
				ResultSet r = statement.executeQuery(String.format("select * from patient where p_id='%s'", key));
				r.last();
				if (r.getRow() != 0) {
					flag = 0;
					MessageDialog.showError(null, String.format("患者：\r\n%s\r\n已存在！", r.getString("p_name")));
				}
				else {
					flag = 1;
					PatientInitStruct patientInitStruct = new PatientInitStruct();
					patientInitStruct.patient_id = key;
					patientInitStruct.patient_name = basic_info.elementText("name");
					patientInitStruct.patient_sex = Integer.parseInt(basic_info.elementText("gender"));
					patientInitStruct.patient_age = basic_info.elementText("age");
					patientInitStruct.patient_height = basic_info.elementText("height");
					patientInitStruct.patient_weight = basic_info.elementText("weight");
					patientInitStruct.patient_phone = contact_info.elementText("phone");
					patientInitStruct.patient_postcode = contact_info.elementText("postcode");
					patientInitStruct.patient_address = contact_info.elementText("address");
					patientInitStruct.patient_department = moreinfo.elementText("department");
					patientInitStruct.patient_bed = moreinfo.elementText("bed");
					patientInitStruct.patient_medins = moreinfo.elementText("medicalinsurance");
					patientInitStruct.patient_clinic = moreinfo.elementText("clinic");
					patientInitStruct.patient_hospital = moreinfo.elementText("hospital");
					patientInitStruct.patient_medrecord = moreinfo.elementText("medicalrecird");
					insertPatient(patientInitStruct);
				}
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
		
	}
	
	
	/**
	 * 获取患者报告
	 * @param code	患者ID
	 * @return
	 */
	public String getReport(String code, String date) {
		String sql = String.format("SELECT p_report FROM report WHERE p_id = '%s' and d_startday='%s'", code, date);
		try {
			ResultSet r = statement.executeQuery(sql);
			if (r.next())
				return r.getString("p_report");
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 插入患者诊断报告
	 * @param code
	 * @param report
	 */
	public void insertPatientReport(String code, String report, String date) {
		String sql = String.format("INSERT INTO report VALUES('%s', '%s', '%s')", code, report, date);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 更新患者诊断报告
	 * @param code
	 * @param report
	 */
	public void updatePatientReport(String code, String report, String date) {
		String 	sql = String.format("UPDATE report SET p_report='%s' WHERE p_id='%s' and d_startday='%s'", report, code, date),
				query = String.format("SELECT * FROM report WHERE p_id = '%s' and d_startday='%s'", code, date);
		try {
			ResultSet r = statement.executeQuery(query);
			r.last();
			if (r.getRow() > 0)
				statement.execute(sql);
			else
				insertPatientReport(code, report, date);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除患者诊断报告
	 * @param code
	 */
	public void deletePatientReport(String code, String date) {
		String 	sql = String.format("DELETE FROM report WHERE p_id='%s' and d_startday='%s'", code, date);
		try {
			statement.execute(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取患者信息列表
	 * @return
	 */
	public String[][] getPatientList() {
		String sql = "SELECT p_id, p_name, p_age FROM PATIENT ORDER BY p_id DESC";
		String[][] data = null;
		try {
			ResultSet r = statement.executeQuery(sql);
			
			r.last();
			data = new String[r.getRow()][3];
			
			r.beforeFirst();
			for (int i = 0; r.next(); i++) {
				data[i][0] = r.getString("p_id");
				data[i][1] = r.getString("p_name");
				data[i][2] = r.getString("p_age");
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 根据患者name或者id获取患者列表
	 * @param t 患者id
	 * @return
	 */
	public String[][] getPatientList(String t) {
		String sql = String.format("SELECT p_id, p_name, p_age FROM PATIENT WHERE p_name like '%%%1$s%%' OR p_id like '%%%1$s%%' ORDER BY p_id DESC", t);
		String[][] data = null;
		try {
			ResultSet r = statement.executeQuery(sql);
			
			r.last();
			data = new String[r.getRow()][3];
			
			r.beforeFirst();
			for (int i = 0; r.next(); i++) {
				data[i][0] = r.getString("p_id");
				data[i][1] = r.getString("p_name");
				data[i][2] = r.getString("p_age");
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 初始化cache
	 * @param code
	 */
	public void initCache(String code) {
		String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", code);
		try {
			ResultSet r = statement.executeQuery(sql);
			r.next();
			Cache.setAgeName(r.getInt("p_age"), r.getString("p_name"));
			Cache.setSex(r.getInt("p_sex"));
			Cache.setCode(r.getString("p_id"));
			if (Cache.getDate() == null) {
				sql = String.format("SELECT D_DATE FROM RECORD WHERE P_ID = '%s' AND D_COUNTER IS NOT NULL ORDER BY D_DATE DESC", code);
				r = statement.executeQuery(sql);
				r.next();
				if (r.getRow() != 0) {
					Cache.setDate(r.getString("d_date"));
					Cache.setEmpty(false);
				} else {
					Cache.setEmpty(true);
					Cache.setDate("--");
				}
			} else {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据患者ID、记录日期、时间、状态查询记录个数
	 * @param code
	 * @param d
	 * @param t
	 * @param status
	 * @return
	 */
	public int queryRecord(String code, Date d, Date t, int status) {
		String sql = String.format("SELECT * FROM RECORD WHERE p_id='%s' AND d_date = '%tF' AND d_time='%tT' and d_code=%d ", code, d, t, status);
		try {
			ResultSet r = statement.executeQuery(sql);
			r.last();
			return r.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/**
	 * 插入患者测量记录
	 * @param d_id
	 * @param p_id
	 * @param d_date
	 * @param d_time
	 * @param d_startday
	 * @param counter
	 * @param d_sbp
	 * @param d_abp
	 * @param d_dbp
	 * @param d_hb
	 * @param code
	 * @param vol
	 */
	public void insertRecord(int d_id, String p_id, Date d_date, Date d_time, long d_startday, String counter, int d_sbp, int d_abp, int d_dbp, int d_hb, int code, float vol) {
		String remark = "";
		switch (code) {
		case 100:
			remark = "手动测量";
			break;
		case 104:
			remark = "仪器关闭";
			break;
		default:
			break;
		}
		String sql = String.format("INSERT INTO record VALUES(%d,'%s', '%tF','%tT', %d, %s, %d, %d, %d, %d, %d, %f,%s)", 
				d_id, p_id, d_date, d_time, d_startday, counter, d_sbp, d_abp,d_dbp, d_hb, code,vol,Input.sqlStringFormat(remark));
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 对话框“打开患者”列表信息
	 * @param code
	 * @return
	 */
	public String[][] getDataList(String code) {
		String sql = String.format("SELECT reviewtime, count(d_counter) as count FROM RECORD,REVIEW WHERE p_id='%s' AND RECORD.d_startday = REVIEW.d_startday  GROUP BY record.d_startday", code);
		try {
			ResultSet r = statement.executeQuery(sql);
			
			r.last();
			String[][] data = new String[r.getRow()][2];
			r.beforeFirst();
			for (int i = 0; r.next(); i++) {
				data[i][0] = r.getString(1);
				data[i][1] = r.getString(2);
			}
			return data;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public String getSelectTime(String code, int k) {

		String sql = String.format("SELECT record.d_startday, count(d_counter) as count FROM RECORD,REVIEW WHERE p_id='%s' AND RECORD.d_startday = REVIEW.d_startday  GROUP BY record.d_startday limit %d,1", code, k);
		try {
			ResultSet r = statement.executeQuery(sql);
			
			r.last();
			return r.getString("d_startday");
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	
	}
	
	/**
	 * 窗口中数据图表
	 * @param code
	 * @param date
	 * @return
	 */
	public String[][] getStatistic(String code, String date) {
		String sql = String.format("select d_id,d_counter,d_date,d_time,d_sbp,d_abp,d_dbp,d_hb,d_code,d_remark from record where p_id='%s' and d_startday = '%s' order by d_id asc", code, date);
		try {
			ResultSet r = statement.executeQuery(sql);
			r.last();
			
			String[][] data = new String[r.getRow()][10];
			r.beforeFirst();
			for (int i = 0; r.next(); i++) {
				for (int j = 1; j <= 10; j++) {
					data[i][j - 1] = r.getString(j);
				}
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setRemark(int r, String code, String date, String re) {
		String sql = String.format("UPDATE RECORD SET d_remark = '%s' WHERE d_id = %d AND p_id = '%s' and d_startday = '%s'", re, r, code, date);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<RecordInitStruct> getStatisticByList(String code, String date){
		String sql = String.format("select * from record where p_id='%s' and d_startday = '%s' order by d_id asc", code, date);
		ArrayList<RecordInitStruct> list = new ArrayList<>();
		
		try {
			ResultSet r = statement.executeQuery(sql);
			while (r.next()) {
				RecordInitStruct record = new RecordInitStruct();
				record.record_id = r.getInt("d_id");
				record.record_counter = r.getInt("d_counter");
				record.record_date = r.getDate("d_date");
				record.record_time = r.getDate("d_time");
				record.record_sbp = r.getInt("d_sbp");
				record.record_abp = r.getInt("d_abp");
				record.record_dbp = r.getInt("d_dbp");
				record.record_hb = r.getInt("d_hb");
				record.record_code = r.getInt("d_code");
				record.record_voltage = r.getFloat("d_voltage");
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	
	
	/**
	 * 导出患者数据到Excel
	 * @param code
	 * @param date
	 */
	public void exportExcel(String code, String date, String file) {
		String sql = String.format("select d_counter,d_date,d_time,d_sbp,d_abp,d_dbp,d_hb,d_code from record where p_id='%s' and d_startday = '%s' order by d_id asc", code, date);
		try {
			
			String[] header = {"编号", "日期", "时间", "收缩压", "舒张压", "平均压", "心率", "备注"};
			
	        @SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook();
	        XSSFSheet sheet = wb.createSheet("title");
	        sheet.setDefaultColumnWidth(12);
	        
	        CellStyle style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        
	        XSSFRow titleRow = sheet.createRow(0);
	        for (int i = 0; i < header.length; i++) {
	        	Cell cell = titleRow.createCell(i);
	        	cell.setCellStyle(style);
	            cell.setCellValue(header[i]);
	        }
			
			ResultSet r = statement.executeQuery(sql);
			r.last();
			
			r.beforeFirst();
			for (int i = 0; r.next(); i++) {
				XSSFRow row = sheet.createRow(i + 1);
				row.setRowStyle(style);
				for (int j = 1; j <= 8; j++) {
					Cell cell = row.createCell(j - 1);
		        	cell.setCellStyle(style);
					cell.setCellValue(r.getString(j));
				}
			}
			FileOutputStream fo;
			if (file.matches(".+\\.xlsx"))
				fo = new FileOutputStream(new File(file));

			else
				fo = new FileOutputStream(new File(file + ".xlsx"));
	        wb.write(fo);
	        fo.flush();
	        fo.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除某天数据
	 * @param code
	 * @param date
	 */
	public void deleteStatistic(String code, String date) {
		String sql = String.format("DELETE FROM record WHERE p_id='%s' AND d_startday='%s'", code, date);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 配置信息获取
	 * @return
	 */
	public Configuration getConfiguration() {
		
		try {
			
			String sql = "SELECT * FROM configuration";
			ResultSet r = statement.executeQuery(sql);
			
			r.last();
			Configuration c = null;
			c = new Configuration(
					r.getString("filenameitem"),
					r.getString("reportitem"),
					r.getString("title"),
					r.getString("prefix"), 
					r.getInt("width"), 
					r.getInt("counter"), 
					r.getTime("daystart"), 
					r.getTime("nightStart"), 
					r.getInt("daysystolic"), 
					r.getInt("daydiastolic"),
					r.getInt("nightsystolic"),
					r.getInt("nightdiastolic"),
					r.getDouble("standmbps"),
					r.getString("customitem1"),
					r.getString("customitem2"),
					r.getString("filenamesplit1"),
					r.getString("filenamesplit2"));
			return c;

		} catch (SQLException e) {
			e.printStackTrace();
			return new Configuration();
		}
	}
	
	public void setFileNameItem(String s) {
		String sql = String.format("UPDATE configuration SET filenameitem = '%s'", s);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setCustomItems(String s1, String s2) {
		String sql = String.format("UPDATE configuration SET customitem1 = '%s', customitem2 = '%s'", s1, s2);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setFileNameSplit(String s1, String s2) {
		String sql = String.format("UPDATE configuration SET filenamesplit1 = '%s', filenamesplit2 = '%s'", s1, s2);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setStandMBPS(String s) {
		String sql = String.format("UPDATE configuration SET standmbps = '%s'", s);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setReportItem(String s) {
		String sql = String.format("UPDATE configuration SET reportitem = '%s'", s);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setTitle(String s) {
		String sql = String.format("UPDATE configuration SET title = '%s'", s);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setDayNight(Date d, Date n) {
		String sql = String.format("UPDATE configuration SET dayStart = '%tT', nightStart = '%tT'", d, n);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void set_SBP_DBP(String ds, String dd, String ns, String nd) {
		String sql = String.format("UPDATE configuration SET daysystolic=%s, daydiastolic=%s, nightsystolic=%s, nightdiastolic=%s", ds, dd, ns, nd);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getCOM() {
		String sql = "SELECT * FROM configuration";
		try {
			ResultSet r = statement.executeQuery(sql);
			r.last();
			return r.getString("COM");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setCOM(String c) {
		String sql = String.format("UPDATE configuration SET COM = '%s'", c);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setReviewTime(long timestamp, Date calendar) {
		String sql = String.format("insert into review values(%s,'%tF')", timestamp, calendar);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public String getReviewTime(String timestamp) {
		String sql = String.format("select * from review where d_startday=%s", timestamp);
		ResultSet r;
		try {
			r = statement.executeQuery(sql);
			r.last();
			return r.getString("reviewtime");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 软件配置表中counter自加
	 */
	public void setCounter() {
		try {
			statement.executeUpdate("UPDATE configuration SET counter = counter + 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setEncode(String prefix, int w, String start) {
		String sql = "UPDATE CONFIGURATION SET PREFIX = '%s', WIDTH = %d, COUNTER = %s";
		try {
			statement.execute(String.format(sql, prefix, w, start));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int startCheck(String prefix, int w) {
		String sql = "SELECT * FROM PATIENT WHERE P_ID REGEXP '%s[0123456789]{%d}' ORDER BY P_ID DESC";
		try {
			ResultSet r = statement.executeQuery(String.format(sql, prefix, w));
			r.next();
			if (r.getRow() > 0) {
				String pattern = String.format("%s([0123456789]{%d})", prefix, w);
				Pattern pat = Pattern.compile(pattern);
				Matcher m = pat.matcher(r.getString("p_id"));
				if (m.find()) {
					return 1 + Integer.parseInt(m.group(1));
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public void setAutoCoding(boolean flag) {
		String sql = String.format("UPDATE configuration SET auto = %d", flag ? 1 : 0);
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public int getAutoCoding() {
		String sql = "SELECT * FROM configuration";
		try {
			ResultSet r = statement.executeQuery(sql);
			r.next();
			return r.getInt("AUTO");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}
	
	public boolean isConnected() {
		return is_Connected;
	}
	
	public void close() {
		try {
			if (connection != null && connection.isClosed() == false)
				connection.close();
			if (statement != null && statement.isClosed() == false)
				statement.close();
			
			connection = null;
			statement = null;
			is_Connected = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		close();
	}


	/**
		 * 导出患者信息到xml
		 * @param code	患者id
		 * @param file	文件路径
		 */
		public void exportPatient(String code, String file) {
			
			String sql = String.format("SELECT * FROM patient WHERE p_id = '%s'", code);
			
			try {
				
				ResultSet r = statement.executeQuery(sql);
				
				r.next();
				
				Document document = DocumentHelper.createDocument();
				Element patients = document.addElement("patinets_list");
				Element patient = patients.addElement("patient");
				
				Element	key = patient.addElement("key"),
						basic_info = patient.addElement("basic_info"),
						contact_info = patient.addElement("contact_info"),
						moreinfo = patient.addElement("moreinfo");
				
				key.setText(r.getString("p_id"));
				
				Element name = basic_info.addElement("name"),
						age = basic_info.addElement("age"),
						sex = basic_info.addElement("gender"),
						height = basic_info.addElement("height"),
						weight = basic_info.addElement("weight");
				name.setText(r.getString("p_name"));
				age.setText(r.getString("p_age"));
				sex.setText(r.getString("p_sex"));
				
				if (r.getString("p_height") != null)
					height.setText(r.getString("p_height"));
				if (r.getString("p_weight") != null)
					weight.setText(r.getString("p_weight"));
				
				Element phone = contact_info.addElement("phone"),
						postcode = contact_info.addElement("postcode"),
						addr = contact_info.addElement("address");
				if (r.getString("p_phone") != null)
					phone.setText(r.getString("p_phone"));
				if (r.getString("p_phone") != null)
					postcode.setText(r.getString("p_phone"));
				if (r.getString("p_address") != null)
					addr.setText(r.getString("p_address"));
				
				Element department = moreinfo.addElement("department"),
						bed = moreinfo.addElement("bed"),
						medins = moreinfo.addElement("medicalinsurance"),
						clinic = moreinfo.addElement("clinic"),
						hospital = moreinfo.addElement("hospital"),
						medrecord = moreinfo.addElement("medicalrecird");
				if (r.getString("p_department") != null) {
					department.setText(r.getString("p_department"));
				}
				if (r.getString("p_bed") != null) {
					bed.setText(r.getString("p_bed"));
				}
				if (r.getString("p_medins") != null) {
					medins.setText(r.getString("p_medins"));
				}
				if (r.getString("p_clinic") != null) {
					clinic.setText(r.getString("p_clinic"));
				}
				if (r.getString("p_hospital") != null) {
					hospital.setText(r.getString("p_hospital"));
				}
				if (r.getString("p_medrecord") != null) {
					medrecord.setText(r.getString("p_medrecord"));
				}
				
				
				OutputFormat format = OutputFormat.createPrettyPrint();
		        format.setEncoding("UTF-8");
		        		        
				
		        XMLWriter writer;
				try {
					if (file.matches(".+\\.xml"))
						writer = new XMLWriter(new FileOutputStream(new File(file)), format);
					else
						writer = new XMLWriter(new FileOutputStream(new File(file + ".xml")), format);
					writer.write(document);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void addCounter(String d_id,String val, String code, String date) {
			try {
				statement.executeUpdate(String.format("update record set d_counter=%s where d_id=%s and p_id = '%s' and d_startday = '%s'", val, d_id, code,date));
				statement.executeUpdate(String.format("update record set d_counter=d_counter+1 where d_id>%s and p_id = '%s' and d_startday = '%s'", d_id, code,date));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void minusCounter(String counter, String code, String date) {
			try {
				statement.executeUpdate(String.format("update record set d_counter = null where p_id = '%s' and d_startday = '%s' and d_counter = %s", code, date, counter));
				statement.executeUpdate(String.format("update record set d_counter=d_counter-1 where p_id = '%s' and d_startday = '%s' and d_counter > %s", code, date, counter));				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


}
