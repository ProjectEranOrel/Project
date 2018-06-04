package controllers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import entities.Main;
import entities.Result;
import entities.Vars;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;



//OPTIMIZE : TAKE SOURCE CODE INTO RAM AND WORK ON IT FRMO THERE
public class FirstScreenController {

	@FXML
	private TextField uploadTextField;
	@FXML
	private Button searchButton;
	@FXML
	private Text subtitleText, fileNameText;

	public void initialize() {
		searchButton.setStyle("-fx-background-color: #96FFFF");
	}

	public void onUpload() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("FASTA files", "*.FASTA", "*.txt");  
		fileChooser.getExtensionFilters().add(extFilter);
		Vars.setUserDNAFile(fileChooser.showOpenDialog(Main.primaryStage));
		if(Vars.getUserDNAFile().exists()) {
			fileNameText.setText(Vars.getUserDNAFile().getName());
			subtitleText.setText("Please insert gene's Tax ID");
			searchButton.setText("Submit");
			
		}
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
		System.out.println("onSearch");
		if(uploadTextField.getText().equals("") && Vars.getUserDNAFile() == null) {
			JOptionPane.showMessageDialog(null,  "Please insert a key word before you search.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if (uploadTextField.getText().equals("")){
			JOptionPane.showMessageDialog(null,  "Please insert TaxID", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Vars.searchWord = uploadTextField.getText();
		Vars.isUserDNA = true;

		if(Vars.getUserDNAFile() != null) 
			if(!getOrthology(uploadTextField.getText())) {
				JOptionPane.showMessageDialog(null,  "The TaxID you've inserted has no orthology.");
				return;
			}
			else {				Vars.userResult = new Result();
				Vars.userResult.setTaxID(uploadTextField.getText());
				Vars.userResult.setGeneID("userFileUpload");
				Vars.userSequence = Vars.setSequence("userDNA");
			}
		
		System.out.println("onSearch");
		
		if(isResults()) {
			Vars.setNodesArray();
			Main.showScreen("GetResultsScreen", Vars.getResultsScreenTitle);
		}
		else 
			JOptionPane.showMessageDialog(null, "No Results were found.\nPlease try something else.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		
		/*CHANGE check if too many results!(will take too long)*/
	}

	public boolean getOrthology(String TaxID) {
		BufferedReader br = ParseSourceCode.getBufferedReader(Vars.orthoSearchLink + TaxID);
		try {
			String str;
			while((str = br.readLine()) != null) 
				if(str.contains("Orthologs"))
					break;
			if(str == null)
				return false;
			str = br.readLine();
			if(!str.contains("ortholog-links"))
				return false;

			while((str = br.readLine()).contains("gene")&&!str.contains("all")) 
				Vars.searchWord = str.substring(str.indexOf("gene/") + 5, str.indexOf(">")-2);
			
			if(str.contains("all"))
				Vars.searchWord = str.substring(str.indexOf("Term=") + 5, str.indexOf(">")-1);
			
			return true;
		}catch(Exception e) {e.printStackTrace(); return false;}
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
