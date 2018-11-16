package dijkstra;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;
import mapGenerator.MapGenerator;
import mathUtilities.MergeSort;


public class Dijkstra {
	
	private static int INF = Integer.MAX_VALUE;
	
	private Node[] nodes;
	
	public ArrayList<Node> currSeq;
	private ArrayList<Node> Q; // TODO: Unvisited
	private Stack<Node> S; // TODO: Visited Nodes
	
	private Node u; // TODO: Also changed, current I think?
		
	public String source;
	public String endID;
	
	private MapGenerator map;
	
	
	public Dijkstra(int nodesWidth, int nodesHeight, Point start, Point end) { 
		
		map = new MapGenerator( nodesWidth,  nodesHeight, this );
		//map = new MapGenerator(nodesWidth*nodesHeight);
		nodes = map.getNodes();
		
		
		source = Integer.toString(start.x + (start.y * nodesWidth));
		endID = Integer.toString(end.x + (end.y * nodesWidth));
		
		initDijkstra();
		runDijkstra();
	}
	
	public void resetDijkstra() {
		initDijkstra();
		runDijkstra();
	}
	
	private void initDijkstra() {
		currSeq = new ArrayList<Node>();

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
	
	private void runDijkstra() {
		// TODO: This will only work if I remove the nodes with no connections, maybe,
		// It might just run through that node quickly and throw it out
		System.out.println("Starting");
		
		while ( !Q.isEmpty() ) {
			Node[] ordered = MergeSort.mergeSort( Q.toArray( new Node[Q.size()] ) );
			u = ordered[0]; // TODO: current node
			S.add(u);
			Q.remove(u);
			for ( Node v: u.getConnections() ) {
				if (v.isVisible()) {
					if (v.getDistance() > (u.getDistance() + u.distToNode(v))) {
						v.setDistance(u.getDistance() + u.distToNode(v));
						v.setFrom(u);
						if (v.getId().equals(endID)) {
							tracebackPath(v);
							return;
						}
					}
				}
			}
		}
	}
	
	
	private void tracebackPath(Node end) {
		Node curr = end;
		System.out.println("Tracing");
		while (curr != null) {
			System.out.printf("%s->", curr.getId());
			curr = curr.getFrom();
		}
		map.setEnd(end);
	}
	
	
	public static void main(String[] args) {
		int width = 20;
		int height  = 20;
		new Dijkstra(width, height, new Point(0, 0), new Point(width - 1, height - 1));
	}
	
	

}
