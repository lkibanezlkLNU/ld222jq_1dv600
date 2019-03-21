package basicGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class WordLibrary {
	static ArrayList<Category> categories = new ArrayList<Category>();
	
	static String currentWord;
	static Category currentCategory;
	private static String currentPath;
	
	public WordLibrary(String path) {
		categories = new ArrayList<Category>();
		currentPath = path;
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
		
		currentCategory = categ;
		currentWord = words.get(randomNumber);
		
		return currentWord.toCharArray();
	}
	
	public static void guessedWord() {
		currentCategory.words.remove(currentWord);
	}
	
	public void updateCategory() {
		File[] fileNames = new File(currentPath).listFiles();
		int i = 0;
		for(File file : fileNames) 
		try {
			PrintWriter pw = new PrintWriter(file);
			pw.println(categories.get(i).categoryName);
			pw.println(categories.get(i).initialSize);
			for(int j = 0; j < categories.get(i).words.size(); j++) {
				pw.print(categories.get(i).words.get(j));
				if(j < categories.get(i).words.size()-1)
					pw.println();
			}
			pw.close();
			i++;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
