package travelSalesman;

import static java.lang.Math.pow;

import java.util.ArrayList;

public class TravelSalesman {
	static int NUMBER_OF_CITIES = 12;
	public static final String ANSI_GREEN = "\u001B[32m";

	public static void main(String[] args) {
		GenerateProblem problemGenerator = new GenerateProblem();
		ArrayList<City> listOfCities = problemGenerator
				.symetricProblemGenerator(NUMBER_OF_CITIES);
		ArrayList<City> listOfCitiesForBruteForce = (ArrayList<City>) listOfCities
				.clone();
		System.out.print("   ");
		for (int i = 0; i < NUMBER_OF_CITIES; i++) {
			System.out.print("| ");
			if (i < 10)
				System.out.print(" ");
			System.out.print(i + " ");
		}
		System.out.println("|");
		for (int i = 0; i < NUMBER_OF_CITIES; i++) {
			System.out.print("______");
		}
		System.out.println();
		for (int i = 0; i < NUMBER_OF_CITIES; i++) {
			City city = listOfCities.get(i);
			if (city.cityNumber < 10)
				System.out.print(" ");
			System.out.print(city.cityNumber + " | ");
			for (int j = 0; j < city.distances.size(); j++) {
				if (city.distances.get(j) < 10)
					System.out.print(" ");
				System.out.print(city.distances.get(j) + " | ");
			}
			System.out.println();
		}
		System.out
				.println("--------------------------------------Wyżarzanie-----------------------------------------------");
		ArrayList<Double> drogiSA = new ArrayList<Double>();
		ArrayList<Double> czasySA = new ArrayList<Double>();
		ArrayList<Integer> drogiBF = new ArrayList<Integer>();
		ArrayList<Double> czasyBF = new ArrayList<Double>();
		ArrayList<City> cities;
		for (int i = 1; i <= 6; i++) {
			cities = problemGenerator.symetricProblemGenerator(NUMBER_OF_CITIES);
			SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(new ArrayList<>(cities));
			simulatedAnnealing.actualTemperature = pow(10, i);
			double timeStart = System.currentTimeMillis();
			simulatedAnnealing.runSimulatedAnnealing();
			double timeEnd = System.currentTimeMillis();
			czasySA.add(timeEnd - timeStart);
			drogiSA.add(simulatedAnnealing.getBestSolutionLength());
			
			BruteForce bruteForce = new BruteForce();
			double timeStart2 = System.currentTimeMillis();
			Route route = bruteForce.calculateBruteForce(new ArrayList<>(cities));
			double timeEnd2 = System.currentTimeMillis();
			
			czasyBF.add(timeEnd2 - timeStart2);
			drogiBF.add(route.lenght);
		}
		
		System.out.println("Czasy: temp=10^x: [symulowane_wyzarzanie / brute force = wynik]");
		for(int i = 0; i < 6; i++){
			System.out.println("10^" + (i+1) + " [" + czasySA.get(i) + "/" + czasyBF.get(i) + " = " + czasySA.get(i)/czasyBF.get(i) + "] ");
		}
		
		System.out.println("Drogi: temp=10^x: [symulowane_wyzarzanie / brute force = wynik]");
		for(int i = 0; i < 6; i++){
			System.out.println("10^" + (i+1) + " [" + drogiSA.get(i) + "/" + drogiBF.get(i) + " = " + drogiSA.get(i)/drogiBF.get(i) + "] ");
		}

/*		System.out.println("Czas symulowanego wyżarzania: "
				+ (timeEnd - timeStart));
		System.out.println("Dlugosc drogi: "
				+ simulatedAnnealing.getBestSolutionLength());
		System.out.println("Sciezka: " + simulatedAnnealing.bestRoadToString());*/
		/*System.out
				.println("-------------------------------------BruteForce-----------------------------------------------");
		BruteForce bruteForce = new BruteForce();
		System.out
				.println("Postęp:\n__________________________________________________");
		double timeStart = System.currentTimeMillis();
		Route route = bruteForce.calculateBruteForce(listOfCitiesForBruteForce);
		double timeEnd = System.currentTimeMillis();
		double wholeTime = (timeEnd - timeStart);
		System.out.print("\n\n czas : " + wholeTime + "\n koszt drogi :"
				+ route.lenght + "\n Droga : ");
		for (int j = 0; j < route.listOfCity.size(); j++) {
			if (j != 0)
				System.out.print("->");
			System.out.print(route.listOfCity.get(j));
		}*/
	}

}
