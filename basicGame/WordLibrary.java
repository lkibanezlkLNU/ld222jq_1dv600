package basicGame;

import java.util.Random;

public class WordLibrary {
	/*
	 * This will later store all the categories, from which the user will be able to pick
	 * and then give a random word from that category
	 * 
	 * Also keep track of the words guessed
	 */
	//private ArrayList<String> categories = new ArrayList<String>();
	private String[] words = {"test", "human", "hangman"};
	
	public WordLibrary() {

	}
	
	public char[] getWord() {
		Random ran = new Random();
		int randomNumber = ran.nextInt(words.length);
		return words[randomNumber].toCharArray();
	}
	
	
}
