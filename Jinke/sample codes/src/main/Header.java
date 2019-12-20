package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Header extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2941958061227052458L;
	private JPanel panel_for_buttons = new JPanel();
	
	public Header(JMenuBar b, JButton[] bts) {
		
		this.setLayout(new BorderLayout());
		
		panel_for_buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel_for_buttons.setBackground(Color.WHITE);
		
		for (JButton bt : bts) {
			panel_for_buttons.add(bt);
		}
		
		this.add(BorderLayout.NORTH, b);
		this.add(BorderLayout.SOUTH, panel_for_buttons);
		
		this.validate();
	}
}
