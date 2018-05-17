/*package controllers;
import java.util.ArrayList;

import Entities.Taxonomy;
import Entities.Vars;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

<<<<<<< HEAD



public class TreeScreenController 
=======
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
>>>>>>> branch 'master' of https://github.com/ProjectEranOrel/Project
{
	public TreeTableView<Taxonomy> treeTable;
	public static ArrayList<Taxonomy> tableData;
	public TableView<Taxonomy> selectedTable;
	
	@SuppressWarnings("unchecked")
	public void initialize()
	{
		TreeTableColumn<Taxonomy, String> IDCol = new TreeTableColumn<>("Taxonomy ID");
		IDCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getValue().getTaxID()));

		TreeTableColumn<Taxonomy, String> nameCol = new TreeTableColumn<>("Organism name");
		nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getValue().getOrganism()));
		
		TableColumn<Taxonomy,String> selectedIDcol = new TableColumn<>("Taxonomy ID");
		selectedIDcol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getTaxID()));
		
		TableColumn<Taxonomy, String> selectedNameCol = new TableColumn<>("Organism name");
		selectedNameCol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getOrganism()));

		treeTable.getColumns().addAll(IDCol,nameCol);
		treeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedTable.getColumns().addAll(selectedIDcol,selectedNameCol);
		selectedTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//populateTree(tableData);
		//After the table is ready we insert the data into it
		//ArrayList<Taxonomy> data = ParseSourceCode.getLineage(Vars.userResult.getTaxID());
		
		
		TreeItem<Taxonomy> root = new TreeItem<Taxonomy>();
		Taxonomy item1 = new Taxonomy("EE12","Charizard",true);
		for(int i=0;i<3;i++)
			item1.addToSons(new Taxonomy("EE12"+i,"Charizard"+i,false));
		Taxonomy item2 = new Taxonomy("EE13","Blastoise",true);
		for(int i=0;i<3;i++)
			item2.addToSons(new Taxonomy("EE13"+i,"Blastoise"+i,false));
		root.getChildren().addAll(new TreeItem<Taxonomy>(item1),new TreeItem<Taxonomy>(item2));
		treeTable.setRoot(root);
		treeTable.setShowRoot(false);
	}

	public void onAdd()
	{
		ObservableList<TreeItem<Taxonomy>> selectedItems = treeTable.getSelectionModel().getSelectedItems();
		for(int i=0;i<selectedItems.size();i++)
		{
			Taxonomy chosen = selectedItems.get(i).getValue();
			if(!selectedTable.getItems().contains(chosen))
				selectedTable.getItems().add(chosen);	
		}		
	}
	public void onRemove()
	{
		selectedTable.getItems().removeAll(selectedTable.getSelectionModel().getSelectedItems());
	}
	public void onClick()
	{
		TreeItem<Taxonomy> chosen = treeTable.getSelectionModel().getSelectedItem();
		if(chosen != null && chosen.getValue().isExpandable() && chosen.getChildren().size()==0)//If the chosen table entry has children and they weren't retrieved yet
			populateTree(chosen);
	}
	private void populateTree(TreeItem<Taxonomy> data)
	{
		ArrayList<Taxonomy> sons = data.getValue().getSons();
		for(int i=1;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);   
			if(son.isExpandable())
				populateTree(new TreeItem<Taxonomy>(son));
			data.getChildren().add(new TreeItem<Taxonomy>(son));
		}
	}


	public static void setTableData(ArrayList<Taxonomy> tableData)
	{
		TreeScreenController.tableData = tableData;
	}

	public void compare()
	{

	}

}
*/