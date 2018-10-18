package mapGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import mathUtilities.MathUtilities;
import dijkstra.Node;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapGenerator extends JPanel {
	

	/* GRAPHICS CONSTANTS */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH  = 800;
	private static final int HEIGHT = 800;
	private static final int SAFE_SIZE = 50;
	private static final int NODE_RADIUS = 5;
	private static final int CONNECTION_WIDTH = 5;
	/* GENERATION CONSTANTS */
	private static final float MIN_WEIGHT_FOR_CONNECTION = 6.0f;
	
	private JFrame frame;
	
	private ArrayList<Node> nodes;	// A linearized version of the node map, a 1x(n^2) matrix
	
	private long currId = 0L;
	private int n = 0;			// number of nodes
	
	// These matrices depend on whether you are using the random map generation or node grid
	// Implementation
	/*								  === Random Map ===					=== Node Grid ===*/
	private double[][] P; 		// - an (nx2) matrix of points			- an (nxm) matrix of points
	private double[][] W;		// - an (2xn) matrix of weights			- not used
	private double[][] res;		// - an (nxn) matrix as a result of 	- not used
								//   P*W using matrix multiplication
	private double[][] conns;	// - an (nxn) matrix that compiles 		- not used
								//   the weights in res together

	// This implementation uses objects and pointers (connections) to generate the representation of the graph
	public MapGenerator(int numberNodes) {
		n = numberNodes;
		
		nodes = new ArrayList<Node>();
		
		P = new double[n][2];
		W = new double[2][n];
		res = new double[n][n];
		conns = new double[n][n];
		
		createNodeMap();
		
		frame = new JFrame();
		
		setPreferredSize( new Dimension( WIDTH, HEIGHT));
		
		frame.setLayout( new BorderLayout());
		frame.add( this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Create a map using an adjacency matrix to create a grid for mimicking real motion.
	 * The generated adjacency matrix is (nxn) where n is the width x height (number of nodes)
	 * @param nodesWidth The number of nodes wide to create the grid
	 * @param nodesHeight The number of nodes tall to create the grid
	 */
	public MapGenerator(int nodesWidth, int nodesHeight) {
		boolean[][] adjacencyMatrix = new boolean[nodesWidth*nodesHeight][nodesWidth*nodesHeight];
		nodes = new ArrayList<Node>();
		
		float xDist = WIDTH / nodesWidth;
		float yDist = HEIGHT / nodesHeight;
		
		for (int y = 0; y < nodesWidth; y++) {
			for (int x = 0; x < nodesHeight; x++) {
				nodes.add( new Node( currId++, new Point( (int) (x*xDist), (int) (y*yDist) ) ) );
			}
		}
		
		createNodeGrid(nodesWidth, nodesHeight);
		
		frame = new JFrame();
		
		setPreferredSize( new Dimension( WIDTH, HEIGHT));
		
		frame.setLayout( new BorderLayout());
		frame.add( this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	private void createNodeGrid(int nodesWidth, int nodesHeight) {
		Node currentNode;
		
		for (int y = 0; y < nodesHeight - 1; y++) {
			for (int x = 0; x < nodesWidth - 1; x++) {
				currentNode = nodes.get( (x + (y * nodesWidth)) );
				
				if ( (x != 0) && (x != nodesWidth) ) {
					if ( (y != 0) && (y != nodesHeight) ) {						
						/* _________
						 * ( a b c )
						 * ( d E f )
						 * ( g h i )
						 * _________
						 */
						
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y - 1) * nodesWidth)) )); // a
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 1) * nodesWidth)) )); // g
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 0) * nodesWidth)) )); // d
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 1) * nodesWidth)) )); // c
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 1) * nodesWidth)) )); // i
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 0) * nodesWidth)) )); // f
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) )); // b
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) )); // h
						// Normal
					} else {
						// Normal Left and Right but edge of top and bottom
						if (y == 0) {
							/* _________
							 * ( a B c )
							 * ( d e f )
							 */
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 0) * nodesWidth))  )); // a
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 0) * nodesWidth))  )); // c
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 1) * nodesWidth))  )); // d
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 1) * nodesWidth))  )); // e
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y + 1) * nodesWidth))  )); // f

							
						} 
						else if (y == nodesHeight) {
							/*
							 * ( a b c )
							 * ( d E f )
							 * __________
							 */
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 0) * nodesWidth))  )); // d
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 0) * nodesWidth))  )); // f
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y - 1) * nodesWidth))  )); // a
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y - 1) * nodesWidth))  )); // b
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y - 1) * nodesWidth))  )); // c
						}
					}
					
				} else {
					if (x == 0) {
						if ( (y != 0) && (y != nodesWidth) ) {
							/*
							 * |( a b )
							 * |( C d )
							 * |( e f )
							 */
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y - 1) * nodesWidth))  )); // a
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y + 1) * nodesWidth))  )); // e
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y - 1) * nodesWidth))  )); // b
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 0) * nodesWidth))  )); // d
							currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 1) * nodesWidth))  )); // f
							
						} else {
							if (y == 0) {
								/* ________
								 * |( C d )
								 * |( e f )
								 */
								currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 0) * nodesWidth))  )); // d
								currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 1) * nodesWidth))  )); // f
								currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y + 1) * nodesWidth))  )); // e
								
							} else {
								/*
								 * |( a b )
								 * |( C d )
								 * ________
								 */
								currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y - 1) * nodesWidth))  )); // a
								currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y - 1) * nodesWidth))  )); // b
								currentNode.addConnection(nodes.get(  ( (x + 1) + ( (y + 0) * nodesWidth))  )); // d
							}
							
						}
					} 
					else if (x == nodesWidth) {
						if ( (y != 0) && (y != nodesHeight) ) {
							if (y == 0) {
								/*_________
								 * ( c D )|
								 * ( e f )|
								 */
								currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 0) * nodesWidth))  )); // c
								currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 1) * nodesWidth))  )); // e
								currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y + 1) * nodesWidth))  )); // f
								
							} else {
								/*
								 * ( a b )|
								 * ( c D )|
								 * ________
								 */
								currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 0) * nodesWidth))  )); // c
								currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y - 1) * nodesWidth))  )); // a
								currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y - 1) * nodesWidth))  )); // b
							}
						}
						else {
							/*
							 * ( a b )|
							 * ( c D )|
							 * ( e f )|
							 */
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y - 1) * nodesWidth))  )); // a
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 0) * nodesWidth))  )); // c
							currentNode.addConnection(nodes.get(  ( (x - 1) + ( (y + 1) * nodesWidth))  )); // e
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y - 1) * nodesWidth))  )); // b
							currentNode.addConnection(nodes.get(  ( (x + 0) + ( (y + 1) * nodesWidth))  )); // f
						}
					}
				}
			}
		}
	}
	
	// TODO: Create a map using an adjacency matrix to create a node 'mesh'
	
	private boolean createNodeMap() {
		generatePoints();
		
		generateWeights();
		
		double[][] temp = MathUtilities.matrixMultiply2D(P, W);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = temp[i][j];
			}
		}
		
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				if (x == y) {
					conns[x][y] = 0.0;
				} else {
					conns[x][y] = res[x][y] + res[y][x];
					if (conns[x][y] >= MIN_WEIGHT_FOR_CONNECTION) {
						nodes.get(x).addConnection(nodes.get(y));
					}
				}
				//System.out.printf("%.1f ", conns[x][y]);
			}
			//System.out.println();
		}
		
		
		return true;
	}
	
	
	private void generatePoints() {
		// Randomly generate the node locations
		for (int i = 0; i < n; i++) {
			int x = (int) ( ( Math.random() * ( WIDTH -  ( 2 * SAFE_SIZE) ) ) + SAFE_SIZE);
			int y = (int) ( ( Math.random() * ( HEIGHT - ( 2 * SAFE_SIZE) ) ) + SAFE_SIZE);
			P[i][0] = x;
			P[i][1] = y;
			nodes.add( new Node( currId++, new Point( x, y)));
		}
	}
	
	private void generateWeights() {
		// Randomly generate the connection weights
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < n; j++) {
				W[i][j] = ( 0.01 * Math.random() );
			}
		}
	}
	
	
	public Node[] getNodes() {
		return nodes.toArray(new Node[nodes.size()]);
	}
	
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		
		for (Node n: nodes) {
			if (n.getConnections() != null) {
				for (Node c: n.getConnections()) {
					if (n.getInSequence() && c.getInSequence()) {
						drawSequenceConnection(g, n, c);
					} else {
						drawConnection(g, n, c);
					}
				}
			}
		}
		
		for (Node n: nodes) {
			drawNode(g, n);
		}
		
		this.repaint();
	}
	
	
	private void drawNode(Graphics g, Node n) {
//		g.setColor(Color.WHITE);
//		g.fillOval(n.getX() - NODE_RADIUS, n.getY() - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
		g.setColor(Color.BLACK);
		g.drawOval(n.getX() - NODE_RADIUS, n.getY() - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
//		g.drawString(n.getId(), n.getX(), n.getY());
	}

	private void drawConnection(Graphics g, Node a, Node b) {
		g.setColor(Color.GRAY.darker());
		g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	private void drawSequenceConnection(Graphics g, Node a, Node b) {
		g.setColor(Color.BLUE.brighter());
		g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
}
