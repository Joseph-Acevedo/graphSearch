package dijkstra;

import java.util.ArrayList;
import java.util.Stack;
import mapGenerator.MapGenerator;


public class Dijkstra {
	private Node[] nodes;
	
	private ArrayList<Node> unvisited;
	private Stack<Node> visited;
	
	private Node current;
	
	private String startID;
	private String endID;
	
	private MapGenerator map;
	
	
	public Dijkstra(int numNodes, String start, String end) { 
		map = new MapGenerator(numNodes);
		nodes = map.getNodes();
		
		unvisited = new ArrayList<Node>();
		visited = new Stack<Node>();
		
		for (Node n: nodes) {
			if (n.getId().equals(start)) {
				current = n;
				current.setDistance(0);
				current.setFrom(null);
			} else {
				unvisited.add(n);
				n.setDistance(-1);
			}
		}
		
		runDijkstra();
	}
	
	private void runDijkstra() {
		
	}
	
	private void considerNode(Node n) {
		Node[] connections = n.getConnections();
		double[] distances = n.getDistances();
		
		// Distance is total current distance + tentative distance to node
		for (int i = 0; i < distances.length; i++) {
			distances[i] += n.getDistance();
			if (distances[i] < connections[i].getDistance() || connections[i].getDistance() == -1) {
				
			}
		}
		
		
	}
	
	
	public static void main(String[] args) {
		new Dijkstra(10, "1", "10");
	}
	
	

}
