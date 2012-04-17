package graphsearch;

import graphinfo.Node;

public class CFEuclideanColourRast implements CostFunction {

	private float dx,dy;
	/**
	 * Width of entire image
	 */
	private float width;
	
	public CFEuclideanColourRast(float width){
		this.width = width;
	}
	

	@Override
	public float getCost(Node a, Node b) {
		float cost = 0.0f;
		dx = (float) b.p.x - (float) a.p.x;
		dy = (float) b.p.y - (float) a.p.y;
		cost = (float) Math.sqrt(dx * dx + dy * dy) / width;
		int[] c0 = { a.c.getRed(),a.c.getGreen(),a.c.getBlue() };
		int[] c1 = { b.c.getRed(),b.c.getGreen(),b.c.getBlue() };
		
		cost = cost + 
				(Math.abs(c0[0] - c1[0]) + Math.abs(c0[1] - c1[1]) + Math.abs(c0[2] - c1[2]))
				/ 255f;
		
		cost = cost - b.pctConnected;
		
		cost = 1.0f - b.pctConnected;
		
		return cost;
	}

}

