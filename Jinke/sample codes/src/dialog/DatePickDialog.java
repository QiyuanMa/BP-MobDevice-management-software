package dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import model.DataPickCellRenderer;
import model.DataPickModel;
import resources.MyFont;

public class DatePickDialog extends JDialog implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1203639946243053078L;
	
	private JButton[] btn = new JButton[4];
	private JLabel label = new JLabel();
	private Calendar calendar = Calendar.getInstance();
	private JTable table = new JTable(new DataPickModel());
	private MyFont font = new MyFont();
	private boolean click = false;
	public DatePickDialog(Calendar c, JFrame f, Point p, Dimension d) {
		
		// 父类构造函数，有模式对话框
		super(f, "新建患者信息", true);
		
		// 无布局，不可伸缩
		setLayout(null);
		setResizable(false);
		
		// 固定大小
		int w = 400, h = 300;
		setBounds(p.x + (d.width - w) / 2, p.y + (d.height - h) / 2, w, h);

		calendar = c;
		label.setText(String.format("%1$tY-%1$tm", calendar.getTime()));
		label.setFont(font.getFont());
		this.add(label);
		btn[0] = new JButton("<<");
		btn[1] = new JButton("<");
		btn[2] = new JButton(">");
		btn[3] = new JButton(">>");
		for (JButton b : btn) {
			this.add(b);
			b.setFont(font.getFont());
			b.addActionListener(this);
		}
		
		label.setBounds(120, 10, 160, 30);
		label.setHorizontalAlignment(JLabel.CENTER);
		btn[0].setBounds(5, 10, 60, 30);
		btn[1].setBounds(70, 10, 50, 30);
		btn[2].setBounds(280, 10, 50, 30);
		btn[3].setBounds(335, 10, 55, 30);
		

		this.add(table);
		DataPickCellRenderer tcr = new DataPickCellRenderer();
		table.setDefaultRenderer(Object.class, tcr);
		table.setFont(font.getFont());
		table.setRowHeight(30);
		table.setBounds(0, 50, 400, 250);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(Color.WHITE);
		flushCalendar();
		table.addMouseListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.validate();

	}
	
	public Calendar showDialog() {
		this.setVisible(true);
		if (click) {
			return calendar;
		} else {
			return null;
		}
		
	}
	
	/**
	 * 更新日期选择控件中显示内容
	 */
	private void flushCalendar() {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK) - 1, y = calendar.get(Calendar.YEAR);
		int[] lenOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
			lenOfMonth[1] = 29;
		}
		for (int r = 1; r < 7; r++) {
			for (int c = 0; c < 7; c++) {
				table.setValueAt(0, r, c);
			}
		}
		for (int r = 1, c = offset, i = 1; i <= lenOfMonth[calendar.get(Calendar.MONTH)]; i++) {
			table.setValueAt(i, r, c);
			if (c == 6) {
				r = (r + 1) % 7;
			}
			c = (c + 1) % 7;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn[0]) {
			calendar.add(Calendar.YEAR, -1);
		} else if (e.getSource() == btn[3]) {
			calendar.add(Calendar.YEAR, 1);
		} else if (e.getSource() == btn[1]) {
			calendar.add(Calendar.MONTH, -1);
		} else if (e.getSource() == btn[2]) {
			calendar.add(Calendar.MONTH, 1);
		}
		flushCalendar();
		table.updateUI();
		label.setText(String.format("%1$tY-%1$tm", calendar.getTime()));

	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if (e.getClickCount() == 2 && table.getSelectedRow() != 0) {
			Object day = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			if (day.toString().equals("") == false) {
				calendar.set(Calendar.DAY_OF_MONTH, (int)day);
				click = true;
				dispose();
			}
		}
	}
	

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

}
