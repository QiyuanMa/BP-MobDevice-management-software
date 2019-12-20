package dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import database.H2_DB;
import resources.MyFont;
import xTool.Configuration;
import xTool.Input;
import xTool.PatientInitStruct;


public class NewDialog extends JDialog implements ActionListener, KeyListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 287496242069676388L;

	// 患者基本信息 通讯信息及附加诊断报告
	private	JPanel	basic_info		= new JPanel(), 
					contact_info	= new JPanel(),
					hospitalization = new JPanel();
	
	// 文本框，其中第2个未使用
	private JTextField[] tx = new JTextField[16];
	// 常用常量
	private static final int	Code 		= 0,
								Name 		= 1,
								Sex 		= 2,
								Age 		= 3,
								Birth		= 4,
								Height 		= 5,
								Weight 		= 6,
								Phone 		= 7,
								Postcode	= 8,
								Address 	= 9,
								Department	= 10,
								Bed			= 11,
								MedIns		= 12,
								Clinic		= 13,
								Hospital	= 14,
								MedRecord	= 15;
	// BMI
	JLabel bmi = new JLabel("BMI：--");
	// 自动编号控制
	JCheckBox auto = new JCheckBox("自动编号");

	// 默认格式字体
	private MyFont font = new MyFont();
	
	// 性别选择下拉类表表单项
	private static final String[] Sex_List = {" -", " 男", " 女"};
	private JComboBox<String> sex = new JComboBox<>(Sex_List);
	
	// 按键及常用常量
	private JButton[] buttons = new JButton[3];
	private static final int	Button_Ok 		= 0,
								Button_Clear 	= 1,
								Button_Close	= 2;
	private JButton datePick = new JButton(new ImageIcon("./icon/calendar.png"));
	
	// 数据库
	H2_DB h2 = new H2_DB();
	
	/**
	 * 构造函数
	 * @param f	窗口
	 * @param p 位置信息
	 * @param d 大小信息
	 */
	public NewDialog(JFrame f, Point p, Dimension d) {
		
		// 父类构造函数，有模式对话框
		super(f, "新建患者信息", true);
		
		// 无布局，不可伸缩
		setLayout(null);
		setResizable(false);
		
		// 固定大小
		int w = 800, h = 650;
		setBounds(p.x + (d.width - w) / 2, p.y + (d.height - h) / 2, w, h);
		
		// 文本标签设置
		JLabel[] label_list = new JLabel[16];
		String[] t = {
				"编号 ：", "姓名 ：", "性别 ：", "年龄 ：", "出生日期 ：", "身高(cm) ：", "体重(kg) ：",
				"联系方式 ：", "邮政编码 ：", "通讯地址 ：", 
				"科室：", "床位：", "医保编号：", "门诊编号：", "住院号：", "病案号："
		};
		
		for (int i = 0; i < label_list.length; i++) {
			// 文本标签初始化
			label_list[i] = new JLabel(t[i]);
			label_list[i].setFont(font.getFont());
			
			// 文本框初始化
			tx[i] = new JTextField();
			tx[i].setFont(font.getFont());
			tx[i].setHorizontalAlignment(JTextField.CENTER);
			tx[i].setSelectedTextColor(Color.WHITE);
			tx[i].setSelectionColor(new Color(148, 207, 91));
			
		}
		
		// 基本信息区初始化
		Border tb1 = BorderFactory.createTitledBorder(new LineBorder(Color.DARK_GRAY), "基本信息", TitledBorder.LEFT,  TitledBorder.DEFAULT_POSITION, font.getFont());
		basic_info.setBorder(tb1);
		
		this.add(basic_info);
		basic_info.setBounds(80, 10, 640, 180);
		
		basic_info.setLayout(null);
		
		basic_info.add(label_list[Code]);
		basic_info.add(tx[Code]);
		label_list[Code].setBounds(50, 30, 60, 30);
		tx[Code].setBounds(140, 30, 200, 30);

		// 复选框自动编号设置
		basic_info.add(auto);
		auto.setFont(font.getFont());
		auto.setBounds(380, 30, 100, 30);
		auto.addActionListener(this);
		
		basic_info.add(label_list[Name]);
		basic_info.add(tx[Name]);
		label_list[Name].setBounds(50, 65, 60, 30);
		tx[Name].setBounds(140, 65, 130, 30);
		
		basic_info.add(label_list[Sex]);
		basic_info.add(sex);
		sex.setFont(font.getFont());
		label_list[Sex].setBounds(320, 65, 60, 30);
		sex.setBounds(380, 65, 60, 30);
		
		basic_info.add(label_list[Age]);
		basic_info.add(tx[Age]);
		label_list[Age].setBounds(470, 65, 60, 30);
		tx[Age].setBounds(530, 65, 70, 30);
		
		basic_info.add(label_list[Birth]);
		basic_info.add(tx[Birth]);
		label_list[Birth].setBounds(50, 100, 90, 30);
		tx[Birth].setBounds(140, 100, 200, 30);
		tx[Birth].setEditable(false);

		
		basic_info.add(label_list[Height]);
		basic_info.add(tx[Height]);
		label_list[Height].setBounds(50, 135, 95, 30);
		tx[Height].setBounds(140, 135, 80, 30);
		
		basic_info.add(label_list[Weight]);
		basic_info.add(tx[Weight]);
		label_list[Weight].setBounds(250, 135, 90, 30);
		tx[Weight].setBounds(340, 135, 80, 30);
		
		// 添加按键事件响应
		tx[Height].addKeyListener(this);
		tx[Weight].addKeyListener(this);
		
		basic_info.add(bmi);
		bmi.setFont(font.getFont());
		bmi.setBounds(450, 135, 100, 30);
		
		basic_info.validate();
		
		// 通讯信息区初始化
		Border tb2 = BorderFactory.createTitledBorder(new LineBorder(Color.DARK_GRAY), "联系信息", TitledBorder.LEFT,  TitledBorder.DEFAULT_POSITION , font.getFont());
		contact_info.setBorder(tb2);
		
		this.add(contact_info);
		contact_info.setBounds(80, 190, 640, 145);
		
		contact_info.add(label_list[Phone]);
		contact_info.add(tx[Phone]);
		label_list[Phone].setBounds(50, 30, 100, 30);
		tx[Phone].setBounds(150, 30, 180, 30);
		
		contact_info.add(label_list[Postcode]);
		contact_info.add(tx[Postcode]);
		label_list[Postcode].setBounds(50, 65, 100, 30);
		tx[Postcode].setBounds(150, 65, 180, 30);
		
		contact_info.add(label_list[Address]);
		contact_info.add(tx[Address]);
		label_list[Address].setBounds(50, 100, 100, 30);
		tx[Address].setBounds(150, 100, 400, 30);
		
		contact_info.setLayout(null);
		contact_info.validate();
		
		// 登记信息
		Border tb3 = BorderFactory.createTitledBorder(new LineBorder(Color.DARK_GRAY), "登记信息", TitledBorder.LEFT,  TitledBorder.DEFAULT_POSITION , font.getFont());
		hospitalization.setBorder(tb3);
		
		this.add(hospitalization);
		hospitalization.setBounds(80, 335, 640, 215);
		hospitalization.setLayout(null);
		
		hospitalization.add(tx[Department]);
		hospitalization.add(label_list[Department]);
		label_list[Department].setBounds(50, 30, 60, 30);
		tx[Department].setBounds(140, 30, 120, 30);
		
		hospitalization.add(tx[Bed]);
		hospitalization.add(label_list[Bed]);
		label_list[Bed].setBounds(320, 30, 60, 30);
		tx[Bed].setBounds(380, 30, 100, 30);

		hospitalization.add(tx[MedIns]);
		hospitalization.add(label_list[MedIns]);
		label_list[MedIns].setBounds(50, 65, 90, 30);
		tx[MedIns].setBounds(140, 65, 250, 30);

		hospitalization.add(tx[Clinic]);
		hospitalization.add(label_list[Clinic]);
		label_list[Clinic].setBounds(50, 100, 90, 30);
		tx[Clinic].setBounds(140, 100, 250, 30);

		hospitalization.add(tx[Hospital]);
		hospitalization.add(label_list[Hospital]);
		label_list[Hospital].setBounds(50, 135, 90, 30);
		tx[Hospital].setBounds(140, 135, 250, 30);

		hospitalization.add(tx[MedRecord]);
		hospitalization.add(label_list[MedRecord]);
		label_list[MedRecord].setBounds(50, 170, 90, 30);
		tx[MedRecord].setBounds(140, 170, 250, 30);

		
		hospitalization.validate();
		
		// 按键初始化
		buttons[Button_Ok] = new JButton("确定");
		buttons[Button_Clear] = new JButton("清空");
		buttons[Button_Close] = new JButton("退出");
		
		for (JButton b : buttons) {
			this.add(b);
			b.setFont(new MyFont().getFont());
			b.setForeground(Color.WHITE);
			b.addActionListener(this);
		}
		
		buttons[Button_Ok].setBounds(200, 560, 160, 35);
		buttons[Button_Ok].setBackground(new Color(148, 207, 91));
		
		buttons[Button_Clear].setBounds(400, 560, 80, 35);
		buttons[Button_Clear].setBackground(Color.LIGHT_GRAY);
		
		buttons[Button_Close].setBounds(520, 560, 80, 35);
		buttons[Button_Close].setBackground(new Color(195, 17, 5));
		// 时间选择按键
		basic_info.add(datePick);
		datePick.setBounds(360, 100, 30, 30);
		datePick.addActionListener(this);
		datePick.setBackground(new Color(148, 207, 91));
		
		if (h2.getAutoCoding() == 1) {
			// 自动生成ID
			generatePatientID();
			tx[Code].setEditable(false);
			auto.setSelected(true);
		} else {
			tx[Code].setText("");
		}
		
		this.validate();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void generatePatientID() {
		
		Configuration configuration = h2.getConfiguration();
		
		String f = String.format("%%s%%0%dd", configuration.getWidth());
		tx[Code].setText(String.format(f, configuration.getPrefix(), configuration.getCounter() + 1));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == buttons[Button_Close]) {
			
			// 关闭窗口
			this.dispose();
			
		} else if (e.getSource() == buttons[Button_Clear]) {
			
			// 清空窗口
			for (int i = 1; i < tx.length; i++)
				tx[i].setText(null);
			sex.setSelectedIndex(0);
			
		} else if (e.getSource() == buttons[Button_Ok]) {

			// 确认
			ok();
			
		} else if (e.getSource() == auto) {
			// 自动编号设置
			if (auto.isSelected()) {
				tx[Code].setEditable(false);
				generatePatientID();
			} else {
				tx[Code].setEditable(true);
				tx[Code].setText("");
			}
		} else if (e.getSource() == datePick) {
			Calendar calendar = Calendar.getInstance();
			String age = tx[Age].getText().toString(), birthday = tx[Birth].getText().toString();
			if (birthday.isEmpty() == false && birthday.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
				try {
					calendar.setTime(ft.parse(birthday));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			} else if (age.isEmpty() == false && Input.isInteger(age)) {
				calendar.set(Calendar.YEAR, 2019 - Integer.parseInt(age));
			}
			DatePickDialog pick = new DatePickDialog(calendar, null, getLocation(), getSize());
			calendar = pick.showDialog();
			if (calendar != null) {
				tx[Birth].setText(String.format("%tF", calendar));
			}
		}
	}
	
	/**
	 * 判断表单信息是否规范，插入数据库
	 */
	private void ok() {
		PatientInitStruct pis = new PatientInitStruct();
		pis.patient_id = tx[Code].getText().toString();
		pis.patient_name = tx[Name].getText().toString();
		pis.patient_age = tx[Age].getText().toString();
		if (pis.patient_id.isEmpty() || pis.patient_name.isEmpty() || pis.patient_age.isEmpty()) {
			MessageDialog.showError(this, "患者编号、姓名及年龄不能为空！");
		} else if (h2.checkPatientID(pis.patient_id)) {
			pis.patient_height = tx[Height].getText().toString();
			pis.patient_weight = tx[Weight].getText().toString();
			pis.patient_phone = tx[Phone].getText().toString();
			pis.patient_postcode = tx[Postcode].getText().toString();
			if (Input.isEmptyOrFloat(pis.patient_height)
					|| Input.isEmptyOrFloat(pis.patient_weight)
					|| Input.isEmptyOrInteger(pis.patient_phone)
					|| Input.isEmptyOrInteger(pis.patient_postcode, 6)
					|| Input.isInteger(pis.patient_age)) {
				pis.patient_address = tx[Address].getText().toString();
				pis.patient_sex = sex.getSelectedIndex();
				pis.patient_birth = tx[Birth].getText().toString();
				pis.patient_department = tx[Department].getText().toString();
				pis.patient_bed = tx[Bed].getText().toString();
				pis.patient_medins = tx[MedIns].getText().toString();
				pis.patient_clinic = tx[Clinic].getText().toString();
				pis.patient_hospital = tx[Hospital].getText().toString();
				pis.patient_medrecord = tx[MedRecord].getText().toString();
				if (h2.insertPatient(pis)) {
					MessageDialog.showMessage(this, "新建成功！");
					h2.setCounter();
					dispose();
				} else {
					MessageDialog.showMessage(this, "新建失败！");
				}
			} else {
				MessageDialog.showMessage(this, "请注意年龄、身高体重、联系电话及邮政编码等数字量的输入格式！");
			}
		} else {
			MessageDialog.showError(this, "患者编号已存在！");
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		h2.close();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == tx[Height] || e.getSource() == tx[Weight]) {
			String h = tx[Height].getText().toString(), w = tx[Weight].getText().toString();
			if (h.isEmpty() || w.isEmpty()) {
				bmi.setText("BMI：--");
			} else if (Input.isFloat(h) && Input.isFloat(w)) {
				float fh = Float.parseFloat(h), fw = Float.parseFloat(w);
				bmi.setText(String.format("BMI：%.2f", 10000 * fw / (fh * fh)));
			}
		}
		
	}
}
