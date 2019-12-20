package panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.ParseException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import main.MainFrame;
import resources.MyFont;
import model.TabbedPaneUI;

public class Charts extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane jTabbedPane = null;
	
	public Charts(MainFrame f, JTable t) throws NumberFormatException, ParseException {
		
		this.setLayout(new BorderLayout());
		
		jTabbedPane = new JTabbedPane();
		MyFont font = new MyFont(0, 13);
		jTabbedPane.setFont(font.getFont());
		jTabbedPane.setUI(new TabbedPaneUI("000000", "000000"));
		BP_Profile bp_Profile = new BP_Profile(t);
		jTabbedPane.addTab("血压轮廓图", bp_Profile.getChartPanel());
		BP_Piegraph bp_Piegraph = new BP_Piegraph(t);
		jTabbedPane.addTab("超过极限值", bp_Piegraph.getChartPanel());
		BP_Bargraph bp_Bargraph = new BP_Bargraph(t);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		panel.add(bp_Bargraph.getChartPanel(0));
		panel.add(bp_Bargraph.getChartPanel(1));
		panel.add(bp_Bargraph.getChartPanel(2));
		jTabbedPane.addTab("直方图", panel);
		
		this.add(jTabbedPane, BorderLayout.CENTER);
		
	}
	
	public JTabbedPane getTabbedPane() {
		return jTabbedPane;
	}

}
