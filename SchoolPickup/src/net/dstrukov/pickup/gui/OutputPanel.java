package net.dstrukov.pickup.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.CORBA.INITIALIZE;

import net.dstrukov.pickup.Schedule;

public class OutputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2474414638433717909L;
	private int marginsX = 50;
	private int marginsY = 10;
	private int gapX = 5;
	private int gapY = 10;
	
	private int graphicsTotalWidth ;
	private int graphicsTotalHeight ;
	private Point graphicsOrigin ;
	
	private int columnWidth;

	private int columnHeight ;
	
	private Schedule schedule = null;
	
	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public OutputPanel() {
		onResize();
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				onResize();
				invalidate();
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}


	private void onResize() {
		Dimension size = getSize();
		
		 graphicsTotalWidth = size.width-2*marginsX;
		 graphicsTotalHeight = size.height-2*marginsY;
		 graphicsOrigin = new Point(marginsX, marginsY);
		 columnWidth = (graphicsTotalWidth -4*gapX )/5;
		 columnHeight = (graphicsTotalHeight -gapY )/2;
			
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawWirework(g);
		fillSchedule(g);
		
	}
	
	private void fillSchedule(Graphics g) {
		
	}

	private void drawWirework(Graphics g) {
		
		drawColumnWirefreame(g,0,0);
		drawColumnWirefreame(g,1,0);
		drawColumnWirefreame(g,2,0);
		drawColumnWirefreame(g,3,0);
		drawColumnWirefreame(g,4,0);
		
		drawColumnWirefreame(g,0,1);
		drawColumnWirefreame(g,1,1);
		drawColumnWirefreame(g,2,1);
		drawColumnWirefreame(g,3,1);
		drawColumnWirefreame(g,4,1);
		
	}
	
	private void drawColumnWirefreame(Graphics g, int col, int row) {
		drawColumn(g,new Point(marginsX+col*columnWidth+col*gapX, marginsY+row*gapY+ row*columnHeight));
	}
	
	private void drawColumn(Graphics g, Point origin) {
		g.drawRect(origin.x, origin.y, columnWidth, columnHeight);
	}
	

	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(600,800);
		f.setTitle("OutputPanel Test");
		f.getContentPane().add(new OutputPanel(), BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
