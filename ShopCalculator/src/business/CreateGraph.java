package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import connection.SQLConnection;

//String jan1,String feb1,String mar1,String apr1,String may1,String jun1,String jul1,String aug1,String sep1,String oct1,String nov1,String dec1,String jan2,String feb2

public class CreateGraph {

	public DefaultCategoryDataset createDataset(String yearChosen) {
		SQLConnection sqlConn = SQLConnection.getInstance();
		Statement st = sqlConn.getSQLConnection();
		ResultSet rs = null;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
		try {
			
			String sql = ("SELECT * FROM Sales WHERE year='"+yearChosen+"';");
			rs = st.executeQuery(sql);
				
				while(rs.next()) {
					
					String year = rs.getString("Year");
					String month = rs.getString("Month");
					Double salesAmount = rs.getDouble("SalesAmt");
					
			        dataset.addValue(salesAmount, month,year.substring(0,4) );
			        
				}
					
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return dataset;
		
	}
	
    public JFreeChart createChart(DefaultCategoryDataset dataset) {
    	
    	
        JFreeChart chart = ChartFactory.createBarChart(
            "Monthly Sales Chart", 
            "Month" /* x-axis label*/, 
            "Sales" /* y-axis label */,
            dataset);
        
        chart.addSubtitle(new TextTitle("Plot of monthly sales amount"));


        CategoryPlot plot = (CategoryPlot) chart.getPlot();



        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }
    
	
}
