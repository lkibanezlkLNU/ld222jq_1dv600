package basicGame;


public class Processor {
	
	char[] word; //The word to be played with.
	boolean[] guessed; //Auxiliary array that keeps track of the letters guessed so far
	int remainingLetters; //Counter for the remaining characters to be guessed
	int wrongGuesses = 0; //Counter for the guesses
	boolean isInWord; //Boolean that remembers if the letter was present in the word or not
	
	//Constructor
	public Processor(char[] word) {
		this.word = word;
		remainingLetters = word.length;
		guessed = new boolean[word.length];
	}
	/* This method should be used instead of the constructor to save memory
	public void giveWord(char[] word) {
		this.word = word;
		remainingLetters = word.length;
		guessed = new boolean[word.length];
		
	}*/
	
	//This method is passed to the GUI
	public String print() {
		String s = "";
		for(int i = 0; i < guessed.length; i++)
			if(guessed[i] == false)
				s+= "_ ";
			else
				s+= word[i] + " ";
		return s;
	}
	
	public String printLives() {
		return (6-wrongGuesses) + " lives left";
	}
	
	public boolean gameOn() {
		return (remainingLetters > 0) && (wrongGuesses < 6);
	}
	
	public void guessLetter(char currentGuess) {		
		isInWord = false;		
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

	}
	
	//Method passed to endScreen in GUI
	public String printResult() {
		//If game ended with less than 6 wrong guesses, the word was guessed and game is won
		if(wrongGuesses != 6)
			return "Congratulations!\nYou guessed the word \"" + this.toString() + "\"";
					
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