package net.dstrukov.pickup.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	// column spacing
	private int marginsX = 50;
	private int marginsY = 10;
	private int gapX = 5;
	private int gapY = 10;
	// end column spacing
	
	
	
	//column element spacing partitioning
	int marginsHeadFoot;
	int hourSpace;
	// end
	
	
	
	private int graphicsTotalWidth ;
	private int graphicsTotalHeight ;
	
	private int columnWidth;

	private int columnHeight ;
	
	private Schedule schedule = null;
	
	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
		onResize();
		invalidate();
		repaint();
	}

	public OutputPanel() {
		onResize();
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				
				
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
		 columnWidth = (graphicsTotalWidth -4*gapX )/5;
		 columnHeight = (graphicsTotalHeight -gapY )/2;
		 
		 marginsHeadFoot =(int)(columnHeight*0.05);
		 hourSpace = (int)((columnHeight - marginsHeadFoot*2)/11);
		 
			
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		fillSchedule(g);
		drawWirework(g);
		drawLabels(g);
		
	}
	
	private void drawLabels(Graphics g) {
		drawColumnLabels(g,0,0);
		drawColumnLabels(g,1,0);
		drawColumnLabels(g,2,0);
		drawColumnLabels(g,3,0);
		drawColumnLabels(g,4,0);
		
		drawColumnLabels(g,0,1);
		drawColumnLabels(g,1,1);
		drawColumnLabels(g,2,1);
		drawColumnLabels(g,3,1);
		drawColumnLabels(g,4,1);
		
	}

	private void fillSchedule(Graphics g) {
		fillColumn(g,0,0);
		fillColumn(g,1,0);
		fillColumn(g,2,0);
		fillColumn(g,3,0);
		fillColumn(g,4,0);
		
		fillColumn(g,0,1);
		fillColumn(g,1,1);
		fillColumn(g,2,1);
		fillColumn(g,3,1);
		fillColumn(g,4,1);
	}

	private void fillColumn(Graphics g, int col, int row) {
		Point p = getOriginPoint(col, row);
		int x = p.x;
		int y = p.y + marginsHeadFoot;
		int hoursHeight = (columnHeight - marginsHeadFoot*2);
		double step = hoursHeight/(11D*60);
		
		int start = 8*60;
		int duration =180;
		
		int startHour = 8*60;
		if(schedule!=null) {
			int[] hours = row==0? schedule.getWorkStart1() : schedule.getWorkStart2();
			int min = 24*60;
			for(int h : hours) if(min>h)min=h;
			startHour= ((int)(min/60))*60;
			start = hours[col];
			
		}
		
		int yOffsetStart = (int)((start -startHour)*step);
		yOffsetStart = yOffsetStart>0? yOffsetStart: 0;
		
		if(schedule!=null) {
			duration =  row==0? schedule.getWorkHours1()[col] : schedule.getWorkHours2()[col];
		}
		
		Color bkp = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillRect(x, y+yOffsetStart, columnWidth, (int)(step*duration));
		g.setColor(bkp);
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
		drawColumn(g,getOriginPoint(col, row));
		drawColumnHourTics(g,getOriginPoint(col,row));
	}
	
	private void drawColumnHourTics(Graphics g, Point originPoint) {
		int x = originPoint.x;
		int y = originPoint.y + marginsHeadFoot;
		
		for(int i=0; i<12; i++) {
			g.drawLine(x, y, x+(int)(columnWidth*0.1), y);
			y+=hourSpace;
		}
		
		
	}

	private void drawColumnLabels(Graphics g, int col, int row) {
		Point origin = getOriginPoint(col, row);
		
		Graphics2D g2d = (Graphics2D)g;
		int x = origin.x +(int)(columnWidth*0.1);
		int y = origin.y + marginsHeadFoot + g2d.getFont().getSize();
		
		int startHour = 8;
		int pickupFlag = 0;
		int dropoffFlag = 0;
		
		if(schedule!=null) {
			int[] hours = row==0? schedule.getWorkStart1() : schedule.getWorkStart2();
			int min = 24*60;
			for(int h : hours) if(min>h)min=h;
			startHour= (int)(min/60);
			pickupFlag = schedule.getPickupFlag()[col];
			dropoffFlag = schedule.getDropOffFlag()[col];
		}
		
		for(int i =startHour; i<startHour+11; i++) {
			g2d.drawString(i+":00", x, y);
			y+=hourSpace;
		}
		
		String dropoff = String.format("%s\t", dropoffFlag == row+1 ? "X" : dropoffFlag == 0 ? "G" : "");
		String pickup = String.format("%s\t", pickupFlag == row+1 ? "X" : pickupFlag == 0 ? "G" : "");
		
		x = origin.x +(int)(columnWidth*0.4);
		y = origin.y + g2d.getFont().getSize();
		
		g2d.drawString(dropoff, x, y);
		
		g2d.drawString(pickup, x, y+columnHeight-marginsHeadFoot);
		
		
	}


	private Point getOriginPoint(int col, int row) {
		return new Point(marginsX+col*columnWidth+col*gapX, marginsY+row*gapY+ row*columnHeight);
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
