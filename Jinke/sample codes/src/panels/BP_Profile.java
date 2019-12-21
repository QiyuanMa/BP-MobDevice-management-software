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
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");                     //Set title font
        standardChartTheme.setExtraLargeFont(new Font("Yahei Consolas Hybrid",Font.BOLD,20));                    //Set the font of the legend
        standardChartTheme.setRegularFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12));					//Set inline font   
        standardChartTheme.setLargeFont(new Font("Yahei Consolas Hybrid",Font.PLAIN,12)); 
        standardChartTheme.setRangeGridlinePaint(Color.GRAY);//Ordinate grid color          
        standardChartTheme.setDomainGridlinePaint(Color.GRAY);//Abscissa grid color         
        standardChartTheme.setPlotBackgroundPaint(Color.WHITE);
        
        ChartFactory.setChartTheme(standardChartTheme); //Apply theme style     
		chart = ChartFactory.createTimeSeriesChart(  
	            "血压轮廓图",      // chart name
	            "时间",               // Horizontal axis label text
	            "血压(mmHg)/心率(1/min)",               // Vertical axis label text 
	            xyDataset,          // Data collection for chart
	            false,               // Whether to show a description of each data series in the chart
	            true,              // Whether to show tooltips
	            false);             // Whether to display the url network connection set in the chart   
		
	    XYPlot xyPlot = (XYPlot) chart.getPlot();  
	    xyPlot.setNoDataMessage("没有足够的测量值用于评估。");
	    XYDifferenceRenderer diffRenderer = new XYDifferenceRenderer(  
	    		new Color(140, 140, 220),     // Color filled when sequence line 1 exceeds sequence line 2
	    		Color.WHITE,   // Color filled when sequence line 2 exceeds sequence line 1
	    		false);         // Whether to plot data points
	    // Set XYPlot's renderer to our custom XYDifferenceRenderer   
	    NumberAxis numberaxis = (NumberAxis)xyPlot.getRangeAxis(); 
        numberaxis.setAutoRangeIncludesZero(false); 
	    xyPlot.setRenderer(0, diffRenderer);  
	    // Set the foreground color transparency to 50%
	    xyPlot.setForegroundAlpha(0.5F);
	    
//	    DateAxis dateAxis = (DateAxis)xyPlot.getDomainAxis();
//	    dateAxis.setAutoTickUnitSelection(true);//Setting does not use automatic selection of scale values
//	    dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//Set marker position
//	    dateAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//Set the standard time scale unit
//	    dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1, new SimpleDateFormat("HH:MM")));//Set the time scale interval
	    
	// Make JFreeChart apply the currently set theme
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
		// Generate data sequence
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
    	//Put both data series in one data set  
    	
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
