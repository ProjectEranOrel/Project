/*package controllers;
import java.util.ArrayList;

import Entities.Result;
import Entities.Taxonomy;
import Entities.Vars;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

function Maximum Consecutive Subsequence(X, n)
Input: X (an array of size n)
Output: GlobalElements (a Queue of the maximum subsequence
elements)
GlobalMax := 0;
SuffixMax := 0;
for i := 1 to n do
if x[i] + SuffixMax > GlobalMax then
put x[i] in Queue GlobalElements
SuffixMax := SuffixMax + x[i];
GlobalMax := SuffixMax;
else if x[i] + SuffixMax > 0 then
put x[i] in SuffixQueue;
 SuffixMax := x[i] + SuffixMax;
else
SuffixMax := 0;
return GlobalElements;

public class TreeScreenController extends Application
{
	public TreeTableView<Taxonomy> treeTable;
	public static ArrayList<Taxonomy> tableData;
	public void start(Stage stage)
	{
		treeTable = new TreeTableView<>();
		TreeTableColumn<Taxonomy, String> IDCol = new TreeTableColumn<>("Taxonomy ID");
		TreeTableColumn<Taxonomy, String> nameCol = new TreeTableColumn<>("Last Name");
		TreeTableColumn<Taxonomy, Boolean> selectedCol = new TreeTableColumn<>("Selected");
		//populateTree(tableData);
		//After the table is ready we insert the data into it
		
		stage.setTitle("CDSF: Conserved DNA sequence finder");
		showScreen("FirstScreen");
//
	}
	public static void showScreen(String screenName) throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(TreeScreenController.class.getResource("/gui/"+screenName+".fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	private void populateTree()
	{
		
		TreeItem<Taxonomy> root = new TreeItem<Taxonomy>();
		root.getChildren().add(new TreeItem<Taxonomy>(tableData.get(0)));
		for(int i=1;i<tableData.size();i++)
		{
			Taxonomy res = tableData.get(i);   
			root.getChildren().add(new TreeItem<Taxonomy>(res));
			if(res.isExpandable())
				populateTree(res.getSons());
		}
	}
	
	
	public static void setTableData(ArrayList<Taxonomy> tableData)
	{
		TreeScreenController.tableData = tableData;
	}
	public static void main(String[] args) 
	{
		launch(args);
	}

	

}
*/