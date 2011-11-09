package graphtoolkit;

import graphinfo.Node;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Vector;

public class AdaptiveCrawler {
	
	ImageVisualization iv;
	int STANDARD_STEP = 8;
	int DELTA_STEP = 2;
	int MAX_STEP = 40;
	
	ArrayList<ArrayList<Point>> maps = new ArrayList<ArrayList<Point>>();
	
	/**
	 * Tolerance of expected pixels
	 */
	private int sigma;
	
	public AdaptiveCrawler(Raster r){
		this.iv = new ImageVisualization(r,0,0);
		new ImageVisualization(r,0,r.getHeight() + 50);
		
		createMaps();
	}
	
	public AdaptiveCrawler(){
		this.iv = null;
		createMaps();
	}
	
	private void createMaps(){
		int left, right;
		right = STANDARD_STEP;
		left = STANDARD_STEP - DELTA_STEP;
		boolean isLeft = true;
		while(right <= MAX_STEP){
			maps.add(PointTools.createWalkMap(right));
			if(isLeft){
				maps.add(PointTools.createWalkMap(left));
				if(left > DELTA_STEP)
					left -= DELTA_STEP;
				else
					isLeft = false;
			}
			right += DELTA_STEP;
		}
	}
	
	public Vector<Point> crawl(Raster r, Point p){
		
		ImageVisualization bwiv = new ImageVisualization(r,r.getWidth() + 30, 0);
		new ImageVisualization(r,r.getWidth() + 30, r.getHeight() + 50);
		
		Vector<Point> nodes = new Vector<Point>();
		nodes.add(new Point(p.x,p.y));
		Point currP = p;
		
		Color currCol = Color.BLACK;

		maps.add(PointTools.createWalkMap(STANDARD_STEP));
		ArrayList<Point> currMap;
		Dimension d = PointTools.findOptimalKernel(r, p);
		sigma = PointTools.kernelCount(d,r,p);
		int tol = sigma - (sigma / 2);
		int[] convolution;
		int x,y,i,numXY,max,index;
		
		int hw = d.width / 2;
		int hh = d.height / 2;
		
		
		while((currP.x + d.width + DELTA_STEP) < r.getWidth()){
			max = numXY = x = y = 0;
			index = 0;
			currCol = Color.black;
			while(numXY < 1 && index < maps.size()){
				currMap = maps.get(index);
				if(iv != null)
					PointTools.convolve(r, iv, currCol, currP, currMap, d);
				
				convolution = PointTools.convolve(r, bwiv, currCol, currP, currMap, d);
				
				for(i=0;i<currMap.size();i++){
					if(convolution[i] >= tol){
						if(convolution[i] > max){
							max = convolution[i];
							numXY = 1;
							x = currMap.get(i).x + hw;
							y = currMap.get(i).y + hh;
						}else if( convolution[i] == max){
							numXY++;
							x += currMap.get(i).x + hw;
							y += currMap.get(i).y + hh;
						}
					}
				}
				index++;
				currCol = (index % 2 == 0) ? Color.red : Color.green;
			}
			if(numXY > 0){
				nodes.add(new Point(currP.x + (x / numXY), currP.y + (y / numXY)));
				currP = nodes.lastElement();
				//System.out.println("Found point at (" + currP.x + "," + currP.y + ")");
			}else{
				break; // End of line
			}
		}
		
		return nodes;
	}
}
