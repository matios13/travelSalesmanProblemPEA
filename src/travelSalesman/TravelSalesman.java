package travelSalesman;

import java.sql.Time;
import java.util.ArrayList;

public class TravelSalesman {
	static int NUMBER_OF_CITIES = 11;

	public static void main(String[] args) {
		GenerateProblem problemGenerator = new GenerateProblem();
		ArrayList<City> listOfCities = problemGenerator
				.randomProblemGenerator(NUMBER_OF_CITIES);
		BruteForce brutForce = new BruteForce();

		Cost cost = new Cost(NUMBER_OF_CITIES, NUMBER_OF_CITIES);
		for (int i = 0; i < listOfCities.size(); ++i) {
			for (int j = 0; j < listOfCities.size(); ++j) {
				if (j == i) {
					cost.assignCost((short) 0, i, j);
				} else if (j > i) {
					cost.assignCost((short) (listOfCities.get(i).getDistances()
							.get(j - (i + 1)).intValue()), i, j);
					cost.assignCost((short) (listOfCities.get(i).getDistances()
							.get(j - (i + 1)).intValue()), j, i);
				}
			}
		}
		System.out
				.println("------------------------------------Tablica Koszt�w---------------------------------------------------------------------");
		for (int i = 1; i < listOfCities.size() + 1; ++i) {
			for (int j = 1; j < listOfCities.size() + 1; ++j) {
				System.out.print(cost.getCost(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------Metoda podzzia�u i ogranicze�--------------------------------------------------------");
		BranchAndBound bab = new BranchAndBound(cost, listOfCities.size());
		bab.generateSolution();
		System.out
				.println("------------------------------------Brute Force--------------------------------------------------------------------------");
		long timeStart = System.currentTimeMillis();
		System.out.println("Post�p:\n__________________________________________________");
		timeStart = System.currentTimeMillis();
		Route route = brutForce.calculateBruteForce(listOfCities);
		long timeEnd = System.currentTimeMillis();
		long wholeTime = (timeEnd - timeStart);
		System.out.print("\n\n czas : " + wholeTime
				+ "\n koszt drogi :" + route.lenght + "\n Droga : ");
		for (int j = 0; j < route.listOfCity.size(); j++) {
			if (j != 0)
				System.out.print("->");
			System.out.print(route.listOfCity.get(j));
		}

		System.out.println("\n------------------------------------Por�wnanie--------------------------------------------------------");
		System.out.println(
				"_______________________________"+
				"\n|Czas |"+wholeTime+"         | "+ bab.getWholeTime()+
				"\n|Droga|"+route.lenght+"        | "+bab.getBestTour());
		
		if(route.lenght!=bab.getBestTour()){
			System.err.println("B��d co� nie dzia�a w algorytmach wynik nie jest r�wny ");
		}
	}

}
