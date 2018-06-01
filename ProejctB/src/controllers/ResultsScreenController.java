package controllers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Entities.Main;
import Entities.Result;
import Entities.Sequence;
import Entities.Vars;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
/*CHANGE: Download DNA, Search for the matching dna(with name through the site zakharya gave us or taxonomy somehow), download that dna and compare
  CHANGE Check weather the list is too long to download and warn the user!
  CHANGE add search options to table
  CHANGE add a new screen for compared results ( as well as the user results, difference: buttons and another cell(Results)*/
public class ResultsScreenController {
	public ObservableList <Result> resultsList;
	ArrayList<Result> resultsArrayList = new ArrayList<Result>();
	@FXML
	public Text checkAllText;
	@FXML
	TableView<Result> resultsTableView = new TableView<Result>();
	@FXML
	TableColumn<Result, String> nameGenTableColumn, descriptionTableColumn, numberTableColumn, aliasesTableColumn, mimTableColumn, resultsTableColumn, orgNameTableColumn;
	@FXML
	Button checkAllButton, checkSelectedButton;
	private int rowNumber = 0;

	Thread t;

	public void initialize() {
		checkAllText.setVisible(false);
		if(Vars.isUserDNA) {
			userResults();
			Vars.isUserDNA = false;
		}
		setResults();
		if(Vars.getUserDNAFile() != null) 
			Main.showScreen("TreeScreen", "bbb");		
		setTable();
	}

	public void userResults() {
		checkAllButton.setText("Select");
		checkAllButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				onSelect();
			}
		});

		checkSelectedButton.setText("Upload");
		checkSelectedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				resultsList.removeAll(resultsList);
				Result.resultsList.removeAll(Result.resultsList);
				rowNumber=0;
				uploadResultsFile();
				setResults();
				setTable();	
				resultsTableView.refresh();
				
			}
		});
	}

	public void onSelect() {
		if((Vars.userResult = resultsTableView.getSelectionModel().getSelectedItem()) == null) {
			JOptionPane.showMessageDialog(null, "Please select a result", "Warning",
					JOptionPane.WARNING_MESSAGE); return;}
		

		Main.showScreen("TreeScreen", "bbb");
	}

	public void onCheckSelected() {
		//Vars.userResult = resultsTableView.getSelectionModel().getSelectedItem();
		//Vars.setDNAFile(Vars.getDNAByGI(Vars.userResult.getGeneID()));
	}

	/*public static boolean setUserSequence() {
		try {
			FileReader	fr = new FileReader(Vars.getUserDNAFile());
			BufferedReader	br = new BufferedReader(fr);
			for(int i=0;i<5;i++) 
				if(br.readLine().contains("ERROR"))
					return false;
			br = new BufferedReader(fr);//reinitialize it
			br.readLine();//Skip first row
			String temp;
			while((temp = br.readLine()) != null)
				Vars.userSequence.dna+=temp;
			Vars.blackBox("dna.txt");
			return true;
		}catch (Exception e) {e.printStackTrace();}
		return false;

	}*/

	public void setResults() {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			if(Vars.resultsFile == null)
				uploadResultsFile();
			fr = new FileReader(Vars.resultsFile);
			br = new BufferedReader(fr);

			String currentLine;
			br.readLine();//Skip first row
			while ((currentLine = br.readLine()) != null) {
				currentLine = currentLine.replace("\t", "<~>");
				getValues(currentLine);
			}

		} catch (IOException e) {e.printStackTrace();}
		finally {

			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();

			} catch (IOException ex) {ex.printStackTrace();}
		}
	}


	public void getValues(String line) {
		Result res = new Result();
		int index=0;
		/*       TaxID     */
		res.setTaxID(line.substring(0, getIndexOf(1, line, false)-3));
		/*     GeneID      */
		index = getIndexOf(2, line, false);
		if(index == -1)
			res.setGeneID("");
		else 
			res.setGeneID(line.substring(index, getIndexOf(3, line, true)-3));
		/*     Name      */
		index = getIndexOf(4, line, false);//status\
		if(index == -1)
			res.setName("");
		else 
			if(((line.substring(index, getIndexOf(5, line, true))).replace("<~>", "")).equals("live")) //Name's in the status
				res.setName((line.substring(getIndexOf(5, line, false), getIndexOf(6, line, true)).replace("<~>", "")));
			else//Name's in the symbol
				res.setName(((line.substring(index, getIndexOf(5, line, true)).replace("<~>", ""))));

		/*         NameGenID         */
		res.setNameGenID(res.getName() + "\n" + res.getGeneID());
		/*         OrgName          */
		index = getIndexOf(1, line, false);
		if(index == -1)
			res.setOrgName("");
		else 
			res.setOrgName((line.substring(index, getIndexOf(2, line, true)).replace("<~>", "")));
		/*     Description    */
		index = getIndexOf(7, line, false);
		if(index == -1)
			res.setDescription("");
		else 
			res.setDescription(line.substring(index, getIndexOf(8, line, true)-3));
		/*     Aliases      */
		index = getIndexOf(6, line, false);
		if(index != -1)
			res.setAliases(line.substring(index, getIndexOf(7, line, true)-3));
		else
			res.setAliases(" ");
		/*     MIM        */
		int mimIndex = line.substring(0, line.length() - 3).lastIndexOf("<~>")+3;// line ends with <~> (\t)
		if(line.charAt(mimIndex) == '<')
			res.setMim(" ");
		else
			res.setMim(line.substring(mimIndex, line.length()-3));
		res.setNumber(rowNumber++);
		
		res.ancestors = new ArrayList<String>(Vars.findAncestors(res.getTaxID()));
		System.out.println("lel: " + res.ancestors.size() + "   index: " + rowNumber);
		Result.resultsList.add(res);
		
	}


	public int getIndexOf(int times, String line, boolean end) {//times -> how many times <~> needs to show, end -> if it is the end of the field, so it won't check if the next is again <~>
		int currIndex = 0;
		for(int i=0;i<times;i++) {
			currIndex = line.indexOf("<~>", currIndex) + 1;
		}
		if(line.charAt(currIndex+2) == '<' && !end)
			return -1;
		else
			return currIndex+2;
	}


	public void setTable() {
		nameGenTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("taxID"));
		orgNameTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("orgName"));
		descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("description"));
		numberTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("number"));
		aliasesTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("aliases"));
		mimTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("mim"));
		resultsTableColumn.setCellValueFactory(new PropertyValueFactory<Result,String>("results"));
		resultsList = FXCollections.observableArrayList(Result.resultsList);
		resultsTableView.setEditable(true);
		resultsTableView.getItems().clear();
		resultsTableView.setItems(resultsList);//Set the list before this command
		resultsTableView.refresh();
	}

	public void onCheckAllEntered() { checkAllText.setVisible(true); }

	public void onCheckAllExited() { checkAllText.setVisible(false); }


	public void uploadResultsFile() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");  
		fileChooser.getExtensionFilters().add(extFilter);
		Vars.resultsFile = null;
		Vars.resultsFile = fileChooser.showOpenDialog(Main.primaryStage);
	}


}


// initialize - when stage is opening
// stop - when stage is closing
