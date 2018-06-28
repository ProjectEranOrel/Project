package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import entities.Main;
import entities.Vars;
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
		if(Vars.resultsFile==null)
		{
			JFrame jf=new JFrame();
	        jf.setAlwaysOnTop(true);
			JOptionPane.showMessageDialog(jf, "You must select a text file before proceeding.", "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(Vars.resultsFile));
			String line = br.readLine();
			br.close();
			if(!line.contains("tax_id	Org_name	GeneID	CurrentID	Status	Symbol	Aliases	description	other_designations	map_location	chromosome	genomic_nucleotide_accession.version	start_position_on_the_genomic_accession	end_position_on_the_genomic_accession	orientation	exon_count	OMIM"))
			{
				JFrame jf=new JFrame();
		        jf.setAlwaysOnTop(true);
				JOptionPane.showMessageDialog(jf, "The file you selected isn't a valid orthology file!", "Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
