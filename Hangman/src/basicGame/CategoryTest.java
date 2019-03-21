package basicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class CategoryTest {

	Scanner input;
	Category testCategory;	
	
	@Test
	void shouldCreateCategoryFromFile() {
		//Mock category file containing the title "Test" and the word "test"
		File f = new File("src\\testData\\goodCategory.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		String expectedName = "Test";
		int expectedLength = 1;
		String[] expectedArray = {"test"};
		
		assertTrue(testCategory.categoryName.equals(expectedName));
		assertTrue(testCategory.initialSize == expectedLength);
		assertArrayEquals(testCategory.words.toArray(),expectedArray);
	}
	
	@Test
	void shouldThrowErrorForEmptyFile(){
		//Empty Text file
		File f = new File("src\\testData\\emptyFile.txt");
		
		//Check if error is thrown
		RuntimeException thrown = assertThrows(RuntimeException.class,
				() -> testCategory = new Category(f.getAbsolutePath()),"no error thrown");

		//Check if the error thrown is the one intended
		assertTrue(thrown.getMessage().contains("File is empty"));
	}
		
	@Test
	void shouldAcessName() {
		//Mock text file containing the title "Animals"
		File f = new File("src\\testData\\goodCategory2.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		String expectedName = "Animals";
		
		assertTrue(testCategory.getName().equals(expectedName));
	}
	
	@Test
	void shouldAccessWords() {

		File f = new File("src\\testData\\goodCategory3.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		String[] expectedArray = {"red", "blue", "yellow"};
		
		assertArrayEquals(testCategory.getWords().toArray(),expectedArray);
	}	
	
	@Test
	void shouldReturnNumberOfWords() {
		File f = new File("src\\testData\\goodCategory4.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		
		assertEquals(testCategory.getSize(),6);
	}
	
	@Test
	void shouldReturnFalseIfNoWords() {
		File f = new File("src\\testData\\nameOnlyCategory.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		assertFalse(testCategory.hasWords());
	}
	
	@Test 
	void shouldReturnFalseIfCategoryHasWords() {
		File f = new File("src\\testData\\goodCategory4.txt");
		testCategory = new Category(f.getAbsolutePath());
		
		assertTrue(testCategory.hasWords());
		
	}

}
