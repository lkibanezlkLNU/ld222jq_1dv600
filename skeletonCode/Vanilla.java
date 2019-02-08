package skeleton;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * This class showcases the concept behind the vanilla version of Hangman.
 * <br>
 * The word to be guessed is given as a string for now, but will be later changed when
 * the other classes and methods will be implemented.
 * <br>
 * It doesn't contain any visual representation of the hanged man, it just stores the
 * "lives" left in a variable <code>wrongGuesses</code>
 * 
 * @author Lucian Dragos
 * @since 08-02-2019
 * @version 1.0
 */

public class Vanilla {
	
	//This is the word that will be provided by different classes/methods depending on the gamemode
	private static String s = "tttt";
	
	//The word converted into a char array
	private static char[] word = s.toCharArray();
	
	//Auxiliary array that keeps track of the letters guessed so far
	private static boolean[] guessed = new boolean[word.length];
	
	//ArrayList that stores the guessed letters to avoid guessing the same letter multiple times
	private static ArrayList<Character> letters = new ArrayList<Character>();

	public static void main(String[] args) {
		//Initializing the scanner to read a char guess
		Scanner input = new Scanner(System.in);
		
		//Counter for the remaining characters to be guessed
		int remainingLetters = word.length;
		
		//Counter for the guesses
		int wrongGuesses = 0;
		
		//The character read from the console, the one that we work with
		char currentGuess;
		
		//Boolean that remembers if the letter was present in the word or not
		boolean isInWord;
		
		while(remainingLetters > 0 && wrongGuesses < 6) {
			System.out.println(print() + "\n");
			System.out.println((6-wrongGuesses) + " \"lives\" left");
			System.out.println(remainingLetters + " characters to guess: ");
			
			System.out.print("Your guess - > ");
			currentGuess = input.next().charAt(0);
			isInWord = false;
			
			//If the letter wasn't guessed before
			if(letters.contains(currentGuess) == false) {
				//Mark where the letters equal to currentGuess are in the word
				for(int i = 0; i < word.length;i++)
					if(word[i] == currentGuess) {
						remainingLetters--;
						guessed[i] = true;
						isInWord = true;
					}
				
				//Add the letter guessed
				letters.add(currentGuess);
				
				//If the letter guessed wasn't in the word, increase wrong guesses
				if(!isInWord)
					wrongGuesses++;
			}
			//If the letter was guessed before, increase wrong guesses
			else {
				System.out.println("You've already guessed the letter " + currentGuess);
				wrongGuesses++;
			}
			
			//Line to show new "turn"
			System.out.println("____________________________________\n");
		}
		//If game ended with less than 6 wrong guesses, the word was guessed and game is won
		if(wrongGuesses != 6)
			System.out.println("Congratulations! You guessed the word \"" + s + "\" with " + wrongGuesses + " mistakes");
		
		//Else, the game is lost
		else
			System.out.println("Game over, the word was: " + s);
		input.close();
		
	}
	//Method that uses the boolean array to print the letters guessed and print dashes for the ones that were not yet guessed
	public static String print() {
		String s = "";
		for(int i = 0; i < guessed.length; i++)
			if(guessed[i] == false)
				System.out.print("_ ");
			else
				System.out.print(word[i] + " ");
		return s;
	}

}