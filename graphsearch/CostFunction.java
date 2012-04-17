package graphsearch;

import graphinfo.Node;

public interface CostFunction {
	public float getCost(Node a, Node b);
}
