/**
 * Collection of graph related tools
 */
package graphtoolkit;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * @author 100174454
 *
 */
public class ImageFilter {
	
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
}
