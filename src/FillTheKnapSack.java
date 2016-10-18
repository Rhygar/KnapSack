import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FillTheKnapSack {
	boolean firstLineInFile = true;
	KnapSack[] knapSacks;
	ArrayList<Item> items = new ArrayList<Item>();
	
	public FillTheKnapSack() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("src/knapsacks_and_items.txt"));
		
		
		String str;
		while((str = br.readLine()) != null) {
			if(firstLineInFile) {
				firstLineInFile = false;
				//read first line which is the knapsacks and their weight
				String[] splitString = str.split(" ");
				knapSacks = new KnapSack[splitString.length];
				for(int j = 0; j < knapSacks.length; j++) {
					knapSacks[j] = new KnapSack(Double.parseDouble(splitString[j]));
				}
				
			} else {
				//read rest of lines which are the items. 1st value then weight
				String[] splitString = str.split(" ");
				//create Item with value and weight
				items.add(new Item(Double.parseDouble(splitString[0]), Double.parseDouble(splitString[1]))); 
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
			System.out.println("Value: " + i.getValue() + ", Weight: " + i.getWeight() + ", Benefit: " + i.getBenefit());
		}
	}
	public static void main(String []args) throws IOException {
		FillTheKnapSack prog = new FillTheKnapSack();
		prog.printSacks();
		prog.printItems();
	}
}
