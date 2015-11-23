package travelSalesman;

import java.util.ArrayList;

public class BruteForce {
	ArrayList<City> listOfCities;

	public Route calculateBruteForce(ArrayList<City> citiesList) {

		long numberOfPerms = numberOfPermCalculator(citiesList.size() - 1);
		listOfCities = citiesList;
		Route route = new Route();
		route.lenght = 2147483647;
		int oneOfFifty = (int)((double)numberOfPerms/50.0);
		for (long i = 0; i < numberOfPerms; ++i) {

			int lenght = 0;
			if (i % oneOfFifty == 0) {
				System.out.print(">");
			}

			for (int j = 0; j < listOfCities.size(); j++) {

				int beginCityNumber = listOfCities.get(j).getCityNumber();

				if (j == listOfCities.size() - 1) {
					int number = (beginCityNumber - 2);
					lenght += listOfCities.get(0).getDistances().get(number);

				} else {

					int endCityNumber;
					if (beginCityNumber > listOfCities.get(j + 1).getCityNumber()) {

						endCityNumber = beginCityNumber;
						beginCityNumber = listOfCities.get(j + 1).getCityNumber();

						lenght += listOfCities.get(j + 1).getDistances()
								.get((endCityNumber - 1) - beginCityNumber);
					} else {
						endCityNumber = listOfCities.get(j + 1).getCityNumber();

						lenght += listOfCities.get(j).getDistances()
								.get((endCityNumber - 1) - beginCityNumber);
					}

				}
			}
			if (lenght < route.lenght) {
				route.lenght = lenght;
				route.listOfCity = new ArrayList<Integer>();
				for (int j = 0; j < listOfCities.size(); j++) {
					route.listOfCity.add(listOfCities.get(j).getCityNumber());
				}
			}

			getNextPerm();
		}
		return route;
	}

	void getNextPerm() {
		int N = listOfCities.size();
		int i = N - 1;
		while (listOfCities.get(i - 1).getCityNumber() >= listOfCities.get(i).getCityNumber())
			i = i - 1;

		int j = N;
		while (listOfCities.get(j - 1).getCityNumber() <= listOfCities.get(i - 1).getCityNumber())
			j = j - 1;

		swap(i - 1, j - 1);

		i++;
		j = N;
		while (i < j) {
			swap(i - 1, j - 1);
			i++;
			j--;
		}
	}

	void swap(int x, int y) {
		City cityY = listOfCities.get(y);
		City cityX = listOfCities.get(x);
		int tempNumber = cityX.getCityNumber();
		ArrayList<Integer> tempList = cityX.getDistances();
		cityX.setCityNumber(cityY.getCityNumber());
		cityX.setDistances(cityY.getDistances());
		cityY.setCityNumber(tempNumber);
		cityY.setDistances(tempList);
	}

	long numberOfPermCalculator(long number) {

		if (number < 1)
			return 1;
		else
			return number * numberOfPermCalculator(number - 1);

	}
}
