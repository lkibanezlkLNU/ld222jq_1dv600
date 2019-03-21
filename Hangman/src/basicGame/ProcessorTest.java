package basicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ProcessorTest {

	@Test
	void shouldUpdateParametersOnConstructorCall() {
		
		Processor test1 = new Processor("test".toCharArray());
		
		char[] expectedWord = {'t','e','s','t'};
		boolean[] expectedGuessed = new boolean[4];
		
		assertTrue(Arrays.equals(test1.word,expectedWord));
		assertTrue(test1.remainingLetters == 4);
		assertTrue(Arrays.equals(test1.guessed, expectedGuessed));
	}
	
	@Test
	void shouldReturnCurrentPrintString() {
		Processor test2 = new Processor("rainbow".toCharArray());
		
		String initialExpectedPrint = "_ _ _ _ _ _ _ ";
		assertEquals(test2.print(),initialExpectedPrint);
		
		//Manually forcing some guesses
		test2.guessed[0] = true;
		test2.guessed[2] = true;
		test2.guessed[4] = true;
		
		String afterGuessExpectedPrint = "R _ I _ B _ _ ";
		assertEquals(test2.print(),afterGuessExpectedPrint);
	}
	
	@Test
	void shouldReturnLivesLeft() {
		Processor test3 = new Processor("association".toCharArray());
		
		String expectedInitialLives = "6 lives left";
		assertEquals(test3.printLives(), expectedInitialLives);
		
		//Manually decreasing the number of lives
		test3.wrongGuesses += 4;
		
		String expectedLives = "2 lives left";
		assertEquals(test3.printLives(), expectedLives);
	}
	
	@Test
	void shouldReturnTrueIfGameIsOn() {
		Processor test4 = new Processor("paradox".toCharArray());
		
		assertTrue(test4.gameOn());
		
		//Case when no lives left
		test4.wrongGuesses = 6; //Fake kill player
		assertTrue(!test4.gameOn());
		
		//Case when word is guessed
		test4 = new Processor("paradox".toCharArray());
		
		test4.remainingLetters = 0; //Fake guess all letters
		assertTrue(!test4.gameOn());
	}
	
	@Test
	void shouldGiveEndScreenMessage() {
		//This test will fail until i can somehow ignore the "guessedWord" method call
		
		Processor test5 = new Processor("chicken".toCharArray());
		//We assume that the game ended, there are two outcomes:
		
		String expectedOutcome1 = "Congratulations!\nYou guessed the word \"CHICKEN\"";
		String expectedOutcome2 = "Game over, the word was: \"CHICKEN\"";
		//Outcome one, wrong guesses < 6
		for(int i = 0; i < 6; i ++) {
			test5.wrongGuesses = i;
			assertEquals(test5.printResult(), expectedOutcome1);
		}
		
		//Outcome two, wrong guesses = 6
		test5.wrongGuesses = 6;
		assertEquals(test5.printResult(), expectedOutcome2);
	}
	
	@Test
	void shouldReturnWord() {
		Processor test6 = new Processor("mayonnaise".toCharArray());
		assertEquals(test6.toString(),"MAYONNAISE");	
	}
	
	@Test
	void shouldDoABunchOfStuffOnLetterGuessed() {
		Processor test7 = new Processor("green".toCharArray());
		
		//We will test this by guessing 2 letters, one "correct" and one "wrong"
		//After every guess, we compare behavior with what we expect
		
		//Correct guess
		test7.guessLetter('e');
		assertTrue(test7.isInWord);
		assertEquals(test7.remainingLetters, "green".length()-2);
		
		boolean[] expectedGuess = {false, false, true, true, false};
		assertTrue(Arrays.equals(test7.guessed, expectedGuess));
		
		//IncorrectGuess
		test7.guessLetter('x');
		assertTrue(!test7.isInWord);
		assertEquals(test7.remainingLetters, "green".length()-2); //same length
		
		//Same guessed array
		assertTrue(Arrays.equals(test7.guessed, expectedGuess));
		assertEquals(test7.wrongGuesses, 1);
	}
	
	@Test
	void shouldRemoveSpaces() {
		Processor test8 = new Processor("a cat".toCharArray());
		
		test8.checkForSpaces(test8.word);
		
		//Check if spaces are "guessed" in the boolean array
		boolean[] expectedGuess = {false, true, false, false, false};
		assertTrue(Arrays.equals(test8.guessed, expectedGuess));
		
		//Check if spaces are "guessed" in the guess counter
		assertEquals(test8.remainingLetters, 3);
	}

}
