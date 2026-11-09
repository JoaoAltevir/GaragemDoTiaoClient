package entities;

public class Vaga {

	private String spotName;
	private String status;
	private int floor;
	private String type;
	private String licensePlate;

	public Vaga() {}

	public Vaga(String spotName, String status, int floor, String type, String licensePlate) {
		this.spotName = spotName;
		this.status = status;
		this.floor = floor;
		this.type = type;
		this.licensePlate = licensePlate;
	}

	public String getSpotName() {
		return spotName;
	}

	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
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
