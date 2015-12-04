package MyBranchAndBound;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import travelSalesman.TimeInterval;
import BranchAndBound.Cost;
import BranchAndBound.Node;

public class MyBranchAndBound {
	public int numRows;
	public int numCols;
	private TimeInterval t = new TimeInterval();
	private int bestTour = Integer.MAX_VALUE / 4;
	private MyNode bestNode;
	public ArrayList<ArrayList<Short>> costList;
	public int leftEdgeIndex = 0, rightEdgeIndex = 0;
	public ArrayList<Point> newEdge = new ArrayList<Point>();
	private int newNodeCount = 0;
	private int numberPrunedNodes = 0;
	private long wholeTime = 0;
	MyNode leftChild = null, rightChild = null;

	public MyBranchAndBound(Cost cost, int size) {
		numRows = numCols = size;
		costList = new ArrayList<ArrayList<Short>>();
		for (int i = 1; i <= size; i++) {
			ArrayList<Short> temp = new ArrayList<Short>();
			for (int j = 1; j <= size; j++) {
				temp.add(cost.getCost(i, j));
			}
			costList.add(temp);
		}
		costList.parallelStream();
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
		try {
			branchAndBound(root, -1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t.endTiming();
		wholeTime = t.getElapsedTime();
		if (bestNode != null) {
			System.out.println("\n\nKoszt optymalnej drogi: " + bestTour
					+ "\nDroga: " + bestNode.tour() + "\nWygenerowano wêzlów: "
					+ newNodeCount + "\nliczba opuszczonych wêz³ów: "
					+ numberPrunedNodes);
		} else {
			System.out.println("Tour obtained heuristically is the best tour.");
		}
		System.out.println("Czas algorytmu dla metody PiOna listach: "
				+ wholeTime + " ms.");
		System.out.println();
	}

	private void branchAndBound(MyNode node, int edgeIndex) throws InterruptedException {
		if (node != null && edgeIndex < newEdge.size()) {
			MyNode leftChild, rightChild;
			int leftEdgeIndex = 0, rightEdgeIndex = 0;
			if (node.isTour()) {
				node.setTour(costList);
				if (node.getTourCost() < bestTour) {
					bestTour = node.getTourCost();
					bestNode = node;
				}
			} else {
				if (node.lowerBound() < 2 * bestTour) {
					// Create left child node
					leftChild = new MyNode(numRows, numCols);
					rightChild = new MyNode(numRows, numCols);
					newNodeCount++;
					newNodeCount++;
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
					leftEdgeIndex = leftChild
							.assignPoint(p, edgeIndex, newEdge);
					rightChild.setConstraint(copyConstraint(node.constraint()));
					if (leftEdgeIndex >= newEdge.size()) {
						return;
					}
					p = (Point) newEdge.get(leftEdgeIndex + 1);
					rightEdgeIndex = rightChild.assignPoint(p,
							leftEdgeIndex + 1, newEdge);
					
					 Thread lChild = new Thread(new PrepareChild(leftChild,costList));
					 Thread rChild = new Thread(new PrepareChild(rightChild,costList));					
					 ExecutorService es = Executors.newCachedThreadPool();
					 es.execute(lChild);
					 es.execute(rChild);
					 es.shutdown();
					 es.awaitTermination(1, TimeUnit.MINUTES);				
					
					if (leftChild.lowerBound() >= 2 * bestTour) {
						leftChild = null;
						numberPrunedNodes++;
					}			
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
							&& leftChild.lowerBound() <= rightChild.lowerBound()) {
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

	public long getWholeTime() {
		return wholeTime;
	}



	private byte[][] copyConstraint(byte[][] constraint) {
		byte[][] toReturn = new byte[numRows + 1][numCols + 1];
		for (int row = 1; row <= numRows; row++) {
			for (int col = 1; col <= numCols; col++) {
				toReturn[row][col] = constraint[row][col];
			}
		}
		return toReturn;
	}

	public int getBestTour() {
		return bestTour;
	}
}

class PrepareChild implements Runnable {

	private MyNode child;
	ArrayList<ArrayList<Short>> costList;

	PrepareChild(MyNode child,ArrayList<ArrayList<Short>> costList) {
		this.child = child;
		this.costList = costList;
	}

	@Override
	public void run() {
		child.addDisallowedEdges();
		child.addRequiredEdges();
		child.addDisallowedEdges();
		child.addRequiredEdges();
		child.computeLowerBound(costList);

	}

}
