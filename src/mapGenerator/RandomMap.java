/**
 * @author	Joseph Acevedo
 * @since 	07 October, 2018
 */

package mapGenerator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import dijkstra.Dijkstra;
import dijkstra.Node;
import mathUtilities.MathUtilities;

public class RandomMap extends MapGenerator {
	
	private static final int WIDTH  = MapGenerator.WIDTH;
	private static final int HEIGHT = MapGenerator.HEIGHT;
	
	private JFrame frame;
	private Dijkstra pathfinder;
	
	private Node endNode = null;
	
	private long currId = 0L;
	private int n = 0;			// number of nodes
	

	/*								  === Random Map ===				*/
	private double[][] P; 		// - an (nx2) matrix of points			
	private double[][] W;		// - an (2xn) matrix of weights			
	private double[][] res;		// - an (nxn) matrix as a result of P*W using matrix multiplication
	private double[][] conns;	// - an (nxn) matrix that compiles the weights in res together
	
	public RandomMap(int numNodes) {
		n = numNodes;
		
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
	 * Generates a map of n randomly placed Nodes which are randomly connected
	 */
	private void createNodeMap() {
		generatePoints();
		
		generateWeights();
		
		double[][] temp = MathUtilities.matrixMultiply(P, W);
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
			}
		}
	}

	/**
	 * Generates random locations for each Node within a certain bounded safe area
	 */
	protected void generatePoints() {
		// Randomly generate the node locations
		for (int i = 0; i < n; i++) {
			int x = (int) ( ( Math.random() * ( WIDTH -  ( 2 * SAFE_SIZE) ) ) + SAFE_SIZE);
			int y = (int) ( ( Math.random() * ( HEIGHT - ( 2 * SAFE_SIZE) ) ) + SAFE_SIZE);
			P[i][0] = x;
			P[i][1] = y;
			nodes.add( new Node( currId++, new Point( x, y)));
		}
	}
	
	/**
	 * Randomly generates weights for the Nodes which is used to determine if a Node is connected to another
	 */
	protected void generateWeights() {
		// Randomly generate the connection weights
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < n; j++) {
				W[i][j] = ( 0.01 * Math.random() );
			}
		}
	}

	
	/* ===== PAINT METHODS ===== */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		
		if (mousePressed) {
			mouseLoc = getMousePosition();
			for (Node n: nodes) {
				if (mouseLoc != null) {
					if (MathUtilities.euclideanDistance(n.getLoc(), mouseLoc) <= MAX_RADIUS_FOR_CLICK) {
						n.setVisibile(setDragVisibility);
					}
				}
			}
		}
		
		for (Node n: nodes) {
			if (n.isVisible() && n.getConnections() != null) {
				for (Node c: n.getConnections()) {
					if (c.isVisible()) {
						drawConnection(g, n, c, Color.BLACK, 1);
					}
				}
			}
		}
		if (endNode != null) {
			drawSequence(g, endNode);
		}
		
		for (Node n: nodes) {
			if (n.isVisible()) {
				drawNode(g, n);
			}
		}
		
		
		this.repaint();
	}
	
	protected void drawNode(Graphics g, Node n) {

		g.setColor(Color.BLACK);
		g.drawOval(n.getX() - NODE_RADIUS, n.getY() - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
		//g.drawString(n.getId(), n.getX(), n.getY());
	}

	protected void drawConnection(Graphics g, Node a, Node b, Color col, int thickness) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(col);
		g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	protected void drawSequence(Graphics g, Node end) {
		Node prev = end.getFrom();
		Node curr = end;
		
		while (curr != null) {
			drawConnection(g, prev, curr, Color.BLUE, 3);
			prev = curr;
			curr = curr.getFrom();
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
