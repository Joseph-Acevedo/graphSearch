package mapGenerator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import mathUtilities.MathUtilities;
import dijkstra.Dijkstra;
import dijkstra.Node;
import mathUtilities.MathUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapGenerator extends JPanel implements MouseListener {
	

	/* GRAPHICS CONSTANTS */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH  = 1000;
	private static final int HEIGHT = 1000;
	private static final int SAFE_SIZE = 50;
	private static final int NODE_RADIUS = 1;
	private static final int CONNECTION_WIDTH = 5;
	/* GENERATION CONSTANTS */
	private static final float MIN_WEIGHT_FOR_CONNECTION = 7.0f;
	private static final float MIN_WEIGHT_FOR_VISIBILITY = 0.2f;
	private static final float MAX_RADIUS_FOR_CLICK = 10.0f;
	/* GLOBAL FLAGS */
	private boolean mousePressed = false;
	private boolean setDragVisibility = false;
	private Point mouseLoc;
	
	private JFrame frame;
	private Dijkstra pathfinder;
	
	private ArrayList<Node> nodes;	// A linearized version of the node map, a 1x(n^2) matrix
	private Node endNode = null;
	
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
	public MapGenerator(int nodesWidth, int nodesHeight, Dijkstra pfinder) {
		// TODO: Creating connections doesn't work correctly
		pathfinder = pfinder;
		addMouseListener(this);
		
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
		setBackground(Color.WHITE);
		
		frame.setLayout( new BorderLayout());
		frame.add( this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private void createNodeGrid(int nodesWidth, int nodesHeight) {
		Node currentNode;
		
		for (int y = 0; y < nodesHeight; y++) {
			for (int x = 0; x < nodesWidth; x++) {
				currentNode = nodes.get( (x + (y * nodesWidth)) );
				
				/* === Edge Flags === */
				boolean f_top    = false;
				boolean f_bottom = false;
				boolean f_left   = false;
				boolean f_right  = false;
				
				if (x == 0) {
					f_left = true;
				}
				else if (x == nodesWidth - 1) {
					f_right = true;
				}
				
				if (y == 0) {
					f_top = true;
				}
				else if (y == nodesHeight - 1) {
					f_bottom = true;
				}
				
				if (f_left) {
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 0) * nodesWidth)) ));
					
					if (f_top) {
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 1) * nodesWidth)) ));
					}
					else if (f_bottom) {
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 1) * nodesWidth)) ));
					} else {
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 1) * nodesWidth)) ));
					}
				}
				else if (f_right) {
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 0) * nodesWidth)) ));
					if (f_top) {
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
					}
					else if (f_bottom) {
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y - 1) * nodesWidth)) ));
					} else {
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y - 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
						currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 1) * nodesWidth)) ));
					}
				}
				else if (f_top) {
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 1) * nodesWidth)) ));

				}
				else if (f_bottom) {
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y - 1) * nodesWidth)) ));
				} else {
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 0) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 0) + ( (y - 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y - 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y - 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x - 1) + ( (y + 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 0) + ( (y + 1) * nodesWidth)) ));
					currentNode.addConnection(nodes.get( ( (x + 1) + ( (y + 1) * nodesWidth)) ));
				}
			}
		}
	}
	
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
	
	public Node getEnd() {
		return endNode;
	}
	
	public void setEnd(Node n) {
		System.out.println("End set");
		endNode = n;
	}
	
	
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
	
	
	private void drawNode(Graphics g, Node n) {

		g.setColor(Color.BLACK);
		g.drawOval(n.getX() - NODE_RADIUS, n.getY() - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
		//g.drawString(n.getId(), n.getX(), n.getY());
	}

	private void drawConnection(Graphics g, Node a, Node b, Color col, int thickness) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(col);
		g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	private void drawSequence(Graphics g, Node end) {
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
		
		for (Node n: nodes) {
			if (MathUtilities.euclideanDistance(n.getLoc(), arg0.getPoint()) <= MAX_RADIUS_FOR_CLICK) {
				n.setVisibile(setDragVisibility);
				pathfinder.resetDijkstra();
				break;
			}
		}
		
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
		mousePressed = true;
		mouseLoc = getMousePosition();
		for (Node n: nodes) {
			if (MathUtilities.euclideanDistance(n.getLoc(), mouseLoc) <= MAX_RADIUS_FOR_CLICK) {
				setDragVisibility = !n.isVisible();
				n.setVisibile(setDragVisibility);
			}
		}
		
	}

	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mousePressed = false;
		pathfinder.resetDijkstra();
	}
	
}
