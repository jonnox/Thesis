package graphtoolkit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageVisualization {
	
	public JFrame frame;
	public BufferedImage image;
	public ImageIcon icon;
	public Raster wr;
	Graphics gr;
	
	public ImageVisualization(Raster wr){
		this.wr = wr;
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(wr.getWidth(),wr.getHeight()));
		frame.setMinimumSize(new Dimension(wr.getWidth() + 30,wr.getHeight() + 50));
		image = new BufferedImage(wr.getWidth(),wr.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.setData(wr);
		icon = new ImageIcon(image);
		JLabel label = new JLabel(icon, JLabel.CENTER);
		frame.add(label);
		gr = image.getGraphics();
		gr.setColor(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public ImageVisualization(Raster wr, int x, int y){
		this.wr = wr;
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(wr.getWidth(),wr.getHeight()));
		frame.setMinimumSize(new Dimension(wr.getWidth() + 30,wr.getHeight() + 50));
		image = new BufferedImage(wr.getWidth(),wr.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.setData(wr);
		icon = new ImageIcon(image);
		JLabel label = new JLabel(icon, JLabel.CENTER);
		frame.add(label);
		frame.setLocation(x, y);
		gr = image.getGraphics();
		gr.setColor(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void paintSquare(Dimension d, int x, int y){
		gr.drawRect(x, y, d.width, d.height);
	}
	
	/*
	public void paint(Raster r){
		int rH,rW;
		int[] p = {0,0,0,1};
		rH = r.getHeight();
		rW = r.getWidth();
		
		if(wr.getHeight() == rH && wr.getWidth() == rW){
			for(int i=0; i < rH; i++)
				for(int j=0; j < rW; j++)
					wr.setPixel(j, i, r.getPixel(j, i, p));
			refresh();
		}
	}
	*/
	
	private void refresh(){
		frame.repaint();
	}
	
	public void setPixel(int x, int y, Color c){
		//wr.setPixel(x, y, dArray);
		gr.setColor(c);
		gr.drawLine(x, y, x, y);
		refresh();
	}
}
