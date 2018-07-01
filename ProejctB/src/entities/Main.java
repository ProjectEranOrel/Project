package entities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application
{
	public static Stage primaryStage;
	public static Parent mainLayout;
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
		showScreen("FirstScreen", Vars.firstScreenTitle);
		//showScreen("GetResultsScreen", "");



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
			  Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			  primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2); 
			  primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4); 
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
		Vars.setFamilies();
		Vars.setNodesArray();
		launch(args);
	}
	
/*	public static Sequence test(String path) {
		Sequence sequence = new Sequence();
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(new File(path));
			br = new BufferedReader(fr);
			br.readLine();//First line is junk
			        DNA          
			String string = "";
			while((string=br.readLine())!=null) 
				sequence.dna+= string;
		        Clusters       
		fr = new FileReader(Vars.blackBox(path));

		br = new BufferedReader(fr);
		String start=br.readLine()=0, end;
		while((end = br.readLine()) != null) {
			sequence.clusters.add(new Cluster(Integer.parseInt(start), Integer.parseInt(end), 
					sequence.dna.substring(Integer.parseInt(start), Integer.parseInt(end))));
			
			start = end;
		}
		return sequence;
		}catch (Exception e) {e.printStackTrace();return null;}
		finally {try {br.close();} catch (IOException e) {e.printStackTrace();}}
	}
	
	*/

}