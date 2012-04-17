package test;

import graphinfo.Node;
import graphsearch.CFPctconWidth;
import graphsearch.CostFunction;
import graphsearch.GreedySearch;
import graphtoolkit.AdaptiveCrawler;
import graphtoolkit.ImageFile;
import graphtoolkit.ImageFilter;
import graphtoolkit.ImageVisualization;
import graphtoolkit.PointTools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

public class EvalTester {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 1)
			usage();
		try{
			
			/****************************************
			 * EVALUATE TESTER FILE
			 ****************************************/
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String filename = reader.readLine();
			int fargs = Integer.parseInt(reader.readLine().trim());
			int numOfTests =  Integer.parseInt(reader.readLine().trim());
			int sX,sY;
			String line[];
			for(int i=0; i < numOfTests; i++){
				line = reader.readLine().split(",");
				sX = Integer.parseInt(line[0]);
				sY = Integer.parseInt(line[1]);
			}
			/****************************************
			 * EVALUATE TESTER FILE
			 ****************************************/
			WritableRaster wr,wrbw,cwr;
			Raster image = ImageFile.loadImage(args[0]);
			cwr = image.createCompatibleWritableRaster();
			
			int p[] = new int[4];
			
			for(int i=0;i<image.getHeight();i++){
				for(int j=0; j < image.getWidth();j++){
					cwr.setPixel(j, i, image.getPixel(j, i, p));
				}
			}
			
			int x,y, iArray[];
			iArray = new int[4];
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			int tol = Integer.parseInt(args[3]);
			
			//if(args[1].compareTo("bw") == 0)
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
			
			Dimension d = PointTools.findOptimalKernel(wrbw, new Point(x,y),false);
			
			/**
			 * Use the parameterized constructor for visualization
			 */
			AdaptiveCrawler aC = new AdaptiveCrawler(cwr);
			//AdaptiveCrawler aC = new AdaptiveCrawler();
			
			//Vector<Point> points = aC.crawl(wrbw, new Point(x,y));
			
			Vector<Node> points = aC.newSmartCrawl(wrbw, new Point(x,y), image);
			
			Node n;
			
			
			//CostFunction CF = new CFEuclideanColourRast((float) wrbw.getWidth());
			
			CostFunction CF = new CFPctconWidth();
			
			GreedySearch graphSearch = new GreedySearch(CF);
			
			ArrayList<Integer> line = graphSearch.search(points);
			
			ImageVisualization finalIV = new ImageVisualization(wrbw,cwr.getWidth() + 30,0);
			
			n = points.get(line.get(0));
			
			int tmp_x0, tmp_y0;
			for(int i=1; i < line.size(); i++){
				tmp_x0 = n.p.x;
				tmp_y0 = n.p.y;
				n = points.get(line.get(i));
				finalIV.drawLine(tmp_x0, tmp_y0, n.p.x, n.p.y, Color.cyan);
				System.out.printf("(%d,%d) -> (%d,%d)\n",tmp_x0, tmp_y0, n.p.x, n.p.y);
			}
			
			/*
			System.out.println("\n*****************************\n\n");
			
			for(int i=0; i < points.size(); i++){
				n = points.get(i);
				for(int j=0;j<n.children.size();j++)
					System.out.printf("%d\t(%d,%d) -> %d (pcn - %f)\n",i,n.p.x, n.p.y,n.children.get(j),n.pctConnected);
			}
			*/
			
			
			//System.out.printf("Points (%d)  Line (%d)\n", points.size(),line.size());
			//System.out.printf("Points (%d)", points.size());
			
			
			/*
			for(int i=0; i < line.size(); i++){
				n = points.get(line.get(i));
				System.out.printf(" -> (%d,%d) ", n.p.x, n.p.y);
			}
			*/
			
			//System.out.println("graph G {");
			
			/*
			for(int i=0; i< points.size(); i++){
				n = points.get(i);
				for(int j=0; j < n.children.size(); j++){
					System.out.println("n(" + n.id + ") -> n(" + n.children.get(j) + ") [pctg = " + points.get(n.children.get(j)).pctConnected + "]");
					System.out.println("\t(" + n.p.x + "," + n.p.y + ") -> (" + points.get(n.children.get(j)).p.x + "," + points.get(n.children.get(j)).p.y + ")");
				}
			}
			*/
			
			//System.out.println("}");
			 
			
			//System.out.println("END (" + points.size() + " points)");
			
			/*

			int first[] = PointTools.convolve(wrbw, iv, new Point(x,y), PointTools.createWalkMap(50), d);
			//PointTools.convolve(wrbw, ivbw, new Point(x,y), PointTools.createWalkMap(50), d);
			
			System.out.print("\n--------------\nFirst convolution for X:" + x + " Y:" + y + "\n[" + first[0]);
			for(int i=1;i < first.length; i++)
				System.out.print("," + first[i]);
			System.out.println("]\n----------------");
			
			*/
			
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	private static void usage(){
		System.out.println("Usage:\n" +
				"java test.EvalTester <tester filename>");
		System.exit(1);
	}

}
