import java.util.HashMap;


public class KnapSack {

//	private Item[] items;
	private double maxWeight;
	private HashMap<String, Item> items;
	
	public KnapSack(double maxWeight) {
		items = new HashMap<>();
		this.maxWeight = maxWeight;
	}
	
	public double addItem(String key, Item item) {
		if(getCurrentWeight() + item.getWeight() < maxWeight) {
			items.put(key, item);
			return getCurrentWeight();
		}
		return -1;
	}
	
//	public Item[] getItems() {
//		return this.items;
//	}
	
	public double getMaxWeight() {
		return this.maxWeight;
	}
	
	public double getCurrentWeight() {
		double totalWeight = 0;
//		if(!items.isEmpty()) {
			for(String s : items.keySet()) {
				totalWeight += items.get(s).getWeight();
			}
//		}
		return totalWeight;
	}
	
	public double getCurrentValue() {
		double totalValue = 0;
		for(String s : items.keySet()) {
			totalValue += items.get(s).getValue();
		}
		return totalValue;
	}
	
	
	
}
