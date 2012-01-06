/**
 * 
 */
package test;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Vector;

import graphinfo.Node;
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
			WritableRaster wr,wrbw,cwr;
			Raster image = ImageFile.loadImage(args[0]);
			cwr = image.createCompatibleWritableRaster();
			
			int p[] = new int[4];
			
			for(int i=0;i<image.getHeight();i++){
				for(int j=0; j < image.getWidth();j++){
					cwr.setPixel(j, i, image.getPixel(j, i, p));
				}
			}
			
			//ImageVisualization iv = new ImageVisualization(cwr);
			
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
			
			//ImageVisualization ivbw = new ImageVisualization(wrbw);
			
			Dimension d = PointTools.findOptimalKernel(wrbw, new Point(x,y));
			
			AdaptiveCrawler aC = new AdaptiveCrawler(cwr);
			
			//Vector<Point> points = aC.crawl(wrbw, new Point(x,y));
			
			Vector<Node> points = aC.newSmartCrawl(wrbw, new Point(x,y), cwr);
			
			/*
			System.out.println("Points:");
			for(int i=0; i <points.size();i++)
				System.out.print("(" + points.get(i).x + "," + + points.get(i).y + ")");
			System.out.println("\n");
			*/
			
			System.out.println("graph G {");
			Node n;
			for(int i=0; i< points.size(); i++){
				n = points.get(i);
				for(int j=0; j < n.children.size(); j++){
					System.out.println("n" + n.id + " -> n" + n.children.get(j) + ";");
				}
			}
			System.out.println("}");
			
			/*

			int first[] = PointTools.convolve(wrbw, iv, new Point(x,y), PointTools.createWalkMap(50), d);
			//PointTools.convolve(wrbw, ivbw, new Point(x,y), PointTools.createWalkMap(50), d);
			
			System.out.print("\n--------------\nFirst convolution for X:" + x + " Y:" + y + "\n[" + first[0]);
			for(int i=1;i < first.length; i++)
				System.out.print("," + first[i]);
			System.out.println("]\n----------------");
			
			*/
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private static void usage(){
		System.out.println("Usage:\n" +
				"java test.ImageCli <filename> <pX> <pY> <tolerance>");
		System.exit(1);
	}

}
