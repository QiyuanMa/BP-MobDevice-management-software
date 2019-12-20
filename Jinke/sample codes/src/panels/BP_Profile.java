package panels;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import database.H2_DB;
import xTool.Analyse;
import xTool.Configuration;

public class BP_Profile{
	ChartPanel chartPanel;
	JFreeChart chart;
	private JTable table;
	private TimeSeries series[] = new TimeSeries[6];
	Configuration conf = null;
	private H2_DB h2 = new H2_DB();
	public BP_Profile(JTable t) {
		table = t;
		conf  = h2.getConfiguration();
		XYDataset xyDataset = createDataset();
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");                     //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("Yahei Consolas Hybrid",Font.BOLD,20));                    //设置图例的字体
        standardChartTheme.setRegularFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12));					//设置轴向的字体          
        standardChartTheme.setLargeFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12)); 
        standardChartTheme.setRangeGridlinePaint(Color.GRAY);//纵坐标格线颜色                
        standardChartTheme.setDomainGridlinePaint(Color.GRAY);//横坐标格线颜色              
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        
        ChartFactory.setChartTheme(standardChartTheme); //应用主题样式      
		chart = ChartFactory.createTimeSeriesChart(  
	            "血压轮廓图",      // 图表名  
	            "时间",               // 横轴标签文字 
	            "血压(mmHg)/心率(1/min)",               // 纵轴标签文字  
	            xyDataset,          // 图表的数据集合 
	            false,               // 是否显示图表中每条数据序列的说明
	            true,              // 是否显示工具提示
	            false);             // 是否显示图表中设置的url网络连接   
		
	    XYPlot xyPlot = (XYPlot) chart.getPlot();  
	    xyPlot.setNoDataMessage("没有足够的测量值用于评估。");
	    XYDifferenceRenderer diffRenderer = new XYDifferenceRenderer(  
	    		new Color(140, 140, 220),     // 序列线1超过序列线2时填充的的颜色 
	    		Color.WHITE,   // 序列线2超过序列线1时填充的的颜色  
	    		false);         // 是否描绘数据点  
	    // 设置XYPlot的描绘器为我们自定义的XYDifferenceRenderer    
	    NumberAxis numberaxis = (NumberAxis)xyPlot.getRangeAxis(); 
        numberaxis.setAutoRangeIncludesZero(false); 
	    xyPlot.setRenderer(0, diffRenderer);  
	    // 设置前景色的透明度为50%  
	    xyPlot.setForegroundAlpha(0.5F);
	    
//	    DateAxis dateAxis = (DateAxis)xyPlot.getDomainAxis();
//	    dateAxis.setAutoTickUnitSelection(true);//设置不采用自动选择刻度值
//	    dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//设置标记的位置
//	    dateAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//设置标准的时间刻度单位
//	    dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1, new SimpleDateFormat("HH:MM")));//设置时间刻度的间隔
	    
	// 使JFreeChart应用当前设置的主题  
	    ChartUtils.applyCurrentTheme(chart);  
	    DateAxis dateaxis = (DateAxis)xyPlot.getDomainAxis(); 
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:MM"));
		StandardXYToolTipGenerator localStandardXYToolTipGenerator = new StandardXYToolTipGenerator("{1}: {2}", new SimpleDateFormat("HH:MM"), new DecimalFormat("0"));
		xyPlot.getRenderer().setDefaultToolTipGenerator(localStandardXYToolTipGenerator);
		xyPlot.getRenderer().setSeriesPaint(0, Color.BLUE);
	    xyPlot.getRenderer().setSeriesPaint(1, Color.BLUE);
	    xyPlot.getRenderer().setSeriesPaint(2, Color.WHITE);
	    xyPlot.getRenderer().setSeriesPaint(3, Color.RED);
	    xyPlot.getRenderer().setSeriesPaint(4, Color.BLACK);
	    xyPlot.getRenderer().setSeriesPaint(5, Color.BLACK);
	    xyPlot.getRenderer().setSeriesStroke(0, new BasicStroke(1.5f));
	    xyPlot.getRenderer().setSeriesStroke(1, new BasicStroke(1.5f));
	    xyPlot.getRenderer().setSeriesStroke(2, new BasicStroke(2.5f));
	    xyPlot.getRenderer().setSeriesStroke(3, new BasicStroke(1.5f));
	    xyPlot.getRenderer().setSeriesStroke(4, new BasicStroke(1.5f));
	    xyPlot.getRenderer().setSeriesStroke(5, new BasicStroke(1.5f));
	    xyPlot.getRenderer().getSeriesToolTipGenerator(0);
	    
//	    chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	    
	    chartPanel = new ChartPanel(chart,true);
	}
	
	private XYDataset createDataset() {  
		Analyse analyse = new Analyse(table);
		if(analyse.getValidNum(Analyse.ALL) <= 1){
			TimeSeriesCollection dataset = new TimeSeriesCollection();
    		return dataset;
    	}
		// 生成数据序列
    	this.series[0] = new TimeSeries("收缩压");  
    	this.series[1] = new TimeSeries("舒张压");
    	this.series[2] = new TimeSeries("平均压");
    	this.series[3] = new TimeSeries("心率");
    	this.series[4] = new TimeSeries("标准收缩压");
    	this.series[5] = new TimeSeries("标准舒张压");
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date startDate = null;
    	Date t = null;
    	for(int i = 0; i < table.getRowCount(); i++) {
    		//System.out.println(table.getValueAt(i, 4));
    		if(table.getValueAt(i, 0) == null || table.getValueAt(i, 3) == null || table.getValueAt(i, 4) == null || table.getValueAt(i, 5) == null || table.getValueAt(i, 6) == null) {
    			continue;
    		}
    		try {
    			if(startDate == null) {
        			startDate = sdf1.parse(table.getValueAt(i, 1).toString() + " " + table.getValueAt(i, 2).toString());
        		}
    			t = sdf1.parse(table.getValueAt(i, 1).toString() + " " + table.getValueAt(i, 2).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		series[0].addOrUpdate(new Second(t), Integer.parseInt(table.getValueAt(i, 3).toString()));
    		series[1].addOrUpdate(new Second(t), Integer.parseInt(table.getValueAt(i, 5).toString()));
    		series[2].addOrUpdate(new Second(t), Integer.parseInt(table.getValueAt(i, 4).toString()));
    		series[3].addOrUpdate(new Second(t), Integer.parseInt(table.getValueAt(i, 6).toString()));
    	}
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(t);
    	while(calendar.compareTo(endCalendar) == -1) {
    		Date date = calendar.getTime();
    		conf.getDayStart().set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
    		conf.getNightStart().set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
    		if(calendar.compareTo(conf.getDayStart()) == 1 && calendar.compareTo(conf.getNightStart()) == -1) {
    			series[4].add(new Second(date), conf.getDaySBP());
    			series[5].add(new Second(date), conf.getDayDBP());
    		}
    		else {
    			series[4].add(new Second(date), conf.getNightSBP());
    			series[5].add(new Second(date), conf.getNightDBP());
    		}
    		calendar.add(Calendar.SECOND, +30);
    	}
    	// 将两条数据序列都放在一个数据集合中  
    	
    	TimeSeriesCollection dataset = new TimeSeriesCollection(); 
    	dataset.addSeries(this.series[0]);
    	dataset.addSeries(this.series[1]);
    	dataset.addSeries(this.series[2]);
    	dataset.addSeries(this.series[3]);
    	dataset.addSeries(this.series[4]);
    	dataset.addSeries(this.series[5]);
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
    
    /*public void exportChartAsSVG(int width,int height) throws IOException {
    	FileOutputStream fos_svg = null; 
    	File file = new File("pic.svg");
    	fos_svg = new FileOutputStream(file,false); 
    	// Get a DOMImplementation and create an XML document
        DOMImplementation domImpl =
                   GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);  
           // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        Rectangle bounds = new Rectangle(0,0,width,height);
           // draw the chart in the SVG generator
        chart.draw(svgGenerator, bounds);  
           // Write svg file
        Writer out = new OutputStreamWriter(fos_svg, "UTF-8");
        svgGenerator.stream(out, true);
        out.close();
        fos_svg.close();
    }*/

}