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
	
	public DataBase(String path) {
		f = new File(path);
		updateData();
	}
	
	
	//Updating the database(from file to program)
	private void updateData(){
		try {
			Scanner in = new Scanner(f);
			while(in.hasNext()) {
				usernameList.add(in.next());
				passList.add(in.next());
			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	//--REGISTERING A NEW USER-- Buggy method
	
	//This should also print the previous users as the print writer overwrites the file
	public void register(String name, String pass) {
		try {
			PrintWriter pw = new PrintWriter(f);
			// --- BUG FIX ---
			//for(int i = 0; i < usernameList.size(); i ++)
				//pw.print(usernameList.get(i) + " " + passList.get(i) + "\r\n");
			pw.print(name + " " + pass + "\r\n");
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		updateData();
	}
	
	
	public boolean isRegistered(String username) {
		return usernameList.contains(username);
	}
	
	//To check if is registered before using this method
	public boolean correctPassword(String username, String password) {
		return passList.get(usernameList.indexOf(username)).equals(password);
	}
	
	// ---- TO BE USED AFTER LOG-IN -----
	
	
	//Not yet implemented
	/*public void changePassword(String oldPassword, String newPassword) {
		passList.add(passList.indexOf(oldPassword), newPassword);
	}*/
	
}
