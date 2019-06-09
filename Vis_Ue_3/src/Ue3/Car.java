package Ue3;

public class Car {
	
	
	//added default values
	private String name = "";
	private String manufacturer= "";
	private String origin = "";
	private int modelYear = 0;
	private int weight = 0;
	private double mpg = 0.0;
	private int horsepower = 0;
	private int displacement = 0;
	private double acceleration = 0.0;
	private int cylinders = 0;

	public Car() {
		
	}
	
	public Car(String name, String manufacturer, String origin, int modelYear, int weight, double mpg,
			int horsepower, int displacement, double acceleration, int cylinders) {
		super();
		this.name = name;
		this.manufacturer = manufacturer;
		this.origin = origin;
		this.modelYear = modelYear;
		this.weight = weight;
		this.mpg = mpg;
		this.horsepower = horsepower;
		this.displacement = displacement;
		this.acceleration = acceleration;
		this.cylinders = cylinders;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public double getMpg() {
		return mpg;
	}

	public void setMpg(double mpg) {
		this.mpg = mpg;
	}

	public int getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(int horsepower) {
		this.horsepower = horsepower;
	}

	public int getDisplacement() {
		return displacement;
	}

	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public int getCylinders() {
		return cylinders;
	}

	public void setCylinders(int cylinders) {
		this.cylinders = cylinders;
	}
	
}
