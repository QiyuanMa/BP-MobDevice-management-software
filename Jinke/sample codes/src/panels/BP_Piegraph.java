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
		
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");                     //Set title font
        standardChartTheme.setExtraLargeFont(new Font("Yahei Consolas Hybrid",Font.BOLD,16));                    //Set the font of the legend
        standardChartTheme.setRegularFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12));					//Set inline font          
        standardChartTheme.setLargeFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12)); 
        standardChartTheme.setRangeGridlinePaint(Color.GRAY);//Ordinate grid color                
        standardChartTheme.setDomainGridlinePaint(Color.GRAY);//Abscissa grid color              
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        
        DefaultCategoryDataset dataset = getPieDataSet();
        ChartFactory.setChartTheme(standardChartTheme); //Apply theme style
        chart = ChartFactory.createMultiplePieChart(
        		"超过极限值",      // chart name
	            dataset,          // Data collection for chart
	            TableOrder.BY_ROW,
	            false,               // Whether to show a description of each data series in the chart
	            true,              // Whether to show tooltips
	            false);             // Whether to display the url network connection set in the chart 
        
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
