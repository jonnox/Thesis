package graphsearch;

import graphinfo.Node;

import java.util.ArrayList;
import java.util.Vector;

public class DijkstraSearch implements GraphSearch {
	
	CostFunction CF;
	
	public DijkstraSearch(CostFunction CF){
		this.CF = CF;
	}

	@Override
	public ArrayList<Integer> search(Vector<Node> points) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareSearch(Vector<Node> points) {
		// TODO Auto-generated method stub

	}

}
