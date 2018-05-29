package Entities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
		showScreen("GetResultsScreen", "");

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
		/*		Vars.userSequence = Vars.setSequence("24475906");
		Sequence seq = Vars.setSequence("24475906");		
		System.out.println(Vars.userSequence.compare(seq));

		Vars.userSequence = Vars.setSequence("24475906");
		Sequence seq1 = Vars.setSequence("50978625");		
		System.out.println(Vars.userSequence.compare(seq1));*/

		//Vars.trimNodesFile(new File("nodes.dmp"));
		/*	System.out.println("start");
		Vars.trimNodesFile();
		System.out.println("end");
	//	launch(args)

		 */	
/*		ArrayList<Result> res = new ArrayList<Result>();
		for(int i=0;i<10000;i++)
			res.add(new Result());
		long startTime = System.nanoTime();
		Vars.trimNodesFile();
		long endTime = System.nanoTime();

		long durationDisk = (endTime - startTime);
		startTime = System.nanoTime();
			for(int i=0;i<10000;i++)
				System.out.println(i);
		endTime = System.nanoTime();
		long durationRam = (endTime-startTime);
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\nDisk Time: " + TimeUnit.SECONDS.convert(durationDisk, TimeUnit.NANOSECONDS) + 
				"s\nRam Time:" + TimeUnit.SECONDS.convert(durationRam, TimeUnit.NANOSECONDS) + 
				"s\ndifference:" + durationDisk/durationRam + "s");*/
		long startTime = System.nanoTime();
		Vars.setNodesList();
		long endTime = System.nanoTime(); 
		System.out.println("Time: " + TimeUnit.SECONDS.convert((endTime-startTime), TimeUnit.NANOSECONDS) + "s");
	}

}
