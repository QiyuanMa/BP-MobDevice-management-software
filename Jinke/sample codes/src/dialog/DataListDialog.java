package dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import database.H2_DB;
import main.MainFrame;
import model.PatientListModel;
import resources.MyFont;
import xTool.Cache;


/**
 * 显示患者各个日期记录个数
 * @author Lova
 *
 */
public class DataListDialog extends JDialog implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 5509991194549528227L;

	// 按键
	private JButton[] buttons = new JButton[2];
	
	// 表格
	private JTable table = null;
	
	// 表头
	private static final String[] title = {"检测日期", "有效记录"};
	
	// 数据库及字体
	private H2_DB h2 = new H2_DB();
	private MyFont font = new MyFont();
	
	// 菜单项索引
//	private static final int	Data_Export_Excel 	= 8,
//								Data_Delete 		= 9;
	
//	private MainFrame frame = null;
	private int flag = 0;
	private String pk;
	
	public DataListDialog(MainFrame f, Point p, Dimension d, String code) {
		
		super(f, "患者数据信息", true);
//		frame = f;
		pk = code;
		setLayout(null);
		setResizable(false);
		
		int w = 350, h = 450;
		setBounds(p.x + (d.width - w) / 2, p.y + (d.height - h) / 2, w, h);
		
		// 表格初始化及格式设置
		table = new JTable(new PatientListModel(h2.getDataList(code), title));
		table.getTableHeader().setReorderingAllowed(false);
		table.setFont(font.getFont());
		table.getTableHeader().setFont(font.getFont());
		table.setRowHeight(24);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionForeground(Color.WHITE);
		table.setSelectionBackground(new Color(148, 207, 91));


		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);

		table.addMouseListener(this);
		
		JScrollPane sp = new JScrollPane(table);
		this.add(sp);
		sp.setBounds(55, 30, 240, 300);

		// 按键设置
		buttons[0] = new JButton("确定");
		buttons[1] = new JButton("关闭");
		
		for (JButton button : buttons) {
			this.add(button);
			button.setFont(font.getFont());
			button.setForeground(Color.WHITE);
			button.addActionListener(this);
		}
		
		buttons[0].setBackground(new Color(148, 207, 91));
		buttons[0].setBounds(50, 350, 100, 30);
		
		buttons[1].setBackground(new Color(195, 17, 5));
		buttons[1].setBounds(200, 350, 100, 30);
		
		this.validate();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public int showDialog() {
		this.setVisible(true);
		return flag;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Cache.setDate(h2.getSelectTime(pk, table.getSelectedRow()));
			flag = 1;
//			frame.footer.showPatientWithDate();
//			frame.its[Data_Export_Excel].setEnabled(true);
//			frame.its[Data_Delete].setEnabled(true);
//			for(int i = 5; i < frame.buttons.length; i++) {
//				frame.buttons[i].setVisible(true);
//			}
//			frame.addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
//			frame.validate();
			dispose();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttons[1]) {
			
			this.dispose();
			
		} else if (e.getSource() == buttons[0]) {
			if(table.getSelectedRow() == -1) {
				
			}
			else {
				flag = 1;
				Cache.setDate(table.getValueAt(table.getSelectedRow(), 0).toString());
//				frame.footer.showPatientWithDate();
//				frame.its[Data_Export_Excel].setEnabled(true);
//				frame.its[Data_Delete].setEnabled(true);
//				for(int i = 2; i < frame.buttons.length; i++) {
//					frame.buttons[i].setVisible(true);
//				}
//				frame.addstatistic(new Statistic(Cache.getCode(), Cache.getDate()));
				this.dispose();
			}
		}
	}

}
