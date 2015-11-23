package travelSalesman;

import java.util.ArrayList;

public class City {
	private ArrayList<Integer> distances;
	private int cityNumber;
	
	public ArrayList<Integer> getDistances() {
		return distances;
	}
	public void setDistances(ArrayList<Integer> distances) {
		this.distances = distances;
	}
	public int getCityNumber() {
		return cityNumber;
	}
	public void setCityNumber(int cityNumber) {
		this.cityNumber = cityNumber;
	}
	public City(){

	}
	public City(ArrayList<Integer> distances, int citynumber){
		this.distances=distances;
		this.cityNumber=citynumber;
	}
	
	public City(int citynumber){
		this.cityNumber=citynumber;
	}
}
