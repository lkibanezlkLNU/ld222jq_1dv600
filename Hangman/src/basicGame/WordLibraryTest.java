package basicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class WordLibraryTest {
	
	WordLibrary library;

	@Test
	void shouldPopulateCategories() {
		//Create a library's Category List using the constructor
		library = new WordLibrary("src\\testData\\testCategoryFolder");
		
		//Create a library's Category List manually 
		ArrayList<Category> expectedCategoryArray = new ArrayList<Category>();
		expectedCategoryArray.add(new Category("src\\testData\\testCategoryFolder\\categ1.txt"));
		expectedCategoryArray.add(new Category("src\\testData\\testCategoryFolder\\categ2.txt"));
		
		//Since the (to be compared) arrayLists are of type Category, all the fields need to be checked
		for(int i = 0; i < expectedCategoryArray.size(); i++) {
			assertTrue(WordLibrary.categories.get(i).categoryName.equals(expectedCategoryArray.get(i).categoryName));
			assertTrue(WordLibrary.categories.get(i).words.equals(expectedCategoryArray.get(i).words));
		}
	}
	
	@Test
	void shouldThrowErrorWhenFolderIsEmpty() {		
		RuntimeException thrown = assertThrows(RuntimeException.class,() -> library = new WordLibrary("src\\testData\\emptyTestCategoryFolder"),"Expected constructor to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("There are no categories for this path"));
	}
	
	@Test
	void shouldReturnWordFromCategory() {
		
		library = new WordLibrary("src\\testData\\testCategoryFolder");
		
		char[] expectedWord = "one".toCharArray();
		char[] wordGiven = library.getWord(WordLibrary.categories.get(0));

		assertTrue(Arrays.equals(expectedWord, wordGiven));
			
	}
	
	@Test
	void shouldRemoveWordFromCategory() {
		library = new WordLibrary("src\\testData\\testCategoryFolder2");
		
		WordLibrary.currentCategory = WordLibrary.categories.get(0);
		WordLibrary.currentWord = "two";
		
		WordLibrary.guessedWord();
		
		assertTrue(WordLibrary.categories.get(0).words.toString().equals("[one, three]"));
		
		library.updateCategory();
		
		Scanner input;
		try {
			input = new Scanner(new File("src\\testData\\testCategoryFolder2\\test.txt"));
			input.nextLine();input.nextLine();
			assertTrue(input.next().equals("one"));
			assertTrue(input.next().equals("three"));
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
