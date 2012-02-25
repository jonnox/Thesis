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
		Dimension d = PointTools.findOptimalKernel(r, p, false);
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
	
	/**
	 * Adaptively crawls from a given point in the positive (right) x direction. The crawler uses
	 * a given colour Raster to implement a WeightCalculator to determine, if a junction of 2 or more
	 * points is reached, which direction to proceed and the level of confidence in that decision.
	 * 
	 * @param r black and white raster of image (already assumed to be clustered)
	 * @param p starting point of line to be crawled
	 * @param original original raster of graph (assumed to be colour)
	 * @return list of points on the line
	 */
	public Vector<Point> smartCrawl(Raster r, Point p, Raster original){
		
		ImageVisualization bwiv = new ImageVisualization(r,r.getWidth() + 30, 0);
		new ImageVisualization(r,r.getWidth() + 30, r.getHeight() + 50);
		
		Vector<Point> nodes = new Vector<Point>();
		Vector<Point> currPossibilities = new Vector<Point>();
		
		nodes.add(new Point(p.x,p.y));
		Point currP = p;
		
		Color currCol = Color.BLACK;

		maps.add(PointTools.createWalkMap(STANDARD_STEP));
		ArrayList<Point> currMap;
		Dimension d = PointTools.findOptimalKernel(r, p, true);
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
	
	/**
	 * Adaptively crawls from a given point in the positive (right) x direction. The crawler uses
	 * a given colour Raster to implement a WeightCalculator to determine, if a junction of 2 or more
	 * points is reached, which direction to proceed and the level of confidence in that decision. This
	 * adaptation logs all possible nodes (could have multiple edges)
	 * 
	 * @param r black and white raster of image (already assumed to be clustered)
	 * @param p starting point of line to be crawled
	 * @param original original raster of graph (assumed to be colour)
	 * @return list of points on the line
	 */
	public Vector<Node> newSmartCrawl(Raster r, Point p, Raster original){
		
		//ImageVisualization bwiv = new ImageVisualization(r,r.getWidth() + 30, 0);
		new ImageVisualization(r,r.getWidth() + 30, r.getHeight() + 50);
		
		Vector<Node> nodes = new Vector<Node>();
		Vector<Node> currPossibilities = new Vector<Node>();
		
		int tmpColor[] = new int[4];
		
		int uniqueNodeId = 0;
		
		Node tmpNode = new Node(p.x,p.y,uniqueNodeId++);
		original.getPixel(p.x, p.y, tmpColor);
		tmpNode.c = new Color(tmpColor[0],tmpColor[1],tmpColor[2]);
		tmpNode.pctConnected = 1.0f;
		currPossibilities.add(tmpNode);
		
		Node currNode = tmpNode;
		currNode.prev = null;
		
		Color currCol = Color.BLACK;

		maps.add(PointTools.createWalkMap(STANDARD_STEP));
		
		ArrayList<Point> currMap;
		Dimension d = PointTools.findOptimalKernel(r, p, false);
		sigma = PointTools.kernelCount(d,r,p);
		int tol = sigma - (sigma / 2);
		int[] convolution;
		int x,y,i,numXY,max,index;
		
		/* APPROXIMATE ARBITRARY WIDTH OF LINE */
		currNode.area = d.width * d.height * 2;
		/* ----------------------------------- */
		
		//System.out.printf("Kernel: %d x %d\n",d.width,d.height);
		
		int hw = d.width / 2;
		int hh = d.height / 2;
		
		int dx,dy;
		int x0,y0,x1,y1;
		int sx,sy;
		int err,e2;
		float lineLen;
		float numConnected;
		int[] tmpColorBW = new int[4];
		Node posNode;
		boolean foundHit;
		
		while(currPossibilities.size() > 0){
			currNode = currPossibilities.remove(0);

			if((currNode.p.x + d.width + DELTA_STEP) < r.getWidth()){
				max = numXY = x = y = 0;
				index = 0;
				currCol = Color.black;
				
				foundHit = false;
				
				while(!foundHit && index < maps.size()){
					currMap = maps.get(index);
					
					// If you want to show visualization, convolve it as well
					if(iv != null)
						PointTools.convolve(r, iv, currCol, currNode.p, currMap, d);
					
					int[][] avgColour = new int[currMap.size()][3];
					
					// Get convolution at current walk map
					convolution = PointTools.convolveWithAttr(r, original, avgColour, currNode.p, currMap, d);
					
					boolean localMax = false;
					int tmparea = 0;
					int currMapSize = currMap.size();
					
					ArrayList<int[]> possibleNodes = new ArrayList<int[]>();
					int currPosNode[] = { 0, 0, 0};
					
					// Iterare through and find all local max
					for(i=0;i < currMapSize;i++){
						if(convolution[i] >= tol){
							if(localMax){
								if(convolution[i] > currPosNode[2]){
									currPosNode[0] = currPosNode[1] = i;
									currPosNode[2] = convolution[i];
								}else if (convolution[i] == currPosNode[2]){
									currPosNode[1] = i;
								}
							}else{
								currPosNode = new int[3];
								foundHit = true; // Mark as success
								currPosNode[0] = currPosNode[1] = i;
								currPosNode[2] = convolution[i];
								localMax = true;
								possibleNodes.add(currPosNode);
							}
						}else{
							localMax = false;
						}
					}
					
					/*
					 * Convert all local maxs into nodes
					 */
					for(i=0; i < possibleNodes.size(); i++){
						currPosNode = possibleNodes.get(i);
						numXY = 0;
						x = 0;
						y = 0;
						for(int cnm=0; cnm < 3; cnm ++)
							tmpColor[cnm] = 0;
						for(int j = currPosNode[0]; j <= currPosNode[1]; j++){
							x += currMap.get(j).x;
							y += currMap.get(j).y;
							for(int cnm=0; cnm < 3; cnm ++)
								tmpColor[cnm] += avgColour[j][cnm];
							numXY++;
						}
						
						tmpNode = new Node(currNode.p.x + (x / numXY), currNode.p.y + (y / numXY), uniqueNodeId);
						if(tmpNode.p.x > currNode.p.x){
							
						currNode.children.add(uniqueNodeId++);
						// Set Radius of current walk map
						tmpNode.rad = currMap.get(0).y;
						// Set area
						tmpNode.area = tmparea;
						
						tmpNode.pctConnected = PointTools.findPercentConnected(currNode.p, tmpNode.p, r);
						
						
						tmpNode.c = new Color(tmpColor[0] / numXY,tmpColor[1] / numXY,tmpColor[2] / numXY);
						tmpNode.prev = currNode;
						
						
							currPossibilities.add(tmpNode);
					}else{
							//System.out.printf("(%d,%d) -> (%d,%d)\n",currNode.p.x, currNode.p.y, tmpNode.p.x,tmpNode.p.y);
							if(tmpNode.p.y >= currNode.p.y + 15)
								currPossibilities.add(tmpNode);
						}
					}
						
					index++;
					currCol = (index % 2 == 0) ? Color.red : Color.green;
				}

				/*
				while(numXY < 1 && index < maps.size()){
					currMap = maps.get(index);
					if(iv != null)
						PointTools.convolve(r, iv, currCol, currNode.p, currMap, d);

					//convolution = PointTools.convolve(r, bwiv, currCol, currNode.p, currMap, d);
					//convolution = PointTools.convolve(r, currNode.p, currMap, d);
					
					int[][] avgColour = new int[currMap.size()][3];
					
					convolution = PointTools.convolveWithAttr(r, original, avgColour, currNode.p, currMap, d);
					
					boolean localMax = false;
					int tmparea = 0;
					int currMapSize = currMap.size();
					
					for(i=0;i < currMapSize;i++){
						if(i < currMapSize && convolution[i] >= tol){
							
							tmparea += convolution[i];
							if(! localMax){
								localMax = true;
							}
							if(convolution[i] > max){
								max = convolution[i];
								numXY = 1;

								x = currMap.get(i).x;
								y = currMap.get(i).y;

								for(int cnm=0; cnm < 3; cnm ++)
									tmpColor[cnm] = avgColour[i][cnm];
							}else if( convolution[i] == max){
								numXY++;

								x += currMap.get(i).x;
								y += currMap.get(i).y;

								
								for(int cnm=0; cnm < 3; cnm ++)
									tmpColor[cnm] += avgColour[i][cnm];
							}

						}else{
							if(localMax){
								
								localMax = false;
								max = 0;
								tmpNode = new Node(currNode.p.x + (x / numXY), currNode.p.y + (y / numXY), uniqueNodeId);
								
								currNode.children.add(uniqueNodeId++);
								// Set Radius of current walk map
								tmpNode.rad = currMap.get(0).y;
								// Set area
								tmpNode.area = tmparea;
				*/				
								//+++++++++++++++++++++++++++++++++++++++++++++++
								// Calculate percentage direct connect
								/*
								x1 = (int) tmpNode.x;
								y1 = (int) tmpNode.y;
								x0 = (int) currNode.p.x;
								y0 = (int) currNode.p.y;
								dx = Math.abs(x1 - x0);
								dy = Math.abs(y1 - y0);
								if(currNode.x < tmpNode.x)
									sx = 1;
								else
									sx = -1;
								   
								 if (currNode.y < tmpNode.y)
									 sy = 1;
								 else 
									 sy = -1;
								 err = dx-dy;
								 lineLen = 0;
								 numConnected = 0;
								 while(true){
									 lineLen++;
									 r.getPixel(x0, y0, tmpColorBW);
								     if(tmpColorBW[0] == 0)
								    	 numConnected++;
								     else{
								    	 try{
								    		 boolean srnding = false;
								    		 for(int dpth = -1; dpth < 2; dpth++){
								    			 for(int brth = -1; brth < 2; brth++){
								    				 if(r.getPixel(x0 + brth, y0 + dpth, tmpColorBW)[0] == 0){
								    					 srnding = true;
										    			 break;
								    				 }
								    		 	}
								    			 if(srnding){
								    				 numConnected++;
								    				 break;
								    			 }
								    		 }
								    	 }catch(Exception e){
								    		 System.out.println("pctcon error: " + e);
								    	 }
								     }
								     if(x0 == x1 && y0 == y1)
								    	 break;
								     e2 = err << 1;
								     if(e2 > -dy){
								       err = err - dy;
								       x0 = x0 + sx;
								     }
								     if(e2 <  dx){ 
								       err = err + dx;
								       y0 = y0 + sy;
								     }
								 }
								 
								 if(lineLen > 0)
									 tmpNode.pctConnected = (float) numConnected / (float) lineLen;
								 else
									 tmpNode.pctConnected = 0.0f;
								 //+++++++++++++++++++++++++++++++++++++++++++++++
								*/
			/*					
								tmpNode.pctConnected = PointTools.findPercentConnected(currNode.p, tmpNode.p, r);
								
								
								tmpNode.c = new Color(tmpColor[0] / numXY,tmpColor[1] / numXY,tmpColor[2] / numXY);
								tmpNode.prev = currNode;
								currPossibilities.add(tmpNode);
							}
							tmparea = 0;
						}
					}
					index++;
					currCol = (index % 2 == 0) ? Color.red : Color.green;
				}
			*/
			}
			nodes.add(currNode);
		}
		
		return nodes;
	}
}
