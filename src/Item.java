
public class Item {

	private int itemNbr;
	private double value;
	private double weight;
	
	public Item(int itemNbr, double value, double weight) {
		this.value = value;
		this.weight = weight;
		this.itemNbr = itemNbr;
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
	
	public int getItemNbr() {
		return this.itemNbr;
	}
	
	
}
