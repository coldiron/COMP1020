import java.util.ArrayList;

public class Activity4C {
	public static void main(String[] args) {
		Portfolio jim, helen;
		Property property;

		jim = new Portfolio("Jim");
		jim.add(new Property(16, 8, 160));
		jim.add(new Property(24, 17, 130));
		jim.add(new Property(129, 180, 35));

		helen = new Portfolio("Helen");
		helen.add(new Property(9, 13, 120));
		helen.add(new Property(15, 15, 210));
		helen.add(new Property(9, 13, 120));


		jim.subdivide(0, true);
		jim.subdivide(2, false);
		helen.subdivide(0, false);

		property = jim.transfer(1, helen);
		System.out.println(property + " transferred from Jim to Helen for $" + property.value());
		property = helen.transfer(3, jim);
		System.out.println(property + " transferred from Helen to Jim for $" + property.value());

		jim.print();
		helen.print();

		System.out.println();
		System.out.println("Swapping properties:");

		int difference;

		difference = jim.swap(helen, 0, 1);

		jim.print();
		helen.print();

		System.out.println("Difference: $" + difference);
		System.out.println();

		difference = helen.swap(jim, 0, 1);

		jim.print();
		helen.print();

		System.out.println("Difference: $" + difference);
		System.out.println();

		jim.print();
		helen.print();

		difference = jim.swap(helen, 0, 1);

		System.out.println("Difference: $" + difference);
		System.out.println();


		System.out.println("\nEnd of processing.");
	}
}

class Property {
	private int length, width; // both are in metres
	private int valuePerSqM;   // value in $ per square metre
	
	public Property(int length, int width, int valuePerSqM) {
		this.length = length;
		this.width = width;
		this.valuePerSqM = valuePerSqM;
	}
	
	public int value() {
		return length * width * valuePerSqM;
	}
	
	public Property subdivide(boolean lengthwise) {
		Property subdivision;
		
		if (lengthwise) {
			subdivision = new Property(length / 2, width, valuePerSqM);
			length = (length + 1) / 2;
		} else {
			subdivision = new Property(length, width / 2, valuePerSqM);
			width = (width + 1) / 2;
		}
		
		return subdivision;
	}
	
	public String toString() {
		return "Property: " + length + "m long by " + width + "m wide ($" +
			valuePerSqM + " per square metre)";
	}
}

class Portfolio {
	private ArrayList<Property> list;
	private String owner;
	
	public Portfolio(String owner) {
		list = new ArrayList<Property>();
		this.owner = owner;
	}

	public int swap(Portfolio p, int firstPosition, int secondPosition) {
		Property firstProperty, secondProperty;

		firstProperty  = this.list.get(firstPosition);
		secondProperty = p.getList().get(secondPosition);

		list.set(firstPosition, secondProperty);
		p.list.set(secondPosition, firstProperty);

		return list.get(firstPosition).value() - p.getList().get(secondPosition).value();
	}

	public ArrayList<Property> getList() {
		return list;
	}
	
	public void add(Property p) {
		list.add(p);
	}

	public void print() {
		int totalValue;

		System.out.println(owner + "'s portfolio:");

		totalValue = 0;
		for (int i = 0; i < list.size(); i++) {
			System.out.println(" " + list.get(i));
			totalValue += list.get(i).value();
		}

		System.out.println("Total value: $" + totalValue);
	}

	public void subdivide(int position, boolean lengthwise) {
		Property toSubdivide;
		Property subdivision;

		toSubdivide = list.get(position);
		subdivision = toSubdivide.subdivide(lengthwise);

		list.add(subdivision);
	}
	
	public Property transfer(int position, Portfolio buyer) {
		Property toTransfer;

		toTransfer = list.get(position);
		list.remove(position);
		buyer.add(toTransfer);

		return toTransfer;
	}
}
