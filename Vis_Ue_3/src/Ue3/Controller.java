package Ue3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;



//TODO 1. Add Shape as well as onMouseClick to American cars by adding a Node ///// DONE - PLS Change Diamond to Triangle in Legende
//TODO 2. Add ColorCoding to data
//TODO 3. Add Size to data according to data
//TODO 4. add filters and set xy axis label when applying filter
//TODO 5. Add some kind or range slider or zoom

public class Controller {
	
	public Polygon polygonRight;
	public Polygon polygonLeft;
	
	public Label carLabel;
	public Label modelYearLabel;
	public Label manufacturerLabel;
	public Label countryLabel;
	public Label displacementLabel;
	public Label accelerationLabel;
	public Label horsepowerLabel;
	public Label weightLabel;
	public Label consumptionLabel;
	public Label cylinderLabel;
	
	public MenuButton filterOptions;
	public MenuButton axisOptions;
	
	
	
	public ScatterChart<Number, Number> scatterChart;
	
	private String labelWeight = "Weight in kg";
	private String labelConsumption = "Consumption in mpg";
	
	public Pane chartContainer;
	
	
	private ArrayList<Car> carList = new ArrayList<Car>();
	private ArrayList<Car> europe = new ArrayList<Car>();
	private ArrayList<Car> america = new ArrayList<Car>();
	private ArrayList<Car> japan = new ArrayList<Car>();
	private ArrayList<Car> displayedList = new ArrayList<>();	
	
	XYChart.Series<Number, Number> dataSeriesEurope = new XYChart.Series();
	XYChart.Series<Number, Number> dataSeriesAmerica = new XYChart.Series();
	XYChart.Series<Number, Number> dataSeriesJapan = new XYChart.Series();
	
	public void initialize() {		          
	    scatterChart.getXAxis().setLabel(labelConsumption);
	    scatterChart.getYAxis().setLabel(labelWeight);
	    scatterChart.setAnimated(false);
	    readCSV();
	    splitByCountry();
	    
	    //Todo add filteritems and addOnaction
	    MenuItem filterItem = new MenuItem("All");
	    filterItem.setOnAction(event -> {
	    	displayOnlyOneCountry("default");
	    });
	    MenuItem filterItem1 = new MenuItem("Europe");
	    filterItem1.setOnAction(event -> {
	    	displayOnlyOneCountry("europe");
	    });
	    
	    MenuItem filterItem2 = new MenuItem("America");
	    filterItem2.setOnAction(event -> {
	    	displayOnlyOneCountry("america");
	    });
	    
	    MenuItem filterItem3 = new MenuItem("Japan");
	 
	    filterItem3.setOnAction(event -> {
	    	displayOnlyOneCountry("japan");
	    });
	    
	    
	    filterOptions.getItems().add(filterItem);
	    filterOptions.getItems().add(filterItem1);
	    filterOptions.getItems().add(filterItem2);
	    filterOptions.getItems().add(filterItem3);
	    
	    
	    MenuItem menuItem1 = new MenuItem("Weight/Consumption");
	    menuItem1.setOnAction(event -> {
	    	setAxisMPGWeight();
        });
	    
	    MenuItem menuItem2 = new MenuItem("Horsepower/Consumption");
        menuItem2.setOnAction(event -> {
        	setAxisHorsepowerConsumption();
        });
        MenuItem menuItem3 = new MenuItem("Weight/Horsepower");
        axisOptions.getItems().add(menuItem1);
        axisOptions.getItems().add(menuItem2);
        axisOptions.getItems().add(menuItem3);
        
        
        menuItem3.setOnAction(event -> {
            System.out.println("Option 3 selected via Lambda");
        });
	}
	
	private void clearAllData() {

		scatterChart.getData().clear();
		scatterChart.getData().removeAll(america);
		scatterChart.getData().removeAll(europe);
		scatterChart.getData().removeAll(japan);
		
		
		scatterChart.layout();
		
	}
	
	
	private void displayOnlyOneCountry(String country) {
		
		clearAllData();
		switch(country) {
		case "europe": scatterChart.getData().add(dataSeriesEurope); break;
		case "america": scatterChart.getData().add(dataSeriesAmerica); break;
		case "japan": scatterChart.getData().add(dataSeriesJapan); break;
		default: 
			scatterChart.getData().add(dataSeriesEurope); 
			scatterChart.getData().add(dataSeriesAmerica);
			scatterChart.getData().add(dataSeriesJapan); 
		
		}	
	}
	
	private void setAxisMPGWeight() {
		
		clearAllData();
		
		for ( Car c: europe) {
			XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			data.setNode(new Rectangle(10, 10, Color.RED));
			data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			dataSeriesEurope.getData().add(data);
		}
		
		
		for ( Car c: japan) {
			XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			data.setNode(new Circle(5, Color.BURLYWOOD));
			data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			dataSeriesJapan.getData().add(data);
		}
		

	
		for ( Car c: america) {
			XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			//TODO add node and onclick
			data.setNode(triangle());
			data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			dataSeriesAmerica.getData().add(data);
		}
		
		
		scatterChart.getData().add(dataSeriesEurope);
		scatterChart.getData().add(dataSeriesJapan);
		scatterChart.getData().add(dataSeriesAmerica);
		
	}
	
	private void setAxisHorsepowerConsumption() {
		
		clearAllData();
		for ( Car c: europe) {
			XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			data.setNode(new Rectangle(5, 5, Color.RED));
			dataSeriesEurope.getData().add(data);
		}
		
		
		for ( Car c: japan) {
			XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			data.setNode(new Circle(5, Color.RED));
			dataSeriesJapan.getData().add(data);
		}

		
		for ( Car c: america) {
			XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			data.setNode(triangle());
			dataSeriesAmerica.getData().add(data);
		}
		
		scatterChart.getData().add(dataSeriesEurope);
		scatterChart.getData().add(dataSeriesJapan);
		scatterChart.getData().add(dataSeriesAmerica);
	}
	
	private Node triangle() {
		
		// Create the Triangle
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(6.25, 0.0,  0.0, 6.25, 12.5, 6.25);
        triangle.setFill(Color.GREEN);
        triangle.setStroke(Color.GREEN);
		return triangle;
		
	}
	
	
	private void splitByCountry() {
		europe = new ArrayList<Car>();
		america = new ArrayList<Car>();
		japan = new ArrayList<Car>();
		
		for (Car c : carList) {
			
			switch(c.getOrigin()) {
			
			case "European": 
				europe.add(c); 
				break;
			
			case "American":
				america.add(c);
				break;
			
			case "Japanese":
				japan.add(c);
				break;
			}
		}
		
	
		dataSeriesEurope.setName("Europe");	
		dataSeriesJapan.setName("japan");
		dataSeriesAmerica.setName("American");
		setAxisMPGWeight();
	}
	
	private void readCSV() {
		
			ObservableList<String> items = FXCollections.observableArrayList();
			ObservableList<String> origins = FXCollections.observableArrayList();
		    String csvFile = "/Users/jannikschmitz/Downloads/cars.csv";
	        String line = "";
	        String cvsSplitBy = ";";

	        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	            int i = 0;
	            while ((line = br.readLine()) != null) {

	               String[] s = line.split(cvsSplitBy)[0].split("\\t");
	             
	               	if(i > 0) {
	               		Car newCar = new Car();
	               		newCar.setName(s[0]);
	               		newCar.setManufacturer(s[1]);
	               		newCar.setOrigin(s[9]);
	               		newCar.setAcceleration(Double.parseDouble((!s[7].equals("NA")? s[7]:"0.0")));
	               		newCar.setCylinders(Integer.parseInt((!s[3].equals("NA")? s[3]:"0")));
	               		newCar.setDisplacement(Double.parseDouble((!s[4].equals("NA")? s[4]:"0.0")));
	               		newCar.setHorsepower(Integer.parseInt((!s[5].equals("NA")? s[5]:"0")));
	               		newCar.setModelYear(Integer.parseInt((!s[8].equals("NA")? s[8]:"0" )));
	               		newCar.setMpg(Double.parseDouble((!s[2].equals("NA")? s[2]:"0.0")));
	               		newCar.setWeight((Double.parseDouble((!s[6].equals("NA")? s[6]:"0" )) * 0.453592));
	               		
	               		
	               		carList.add(newCar);
	               	}
	 
	                i++;
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public void setupBottomView(Car c) {
		carLabel.setText("Model: " + c.getName() + "       ");
		modelYearLabel.setText("Model Year: " + c.getModelYear());
		manufacturerLabel.setText("Manufacturer: " + c.getManufacturer());
		accelerationLabel.setText("Acceleration: " + c.getAcceleration());
		displacementLabel.setText("Displacement: " + c.getDisplacement());
		countryLabel.setText("Origin: " + c.getOrigin());
		cylinderLabel.setText("Cylinder " +  c.getCylinders());
		consumptionLabel.setText("mpg: " + c.getMpg());
		horsepowerLabel.setText("Horsepower: " + c.getHorsepower());
		weightLabel.setText("Weight:" + c.getWeight());
	}
	
	 @FXML
	 public void buttonClicked(Event e){
	        System.out.println("Button clicked");
	 }

}
