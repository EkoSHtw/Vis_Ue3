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
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;



//TODO 1. Add Shape as well as onMouseClick to American cars by adding a Node ///// DONE - PLS Change Diamond to Triangle in Legende
//TODO 2. Add ColorCoding to data ///// DONE - HEXtoRGB does the Job. Some Problems from Java Lib to JavaFX Lib fixed with ConverterFunction 
//TODO 3. Add Size to data according to data ///// DONE - With function "cylinderToSize"
//TODO 4. add filters and set xy axis label when applying filter //DONE
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
	public MenuButton filterOptionsManufacturer;
	public MenuButton axisOptions;
	
	public Slider minSliderX;
	public Slider maxSliderX;
	public Slider minSliderY;
	public Slider maxSliderY;
	
	public ScatterChart<Number, Number> scatterChart;
	
	private String labelWeight = "Weight in kg";
	private String labelConsumption = "Consumption in l/km";
	private String labelHorsepower = "Horsepower";
	
	public Pane chartContainer;
	
	
	private ArrayList<Car> carList = new ArrayList<Car>();
	private ArrayList<Car> europe = new ArrayList<Car>();
	private ArrayList<Car> america = new ArrayList<Car>();
	private ArrayList<Car> japan = new ArrayList<Car>();
	private ArrayList<String> manufacturerList = new ArrayList<>();	
	
	XYChart.Series<Number, Number> dataSeriesEurope = new XYChart.Series();
	XYChart.Series<Number, Number> dataSeriesAmerica = new XYChart.Series();
	XYChart.Series<Number, Number> dataSeriesJapan = new XYChart.Series();
	
	private static int ENABLE_ALL = 0;
	private static int ENABLE_EUROPE = 1;
	private static int ENABLE_AMERICA = 2;
	private static int ENABLE_JAPAN = 3;
	private int enabled = 0;
	
	private static final String ALLMANUFACTURER = "All";
	private String displayedManufacturer = "All";
	
	private int selectedAxis = 0; 
	
	
	public void initialize() {		          
	 
	    scatterChart.setAnimated(false);
	    scatterChart.setLegendVisible(false);
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
        menuItem3.setOnAction(event -> {
            setAxisWeightHorsepower();
        });
        
        axisOptions.getItems().add(menuItem1);
        axisOptions.getItems().add(menuItem2);
        axisOptions.getItems().add(menuItem3);
        
        
       
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
		case "europe": 
			scatterChart.getData().add(dataSeriesEurope); 
			enabled = ENABLE_EUROPE;
			break;
		case "america": 
			scatterChart.getData().add(dataSeriesAmerica); 
			enabled = ENABLE_AMERICA;
			break;
		case "japan": 
			scatterChart.getData().add(dataSeriesJapan); 
			enabled = ENABLE_JAPAN;
			break;
		default: 
			scatterChart.getData().add(dataSeriesEurope); 
			scatterChart.getData().add(dataSeriesAmerica);
			scatterChart.getData().add(dataSeriesJapan); 
			enabled = ENABLE_ALL;
		}	
	}
	
	private void setAxisMPGWeight() {
		
		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);
		
		scatterChart.getXAxis().setLabel(labelConsumption);
		scatterChart.getYAxis().setLabel(labelWeight);
		
		
		for ( Car c: europe) {
			
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			
				String colorString = new String(); 
			
				colorString = toHex(c.getManufacturer()); // Get Manufacturer Hex for Color
			
				data.setNode(new Rectangle(10*cylinderToSize(c.getCylinders()), 10*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration()))); // convert It to JavaFX Color
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			
				dataSeriesEurope.getData().add(data);
			}
		}
		
	
		
		for ( Car c: japan) {
			
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			
				String colorString = new String();
			
				colorString = toHex(c.getManufacturer());	
			
				data.setNode(new Circle(5*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			
				dataSeriesJapan.getData().add(data);
			}
		}
		

	
		for ( Car c: america) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data( c.getMpg(), c.getWeight());
			
				String colorString = new String();
			
				colorString = toHex(c.getManufacturer());
			
				System.out.print(c.getCylinders());
			
				System.out.print(c.getAcceleration());
					
				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
			
				dataSeriesAmerica.getData().add(data);
			}
		}
		
		if(enabled == ENABLE_ALL || enabled == ENABLE_EUROPE) scatterChart.getData().add(dataSeriesEurope);
		if(enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)scatterChart.getData().add(dataSeriesJapan);
		if(enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)scatterChart.getData().add(dataSeriesAmerica);
		
	}
	
	private void setAxisHorsepowerConsumption() {
		
		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);
		
		scatterChart.getXAxis().setLabel(labelConsumption);
		scatterChart.getYAxis().setLabel(labelHorsepower);
		
		
		for ( Car c: europe) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(new Rectangle(10*cylinderToSize(c.getCylinders()), 10*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesEurope.getData().add(data);
			}
		}
		
		for ( Car c: japan) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {	
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(new Circle(5*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesJapan.getData().add(data);
			}
		}

		
		for ( Car c: america) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesAmerica.getData().add(data);
			}
		}
		

		if(enabled == ENABLE_ALL || enabled == ENABLE_EUROPE) scatterChart.getData().add(dataSeriesEurope);
		if(enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)scatterChart.getData().add(dataSeriesJapan);
		if(enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)scatterChart.getData().add(dataSeriesAmerica);
	}
	
private void setAxisWeightHorsepower() {
		
		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);
		
		scatterChart.getXAxis().setLabel(labelHorsepower);
		scatterChart.getYAxis().setLabel(labelWeight);
		
		
		for ( Car c: europe) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(new Rectangle(10*cylinderToSize(c.getCylinders()), 10*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesEurope.getData().add(data);
			}
		}
		
		for ( Car c: japan) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(new Circle(5*cylinderToSize(c.getCylinders()), colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesJapan.getData().add(data);
			}
		}

		
		for ( Car c: america) {
			if(displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer())) {
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());
			
				String colorString = new String();
				colorString = toHex(c.getManufacturer());
			
				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesAmerica.getData().add(data);
			}
		}
		

		if(enabled == ENABLE_ALL || enabled == ENABLE_EUROPE) scatterChart.getData().add(dataSeriesEurope);
		if(enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)scatterChart.getData().add(dataSeriesJapan);
		if(enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)scatterChart.getData().add(dataSeriesAmerica);
	}
	
	
	
	private Node triangle(Color color, int cylinders) {
		
		// Create the Triangle
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(6.25*cylinderToSize(cylinders)
        		, 0.0,
        		0.0, 
        		6.25*cylinderToSize(cylinders), 
        		12.5*cylinderToSize(cylinders), 
        		6.25*cylinderToSize(cylinders));    
        triangle.setFill(color);
        //triangle.setStroke(Color.BLACK);
		return triangle;
		
	}
	
	private String toHex(String arg) {
		  return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));

		}
	
	
	private static java.awt.Color hex2Rgb(String colorStr) {
	
			colorStr = colorStr + "0000"; // for smallstrings like "VW"
				
		
	    return new java.awt.Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
	
	private javafx.scene.paint.Color colorConverter(String colorString, double acceleration){
	
		java.awt.Color awtColor = hex2Rgb(colorString) ;
		int r = awtColor.getRed();
		int g = awtColor.getGreen();
		int b = awtColor.getBlue();
		int a = awtColor.getAlpha();
		double opacity = (a - (acceleration * 10)) / 255.0 ;
		javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, opacity);
		return fxColor;
	}
	
	
	private double cylinderToSize(int cylinder) {
		
		double multSize = 1;
		

		if (cylinder == 4) {
			multSize = 1.2;					
		}

		else if (cylinder == 5) {
			multSize = 1.4;			
		}
		
		else if (cylinder == 6) {
			multSize = 1.6;
		}
		
		else if (cylinder == 7) {
			multSize = 1.8;
		}
		
		else if (cylinder == 8) {
			multSize = 2;			
		}
		
		
		return multSize;
		
		
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
		    String csvFile = "C:\\\\Users\\\\User\\\\eclipse-workspace\\\\Ue3\\\\cars.csv";
	        String line = "";
	        String cvsSplitBy = ";";
	        
	        MenuItem filterItem = new MenuItem(ALLMANUFACTURER);
	        displayedManufacturer = filterItem.getText();
    	      filterItem.setOnAction(event -> {
    	    	 displayedManufacturer = filterItem.getText();
    	      });
    	      filterOptionsManufacturer.getItems().add(filterItem);
	        
	        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	            int i = 0;
	            while ((line = br.readLine()) != null) {

	               String[] s = line.split(cvsSplitBy)[0].split("\\t");
	             
	               	if(i > 0) {
	               		Car newCar = new Car();
	               		newCar.setName(s[0]);
	               		newCar.setManufacturer(s[1]);
	               		newCar.setOrigin(s[9]);
	               		newCar.setAcceleration((Double.parseDouble((!s[7].equals("NA")? s[7]:"0.0"))));
	               		newCar.setCylinders(Integer.parseInt((!s[3].equals("NA")? s[3]:"0")));
	               		newCar.setDisplacement((Double.parseDouble((!s[4].equals("NA")? s[4]:"0.0"))* 16.387));
	               		newCar.setHorsepower(Integer.parseInt((!s[5].equals("NA")? s[5]:"0")));
	               		newCar.setModelYear(Integer.parseInt((!s[8].equals("NA")? s[8]:"0" )));
	               		newCar.setMpg((Double.parseDouble((!s[2].equals("NA")? s[2]:"0.0")) * 0.425144));
	               		newCar.setWeight((Double.parseDouble((!s[6].equals("NA")? s[6]:"0" )) * 0.453592));
	               		
	               		if(!manufacturerList.contains(newCar.getManufacturer())) {
	               			manufacturerList.add(newCar.getManufacturer());
	               		  MenuItem filterItem1 = new MenuItem(newCar.getManufacturer());
	              	      filterItem1.setOnAction(event -> {
	              	    	displayedManufacturer = filterItem1.getText();
	              	      });
	              	      filterOptionsManufacturer.getItems().add(filterItem1);
	               		}
	               		carList.add(newCar);
	               	}
	                i++;
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	private void filterManufacturer(String name) {
		clearAllData();
		
		if(enabled == ENABLE_ALL || enabled == ENABLE_EUROPE) scatterChart.getData().add(dataSeriesEurope);
		if(enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)scatterChart.getData().add(dataSeriesJapan);
		if(enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)scatterChart.getData().add(dataSeriesAmerica);
	}
	
	
	public void setupBottomView(Car c) {
		carLabel.setText("Model: " + c.getName() + "       ");
		modelYearLabel.setText("Model Year: " + c.getModelYear());
		manufacturerLabel.setText("Manufacturer: " + c.getManufacturer());
		accelerationLabel.setText("Acceleration: " + (c.getAcceleration() * 1.609) + " m/s");
		displacementLabel.setText("Displacement: " + c.getDisplacement() + " ccm");
		countryLabel.setText("Origin: " + c.getOrigin());
		cylinderLabel.setText("Cylinder " +  c.getCylinders());
		consumptionLabel.setText("Consumption: " + c.getMpg() + " l/km");
		horsepowerLabel.setText("Horsepower: " + c.getHorsepower());
		weightLabel.setText("Weight:" + c.getWeight() + " km");
	}

}
