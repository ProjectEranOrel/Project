package Entities;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application
{
	public static Stage primaryStage;
	public static Parent mainLayout;
	public static int port=5555;
	public static String host = "localhost";
	public static Stage popup;
	public static Scene currentScene;
	public static Pane popUp;


	@Override
	public void start(Stage primaryStage) throws IOException {
		Main.primaryStage = primaryStage;
		popup = new Stage();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primaryStage);
		popup = new Stage();
		//showScreen("ResultsScreen", Vars.firstScreenTitle);
		showScreen("GetResultsScreen", "Boobs and drugs");
	}
	public static void showScreen(String screenName, String screenTitle) 
	{
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/gui/"+screenName+".fxml"));
			mainLayout = loader.load();
			currentScene = new Scene(mainLayout);
			primaryStage.setScene(currentScene);
			primaryStage.setTitle(screenTitle);
			primaryStage.show();
		}catch(Exception e) {e.printStackTrace();}
	}
	/*public static void showPopUpWait(String screenName) {
		try {
			popUp = FXMLLoader.load(Main.class.getResource("/gui/"+screenName+".fxml"));
		} catch (IOException e) {System.out.println("DeFuck");e.printStackTrace();}
		Main.popup.setScene(new Scene(popUp));
		Main.popup.showAndWait();
	}
	public static void showPopUp(String screenName) {
		try {
			popUp = FXMLLoader.load(Main.class.getResource("/gui/"+screenName+".fxml"));
		} catch (IOException e) {e.printStackTrace();}
		Main.popup.setScene(new Scene(popUp));
		Main.popup.show();
	}*/


	public static void main(String[] args) 
	{
		Vars.userSequence = Vars.setSequence("24475906");
		Sequence seq = Vars.setSequence("24475906");
		System.out.println(Vars.userSequence.compare(seq));
		//launch(args);
	}

}
