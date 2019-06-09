package Ue3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	
	public Label CylinderLabel;
	
	private LineChart linechart;
	
	public void initialize() {
	
		
		final NumberAxis xAxis = new NumberAxis(0, 10, 1);
	    final NumberAxis yAxis = new NumberAxis(-100, 500, 100);        
	    final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);
	    xAxis.setLabel("Acceleration in m/s");                
	    yAxis.setLabel("Wight in kg");
	    sc.setTitle("Cars Overview");
	
		
		
	}

}