package graphtoolkit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.util.ArrayList;

/**
 * Collection of tools used when dealing with a point on a line in a
 * given image
 * @author 100174454
 *
 */
public class PointTools {
	
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
	 * @param img black and white image
	 * @param p center of walkmap arc
	 * @param map walkmap of points
	 * @param kw width of the kernel
	 * @param kh height of the kernel
	 * @return number of black pixels at each map step 
	 */
	public static int[] convolve(Raster r, Point p, ArrayList<Point> map, int kw, int kh){
		int result[] = new int[map.size()];
		int hwidth = kw / 2;
		int hheight = kh / 2;
		int[] tmp = new int[4];
		for(int i=0;i<map.size();i++){
			for(int j= map.get(i).y - hheight + p.y; j < (map.get(i).y - hheight + p.y + kh); j++){
				for(int k = map.get(i).x - hwidth + p.x; k < (map.get(i).x - hwidth + p.x + kw); k++){
					r.getPixel(k,j, tmp);
					if(tmp[0] == 0)
						result[i]++;
				}
			}
		}
		
		return result;
	}
}
