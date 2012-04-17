package graphsearch;

import graphinfo.Node;

import java.util.ArrayList;
import java.util.Vector;

public interface GraphSearch {
	public ArrayList<Integer> search (Vector<Node> points);
	public void prepareSearch(Vector<Node> points);
}
