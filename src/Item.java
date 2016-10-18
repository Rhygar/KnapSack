
public class Item {

	private double value;
	private double weight;
	
	public Item(double value, double weight) {
		this.value = value;
		this.weight = weight;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public double getBenefit() {
		return (value / weight);
	}
	
	
}
