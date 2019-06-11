package Ue3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.StringConverter;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
	public Label textFilterOrigin;
	public Label textFilterManufacturer;
	public Label textFilterYear;
	public Label textFilterAxis;

	public MenuButton filterOptions;
	public MenuButton filterOptionsManufacturer;
	public MenuButton filterOptionsYear;
	public MenuButton axisOptions;

	public Slider minSliderX;
	public Slider maxSliderX;
	public Slider minSliderY;
	public Slider maxSliderY;
	public Slider minSliderYear;
	public Slider maxSliderYear;
	public Slider minSliderDisplacement;
	public Slider maxSliderDisplacement;
	public Slider minSliderCylinder;
	public Slider maxSliderCylinder;
	
	
	public NumberAxis xAxis;
	public NumberAxis yAxis;
	public Slider yearSlider;
	
	public ScatterChart<Number, Number> scatterChart;

	private String labelWeight = "Weight in kg";
	private String labelConsumption = "Consumption in l/100km";
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
	private int filteredYear = 0;
	private ArrayList<Integer> yearList = new ArrayList<>();

	private int selectedAxis = 0;
	
	private double maxWeight = 0;
	private double minWeight = 0;
	private double maxConsumption = 0;
	private double minConsumption;
	private double maxHorsePower = 0;
	private double minHorsePower = 0;
	private double minAcceleration = 1000;
	private double maxAcceleration = 0;
	private int minYear = 70;
	private int maxYear = 82;
	private double minDisplacement = 3000;
	private double maxDisplacement = 0;
	private int maxCylinders = 0;
	private int minCylinders = 100;
	
	
	public void initialize() {		          
	 
	    scatterChart.setAnimated(false);
	    scatterChart.setLegendVisible(false);
	    readCSV();
	    
	    
	      
	    yearSlider.setMin(69);
	    yearSlider.setMax(82);
	    yearSlider.setValue(69);
	 
	    yearSlider.setBlockIncrement(1);
	    yearSlider.setMajorTickUnit(1);
	    yearSlider.setMinorTickCount(0);
	    yearSlider.setShowTickMarks(true);
	    yearSlider.setShowTickLabels(true);
	    yearSlider.setSnapToTicks(true);
	    
	    
	    yearSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 69.5) return "all";
                if (n < 71.5) return "71";
                if (n < 73.5) return "73";
                if (n < 75.5) return "75";
                if (n < 77.5) return "77";
                if (n < 79.5) return "79";
                if (n < 81.5) return "81";
                
               
                return "all";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "all":
                        return (double) 69;
 
                    default:
                        return (double) 69;
                }
            }
        });
	 
	  
	    
	    //Todo add filteritems and addOnaction
	    MenuItem filterItem = new MenuItem("All");
	    filterItem.setOnAction(event -> {
	    	enabled = ENABLE_ALL;
	    	textFilterOrigin.setText("All");
			reload();
	    });

		
		minSliderYear.setMin(minYear);
		minSliderYear.setMax(maxYear);
		minSliderYear.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		

		maxSliderYear.setMin(minYear);
		maxSliderYear.setMax(maxYear);
		maxSliderYear.valueProperty().setValue(maxYear);
		maxSliderYear.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		
		minSliderYear.setBlockIncrement(1);
		minSliderYear.setMajorTickUnit(1);
		minSliderYear.setMinorTickCount(0);
		minSliderYear.setShowTickMarks(true);
		minSliderYear.setShowTickLabels(true);
		minSliderYear.setSnapToTicks(true);

		maxSliderYear.setBlockIncrement(1);
		maxSliderYear.setMajorTickUnit(1);
		maxSliderYear.setMinorTickCount(0);
		maxSliderYear.setShowTickMarks(true);
		maxSliderYear.setShowTickLabels(true);
		maxSliderYear.setSnapToTicks(true);
		
		
		maxSliderX.setBlockIncrement(1);
		maxSliderX.setMajorTickUnit(1);
		maxSliderX.setMinorTickCount(0);
		maxSliderX.setShowTickMarks(true);
		maxSliderX.setShowTickLabels(true);
		maxSliderX.setSnapToTicks(true);
		
		maxSliderX.valueProperty().setValue(maxAcceleration);
		maxSliderX.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
	    
		
		minSliderX.setBlockIncrement(1);
		minSliderX.setMajorTickUnit(1);
		minSliderX.setMinorTickCount(0);
		minSliderX.setShowTickMarks(true);
		minSliderX.setShowTickLabels(true);
		minSliderX.setSnapToTicks(true);
		minSliderX.valueProperty().setValue(minAcceleration);
		minSliderX.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		
		minSliderDisplacement.setBlockIncrement(1);
		minSliderDisplacement.setMajorTickUnit(150);
		minSliderDisplacement.setMinorTickCount(0);
		minSliderDisplacement.setShowTickMarks(true);
		minSliderDisplacement.setShowTickLabels(true);
		minSliderDisplacement.setSnapToTicks(true);
		minSliderDisplacement.valueProperty().setValue(minDisplacement);
		minSliderDisplacement.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		maxSliderDisplacement.setBlockIncrement(1);
		maxSliderDisplacement.setMajorTickUnit(150);
		maxSliderDisplacement.setMinorTickCount(0);
		maxSliderDisplacement.setShowTickMarks(true);
		maxSliderDisplacement.setShowTickLabels(true);
		maxSliderDisplacement.setSnapToTicks(true);
		maxSliderDisplacement.valueProperty().setValue(maxDisplacement);
		maxSliderDisplacement.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		minSliderCylinder.setBlockIncrement(1);
		minSliderCylinder.setMajorTickUnit(1);
		minSliderCylinder.setMinorTickCount(0);
		minSliderCylinder.setShowTickMarks(true);
		minSliderCylinder.setShowTickLabels(true);
		minSliderCylinder.setSnapToTicks(true);
		minSliderCylinder.valueProperty().setValue(minCylinders);
		minSliderCylinder.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		maxSliderCylinder.setBlockIncrement(1);
		maxSliderCylinder.setMajorTickUnit(1);
		maxSliderCylinder.setMinorTickCount(0);
		maxSliderCylinder.setShowTickMarks(true);
		maxSliderCylinder.setShowTickLabels(true);
		maxSliderCylinder.setSnapToTicks(true);
		maxSliderCylinder.valueProperty().setValue(maxCylinders);
		maxSliderCylinder.valueProperty().addListener((observable, oldValue, newValue) -> {
	           reload();
	    });
		
		splitByCountry();
		
		MenuItem filterItem1 = new MenuItem("Europe");
		filterItem1.setOnAction(event -> {
			enabled = ENABLE_EUROPE;
			textFilterOrigin.setText("Europe");
			reload();
		});

		MenuItem filterItem2 = new MenuItem("America");
		filterItem2.setOnAction(event -> {
			enabled = ENABLE_AMERICA;
			textFilterOrigin.setText("America");
			reload();
		});

		MenuItem filterItem3 = new MenuItem("Japan");
		filterItem3.setOnAction(event -> {
			enabled = ENABLE_JAPAN;
			textFilterOrigin.setText("Japan");
			reload();
		});

		filterOptions.getItems().add(filterItem);
		filterOptions.getItems().add(filterItem1);
		filterOptions.getItems().add(filterItem2);
		filterOptions.getItems().add(filterItem3);

		MenuItem menuItem1 = new MenuItem("Weight/Consumption");
		menuItem1.setOnAction(event -> {
			selectedAxis = 0;
			reload();
		});

		MenuItem menuItem2 = new MenuItem("Consumption/Horsepower");
		menuItem2.setOnAction(event -> {
			selectedAxis = 1;
			reload();
		});
		MenuItem menuItem3 = new MenuItem("Weight/Horsepower");
		menuItem3.setOnAction(event -> {
			selectedAxis = 2;
			reload();
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

	private void setAxisConsumptionWeight() {

		textFilterAxis.setText("Consumption/Weight");

		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);

		scatterChart.getXAxis().setLabel(labelConsumption);
		scatterChart.getYAxis().setLabel(labelWeight);
		
		//xAxis.setLowerBound(minSliderX.valueProperty().intValue());
		//yAxis.setLowerBound(minSliderY.valueProperty().intValue());
//		minSliderY.setMin(minWeight);
//		minSliderY.setMax(maxWeight);
//		maxSliderY.setMax(maxConsumption);
//		maxSliderY.setMin(max);

		for (Car c : europe) {
			
	
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){	
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getWeight());

				String colorString = new String();

				colorString = toHex(c.getManufacturer()); // Get Manufacturer Hex for Color

				data.setNode(new Rectangle(10 * cylinderToSize(c.getCylinders()), 10 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration()))); // convert It to JavaFX Color
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));

				dataSeriesEurope.getData().add(data);
				
			}
		}
		
		/*
		xAxis.setLowerBound(xMin);
		yAxis.setLowerBound(yMin);
		xAxis.setUpperBound(xMax);
		yAxis.setUpperBound(yMax);
		
		*/
		for (Car c : japan) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){
				
				/*
				  && (c.getMpg() >= minSliderX.valueProperty().doubleValue() && c.getMpg() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getWeight() >= minSliderY.valueProperty().doubleValue() && c.getWeight() <= maxSliderY.valueProperty().doubleValue())

				 */
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getWeight());

				String colorString = new String();

				colorString = toHex(c.getManufacturer());

				data.setNode(new Circle(5 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));

				dataSeriesJapan.getData().add(data);
			}
		}

		for (Car c : america) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
				&& (filteredYear == 0 || filteredYear == c.getModelYear())
				&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
				&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
				&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
				&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
			    ){
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getWeight());

				String colorString = new String();

				colorString = toHex(c.getManufacturer());


				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));

				dataSeriesAmerica.getData().add(data);
			}
		}

		if (enabled == ENABLE_ALL || enabled == ENABLE_EUROPE)
			scatterChart.getData().add(dataSeriesEurope);
		if (enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)
			scatterChart.getData().add(dataSeriesJapan);
		if (enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)
			scatterChart.getData().add(dataSeriesAmerica);

	}

	private void setAxisHorsepowerConsumption() {

		textFilterAxis.setText("Consumption/Horsepower");

		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);

		scatterChart.getXAxis().setLabel(labelConsumption);
		scatterChart.getYAxis().setLabel(labelHorsepower);

		for (Car c : europe) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(new Rectangle(10 * cylinderToSize(c.getCylinders()), 10 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesEurope.getData().add(data);
			}
		}

		for (Car c : japan) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue()) 
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(new Circle(5 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesJapan.getData().add(data);
			}
		}

		for (Car c : america) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					) {
				XYChart.Data data = new XYChart.Data(c.getMpg(), c.getHorsepower());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesAmerica.getData().add(data);
			}
		}

		if (enabled == ENABLE_ALL || enabled == ENABLE_EUROPE)
			scatterChart.getData().add(dataSeriesEurope);
		if (enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)
			scatterChart.getData().add(dataSeriesJapan);
		if (enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)
			scatterChart.getData().add(dataSeriesAmerica);
	}

	private void setAxisWeightHorsepower() {

		textFilterAxis.setText("Horsepower/Weight");

		clearAllData();
		dataSeriesEurope.getData().clear();
		dataSeriesEurope.getData().removeAll(europe);
		dataSeriesAmerica.getData().clear();
		dataSeriesAmerica.getData().removeAll(america);
		dataSeriesJapan.getData().clear();
		dataSeriesJapan.getData().removeAll(japan);

		scatterChart.getXAxis().setLabel(labelHorsepower);
		scatterChart.getYAxis().setLabel(labelWeight);
		

		for (Car c : europe) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue()) 
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(new Rectangle(10 * cylinderToSize(c.getCylinders()), 10 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesEurope.getData().add(data);
			}
		}

		for (Car c : japan) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					){
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(new Circle(5 * cylinderToSize(c.getCylinders()),
						colorConverter(colorString, c.getAcceleration())));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesJapan.getData().add(data);
			}
		}

		for (Car c : america) {
			if ((displayedManufacturer.equals(ALLMANUFACTURER) || displayedManufacturer.equals(c.getManufacturer()))
					&& (filteredYear == 0 || filteredYear == c.getModelYear())
					&& (c.getModelYear() >= minSliderYear.valueProperty().intValue() && c.getModelYear() <= maxSliderYear.valueProperty().intValue())
					&& (c.getAcceleration() >= minSliderX.valueProperty().doubleValue() && c.getAcceleration() <= maxSliderX.valueProperty().doubleValue())
					&& (c.getDisplacement() >= minSliderDisplacement.valueProperty().doubleValue() && c.getDisplacement() <= maxSliderDisplacement.valueProperty().doubleValue())
					&& (c.getCylinders() >= minSliderCylinder.valueProperty().doubleValue() && c.getCylinders() <= maxSliderCylinder.valueProperty().doubleValue())
					) {
				XYChart.Data data = new XYChart.Data(c.getHorsepower(), c.getWeight());

				String colorString = new String();
				colorString = toHex(c.getManufacturer());

				data.setNode(triangle(colorConverter(colorString, c.getAcceleration()), c.getCylinders()));
				data.getNode().setOnMouseClicked(e -> setupBottomView(c));
				dataSeriesAmerica.getData().add(data);
			}
		}

		if (enabled == ENABLE_ALL || enabled == ENABLE_EUROPE)
			scatterChart.getData().add(dataSeriesEurope);
		if (enabled == ENABLE_ALL || enabled == ENABLE_JAPAN)
			scatterChart.getData().add(dataSeriesJapan);
		if (enabled == ENABLE_ALL || enabled == ENABLE_AMERICA)
			scatterChart.getData().add(dataSeriesAmerica);
	}

	private Node triangle(Color color, int cylinders) {

		// Create the Triangle
		Polygon triangle = new Polygon();
		triangle.getPoints().addAll(7.0 * cylinderToSize(cylinders), 0.0, 0.0, 7.0 * cylinderToSize(cylinders),
				14 * cylinderToSize(cylinders), 7.0 * cylinderToSize(cylinders));
		triangle.setFill(color);
		// triangle.setStroke(Color.BLACK);
		return triangle;
	}

	private String toHex(String arg) {
		return String.format("%x", new BigInteger(1, arg.getBytes(/* YOUR_CHARSET? */)));

	}

	private static java.awt.Color hex2Rgb(String colorStr) {

		colorStr = colorStr + "0000"; // for smallstrings like "VW"

		return new java.awt.Color(Integer.valueOf(colorStr.substring(1, 3), 16),
				Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	private javafx.scene.paint.Color colorConverter(String colorString, double acceleration) {

		java.awt.Color awtColor = hex2Rgb(colorString);
		int r = awtColor.getRed();
		int g = awtColor.getGreen();
		int b = awtColor.getBlue();
		int a = awtColor.getAlpha();
		double opacity = (a - (acceleration * 10)) / 255.0;
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

			switch (c.getOrigin()) {

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
		setAxisConsumptionWeight();
	}

	private void readCSV() {
		
			ObservableList<String> items = FXCollections.observableArrayList();
			ObservableList<String> origins = FXCollections.observableArrayList();
		    String csvFile = "C:\\Users\\User\\eclipse-workspace\\Ue3\\cars.csv";
		    //C:\\Users\\User\\eclipse-workspace\\Ue3\\cars.csv
	        String line = "";
	        String cvsSplitBy = ";";
	        
	        MenuItem filterItem = new MenuItem(ALLMANUFACTURER);
	        displayedManufacturer = filterItem.getText();
    	      filterItem.setOnAction(event -> {
    	    	 displayedManufacturer = filterItem.getText();
    	    	 textFilterManufacturer.setText(filterItem.getText());
    	    	 reload();
    	      });
    	    filterOptionsManufacturer.getItems().add(filterItem);
    	    MenuItem filterItem0 = new MenuItem("0");
     			filterItem0.setOnAction(event -> {
     				filteredYear = Integer.parseInt(filterItem0.getText());
     				textFilterYear.setText("All");
     				reload();
     			});
     			filterOptionsYear.getItems().add(filterItem0);
    	    
     		
     	// Adding Listener to value property.
     		
     		yearSlider.valueProperty().addListener(new ChangeListener<Number>() {
     	 
     	         @Override
     	         public void changed(ObservableValue<? extends Number> observable, //
     	               Number oldValue, Number newValue) {
     	        	      	
     	        	if(newValue.doubleValue() == 69.00) {
     	        		
     	        		filteredYear = 0;
     	        		textFilterYear.setText("All");
     	        		reload();
     	        		
     	        	}
     	        	
     	        	else {
     	 
     	        	filteredYear = newValue.intValue();
     	        	textFilterYear.setText(newValue.toString());
     	        	reload();
     	        	
     	        	}
     	         }
     	      });
     		
    	      

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			int i = 0;
			
			
			
			while ((line = br.readLine()) != null) {

				String[] s = line.split(cvsSplitBy)[0].split("\\t");

				if (i > 0) {
					Car newCar = new Car();
					newCar.setName(s[0]);
					newCar.setManufacturer(s[1]);
					newCar.setOrigin(s[9]);
					newCar.setAcceleration((Double.parseDouble((!s[7].equals("NA") ? s[7] : "0.0"))));
					newCar.setCylinders(Integer.parseInt((!s[3].equals("NA") ? s[3] : "0")));
					newCar.setDisplacement((Double.parseDouble((!s[4].equals("NA") ? s[4] : "0.0")) * 16.387));
					newCar.setHorsepower(Integer.parseInt((!s[5].equals("NA") ? s[5] : "0")));
					newCar.setModelYear(Integer.parseInt((!s[8].equals("NA") ? s[8] : "0")));
					newCar.setMpg(mpgToLiterPro100Kilometer((Double.parseDouble((!s[2].equals("NA") ? s[2] : "0.0"))))); //* 0.425144
					newCar.setWeight((Double.parseDouble((!s[6].equals("NA") ? s[6] : "0")) * 0.453592));
					
					/*
					if(newCar.getWeight() > maxWeight) {
						maxWeight = newCar.getWeight();
					}
					
					if(newCar.getWeight() < minWeight) {
						minWeight = newCar.getWeight();
					}
					
					if(newCar.getHorsepower() > maxHorsePower) {
						maxHorsePower = newCar.getHorsepower();
					}
					
					if(newCar.getHorsepower() < minHorsePower) {
						minHorsePower = newCar.getHorsepower();
					}
					
					if(newCar.getMpg() > maxConsumption) {
						maxConsumption = newCar.getMpg();
					}
					
					if(newCar.getMpg() < minConsumption) {
						minConsumption = newCar.getMpg();
					}*/
					
					if(newCar.getAcceleration() < minAcceleration) {
						minAcceleration = newCar.getAcceleration();
					}
					if(newCar.getAcceleration() > maxAcceleration) {
						maxAcceleration = newCar.getAcceleration();
					}
					
					if(newCar.getDisplacement() < minDisplacement) {
						minDisplacement = newCar.getAcceleration();
					}
					if(newCar.getDisplacement() > maxDisplacement) {
						maxDisplacement = newCar.getDisplacement();
					}
					if(newCar.getCylinders() > maxCylinders) {
						maxCylinders = newCar.getCylinders();
					}
					
					if(newCar.getCylinders() < minCylinders) {
						minCylinders = newCar.getCylinders();
					}
					
					if (!manufacturerList.contains(newCar.getManufacturer())) {
						manufacturerList.add(newCar.getManufacturer());
						MenuItem filterItem1 = new MenuItem(newCar.getManufacturer());
						filterItem1.setOnAction(event -> {
							displayedManufacturer = filterItem1.getText();
							textFilterManufacturer.setText(filterItem1.getText());
							reload();
						});
						filterOptionsManufacturer.getItems().add(filterItem1);
					}

					if (!yearList.contains(newCar.getModelYear())) {
						yearList.add(new Integer(newCar.getModelYear()));
						MenuItem filterItem2 = new MenuItem(newCar.getModelYear() + "");
						filterItem2.setOnAction(event -> {
							filteredYear = Integer.parseInt(filterItem2.getText());
							textFilterYear.setText(filterItem2.getText());
							reload();
						});
						filterOptionsYear.getItems().add(filterItem2);
					}
					carList.add(newCar);
				}
				i++;
				
			}
			minSliderX.setMax(maxAcceleration);
			minSliderX.setMin(minAcceleration);
			maxSliderX.setMin(minAcceleration);
			maxSliderX.setMax(maxAcceleration);
			minSliderDisplacement.setMax(maxDisplacement);
			minSliderDisplacement.setMin(minDisplacement);
			maxSliderDisplacement.setMax(maxDisplacement);
			maxSliderDisplacement.setMin(minDisplacement);
			minSliderCylinder.setMax(maxCylinders);
			minSliderCylinder.setMin(minCylinders);
			maxSliderCylinder.setMax(maxCylinders);
			maxSliderCylinder.setMin(minCylinders);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {

		switch (selectedAxis) {

		case 0:
			setAxisConsumptionWeight();
			break;
		case 1:
			setAxisHorsepowerConsumption();
			break;
		case 2:
			setAxisWeightHorsepower();
			break;
		}
	}

	public void setupBottomView(Car c) {
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		carLabel.setText("Model: " + c.getName() + "       ");
		modelYearLabel.setText("Model Year: " + c.getModelYear());
		manufacturerLabel.setText("Manufacturer: " + c.getManufacturer());
		accelerationLabel.setText("Acceleration: " + (df.format(c.getAcceleration())) + " s (0 to 100km/h)");
		displacementLabel.setText("Displacement: " + df.format(c.getDisplacement()) + " ccm");
		countryLabel.setText("Origin: " + c.getOrigin());
		cylinderLabel.setText("Cylinder " + c.getCylinders());
		consumptionLabel.setText("Consumption: " + df.format(c.getMpg()) + " l/100km");
		horsepowerLabel.setText("Horsepower: " + c.getHorsepower());
		weightLabel.setText("Weight: " + df.format(c.getWeight()) + " kg");

	}
	
	public double mpgToLiterPro100Kilometer(double mpg) {
		
		double result;
		
		
		double kilometer = mpg * 1.6;
		double sub = 100/kilometer;
		result = 3.7 * sub;
		
		if(mpg == 0.0) {
			
			result = 0.0;
			
		}
		
		return result;
		
	}

}
