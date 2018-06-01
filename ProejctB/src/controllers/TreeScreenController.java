package controllers;

import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

import Entities.Result;
import Entities.Sequence;
import Entities.Taxonomy;
import Entities.Vars;
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
	private ArrayList<TreeItem<Taxonomy>> chosenItemHeritage = new ArrayList<TreeItem<Taxonomy>>();

	private int startIndex, endIndex;

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
		{
			populateTree(chosen,ParseSourceCode.getSons(chosen.getValue()).getSons(),2);
			for(int i=0;i<resultList.size();i++)
			{
				for(int j=0;j<chosenItemHeritage.size();j++)
					if(resultList.get(i).ancestors.contains(chosenItemHeritage.get(j).getValue().getTaxID()))
					{
						/*ObservableList<TreeTableColumn<Taxonomy, ?>> columns = treeTable.getColumns();
						for(int l=0;l<columns.size();l++)
						{
							columns.get(l).setR
							treeTable.
						}*/
						//treeTable.getRow
						//System.out.println(i+": "+chosenItemHeritage.get(j).getValue().getTaxID(


					}
			}
			chosenItemHeritage.removeAll(chosenItemHeritage);
		}
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
			chosenItemHeritage.add(sonItem);
		}
	}

	private void createAndStartThreads(Taxonomy son)
	{
		int numOfEntries = 20;
		while(((double)resultList.size()/(double)numOfEntries)!=resultList.size()/numOfEntries)
			numOfEntries++;
		int numOfThreads = (resultList.size()/numOfEntries)-1;
		Thread[] markingThreads = new Thread[numOfThreads];
		/*System.out.println("numOfEntires: "+numOfEntries);
  System.out.println("numOfThreads: "+numOfThreads);*/
		startIndex = 0;
		endIndex = numOfEntries;
		long startTime = System.nanoTime();
		for(int i=0;i<resultList.size();i++)
		{
			if(Vars.findAncestors(resultList.get(i).getTaxID()).contains(son.getTaxID()))
			{
				//System.out.println("Found something...");
				ObservableList<TreeTableColumn<Taxonomy, ?>> columns = treeTable.getColumns();
				//System.out.println("Going thrrough the columns");
				for(int l=0;l<columns.size();l++)
				{
					System.out.println("Column"+l);
					columns.get(l).setStyle("-fx-background-color:#558C8C");
				}
			}
		}


		/*for(int j=0;j<numOfThreads;j++)
  {
   System.out.println("Thread "+j);
   System.out.println("startIndex:" + startIndex);
   System.out.println("endIndex:" + endIndex+"\n");
   markingThreads[j] = new Thread(new Runnable()
   {
    @Override
    public void run() 
    {
     int localStartIndex = startIndex;
     int localEndIndex = endIndex;
     for(int k=localStartIndex;k<localEndIndex;k++)
     {
      try 
      {
       if(findAncestors(resultList.get(k).getTaxID()).contains(son.getTaxID()))
       {
        //System.out.println("Found something...");
        ObservableList<TreeTableColumn<Taxonomy, ?>> columns = treeTable.getColumns();
        //System.out.println("Going thrrough the columns");
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
   startIndex = endIndex;
   endIndex += numOfEntries;
  }
  for(int j=0;j<numOfThreads;j++)
   try {
    //System.out.println(numOfThreads);
    markingThreads[j].join();
   } catch (InterruptedException e) {
    System.out.println("j: "+j+"\nresultList: "+resultList.size());
    e.printStackTrace();
   }*/
		long endTime = System.nanoTime();
		System.out.println("Time: " + TimeUnit.SECONDS.convert((endTime-startTime), TimeUnit.NANOSECONDS) + "s");

	}
	public void compare()
	{
		selectedData = selectedTable.getItems();
		ArrayList<Taxonomy> itemsToBeCompared = new ArrayList<Taxonomy>();
		int i;
		for(i=0;i<selectedData.size();i++)
		{
			Taxonomy item = selectedData.get(i);
			for(int j=0;j<resultList.size();j++)
				if(resultList.get(j).getTaxID().equals(item.getTaxID()))
				{
					System.out.println("GeneID: "+resultList.get(j).getGeneID());
					System.out.println("TaxID: "+item.getTaxID());
					item.setSequence(Vars.setSequence(resultList.get(j).getGeneID()));
					if(item.getSequence().getDNA().equals("bad dna"))
						item.getSequence().setMatchScore(-1);	
					else item.getSequence().setMatchScore(new Sequence(Vars.userResult.getTaxID()).compare(item.getSequence()));
					System.out.println(item.getSequence().getMatchScore());
					itemsToBeCompared.add(item);
					if(item.isExpandable())
						itemsToBeCompared.addAll(getChildren(item,new ArrayList<Taxonomy>()));
					break;
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
		} catch(Exception e) {
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