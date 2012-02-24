package graphtoolkit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Collection of tools used when dealing with a point on a line in a
 * given image
 * @author 100174454
 *
 */
public class PointTools {
	
	/**
	 * Calculates the number of directly connected pixels (3x3 winner-take-all square)
	 * of a line between 2 points on a raster
	 */
	public static float findPercentConnected(Point p0, Point p1, Raster r){
		//+++++++++++++++++++++++++++++++++++++++++++++++
		// Calculate percentage direct connect
		int x1 = p1.x; //(int) tmpNode.x;
		int y1 = p1.y; //(int) tmpNode.y;
		int x0 = p0.x; // (int) currNode.p.x;
		int y0 = p0.y; //(int) currNode.p.y;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx,sy,err,numConnected,lineLen, e2;
		int tmpColorBW[] = new int[4];
		
		if(x0 < x1)
			sx = 1;
		else
			sx = -1;
		   
		 if (y0 < y1)
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
			 return (float) numConnected / (float) lineLen;
		 
		 return 0.0f;

		 //+++++++++++++++++++++++++++++++++++++++++++++++
	}
	
	/**
	 * Computes a walkmap for the semi-circle sampling for faster computation
	 * 
	 * Taken from http://www.mindcontrol.org/~hplus/graphics/RasterizeCircle.html
	 * @param radius
	 * @return
	 */
	public static ArrayList<Point> createWalkMap(int radius){
		ArrayList<Point> map = new ArrayList<Point>();

		int rs2 = radius*radius*4; /* this could be folded into ycs2 */
		int xs2 = 0;
		int ys2m1 = rs2 - (2*radius) + 1;
		int x = 0;
		int y = radius;
		int ycs2;

		map.add(new Point(x,y));

		while( x <= y ) {
		  /* advance to the right */
		  xs2 = xs2 + 8*x + 4;
		  ++x;
		  /* calculate new Yc */
		  ycs2 = rs2 - xs2;
		  if( ycs2 < ys2m1 ) {
		    ys2m1 = ys2m1 - 8*y + 4;
		    --y;
		  }
		  map.add(new Point(x,y));
		}

		// ---------------------------------
		// Copy 1/8 circle to create  1/4 circle
		// ---------------------------------
		int j = map.size() - 2;

		for(int i=j;i>=0;i--){
			map.add(new Point(map.get(i).y,map.get(i).x));
		}


		//---------------------------------
		//Remove any backtracking points
		//---------------------------------
		int curx = 0;
		for(int i=0;i < map.size();i++){
			if(map.get(i).x < curx){
				map.remove(i);
				i--;
			}else{
				curx = map.get(i).x;
			}
		}

		//---------------------------------
		//Remove duplicates
		//---------------------------------
		for(int i=1;i < map.size();i++){
			if(map.get(i).x  == map.get(i-1).x){
				if(map.get(i).y  == map.get(i-1).y){
					map.remove(i);
					i--;
				}
			}
		}

		//---------------------------------
		//Copy 1/4 circle to create  1/2 circle
		//---------------------------------
		j = map.size() - 2;

		for(int i=j;i>=0;i--){
			map.add(new Point(map.get(i).x,map.get(i).y * -1));
		}
	    
		return map;
	}
	
	/**
	 * Convolves a square kernel with a patch on a walkmap of a given image
	 * @param r black and white image
	 * @param p center of walkmap arc
	 * @param map walkmap of points
	 * @param kw width of the kernel
	 * @param kh height of the kernel
	 * @return number of black pixels at each map step 
	 */
	public static int[] convolve(Raster r, Point p, ArrayList<Point> map, Dimension d){
		
		int kw = d.width, kh = d.height;

		int result[] = new int[map.size()];
		int hheight = kh / 2;
		int[] tmp = new int[4];
		int yPos,xPos,yfPos,xfPos,rWidth,rHeight;
		
		rWidth = r.getWidth();
		rHeight = r.getHeight();
		
		for(int i=0;i<map.size();i++){
			
			yPos = map.get(i).y - hheight + p.y;
			xPos = map.get(i).x + p.x;
			
			if((xPos + kw) > r.getWidth())
				xPos = r.getWidth() - kw;
			
			if(yPos < 0)
				yPos = 0;
			else if((yPos + kh) > r.getHeight())
				yPos = r.getHeight() - kh;
			
			xfPos = xPos + kw;
			yfPos = yPos + kh;
			
			if(xfPos > rWidth)
				xfPos = rWidth;
			if(yfPos > rHeight)
				yfPos = rHeight;
			
			for(int j= yPos; j < yfPos; j++){
				for(int k = xPos; k < xfPos; k++){
					r.getPixel(k,j, tmp);
					if(tmp[0] == 0)
						result[i]++;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Convolves a square kernel with a patch on a walkmap of a given image, calculating the average colour
	 * @param r black and white image
	 * @param origR original raster (assumed to be colour)
	 * @param avgColour the average RGB values of the kernel taken from origR
	 * @param p center of walkmap arc
	 * @param map walkmap of points
	 * @param kw width of the kernel
	 * @param kh height of the kernel
	 * @return number of black pixels at each map step 
	 */
	public static int[] convolveWithAttr(Raster r, Raster origR, int[][] avgColour, Point p, ArrayList<Point> map, Dimension d){
		
		int kw = d.width, kh = d.height;

		int result[] = new int[map.size()];
		int hheight = kh / 2;
		int[] tmp = new int[4];
		int yPos,xPos,yfPos,xfPos,rWidth,rHeight;
		
		for(int i=0; i < map.size(); i++){
			for(int j=0; j < 3; j++){
				avgColour[i][j] = 0;
			}
		}
		
		rWidth = r.getWidth();
		rHeight = r.getHeight();
		
		for(int i=0;i<map.size();i++){
			
			yPos = map.get(i).y - hheight + p.y;
			xPos = map.get(i).x + p.x;
			
			if((xPos + kw) > r.getWidth())
				xPos = r.getWidth() - kw;
			
			if(yPos < 0)
				yPos = 0;
			else if((yPos + kh) > r.getHeight())
				yPos = r.getHeight() - kh;
			
			xfPos = xPos + kw;
			yfPos = yPos + kh;
			
			if(xfPos > rWidth)
				xfPos = rWidth;
			if(yfPos > rHeight)
				yfPos = rHeight;
			
			for(int j= yPos; j < yfPos; j++){
				for(int k = xPos; k < xfPos; k++){
					System.out.printf("(%d,%d)",k,j);
					r.getPixel(k,j, tmp);
					if(tmp[0] == 0){
						result[i]++;
						origR.getPixel(k, j, tmp);
						for(int l=0; l < 3; l++)
							avgColour[i][l] += tmp[l];
					}
				}
			}
			System.out.printf("\n");
		}
		
		// Complete average for each result
		for(int i=0; i < map.size(); i++){
			if(result[i] > 0){
				for(int j=0; j < 3; j++)
					avgColour[i][j] = avgColour[i][j] / result[i]; 
			}
		}
		return result;
	}
	
	/**
	 * Calculates the 'expected' number of pixels at the peak of a convolution
	 * @param d dimensions of the kernel
	 * @param r black and white image
	 * @param p point on the line
	 * @return expected number of pixels in the kernel at max
	 */
	public static int kernelCount(Dimension d, Raster r, Point p){
		int max = 0,yPos,yfPos, pix[] = {0,0,0,0};
		yPos = p.y - (d.height / 2) - 2;
		if(yPos < 0)
			yPos = 0;
		
		for(int i=0;i<5;i++){
			yfPos = yPos + d.height;
			int tmp = 0;
			for(int j=yPos; j < yfPos; j++){
				for(int k=p.x; k < (p.x + d.width); k++){
					if(r.getPixel(k, j, pix)[0] == 0)
						tmp++;
				}
			}
			if(tmp > max)
				max = tmp;
			yPos++;
		}
		return max;
	}
	
	/**
	 * Calculates an optimal kernel size for the line segment. The default width is 3px
	 * @param r black and white image
	 * @param p point on line
	 * @param prnt print kernel to stdout
	 * @return dimension of optimal kernel for convolution
	 */
	public static Dimension findOptimalKernel(Raster r, Point p, boolean prnt){
		int width = 3,i,area,max,yPos,yfPos;
		int height = 5;
		int[] range = {0,0,0,0,0};
		int[] pix = {0,0,0,1};
		boolean done = false;
		
		while(! done){
			area = width * height;
			max = 0;
			
			yPos = p.y - (height / 2) - 2;
			if(yPos < 0)
				yPos = 0;
			
			for(i=0;i<5;i++){
				range[i] = 0; // reset count
				yfPos = yPos + height;
				for(int j=yPos; j < yfPos; j++){
					for(int k=p.x; k < (p.x + width); k++){
						if(r.getPixel(k, j, pix)[0] == 0)
							range[i]++;
					}
				}
				yPos++;
			}
			
			done = true;
			for(i=1;i<5;i++){
				if(range[i] == range[i-1]){
					if(range[i] > range[max]){
						max = i;
						done = false;
					}
				}
			}
			
			if(prnt){
			
			for(i=0;i<5;i++)
				System.out.print("["+range[i]+"]");
			System.out.print("\t(" + width + "x" + height + ")");
			}
			
			if(! done){
				if(range[max] < area)
					height--;
				else
					height++;
			}
			
			if(prnt)
				System.out.print(" -> (" + width + "x" + height + ")\n");	
			
		}
		
		return new Dimension(width,height);
	}
	
	/**
	 * Convolves a square kernel with a patch on a walkmap of a given image
	 * @param r black and white image
	 * @param wr parent Vis to draw progress on
	 * @param p center of walkmap arc
	 * @param map walkmap of points
	 * @param dimensions of the kernel
	 * @return number of black pixels at each map step 
	 */
	public static int[] convolve(Raster r, ImageVisualization wr, Point p, ArrayList<Point> map, Dimension d){
		int kw = d.width, kh = d.height;
		Color blk = Color.BLACK;
		Color red = Color.red;
		Color ired = Color.PINK;
		int blu[] = {0,0,0,1};
		
		int result[] = new int[map.size()];
		int hheight = kh / 2;
		int[] tmp = new int[4];
		int yPos,xPos,yfPos,xfPos,rWidth,rHeight;
		
		rWidth = r.getWidth();
		rHeight = r.getHeight();
		
		wr.setPixel(p.x, p.y, red);
			wr.setPixel(p.x - 1, p.y, ired);
			wr.setPixel(p.x + 1, p.y, ired);
			wr.setPixel(p.x, p.y + 1, ired);
			wr.setPixel(p.x, p.y - 1, ired);
			wr.setPixel(p.x - 1, p.y+1, ired);
			wr.setPixel(p.x - 1, p.y-1, ired);
			wr.setPixel(p.x + 1, p.y+1, ired);
			wr.setPixel(p.x + 1, p.y-1, ired);
		
		for(int i=0;i<map.size();i++){
			
			yPos = map.get(i).y - hheight + p.y;
			xPos = map.get(i).x + p.x;
			
			if((xPos + kw) > r.getWidth())
				xPos = r.getWidth() - kw;
			
			if(yPos < 0)
				yPos = 0;
			else if((yPos + kh) > r.getHeight())
				yPos = r.getHeight() - kh;
			
			xfPos = xPos + kw;
			yfPos = yPos + kh;
			
			if(i%2 == 0)
				wr.setPixel(xPos, yPos + hheight, blk);
			
			if(xfPos > rWidth)
				xfPos = rWidth;
			if(yfPos > rHeight)
				yfPos = rHeight;
			
			for(int j= yPos; j < yfPos; j++){
				for(int k = xPos; k < xfPos; k++){
					r.getPixel(k,j, tmp);
					if(tmp[0] == 0)
						result[i]++;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Convolves a square kernel with a patch on a walkmap of a given image
	 * @param r black and white image
	 * @param wr parent Vis to draw progress on
	 * @param p center of walkmap arc
	 * @param map walkmap of points
	 * @param dimensions of the kernel
	 * @return number of black pixels at each map step 
	 */
	public static int[] convolve(Raster r, ImageVisualization wr, Color c, Point p, ArrayList<Point> map, Dimension d){
		int kw = d.width, kh = d.height;
		
		int result[] = new int[map.size()];
		int hheight = kh / 2;
		int[] tmp = new int[4];
		int yPos,xPos,yfPos,xfPos,rWidth,rHeight;
		
		rWidth = r.getWidth();
		rHeight = r.getHeight();
		
		for(int i=0;i<map.size();i++){
			
			yPos = map.get(i).y - hheight + p.y;
			xPos = map.get(i).x + p.x;
			
			if((xPos + kw) > r.getWidth())
				xPos = r.getWidth() - kw;
			
			if(yPos < 0)
				yPos = 0;
			else if((yPos + kh) > r.getHeight())
				yPos = r.getHeight() - kh;
			
			xfPos = xPos + kw;
			yfPos = yPos + kh;
			
			if(i%2 == 0)
				wr.setPixel(xPos, yPos + hheight, c);
			
			if(xfPos > rWidth)
				xfPos = rWidth;
			if(yfPos > rHeight)
				yfPos = rHeight;
			
			for(int j= yPos; j < yfPos; j++){
				for(int k = xPos; k < xfPos; k++){
					r.getPixel(k,j, tmp);
					if(tmp[0] == 0)
						result[i]++;
				}
			}
		}
		
		return result;
	}
}
