package controllers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Entities.Main;
import Entities.Result;
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
	private ArrayList<Integer> selectedItemsIndexes = new ArrayList<Integer>();
	private ArrayList<Result> resultList;

	private int startIndex = 0, endIndex;

	@SuppressWarnings("unchecked")
	public void initialize()
	{
		resultList = Result.resultsList;
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
		ArrayList<Taxonomy> data = ParseSourceCode.getLineage(Vars.userResult.getTaxID());
		for(int i=0;i<data.size();i++)
			root.getChildren().add(new TreeItem<Taxonomy>(data.get(i)));
		treeTable.setRoot(root);
		treeTable.setShowRoot(false);
		//markEntries(treeTable.getRoot());
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
		if(selectedItemsIndexes.contains(treeTable.getSelectionModel().getSelectedIndex()))
			return;
		System.out.println("Starting to expand...");
		selectedItemsIndexes.add(treeTable.getSelectionModel().getSelectedIndex());
		/*Thread t1 = new Thread(new Runnable() {
			public void run() {
				TreeItem<Taxonomy> chosen = treeTable.getSelectionModel().getSelectedItem();

				if(chosen != null && chosen.getValue().isExpandable() && chosen.getChildren().size()==0)//If the chosen table entry has children and they weren't retrieved yet
					populateTree(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);
			}
		});t1.start();*/
		TreeItem<Taxonomy> chosen = treeTable.getSelectionModel().getSelectedItem();
		if(chosen != null && chosen.getValue().isExpandable() && chosen.getChildren().size()==0)//If the chosen table entry has children and they weren't retrieved yet
			populateTree(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);

	}


	private void populateTree(TreeItem<Taxonomy> chosenTreeItem, ArrayList<Taxonomy> sons,int depth)
	{
		for(int i=0;i<sons.size();i++)
		{
			Taxonomy son = sons.get(i);  
			TreeItem<Taxonomy> sonItem = new TreeItem<Taxonomy>(son);
			if(son.isExpandable() && depth>0)
				populateTree(sonItem,son.getSons(),depth-1);
			chosenTreeItem.getChildren().add(sonItem);
			double numOfThreads = 2;
			while(((double)resultList.size()/numOfThreads)%numOfThreads!=0)
				numOfThreads++;
			System.out.println(numOfThreads);
			int numOfEntries = (int) (resultList.size()/numOfThreads);
			/*if(numOfEntries != (int)numOfEntries)
				numOfEntries++;*/
			endIndex = numOfEntries;
			Thread markingThreads[] = new Thread[(int)numOfThreads];
			for(int j=0;j<numOfThreads;j++)
			{
				markingThreads[j] = new Thread(new Runnable()
				{
					@Override
					public void run() 
					{
						int localStartIndex = startIndex;
						int localEndIndex = endIndex;
						for(int k=localStartIndex;k<localEndIndex-1;k++)
						{
							try 
							{
								if(findAncestors(resultList.get(k).getTaxID()).contains(son.getTaxID()))
								{
									System.out.println("Found something...");
									ObservableList<TreeTableColumn<Taxonomy, ?>> columns = treeTable.getColumns();
									System.out.println("Going thrrough the columns");
									for(int l=0;l<columns.size();l++)
									{
										System.out.println("Column"+l);
										columns.get(l).setStyle("-fx-background-color:#558C8C");
									}
								}
							} 
							catch (IOException e) 
							{
								System.out.println("k: "+k+"\nresultList: "+resultList.size());
								e.printStackTrace();
							}
						}
					}
				});
				markingThreads[j].start();
				System.out.println("Thread "+j+" started");
				startIndex = endIndex;
				endIndex += (int)numOfEntries;
			}
			for(int j=0;j<numOfThreads;j++)
				try {
					markingThreads[j].join();
				} catch (InterruptedException e) {
					System.out.println("j: "+j+"\nresultList: "+resultList.size());
					e.printStackTrace();
				}
		}
		System.out.println("DONE");
	}

	/*private void markEntries(TreeItem<Taxonomy> root)
	{

		ObservableList<TreeItem<Taxonomy>> treeItems = root.getChildren();
		for(int i=0;i<resultList.size();i++)
		{
			try {
				ArrayList<String> ancestors = findAncestors(resultList.get(i).getTaxID());
				for(int j=0;j<treeItems.size();j++)
				{

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}*/

	public ArrayList<String> findAncestors(String taxID) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("nodes.dmp"));
		String readLine;
		ArrayList<String> sonColumn = new ArrayList<String>();
		ArrayList<String> parentColumn = new ArrayList<String>();
		while((readLine = br.readLine())!=null)
		{
			//Add to SonColumn
			String temp =readLine.substring(0, readLine.indexOf('|'));
			temp = temp.replaceAll("\\s+", "");
			sonColumn.add(temp);

			//Add to parentColumn
			temp = readLine.substring(readLine.indexOf('|')+1,readLine.indexOf('|',readLine.indexOf('|')+1));
			temp = temp.replaceAll("\\s+", "");
			parentColumn.add(temp);
		}
		ArrayList<String> ancestors = new ArrayList<String>();
		String currAncestor = parentColumn.get(sonColumn.indexOf(taxID));
		String nextAncestor = null;
		ancestors.add(currAncestor);
		while(true)
		{
			nextAncestor = parentColumn.get(sonColumn.indexOf(currAncestor));
			ancestors.add(nextAncestor);
			if(currAncestor.equals(nextAncestor)) break;
			else currAncestor = nextAncestor;
		}
		br.close();
		return ancestors;
	}




	public void compare()
	{
		selectedData = selectedTable.getItems();
		Main.showScreen("ResultMatchScreen", "");
	}

}