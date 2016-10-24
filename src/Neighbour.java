import java.util.ArrayList;


public class Neighbour {
	
	private double[][] knapSackMatrix;
	private double totalWeight = 0;
	private double totalValue = 0;
	private int nbrOfItems;
	private ArrayList<KnapSack> knapSacks = new ArrayList<KnapSack>();
	
	public Neighbour() {
		
	}
	public Neighbour(ArrayList<KnapSack> ksList, ArrayList<Item> items, KnapSack k1, KnapSack k2) {
		nbrOfItems = items.size();
		knapSackMatrix = new double[nbrOfItems][ksList.size()];
		KnapSack currentKnapSack;
		Item currentItem;
		for(int k = 0; k < ksList.size(); k++) {
			currentKnapSack = ksList.get(k);
			if(k1 != null && currentKnapSack.getKnapSackNbr() == k1.getKnapSackNbr()) {
				currentKnapSack = k1;
			} else if(k2 != null && currentKnapSack.getKnapSackNbr() == k2.getKnapSackNbr()) {
				currentKnapSack = k2;
			}
			knapSacks.add(currentKnapSack);
			totalValue  += currentKnapSack.getCurrentValue();
			totalWeight += currentKnapSack.getCurrentWeight();
//			for(int i = 0; i < currentKnapSack.getItems().size(); i++) {
//				currentItem = currentKnapSack.getItems().get(i);
////				knapSackMatrix[currentItem.getItemNbr()][currentKnapSack.getKnapSackNbr()] = 1;
//				totalWeight += currentItem.getWeight();
//				totalValue  += currentItem.getValue();
//			}
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

	
	

}
