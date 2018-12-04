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
import mathUtilities.MathUtilities;
import dijkstra.Dijkstra;
import dijkstra.Node;
import mathUtilities.MathUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class MapGenerator extends JPanel implements MouseListener {
	
	
	/*
	 * This is a super-class for all the other map generation types. It contains basic functions and fields that 
	 * the other sub-classes need. There are most likely other methods or fields that could be brought over to 
	 * this class to avoid re-writing code but I haven't had a chance to go back and look for them since this 
	 * was added after I had a working sample to make future expansion easier
	 */
	

	/* GRAPHICS CONSTANTS */
	protected static final long serialVersionUID = 1L;
	protected static final int WIDTH  = 750;
	protected static final int HEIGHT = 750;
	protected static final int SAFE_SIZE = 25;
	protected static final int NODE_RADIUS = 1;
	protected static final int CONNECTION_WIDTH = 5;
	/* GENERATION CONSTANTS */
	protected static final float MIN_WEIGHT_FOR_CONNECTION = 7.0f;
	protected static final float MIN_WEIGHT_FOR_VISIBILITY = 0.2f;
	protected static final float MAX_RADIUS_FOR_CLICK = 10.0f;
	/* GLOBAL FLAGS */
	protected boolean mousePressed = false;
	protected boolean setDragVisibility = false;
	protected Point mouseLoc;
	
	protected JFrame frame;
	protected Dijkstra pathfinder;
	
	protected ArrayList<Node> nodes;
	protected Node endNode = null;
	
	protected long currId = 0L;
	protected int n = 0;
	

	
	/* ===== GETTERS AND SETTERS ===== */
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
	
	
}
