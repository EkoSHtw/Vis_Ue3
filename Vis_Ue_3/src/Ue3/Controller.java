package Ue3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


public class Controller {
	
	public Polygon polygonRight;
	public Polygon polygonLeft;
	public Label AccelerationLabel;
	public Label HorsepowerLabel;
	public Label WeightLabel;
	public Label ConsumptionLabel;
	
	public ComboBox filterOptions;
	public MenuButton axisOptions;
	
	public Label CylinderLabel;
	
	private ScatterChart<Number, Number> scatterChart;
	
	private String labelWeight = "Weight in kg";
	private String labelConsumption = "Consumption in mpg";
	
	
	public void initialize() {	
		final NumberAxis xAxis = new NumberAxis(0, 10, 1);
	    final NumberAxis yAxis = new NumberAxis(-100, 500, 100);        
	    /*
	    	scatterChart =  new ScatterChart(xAxis, yAxis);         
	    	scatterChart.getXAxis().setLabel(labelConsumption);
	    	scatterChart.getYAxis().setLabel(labelWeight);
	    */
	    
	    //Todo add filteritems and addOnaction
	    MenuItem filterItem1 = new MenuItem("Country");
	    filterOptions.getItems().add("America");
	    filterOptions.getItems().add("Europe");
	    filterOptions.getItems().add("Japan");
	    
	    MenuItem menuItem1 = new MenuItem("Weight/Consumption");
        MenuItem menuItem2 = new MenuItem("Acceleration/Consumption in mpg");
        MenuItem menuItem3 = new MenuItem("Weight/Horsepower");
        axisOptions.getItems().add(menuItem1);
        axisOptions.getItems().add(menuItem2);
        axisOptions.getItems().add(menuItem3);
        
        
        menuItem3.setOnAction(event -> {
            System.out.println("Option 3 selected via Lambda");
        });
        //TODO add onAction to menuitems
	}
	
	 @FXML
	 public void buttonClicked(Event e){
	        System.out.println("Button clicked");
	 }

}
