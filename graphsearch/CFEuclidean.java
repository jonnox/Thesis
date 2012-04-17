package graphsearch;

import graphinfo.Node;

public class CFEuclidean implements CostFunction {
	
	private float dx,dy;

	@Override
	public float getCost(Node a, Node b) {
		dx = (float) b.p.x - (float) a.p.x;
		dy = (float) b.p.y - (float) a.p.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
}
