package travelSalesman;

public class Cost {
short[][] costs;



public Cost(int row, int col){
	costs = new short[row+1][col+1];
}

public short getCost(int row, int col){
	return costs[row][col];
}

public void assignCost(short num, int row, int col){
	costs[row+1][col+1]=num;
}

}
