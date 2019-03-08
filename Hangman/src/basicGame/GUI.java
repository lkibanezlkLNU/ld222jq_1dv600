package basicGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import basicGame.Processor;
import basicGame.DataBase;

public class GUI extends Application {
	
	
	//the main stage
	private Stage window;
	
	private Processor proc;
	private Text currentCategoryName = new Text("This shows the current category");;
	private Text livesLeft = new Text("This shows lives left, will be replaced with graphics");
	private Text word = new Text("This will contain the letters to be guessed");
	private Button quit = new Button ("Quit");	
	private Button backToMenu = new Button("Back To Menu");
	
	//Initializing a library and a data base
	private WordLibrary library = new WordLibrary("src\\categoryFolder");
	private DataBase db = new DataBase("src\\db.txt");

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
            if (result.get() == ButtonType.OK){
                window.close();
            }

            if(result.get()==ButtonType.CANCEL){
                quitAlert.close();
            }
			
		});
		
		backToMenu.setOnAction(x -> {
			Alert menuAlert = new Alert(AlertType.CONFIRMATION);
			menuAlert.setTitle("Back to menu");
			menuAlert.setHeaderText("Menu Confirmation");
			menuAlert.setContentText("Are you sure you want to go back to menu?");
			
			Optional<ButtonType> result = menuAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                window.setScene(menu());
            }

            if(result.get()==ButtonType.CANCEL){
            	menuAlert.close();
            }
			
		});
	}
	
	
	
	private Scene menu () {
		VBox menuPane = hangmanRoot();
		
		Text title = new Text("Hangman Revisited");
		title.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		
		Button login = new Button("Log In");
		login.setOnAction(x ->{window.setScene(login());});
		
		menuPane.getChildren().addAll(title,login, quit);
		return new Scene(menuPane);
	}
	
	private Scene login() {
		VBox loginPane = hangmanRoot();
		
		Text title = new Text("Login");
		title.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		
		Text info = new Text("Enter your username and password:");
		
		GridPane login = new GridPane();
		
		login.add(new Label("Username: "), 0, 0);
		final TextField username = new TextField();
		login.add(username, 1, 0);
		login.add(new Label("Password:"), 0, 1);
		final PasswordField password = new PasswordField();
		login.add(password, 1, 1);
		final PasswordField confirmPassword = new PasswordField();
		
		
		Button loginButton = new Button("Log In");
		Button createAccount = new Button("Create Account");
		Button confirmAccount = new Button("Confirm Account");
		Button goToGame = new Button("Go to game");
		
		loginButton.setOnAction(x ->{
			if(!db.isRegistered(username.getText()))
				info.setText("Username incorrect/not registered");
			else
				if(!db.correctPassword(username.getText(), password.getText()))
					info.setText("Password incorrect!");
				else
					window.setScene(categories());
			
		});
		
		createAccount.setOnAction(x ->{
			title.setText("Create Account");
			login.getChildren().remove(loginButton);
			login.add(new Label("Confirm Password:"), 0, 2);
			login.add(confirmPassword, 1, 2);
			login.getChildren().remove(createAccount);
			login.add(confirmAccount, 0, 4);
		});
		
		confirmAccount.setOnAction(x ->{
			if(db.isRegistered(username.getText()))
				info.setText("Username already registered!");
			else
				if(!username.getText().isBlank())
					if(!password.getText().equals(confirmPassword.getText())) {
						info.setText("Passwords do not match!");
					}
					else {
						db.register(username.getText(), password.getText());
						info.setText("Account created!");
						username.setDisable(true);
						password.setDisable(true);
						confirmPassword.setDisable(true);
						login.getChildren().remove(confirmAccount);
						login.add(goToGame, 0, 4);
					}
		});
		
		goToGame.setOnAction(x ->{ window.setScene(categories());});
		
		
		login.add(loginButton, 0, 3);
		login.add(createAccount, 0, 4);
		login.add(backToMenu, 1, 4);
		login.add(quit, 2, 4);
		
		login.setVgap(10);
		login.setHgap(5);
		login.setPadding(new Insets(10));
		login.setAlignment(Pos.CENTER);
		
		loginPane.getChildren().addAll(title, info, login);
		return new Scene(loginPane);
	}
	
	private Scene categories() {
		
		VBox categoriesPane = hangmanRoot();
		
		Text title = new Text("Categories");
		title.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		categoriesPane.getChildren().add(title);
		
		int i = 0;
		while(i < library.categories.size()) {
			Category currentCateg = library.categories.get(i);
			
			Button x = new Button(currentCateg.getName());
			
			x.setOnAction(b -> {
				currentCategoryName.setText(currentCateg.getName());
				window.setScene(game(library.getWord(currentCateg)));
				
			});
			categoriesPane.getChildren().add(x);
			
			i++;
		}
		
		categoriesPane.getChildren().addAll(backToMenu);
		return new Scene(categoriesPane);
	}
	
	private Scene game(char[] givenWord) {
		VBox gamePane = hangmanRoot();
	
		//Give a new word to the processor
		proc = new Processor(givenWord);
		livesLeft = new Text(proc.printLives());
		word.setText(proc.print());
		
		HBox leaveButtons = new HBox();
		leaveButtons.getChildren().addAll(backToMenu, quit);
		leaveButtons.setAlignment(Pos.CENTER);
		leaveButtons.setSpacing(10);
		
		gamePane.getChildren().addAll(currentCategoryName, livesLeft, word, keyboard(), leaveButtons);
		return new Scene(gamePane);
	}
	
	private Scene end() {
		VBox endPane = hangmanRoot();
		Text finalText = new Text(proc.printResult());
		finalText.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
		finalText.setTextAlignment(TextAlignment.CENTER);
		
		Button keepPlaying = new Button("Keep playing");
		keepPlaying.setOnAction(x->{
			window.setScene(categories());
		});
		
		
		endPane.getChildren().addAll(finalText, keepPlaying, backToMenu, quit);
		return new Scene(endPane);
		
	}
	
	//All the scenes have this as the root
	private VBox hangmanRoot() {
		VBox root = new VBox();
			
		root.setStyle("-fx-background-color: beige;");
		root.setSpacing(20);
		root.setAlignment(Pos.CENTER);
		root.setPrefSize(800, 600);
		return root;
	}

	private VBox keyboard() {
		
		VBox keysV = new VBox();
		HBox keysH1 = new HBox();
		HBox keysH2 = new HBox();
		
		ArrayList<String> keys = new ArrayList<String>(
				Arrays.asList("A", "B", "C", "D", "E", "F","G", "H", "I",
						"J", "K", "L", "M", "N", "O", "P", "Q", "R",
						"S", "T", "U", "V", "W", "X", "Y", "Z"));
		
		int i = 0;
		while(i < 13) {
			Button a = new Button(keys.get(i));
			a.setOnAction(letterHandler);
			keysH1.getChildren().add(a);
			i++;
		}
		while(i < 26) {
			Button a = new Button(keys.get(i));
			a.setOnAction(letterHandler);
			keysH2.getChildren().add(a);
			i++;
		}
		
		keysH1.setAlignment(Pos.CENTER);
		keysH2.setAlignment(Pos.CENTER);
		keysV.getChildren().addAll(keysH1, keysH2);
		return keysV;
	}

	EventHandler<ActionEvent> letterHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			proc.guessLetter(((Button) event.getSource()).getText().toLowerCase().charAt(0));
			if(!proc.gameOn()) {
				window.setScene(end());
			}
			else {
				word.setText(proc.print());
				livesLeft.setText(proc.printLives());
			}
			
			((Button) event.getSource()).setDisable(true);
		}
		
	};
	
	public static void main(String[] args) {
		launch();
	}

}
