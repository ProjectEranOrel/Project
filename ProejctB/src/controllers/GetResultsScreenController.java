package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import entities.Main;
import entities.Vars;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

public class GetResultsScreenController 
{
	public boolean firstUpload = true, firstDesktop = true;
	public void initialize() {
		// Create a short clip on how to do that before opening the link
		String search = Vars.searchLink + Vars.searchWord;
		if(Vars.isUserDNA) {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(search));
			} catch (IOException | URISyntaxException e) {e.printStackTrace();} 
			Vars.desktopPath = System.getProperty("user.home") + "\\Desktop";

			/*AFTER DOWNLOADED!*/
		}
	}
	public void onUpload() 
	{
		if(firstUpload) {
			Thread thread = new Thread() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Please proceed ONLY if the download was finished.", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			};//thread.run();
			firstUpload = false;
		}

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");  
		fileChooser.getExtensionFilters().add(extFilter);
		Vars.resultsFile = fileChooser.showOpenDialog(Main.primaryStage);
		Main.showScreen("ResultsScreen", Vars.resultsScreenScreenTitle);
	}

	//If doesn't work, ask for path
	public void onDesktop() {
		if(firstDesktop) {
			Thread thread = new Thread() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Please proceed ONLY if the download was finished.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				}
			};thread.run();
			firstUpload = false;



			String filePath = Vars.desktopPath + "\\gene_results.txt";
			Vars.resultsFile = new File(filePath);
			if(Vars.resultsFile.isFile())
				System.out.println("IS FILE!");

			//Main.showScreen("ResultsScreen", Vars.resultsScreenScreenTitle);
		}

	}
}
