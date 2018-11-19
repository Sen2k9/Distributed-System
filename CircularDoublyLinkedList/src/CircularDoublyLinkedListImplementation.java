import java.util.*;

import javax.swing.JFrame;


public class CircularDoublyLinkedListImplementation {
	private static Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		CircularDoublyLinkedList list = new CircularDoublyLinkedList();

		

		
		//single node input
		InsertFirstNode(list);
		update(list);
		
		System.out.println("What operation you want to perform? (1)Insert, (2)Delete, (3)Lookup, (0)Stop");
		
		int operation = in.nextInt();
		while(operation!=0) {
			
			if(operation==1) {
				insert(list);
				update(list);
				list.print();
				JFrame f = new JFrame("Visualization of Chord system");
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setVisible(true);
				f.setSize(2400, 2400);
				chordVisualization visualize = new chordVisualization(list, operation);
				
				f.add(visualize);
				
			}
			if(operation==2) {
				System.out.println("Insert the node ID which you want to delete: ");
				int id=in.nextInt();
				delete(list,id);
				update(list);
				
				list.print();
				JFrame f = new JFrame("Visualization of Chord system");
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setVisible(true);
				f.setSize(2400, 2400);
				chordVisualization visualize = new chordVisualization(list, operation);
				
				f.add(visualize);
			}
			if(operation==3) {
				System.out.println("Insert the node ID which you want to lookup: ");
				int id=in.nextInt();
				lookup(list,id);
			}
			System.out.println("See the visualization window based on your choice ");
			System.out.println("What operation you want to perform? (1)Insert, (2)Delete, (3)Lookup, (0)Stop");
			operation = in.nextInt();

			
		}
		


	}

	private static void lookup(CircularDoublyLinkedList list, int id) {
		
		if(id>32) {
			System.out.println("Input a valid ID");
			return;
		}
		map= list.update();
		int key= list.loopupWithId(map,id);
		System.out.println("The lookup ID: "+id+" contains in node: "+key);
		JFrame f = new JFrame("Visualization of lookup system");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
		f.setSize(2400, 2400);
		lookupVisulatization  lookup = new lookupVisulatization(list, key);
		
		f.add(lookup);
	}
	private static void InsertFirstNode(CircularDoublyLinkedList list) {
		int value=0;
		int position= list.FindPostion(value);
		
		list.InsertwithPosition(position, value);
		
	}
	private static void update(CircularDoublyLinkedList list) {
		map=list.update();
		
	}
	private static void delete(CircularDoublyLinkedList list, int id) {
		list.DeleteNode(id);
		
	}
	public static void insert(CircularDoublyLinkedList list) {
		int value = (int) (1+Math.random() * 31);
		while(list.IsConflict(value)) {
			
			value = (int) (1+Math.random() * 31);
		}
		
		int position= list.FindPostion(value);
		
		list.InsertwithPosition(position, value);
		
		
	}

}
