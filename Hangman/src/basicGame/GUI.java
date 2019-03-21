package basicGame;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {
	
	/*
	 * A library in generated for every user in order to keep track of the words already guessed (the progress)
	 * In order to add categories, you can just create a text file and add it to the "categoryFolder", the system does the rest.
	 * 
	 * 
	 * Multiple features could be added in the future to make the game even more complex such as:
	 * - End screen when all the words are guessed
	 * - Store the global high score in the database
	 * - Have a "proper" data base, not a text file
	 * - More login features (remove account, reset password, change password etc)
	 * - In-Game category creator
	 * - Keyboard input
	 * - Improve design using CSS files
	 */	
	
	private Processor proc;
	private WordLibrary library;
	private DataBase db = new DataBase("src\\db.txt");
	private File baseCateg = new File("src\\categoryFolder");
	
	private int currentScore;
	private Category playingCategory;
	
	private Stage window;
	private Text word = new Text("This will contain the letters to be guessed");
	private Text timer = new Text("00:10");
	private Text title = new Text("Hangman Revisited");//Used in different scenes
	private Text currentUser = new Text();
	private TextField username; //The field used to enter a user name (used for knowing which user is playing)
	private Button quit = new Button ("Quit");	
	private Button backToMenu = new Button("Main Menu");
	private Button backToCategories = new Button ("Back to Categories");
	private Button backToMenuLogin = new Button("Back to Menu");
	private CheckBox timedMode = new CheckBox(); //The check box that changed the difficulty of the game
	private Label timedModeLabel = new Label("Timed mode (Lose a life every 10 seconds)\r\nworth 3 points/word but words not guessed don't come back");
	private HBox timed = new HBox(); //Hbox containing the checkbox and label
	private Timeline oneSecondTimer; //Used to update the timer that the user sees
	private Timeline tenSecondTimer; //Used to "lose" a life every 10 seconds
	private Image[] hangmanPics = new Image[8]; //Array that stores the images representing the man to be hanged
	private ImageView hangman = new ImageView();

	@Override
	public void start(Stage stage) throws Exception { 
		window = stage;
		window.setTitle("Hangman");
		window.setResizable(false);
		window.setScene(menu());
		window.show();
		
		quit.setOnAction(x -> {
			Alert quitAlert = new Alert(AlertType.CONFIRMATION);
			quitAlert.setTitle("Exit");
			quitAlert.setHeaderText("Exit confirmation");
			quitAlert.setContentText("Are you sure you want to exit?");
			
			Optional<ButtonType> result = quitAlert.showAndWait();
            if (result.get() == ButtonType.OK) {window.close();}
            if(result.get()==ButtonType.CANCEL) {quitAlert.close();}	
		});
		
		backToMenu.setOnAction(x -> {
			Alert loginAlert = new Alert(AlertType.CONFIRMATION);
			loginAlert.setTitle("Back to Menu");
			loginAlert.setHeaderText("Menu confirmation");
			loginAlert.setContentText("Are you sure you want to go back to the menu?");
			
			Optional<ButtonType> result = loginAlert.showAndWait();
            if (result.get() == ButtonType.OK){
            	if(timedMode.isSelected()) {
            		oneSecondTimer.stop();
            		tenSecondTimer.stop();
            	}
            	library.updateCategory();
                window.setScene(menu());
                
            }
            if(result.get()==ButtonType.CANCEL) {loginAlert.close();}
		});
		
		backToCategories.setOnAction(x->{
			if(timedMode.isSelected()) {
        		oneSecondTimer.stop();
        		tenSecondTimer.stop();
        	}
			window.setScene(categories());
		});
		
		//When the timed options is checked, initialize the time lines but don't start them until a category is picked
		timedMode.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				oneSecondTimer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
					int i = 9;
					@Override
					public void handle(ActionEvent event) {
						timer.setText("00:0" + i);
						if(i == 0) { timer.setText("00:10"); i = 10;}
						i--;
					}
				}));
				tenSecondTimer = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
					@Override
			    	public void handle(ActionEvent event) {
						proc.wrongGuesses++;
						hangman.setImage(hangmanPics[proc.wrongGuesses]);
						if(!proc.gameOn()) {
							oneSecondTimer.stop();
							tenSecondTimer.stop();
							window.setScene(end());
						}	
			    }
				}));
				oneSecondTimer.setCycleCount(Timeline.INDEFINITE);
				tenSecondTimer.setCycleCount(Timeline.INDEFINITE);
			}
			
		});
		
		backToMenuLogin.setOnAction(x->{window.setScene(menu());});

		//The word as seen by the user
		word.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
		
		//Showing the time for the timed difficulty
		timer.setManaged(false);
		timer.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		timer.setFill(Color.RED);
		timer.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
		timer.setX(710);
		timer.setY(40);
		
		//HBox with checkbox and label
		timed.getChildren().addAll(timedMode, timedModeLabel);
		timed.setAlignment(Pos.CENTER);
		timed.setSpacing(10);
				
		//Title
		title.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));
		title.setFill(Color.DARKSLATEGREY);
		title.setStyle("-fx-stroke: white; -fx-stroke-width: 1.5;");
		
		//User stats (name and score)
		currentUser.setManaged(false);
		currentUser.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
		currentUser.setFill(Color.WHITE);
		currentUser.setStyle("-fx-stroke: black; -fx-stroke-width: .5;");
		currentUser.setX(10); currentUser.setY(20);
		
		//Images for the different states of the game
		hangmanPics[0] = new Image("file:src\\hangmanPics\\0.png");
		hangmanPics[1] = new Image("file:src\\hangmanPics\\1.png");
		hangmanPics[2] = new Image("file:src\\hangmanPics\\2.png");
		hangmanPics[3] = new Image("file:src\\hangmanPics\\3.png");
		hangmanPics[4] = new Image("file:src\\hangmanPics\\4.png");
		hangmanPics[5] = new Image("file:src\\hangmanPics\\5.png");
		hangmanPics[6] = new Image("file:src\\hangmanPics\\win.png");
		hangmanPics[7] = new Image("file:src\\hangmanPics\\lose.png");
		
		//Imageview for the man
		hangman.setFitHeight(337);
		hangman.setFitWidth(410);
	}
	
	private Scene menu () {
		VBox menuPane = hangmanRoot();
		
		Button login = new Button("Log In");
		login.setOnAction(x ->{window.setScene(login());});
		
		Button leaderboard = new Button("Leaderboard");
		leaderboard.setOnAction(x ->{window.setScene(highScores());});
		
		//Game logo on first screen
		Image logoImg = new Image("file:src\\hangmanPics\\logo.png");
		ImageView logo = new ImageView(logoImg);
		
		menuPane.getChildren().addAll(logo, login, leaderboard, quit);
		
		return new Scene(menuPane);
	}
	
	private Scene login() {
		VBox loginPane = hangmanRoot();
		GridPane login = new GridPane();
		
		title.setText("Login");
		Text info = new Text("Enter your username and password:");
		
		login.add(new Label("Username: "), 0, 0);
		username = new TextField();
		login.add(username, 1, 0);
		
		login.add(new Label("Password:"), 0, 1);
		PasswordField password = new PasswordField();
		login.add(password, 1, 1);
		
		PasswordField confirmPassword = new PasswordField();
		
		//Buttons used only in the log in section
		Button loginButton = new Button("Log In");
		Button createAccount = new Button("Create Account");
		Button confirmAccount = new Button("Confirm & Log In");
		
		loginButton.setOnAction(x ->{
			if(!db.isRegistered(username.getText()))
				info.setText("Username incorrect/not registered");
			else
				if(!db.correctPassword(username.getText(), password.getText()))
					info.setText("Password incorrect!");
				else {
					//Accessing the library of the current player.
					library = new WordLibrary("src\\userLibraries\\" + username.getText() + "Library");
					currentScore = db.highScore.get(db.usernameList.indexOf(username.getText()));
					updateDisplayedScore();
					timedMode.setSelected(false);
					window.setScene(categories());
				}
			
		});
		
		createAccount.setOnAction(x ->{
			title.setText("Create Account");
			login.getChildren().remove(loginButton);
			
			login.add(new Label("Confirm Password:"), 0, 2);
			login.add(confirmPassword, 1, 2);
			login.getChildren().remove(createAccount);
			login.add(confirmAccount, 0, 3);
			
			//Moving the last to buttons to front to have better ordering of children (when using TAB)
			backToMenuLogin.toFront();
			quit.toFront();
			
		});
		
		confirmAccount.setOnAction(x ->{
			if(db.isRegistered(username.getText()))
				info.setText("Username already registered!");
			else
				if(!username.getText().isBlank())
					if(password.getText().isBlank())
						info.setText("Please enter a password!");
					else
						if(!password.getText().equals(confirmPassword.getText())) {
							info.setText("Passwords do not match!");
						}
						else {
							//Copying the categories in a library generated for the new user
							copyLibrary(username.getText());
							library = new WordLibrary("src\\userLibraries\\" + username.getText() + "Library");
							
							db.register(username.getText(), password.getText());
							currentScore = db.highScore.get(db.usernameList.indexOf(username.getText()));
							updateDisplayedScore();
							timedMode.setSelected(false);
							window.setScene(categories());
						}
		});
		
		login.add(loginButton, 0, 3);
		login.add(createAccount, 0, 4);
		login.add(backToMenuLogin, 1, 3);
		GridPane.setHalignment(backToMenuLogin, HPos.RIGHT);
		login.add(quit, 1, 4);
		GridPane.setHalignment(quit, HPos.RIGHT);
		
		login.setVgap(10);
		login.setHgap(5);
		login.setPadding(new Insets(10));
		login.setAlignment(Pos.CENTER);
		
		loginPane.getChildren().addAll(title, info, login);
		return new Scene(loginPane);
	}
	
	private Scene categories() {
		VBox categoriesPane = hangmanRoot();
		
		title.setText("Categories");
		categoriesPane.getChildren().add(title);
		
		int i = 0;
		//Populating the category list
		while(i < WordLibrary.categories.size()) {
			Category generatedCategory = WordLibrary.categories.get(i++);
			GridPane categoryPane = new GridPane();
			
			Button x = new Button(generatedCategory.getName());
			x.setPrefWidth(200);
			
			//Showing the progress for every category
			ProgressBar progressBar = new ProgressBar((double)(generatedCategory.initialSize - generatedCategory.getSize()) / generatedCategory.initialSize);
			Text progressInfo = new Text((generatedCategory.initialSize - generatedCategory.getSize()) + "/" + generatedCategory.initialSize + " words played");
			progressInfo.prefWidth(200);
			
			//Getting a word from a category if it has words left
			if(generatedCategory.hasWords())
				x.setOnAction(b -> {
					title.setText(generatedCategory.getName());
					playingCategory = generatedCategory;
					window.setScene(game(library.getWord(generatedCategory)));
				});
			else {
				x.setDisable(true);
				progressInfo.setText("completed");
			}
			
			categoryPane.add(x, 0, 0);
			categoryPane.add(progressBar, 1, 0);
			categoryPane.add(progressInfo, 2, 0);
			
			categoryPane.setVgap(20);
			categoryPane.setHgap(40);
			categoryPane.setAlignment(Pos.CENTER);
			categoriesPane.getChildren().add(categoryPane);
		}
		

		HBox leaveButtons = new HBox();
		leaveButtons.getChildren().addAll(backToMenu, quit);
		leaveButtons.setAlignment(Pos.CENTER);
		leaveButtons.setSpacing(10);
		
		categoriesPane.getChildren().addAll(currentUser, timed, leaveButtons);
		return new Scene(categoriesPane);
	}
	
	private Scene game(char[] givenWord) {
		VBox gamePane = hangmanRoot();
	
		//Give a new word to the processor
		proc = new Processor(givenWord);
		word.setText(proc.print());
		
		HBox leaveButtons = new HBox();
		leaveButtons.getChildren().addAll(backToCategories, backToMenu, quit);
		leaveButtons.setAlignment(Pos.CENTER);
		leaveButtons.setSpacing(10);
		
		//Setting the image according to the number of wrong guesses (always 0 in the beginning, it will be changed at wrong key presses)
		hangman.setImage(hangmanPics[proc.wrongGuesses]);
		
		gamePane.getChildren().addAll(title, hangman);

		//If the check box is selected, the game uses time-mode to play
		if(timedMode.isSelected()) {
			timer.setText("00:10");
			gamePane.getChildren().add(timer);
			oneSecondTimer.play();
			tenSecondTimer.play();	
		}	
		
		gamePane.getChildren().addAll(word, keyboard(), leaveButtons, currentUser);
		return new Scene(gamePane);
	}
	
	private Scene end() {
		VBox endPane = hangmanRoot();
		
		Text finalText = new Text(proc.printResult());
		finalText.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
		finalText.setTextAlignment(TextAlignment.CENTER);
		
		//Stopping the timer when a word is guessed
		if(timedMode.isSelected()) {
    		oneSecondTimer.stop();
    		tenSecondTimer.stop();
    	}
		
		Button nextWord = new Button("Next Word (same category)");
		if(!playingCategory.hasWords()) {
			nextWord.setDisable(true);
			nextWord.setText("Category completed!");
		}
		
		nextWord.setOnAction(x -> {
			if(timedMode.isSelected()) {
				oneSecondTimer.stop();
        		tenSecondTimer.stop();
			}
			window.setScene(game(library.getWord(playingCategory)));
		});
		
		HBox leaveButtons = new HBox();
		leaveButtons.getChildren().addAll(nextWord, backToCategories, backToMenu,  quit);
		leaveButtons.setAlignment(Pos.CENTER);
		leaveButtons.setSpacing(10);
		
		//Showing the final image and updating the current score
		if(proc.wrongGuesses < 6) {
			hangman.setImage(hangmanPics[6]);
			WordLibrary.guessedWord();
			if(timedMode.isSelected())
				currentScore+=3;
			else
				currentScore++;
			updateDisplayedScore();
		}
		else {
			if(timedMode.isSelected())
				WordLibrary.guessedWord();
			hangman.setImage(hangmanPics[7]);
		}
		endPane.getChildren().addAll(currentUser, finalText, hangman, timed, leaveButtons);
		return new Scene(endPane);
	}
	
	private Scene highScores() {
		VBox scoreList = hangmanRoot();
		title.setText("Leaderboard");
		
		//Combining the score and user name in a string list (for ease of sorting)
		String[] scores = new String[db.highScore.size()];
		for(int i = 0; i < db.highScore.size(); i++)
			scores[i] = db.highScore.get(i) + " " + db.usernameList.get(i);
		
		//Sorting the list based on the score
		Arrays.sort(scores, Collections.reverseOrder());
		
		//Displaying the score list
		VBox entries = new VBox();
		entries.setAlignment(Pos.CENTER);
		for(int i = 0; i < db.highScore.size(); i++) {
			Text entry = new Text(scores[i]);
			entry.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
			entries.getChildren().add(entry);
		}
		
		scoreList.getChildren().addAll(title, entries, backToMenuLogin);
		return new Scene(scoreList);
	}
	
	//All the scenes have this as the root
	private VBox hangmanRoot() {
		VBox root = new VBox();
		
		Image background = new Image("file:src\\hangmanPics\\background.jpg");
		ImageView back = new ImageView(background);
		back.setManaged(false);
		
		root.setSpacing(20);
		root.setAlignment(Pos.CENTER);
		root.setPrefSize(800, 600);
		root.getChildren().add(back);
		return root;
	}

	private VBox keyboard() {
		
		//The vertical rows of the keyboard
		VBox keysV = new VBox();
		
		//The two rows of keys
		HBox keysH1 = new HBox();
		HBox keysH2 = new HBox();
		
		ArrayList<String> keys = new ArrayList<String>(
				Arrays.asList("A", "B", "C", "D", "E", "F","G", "H", "I",
						"J", "K", "L", "M", "N", "O", "P", "Q", "R",
						"S", "T", "U", "V", "W", "X", "Y", "Z"));
		
		int i = 0;
		while(i < 13) {
			Button a = new Button(keys.get(i));
			a.setPrefWidth(50);
			a.setPrefHeight(30);
			a.setOnAction(letterHandler);
			keysH1.getChildren().add(a);
			i++;
		}
		while(i < 26) {
			Button a = new Button(keys.get(i));
			a.setPrefWidth(50);
			a.setPrefHeight(30);
			a.setOnAction(letterHandler);
			keysH2.getChildren().add(a);
			i++;
		}
		
		keysH1.setAlignment(Pos.CENTER);
		keysH2.setAlignment(Pos.CENTER);
		keysV.getChildren().addAll(keysH1, keysH2);
		return keysV;
	}

	//Event handler that gives the processor the letter or the button pressed and updates the image & text
	EventHandler<ActionEvent> letterHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			proc.guessLetter(((Button) event.getSource()).getText().toLowerCase().charAt(0));
			if(!proc.gameOn()) {
				window.setScene(end());
			}
			else {
				if(timedMode.isSelected()) { 
					
					//Inefficiently resetting the 1 sec timer by declaring it again, the issue is that i don't know how to reset the "i" variable in the event handler
					oneSecondTimer.stop();
					timer.setText("00:10");
					oneSecondTimer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						int i = 9;
						@Override
						public void handle(ActionEvent event) {
							timer.setText("00:0" + i);
							if(i == 0) { timer.setText("00:10"); i = 10;}
							i--;
						}
					}));
					oneSecondTimer.setCycleCount(Timeline.INDEFINITE);
					oneSecondTimer.play();
					
					//Resetting the 10 seconds timer is easy
					tenSecondTimer.stop();
					tenSecondTimer.playFromStart();
				}
				word.setText(proc.print());
				hangman.setImage(hangmanPics[proc.wrongGuesses]);
			}
			//Disable keys that were pressed
			((Button) event.getSource()).setDisable(true);
		}
		
	};
	
	//Method that updates the score shown in game as well as the score saved in the data base
	private void updateDisplayedScore() {
		db.highScore.set(db.usernameList.indexOf(username.getText()), currentScore);
		db.updateScore();
		currentUser.setText("User: " + username.getText() + "\r\nScore: " + currentScore);
	}
	
	//Method that creates a library(copies the base one in a new file) for a newly registered user
	private void copyLibrary(String name) {
		try {
			File destination = new File("src\\userLibraries\\" + name + "Library");
			Files.copy(baseCateg.toPath(), destination.toPath(),StandardCopyOption.REPLACE_EXISTING);
			
			String files[] = baseCateg.list();
    		for (String file : files) {
    		   File srcFile = new File(baseCateg, file);
    		   File destFile = new File(destination, file);
    		   Files.copy(srcFile.toPath(), destFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
    		} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		//When the game is stopped, the categories stored in the library are replaced with the ones in the system
		library.updateCategory();
	}
	
	public static void main(String[] args) {
		launch();
	}
}