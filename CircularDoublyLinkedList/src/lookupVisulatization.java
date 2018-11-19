
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class lookupVisulatization extends JPanel {
	private CircularDoublyLinkedList list = new CircularDoublyLinkedList();
	private int no_use=0;
	private static Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
	public lookupVisulatization(CircularDoublyLinkedList list, int operation) {

		this.list = list;
		this.no_use=operation;

	}

	public void paint(Graphics g) {
		int width= 1200/2;
		int height= 1200/2;
		super.paint(g);
		this.setBackground(Color.WHITE);

		
		map = list.update();
		ArrayList<Integer> key = new ArrayList<Integer>();

		int a = 0;
		map.forEach((name, age) -> key.add(name));
		Collections.sort(key);
		ArrayList<Integer> lookup_key = new ArrayList<Integer>();
		Collections.sort(lookup_key);
		for(int i=0;i<key.size();i++) {
			if(no_use==key.get(i)) {
				lookup_key.add(key.get(i));
				break;
			}
			lookup_key.add(key.get(i));
		}
		double x=0;
		double y=0;
		int p,q;

		for(int i=1;i<=32;i++) {
			x= (width)+270*Math.cos(Math.toRadians((2*180)*(i-1)/32));
			y= (height/2)+270*Math.sin(Math.toRadians((2*180)*(i-1)/32));

					
			p= (int)x;
			q= (int)y;
			String s= Integer.toString(i-1);
			

			g.setColor(Color.blue);
			
			
			g.drawString(s, Math.abs(p), Math.abs(q));
			g.setColor(Color.RED);
			g.drawOval(Math.abs(p), Math.abs(q), 20, 20);
			
		}
		for (int i = 1; i <= key.size(); i++) {
			x= (width)+270*Math.cos(Math.toRadians((2*180)*key.get(i-1)/32));
			y= (height/2)+270*Math.sin(Math.toRadians((2*180)*key.get(i-1)/32));

			p= (int)x;
			q= (int)y;

			
			g.setColor(Color.GREEN);
			g.fillOval(Math.abs(p), Math.abs(q), 20, 20);

		}
		
		int c=0;
		int d=0;

		for (int i = 1; i <= lookup_key.size(); i++) {
			x= (width)+270*Math.cos(Math.toRadians((2*180)*lookup_key.get(i-1)/32));
			y= (height/2)+270*Math.sin(Math.toRadians((2*180)*lookup_key.get(i-1)/32));

			p= (int)x;
			q= (int)y;

			
			g.setColor(Color.PINK);
			g.fillOval(Math.abs(p), Math.abs(q), 20, 20);

		}
		c=  (int) ((int)(width)+270*Math.cos(Math.toRadians((2*180)*lookup_key.get(0)/32)));
		d= (int) ((int)(height/2)+270*Math.sin(Math.toRadians((2*180)*lookup_key.get(0)/32)));
		for (int i = 1; i < lookup_key.size(); i++) {
			x= (width)+270*Math.cos(Math.toRadians((2*180)*lookup_key.get(i)/32));
			y= (height/2)+270*Math.sin(Math.toRadians((2*180)*lookup_key.get(i)/32));

			p= (int)x;
			q= (int)y;

		  g.setColor(Color.BLACK);
		  g.drawLine(c+5, d, p+5, q);// line 1
		  c=p;
		  d=q;

		}
	}

	


}
