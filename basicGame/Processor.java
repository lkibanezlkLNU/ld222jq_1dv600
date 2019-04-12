package basicGame;

import java.util.ArrayList;

public class Processor {
	
	private char[] word; //The word to be played with.
	private boolean[] guessed; //Auxiliary array that keeps track of the letters guessed so far
	private ArrayList<Character> letters = new ArrayList<Character>(); //ArrayList that stores the guessed letters to avoid guessing the same letter multiple times
	private int remainingLetters; //Counter for the remaining characters to be guessed
	private int wrongGuesses = 0; //Counter for the guesses
	private boolean isInWord; //Boolean that remembers if the letter was present in the word or not
	
	//Constructor
	public Processor(char[] word) {
		this.word = word;
		remainingLetters = word.length;
		guessed = new boolean[word.length];
	}
	
	//This method will be converted into JavaFX visual output
	public String print() {
		String s = "";
		for(int i = 0; i < guessed.length; i++)
			if(guessed[i] == false)
				s+= "_ ";
			else
				s+= word[i] + " ";
		s+="\n" + (6-wrongGuesses) + " \"lives\" left";
		s+="\n" + remainingLetters + " characters to guess";
		return s;
	}
	
	public boolean gameOn() {
		return (remainingLetters > 0) && (wrongGuesses < 6);
	}
	
	public void guessLetter(char currentGuess) {		
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
						
			//If the letter guessed wasn't in the word, increase wrong guesses
			if(!isInWord)
				wrongGuesses++;
			
			//Add the letter guessed
			letters.add(currentGuess);
		}
		//If the letter was guessed before, increase wrong guesses
		else 
			System.out.println("You've already guessed the letter " + currentGuess);
		System.out.println(print() + "\n");
	}
	
	//Same thing, this should generate pop-ups with JavaFX
	public String printResult() {
		//If game ended with less than 6 wrong guesses, the word was guessed and game is won
		if(wrongGuesses != 6)
			return "Congratulations! You guessed the word \"" + this.toString() + "\" with " + wrongGuesses + " mistakes";
					
		//Else, the game is lost
		else
			return "Game over, the word was: " + this.toString();
	}
	
	//Helping/cheating method
	public String toString() {
		String s = "";
		for(int i = 0; i < word.length; i++)
			s+=word[i];
		return s;
	}
}