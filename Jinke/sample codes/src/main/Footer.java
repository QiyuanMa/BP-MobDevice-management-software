package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import database.H2_DB;
import dialog.MyImplement;
import resources.MyFont;
import xTool.Cache;
import xTool.Device;

public class Footer extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7890477447295450646L;

	// Right and wrong icons
	private static final 	ImageIcon State_Ok = new ImageIcon("./icon/ok.png"), 
							State_Error = new ImageIcon("./icon/error.png");
	
	// 
	private JLabel tips = new JLabel(State_Error);
	private JLabel label = new JLabel();
	
	
	public Footer(MainFrame f) {
		
		// Format and font
		label.setBackground(new Color(148, 207, 91));
		label.setOpaque(true);
		label.setFont(new MyFont(0, 18).getFont());
		label.setFont(label.getFont().deriveFont((float)18));
		label.setForeground(Color.WHITE);
		
		tips.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				new MyImplement(f, f.getLocation(), f.getSize());
				if (Device.deviceIndentify(Cache.getCom())) {
					tips.setIcon(State_Ok);
				}
				else {
					tips.setIcon(State_Error);
				}
			}
		});
		
		// Facility layout
		this.setLayout(new BorderLayout());
		
		this.add(BorderLayout.WEST, tips);
		this.add(BorderLayout.CENTER, label);
		
		this.setVisible(true);
		this.validate();
	}
	
	public void setOk() {
		tips.setIcon(State_Ok);
	}
	
	public void setError() {
		tips.setIcon(State_Error);
	}
	
	/**
	 * Status bar shows information of currently selected patient
	 * @param p
	 */
//	public void showPatient() {
//		if (Cache.getCode() != null)
//			label.setText(String.format(" >>> 患者编号：%-20s姓名：%-12s年龄：%-8d性别：%-10s 最近一次监测时间：%-15s", 
//					Cache.getCode(), 
//					Cache.getName(), 
//					Cache.getAge(), 
//					Cache.getSex(),
//					Cache.getDate()));
//	}
	
	/**
	 * Status bar displays information of currently selected patient, including data monitoring time
	 * @param p
	 */
//	public void showPatientWithDate() {
//		H2_DB h2_DB = new H2_DB();
//		if (Cache.getCode() != null)
//			label.setText(String.format(" >>> 患者编号：%-20s姓名：%-12s年龄：%-8d性别：%-10s 当前数据监测时间：%-15s", 
//					Cache.getCode(), 
//					Cache.getName(), 
//					Cache.getAge(), 
//					Cache.getSex(),
//					h2_DB.getReviewTime(Cache.getDate())));
//	}
	public void showPatient() {
	if (Cache.getCode() != null) {
		if (Cache.getDate().equals("--") || Cache.getDate().matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
			label.setText(String.format(" >>> 患者编号：%-20s姓名：%-12s年龄：%-8d性别：%-10s 最近一次监测时间：%-15s", 
					Cache.getCode(), 
					Cache.getName(), 
					Cache.getAge(), 
					Cache.getSex(),
					Cache.getDate()));
		}
		else {
			H2_DB h2_DB = new H2_DB();
			label.setText(String.format(" >>> 患者编号：%-20s姓名：%-12s年龄：%-8d性别：%-10s 当前数据监测时间：%-15s", 
			Cache.getCode(), 
			Cache.getName(), 
			Cache.getAge(), 
			Cache.getSex(),
			h2_DB.getReviewTime(Cache.getDate())));
			h2_DB.close();

		}
	}
}

	public void clear() {
		label.setText("");
	}
	
}
