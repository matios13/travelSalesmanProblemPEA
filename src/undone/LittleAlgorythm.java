package undone;

import java.util.ArrayList;

import travelSalesman.City;
import travelSalesman.Route;

public class LittleAlgorythm {
	private Route route;
	private int[][] matrix;
	private int numberOfCities ;
	private int LB ;
	private int[][]cities;
	private int jResult;
	private int iResult;
	private int zeroCounter=0;
	public Route calculateLittleAlgorythm(ArrayList<City> listOfCities) {
		cities = new int[listOfCities.size()][listOfCities.size()];
		int[] tableOfCities = new int[listOfCities.size()];
		for(int i=0;i<listOfCities.size();++i){
			tableOfCities[i]=0;
		}
		for(int i=0;i<listOfCities.size();++i){
			for(int j=0;j<listOfCities.size();++j){
				cities[i][j]=-1;
			}
		}
		route = new Route();
		LB=0;
		numberOfCities=listOfCities.size();
		madeMatrix(listOfCities);
		
		int[][] tempMatrix = new int[numberOfCities][numberOfCities];
		
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = 0; j < numberOfCities; j++) {
				if(matrix[i][j]>-1){
					tempMatrix[i][j]=matrix[i][j];
					tempMatrix[j][i]=matrix[i][j];
				}
				if(i==j){
					tempMatrix[i][j]=0;
				}
			}
		}
		 //displayMatrix();
		// System.out.println("-------------------------");
		for(int counter=0;counter<numberOfCities-1;counter++){
			
			if(counter==0){
				LB=findMinimumWithZero();
			}else
				findMinimumWithZero();

			findMaximum();
			tableOfCities[iResult]++;
			tableOfCities[jResult]++;
			if (iResult != 0 || jResult != 0) {
				if (iResult > jResult) {
					cities[counter][0] = jResult;
					cities[counter][1] = iResult;
				} else {
					cities[counter][0] = iResult;
					cities[counter][1] = jResult;
				}
			}else{
				zeroCounter++;
			}
			
			//System.out.println("I : "+cities[counter][0]+" J : "+cities[counter][1]);

		}
		//System.out.println("LB = "+LB);
		int counter=0;
		int numberOfFirst=-1;
		for(int i=0;i<listOfCities.size();++i){
			if(tableOfCities[i]==0){
				counter++;
			}
			if(numberOfFirst<0&&tableOfCities[i]==1){
				numberOfFirst=tableOfCities[i];
			}
		}
		
		connect(numberOfFirst);

		
		ArrayList<Integer> listOfPairs =new ArrayList<Integer>();
		for(int i=0;i<numberOfCities;i++){
			for(int j=0;j<numberOfCities;j++){
				if (cities[i][j]==-1){
					if(j>1){
						listOfPairs.add(cities[i][j-1]);
					}
					break;
				}else{
					if(j==0)
						listOfPairs.add(cities[i][j]);
				}
			}
		}
		for(int i=0;i<listOfCities.size();++i){
			if(tableOfCities[i]==0){
				listOfPairs.add(i);
				listOfPairs.add(i);
			}
		}
		int[] tableOfPairs = new int[listOfPairs.size()];
		int i=0;
		for (Integer integer : listOfPairs) {
			tableOfPairs[i]=integer;
			i++;
		}
		CalculateFromTree calculateFromTree = new CalculateFromTree(tempMatrix,tableOfPairs);
		ArrayList<Integer> Connections = calculateFromTree.calculate();
		System.out.println("LB: "+LB);
		//displayMatrix();
		return route;
	}
	
	private void connect(int numberOfFirst){
		boolean end = false;
		int actualRow=0;
		for(int i=0;i<numberOfCities;++i){
			if(cities[i][0]==numberOfFirst||cities[i][1]==numberOfFirst){
				actualRow=i;
				break;
			}
		}
		int lastOne=cities[actualRow][1];
		boolean swaped=false;
		int withoutSwaped=0;
		while(withoutSwaped!=numberOfCities-2){
			swaped=false;
			int counter=0;
			
			while(cities[actualRow][counter]!=-1){
				lastOne=cities[actualRow][counter];
				counter++;
			}
			for (int i = 0; i < numberOfCities; ++i) {
				if (actualRow != i && cities[i][0] == lastOne) {
					swaped = true;
					withoutSwaped = 0;
					swapRows(actualRow, i);
					break;
				}

			}
			if(!swaped){
				withoutSwaped++;
				if(actualRow==numberOfCities-2){
					actualRow=0;
				}else{
					actualRow++;
				}
			}
		}
	}
	private void swapRows(int parent, int child){
		int i =1;
		cities[child][0]=-1;
		int actualColumn=0;
		while (cities[parent][actualColumn]!=-1){
			actualColumn++;
		}
		while(cities[child][i]!=-1){
			cities[parent][actualColumn]=cities[child][i];
			cities[child][i]=-1;
			actualColumn++;
			i++;
		}
	}
	
	private int findMinimumWithZero() {
		int tempLb=0;
		for (int i = 0; i < numberOfCities; ++i) {
			int minimumRow = 2147483647;
			int minimumColumn = 2147483647;
			for (int j = 0; j < numberOfCities; ++j) {
				if(matrix[i][j]>=0&&matrix[i][j]<minimumRow){
					minimumRow=matrix[i][j];
				}
				if(matrix[j][i]>=0&&matrix[j][i]<minimumColumn){
					minimumColumn=matrix[j][i];
				}
			}
			if(minimumRow != 2147483647){
				tempLb+=minimumRow;
			}
			if(minimumColumn != 2147483647){
				tempLb+=minimumColumn;
			}
			for (int j = 0; j < numberOfCities; ++j) {
				if(matrix[i][j]>=0){
					matrix[i][j]-=minimumRow;
				}
				if(matrix[j][i]>=0){
					matrix[j][i]-=minimumColumn;
				}
			}
			

		}
		return tempLb;
	}
	
	private void findMaximum(){
		int maximumRow=-1;
		int maximumColumn=-1;
		
		int rowI=0;
		int rowJ =0;
		int columnI =0;
		int columnJ =0;
		
		for (int i = 0; i < numberOfCities; ++i) {
			int minimumRow = 2147483647;
			int minimumColumn = 2147483647;
			
			int zeroColumnCounter=0;
			int zeroRowCounter=0;
			
			int TempRowI=0;
			int TempRowJ =0;
			int TempColumnI =0;
			int TempColumnJ =0;
			
			for (int j = 0; j < numberOfCities; ++j) {
				if(((zeroRowCounter>0 && matrix[i][j]>=0)||(matrix[i][j]>0))&&matrix[i][j]<minimumRow){
					minimumRow=matrix[i][j];
					TempRowI=i;
					TempRowJ=j;
				}
				if(matrix[i][j]==0){
					zeroRowCounter++;
				}
				if(((zeroColumnCounter>0 && matrix[j][i]>=0)||(matrix[j][i]>0))&&matrix[j][i]<minimumColumn){
					TempColumnI=i;
					TempColumnJ=j;
					
					minimumColumn=matrix[j][i];
				}
				if(matrix[j][i]==0){
					zeroColumnCounter++;
				}
				
			}
			if (minimumColumn != 2147483647&&minimumColumn>maximumColumn){
				maximumColumn=minimumColumn;
				columnI=TempColumnI;
				columnJ=TempColumnJ;
				
			}
			if (minimumRow != 2147483647&&minimumRow>maximumRow){
				rowI=TempRowI;
				rowJ=TempRowJ;
				maximumRow=minimumRow;
			}
		}
		//System.out.println("MaximumRow " + maximumRow+" I :"+rowI+" J "+rowJ+" \nmaxCol : "+maximumColumn+" I "+columnI+ " J " +columnJ);
		if(maximumRow>maximumColumn){
			//LB+=maximumRow;
			boolean findZero=false;
			for(int i=0;i<numberOfCities; ++i){
				if(!findZero&&matrix[rowI][i]==0){
					findZero=true;
					rowJ=i;
				}
				matrix[rowI][i]=-1;
			}
			for(int i=0;i<numberOfCities; ++i){
				matrix[i][rowJ]=-1;
			}
			iResult =rowI;
			jResult = rowJ;
		}else{
			//LB+=maximumColumn;
			boolean findZero=false;
			for(int i=0;i<numberOfCities; ++i){
				if(!findZero&&matrix[i][columnI]==0){
					findZero=true;
					columnJ=i;
				}
				matrix[i][columnI]=-1;
			}
			for(int i=0;i<numberOfCities; ++i){
				matrix[columnJ][i]=-1;
			}
			iResult =columnI;
			jResult = columnJ;
		}
	}
	
	private void madeMatrix(ArrayList<City> listOfCities) {
		matrix = new int[listOfCities.size()][listOfCities.size()];

		for (int i = 0; i < listOfCities.size(); ++i) {
			for (int j = 0; j < listOfCities.size(); ++j) {
				if (j <= i) {
					matrix[i][j] = -1;
				} else {
					matrix[i][j] = listOfCities.get(i).getDistances().get(j- (i + 1));
				}
			}
		}
	}
	
	private void displayMatrix() {

		for (int i = 0; i < numberOfCities; ++i) {
			for (int j = 0; j < numberOfCities; ++j) {
				if(matrix[i][j]<10&&matrix[i][j]>=0){
					System.out.print(" ");
				}
				if(matrix[i][j]==-1){
					System.out.print(" X  ");
				}else{
					System.out.print(matrix[i][j]+"  ");
				}
			}
			System.out.print("\n");
		}
	}
}
