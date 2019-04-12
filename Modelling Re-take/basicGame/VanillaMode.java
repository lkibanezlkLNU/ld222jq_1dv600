package basicGame;
import java.util.Scanner;

public class VanillaMode {
	
	
	public static void main(String[] args){
		
		//MENU, should be implemented in the GUI
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the game of Hangman!\n");
		System.out.print("Input comand \"p\" to play or \"q\" to exit:");
		String controler = input.next();
		
		if(controler.equals("q") || controler.equals("Q")) {
			System.out.println("Bubye! :)");
			System.exit(-1);
		}
		
		if(controler.equals("p") || controler.equals("P")) {
		//Getting the word from the library
			//This will later be replaced to a loop for the Campaign method, with an option to exit
		WordLibrary library = new WordLibrary();
		char[] word = library.getWord();
		
		//Giving the word to the processing machine
		Processor proc = new Processor(word);
		
		
		String currentInput;
		
		//The first printout to show the interface
		System.out.println(proc.print());
		
		do{
			System.out.print("Type your next quess or \"quit\" to exit the game -->");
			currentInput = input.next();
			if(oneLetterInput(currentInput))
				proc.guessLetter(currentInput.charAt(0));
			else
				if(currentInput.equals("quit"))
					quit();
				else
					System.out.println("Invalid input!");
		}while(proc.gameOn());
		
		
		
		//Final screen, to be implemented with GUI
		System.out.println(proc.printResult());
		}
		else
			System.out.println("Unknown command, please restart");
		input.close();
	}
	
	private static void quit() {
		Scanner input = new Scanner(System.in);
		System.out.println("Are you sure you want to quit? y/n");
		if(input.next().charAt(0) == 'y')
			System.exit(-1);
		else
			return;
		
		input.close();
	}
	
	private static boolean oneLetterInput(String s) {
		return s.length() == 1;
	}
}
