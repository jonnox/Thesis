package graphtoolkit;

import java.awt.Point;
import java.awt.image.Raster;
import java.util.Vector;

public class WeightCalculator {
	private Raster original;
	/**
	 * Instantiate with an original raster of image
	 * @param original original raster of entire graph
	 */
	public WeightCalculator(Raster original){
		this.original = original;
	}
	
	public int selectBestPoint(Vector<Point> prevPoints, Vector<Point> points){
		int index = 0;
		float cost[];
		if(points.size() < 2){
			index = 0;
		}else{
			cost = new float[points.size()];
		}
		return index;
	}
	
	private float calculateCost(Point p, Vector<Point> prevPoints){
		float cost = 0.0f;
		
		return cost;
	}
}
