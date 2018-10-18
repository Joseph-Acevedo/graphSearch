package dijkstra;

import java.util.ArrayList;
import java.util.Stack;
import mapGenerator.MapGenerator;


public class Dijkstra {
	
	private static int INF = -1;
	
	private Node[] nodes;
	
	//private ArrayList<Node> sequence;
	private ArrayList<Node> unvisited;
	private Stack<Node> visited;
	
	private Node current;
	
	private String startID;
	private String endID;
	
	private MapGenerator map;
	
	
	public Dijkstra(int numNodes, String start, String end) { 
		//map = new MapGenerator(numNodes);
		map = new MapGenerator( (int) (Math.sqrt(numNodes)),  (int) (Math.sqrt(numNodes)) );
		nodes = map.getNodes();
		
		startID = start;
		endID = end;
		
		//sequence = new ArrayList<Node>();
		unvisited = new ArrayList<Node>();
		visited = new Stack<Node>();
		
		for (Node n: nodes) {
			if (n.getId().equals(start)) {
				current = n;
				current.setDistance(0);
				current.setFrom(null);
				unvisited.add(n);
			} else {
				unvisited.add(n);
				n.setDistance(INF);
			}
		}
		
		runDijkstra();
	}
	
	private void runDijkstra() {
		// While we still have nodes to check
		// TODO: This will only )work if I remove the nodes with no connections, maybe,
		// It might just run through that node quickly and throw it out
		while ( !unvisited.isEmpty() ) {
			double minDist = unvisited.get(0).getDistance();
			Node minimum = unvisited.get(0);
			for (Node n: unvisited) {
				if ( (n.getDistance() < minDist) && (minDist != INF) ) {
					minDist = n.getDistance();
					minimum = n;
				}
			}
			
			unvisited.remove(minimum);
			current = minimum;
			considerNode(current);
			visited.add(current);
			current.setInSequence(true);
		}
	}
	
	private void considerNode(Node n) {
		Node[] connections = n.getConnections();
		double[] tenativeDistances = n.getDistances();
		
		// Distance is total current distance + tentative distance to node
		// TODO: Make sure visited nodes are never checked again (Done, hopefully)
		// This loops through all connections and checks if their length is lower
		for (int i = 0; i < tenativeDistances.length; i++) {
			tenativeDistances[i] += n.getDistance();
			if ( !visited.contains(connections[i]) && (tenativeDistances[i] < connections[i].getDistance() || connections[i].getDistance() == INF) ) {
				connections[i].setDistance(tenativeDistances[i]);
				connections[i].setFrom(n);
			}
		}
		
		
	}
	
	
	public static void main(String[] args) {
		new Dijkstra(25, "1", "10");
		//new MapGenerator(50, 50);
	}
	
	

}
