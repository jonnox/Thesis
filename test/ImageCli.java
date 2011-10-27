/**
 * 
 */
package test;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import graphtoolkit.*;

/**
 * @author 100174454
 *
 */
public class ImageCli {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 3)
			usage();
		try{
			WritableRaster wr,wrbw;
			Raster image = ImageFile.loadImage(args[0]);
			int x,y, iArray[];
			iArray = new int[4];
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			int tol = Integer.parseInt(args[3]);
			wrbw = ImageFilter.isolateColour(image, image.getPixel(x, y, iArray), tol);
			ImageFile.writeImage("x" + x + "-y" + y + "-tol" + tol +
					"-r" + iArray[0] + "g" + iArray[1]
							+ "b" + iArray[2] + ".png", wrbw);
			wr = ImageFilter.convertToGrayScale(wrbw);
			wrbw = ImageFilter.convertToBW(wr, wr.getPixel(x, y, iArray)[0] + tol);
			ImageFile.writeImage("x" + x + "-y" + y + "-tol" + tol +
					"-r" + iArray[0] + "g" + iArray[1]
							+ "b" + iArray[2] + "-bw.png", wrbw);
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private static void usage(){
		System.out.println("Usage:\n" +
				"java test.ImageCli <filename> <pX> <pY> <tolerance> [<radius> <width> <height>]");
		System.exit(1);
	}

}
