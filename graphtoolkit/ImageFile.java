package graphtoolkit;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Tools for reading and writing image files
 * @author 100174454
 *
 */
public class ImageFile {
	
	/**
	 * Creates a Raster from an image file
	 * @param filename path to file
	 * @return image data
	 */
	public static Raster loadImage(String filename) throws Exception{
		Raster image;
		image = ImageIO.read(new File(filename)).getData();
		return image;
	}
	
	/**
	 * Writes an image to a file in PNG format
	 * @param filename name of file
	 * @param r image data
	 * @return if successful creating file
	 */
	public static boolean writeImage(String filename, Raster r){
		BufferedImage bi = new BufferedImage(
				r.getWidth(), r.getHeight(), BufferedImage.TYPE_INT_RGB);
		bi.setData(r);
		try{
			ImageIO.write(bi, "png", 
				new File(filename));
		}catch(Exception e){
			System.err.println("File error:" + e.getMessage());
			return false;
		}
		return true;
	}
}
