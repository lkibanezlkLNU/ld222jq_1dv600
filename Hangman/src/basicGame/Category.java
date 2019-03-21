package basicGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Category {
	
	String categoryName;
	ArrayList<String> words = new ArrayList<String>();
	int initialSize;
	
	//Constructor that takes a category from a text file.
	public Category(String path) {
		File f = new File(path);
		Scanner input;
		try {
			input = new Scanner(f);
			if(!input.hasNext())
				throw new RuntimeException("File is empty");
			else {
				categoryName = input.nextLine();
				initialSize = Integer.parseInt(input.nextLine());
				while(input.hasNext())
					words.add(input.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	
	public ArrayList<String> getWords(){
		return words;
	}
	
	public String getName() {
		return categoryName;
	}
	
	public int getSize() {
		return words.size();
	}
	
	public boolean hasWords() {
		return words.size()!=0;
	}
}
