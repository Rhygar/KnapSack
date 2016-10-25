import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FillTheKnapSack {
	boolean firstLineInFile = true;
	ArrayList<KnapSack> knapSacks = new ArrayList<KnapSack>();
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Item> allItems = new ArrayList<Item>();
	ArrayList<Item> itemsLeft = new ArrayList<Item>();
	ArrayList<Neighbour> neighbours;
	Item itemAddedinNewBestNeighbour;

	public FillTheKnapSack() throws IOException {
		readKnapSacksAndItemsFromFile();
		
	}
	
	public void neighbourhoodSearchAlg() {
		
	}
	
	public Neighbour getBestNeighbour() {
		Neighbour currentSolution = new Neighbour(knapSacks, allItems, null, null);
		Neighbour bestFoundSolution = new Neighbour(knapSacks, allItems, null, null);
		System.out.println("\n********** Neigbourhood search started **********\n");
		System.out.println("Current value in sacks: " + currentSolution.getTotalValue());
		//determine current total value
		
		do {
			if(bestFoundSolution.getTotalValue() > currentSolution.getTotalValue()) {
				currentSolution = bestFoundSolution;
				System.out.println("\n********** New solution found **********\n");
				System.out.println(currentSolution.toString());
			}
			findNeighbours(currentSolution);
			System.out.println("Neighbours found: " + neighbours.size());
			double highestValueInNeighbours = 0;
			for(Neighbour N : this.neighbours) {
				if(N.getTotalValue() > highestValueInNeighbours) {
					highestValueInNeighbours = N.getTotalValue();
					bestFoundSolution = N;
//					printKnapSacks(N.getKnapSacks());
//					printItemsLeft();
				}
			}
		} while(bestFoundSolution.getTotalValue() > currentSolution.getTotalValue());

		return currentSolution;
	}
	
	public void findNeighbours(Neighbour currentSolution) {
		neighbours = new ArrayList<Neighbour>();
//		ArrayList<Item> itemsLeft = currentSolution.getItemsLeft();
		ArrayList<KnapSack> knapSacks = currentSolution.getKnapSacks();
		//for every knapsack
		for(KnapSack k1 : knapSacks) {
			//for every knapsack
			for(KnapSack k2 : knapSacks) {
				ArrayList<Item> itemsLeft = currentSolution.getItemsLeft();
				//if not looking at same knapsack
				if(k1.getKnapSackNbr() != k2.getKnapSackNbr()) {
//					System.out.println("K1: " + k1.getKnapSackNbr());
//					System.out.println("K2: " + k2.getKnapSackNbr());
					KnapSack tempK1 = new KnapSack(k1);
					KnapSack tempK2 = new KnapSack(k2);
					//for every item in k1
					for(int i = 0; i < tempK1.getItems().size(); i++) {
						tempK1 = new KnapSack(k1);
						tempK2 = new KnapSack(k2);
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
									neighbours.add(new Neighbour(knapSacks, allItems, tempK1, tempK2));
									tempK1.remove(IL);
								}
							}
						} else { //could not add item in k2
							//for all items in k2 
							for(int j = 0; j < tempK2.getItems().size();j++) {
								//remove item
								tempK2 = new KnapSack(k2);
								tempK1 = new KnapSack(k1);
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
											neighbours.add(new Neighbour(knapSacks, allItems, tempK1, tempK2));
											tempK1.remove(IL);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}	
	
	public void greedAlg() {
		System.out.println("\n********** Greedy algorithm search started **********\n");
		Item bestItem;
		while(!items.isEmpty()) {
			
//			bestItem = getBestBenefitItem(this.items);
//			bestItem = getBestValueItem(this.items);
			bestItem = getHighestWeightItem(this.items);
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
		System.out.println("\n********** Greedy algorithm finished! **********\nRESULT:\n");
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
	
	public Item getBestValueItem(ArrayList<Item> items) {
		double highestValue = 0;
		Item highestValueItem = null;
		for(Item i : items) {
			if(i.getValue() > highestValue) {
				highestValue = i.getValue();
				highestValueItem = i;
			}
		}
		return highestValueItem;
	}
	
	public Item getHighestWeightItem(ArrayList<Item> items) {
		double highestWeight = 0;
		Item highestWeightItem = null;
		for(Item i : items) {
			if(i.getWeight() > highestWeight) {
				highestWeight = i.getWeight();
				highestWeightItem = i;
			}
		}
		return highestWeightItem;
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
				allItems.add(new Item(itemNumber, itemValue, itemWeight));
			}
			
		}
	}
	
	public void printItemsLeft() {
		String str = "Items not fit in any knapsack: ";
		for(Item i : itemsLeft) {
			str += i.getItemNbr() + " ";
		} System.out.println(str);
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
		int index = 0;
		System.out.println("********** Knapsacks **********");
		for(KnapSack k : knapSacks) {
			System.out.println("Knapsack: " + knapSacks.get(index).getKnapSackNbr() + ", Max capacity weight: " + k.getMaxWeight());
			index++;
		}
		System.out.println("");
	} 
	
	public void printItems() {
		System.out.println("********** Items **********");
		for(Item i : items) {
			System.out.println("ItemNumber: " + i.getItemNbr() +  ", Value: " + i.getValue() + ", Weight: " + i.getWeight() + ", Benefit: " + i.getBenefit());
		}
		System.out.println("");
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
