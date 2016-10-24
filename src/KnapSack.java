import java.util.ArrayList;
import java.util.HashMap;


public class KnapSack {

	private double maxWeight, currentWeight = 0, currentValue = 0;
	private ArrayList<Item> items = new ArrayList<Item>();
	private int knapSackNbr;
	
	public KnapSack(double maxWeight, int knapSackNbr) {
//		items = new HashMap<>();
//		items = new ArrayList<Item>();
		this.maxWeight = maxWeight;
		this.knapSackNbr = knapSackNbr;
	}
	
	public KnapSack(KnapSack k) {
		this.maxWeight = k.getMaxWeight();
		this.currentValue = k.getCurrentValue();
		this.currentWeight = k.getCurrentWeight();
		this.items = (ArrayList<Item>) k.getItems().clone();
		this.knapSackNbr = k.getKnapSackNbr();
	}
	
	public double addItem(Item item) {
		if(this.currentWeight + item.getWeight() <= maxWeight) {
			items.add(item);
			currentWeight += item.getWeight();
			currentValue  += item.getValue();
			return currentWeight;
		}
		return -1;
	}
	
	public void remove(Item item) {
		currentWeight -= item.getWeight();
		currentValue  -= item.getValue();
		items.remove(item);
	}
	
	public ArrayList<Item> getItems() {
		return this.items;
	}
	
	public double getMaxWeight() {
		return this.maxWeight;
	}
	
	public int getKnapSackNbr() {
		return this.knapSackNbr;
	}
	
	public double getCurrentWeight() {
		return currentWeight;
	}
	
	public double getCurrentValue() {
		return currentValue;
	}
	
	
	
}
