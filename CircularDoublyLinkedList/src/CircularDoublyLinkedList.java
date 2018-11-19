import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class CircularDoublyLinkedList {
	private Map<Integer, ArrayList<Integer>> listMap;
	private Node head;
	private Node dynamic_list;
	private int count;

	CircularDoublyLinkedList() {
		count = 0;
	}

	public void addNode(int value) {
		Node newNode = new Node(value);

		if (count == 0) {
			dynamic_list = newNode;
			dynamic_list.next = dynamic_list;
			dynamic_list.prev = dynamic_list;
			head = dynamic_list;

		} else {

			newNode.prev = dynamic_list.next;
			newNode.next = dynamic_list;

			dynamic_list.prev.next = newNode;
			dynamic_list.prev = newNode;

			head = dynamic_list;

		}
		count++;

	}

	public void print() {
		if (count == 0) {
			System.out.println("Empty");
			return;
		}
		Node current = head;
		do {

			//System.out.println(current.value);

			System.out.printf("Node %d contains nodes :\n [", current.value);
			ArrayList<Integer> nodelist = new ArrayList<Integer>(listMap.get(current.value));
			for (int j = 0; j < nodelist.size(); j++) {
				System.out.printf(nodelist.get(j) + " ");
			}
			System.out.println("]");
			current = current.next;
		} while (current != head);

	}

	public boolean IsConflict(int value) {
		boolean status = false;
		if (count == 0) {

			status = false;

		} else {

			Node current = head;

			do {
				if (value == current.value) {
					status = true;
					break;
				}
				current = current.next;
			} while (current != head);
		}

		return status;
	}

	public void InsertwithPosition(int position, int value) {

		if (position > count) {

			return;
		} else {
			Node newNode = new Node(value);

			Node current = head;
			if (count == 0) {

				dynamic_list = newNode;
				dynamic_list.next = dynamic_list;
				dynamic_list.prev = dynamic_list;
				head = dynamic_list;

			} else {

				int i = 0;
				if (position == 0) {

					newNode.prev = current.prev;
					newNode.next = current;

					current.prev.next = newNode;
					current.prev = newNode;
					head = newNode;

				} else {
					while (i < position) {

						current = current.next;
						i++;
					}

					newNode.prev = current.prev;
					newNode.next = current;
					current.prev.next = newNode;
					current.prev = newNode;
				}

			}
			count++;
		}
	}

	public void DeleteNode(int value) {

		Node current = head;

		boolean found = false;
		for (int i = 1; i <= count; i++) {
			if (value == current.value) {

				found = true;
				break;
			}
			current = current.next;

		}
		if (found == true) {

			if (value == 0 || current == head) {
				head = current.next;
				current.prev.next = current.next;
				current.next.prev = current.prev;
				count--;
				head = current.next;

			} else {
				current.prev.next = current.next;
				current.next.prev = current.prev;
				count--;
			}
		} else {
			System.out.println("Invalid ID");
			System.out.println("Input valid ID to delete node");
			System.out.println("See the chord system again. Green nodes are actual nodes ");
		}

	}

	private class Node {
		Node next;
		Node prev;
		int value;

		Node(int value) {
			this.value = value;
		}
	}

	public int FindPostion(int value) {

		int i = 0;
		if (count == 0) {
			return i;

		}

		Node current = head;

		if (count == 1 && value > current.value) {
			i++;

		}

		if (count > 1) {
			if (value < head.value) {

			} else {
				for (int j = 1; j <= count; j++) {
					if (value > current.value && value > current.next.value) {
						i++;

						current = current.next;

					}
					if (value > current.value && value < current.next.value) {
						i++;

						break;
					}

				}

			}

		}

		return i;

	}

	public Map<Integer, ArrayList<Integer>> update() {
		Node current = head;
		listMap = new HashMap<Integer, ArrayList<Integer>>();
		do {

			ArrayList<Integer> prenode = new ArrayList<Integer>();

			listMap.put(current.value, prenode);
			InputPreNode(listMap, current);

			current = current.next;
		} while (current != head);
		return listMap;

	}

	private void InputPreNode(Map<Integer, ArrayList<Integer>> adjListMap2, Node current) {
		if (current.prev.value > current.value) {

			for (int i = current.prev.value + 1; i <= 128; i++) {

				(listMap.get(current.value)).add(i);
			}
			for (int i = 0; i <= current.value; i++) {

				(listMap.get(current.value)).add(i);
			}
		} else {
			for (int i = current.prev.value + 1; i <= current.value; i++) {

				(listMap.get(current.value)).add(i);
			}
		}

	}

	public int loopupWithId(Map<Integer, ArrayList<Integer>> map, int id) {
		Node current = head;

		do {

			ArrayList<Integer> key = map.get(current.value);
			if (key.contains(id)) {
				break;

			}

			current = current.next;
		} while (current != head);
		return current.value;

	}

}
