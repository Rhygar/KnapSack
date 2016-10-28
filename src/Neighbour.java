import java.util.ArrayList;


public class Neighbour {
	
	private double[][] knapSackMatrix;
	private double totalWeight = 0;
	private double totalValue = 0;
	private ArrayList<Item> itemsLeft;
	private ArrayList<KnapSack> knapSacks = new ArrayList<KnapSack>();
	
	public Neighbour() {
		
	}
	public Neighbour(ArrayList<KnapSack> ksList, ArrayList<Item> allItems, KnapSack k1, KnapSack k2) {
		KnapSack currentKnapSack;
		this.itemsLeft = (ArrayList<Item>) allItems.clone();
		for(int k = 0; k < ksList.size(); k++) {
			currentKnapSack = ksList.get(k);
			if(k1 != null && currentKnapSack.getKnapSackNbr() == k1.getKnapSackNbr()) {
				currentKnapSack = k1;
			} else if(k2 != null && currentKnapSack.getKnapSackNbr() == k2.getKnapSackNbr()) {
				currentKnapSack = k2;
			}
			knapSacks.add(currentKnapSack.copy(currentKnapSack));
			totalValue  += currentKnapSack.getCurrentValue();
			totalWeight += currentKnapSack.getCurrentWeight();
			removeFromItemList(currentKnapSack);
		}
	}
	
	private void removeFromItemList(KnapSack k) {
		for(int i = 0; i < k.getItems().size(); i++) {
			for(int j = 0; j < this.itemsLeft.size(); j++) {
				if(k.getItems().get(i).getItemNbr() == itemsLeft.get(j).getItemNbr()) {
					itemsLeft.remove(j);
				}
			}
		}
	}
	public double getTotalValue() {
		return this.totalValue;
	}
	
	public double getTotalWeight() {
		return this.totalWeight;
	}
	
	public ArrayList<KnapSack> getKnapSacks() {
		return this.knapSacks;
	}

	public ArrayList<Item> getItemsLeft() {
		return (ArrayList<Item>) itemsLeft.clone();
	}
	
	public String printItemsLeft() {
		String str = "Items (" + itemsLeft.size() + ") not fit in any knapsack: ";
		for(int i = 0; i < itemsLeft.size(); i++) {
			str += itemsLeft.get(i).getItemNbr() + " ";
		}
		return str;
	}
	
	public String toString() {
		String str = "Total value of solution: " + this.totalValue + "\n\n";
		for(KnapSack k : this.knapSacks) {
			str += "Knapsack: " + k.getKnapSackNbr() + ", Value: " + k.getCurrentValue() + ", Weight: " + k.getCurrentWeight() + "\n";
			str += "Items included: \n";
			for(Item i : k.getItems()) {
				str += "Item: " + i.getItemNbr() + ", value: " + i.getValue() + ", weight: " + i.getWeight() + "\n";
			} str += "\n";
		}
		str += printItemsLeft();
		return str;
	}
	
	

}
