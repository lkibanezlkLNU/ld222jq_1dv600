package basicGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Category {
	
	String categoryName;
	ArrayList<String> words = new ArrayList<String>();
	
	/*
	 * In campaign mode, after a word is guessed it is removed from the list
	 * the progress is initial size-current size / initial size * 100;
	 * in Hardcore mode, if a word is not guessed, it adds to the wrongGuesses parameter 
	 * that keeps track of the mistakes
	 */
	
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
				if(!input.hasNext())
					throw new RuntimeException("Category doesn't contain words");
				else
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
}
