public class Activity9C {
	public static void main(String[] args) {
		String[] names = { "Zelma", "Clayton", "Casper", "Ihor", "Edwina" };
		int[] severities = { 1, 2, 3, 1, 3 };
		PatientList list;

		list = new PatientList();
		testPatientList(list, names, severities, 0);

		System.out.println("\nEnd of processing.");
	}

	public static void testPatientList(PatientList list, String[] names, int[] severities, int pos) {
		PatientList copy;

		if (pos < names.length) {
			list.add(names[pos], severities[pos]);
			copy = list.clone();

			copy.print();
			System.out.println("Admitting: " + copy.nextAdmission());
			System.out.println();

			testPatientList(list, names, severities, pos + 1);
			testPatientList(copy, names, severities, pos + 1);
		}
	}
}

class Patient {
	private String name;
	private int arrival;
	private int severity;
	
	public Patient(String name, int arrival, int severity) {
		this.name = name;
		this.arrival = arrival;
		this.severity = severity;
	}
	
	public boolean isAdmittedBefore(Patient other, int lastArrival) {
		boolean before;
		int priority, otherPriority;

		if (severity == 3) {
			// admitted before, unless the other's severity is 3 and arrived earlier
			before = (other.severity != 3) || (other.arrival > arrival);
		} else if (other.severity == 3) {
			before = false;
		} else {
			priority = (lastArrival - arrival) * severity;
			otherPriority = (lastArrival - other.arrival) * other.severity;
			before = (priority > otherPriority) ||
			         ((priority == otherPriority) && other.arrival > arrival);
		}
		
		return before;
	}

	public int getSeverity() {
		return severity;
	}

	public String toString() {
		return name + " arrived at " + arrival + " with severity " + severity;
	}
}

class PatientList {
	private PatientNode head;
	private int lastArrival;
	private int size;

	public PatientList() {
		head = null;
		lastArrival = 0;
		size = 0;
	}
	
	public void add(String name, int severity) {
		Patient patient;
		
		lastArrival++;

		patient = new Patient(name, lastArrival, severity);

		if(head == null || head.data.getSeverity() < severity) {
			head = new PatientNode(patient, head);
		}
		else {
			recursiveAdd(patient, head);
		}
	}

	public void recursiveAdd(Patient patient, PatientNode node) {
		PatientNode newNode;
		if(node.next == null) {
			newNode = new PatientNode(patient, null);
			node.next = newNode;
		}
		else if(patient.getSeverity() > node.next.data.getSeverity()) {
			newNode = new PatientNode(patient, node.next);
			node.next = newNode;
		}
		else {
			recursiveAdd(patient, node.next);
		}
	}


/* 	public PatientNode getInsertionNode(Patient patient) {
		PatientNode insertionNode = null;
		PatientNode current = head;
		PatientNode previous = current;
		boolean found = false;

		while(found == false && current.next != null) {
			if(patient.shouldComeBefore(current.data)) {
				insertionNode = previous;
				found = true;
			} 
			else {
				previous = current;
				current = current.next;
			}
		}

		return insertionNode;
	} */

/* 	public void add(String name, int severity) {
		Patient patient;
		
		lastArrival++;
		size++;

		patient = new Patient(name, lastArrival, severity);

		head = new PatientNode(patient, head);
	} */

	public int size() {
		return size;
	}
	
	public Patient nextAdmission() {
		PatientNode current;
		PatientNode previous;
		PatientNode toAdmitCurrent = null;
		PatientNode toAdmitPrevious = null;
		
		current = head;
		previous = null;
		while (current != null) {
			if (toAdmitCurrent == null) {
				toAdmitCurrent = current;
			} else {
				if (current.data.isAdmittedBefore(toAdmitCurrent.data, lastArrival)) {
					toAdmitCurrent = current;
					toAdmitPrevious = previous;
				}
			}
			previous = current;
			current = current.next;
		}
		
		if (toAdmitCurrent != null) {

			if (toAdmitPrevious == null) {
				head = toAdmitCurrent.next;
			} else {
				toAdmitPrevious.next = toAdmitCurrent.next;
			}
			
			return toAdmitCurrent.data;
		} else {
			return null;
		}
	}
	
	public void print() {
		PatientNode current;
		
		current = head;
		while (current != null) {
			System.out.println(current.data);
			current = current.next;
		}
		
		System.out.println("Size = " + size());
		System.out.println("Last arrival = " + lastArrival);
	}
	
	public PatientList clone() {
		PatientList copy;
		PatientNode current;
		PatientNode copyCurrent;
		PatientNode newNode;
		
		copy = new PatientList();
		current = head;
		copyCurrent = null;
		while (current != null) {
			newNode = new PatientNode(current.data, null);
			if (copyCurrent == null) {
				copy.head = newNode;
			} else {
				// last node in copy points to the new node
				copyCurrent.next = newNode;
			}
			// move to the next node in both lists
			copyCurrent = newNode;
			current = current.next;
			copy.size++;
		}
		copy.lastArrival = lastArrival;
		
		return copy;
	}
}

class PatientNode {
	public Patient data;
	public PatientNode next;
	
	public PatientNode(Patient data, PatientNode next) {
		this.data = data;
		this.next = next;
	}
}
