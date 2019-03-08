package basicGame;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class WordLibrary {
	/*
	 * 
	 * This will eventually keep track of the words guessed
	 */
	ArrayList<Category> categories = new ArrayList<Category>();
	
	public WordLibrary(String path) {
		File categoryFolder = new File(path);
		File[] fileNames = categoryFolder.listFiles();
		if(fileNames.length > 0)
			for(File file : fileNames)
				categories.add(new Category(file.getAbsolutePath()));
		else
			throw new RuntimeException("There are no categories for this path");
	}
	
	
	public char[] getWord(Category categ) {
		ArrayList<String> words = categ.getWords();
		Random ran = new Random();
		int randomNumber = ran.nextInt(words.size());
		return words.get(randomNumber).toCharArray();
	}
	
	
	
	
}
