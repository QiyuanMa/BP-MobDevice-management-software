package dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import resources.MyFont;
import xTool.Device;

public class ConsoleDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5927670328017772758L;
	private JLabel msg = new JLabel();
	private MyFont font = new MyFont();
	public ConsoleDialog(JFrame f, Point p, Dimension d) {
		// 父类构造函数
		super(f, "提示信息", false);
		setLayout(new BorderLayout());
		setResizable(true);
		
		int w = 300, h = 100;
		setBounds(p.x + (d.width - w) / 2, p.y + (d.height - h) / 4, w, h);
		
		this.add(BorderLayout.CENTER, msg);
		msg.setFont(font.getFont());
		msg.setHorizontalAlignment(SwingConstants.CENTER);
		setVisible(true);
		validate();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void showConsoleDialog(String message) {
		msg.setText(message);
		System.out.println(message);
		Device.pause(500);

	}
	public void closeConsoleDialog() {
		dispose();
	}

}
