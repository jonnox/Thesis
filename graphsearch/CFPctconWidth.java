package graphsearch;

import graphinfo.Node;

public class CFPctconWidth implements CostFunction {

	@Override
	public float getCost(Node a, Node b) {
		float cost = 0.0f;
		cost = 1.0f - b.pctConnected;
		if(a.area > 0)
			cost += Math.min(0.1f, Math.abs((a.area - b.area)/a.area));
		return cost;
	}
}
