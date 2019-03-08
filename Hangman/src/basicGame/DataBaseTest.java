package basicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class DataBaseTest {
	File f = new File("src\\testData\\dbTest.txt");
	
	@Test
	void ShouldUpdateDataBaseAfterMultipleRegisters() throws FileNotFoundException {
		
		//Preparing the database by clearing the file
		clearFile(f);
		DataBase db = new DataBase("src\\testData\\dbTest.txt");
		
		//Registering two users
		db.register("mark", "rainbow");
		db.register("john", "border");
		
		//This is how they should be stored
		String expectedFileContent = "mark rainbow\r\njohn border\r\n";
		
		//Importing the file to be analyzed
		File f = new File("src\\testData\\dbTest.txt");
		Scanner input = new Scanner(f);
		
		String actualFileContent = "";
		
		//Reading the names from file for ease of comparing
		while(input.hasNext())
			actualFileContent += input.next() + " " + input.next() + "\r\n";
		
		//The expected string should match the string generated from the file
		assertTrue(actualFileContent.equals(expectedFileContent));	
		input.close();
	}

	@Test
	void shouldTakeDataFromFile() {
		
		//Since the updateData method is private, we test the constructor that calls this method
		//This file contains one user, (parker princess)
		DataBase db = new DataBase("src\\testData\\dbTest2.txt");
		
		ArrayList<String> expectedUsername = new ArrayList<String>();
			expectedUsername.add("parker");
		ArrayList<String> expectedPassword = new ArrayList<String>();
			expectedPassword.add("princess");
		
		//We test whether the method gets the information from the file correctly
		assertTrue(Arrays.equals(db.usernameList.toArray(), expectedUsername.toArray()));
		assertTrue(Arrays.equals(db.passList.toArray(), expectedPassword.toArray()));
	}
	
	@Test
	void shouldReturnTrueIfUserIsRegistered() {
		//This file contains one user, (parker princess)
		DataBase db = new DataBase("src\\testData\\dbTest2.txt");
		
		//We check whether the system correctly recognize whether a user is registered or not
		assertTrue(db.isRegistered("parker"));
		assertTrue(!db.isRegistered("princess"));
	}
	
	@Test
	void shouldReturnTrueOnCorrectCredentialInput() {
		//This file contains one user, (parker princess)
		DataBase db = new DataBase("src\\testData\\dbTest2.txt");
		
		assertTrue(db.correctPassword("parker", "princess"));
		assertTrue(!db.correctPassword("parker", "prince"));
		//This method shouldn't work on non registered users since the isRegistered method is always called before
	}
	
	//Help method for clearing the file before doing a test
	static void clearFile(File f) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
