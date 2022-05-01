package presentation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import com.ex.calculate.ComputeBean;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import business.CreateGraph;
import connection.SQLConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class MainWindow extends Application {

	CreateGraph a = new CreateGraph();
	SQLConnection sqlConn = SQLConnection.getInstance();
	Statement st = sqlConn.getSQLConnection();
	ResultSet rs = null;


	@Override
	public void start(Stage primaryStage) throws Exception {
		initUI(primaryStage);

	}

	private void initUI(Stage primaryStage) {
		VBox root = new VBox();
		root.setMinWidth(300);
		root.setMinHeight(500);

		root.getChildren().addAll(enterSales(primaryStage));
		Platform.runLater(() -> root.requestFocus());

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ShopCharts");
		primaryStage.show();

	}

	private VBox enterSales(Stage primaryStage) {

		VBox inputs = new VBox(5);

		Label inputLabel = new Label("Input sales values: ");

		TextField jan1 = new TextField();
		jan1.setPromptText("JANUARY 2017");

		TextField feb1 = new TextField();
		feb1.setPromptText("FEBRUARY 2017");

		TextField mar1 = new TextField();
		mar1.setPromptText("MARCH 2017");

		TextField apr1 = new TextField();
		apr1.setPromptText("APRIL 2017");

		TextField may1 = new TextField();
		may1.setPromptText("MAY 2017");

		TextField jun1 = new TextField();
		jun1.setPromptText("JUNE 2017");

		TextField jul1 = new TextField();
		jul1.setPromptText("JULY 2017");

		TextField aug1 = new TextField();
		aug1.setPromptText("AUGUST 2017");

		TextField sep1 = new TextField();
		sep1.setPromptText("SEPTEMBER 2017");

		TextField oct1 = new TextField();
		oct1.setPromptText("OCTOBER 2017");

		TextField nov1 = new TextField();
		nov1.setPromptText("NOVEMBER 2017");

		TextField dec1 = new TextField();
		dec1.setPromptText("DECEMBER 2017");

		TextField jan2 = new TextField();
		jan2.setPromptText("JANUARY 2018");

		TextField feb2 = new TextField();
		feb2.setPromptText("FEBRUARY 2018");

		Button btnGo = new Button("GO");
		btnGo.setId("btndetailUser");

		btnGo.setOnAction(e -> {

			try {

				String sql = ("INSERT INTO Sales(Year,Month,SalesAmt) " + "VALUES (2017,'Jan','"
						+ Double.valueOf(jan1.getText()) + "' ) , " + "(2017,'Feb','" + Double.valueOf(feb1.getText())
						+ "' ) ," + "(2017,'Mar','" + Double.valueOf(mar1.getText()) + "' ) ," + "(2017,'Apr','"
						+ Double.valueOf(apr1.getText()) + "' ) ," + "(2017,'May','" + Double.valueOf(may1.getText())
						+ "' ) ," + "(2017,'Jun','" + Double.valueOf(jun1.getText()) + "' ) ," + "(2017,'Jul','"
						+ Double.valueOf(jul1.getText()) + "' ) ," + "(2017,'Aug','" + Double.valueOf(aug1.getText())
						+ "' ) ," + "(2017,'Sep','" + Double.valueOf(sep1.getText()) + "' ) ," + "(2017,'Oct','"
						+ Double.valueOf(oct1.getText()) + "' ) ," + "(2017,'Nov','" + Double.valueOf(nov1.getText())
						+ "' ) ," + "(2017,'Dec','" + Double.valueOf(dec1.getText()) + "' ) ," + "(2018,'Jan','"
						+ Double.valueOf(jan2.getText()) + "' ) ," + "(2018,'Feb','" + Double.valueOf(feb2.getText())
						+ "' ) ;");
				st.executeUpdate(sql);

				btnGo.setDisable(true);

			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			showChart(primaryStage);
		});

		inputs.getChildren().addAll(inputLabel, jan1, feb1, mar1, apr1, may1, jun1, jul1, aug1, sep1, oct1, nov1, dec1,
				jan2, feb2, btnGo);

		return inputs;
	}

	String chosenYear = "2017";

	private HBox details(Stage primaryStage) {
		HBox details = new HBox();
		ObservableList<String> options = FXCollections.observableArrayList("2017", "2018");
		final ComboBox<String> yearChosen = new ComboBox<String>(options);
		yearChosen.setPromptText("Year");

		yearChosen.setOnAction(e -> {
			chosenYear = (yearChosen.getValue());
			System.out.println("changeing to " + chosenYear);
			showChart(primaryStage);
		});
		
		ArrayList<Integer> temp = new ArrayList<Integer>();

		try {
			
			System.out.println(chosenYear);

			String sql = ("SELECT * FROM Sales WHERE year='" + chosenYear + "';");
			rs = st.executeQuery(sql);

			while (rs.next()) {

				Double salesAmount = rs.getDouble("SalesAmt");
				Integer sales = salesAmount.intValue();
				temp.add(sales);

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		int[] salesArray = temp.stream().mapToInt(Integer::intValue).toArray();

		VBox texts = new VBox(5);
		ComputeBean compute = new ComputeBean();
		
		int totalAmt = compute.computeTotal(salesArray);
		String textTotal = Integer.toString(totalAmt);
		Label total = new Label(" Total Sales "+chosenYear+" : "+textTotal);
		
		double averageAmt = compute.computeAverage(salesArray);
		String textAvg = Double.toString(averageAmt);
		Label average = new Label(" Average Sales this year : "+textAvg);
		
		int smallestAmt = compute.findSmallestElement(salesArray);
		String textSmallest = Integer.toString(smallestAmt);
		Label smallest = new Label(" Least monthly sale this year : "+textSmallest);
		
		int largestAmt = compute.findLargestElement(salesArray);
		String textLargest = Integer.toString(largestAmt);
		Label largest = new Label(" Highest monthly sale this year : "+textLargest);
		
		texts.getChildren().addAll(total,average,smallest,largest);
		details.getChildren().addAll(yearChosen,texts);

		return details;
	}

	private void showChart(Stage primaryStage) {

		DefaultCategoryDataset b = a.createDataset(chosenYear);

		final SwingNode chartSwingNode = new SwingNode();
		chartSwingNode.setContent(

				new ChartPanel(a.createChart(b)));

		primaryStage.setScene(new Scene(new VBox(details(primaryStage), new StackPane(chartSwingNode))

		));

		primaryStage.show();
		chartSwingNode.getScene().getWindow().setHeight(500);
		chartSwingNode.getScene().getWindow().setWidth(500);

	}

	public static void main(String[] args) {

		launch(args);

	}

}
