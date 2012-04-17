package graphsearch;

import java.awt.Point;

import graphinfo.Node;

public class CFWColSlopePct implements CostFunction{
	
	private float th_RGB = 1.0f;
	private float th_SLOPE = 1.0f;
	
	public CFWColSlopePct(){
		
	}

	@Override
	public float getCost(Node a, Node b) {
		float cost = 0.0f;
		Point p = new Point(a.p.x,a.p.y);
		if(a.prev != null){
			p.x = a.prev.p.x;
			p.y = a.prev.p.y;
		}
		Point deltaP = new Point(a.x - p.x, a.y - p.y);
		float delta = 0.0f;
		if(deltaP.x != 0)
			delta = (float) deltaP.y / (float)deltaP.x;
		
		if(b.p.x == a.p.x)
			cost += 10.0f;
		else{
			// Difference in slope
			cost += th_SLOPE * (Math.abs(((b.p.y - a.p.y)/(b.p.x - a.p.x)) - delta));
		}
		cost += th_RGB * (Math.abs(a.c.getRed() - b.c.getRed()) / 255.0f);
		cost += th_RGB * (Math.abs(a.c.getGreen() - b.c.getGreen()) / 255.0f);
		cost += th_RGB * (Math.abs(a.c.getBlue() - b.c.getBlue()) / 255.0f);
		
		return cost;
	}
	
}
