package panels;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import xTool.Analyse;

public class BP_Bargraph {
	private ChartPanel chartPanel[] = new ChartPanel[3];
	private JFreeChart chart[] = new JFreeChart[3];
	private JTable table;
	public BP_Bargraph(JTable t) {
		table = t;
		
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");                     //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("Yahei Consolas Hybrid",Font.BOLD,20));                    //设置图例的字体
        standardChartTheme.setRegularFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12));					//设置轴向的字体          
        standardChartTheme.setLargeFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12)); 
        standardChartTheme.setRangeGridlinePaint(Color.GRAY);//纵坐标格线颜色                
        standardChartTheme.setDomainGridlinePaint(Color.GRAY);//横坐标格线颜色              
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        
        DefaultCategoryDataset dataset = getBarDataSet(Analyse.SBP); 
        ChartFactory.setChartTheme(standardChartTheme); //应用主题样式      
        chart[0] = ChartFactory.createBarChart(
        		"直方图",      // 图表名  
	            "",               // 横轴标签文字 
	            "%收缩压",               // 纵轴标签文字  
	            dataset,          // 图表的数据集合 
	            PlotOrientation.VERTICAL,
	            false,               // 是否显示图表中每条数据序列的说明
	            true,              // 是否显示工具提示
	            false);             // 是否显示图表中设置的url网络连接   
        
        CategoryPlot categoryPlot = (CategoryPlot) chart[0].getPlot();  
        categoryPlot.setNoDataMessage("没有足够的测量值用于评估。");
        BarRenderer customBarRenderer = (BarRenderer) categoryPlot.getRenderer();
        customBarRenderer.setSeriesPaint(0, new Color(140, 140, 220));
	    chartPanel[0] = new ChartPanel(chart[0],true);
	    
	    dataset = getBarDataSet(Analyse.DBP); 
        ChartFactory.setChartTheme(standardChartTheme); //应用主题样式      
        chart[1] = ChartFactory.createBarChart(
        		"",      // 图表名  
	            "",               // 横轴标签文字 
	            "%舒张压",               // 纵轴标签文字  
	            dataset,          // 图表的数据集合 
	            PlotOrientation.VERTICAL,
	            false,               // 是否显示图表中每条数据序列的说明
	            true,              // 是否显示工具提示
	            false);             // 是否显示图表中设置的url网络连接   
        
        categoryPlot = (CategoryPlot) chart[1].getPlot();  
        categoryPlot.setNoDataMessage("没有足够的测量值用于评估。");
        customBarRenderer = (BarRenderer) categoryPlot.getRenderer();
        customBarRenderer.setSeriesPaint(0, new Color(140, 140, 220));
        chartPanel[1] = new ChartPanel(chart[1],true);
        
        dataset = getBarDataSet(Analyse.HR); 
        ChartFactory.setChartTheme(standardChartTheme); //应用主题样式      
        chart[2] = ChartFactory.createBarChart(
        		"",      // 图表名  
	            "",               // 横轴标签文字 
	            "%心率",               // 纵轴标签文字  
	            dataset,          // 图表的数据集合 
	            PlotOrientation.VERTICAL,
	            false,               // 是否显示图表中每条数据序列的说明
	            true,              // 是否显示工具提示
	            false);             // 是否显示图表中设置的url网络连接   
        
        categoryPlot = (CategoryPlot) chart[2].getPlot();  
        categoryPlot.setNoDataMessage("没有足够的测量值用于评估。");
        chartPanel[2] = new ChartPanel(chart[2],true);
	}
	
	private DefaultCategoryDataset getBarDataSet(int type) {
		Analyse analyse = new Analyse(table);
		if(analyse.getValidNum(Analyse.ALL) < 1){
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    		return dataset;
    	}
		int[] count = new int[27];
		for(int i = 0; i < 27; i++) {
			count[i] = 0;
		}
		for(int i = 0; i < table.getRowCount(); i++) {
			if(table.getValueAt(i, 0) == null || table.getValueAt(i, 3) == null || table.getValueAt(i, 4) == null || table.getValueAt(i, 5) == null || table.getValueAt(i, 6) == null) {
    			continue;
    		}
			for(int j = 10; j < 270; j += 10) {
				if(Integer.parseInt(table.getValueAt(i, type).toString()) < j) {
					count[j / 10 - 1] += 1;
					break;
				}
			}
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int j = 10; j < 270; j += 10) {
			dataset.addValue(count[j / 10 - 1], type+"", (j - 10)+"");
		}
		return dataset;
	}
	
	public ChartPanel getChartPanel(int index){
    	return chartPanel[index];
    }
	
	public void createPic() throws Exception {
    	FileOutputStream fos_jpg=null; 
    	for(int i = 0; i < 3; i++) {
    		File file = new File("temp/pic" + i + ".png");
            try {  
                fos_jpg = new FileOutputStream(file,false);    
                ChartUtils.writeChartAsPNG(fos_jpg, chart[i], 1000, 350, null, true, 0);
                fos_jpg.close();
            } catch (Exception e) {
                e.printStackTrace(); 
                throw e;
            } finally {  
                try {
                    fos_jpg.close();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }  
    	}
    }
}
