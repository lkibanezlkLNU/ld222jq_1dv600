package basicGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBase {
	
	File f;
	ArrayList<String> usernameList = new ArrayList<String>();
	ArrayList<String> passList = new ArrayList<String>();
	ArrayList<Integer> highScore = new ArrayList<Integer>();
	
	public DataBase(String path) {
		f = new File(path);
		updateData();
	}
	
	
	//Updating the database(from file to program)
	private void updateData(){
		try {
			Scanner in = new Scanner(f);
			usernameList = new ArrayList<String>();
			passList = new ArrayList<String>();
			highScore = new ArrayList<Integer>();
			while(in.hasNext()) {
				usernameList.add(in.next());
				passList.add(in.next());
				highScore.add(in.nextInt());
			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Registering a new user in the database
	public void register(String name, String pass) {
		try {
			PrintWriter pw = new PrintWriter(f);
			for(int i = 0; i < usernameList.size(); i ++)
				pw.print(usernameList.get(i) + " " + passList.get(i) +" "+ highScore.get(i) + "\r\n");
			pw.print(name + " " + pass + " " +0+ "\r\n");
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		updateData();
	}
	
	public void updateScore() {
		try {
			PrintWriter pw2 = new PrintWriter(f);
			for(int i = 0; i < usernameList.size(); i ++)
				pw2.print(usernameList.get(i) + " " + passList.get(i) +" "+ highScore.get(i) + "\r\n");
			pw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isRegistered(String username) {
		return usernameList.contains(username);
	}
	
	//To check if is registered before using this method
	public boolean correctPassword(String username, String password) {
		return passList.get(usernameList.indexOf(username)).equals(password);
	}
	
	public int numberOfAccounts() {
		return usernameList.size();
	}
	
	// ---- TO BE USED AFTER LOG-IN -----
	
	
	//Not yet implemented
	/*public void changePassword(String oldPassword, String newPassword) {
		passList.add(passList.indexOf(oldPassword), newPassword);
	}*/
	
}
