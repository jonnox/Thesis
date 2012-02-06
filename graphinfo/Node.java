package graphinfo;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Node {
	//public float x,y;
	public int x,y;
	public Color c;
	public Point p;
	public Node prev;
	/**
	 * Radius from parent point to <code>this</code>
	 */
	public int rad;
	/**
	 * Percentage of connected pixels from parent to <code>this</code>
	 */
	public float pctConnected;
	/**
	 * Percentage of black pixels at max over convolution
	 */
	public float coverage;
	public int id;
	public ArrayList<Integer> children;
	// Representation of 'width' of line at point
	public int area;
	
	// Assumes Black and white
	public Node(int x, int y, Node prev){
		this.p = new Point(x,y);
		c = new Color(0, 0, 0);
		children = new ArrayList<Integer>();
	}
	
	// Assumes Black and white
	public Node(int x, int y, int id){
		this.x = x;
		this.y = y;
		this.p = new Point(x,y);
		//c = new Color(0, 0, 0);
		this.id = id;
		this.children = new ArrayList<Integer>();
		//this.prev = null;
		//this.next = null;
	}
	
	// Assumes Black and white
	public Node(int x, int y, Color c){
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	/**
	 * Calculate the cost from <code>this</code> node to <code>n</code>
	 * @param n the node being transferred to
	 * @return cost to move to the given node (lower cost is desirable)
	 */
	public float getCost(Node n){
		return 0.0f;
	}
}
