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
	
	private ArrayList<Node> nodes;
	
	private long currId = 0L;
	private int n = 0;			// number of nodes
	
	private double[][] P; 		// an (nx2) matrix of points
	private double[][] W;		// an (2xn) matrix of weights
	private double[][] res;		// an (nxn) matrix as a result of P*W using matrix multiplication
	private double[][] conns;	// an (nxn) matrix that compiles the weights in res together

	
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
	
	
	private boolean createNodeMap() {
		generatePoints();
		/*
		P[0][0] = 150;
		P[0][1] = 150;
		nodes.add( new Node(currId++, new Point(150, 150)));
		P[1][0] = 650;
		P[1][1] = 650;
		nodes.add( new Node(currId++, new Point(650, 650)));
		P[2][0] = 150;
		P[2][1] = 650;
		nodes.add( new Node(currId++, new Point(150, 650)));
		P[3][0] = 650;
		P[3][1] = 150;
		nodes.add( new Node(currId++, new Point(650, 150)));
		*/
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
					drawConnection(g, n, c);
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
	
}
