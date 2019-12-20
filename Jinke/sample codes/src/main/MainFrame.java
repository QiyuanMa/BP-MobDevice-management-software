package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;

import com.itextpdf.text.DocumentException;

import database.H2_DB;
import dialog.DataListDialog;
import dialog.DayNightDialog;
import dialog.DeviceDialog;
import dialog.EncodeDialog;
import dialog.FileNameDialog;
import dialog.MessageDialog;
import dialog.MyImplement;
import dialog.NewDialog;
import dialog.OpenDialog;
import dialog.PatientInfoDialog;
import dialog.StandardDialog;
import panels.Charts;
import panels.Statistic;
import panels.Print;
import panels.Report;
import resources.MyFont;
import xTool.Cache;
import xTool.Configuration;
import xTool.Device;
import xTool.PatientInitStruct;

public class MainFrame extends JFrame implements ActionListener, ComponentListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6180549581204532096L;
	
	private Header header = null;
	public Footer footer = new Footer(this);
	
	// 窗口、控件
	private Statistic statistic;
	private Charts charts;
	private Print print;
	private Report report;
	
	// 图标按钮
	public JButton[] buttons = new JButton[10];
	
	// 菜单栏、菜单及菜单项
	private JMenuBar bar = new JMenuBar();
	private JMenu[] menus = new JMenu[6];
	public JMenuItem[] its = new JMenuItem[17];
	
	// 菜单项索引
	private static final int	File_Import_Patient = 0,
								File_Store_Data 	= 1,
								File_Restore_Data 	= 2,
								File_Exit 			= 3,
								Patient_New 		= 4,
								Patient_List 		= 5,
								Patient_Export 		= 6,
								Patient_Delete 		= 7,
								Data_Export_XML		= 8,
								Data_Export_Excel 	= 9,
								Data_Delete 		= 10,
								Setting_Implement 	= 11,
								Setting_Day_Night 	= 12,
								Setting_Standard 	= 13,
								Setting_Encode		= 14,
								Setting_FileName	= 15,
								Help_About 			= 16;
	// 工具栏索引项
	private static final int	Tool_New			= 0,
								Tool_Open			= 1,
								Tool_Info			= 2,
								Tool_Upload			= 3,
								Tool_Download		= 4,
								Tool_Datalist		= 5,
								Tool_Statistic		= 6,
								Tool_Paint			= 7,
								Tool_Report			= 8,
								Tool_Print			= 9;
								
	
	
	// 字体，默认格式
	private MyFont font = new MyFont();
	
	public MainFrame() {
		
		// 设置窗口标题及布局方式
		super("北京世纪今科动态血压监测软件");
		this.setLayout(new BorderLayout());
		
		// 获取当前屏幕尺寸
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		
		// 设置窗口位置
		int x = (int)(width * 0.2);
		int y = (int)(height * 0.2);
		this.setBounds(x, y, (int)(width * 0.6), (int)(height * 0.6));
		
		// 设置窗口图标
		setIconImage(getToolkit().getImage("./icon/Jinco.png"));

		// 设置菜单栏及菜单
		bar.setOpaque(true);
		String[] t1 = {" 文件 ", " 患者 ", " 测量数据 ", " 设置 ", " 帮助 ", " 数据备份 "};
		for (int i = 0; i < menus.length - 1; i++) {
			menus[i] = new JMenu(t1[i]);
			menus[i].setFont(font.getFont());
			menus[i].setFont(menus[i].getFont().deriveFont((float) 15));

			bar.add(menus[i]);
		}
		
		// 设置菜单项
		String[] t2 = {" 导入患者 ", " 存储数据 ", " 恢复数据 "," 退出 ", " 新建患者 ", " 患者列表 ", " 导出患者 ", " 删除患者 ", " 导出详细信息", " 导出Excel ", " 删除数据 ", " 设备端口 ", " 时间设置 ", " 血压正常范围 ", " 自动编号设置", " 文件名称设置", " 关于  "};
		for (int i = 0; i < its.length; i++) {
			its[i] = new JMenuItem(t2[i]);
			its[i].setFont(font.getFont());
			its[i].setFont(its[i].getFont().deriveFont((float)15));
			its[i].addActionListener(this);
		}
		
		// 菜单添加
		menus[0].add(its[File_Import_Patient]);
		menus[5] = new JMenu(t1[5]);
		menus[5].setFont(font.getFont());
		menus[5].setFont(menus[5].getFont().deriveFont((float) 15));
		menus[0].add(menus[5]);
		menus[5].add(its[File_Store_Data]);
		menus[5].add(its[File_Restore_Data]);
		menus[0].add(its[File_Exit]);

		menus[1].add(its[Patient_New]);
		menus[1].add(its[Patient_List]);
		menus[1].add(its[Patient_Export]);
		menus[1].add(its[Patient_Delete]);
		
		menus[2].add(its[Data_Export_XML]);
		menus[2].add(its[Data_Export_Excel]);
		menus[2].add(its[Data_Delete]);
		
		menus[3].add(its[Setting_Implement]);
		menus[3].add(its[Setting_Day_Night]);
		menus[3].add(its[Setting_Standard]);
		menus[3].add(its[Setting_Encode]);
		menus[3].add(its[Setting_FileName]);
		
		menus[4].add(its[Help_About]);
		
		// 按钮设置
		ImageIcon[] icon = new ImageIcon[10];
		icon[0] = new ImageIcon("./icon/new.png");
		icon[1] = new ImageIcon("./icon/open.png");
		icon[2] = new ImageIcon("./icon/info.png");		
		icon[3] = new ImageIcon("./icon/upload.png");
		icon[4] = new ImageIcon("./icon/download.png");
		icon[5] = new ImageIcon("./icon/list.png");
		icon[6] = new ImageIcon("./icon/table.png");
		icon[7] = new ImageIcon("./icon/chart.png");
		icon[8] = new ImageIcon("./icon/result.png");
		icon[9] = new ImageIcon("./icon/print.png");
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(icon[i]);
			buttons[i].setBackground(Color.DARK_GRAY);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setOpaque(true);
			buttons[i].addActionListener(this);
			if(i >= Tool_Datalist) {
				buttons[i].setVisible(false);
			}
		}
		buttons[Tool_Info].setVisible(false);
		
		buttons[0].setToolTipText("新建患者");
		buttons[1].setToolTipText("打开患者");
		buttons[2].setToolTipText("患者信息");
		buttons[3].setToolTipText("上传");
		buttons[4].setToolTipText("下载");
		buttons[5].setToolTipText("测量列表");
		buttons[6].setToolTipText("测量数据表单");
		buttons[7].setToolTipText("图表");
		buttons[8].setToolTipText("报告");
		buttons[9].setToolTipText("打印");
		
		its[Patient_Export].setEnabled(false);
		its[Patient_Delete].setEnabled(false);
		its[Data_Export_XML].setEnabled(false);
		its[Data_Export_Excel].setEnabled(false);
		its[Data_Delete].setEnabled(false);
		
		header = new Header(bar, buttons);
		this.add(BorderLayout.NORTH, header);
		this.add(BorderLayout.SOUTH, footer);
		
		this.validate();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		H2_DB h2_DB = new H2_DB();
		Cache.setCom(h2_DB.getCOM());
		if (Device.deviceIndentify(Cache.getCom())) {
			footer.setOk();
		}
		else {
			footer.setError();
		}
		h2_DB.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == its[Patient_New] || e.getSource() == buttons[Tool_New]) {
			/**
			 * 新建患者
			 */
			new NewDialog(this, this.getLocation(), getSize());
			
			
		} else if (e.getSource() == its[Patient_List] || e.getSource() == buttons[Tool_Open]) {
			/**
			 * 患者列表
			 */
			
			OpenDialog openDialog = new OpenDialog(this, this.getLocation(), getSize());
			String key = openDialog.showDialog();
			if (key != null) {
				closeAll();
				H2_DB h2_DB = new H2_DB();
				Cache.setDate(null);
				
				h2_DB.initCache(key);
				h2_DB.close();
				buttons[Tool_Info].setVisible(true);
				buttons[Tool_Datalist].setVisible(true);
				footer.showPatient();
				for(int i = Tool_Statistic; i < buttons.length; i++) {
					buttons[i].setVisible(false);
				}
				its[Patient_Export].setEnabled(true);
				its[Patient_Delete].setEnabled(true);

			}

		} else if (e.getSource() == buttons[Tool_Info]) {
			/**
			 * 患者信息
			 */
			PatientInfoDialog patientInfoDialog = new PatientInfoDialog(this, getLocation(), getSize(), Cache.getCode());
			int key = patientInfoDialog.showDialog();
			if (key == 2) {
				closeAll();
				buttons[Tool_Info].setVisible(false);
				footer.clear();
				for(int i = Tool_Datalist; i < buttons.length; i++) {
					buttons[i].setVisible(false);
				}
				its[Patient_Export].setEnabled(false);
				its[Patient_Delete].setEnabled(false);
				Cache.setCode(null);
				Cache.setDate(null);
				
			} else {
				footer.showPatient();
			}
			// 显示状态栏
			
		} else if (e.getSource() == its[File_Exit]) {
			/**
			 * 退出
			 */
			this.dispose();
			// 显示状态栏
			
		} else if (e.getSource() == its[Setting_Standard]) {
			/**
			 * 血压标准
			 */
			new StandardDialog(this, getLocation(), getSize());
			
		} else if (e.getSource() == its[Setting_FileName]) {
			/**
			 * 文件名
			 */
			new FileNameDialog(this, getLocation(), getSize());
			
		} else if (e.getSource() == its[Setting_Day_Night]) {
			/**
			 * 设置白天夜晚开始时间
			 */
			new DayNightDialog(this, getLocation(), getSize());
			
		} else if (e.getSource() == buttons[Tool_Datalist]) {
			/**
			 * 患者数据列表
			 */
			DataListDialog dataListDialog = new DataListDialog(this, getLocation(), getSize(), Cache.getCode());
			if (dataListDialog.showDialog() == 1) {
//				footer.showPatientWithDate();
				footer.showPatient();
				its[Data_Export_XML].setEnabled(true);
				its[Data_Export_Excel].setEnabled(true);
				its[Data_Delete].setEnabled(true);
				for(int i = 5; i < buttons.length; i++) {
					buttons[i].setVisible(true);
				}
				addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
				validate();

			}
			
		} else if (e.getSource() == buttons[Tool_Statistic]) {
			/**
			 * 患者数据表
			 */
			this.addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
			
		} else if (e.getSource() == buttons[Tool_Paint]) {
			/**
			 * 绘图
			 */
			closeAll();
			try {
				charts = new Charts(this, statistic.getTable());
			} catch (NumberFormatException | ParseException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			this.add(charts);
			this.revalidate();
			
		} else if (e.getSource() == buttons[Tool_Report]) {
			/**
			 * 报告
			 */
			closeAll();
			try {
				report = new Report(this, statistic.getTable());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(report);
			this.revalidate();
		} else if (e.getSource() == buttons[Tool_Print]) {
			/**
			 * 打印
			 */
			closeAll();
			try {
				print = new Print(this, statistic.getTable());
			} catch (DocumentException | IOException | ParseException e1) {
				e1.printStackTrace();
			}
			this.add(print);
			this.revalidate();
			
		} else if (e.getSource() == buttons[Tool_Upload]) {
			/**
			 * 设备测量协议设置
			 */
			if (Device.deviceIndentify(Cache.getCom())) {
				new DeviceDialog(this, getLocation(), getSize());
			} else {
				MessageDialog.showError(this, "端口打开失败，请重新连接！");
			}
			
		} else if (e.getSource() == its[Setting_Implement]) {
			/**
			 * 盒子接口
			 */
			new MyImplement(this, getLocation(), getSize());
			if (Device.deviceIndentify(Cache.getCom())) {
				footer.setOk();
			}
			else {
				footer.setError();
			}
			
		} else if (e.getSource() == buttons[Tool_Download]) {
			/**
			 * 盒子内容读取
			 */
			if (Device.deviceIndentify(Cache.getCom())) {
				Device device = new Device(Cache.getCom());
				H2_DB h2_DB = new H2_DB();
				String idInBox = device.readPatientKey();
				int err = -1;
				if (Cache.getCode() == null) {
					if (h2_DB.checkPatientID(idInBox) == false) {
						if (MessageDialog.showConfirm(this, "当前未打开患者!\r\n已在数据库中检索到与记录盒中编号(" + idInBox + ")相匹配的患者，\r\n是否进行数据读取？") == 0) {
							err = device.read_measurement(idInBox);
						}
					} else {
						MessageDialog.showError(this, "当前未打开患者且数据库中不存在与记录盒中编号相匹配的患者！\r\n请新建患者或打开某一患者进行数据读取！");
					}
					
				} else if (Cache.getCode().equals(idInBox)) {
					err = device.read_measurement(idInBox);

				} else {
					if (MessageDialog.showConfirm(this, "记录盒中患者编号：" + idInBox + "与当前打开患者编号不一致，是否将数据读入当前患者？") == 0) {
						err = device.read_measurement(Cache.getCode());
					} else {
						MessageDialog.showMessage(this, "记录盒读取操作终止。\r\n请打开正确患者后进行操作！");
					}
				}
				switch (err) {
				case 0:
					if (MessageDialog.showConfirm(this, "记录盒中数据回放成功，是否删除盒中数据？") == 0) {
						device.earse_mearsurement();
						System.out.println("删除数据！");
					}
					break;
				case 1:
					MessageDialog.showError(this, "记录盒Hash区域更新失败！");
					break;
				case 2:
					MessageDialog.showError(this, "指令加密失败！");
					break;
				case 3:
					MessageDialog.showError(this, "记录盒中记录条数获取失败！");
					break;
				case 4:
					MessageDialog.showError(this, "记录盒中记录获取失败！");
					break;
				case 5:
					MessageDialog.showError(this, "记录盒中无数据！");
					break;
				default:
					break;
				}
				System.out.println("关闭串口！");
				device.close();
				h2_DB.close();

			} else {
				MessageDialog.showError(this, "端口打开失败，请重新连接！");

			}
		} else if (e.getSource() == its[Data_Delete]) {
			/**
			 * 删除数据
			 */
			if (MessageDialog.showConfirm(null, "确认删除？\r\n本操作将删除此次测量所有数据！") == 0) {
				H2_DB h2_DB = new H2_DB();
				h2_DB.deleteStatistic(Cache.getCode(), Cache.getDate());
				h2_DB.close();
				this.addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
				
				for(int i = 5; i < buttons.length; i++) {
					buttons[i].setVisible(false);
				}
				closeAll();

			}
			
		} else if (e.getSource() == its[Patient_Delete]) {
			/**
			 * 删除患者
			 */
			if (MessageDialog.showConfirm(null, "确认删除？本操作将删除此患者所有数据！") == 0) {
				H2_DB h2_DB = new H2_DB();
				h2_DB.deletePatient(Cache.getCode());
				h2_DB.close();
				footer.clear();
				for(int i = 4; i < buttons.length; i++) {
					buttons[i].setVisible(false);
				}
				its[Patient_Export].setEnabled(false);
				its[Patient_Delete].setEnabled(false);
				closeAll();

			}
			
		} else if (e.getSource() == its[Patient_Export]) {
			/**
			 * 导出患者信息到xml
			 */
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}
			JFileChooser chooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			chooser.setCurrentDirectory(fsv.getHomeDirectory());
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			H2_DB h2_DB = new H2_DB();
			PatientInitStruct p = h2_DB.getPatient(Cache.getCode());
			h2_DB.close();
			chooser.setSelectedFile(new File(p.patient_id + "_" + p.patient_name + ".xml"));
			chooser.setFileFilter(new FileNameExtensionFilter("xml文件", "xml"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				h2_DB = new H2_DB();
				h2_DB.exportPatient(Cache.getCode(), chooser.getSelectedFile().getPath());
				h2_DB.close();
				MessageDialog.showMessage(null, "导出成功！");
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}

			
		} else if (e.getSource() == its[File_Import_Patient]) {
			/**
			 * 导入患者信息
			 */
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setFont(font.getFont());
			fileChooser.setApproveButtonText("OK");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("xml文件", "xml"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				H2_DB h2_DB = new H2_DB();
				
				switch (h2_DB.importPatient(fileChooser.getSelectedFile())) {
				case -1:
					MessageDialog.showError(this, "患者信息导入失败！");
					break;
				case 1:
					MessageDialog.showMessage(this, "导入成功！");
					break;

				default:
					break;
				}
				h2_DB.close();
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}

			
		}
		else if (e.getSource() == its[Data_Export_Excel]) {
			/**
			 * 导出患者信息到Excel
			 */
			
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setFont(font.getFont());
			fileChooser.setApproveButtonText("OK");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件（2007或以上版本）（*.xlsx）", "xlsx"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			H2_DB h2 = new H2_DB();
			Configuration conf = h2.getConfiguration();
			String[] filename = conf.getFileNameItem();
			String[] split = conf.getFileNameSplit();
			fileChooser.setSelectedFile(new File(filename[0] + split[0] + filename[1] + split[1] + filename[2] + ".xlsx"));
			h2.close();
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

				H2_DB h2_DB = new H2_DB();
				h2_DB.exportExcel(Cache.getCode(), Cache.getDate(), fileChooser.getSelectedFile().getPath());
				h2_DB.close();
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == its[Help_About]) {
			UIManager.put("OptionPane.buttonFont", new FontUIResource(new MyFont(0, 14).getFont()));
			UIManager.put("OptionPane.messageFont", new FontUIResource(new MyFont(0, 14).getFont()));
			JOptionPane.showMessageDialog(this, "北京世纪今科动态血压监测软件\r\nv 0.3.3 Beta", "关于我们", JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == its[File_Store_Data]) {
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setFont(font.getFont());
			fileChooser.setApproveButtonText("保存");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("XML文件", "xml"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

				H2_DB h2_DB = new H2_DB();
				h2_DB.store(fileChooser.getSelectedFile().getAbsolutePath());
				h2_DB.close();
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}

		} else if (e.getSource() == its[File_Restore_Data]) {
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setFont(font.getFont());
			fileChooser.setApproveButtonText("打开");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("XML文件", "xml"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

				H2_DB h2_DB = new H2_DB();
				h2_DB.restore(fileChooser.getSelectedFile());
				h2_DB.close();
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == its[Setting_Encode]) {
			new EncodeDialog(this, getLocation(), getSize());
		} else if (e.getSource() == its[Data_Export_XML]) {
			// 导出患者详细信息+数据+计算变量
			LookAndFeel origenal = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e2) {
				e2.printStackTrace();
			}

			JFileChooser fileChooser = new JFileChooser();
			FileSystemView fsv = FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setFont(font.getFont());
			fileChooser.setApproveButtonText("保存");
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("XML文件", "xml"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			H2_DB h2 = new H2_DB();
			Configuration conf = h2.getConfiguration();
			String[] filename = conf.getFileNameItem();
			String[] split = conf.getFileNameSplit();
			fileChooser.setSelectedFile(new File(filename[0] + split[0] + filename[1] + split[1] + filename[2] + ".xml"));
			h2.close();
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

				H2_DB h2_DB = new H2_DB();
				try {
					h2_DB.exportPatientAndRecord(Cache.getCode(), fileChooser.getSelectedFile().getAbsolutePath(), statistic.getTable());
				} catch (ParseException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				h2_DB.close();
				MessageDialog.showMessage(this, "导出成功！");
			}
			try {
				UIManager.setLookAndFeel(origenal);
				SwingUtilities.updateComponentTreeUI(this);
				if(charts != null)
					charts.getTabbedPane().setUI(new model.TabbedPaneUI("000000", "000000"));
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	public void addstatistic(Statistic s) {
		closeAll();
		statistic = s;
		this.add(BorderLayout.CENTER, statistic);
		this.validate();
	}
	
	public void closeAll() {
		if (statistic != null) {
			remove(statistic);
		}
		if (charts != null) {
			remove(charts);
		}
		if (print != null) {
			remove(print);
		}
		if (report != null && report.getPanel() != null) {
			remove(report.getPanel());
		}
		if (report != null) {
			remove(report);
		}
		SwingUtilities.updateComponentTreeUI(this);
		validate();
	}
}
