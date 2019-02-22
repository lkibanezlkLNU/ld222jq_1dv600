package basicGame;

import java.util.ArrayList;
import java.util.Random;

public class Category {
	
	// NOT YET USED
	private String categoryName;
	private ArrayList<String> words = new ArrayList<String>();
	
	/*
	 * In campaign mode, after a word is guessed it is removed from the list
	 * the progress is initial size-current size / initial size * 100;
	 * in Hardcore mode, if a word is not guessed, it adds to the wrongGuesses parameter that keeps track of the mistakes
	 */
	
	public Category(String name) {
		categoryName = name;
	}
	
	
	
}
