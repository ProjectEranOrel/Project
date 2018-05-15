package controllers;
import java.util.ArrayList;

import Entities.Result;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;



public class TreeScreenController 
{
	public TreeTableView<Result> treeTable;
	public static ArrayList<Result> tableData;
	public void start(Stage stage)
	{
		treeTable = new TreeTableView<>();
		TreeTableColumn<Result, String> IDCol = new TreeTableColumn<>("Taxonomy ID");
		TreeTableColumn<Result, String> nameCol = new TreeTableColumn<>("Last Name");
		TreeTableColumn<Result, Boolean> selectedCol = new TreeTableColumn<>("Selected");
		populateTree(tableData);
	}
	
	private void populateTree(ArrayList<Result> tableData)
	{
		TreeItem<Result> root = new TreeItem<Result>();
		root.getChildren().add(new TreeItem<Result>(tableData.get(0)));
		for(int i=1;i<tableData.size();i++)
		{
			Result res = tableData.get(i);   
			root.getChildren().add(new TreeItem<Result>(res));
			/*if(res.isExpandable())
				populateTree(res.getSons());*/	
		}
	}
	/*ERAN SIMHON*/
	
	public static void setTableData(ArrayList<Result> tableData)
	{
		TreeScreenController.tableData = tableData;
	}
	public static void main(String[] args)
	{
		//Result res1 = new Result()
	}
	

}
