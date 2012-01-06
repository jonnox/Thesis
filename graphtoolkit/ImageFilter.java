/**
 * Collection of graph related tools
 */
package graphtoolkit;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * @author 100174454
 *
 */
public class ImageFilter {
	
	private static final float MAX_DISTANCE = 442.0f;
	
	/**
	 * Luminosity bias of red data 
	 */
	private static final double RED_BIAS = 0.21;
	/**
	 * Luminosity bias of green data 
	 */
	private static final double GREEN_BIAS = 0.71;
	/**
	 * Luminosity bias of blue data 
	 */
	private static final double BLUE_BIAS = 0.07;
	
	/**
	 * Creates an RGB gray scale <code>Raster</code>
	 * @param r input image
	 * @return gray scale of <code>r</code>
	 */
	public static WritableRaster convertToGrayScale(Raster r){
		WritableRaster wr = r.createCompatibleWritableRaster();
		int image_w = r.getWidth();
		int image_h = r.getHeight();
		int grey_val = 0;
		int pVals[] = new int[4];
		// Iterate through each row, converting each pixel to grey
		for(int j = 0; j < image_h; j++){
			for(int i = 0; i < image_w; i++){
				grey_val = convLum(r.getPixel(i, j, pVals));
				pVals[0] = pVals[1] = pVals[2] = grey_val;
				wr.setPixel(i, j, pVals);
			}
		}
		return wr;
	}
	
	/**
	 * Uses the luminosity algorithm to convert a pixel to gray scale
	 * @param pData array containing the pixel colour RGB data values
	 * @return gray RGB value
	 */
	public static int convLum(int[] pData){
		int rvalue = 0;
		rvalue = (int)(pData[0] * RED_BIAS + pData[1] * GREEN_BIAS + pData[2] * BLUE_BIAS);
		return rvalue;
	}
	
	/**
	 * Uses the average RGB values to convert pixel to gray scale
	 * @param pData array containing the pixel colour RGB data values
	 * @return gray RGB value
	 */
	public static int convAvg(int[] pData){
		int rvalue = 0;
		rvalue = (int)((pData[0] + pData[1] + pData[2]) / 3);
		return rvalue;
	}
	
	/**
	 * Creates a black and white copy of a grey scale image based on a
	 * given threshold
	 * 
	 * @param r gray scale image
	 * @param intensity gray threshold
	 * @return black and white image
	 */
	public static WritableRaster convertToBW(Raster r, int intensity){
		WritableRaster wr = r.createCompatibleWritableRaster();
		int image_w = r.getWidth();
		int image_h = r.getHeight();
		int pVals[] = new int[4];
		int blPix[] = {0,0,0,1};
		int wtPix[] = {255,255,255,1};
		// Iterate through each row, converting each pixel to grey
		for(int j = 0; j < image_h; j++){
			for(int i = 0; i < image_w; i++){
				if(r.getPixel(i, j, pVals)[0] > intensity)
					wr.setPixel(i, j, wtPix);
				else
					wr.setPixel(i, j, blPix);
			}
		}
		return wr;
	}
	
	/**
	 * Creates a copy of an image thresholded by a given RGB value and
	 * tolerance
	 * 
	 * @param r colour image
	 * @param rgb pixel colour to isolate
	 * @param tol devience tolerated from colour isolation 
	 * @return isolated colour image
	 */
	public static WritableRaster isolateColour(Raster r, int rgb[], int tol){
		WritableRaster wr = r.createCompatibleWritableRaster();
		int image_w = r.getWidth();
		int image_h = r.getHeight();
		int pVals[] = new int[4];
		int wtPix[] = {255,255,255,1};
		// Iterate through each row, converting each pixel to grey
		for(int j = 0; j < image_h; j++){
			for(int i = 0; i < image_w; i++){
				if(Math.abs(r.getPixel(i, j, pVals)[0] - rgb[0]) > tol)
					wr.setPixel(i, j, wtPix);
				else if(Math.abs(r.getPixel(i, j, pVals)[1] - rgb[1]) > tol)
					wr.setPixel(i, j, wtPix);
				else if(Math.abs(r.getPixel(i, j, pVals)[2] - rgb[2]) > tol)
					wr.setPixel(i, j, wtPix);
				else
					wr.setPixel(i, j, pVals);
			}
		}
		return wr;
	}
	
	/**
	 * Clusters an image based on RGB space into a given number of clusters
	 * @param r colour image
	 * @param rgb RGB value of main cluster
	 * @param tol RGB spatial tolerance to split cluster into 2
	 * @return main cluster based on given RGB values
	 */
	
	/*
	public static WritableRaster kMeanCluster(Raster r, int rgb[], float tol){
		// start with 3 clusters - <colour>, black, white
		ArrayList<int[]> clusters = new ArrayList<int[]>();
		ArrayList<int[]> varCol = new ArrayList<int[]>();
		ArrayList<Integer> varN = new ArrayList<Integer>();
		
		int wtPix[] = {255,255,255};
		int blPix[] = {0,0,0};
		clusters.add(rgb);
		clusters.add(wtPix);
		clusters.add(blPix);
		
		//int varCol[][] = new int[clusters.length][3];
		//float varDis[] = new float[3];
		
		for(int i=0; i < 3; i++)
				varCol.add(clusters.get(i));
		
		int image_h = r.getHeight();
		int image_w = r.getWidth();
		
		int tmpk = 0;
		
		// Cluster assignment list
		char[][] cList = new char[r.getHeight()][r.getWidth()];
		boolean isChange = true;
		float d,tmpd,tmps; // distance
		
		int pVals[] = new int[4];
		
		int[] tmpc;
		
		// Will store final product to be returned
		WritableRaster wr = r.createCompatibleWritableRaster();
		
		while(isChange){
			isChange = false;
			for(int i = 0; i < image_h; i++){
				for(int j = 0; j < image_w; j++){
					d = MAX_DISTANCE;
					r.getPixel(j, i, pVals);
					for(int k=0;k<clusters.size();k++){
						tmpd = calcDistance(pVals,clusters.get(k));
						if(tmpd < d){
							tmpk = k;
							if(cList[i][j] != (char)k){
								cList[i][j] = (char)k;
								isChange = true;
							}
							d = tmpd;
						}
					}
					varN.set(tmpk, varN.get(tmpk) + 1);
					tmpc = varCol.get(tmpk);
					for(int k=0;k<3;k++)
						tmpc[k] = pVals[k] + tmpc[k];
					varCol.set(tmpk, tmpc);
				}
			}
			tmps = clusters.size();
			for(int k=0; k < tmps;k++){
				tmpc = varCol.get(k);
				for(int l=0; l < 3; l++){
					tmpc[l] = tmpc[l] / varN.get(k);
				}
				if(calcDistance(tmpc,clusters.get(k)) > tol){
					clusters.add(k + 1,tmpc);
					varCol.add(k + 1, tmpc);
					varN.add(k + 1,0);
					k++;
					tmps++;
				}else{
					varCol.set(k, tmpc);
				}
				varN.set(k,0);
			}
		}
		
		for(int i = 0; i < image_h; i++){
			for(int j = 0; j < image_w; j++){
				
			}
		}
		
		return wr;
	}
	*/
	
	/**
	 * Splits a 3D cluster into 2 clusters, weighted toward original points
	 * @param c1 first 3D point
	 * @param c2 second 3D point
	 * @return 2 clusters
	 */
	private static int[][] splitCluster(int[] c1, int[] c2){
		int res[][] = new int[2][3];
		int v[] = {c2[0] - c1[0],c2[1] - c1[1],c2[2] - c1[2]};
		for(int i=0; i < 3; i++){
			res[0][i] = (int)(v[i] * 0.3 + c1[i]);
			res[1][i] = (int)(v[i] * 0.7 + c1[i]);
		}
		return res;
	}
	
	/**
	 * Calculates the Euclidean distance between 2 points
	 * @param p1 first 3D point
	 * @param p2 second 3D point
	 * @return Euclidean distance between 2 points
	 */
	private static float calcDistance(int[] p1,int[] p2){
		float xl,yl,zl;
		xl = (float)(p2[0] - p1[0]);
		yl = (float)(p2[1] - p1[1]);
		zl = (float)(p2[2] - p1[2]);
		return (float)Math.sqrt(xl * xl + yl * yl + zl * zl);
	}
}
