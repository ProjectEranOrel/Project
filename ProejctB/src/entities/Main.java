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
		//showScreen("FirstScreen", Vars.firstScreenTitle);
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
		/*  Vars.userSequence = Vars.setSequence("24475906");
  Sequence seq = Vars.setSequence("24475906");  
  System.out.println(Vars.userSequence.compare(seq));

  Vars.userSequence = Vars.setSequence("24475906");
  Sequence seq1 = Vars.setSequence("50978625");  
  System.out.println(Vars.userSequence.compare(seq1));*/

		//Vars.trimNodesFile(new File("nodes.dmp"));

		//Vars.trimNodesFile();

		/* System.out.println("start");
  Vars.trimNodesFile();
>>>>>>> branch 'master' of https://github.com/ProjectEranOrel/Project
  System.out.println("end");
  launch(args)

		 */ 
		/*  ArrayList<Result> res = new ArrayList<Result>();
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
		/*  long startTime = System.nanoTime();
  Vars.setNodesList();
  long endTime = System.nanoTime(); 
  System.out.println("Time: " + TimeUnit.SECONDS.convert((endTime-startTime), TimeUnit.NANOSECONDS) + "s");*/
		Vars.setFamilies();
		Vars.setNodesArray();
		launch(args);
		//ParseSourceCode.getLineage("9615");
		
//		
//		double maxScore = 0, score=0;
//		/*		//Run a loop to look for dnas in the db, get them and run them through the black box and get File pointer
//		ArrayList<SequenceAlignment> alignedClustersList = new ArrayList<SequenceAlignment>();
//		for(int i=0;i<Math.min((double)Main.clustersIndexes.size(), (double)clustersIndexes.size());++i) {
//			ADD TO THE SEQUENCEALIGNMENT INDEX VARIABLES TO KNOW WHICH CLUSTER IT IS REPRESENTING
//			alignedClustersList.add(i, new SequenceAlignment());
//			alignedClustersList.get(i).calcOptimalAlignment(Main.userSequence,Main.);//Use the indexes on both files and align each cluster against the other
//		 */		
//		
//		Sequence toCompareDNA = test("C:\\Users\\Orel\\git\\Project\\ProejctB\\dna.txt");
//		
///*		toCompareDNA.clusters.add(new Cluster(0,3, "abc"));
//		toCompareDNA.clusters.add(new Cluster(3,9, "ccc"));
//		toCompareDNA.clusters.add(new Cluster(9,17, "cba"));
//		toCompareDNA.clusters.add(new Cluster(17,30, "cbc"));
//		toCompareDNA.clusters.add(new Cluster(30,35, "aaa"));
//		toCompareDNA.clusters.add(new Cluster(35,45, "aba"));*/
//
//		Vars.userSequence = test("C:\\Users\\Orel\\git\\Project\\ProejctB\\dna1.txt");
//		//Vars.setSequence(geneID)
///*		Vars.userSequence.clusters.add(new Cluster(0,3, "abc"));
//		Vars.userSequence.clusters.add(new Cluster(3,9, "ccc"));
//		Vars.userSequence.clusters.add(new Cluster(9,17, "cba"));
//		Vars.userSequence.clusters.add(new Cluster(17,30, "cbc"));
//		Vars.userSequence.clusters.add(new Cluster(30,35, "aaa"));
//		Vars.userSequence.clusters.add(new Cluster(35,45, "aba"));*/
//		for(int i=0;i<Math.min((double)Vars.userSequence.clusters.size(), (double)toCompareDNA.clusters.size());i++)
//			System.out.println(Vars.userSequence.clusters.get(i).getHiddenRepeat() + "     " + toCompareDNA.clusters.get(i).getHiddenRepeat());
//
//
//		toCompareDNA.setMatchScore(Vars.userSequence.compare(toCompareDNA));
//		System.out.println(toCompareDNA.getMatchScore());

	}
	
	public static Sequence test(String path) {
		Sequence sequence = new Sequence();
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(new File(path));
			br = new BufferedReader(fr);
			br.readLine();//First line is junk
			/*        DNA          */
			String string = "";
			while((string=br.readLine())!=null) 
				sequence.dna+= string;
		/*        Clusters       */
		fr = new FileReader(Vars.blackBox(path));

		br = new BufferedReader(fr);
		String start=br.readLine()/*=0*/, end;
		while((end = br.readLine()) != null) {
			sequence.clusters.add(new Cluster(Integer.parseInt(start), Integer.parseInt(end), 
					sequence.dna.substring(Integer.parseInt(start), Integer.parseInt(end))));
			
			start = end;
		}
		return sequence;
		}catch (Exception e) {e.printStackTrace();return null;}
		finally {try {br.close();} catch (IOException e) {e.printStackTrace();}}
	}
	
	

}