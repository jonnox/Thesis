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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class GUITester extends JFrame {
	
	public GUITester(String title){
		super(title);
		this.init(title);
		this.setVisible(true);
	}
	
	/**
	 * Initialize the frame
	 */
	private void init(String filename){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel leftpanel = new JPanel();  
		JScrollPane scrollPane = new JScrollPane(leftpanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel rightpanel = new JPanel();
		
		this.add(scrollPane);
		this.add(rightpanel);
		
		WritableRaster cwr;
		
		int p[] = new int[4];
		int iArray[] = new int[4];
		
		try{
			Raster image = ImageFile.loadImage(filename);
			cwr = image.createCompatibleWritableRaster();
			
			for(int i=0;i<image.getHeight();i++){
				for(int j=0; j < image.getWidth();j++){
					cwr.setPixel(j, i, image.getPixel(j, i, p));
				}
			}
			
		}catch (Exception e){
			System.out.println("Error: " + e.getMessage());
		}
		
	}// END init(String filename)
	
	public void findLine(Raster raster, Point p, int tolerance){
		WritableRaster wr,wrbw;
		int x,y, iArray[];
		iArray = new int[4];
		
		wrbw = ImageFilter.isolateColour(raster, raster.getPixel(p.x, p.y, iArray), tolerance);
		wr = ImageFilter.convertToGrayScale(wrbw);
		wrbw = ImageFilter.convertToBW(wr, wr.getPixel(p.x, p.y, iArray)[0] + tolerance);
		
		
	}
	
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			if(args.length < 1)
				usage();
			try{
				
				
				GUITester tester = new GUITester(args[0]);
				
				/*
				
				int p[] = new int[4];
				
				
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
				
				ImageFile.writeImage("x" + x + "-y" + y + "-tol" + tol +
						"-r" + iArray[0] + "g" + iArray[1]
								+ "b" + iArray[2] + "-bw.png", wrbw);
				
				//ImageVisualization ivbw = new ImageVisualization(wrbw);
				
				Dimension d = PointTools.findOptimalKernel(wrbw, new Point(x,y),false);
				
				// Use the parameterized constructor for visualization
				 
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
				}
				
				*/

			}catch(Exception e){
				System.out.println("Error: " + e.getMessage());
				System.exit(1);
			}
		}
		
		private static void usage(){
			System.out.println("Usage:\n" +
					"java test.GUITester <filename>");
			System.exit(1);
		}

	}