package graphsearch;

import graphinfo.Node;

import java.util.ArrayList;
import java.util.Vector;

public class GreedySearch {
	
	private CostFunction CF;
	
	public GreedySearch (CostFunction CF){
		this.CF = CF;
	}
	
	public ArrayList<Integer> search (Vector<Node> points){
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
				
				//System.out.printf("(%d,%d) ->\n", node.p.x,node.p.y);
				
				for(int i=0 ; i < children; i++){
					
					Node tmpn = points.get(node.children.get(i));
					
					tmpCost = CF.getCost(node, points.get(node.children.get(i)));
					
					//System.out.printf("\t%d (%d,%d) - [Cost:%f  pctCn:%f wid:%d]\n",node.children.get(i),tmpn.p.x,tmpn.p.y, tmpCost,tmpn.pctConnected,tmpn.area);
					
					if(tmpCost < cost){
						cost = tmpCost;
						index = node.children.get(i);
					}
				}
				
				//System.out.printf("\tChoosing: %d\n\n",index);
			}
			
			node = points.get(index);
			children = node.children.size();
			indicies.add(index);
		}
		
		return indicies;
	}


}
