package panels;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.DefaultCategoryDataset;

import xTool.Analyse;

public class BP_Piegraph {
	private ChartPanel chartPanel;
	private JFreeChart chart;
	private JTable table;
	
	public BP_Piegraph(JTable t) throws NumberFormatException, ParseException {
		table = t;
		
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");                     //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("Yahei Consolas Hybrid",Font.BOLD,16));                    //设置图例的字体
        standardChartTheme.setRegularFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12));					//设置轴向的字体          
        standardChartTheme.setLargeFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12)); 
        standardChartTheme.setRangeGridlinePaint(Color.GRAY);//纵坐标格线颜色                
        standardChartTheme.setDomainGridlinePaint(Color.GRAY);//横坐标格线颜色              
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        
        DefaultCategoryDataset dataset = getPieDataSet();
        ChartFactory.setChartTheme(standardChartTheme); //应用主题样式      
        chart = ChartFactory.createMultiplePieChart(
        		"超过极限值",      // 图表名  
	            dataset,          // 图表的数据集合 
	            TableOrder.BY_ROW,
	            false,               // 是否显示图表中每条数据序列的说明
	            true,              // 是否显示工具提示
	            false);             // 是否显示图表中设置的url网络连接   
        
        MultiplePiePlot multiplepieplot = (MultiplePiePlot) chart.getPlot();
        JFreeChart jChart = multiplepieplot.getPieChart();
        PiePlot piePlot = (PiePlot) jChart.getPlot();
        piePlot.setNoDataMessage("没有足够的测量值用于评估。");
        piePlot.setSectionPaint("正常", Color.GREEN);
        piePlot.setSectionPaint("异常", Color.RED);
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        chartPanel = new ChartPanel(chart,true);
        
	}

	private DefaultCategoryDataset getPieDataSet() throws NumberFormatException, ParseException {
		Analyse analyse = new Analyse(table);
		if(analyse.getValidNum(Analyse.ALL) < 1){
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    		return dataset;
    	}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(analyse.getOverNum(Analyse.SBP, Analyse.DAY), "收缩压 白天", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.DAY) - analyse.getOverNum(Analyse.SBP, Analyse.DAY), "收缩压 白天", "正常");
		dataset.addValue(analyse.getOverNum(Analyse.SBP, Analyse.NIGHT), "收缩压 夜晚", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.NIGHT) - analyse.getOverNum(Analyse.SBP, Analyse.NIGHT), "收缩压 夜晚", "正常");
		dataset.addValue(analyse.getOverNum(Analyse.SBP, Analyse.ALL), "收缩压 总计", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.ALL) - analyse.getOverNum(Analyse.SBP, Analyse.ALL), "收缩压 总计", "正常");
		dataset.addValue(analyse.getOverNum(Analyse.DBP, Analyse.DAY), "舒张压 白天", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.DAY) - analyse.getOverNum(Analyse.DBP, Analyse.DAY), "舒张压 白天", "正常");
		dataset.addValue(analyse.getOverNum(Analyse.DBP, Analyse.NIGHT), "舒张压 夜晚", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.NIGHT) - analyse.getOverNum(Analyse.DBP, Analyse.NIGHT), "舒张压 夜晚", "正常");
		dataset.addValue(analyse.getOverNum(Analyse.DBP, Analyse.ALL), "舒张压 总计", "异常");
		dataset.addValue(analyse.getValidNum(Analyse.ALL) - analyse.getOverNum(Analyse.DBP, Analyse.ALL), "舒张压 总计", "正常");
		return dataset;
	}
	
	public ChartPanel getChartPanel(){
    	return chartPanel;
    }
	
	public void createPic() throws Exception {
    	FileOutputStream fos_jpg=null; 
    	File file = new File("temp/pic.png");
        try {  
            fos_jpg = new FileOutputStream(file,false);    
            ChartUtils.writeChartAsPNG(fos_jpg, chart, 1000, 840, null, true, 0);
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
