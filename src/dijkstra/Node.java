package dijkstra;

import java.awt.Point;
import java.util.ArrayList;

import mathUtilities.MathUtilities;

public class Node {
	
	private long id;
	private Point loc;
	private ArrayList<Node> connections;
	private double distance;
	private Node nodeFrom;
	private boolean isSequence = false;
	
	public Node(long id, Point location) {
		this.id = id;
		this.loc = new Point(location);
		this.distance = -1;
		connections = new ArrayList<Node>();
	}
	
	
	public String getId() {
		return Long.toString(id);
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double d) {
		distance = d;
	}
	
	public Point getLoc() {
		return loc;
	}
	
	public int getX() {
		return loc.x;
	}

	public int getY() {
		return loc.y;
	}
	
	public Node getFrom() {
		return nodeFrom; 
	}
	
	public void setFrom(Node n) {
		nodeFrom = n;
	}
	
	public boolean getInSequence() {
		return isSequence;
	}
	
	public void setInSequence(boolean is) {
		isSequence = is;
	}
	
	public Node getConnection(int index) {
		try {
			return connections.get(index);
		} 
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public double[] getDistances() {
		double[] distances = new double[connections.size()];
		for (int i = 0; i < connections.size(); i++) {
			distances[i] = MathUtilities.euclideanDistance(this.loc, connections.get(i).getLoc());
		}
		return distances;
	}
	
	public Node[] getConnections() {
		if(connections.size() != 0) {
			return connections.toArray(new Node[connections.size()]);
		} else {
			return null;
		}
	}
	
	public boolean addConnection(Node n) {
		connections.add(n);
		return true;
	}
	
	public double distToNode(Node n) {
		return MathUtilities.euclideanDistance(this.loc, n.getLoc());
	}
	
}
