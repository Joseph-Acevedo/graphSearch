/**
 * This is the main file for an implementation of Dijkstra's path finding algorithm. 
 * Multiple types of map generators have been made and added.
 * The GitHub repository for this project can be found at: https://github.com/joseph-acevedo/graphSearch
 * 
 * 
 * @author	Joseph Acevedo
 * @since 	07 October, 2018
 */

package dijkstra;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;
import mapGenerator.MapGenerator;
import mapGenerator.NodeGrid;
import mapGenerator.RandomMap;
import mapGenerator.UserMap;
import mathUtilities.MergeSort;


public class Dijkstra {
	
	/* === CONSTANTS === */
	private static final int INF = Integer.MAX_VALUE;
	private static final int RANDOM = 0;
	private static final int GRID = 1;
	private static final int USER = 2;
	
	private Node[] nodes; // List of all Nodes on map
	
	private ArrayList<Node> Q; // List of Unvisited Nodes
	private Stack<Node> S; // Stack of Visited Nodes
	
	private Node u; // current Node
		
	public String source;
	public String endID;
	
	private MapGenerator map;

	/**
	 * Constructor for the PathFinder using Dijkstra path finding algorithm
	 * @param nodesWidth The number of Nodes wide to create the map
	 * @param nodesHeight The number of Nodes tall to create the map
	 * @param start The ID of the source Node
	 * @param end The ID of the end Node
	 * @param mode Which mapGenerator mode to use
	 */
	public Dijkstra(int nodesWidth, int nodesHeight, Point start, Point end, int mode) { 
		// Create the map based on which mode was given
		switch (mode) {
		case RANDOM:
			map = new RandomMap(nodesWidth * nodesHeight);
			break;
		case GRID:
			map = new NodeGrid(nodesWidth, nodesHeight, this);
			break;
		case USER: 
			// TODO: Work in progress
			map = new UserMap(this);
			break;
		default:
			throw new IllegalArgumentException("Illegal MapGeneration mode");
		}
		
		// When testing UserMap comment out the rest of this constructor to avoid errors having to do with an empty Node list
		
		nodes = map.getNodes();
		
		
		source = Integer.toString(start.x + (start.y * nodesWidth));
		endID = Integer.toString(end.x + (end.y * nodesWidth));
		
		initDijkstra();
		runDijkstra();
	}
	
	/**
	 * Resets the pathFinder so that it can re-run on a new map
	 */
	public void resetDijkstra() {
		initDijkstra();
		runDijkstra();
	}
	
	/**
	 * Initializes the pathFinder by setting all Node distances to INF or 0 and deleting the current path, if any
	 */
	private void initDijkstra() {

		Q = new ArrayList<Node>();
		S = new Stack<Node>();
		
		for (Node n: nodes) {
			if (n.getId().equals(source)) {
				u = n;
				u.setDistance(0);
				u.setFrom(null);
				Q.add(n);
			} else {
				if (n.isVisible()) {
					Q.add(n);
					n.setDistance(INF);
				}
			}
		}
	}
	
	/**
	 * Runs the pathFinder based on Dijkstra's path finding algorithm detailed here:
	 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Algorithm
	 */
	private void runDijkstra() {
		
		while ( !Q.isEmpty() ) {
			// This adds the priority part to the queue by sorting them based on their distances using a modified MergeSort
			Node[] ordered = MergeSort.mergeSort( Q.toArray( new Node[Q.size()] ) );
			u = ordered[0];
			S.add(u);
			Q.remove(u);
			for ( Node v: u.getConnections() ) {
				if (v.isVisible()) {
					// Check if the total distance to this Node is lower
					if (v.getDistance() > (u.getDistance() + u.distToNode(v))) {
						v.setDistance(u.getDistance() + u.distToNode(v));
						v.setFrom(u);
						// If we've gotten to the end then set the current Node as the end and traceback the path in paintComponent
						if (v.getId().equals(endID)) {
							map.setEnd(v);
							return;
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		int width  = 75;
		int height = 75;
		new Dijkstra(width, height, new Point(0, 0), new Point(width - 1, height - 1), GRID);
		
	}
	
	

}
