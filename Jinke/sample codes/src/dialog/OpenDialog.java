package dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;

import database.H2_DB;
import model.PatientListModel;
import resources.MyFont;
	
public class OpenDialog extends JDialog implements ActionListener, MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4957883459775963618L;

	// 搜索框
	private JTextField search = new JTextField();
	
	// 放大镜图标
	private JLabel label = new JLabel(new ImageIcon("./icon/search.png"));
	
	// 按键
	private JButton[] buttons = new JButton[3];
	
	// 显示表格
	private JTable table = null;
	
	// 数据库及字体
	private MyFont font = new MyFont();
	private H2_DB h2 = new H2_DB();
	
//	private MainFrame frame = null;
	private String flag = null;
	
	public OpenDialog(JFrame f, Point p, Dimension d) {
		
		// 父类构造函数
		super(f, "患者信息列表", true);
//		frame = (MainFrame)f;
		setLayout(null);
		setResizable(false);
		
		int w = 800, h = 600;
		setBounds(p.x + (d.width - w) / 2, p.y + (d.height - h) / 2, w, h);
		
		// 设置放大镜图标
		this.add(label);
		label.setBounds(510, 30, 30, 30);
		
		// 设置搜索框
		this.add(search);
		search.setBounds(550, 30, 200, 30);
		search.setFont(font.getFont());
		search.setSelectedTextColor(Color.WHITE);
		search.setSelectionColor(new Color(148, 207, 91));
		search.addActionListener(this);
		
		// 新建table
		table = new JTable(new PatientListModel(h2.getPatientList()));
		// 设置列不可移动
		table.getTableHeader().setReorderingAllowed(false);

		table.setFont(font.getFont());
		table.getTableHeader().setFont(font.getFont());
		table.setRowHeight(24);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, tcr);
		table.setSelectionForeground(Color.WHITE);
		table.setSelectionBackground(new Color(148, 207, 91));
		table.addMouseListener(this);
				
		// 滚动页面
		JScrollPane sp = new JScrollPane(table);

		sp.validate();
		this.add(sp);
		sp.setBounds(80, 90, 640, 400);
		
		buttons[0] = new JButton("确定");
		buttons[1] = new JButton("删除");
		buttons[2] = new JButton("导出");
		
		for (JButton button : buttons) {
			button.setFont(font.getFont());
			button.setForeground(Color.WHITE);
			button.addActionListener(this);
			this.add(button);
		}
		buttons[0].setBackground(new Color(148, 207, 91));
		buttons[0].setBounds(200, 500, 100, 30);
		
		buttons[2].setBackground(new Color(148, 207, 91));
		buttons[2].setBounds(350, 500, 100, 30);
		
		buttons[1].setBackground(new Color(195, 17, 5));
		buttons[1].setBounds(500, 500, 100, 30);

		this.validate();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public String showDialog() {
		this.setVisible(true);
		return flag;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		h2.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search) {
			// 更新表格
			table.setModel(new PatientListModel(h2.getPatientList(search.getText().toString())));
		} else if (e.getSource() == buttons[1]) {
			// 删除患者
			int rows[] = table.getSelectedRows();
			if (rows.length == 0) {
				MessageDialog.showError(this, "未选中患者！");
				return;
			} else if (MessageDialog.showConfirm(null, "确认删除？\r\n本操作将删除 " + rows.length + " 位患者的所有数据！") == 0) {
				String s[] = new String[rows.length];
				for (int i = 0; i < rows.length; i++)
					s[i] = table.getValueAt(rows[i], 0).toString();
				for(String code : s) {
					h2.deletePatient(code);
				}
				table.setModel(new PatientListModel(h2.getPatientList()));
			}
		} else if (e.getSource() == buttons[0]) {
			// 确定
			if (table.getSelectedRowCount() == 1) {
				flag = table.getValueAt(table.getSelectedRow(), 0).toString();
				this.dispose();
			} else {
				MessageDialog.showError(this, "一次只可打开一位患者的信息");
			}
		} else if (e.getSource() == buttons[2]) {
			// 导出患者
			int rows[] = table.getSelectedRows();
			String s[] = new String[rows.length];
			for (int i = 0; i < rows.length; i++)
				s[i] = table.getValueAt(rows[i], 0).toString();
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
			chooser.setFileFilter(new FileNameExtensionFilter("xml文件", "xml"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				h2.exportPatient(s, chooser.getSelectedFile().getPath());
				MessageDialog.showMessage(null, "导出成功！");
			}
			try {
				UIManager.setLookAndFeel(origenal);
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			flag = table.getValueAt(table.getSelectedRow(), 0).toString();
			this.dispose();
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
	
}
