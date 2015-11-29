package travelSalesman;
import java.awt.*;
import java.util.*;

public class BranchAndBound {

	private final int numRows;
	private final int numCols;
	private TimeInterval t = new TimeInterval();
	private int bestTour = Integer.MAX_VALUE / 4;
	private Node bestNode;
	public static Cost costTable;
	public static ArrayList<Point> newEdge = new ArrayList<Point>();
	private int newNodeCount = 0;
	private int numberPrunedNodes = 0;
	private long wholeTime = 0;


	public BranchAndBound(Cost cost, int size) {
		numRows = numCols = size;
		this.costTable = cost;
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

		Node root = new Node(numRows, numCols);
		newNodeCount++;
		root.computeLowerBound();
		t.startTiming();
		branchAndBound(root, -1);
		t.endTiming();
		wholeTime=t.getElapsedTime();
		if (bestNode != null) {
			System.out.println("\n\nKoszt optymalnej drogi: " + bestTour
					+ "\nDroga: " + bestNode.tour()
					+ "\nWygenerowano wêzlów: " + newNodeCount
					+ "\nliczba opuszczonych wêz³ów: " + numberPrunedNodes);
		} else {
			System.out.println("Tour obtained heuristically is the best tour.");
		}
		System.out.println("Czas algorytmu dla metody PiO: "
				+ wholeTime + " ms.");
		System.out.println();
	}

	private void branchAndBound(Node node, int edgeIndex) {
		if (node != null && edgeIndex < newEdge.size()) {
			Node leftChild, rightChild;
			int leftEdgeIndex = 0, rightEdgeIndex = 0;
			if (node.isTour()) {
				node.setTour();
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
					// Create left child node
					leftChild = new Node(numRows, numCols);
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
					leftChild.setConstraint(copy(node.constraint()));
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
					leftEdgeIndex = leftChild.assignPoint(p, edgeIndex);
					leftChild.addDisallowedEdges();
					leftChild.addRequiredEdges();
					leftChild.addDisallowedEdges();
					leftChild.addRequiredEdges();
					leftChild.computeLowerBound();
					if (leftChild.lowerBound() >= 2 * bestTour) {
						leftChild = null;
						numberPrunedNodes++;
					}
					// Create right child node
					rightChild = new Node(numRows, numCols);
					newNodeCount++;
					if (newNodeCount % 1000 == 0) {
					} else if (newNodeCount % 25 == 0) {
					}
					rightChild.setConstraint(copy(node.constraint()));
					if (leftEdgeIndex >= newEdge.size()) {
						return;
					}
					p = (Point) newEdge.get(leftEdgeIndex + 1);
					rightEdgeIndex = rightChild.assignPoint(p,
							leftEdgeIndex + 1);
					rightChild.addDisallowedEdges();
					rightChild.addRequiredEdges();
					rightChild.addDisallowedEdges();
					rightChild.addRequiredEdges();
					rightChild.computeLowerBound();
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

	private byte[][] copy(byte[][] constraint) {
		byte[][] toReturn = new byte[numRows + 1][numCols + 1];
		for (int row = 1; row <= numRows; row++) {
			for (int col = 1; col <= numCols; col++) {
				toReturn[row][col] = constraint[row][col];
			}
		}
		return toReturn;
	}
	public long getWholeTime(){
		return wholeTime;
	}
	
	public int getBestTour(){
		return bestTour;
	}
}