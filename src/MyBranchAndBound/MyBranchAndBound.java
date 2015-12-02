package MyBranchAndBound;

import java.awt.Point;
import java.util.ArrayList;

import travelSalesman.TimeInterval;
import BranchAndBound.Cost;

public class MyBranchAndBound {
	private int numRows;
	private int numCols;
	private TimeInterval t = new TimeInterval();
	private int bestTour = Integer.MAX_VALUE /4;
	private MyNode bestNode;
	public ArrayList<ArrayList<Short>> costList;
	public ArrayList<Point> newEdge = new ArrayList<Point>();
	private int newNodeCount = 0;
	private int numberPrunedNodes = 0;
	private long wholeTime = 0;


	public MyBranchAndBound(Cost cost, int size) {
		numRows = numCols = size;
		costList = new ArrayList<ArrayList<Short>>();
		for(int i=1;i<=size;i++){
			ArrayList<Short> temp = new ArrayList<Short>();
			for(int j = 1;j<=size;j++){
				temp.add( cost.getCost(i, j));
			}
			costList.add(temp);
		}
	}

	public void generateSolution() {
		Point pt;
		for (int row = 1; row <= numRows; row++) {
			for (int col = row + 1; col <= numCols; col++) {
				pt = new Point(row, col);
				newEdge.add(pt);
				pt = new Point(-row, -col);
				newEdge.add(pt);
			}
		}

		MyNode root = new MyNode(numRows, numCols);
		newNodeCount++;
		t.startTiming();
		root.computeLowerBound(costList);		
		branchAndBound(root, -1);
		t.endTiming();
		wholeTime=t.getElapsedTime();
		if (bestNode != null) {
			System.out.println("\n\nKoszt optymalnej drogi: " + bestTour
					+ "\nDroga: " + bestNode.tour()
					+ "\nWygenerowano w�zl�w: " + newNodeCount
					+ "\nliczba opuszczonych w�z��w: " + numberPrunedNodes);
		} else {
			System.out.println("Tour obtained heuristically is the best tour.");
		}
		System.out.println("Czas algorytmu dla metody PiOna listach: "
				+ wholeTime + " ms.");
		System.out.println();
	}

	private void branchAndBound(MyNode node, int edgeIndex) {
		if (node != null && edgeIndex < newEdge.size()) {
			MyNode leftChild, rightChild;
			int leftEdgeIndex = 0, rightEdgeIndex = 0;
			if (node.isTour()) {
				node.setTour(costList);
				if (node.getTourCost() < bestTour) {
					bestTour = node.getTourCost();
					bestNode = node;
                    System.out.println("\n\nBest tour cost so far: " + 
                            bestTour + "\nBest tour so far: " + 
                            bestNode.tour() + 
                            "\nNumber of nodes generated so far: " + 
                            newNodeCount + 
                            "\nTotal number of nodes pruned so far: " + 
                            numberPrunedNodes + 
                            "\nElapsed time to date for branch and bound: " + 
                                  t.getElapsedTime() + " seconds.\n"); 
				}
			} else {
				if (node.lowerBound() < 2 * bestTour) {
					System.out.println("tu2");
					// Create left child node
					leftChild = new MyNode(numRows, numCols);
					newNodeCount++;
                    
					if (newNodeCount % 1000 == 0) {
						Point p = (Point) newEdge.get(edgeIndex);
		
					} else if (newNodeCount % 25 == 0) {
					}
					if (newNodeCount % 10000 == 0 && bestNode != null) {
                        System.out.println( 
                                "\n\nBest tour cost so far: " + 
                                bestTour + "\nBest tour so far: " + 
                                bestNode.tour()); 
					}
					
					leftChild.setConstraint(copyConstraint(node.constraint()));
					if (edgeIndex != -1
							&& ((Point) newEdge.get(edgeIndex)).getX() > 0) {
						edgeIndex += 2;
					} else {
						edgeIndex++;
					}
					if (edgeIndex >= newEdge.size()) {
						return;
					}
					Point p = (Point) newEdge.get(edgeIndex);
					leftEdgeIndex = leftChild.assignPoint(p, edgeIndex,newEdge );
					leftChild.addDisallowedEdges();
					leftChild.addRequiredEdges();
					leftChild.addDisallowedEdges();
					leftChild.addRequiredEdges();
					leftChild.computeLowerBound(costList);
					if (leftChild.lowerBound() >= 2 * bestTour) {
						leftChild = null;
						numberPrunedNodes++;
					}
					// Create right child node
					rightChild = new MyNode(numRows, numCols);
					newNodeCount++;
					if (newNodeCount % 1000 == 0) {
					} else if (newNodeCount % 25 == 0) {
					}
					rightChild.setConstraint(copyConstraint(node.constraint()));
					if (leftEdgeIndex >= newEdge.size()) {
						return;
					}
					p = (Point) newEdge.get(leftEdgeIndex + 1);
					rightEdgeIndex = rightChild.assignPoint(p,
							leftEdgeIndex + 1,newEdge);
					rightChild.addDisallowedEdges();
					rightChild.addRequiredEdges();
					rightChild.addDisallowedEdges();
					rightChild.addRequiredEdges();
					rightChild.computeLowerBound(costList);
					if (rightChild.lowerBound() > 2 * bestTour) {
						rightChild = null;
						numberPrunedNodes++;
					}
					if (leftChild != null && rightChild == null) {
						branchAndBound(leftChild, leftEdgeIndex);
					} else if (leftChild == null && rightChild != null) {
						branchAndBound(rightChild, rightEdgeIndex);
					} else if (leftChild != null
							&& rightChild != null
							&& leftChild.lowerBound() <= rightChild
									.lowerBound()) {
						if (leftChild.lowerBound() < 2 * bestTour) {
							System.out.println("left "+leftEdgeIndex);
							branchAndBound(leftChild, leftEdgeIndex);
						} else {
							leftChild = null;
							numberPrunedNodes++;
						}
						if (rightChild.lowerBound() < 2 * bestTour) {
							branchAndBound(rightChild, rightEdgeIndex);
						} else {
							rightChild = null;
							numberPrunedNodes++;
						}
					} else if (rightChild != null) {
						if (rightChild.lowerBound() < 2 * bestTour) {
							branchAndBound(rightChild, rightEdgeIndex);
						} else {
							rightChild = null;
							numberPrunedNodes++;
						}
						if (leftChild.lowerBound() < 2 * bestTour) {
							System.out.println("left "+leftEdgeIndex);
							branchAndBound(leftChild, leftEdgeIndex);
						} else {
							leftChild = null;
							numberPrunedNodes++;
						}
					}
				}
			}
		}
	}

	public long getWholeTime(){
		return wholeTime;
	}
	
	public ArrayList<ArrayList<Byte>> copyConstraint(ArrayList<ArrayList<Byte>> constraint){
		ArrayList<ArrayList<Byte>> newConstraint = new ArrayList<ArrayList<Byte>>();
		for(ArrayList<Byte> a : constraint){
			newConstraint.add(new ArrayList<Byte>(a));
		}
		return newConstraint;
	}
	
	public int getBestTour(){
		return bestTour;
	}
}
