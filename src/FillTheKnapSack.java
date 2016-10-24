import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FillTheKnapSack {
	boolean firstLineInFile = true;
	ArrayList<KnapSack> knapSacks = new ArrayList<KnapSack>();
	ArrayList<Item> items = new ArrayList<Item>();
	
	public FillTheKnapSack() throws IOException {

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
			} else {
				System.out.println("Item: " + bestItem.getItemNbr() + " with value: " + bestItem.getValue() + " was added to knapsack: " + knapSacks.get(index-1).getKnapSackNbr());
			}
			items.remove(bestItem);
		}
		System.out.println("\nFinished with Greedy Algorithm!\n");
		printKnapSacks();
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
	
	public void printKnapSacks() {
		for(KnapSack k : this.knapSacks) {
			System.out.println("Knapsack: " + k.getKnapSackNbr() + ", Weight: " + k.getCurrentWeight() + ", Value: " + k.getCurrentValue());
		}
	}
	
	public static void main(String []args) throws IOException {
		FillTheKnapSack prog = new FillTheKnapSack();
		prog.printSacks();
		prog.printItems();
		prog.greedAlg();
	}
}
