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
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import dijkstra.Dijkstra;
import dijkstra.Node;
import mathUtilities.MathUtilities;

public class NodeGrid extends MapGenerator implements MouseListener {
		
	private static final long serialVersionUID = 1L;

	/**
	 * Create an even grid of Nodes where each Node is connected to each of its neighbors.
	 * Each Node can be removed or re-added by clicking on it with a mouse
	 * @param nodesWidth The number of nodes wide to create the grid
	 * @param nodesHeight The number of nodes tall to create the grid
	 */
	public NodeGrid(int nodesWidth, int nodesHeight, Dijkstra pfinder) {
				pathfinder = pfinder;
				addMouseListener(this);
				
				nodes = new ArrayList<Node>();
				
				// Determining the spacing between Nodes
				float xDist = WIDTH / nodesWidth;
				float yDist = HEIGHT / nodesHeight;
				
				// Adding the Nodes to a 1D representation of the 2D map
				for (int y = 0; y < nodesWidth; y++) {
					for (int x = 0; x < nodesHeight; x++) {
						nodes.add( new Node( currId++, new Point( (int) (x*xDist), (int) (y*yDist) ) ) );
					}
				}
				
				// Creating the connections between Nodes and their neighbors
				createNodeGrid(nodesWidth, nodesHeight);
				
				frame = new JFrame("Node Grid - Dijkstra");
				
				setPreferredSize( new Dimension( WIDTH, HEIGHT));
				setBackground(Color.WHITE);
				
				frame.setLayout( new BorderLayout());
				frame.add( this, BorderLayout.CENTER);
				frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
				
				frame.pack();
				frame.setVisible(true);
	}
	

	/* ===== GENERATING MAP FUNCTIONS ===== */
	
	/**
	 * Creates the connections between the Nodes and their neighbors
	 * @param nodesWidth How many Nodes the map is wide
	 * @param nodesHeight How many Nodes the map is tall
	 */
	protected void createNodeGrid(int nodesWidth, int nodesHeight) {
		
		Node currentNode;
		
		for (int y = 0; y < nodesHeight; y++) {
			for (int x = 0; x < nodesWidth; x++) {
				
				// Get the current Node by traversing the 2D map, then convert the x,y position to a linear position
				currentNode = nodes.get( (x + (y * nodesWidth)) );
				
				/* === Edge Flags === */
				boolean f_top    = false;
				boolean f_bottom = false;
				boolean f_left   = false;
				boolean f_right  = false;
				
				// Check for any edge cases for the Nodes to avoid indexing out of bounds
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
				
				// Add the connections. This requires that the Nodes be in the correct order in the 1D list
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
	
	
	/* ===== PAINT METHODS ===== */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		
		// Check if the user has clicked on any Nodes, then change it's visibility
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

		
		// If the Nodes are visible then draw it and its connections to other Nodes
		for (Node n: nodes) {
			if (n.isVisible() && n.getConnections() != null) {
				for (Node c: n.getConnections()) {
					if (c.isVisible()) {
						drawConnection(g, n, c, Color.BLACK, 1);
					}
				}
			}
		}
		
		// If the pathFinder has found a path then draw the path
		if (endNode != null) {
			drawSequence(g, endNode);
		}
		
		// Draw all visible Nodes
		for (Node n: nodes) {
			if (n.isVisible()) {
				drawNode(g, n);
			}
		}
		
		this.repaint();
	}
	
	/**
	 * Draws the given Node using the Graphics component
	 * @param g The Graphics object used to draw on the JPanel
	 * @param n The Node object to draw
	 */
	protected void drawNode(Graphics g, Node n) {

		g.setColor(Color.BLACK);
		g.drawOval(n.getX() - NODE_RADIUS, n.getY() - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
	}

	/**
	 * A generic method for drawing a connection between two Nodes given multiple parameters
	 * @param g The Graphics objet for drawing on the JPanel
	 * @param a The Node you are drawing from
	 * @param b The Node you are drawing to
	 * @param col The color to make the connection, black for regular, blue for the path
	 * @param thickness The thickness to draw the connection. Connections that are part of the final path are thicker
	 */
	protected void drawConnection(Graphics g, Node a, Node b, Color col, int thickness) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(thickness));
		g2.setColor(col);
		g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	/**
	 * Draws the given path from the source Node to the end Node
	 * @param g The Graphics object to draw on the JPanel
	 * @param end The end Node
	 */
	protected void drawSequence(Graphics g, Node end) {
		Node prev = end.getFrom();
		Node curr = end;
		
		while (curr != null) {
			drawConnection(g, prev, curr, Color.BLUE, 3);
			prev = curr;
			curr = curr.getFrom();
		}
	}

	
	/* ===== MOUSE METHODS ===== */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		// Checks if the mouse click was close to any Nodes, and if it is sets a global flag to start removing or re-adding Nodes
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
		// Un-used
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// Un-used
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// Allows for press and hold adding or removing Nodes
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
		
		// Once the mouse and been released, re-run the pathFinder to find new path
		mousePressed = false;
		pathfinder.resetDijkstra();
	}
	
}
