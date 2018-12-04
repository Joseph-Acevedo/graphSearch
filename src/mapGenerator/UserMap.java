/**
 * @author	Joseph Acevedo
 * @since 	07 October, 2018
 */

package mapGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import dijkstra.Dijkstra;

public class UserMap extends MapGenerator implements MouseListener, ActionListener {
	
	private Dijkstra pathfinder;
	private JFrame frame;
	
	private JButton changeToNodes;
	private JButton changeToConns;
	private JButton setStart;
	private JButton setEnd;
	private JCheckBox autoConnectNodes;
	
	private static final String AC_NODES = "nodes";
	private static final String AC_CONNS = "conns";
	private static final String AC_START = "start";
	private static final String AC_END   = "end";
	private static final String AC_AUTO  = "auto";
	
	private String addMode = AC_NODES;
	private boolean autoConnect = false;

	// TODO: This is a work in progress. Currently UserMap does not function in a way that a pathFinder
	// Can run on it
	public UserMap(Dijkstra pfinder) {
		pathfinder = pfinder;
		setupWidgets();
		setupListeners();
		setupWindow();
	}
	
	/**
	 * Does all necessary work to setup a JPanel and JFrame to make the window appear
	 */
	private void setupWindow() {
		frame = new JFrame("User Created Map - Dijkstra");
		
		setPreferredSize( new Dimension(WIDTH,(int) (HEIGHT * 1.20)));
		setBackground(Color.WHITE);
		
		frame.setLayout(new BorderLayout());
		
		setLayout(null);
		add(changeToNodes);
		add(changeToConns);
		add(setStart);
		add(setEnd);
		add(autoConnectNodes);
		frame.add(this, BorderLayout.CENTER);

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Add a listener for all the widgets on screen
	 */
	private void setupListeners() {
		addMouseListener(this);
		
		changeToNodes.addActionListener(this);
		changeToConns.addActionListener(this);
		setStart.addActionListener(this);
		setEnd.addActionListener(this);
		
		autoConnectNodes.addActionListener(this);
	}
	
	/**
	 * Sets up the following for all widgets on screen: Color, size, location, fill, border, tooltips and text
	 */
	private void setupWidgets() {
		/* === INITIALIZE COMPONENTS === */
		changeToConns = new JButton("CONNECTIONS"); // Add an image here if you want
		changeToNodes = new JButton("NODES");
		setStart = new JButton("SET START");
		setEnd = new JButton("SET END");
		autoConnectNodes = new JCheckBox("AUTO-CONNECT NODES");
		
		/* === COLOR COMPONENTS === */
		changeToNodes.setBackground(Color.LIGHT_GRAY);
		changeToNodes.setBorder(new LineBorder(Color.BLACK));
		changeToNodes.setOpaque(false);
		
		changeToConns.setBackground(Color.LIGHT_GRAY);
		changeToConns.setBorder(new LineBorder(Color.BLACK));
		changeToConns.setOpaque(false);
		
		setStart.setBackground(Color.GREEN);
		setStart.setBorder(new LineBorder(Color.GREEN.darker()));
		setStart.setOpaque(true);
		
		setEnd.setBackground(Color.RED);
		setEnd.setBorder(new LineBorder(Color.RED.darker()));
		setEnd.setOpaque(true);
		
		/* === POSITION COMPONENTS === */
		changeToNodes.setBounds(SAFE_SIZE, HEIGHT + SAFE_SIZE, WIDTH / 5, (int) (1.5 * SAFE_SIZE) );
		
		changeToConns.setBounds(WIDTH - SAFE_SIZE - (WIDTH / 5) , 
				HEIGHT + SAFE_SIZE, WIDTH / 5, (int) (1.5 * SAFE_SIZE) );
		
		setStart.setBounds(SAFE_SIZE, HEIGHT + 3 * SAFE_SIZE, WIDTH / 5, (int) (1.5 * SAFE_SIZE) );
		
		setEnd.setBounds(WIDTH - SAFE_SIZE - (WIDTH / 5) , 
				HEIGHT + 3 * SAFE_SIZE, WIDTH / 5, (int) (1.5 * SAFE_SIZE) );
		
		autoConnectNodes.setBounds(WIDTH / 2, HEIGHT  + 2 * SAFE_SIZE, SAFE_SIZE, SAFE_SIZE);
		/* === SET TOOLSTIPS === */
		changeToNodes.setToolTipText("Click this to change mode to add/remove Nodes");
		changeToConns.setToolTipText("Click this to change mode to add/remove Connections");
		setStart.setToolTipText("Click this to set the next Node you click to be the Start");
		setEnd.setToolTipText("Click this to set the next Node you click to be the End");
		autoConnectNodes.setToolTipText("Click this to auto-connect a newly added Node to the previous Node");
		
		/* == ADD ACTION LISTENERS === */
		changeToNodes.setActionCommand(AC_NODES);
		changeToConns.setActionCommand(AC_CONNS);
		setStart.setActionCommand(AC_START);
		setEnd.setActionCommand(AC_END);
		autoConnectNodes.setActionCommand(AC_AUTO);
		
	}
	
	
	/* ===== ACTION LISTENER METHODS ===== */
	@Override
	public void actionPerformed(ActionEvent event) {
		if ( event.getActionCommand().equals(AC_NODES) ) {
			System.out.println("Nodes");
			addMode = AC_NODES;
		}
		else if ( event.getActionCommand().equals(AC_CONNS) ) {
			System.out.println("Connections");
			addMode = AC_CONNS;
		}
		else if ( event.getActionCommand().equals(AC_START) ) {
			System.out.println("Start");
		}
		else if ( event.getActionCommand().equals(AC_END) ) {
			System.out.println("End");
		}
		else if ( event.getActionCommand().equals(AC_AUTO) ) {
			autoConnect = autoConnectNodes.isSelected();
			System.out.println(autoConnect);
		} else {
			throw new IllegalArgumentException("Invalid action event received");
		}
		
	}

	/* ===== PAINT METHODS ===== */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawLine(0, HEIGHT, WIDTH, HEIGHT);
	}
	
	/* ===== MOUSE METHODS ===== */ 
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
