package controllers;
import java.util.ArrayList;

import Entities.Sequence;
import Entities.Main;
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




public class TreeScreenController 
{
	public TreeTableView<Taxonomy> treeTable;
	public static ObservableList<Taxonomy> selectedData;
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
		ArrayList<Taxonomy> data = ParseSourceCode.getLineage(Vars.userResult.getGeneID());
		for(int i=0;i<data.size();i++)
			root.getChildren().add(new TreeItem<Taxonomy>(data.get(i)));
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
			populateTree(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);

	}
	private void populateTree(TreeItem<Taxonomy> data, ArrayList<Taxonomy> sons,int depth)
	{
		for(int i=0;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);  
			TreeItem<Taxonomy> sonItem = new TreeItem<Taxonomy>(son);
			if(son.isExpandable() && depth>0)
				populateTree(sonItem,son.getSons(),depth-1);
			data.getChildren().add(sonItem);
		}
	}

	public static void runCompare(Sequence seq) {
		//Vars.userSequence.
		
	}

	/*public static void setTableData(ArrayList<Taxonomy> tableData)
	{
		TreeScreenController.tableData = tableData;
	}*/

	public void compare()
	{
		selectedData = selectedTable.getItems();
		Main.showScreen("ResultMatchScreen", "Boobs and drugs");
	}

}
