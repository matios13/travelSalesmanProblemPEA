package travelSalesman;

import java.util.ArrayList;

import BranchAndBound.BranchAndBound;
import BranchAndBound.Cost;
import MyBranchAndBound.MyBranchAndBound;

public class TravelSalesman {
	static int NUMBER_OF_CITIES = 21;
	static boolean IS_RANDOM = false;

	static String XML_NAME = "ulysses16";

	public static void main(String[] args) {
		GenerateProblem problemGenerator = new GenerateProblem();
		BruteForce brutForce = new BruteForce();
		ArrayList<City> listOfCities;
		if (IS_RANDOM) {
			listOfCities = problemGenerator
					.randomProblemGenerator(NUMBER_OF_CITIES);
		} else {
			listOfCities = problemGenerator.generateFromXML(XML_NAME);
			NUMBER_OF_CITIES = problemGenerator.getNumberOfCities(XML_NAME);
		}
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
				.println("------------------------------------Tablica Kosztów---------------------------------------------------------------------");
		for (int i = 1; i < listOfCities.size() + 1; ++i) {
			for (int j = 1; j < listOfCities.size() + 1; ++j) {
				System.out.print(cost.getCost(i, j) + " ");
			}
			System.out.println();
		}
		System.out
				.println("------------------------------------Metoda podzzia³u i ograniczeñ--------------------------------------------------------");
		BranchAndBound bab = new BranchAndBound(cost, listOfCities.size());
		MyBranchAndBound bab2 = new MyBranchAndBound(cost, listOfCities.size());
		bab.generateSolution();
		System.out.println("--------------------------------2    Metoda podzzia³u i ograniczeñ     2-------------------------------------------------------");
		bab2.generateSolution();
		if(bab.getWholeTime()>bab2.getWholeTime())
			System.err.println("szybciej");
		if (NUMBER_OF_CITIES < 5) {
			System.out
					.println("------------------------------------Brute Force--------------------------------------------------------------------------");
			long timeStart = System.currentTimeMillis();
			System.out
					.println("Postêp:\n__________________________________________________");
			timeStart = System.currentTimeMillis();
			Route route = brutForce.calculateBruteForce(listOfCities);
			long timeEnd = System.currentTimeMillis();
			long wholeTime = (timeEnd - timeStart);
			System.out.print("\n\n czas : " + wholeTime + "\n koszt drogi :"
					+ route.lenght + "\n Droga : ");
			for (int j = 0; j < route.listOfCity.size(); j++) {
				if (j != 0)
					System.out.print("->");
				System.out.print(route.listOfCity.get(j));
			}

			System.out
					.println("\n------------------------------------Porównanie--------------------------------------------------------");
			System.out.println("_______________________________" + "\n|Czas |"
					+ wholeTime + "         | " + bab.getWholeTime()
					+ "         | " + bab2.getWholeTime() + "\n|Droga|"
					+ route.lenght + "        | " + bab.getBestTour()
					+ "        | " + bab2.getBestTour());

			if (route.lenght != bab.getBestTour()) {
				System.err
						.println("B³¹d coœ nie dzia³a w algorytmach wynik nie jest równy ");
			}
		}
	}
}
