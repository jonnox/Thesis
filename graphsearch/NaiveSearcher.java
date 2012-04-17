package graphsearch;

import graphinfo.Node;

import java.util.ArrayList;
import java.util.Vector;

public class NaiveSearcher {
	
	private CostFunction CF;
	
	public NaiveSearcher(){
		CF = new CFWColSlopePct();
	}
	
	public ArrayList<Integer> naiveSearch (Vector<Node> points){
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		Node node = points.get(0);
		indicies.add(0);
		
		int children = node.children.size(); 
		
		
		
		float cost, tmpCost;
		int index = 0;
		
		while(children > 0){			
			if(children == 1){
				index = node.children.get(0);
			}else{
				cost = 1000000;
				index = 0;
				
				for(int i=0 ; i < children; i++){
					tmpCost = CF.getCost(node, points.get(node.children.get(i)));
					if(tmpCost < cost){
						cost = tmpCost;
						index = node.children.get(i);
					}
				}
			}
			node = points.get(index);
			children = node.children.size();
			indicies.add(index);
		}
		
		
		return indicies;
	}

}
