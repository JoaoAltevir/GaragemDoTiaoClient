package entities;

public class Vehicle {

	private String brand;
	private String model;
	private String type;
	private String licensePlate;

	public Vehicle() {}

	public Vehicle(String brand, String model, String type, String licensePlate) {
		this.brand = brand;
		this.model = model;
		this.type = type;
		this.licensePlate = licensePlate;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

}
