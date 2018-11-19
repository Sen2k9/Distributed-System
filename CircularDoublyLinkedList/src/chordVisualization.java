
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class chordVisualization extends JPanel {
	private CircularDoublyLinkedList list = new CircularDoublyLinkedList();
	private int no_use=0;
	private static Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
	public chordVisualization(CircularDoublyLinkedList list, int operation) {

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
	}

}
