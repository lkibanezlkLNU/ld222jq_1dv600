package basicGame;

public class User {
	/*
	 * This class will generate Users, and should store them in a database
	 * 
	 * TO BE IMPLEMENTED
	 */
	private String username;
	private String password;
	
	public User(String name, String pass) {
		setUsername(name);
		setPassword(pass);
	}
	
	//Name setter, should be improved
	public void setUsername(String name) {
		if(!name.isEmpty() && name.length() < 20)
			username = name;
	}
	
	//Password setter, should be improved
	public void setPassword(String pass) {
		if(!pass.isEmpty())
			password = pass;
	}
	
	public String getUsername() {
		return username;
	}
}
