package undone;

import java.util.ArrayList;

public class CalculateFromTree {
	
	int[] citiesPairs;
	ArrayList<Integer> listOfIndexes;
	int lenght;
	int finalCost=2000000;
	int[][] costs;
	ArrayList<Integer> tempRoad;
	ArrayList<Integer> finalRoad;
	
	public CalculateFromTree(int[][] matrix, int []roads){
		costs=matrix.clone();
		citiesPairs=roads;
		lenght=roads.length;
		listOfIndexes= new ArrayList<Integer>();
		for(int i =0 ; i<roads.length;i++){
			listOfIndexes.add(i);
		}
		tempRoad = new ArrayList<Integer>();
		finalRoad =new ArrayList<Integer>();
		
	}
	public ArrayList<Integer> calculate(){
		connect(0,0);
		System.out.println("Final ROAD");
		for (Integer integer : finalRoad) {
			System.out.print(" - "+integer);
		}
		//System.out.println(" \n le : "+);
		return finalRoad;
	}
	
	private void connect(int actualCity, int tempCost){
		//System.out.print(listOfIndexes.size()/2+".) - ");
        //System.out.print(citiesPairs[actualCity]+"  ");

        if(listOfIndexes.size()>2){
                Integer numberOneToRemove = actualCity;
                Integer numberSecondToRemove;
                if(actualCity%2==0){
                        numberSecondToRemove = actualCity+1;
                }else{
                        numberSecondToRemove = actualCity-1;
                }
                listOfIndexes.remove(numberOneToRemove);
                listOfIndexes.remove(numberSecondToRemove);
                int nodeCost;
                for(Integer i=0;i<lenght;i++){
                       
                        if(listOfIndexes.contains(i)){
                               
                                if(actualCity%2==0){
                                        nodeCost=tempCost+costs[citiesPairs[actualCity+1]][citiesPairs[i]];
                                        tempRoad.add(citiesPairs[actualCity+1]);
                                        tempRoad.add(citiesPairs[i]);
                                }else{
                                        nodeCost=tempCost+costs[citiesPairs[actualCity-1]][citiesPairs[i]];
                                        tempRoad.add(citiesPairs[actualCity-1]);
                                        tempRoad.add(citiesPairs[i]);
                                }
                                if(nodeCost<=finalCost)
                                        connect( i, nodeCost);
                                tempRoad.remove(new Integer(citiesPairs[i]));
                                if(actualCity%2==0){
                                    tempRoad.remove(new Integer(citiesPairs[actualCity+1]));
                                }else{
                                    tempRoad.remove(new Integer(citiesPairs[actualCity-1]));                   
                            }
                        }
                }      
                listOfIndexes.add(numberOneToRemove);
                listOfIndexes.add(numberSecondToRemove);
        }else{
                int endCity=0;
                if(actualCity%2==0){
                        endCity= actualCity+1;
                }else{
                        endCity = actualCity-1;
                }
                if(tempCost+costs[citiesPairs[endCity]][citiesPairs[0]]<finalCost){
                        finalCost = tempCost+costs[citiesPairs[endCity]][citiesPairs[0]];
                        finalRoad = (ArrayList<Integer>) tempRoad.clone();
                        finalRoad.add(citiesPairs[endCity]);
                        finalRoad.add(citiesPairs[0]);
                }
                System.out.println(" cost : "+finalCost+"\n");
        }
       
}
}
