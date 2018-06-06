package controllers;

import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

import entities.Result;
import entities.Sequence;
import entities.Taxonomy;
import entities.Vars;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;




public class TreeScreenController 
{
	public TreeTableView<Taxonomy> treeTable;
	public static ObservableList<Taxonomy> selectedData;
	public TableView<Taxonomy> selectedTable;
	private ArrayList<Integer> selectedItemsIndexes = new ArrayList<Integer>();
	private ArrayList<Result> resultList;
	int cnt = 0;

	private int startIndex, endIndex;

	@SuppressWarnings("unchecked")
	public void initialize()
	{
		/*for(int i=0;i<Result.orthology.size();i++)
			if(Vars.setSequence(Result.orthology.get(i).getGeneID()).getDNA().equals("bad dna"))
				System.out.println("BAD "+i);
			else System.out.println("Has dna...");*/
		resultList = Result.orthology;
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



		TreeItem<Taxonomy> root = new TreeItem<Taxonomy>();
		ArrayList<Taxonomy> data = ParseSourceCode.getLineage(Vars.userResult.getTaxID());
		for(int i=0;i<data.size();i++)
		{
			TreeItem<Taxonomy> item = new TreeItem<Taxonomy>(data.get(i));
			if(data.get(i).getOrganism().contains("*"))
				addSonsFromOrthology(item);
			root.getChildren().add(item);
		}
		treeTable.setRoot(root);
		treeTable.setShowRoot(false);
	}

	private int findInOrthology(String taxID)
	{
		for(int i=0;i<resultList.size();i++)
			if(resultList.get(i).getTaxID().equals(taxID))
				return i;
		return -1;
	}
	//Checks if the given taxID is an ancestor of a certain entry in the orthology list
	private ArrayList<Taxonomy> isAnAncestor(String taxID)
	{
		ArrayList<Taxonomy> res = new ArrayList<Taxonomy>();
		for(int i=0;i<resultList.size();i++)
		{
			int index = Integer.parseInt(resultList.get(i).getTaxID());
			while(Vars.nodesArray[index]!=null && !Vars.nodesArray[index].equals(""+index))
				if(Vars.nodesArray[index].equals(taxID))
				{

					Taxonomy tax = new Taxonomy(resultList.get(i).getTaxID(),resultList.get(i).getOrgName(),false);	
					res.add(tax);
					break;
				}
				else index = Integer.parseInt(Vars.nodesArray[index]);
		}
		return res;
	}
	public void onAdd()
	{
		ObservableList<TreeItem<Taxonomy>> selectedItems = treeTable.getSelectionModel().getSelectedItems();
		for(int i=0;i<selectedItems.size();i++)
		{
			Taxonomy chosen = selectedItems.get(i).getValue();
			if(findInOrthology(chosen.getTaxID())>-1)//If the user marked an entry from the orthology
				selectedTable.getItems().add(chosen);
			else//We check if the marked entry is an ancestor of certain entries in the orthology and add those to the table
			{
				ArrayList<Taxonomy> orthologyEntries = isAnAncestor(chosen.getTaxID());
				for(int j=0;j<orthologyEntries.size();j++)
				{
					boolean add = true;
					for(int k=0;k<selectedTable.getItems().size();k++)
					{
						if(selectedTable.getItems().get(k).getTaxID().equals(orthologyEntries.get(j).getTaxID()))
						{
							add = false;
							break;
						}
					}
					if(add)
						selectedTable.getItems().add(orthologyEntries.get(j));		
				}
			}
		}  
	}
	public void onRemove()
	{
		selectedTable.getItems().removeAll(selectedTable.getSelectionModel().getSelectedItems());
	}
	public void onClick() 
	{
		if(selectedItemsIndexes.contains(treeTable.getSelectionModel().getSelectedIndex()))
			return;
		selectedItemsIndexes.add(treeTable.getSelectionModel().getSelectedIndex());
		TreeItem<Taxonomy> chosen = treeTable.getSelectionModel().getSelectedItem();
		if(chosen != null && chosen.getValue().isExpandable() && chosen.getChildren().size()==0)//If the chosen table entry has children and they weren't retrieved yet
		{
			System.out.println("Starting to expand...");
			if(chosen.getValue().getOrganism().contains("*"))
				populateTreeMarked(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);
			else
				populateTreeUnmarked(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);
		}
	}
	private void addSonsFromOrthology(TreeItem<Taxonomy> item)
	{
		for(int j=0;j<resultList.size();j++)
		{
			Result res = resultList.get(j);//An entry from the orthology
			if(Vars.nodesArray[Integer.parseInt(res.getTaxID())].equals(item.getValue().getTaxID()))//If the item from the tree is the father of the entry from the orthology
			{
				//We check if the son is already in the tree
				boolean stopped = false;
				for(int k=0;k<item.getChildren().size();k++)
				{
					Taxonomy son = item.getChildren().get(k).getValue();
					if(son.getTaxID().equals(res.getTaxID()))
					{
						son.setOrganism("**"+son.getOrganism()+"**");
						stopped = true;
						break;
					}
				}
				if(!stopped)
					item.getChildren().add(new TreeItem<Taxonomy>(new Taxonomy(res.getTaxID(),"**"+res.getOrgName()+"**",false)));
				break;
			}
		}
	}
	private void populateTreeMarked(TreeItem<Taxonomy> chosenTreeItem, ArrayList<Taxonomy> sons,int depth)
	{
		for(int i=0;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);  
			TreeItem<Taxonomy> sonItem = new TreeItem<Taxonomy>(son);
			addSonsFromOrthology(sonItem);
			if(son.isExpandable() && depth>0)
				populateTreeMarked(sonItem,son.getSons(),depth-1);	
			chosenTreeItem.getChildren().add(sonItem);
		}
	}
	private void populateTreeUnmarked(TreeItem<Taxonomy> chosenTreeItem, ArrayList<Taxonomy> sons,int depth)
	{
		for(int i=0;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);  
			TreeItem<Taxonomy> sonItem = new TreeItem<Taxonomy>(son);
			if(son.isExpandable() && depth>0)
				populateTreeUnmarked(sonItem,son.getSons(),depth-1);
			chosenTreeItem.getChildren().add(sonItem);
		}
	}



	public void compare()
	{
		selectedData = selectedTable.getItems();
		ArrayList<Taxonomy> itemsToBeCompared = new ArrayList<Taxonomy>();
		for(int i=0;i<selectedData.size();i++)
		{
			Taxonomy item = selectedData.get(i);
			int index = findInOrthology(item.getTaxID());
			if(index>-1)
			{
				item.setSequence(Vars.setSequence(resultList.get(index).getGeneID()));
				if(item.getSequence().getDNA().equals("bad dna"))
					item.getSequence().setMatchScore(-1);
				else item.getSequence().setMatchScore(Vars.userSequence.compare(item.getSequence()));
				//else item.getSequence().setMatchScore(new Sequence(Vars.userResult.getTaxID()).compare(item.getSequence()));
				itemsToBeCompared.add(item);
			}
		}
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ResultMatchScreen.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			ResultMatchScreen controller = fxmlLoader.getController();
			controller.setItems(itemsToBeCompared);
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));  
			stage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		selectedTable.getItems().removeAll(selectedTable.getItems());
	}

	private ArrayList<Taxonomy> getChildren(Taxonomy tax,ArrayList<Taxonomy> res)
	{
		ArrayList<Taxonomy> sons = tax.getSons();
		for(int i=0;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);
			if(son.getOrganism().contains("**"))
				for(int j=0;j<resultList.size();j++)
					if(resultList.get(j).getTaxID().equals(son.getTaxID()))
					{
						son.setSequence(Vars.setSequence(resultList.get(j).getGeneID()));
						if(son.getSequence().getDNA().equals("bad dna"))
							son.getSequence().setMatchScore(-1);
						else son.getSequence().setMatchScore(new Sequence(Vars.userResult.getTaxID()).compare(son.getSequence()));	
						res.add(son);
						if(son.isExpandable())
							getChildren(son,res);
						break;
					}
		}
		return res;
	}

}