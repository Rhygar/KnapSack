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
	Item itemAddedinNewBestNeighbour;

	public FillTheKnapSack() throws IOException {
		readKnapSacksAndItemsFromFile();
		
	}
	
	public void neighbourhoodSearchAlg() {
		
	}
	
	public Neighbour getBestNeighbour() {
		//determine current total value
		double bestTotalValue = new Neighbour(knapSacks, items, null, null).getTotalValue();
		System.out.println("Total value is...:" + bestTotalValue);
		double highestValueInNeighbours = bestTotalValue + 1;
		while(highestValueInNeighbours >= bestTotalValue) {
			//determine neighbourhood
			findNeighbours();
			System.out.println("Number of found neighbours: " + neighbours.size());
			//iterate through all points in neighbourhood and take the N(x) which is highest
			highestValueInNeighbours = 0;
			for(Neighbour N : this.neighbours) {
				if(N.getTotalValue() > highestValueInNeighbours) {
					highestValueInNeighbours = N.getTotalValue();
					printKnapSacks(N.getKnapSacks());
					
				}
			}
			//if N(x) > current total value, update current to best neighbour, continue looping
			if(highestValueInNeighbours > bestTotalValue) {
				bestTotalValue = highestValueInNeighbours;
			}
		}
		return null;
	}
	
	public Neighbour findNeighbours() {
		neighbours = new ArrayList<Neighbour>();
		
		//for every knapsack
		for(KnapSack k1 : knapSacks) {
			//for every knapsack
			for(KnapSack k2 : knapSacks) {
				//if not looking at same knapsack
				if(k1.getKnapSackNbr() != k2.getKnapSackNbr()) {
					KnapSack tempK1 = cloneKnapSack(k1);
					KnapSack tempK2 = cloneKnapSack(k2);
					//for every item in k1
					for(int i = 0; i < tempK1.getItems().size(); i++) {
						Item itemToMove = tempK1.getItems().get(i);
						//try to add in k2
						if(tempK2.addItem(itemToMove) != -1) {
							//succeed! remove from k1
							tempK1.remove(itemToMove);
							//for all items not in a knapsack
							for(Item IL : itemsLeft) {
								//try to add in k1
								if(tempK1.addItem(IL) != -1) {
									//succeed! neighbour found
									ArrayList<KnapSack> cloneKS = (ArrayList<KnapSack>) knapSacks.clone();
									neighbours.add(new Neighbour(knapSacks, items, tempK1, tempK2));
									tempK1.remove(IL);
								}
							}
						} else { //could not add item in k2
							//for all items in k2 
							for(int j = 0; j < tempK2.getItems().size();j++) {
								//remove item
								Item itemToRemove = tempK2.getItems().get(j);
								tempK2.remove(itemToRemove);
								//try again to add in k2
								if(tempK2.addItem(itemToMove) != -1) {
									//succeed! remove from k1
									tempK1.remove(itemToMove);
									//for all items not in a knapsack
									for(Item IL : itemsLeft) {
										//try to add in k1
										if(tempK1.addItem(IL) != -1) {
											//succeed! neighbour found
//											neighbours.add(new Neighbour(knapSacks, items, tempK1, tempK2));
											ArrayList<KnapSack> cloneKS = (ArrayList<KnapSack>) knapSacks.clone();
											neighbours.add(new Neighbour(knapSacks, items, tempK1, tempK2));
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
		printItemsLeft();
	}
	
	public KnapSack cloneKnapSack(KnapSack ks) {
		KnapSack clone = new KnapSack(ks.getMaxWeight(),ks.getKnapSackNbr());
		for(int i = 0; i < ks.getItems().size(); i++) {
			clone.addItem(ks.getItems().get(i));
		}
		return clone;
	}
	
	public ArrayList<KnapSack> cloneKnapSackList(ArrayList<KnapSack> knapSacks) {
		ArrayList<KnapSack> clonedList = new ArrayList<KnapSack>(knapSacks.size());
		for(KnapSack k : knapSacks) {
			clonedList.add(new KnapSack(k));
		}
		return clonedList;
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
	
	public void printItemsLeft() {
		for(Item i : itemsLeft) {
			System.out.println("ItemNumber: " + i.getItemNbr());
		}
	}
	
	public void printKnapSacks() {
		for(KnapSack k : this.knapSacks) {
			System.out.println("Knapsack: " + k.getKnapSackNbr() + ", Value: " + k.getCurrentValue() + ", Weight: " + k.getCurrentWeight());
			System.out.println("Items included: ");
			for(Item i : k.getItems()) {
				System.out.println("Item: " + i.getItemNbr() + ", value: " + i.getValue() + ", weight: " + i.getWeight());
			}
			System.out.println();
		}
	}
	
	public void printKnapSacks(ArrayList<KnapSack> ks) {
		for(KnapSack k : ks) {
			System.out.println("Knapsack: " + k.getKnapSackNbr() + ", Value: " + k.getCurrentValue() + ", Weight: " + k.getCurrentWeight());
			System.out.println("Items included: ");
			for(Item i : k.getItems()) {
				System.out.println("Item: " + i.getItemNbr() + ", value: " + i.getValue() + ", weight: " + i.getWeight());
			}
			System.out.println();
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
