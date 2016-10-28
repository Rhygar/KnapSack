import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * This class solves the multiple knapsack problem by using greedy algorithm and 
 * neighbourhood search. Though no tabu list is used, so the program will only find 
 * the first visited local optima.
 * 
 * The class reads the file knapsacks_and_items.txt included in this package.
 * 
 * The result is written in the file output.txt in this package. 
 *  
 * @author David Tran & John Tengvall
 * @date 25-10-16
 *
 */
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
		//prints all outputs into a file output.txt. Contains the decision tree
		try {
			System.setOut(new PrintStream(new FileOutputStream("src/output.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		printSacks();
		printItems();
		greedAlg();
		getBestNeighbour();
		System.out.println("\n********** Local optima found, program stopped **********");
	}
	
	public Neighbour getBestNeighbour() {
		Neighbour currentSolution = new Neighbour(knapSacks, allItems, null, null);
		Neighbour bestFoundSolution = new Neighbour(knapSacks, allItems, null, null);
		System.out.println("\n********** Neighbourhood search started **********\n");
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
				}
			}
			System.out.println("Best value among found neighbours: " + bestFoundSolution.getTotalValue());
		} while(bestFoundSolution.getTotalValue() > currentSolution.getTotalValue());

		return currentSolution;
	}
	
	public void findNeighbours(Neighbour currentSolution) {
		neighbours = new ArrayList<Neighbour>();
		ArrayList<KnapSack> knapSacks = currentSolution.getKnapSacks();
		ArrayList<Item> itemsLeft = currentSolution.getItemsLeft();
		//check if possible to fit in any itemLeft in any knapsack
		for(Item i : itemsLeft) {
			for(KnapSack k : knapSacks) {
				if(k.addItem(i) != -1) {
					neighbours.add(new Neighbour(knapSacks, allItems, k, null));
					k.remove(i);
				}
			}
		}
		//for every knapsack
		for(KnapSack k1 : knapSacks) {
			//for every knapsack
			for(KnapSack k2 : knapSacks) {
				itemsLeft = currentSolution.getItemsLeft();
				//if not looking at same knapsack
				if(k1.getKnapSackNbr() != k2.getKnapSackNbr()) {
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
	
	public Item getBestBenefitItem(ArrayList<Item> items) {
		Item highestBenefitItem = items.get(0);
		for(Item i : items) {
			if(i.getBenefit() > highestBenefitItem.getBenefit()) {
				highestBenefitItem = i;
			}
		}
		return highestBenefitItem;
	}
	
	public Item getBestValueItem(ArrayList<Item> items) {
		Item highestValueItem = items.get(0);
		for(Item i : items) {
			if(i.getValue() > highestValueItem.getValue()) {
				highestValueItem = i;
			}
		}
		return highestValueItem;
	}
	
	public Item getHighestWeightItem(ArrayList<Item> items) {
		Item highestWeightItem = items.get(0);
		for(Item i : items) {
			if(i.getWeight() > highestWeightItem.getWeight()) {
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
		int totValue = 0;
		for(KnapSack k : this.knapSacks) {
			totValue += k.getCurrentValue();
		}
		System.out.println("Total value of solution: " + totValue + "\n");
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
		int index = 0, totalCap = 0;
		System.out.println("********** Knapsacks **********");
		for(KnapSack k : knapSacks) {
			System.out.println("Knapsack: " + knapSacks.get(index).getKnapSackNbr() + ", Max capacity weight: " + k.getMaxWeight());
			totalCap += knapSacks.get(index).getMaxWeight();
			
		}
		System.out.println("\nTotal capacity of knapsacks: " + totalCap + "\n");
	} 
	
	public void printItems() {
		System.out.println("********** Items **********");
		int totalWeight = 0;
		for(Item i : items) {
			System.out.println("ItemNumber: " + i.getItemNbr() +  ", Value: " + i.getValue() + ", Weight: " + i.getWeight() + ", Benefit: " + i.getBenefit());
			totalWeight += i.getWeight();
		}
		System.out.println("\nTotal weight of items: " + totalWeight + "\n");
	}
	
	public static void main(String []args) throws IOException {
		FillTheKnapSack prog = new FillTheKnapSack();
		prog.run();
	}
}
