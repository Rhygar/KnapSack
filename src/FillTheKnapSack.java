import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FillTheKnapSack {
	boolean firstLineInFile = true;
	ArrayList<KnapSack> knapSacks = new ArrayList<KnapSack>();
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Item> itemsLeft = new ArrayList<Item>();
	ArrayList<Neighbour> neighbours;
	Neighbour bestNeighbour;

	public FillTheKnapSack() throws IOException {
		readKnapSacksAndItemsFromFile();
		
	}
	
	public void neighbourhoodSearchAlg() {
		
	}
	
	public Neighbour getBestNeighbour() {
		Neighbour bestNeighbour = new Neighbour(knapSacks, items, null, null);
		Neighbour tempNeighbour = new Neighbour(knapSacks, items, null, null);
		
		do {
			if(tempNeighbour.getTotalValue() > bestNeighbour.getTotalValue() ) {
				bestNeighbour = tempNeighbour;
				this.knapSacks = bestNeighbour.getKnapSacks();
			}
			tempNeighbour = findNeighbours(bestNeighbour);
			
		} while(tempNeighbour.getTotalValue() > bestNeighbour.getTotalValue());
		printKnapSacks();
		
		return bestNeighbour;
		
	}
	
	public Neighbour findNeighbours(Neighbour currentState) {
		bestNeighbour = currentState;
		double bestValueNeighbour = bestNeighbour.getTotalValue();
		Neighbour newNeighbour;
		
		for(KnapSack k1 : knapSacks ) {
			for(KnapSack k2 : knapSacks ) {
				if(k1.getKnapSackNbr() != k2.getKnapSackNbr()) {
					KnapSack tempK1 = cloneKnapSack(k1);
					KnapSack tempK2 = cloneKnapSack(k2);
					for(int i = 0; i < tempK1.getItems().size(); i++) {
						Item itemToMove = tempK1.getItems().get(i);
						if(tempK2.addItem(itemToMove) != -1) {
							tempK1.remove(itemToMove);
							for(Item IL : itemsLeft) {
								if(tempK1.addItem(IL) != -1) {
									//add neighbour
									System.out.println("new best found");
//									neighbours.add(new Neighbour(knapSacks, items, tempK1, tempK2));
									newNeighbour = new Neighbour(knapSacks, items, tempK1, tempK2);
									if(newNeighbour.getTotalValue() > bestNeighbour.getTotalValue()) {
										bestNeighbour = newNeighbour;
										System.out.println("new best found");
									} 
									
									tempK1.remove(IL);
								}
							}
						} else {
							ArrayList<Item> tempRemoveItems = tempK2.getItems();
//							System.out.println(tempItems.size());
							for(int j = 0; j < tempK2.getItems().size();j++){
								Item itemToRemove = tempK2.getItems().get(j);
								tempK2.remove(itemToRemove);
								if(tempK2.addItem(itemToMove) != -1) {
									tempK1.remove(itemToMove);
									for(Item IL : itemsLeft) {
										if(tempK1.addItem(IL) != -1) {
											//add neighbour
//											neighbours.add(new Neighbour(knapSacks, items, tempK1, tempK2));
											newNeighbour = new Neighbour(knapSacks, items, tempK1, tempK2);
											if(newNeighbour.getTotalValue() > bestNeighbour.getTotalValue()) {
												bestNeighbour = newNeighbour;
												System.out.println("new best found2");
											}
											
											tempK1.remove(IL);
										}
									}
								}
							}
						}
					}
				}
			}
		} return bestNeighbour;
	}	
	
	public double[][] createNeighbour(KnapSack k1, KnapSack k2) {
		KnapSack currentKS;
		for(int k = 0; k < this.knapSacks.size(); k++) {
			if(knapSacks.get(k).getKnapSackNbr() == k1.getKnapSackNbr()) {
				currentKS = k1;
			} else if(knapSacks.get(k).getKnapSackNbr() == k2.getKnapSackNbr()) {
				currentKS = k2;
			} else {
				currentKS = knapSacks.get(k);
			}
		}
		return null;
		
	}
	
	public void greedAlg() {
		Item bestItem;
		while(!items.isEmpty()) {
			
			bestItem = getBestBenefitItem(this.items);
			double returnValue = -1;
			int index = 0;
			while(returnValue == -1 && index < knapSacks.size()) {
				returnValue = knapSacks.get(index).addItem(bestItem);
				index++;
			}
			if(returnValue == -1) {
				System.out.println("Item: " + bestItem.getItemNbr() + " did not fit in any knapsack!");
				itemsLeft.add(bestItem);
			} else {
				System.out.println("Item: " + bestItem.getItemNbr() + " with value: " + bestItem.getValue() + " was added to knapsack: " + knapSacks.get(index-1).getKnapSackNbr());
			}
			items.remove(bestItem);
		}
		System.out.println("\nFinished with Greedy Algorithm!\n");
		printKnapSacks();
	}
	
	public KnapSack cloneKnapSack(KnapSack ks) {
		KnapSack clone = new KnapSack(ks.getMaxWeight(),ks.getKnapSackNbr());
		for(int i = 0; i < ks.getItems().size(); i++) {
			clone.addItem(ks.getItems().get(i));
		}
		return clone;
	}
	
	public Item getBestBenefitItem(ArrayList<Item> items) {
		double highestBenefit = 0;
		Item highestBenefitItem = null;
		for(Item i : items) {
			if(i.getBenefit() > highestBenefit) {
				highestBenefit = i.getBenefit();
				highestBenefitItem = i;
			}
		}
		return highestBenefitItem;
	}
	public void readKnapSacksAndItemsFromFile() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/knapsacks_and_items.txt"));
		
		String str;
		while((str = br.readLine()) != null) {
			if(firstLineInFile) {
				firstLineInFile = false;
				//read first line which is the knapsacks and their weight
				String[] splitString = str.split(" ");
				for(int j = 0; j < splitString.length; j++) {
					knapSacks.add(new KnapSack(Double.parseDouble(splitString[j]), j));
				}
				
			} else {
				//read rest of lines which are the items.  itemNumber, 
				String[] splitString = str.split(" ");
				int itemNumber = Integer.parseInt(splitString[0]);
				double itemValue = Double.parseDouble(splitString[1]);
				double itemWeight = Double.parseDouble(splitString[2]);
				//create Item with itemNumber, value and weight
				items.add(new Item(itemNumber, itemValue, itemWeight)); 
			}
			
		}
	}
	public void printKnapSacks() {
		for(KnapSack k : this.knapSacks) {
			System.out.println("Knapsack: " + k.getKnapSackNbr() + ", Weight: " + k.getCurrentWeight() + ", Value: " + k.getCurrentValue());
		}
	}
	public void printSacks() {
		for(KnapSack k : knapSacks) {
			System.out.println(k.getMaxWeight());
		}
	}
	public void printItems() {
		for(Item i : items) {
			System.out.println("ItemNumber: " + i.getItemNbr() +  ", Value: " + i.getValue() + ", Weight: " + i.getWeight() + ", Benefit: " + i.getBenefit());
		}
	}
	
	public static void main(String []args) throws IOException {
		FillTheKnapSack prog = new FillTheKnapSack();
		prog.printSacks();
		prog.printItems();
		prog.greedAlg();
		prog.getBestNeighbour();
		System.out.println("finito");
	}
}
