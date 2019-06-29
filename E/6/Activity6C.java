import java.io.*;

public class Activity6C {
	public static void main(String[] args) {
		Student[] students = new Student[5];

		students[0] = new UndergradStudent(8032, "Casper", 2.78, 2);
		students[1] = new GraduateStudent(3044, "Sheena", 3.92, "Natural Language Processing");
		students[2] = new UndergradStudent(6170, "Yolanda", 4.26, 3);
		students[3] = new GraduateStudent(1755, "Geordi", 3.58, "Human-Computer Interaction");
		students[4] = new VisitingStudent(1234, "Dirk", 4.27, "University of Winnipeg");

		printStudents(students);
		printDeansList(students);

		System.out.println("\nEnd of processing.");
	}

	public static void printStudents(Student[] students) {
		System.out.println("\nList of all students:\n");
		for (int i = 0; i < students.length; i++) {
			System.out.println(i + 1 + ": " + students[i]);
		}
	}
	
	public static void printDeansList(Student[] students) {
		System.out.println("\nDean's honour list:\n");
		for (int i = 0; i < students.length; i++) {
			if (students[i].deansHonourList()) {
				System.out.println(students[i]);
			}
		}
	}
}

class Student {
	private int number;
	private String name;
	private double gpa;

	public Student(int number, String name, double gpa) {
		this.number = number;
		this.name = name;
		this.gpa = gpa;
	}

	public double minGPA() {
		return 100;
	}

	public boolean deansHonourList() {
		boolean result = false;
		if (gpa >= minGPA())
			result = true;
		return result;
	}


	public String toString() {
		return number + " " + name + " (" + gpa + ")";
	}
}

class UndergradStudent extends Student {
	private int year;

	public UndergradStudent(int number, String name, double gpa, int year) {
		super(number, name, gpa);
		this.year = year;
	}

	public double minGPA() {
		return 3.5;
	}

	public String toString() {
		return "Undergraduate: " + super.toString() + " year: " + year;
	}
}

class GraduateStudent extends Student {
	private String thesis;

	public GraduateStudent(int number, String name, double gpa, String thesis) {
		super(number, name, gpa);
		this.thesis = thesis;
	}

	public String toString() {
		return "Graduate: " + super.toString() + " thesis: " + thesis;
	}

}

class VisitingStudent extends Student {
	private String homeUniversity;

	public VisitingStudent(int number, String name, double gpa, String homeUniversity) {
		super(number, name, gpa);
		this.homeUniversity = homeUniversity;
	}

	public String toString() {
		return "Visiting: " + super.toString() + " home university: " + homeUniversity;
	}
}
