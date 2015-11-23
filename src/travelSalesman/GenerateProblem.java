package travelSalesman;

import java.util.ArrayList;
import java.util.Random;

public class GenerateProblem {

	public ArrayList<City> randomProblemGenerator(int numberOfCities){
		Random generator = new Random();
		ArrayList<City> listOfCity=new ArrayList<City>();
		for(int i=1;i<=numberOfCities;++i){
			
			ArrayList<Integer> distances= new ArrayList<Integer>();
			
			for(int j=0;j<numberOfCities-i;++j){
				distances.add(generator.nextInt(9)+1);
			}
			listOfCity.add(new City(distances,i));
			
		}
		return listOfCity;
	}
}
