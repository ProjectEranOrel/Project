package controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Entities.Main;
import Entities.Vars;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;




public class FirstScreenController {

	@FXML
	private TextField uploadTextField;
	@FXML
	private Button searchButton;

	public void initialize() {
		searchButton.setStyle("-fx-background-color: #96FFFF");
	}
	
	public void onUpload() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("FASTA files (*.fasta)", "*.FASTA");  
		fileChooser.getExtensionFilters().add(extFilter);
		File dnaFile = fileChooser.showOpenDialog(Main.primaryStage);
		if(dnaFile.exists())
			uploadTextField.setText(dnaFile.getName());
		//CHANGE Run CPP code but check first for 5(more or less) consequent letters that contain only A G C T
	}

	public void onInputKeyPressed() {
		Main.currentScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				try { onSearch(); }
				catch (IOException e1) {e1.printStackTrace();}
			}
		});
	}
	/*CHANGE add a short video on how to do it and check weather its user's or looking for a match*/
	public void onSearch() throws IOException
	{
		if(uploadTextField.getText().equals("")) {
			JOptionPane.showMessageDialog(null,  "Please insert a key word before you search.");
			return;
		}
		Vars.searchWord = uploadTextField.getText();
		Vars.isUserDNA = true;
		if(isResults())
			Main.showScreen("GetResultsScreen", Vars.getResultsScreenTitle);
		else {
			JOptionPane.showMessageDialog(null, "No Results were found.\nPlease try something else.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		/*CHANGE check if too many results!(will take too long)*/		
	}
	
	
	public void onSearchPressed() {
		searchButton.setStyle("-fx-background-color:#558C8C");
	}
	public void onSearchReleased() {
		searchButton.setStyle("-fx-background-color: #96FFFF");

	}
	
	
	public boolean isResults() {//Checks if the any results were found
		URLConnection conn;
		try {
			URL url = new URL(Vars.searchLink + Vars.searchWord);
			conn = url.openConnection();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) 
				if(inputLine.contains(Vars.noResultsString)) 
					return false;
			return true;

		}catch(Exception e) {e.printStackTrace();}
		return false;
	}

}
