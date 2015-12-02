package MyBranchAndBound;

import java.awt.Point;
import java.util.ArrayList;
import java.util.BitSet;


public class MyNode {
	private int lowerBound;
	private int numRows, numCols;
	private ArrayList<ArrayList<Byte>> constraint;
	private ArrayList<Short> nodeCosts;
	private int tourCost;
	private ArrayList<Byte> trip;
	private String nodeAsString;
	static BitSet b;

	// konstruktor
	public MyNode(int numRows, int numCols) {
		nodeCosts = new ArrayList<Short>();
		constraint = new ArrayList<ArrayList<Byte>>();
		trip = new ArrayList<Byte>();
		this.numRows = numRows;
		this.numCols = numCols;
		for (int i = 0; i < numRows+1; i++) {
			nodeCosts.add((short) 0);
			trip.add((byte) 0);
			ArrayList<Byte> temp = new ArrayList<Byte>();
			for (int j = 0; j < numCols+1; j++) {
				temp.add((byte) 0);
			}
			constraint.add(temp);

		}

	}

	public void assignConstraint(byte value, int row, int col) {
		constraint.get(row).set(col, (byte) (value));
		constraint.get(col).set(row, (byte) (value));
	}

	public int assignPoint(Point p, int edgeIndex, ArrayList<Point> newEdge) {
		Point pt = p;
		while (edgeIndex < newEdge.size()
				&& constraint.get((int) Math.abs(pt.getX())).get(
						(int) Math.abs(pt.getY())) != 0) {
			edgeIndex++;
			if (edgeIndex < newEdge.size()) {
				pt = (Point) newEdge.get(edgeIndex);
			}
			System.out.println("ei "+edgeIndex+" size " + newEdge.size());
		}
		if (edgeIndex < newEdge.size()) {
			System.out.println("in");
			if (pt.getX() < 0) {
				assignConstraint((byte) -1, (int) Math.abs(pt.getX()),
						(int) Math.abs(pt.getY()));
			} else {
				assignConstraint((byte) 1, (int) pt.getX(), (int) pt.getY());
			}
		}
		return edgeIndex;
	}

	public void setConstraint(ArrayList<ArrayList<Byte>> constraint) {
		this.constraint = constraint;
	}

	public void addDisallowedEdges() {
		for (int row = 1; row <= numRows; row++) {
			int count = 0;

			for (int col = 1; col <= numCols; col++) {
				if (row != col && constraint.get(row).get(col) == 1) {
					count++;
				}
			}
			if (count >= 2) {
				for (int col = 1; col <= numCols; col++) {
					if (row != col && constraint.get(row).get(col) == 0) {
						constraint.get(row).set(col, (byte) -1);
						constraint.get(col).set(row , (byte) -1);
					}
				}
			}
		}

		for (int row = 1; row <= numRows; row++) {
			for (int col = 1; col <= numCols; col++) {
				if (row != col && isCycle(row, col) && numCities(b) < numRows) {
					if (constraint.get(row).get(col) == 0) {
						constraint.get(row).set(col, (byte) -1);
						constraint.get(col).set(row, (byte) -1);
					}
				}
			}
		}
	}

	public void addRequiredEdges() {
		for (int row = 1; row <= numRows; row++) {

			int count = 0;
			for (int col = 1; col <= numCols; col++) {
				if (row != col && constraint.get(row).get(col) == -1) {
					count++;
				}
			}
			if (count >= numRows - 3) {
				for (int col = 1; col <= numCols; col++) {
					if (row != col && constraint.get(row).get(col) == 0) {
						constraint.get(row).set(col, (byte) -1);
						constraint.get(col).set(row, (byte) -1);
					}
				}
			}
		}
	}

	public void computeLowerBound(ArrayList<ArrayList<Short>> costList) {
		int lowB = 0;
		for (int row = 1; row <= numRows; row++) {

			for (int col = 1; col <= numCols; col++) {
				nodeCosts.set(col - 1, (costList.get(row - 1).get(col - 1)));
			}
			nodeCosts.set(row-1, Short.MAX_VALUE);
			for (int col = 1; col <= numCols; col++) {
				if (constraint.get(row).get(col) == -1) {
					nodeCosts.set(col - 1, Short.MAX_VALUE);
				}
			}
			int[] required = new int[numCols - 1];
			int numRequired = 0;

			for (int col = 1; col <= numCols; col++) {
				if (constraint.get(row).get(col) == 1) {
					numRequired++;
					required[numRequired] = nodeCosts.get(col-1);
					nodeCosts.set(col-1, Short.MAX_VALUE);
				}
			}
			int smallest = 0, nextSmallest = 0;
			if (numRequired == 0) {
				smallest = smallest();
				nextSmallest = nextSmallest();
			} else if (numRequired == 1) {
				smallest = required[1];
				nextSmallest = smallest();
			} else if (numRequired == 2) {
				smallest = required[1];
				nextSmallest = required[2];
			}
			if (smallest == Short.MAX_VALUE) {
				smallest = 0;
			}
			if (nextSmallest == Short.MAX_VALUE) {
				nextSmallest = 0;
			}
			lowB += smallest + nextSmallest;
		}
		lowerBound = lowB;
	}

	public void setTour(ArrayList<ArrayList<Short>> costList) {
		byte path = 0;
		for (int col = 2; col <= numCols; col++) {
			if (constraint.get(1).get(col) == 1) {
				path = (byte) col;
				break;
			}
		}
		tourCost = costList.get(0).get(path - 1);
		trip.set(0, path);
		int row = 1;
		int col = path;
		int from = row;
		byte pos = path;
		nodeAsString = "" + row + "->" + col;
		while (pos != row) {
			for (byte column = 1; column <= numCols; column++) {
				if (column != from
						&& constraint.get(pos).get(column) == 1) {
					from = pos;
					pos = column;
					nodeAsString += "->" + pos;
					tourCost += costList.get(from - 1).get(pos - 1);
					trip.set(from - 1, pos);
					break;
				}
			}
		}
	}

	public int getTourCost() {
		return tourCost;
	}

	public ArrayList<Byte> trip() {
		return trip;
	}

	public byte constraint(int row, int col) {
		return constraint.get(row).get(col);
	}

	public ArrayList<ArrayList<Byte>> constraint() {
		return constraint;
	}

	public int lowerBound() {
		return lowerBound;
	}

	public boolean isTour() {
		int path = 0;
		for (int col = 2; col <= numCols; col++) {
			if (constraint.get(1).get(col) == 1) {
				path = col;
				break;
			}
		}
		if (path > 0) {
			boolean cycle = isCycle(1, path);
			return cycle && numCities(b) == numRows;
		} else {
			return false;
		}
	}

	public boolean isCycle(int row, int col) {
		b = new BitSet(numRows + 1);
		for (int i = 0; i < numRows + 1; i++) {
			b.clear(i);
		}
		b.set(row);
		b.set(col);
		int from = row;
		int pos = col;
		int edges = 1;
		boolean quit = false;
		while (pos != row && edges <= numCols && !quit) {
			quit = true;
			for (int column = 1; column <= numCols; column++) {
				if (column != from
						&& constraint.get(pos).get(column) == 1) {
					edges++;
					from = pos;
					pos = column;
					b.set(pos);
					quit = false;
					break;
				}
			}
		}
		return pos == row || edges >= numCols;
	}

	public String tour() {
		return nodeAsString;
	}

	private int smallest() {
		int s = nodeCosts.get(0);
		int index = 1;
		for (int i = 2; i <= numCols; i++) {
			if (nodeCosts.get(i - 1) < s) {
				s = nodeCosts.get(i - 1);
				index = i;
			}
		}
		short temp = nodeCosts.get(0);
		nodeCosts.set(0, nodeCosts.get(index - 1));
		nodeCosts.set(index - 1, temp);
		return nodeCosts.get(0);
	}

	private int nextSmallest() {
		int ns = nodeCosts.get(1);
		int index = 2;
		for (int i = 2; i <= numCols; i++) {
			if (nodeCosts.get(i - 1) < ns) {
				ns = nodeCosts.get(i - 1);
				index = i;
			}
		}
		short temp = nodeCosts.get(1);
		nodeCosts.set(1, nodeCosts.get(index - 1));
		nodeCosts.set(index - 1, temp);
		return nodeCosts.get(1);
	}

	private int numCities(BitSet b) {
		int num = 0;
		for (int i = 1; i <= numRows; i++) {
			if (b.get(i)) {
				num++;
			}
		}
		return num;
	}
}
