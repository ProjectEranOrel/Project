package controllers;


import java.util.ArrayList;

import entities.Result;
import entities.Sequence;
import entities.Taxonomy;
import entities.Vars;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;


public class ResultMatchScreen 
{
	@FXML
	public TableView<Taxonomy> resTable;
	ArrayList<Taxonomy> itemsToBeCompared;
	private ArrayList<Result> resultList;
	private Thread setter;

	@SuppressWarnings("unchecked")
	public void initialize()
	{
		TableColumn<Taxonomy, String> IDCol = new TableColumn<>("Tax ID");
		IDCol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getTaxID()));

		TableColumn<Taxonomy, String> nameCol = new TableColumn<>("Organism name");
		nameCol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getOrganism()));

		TableColumn<Taxonomy, Sequence> scoreCol = new TableColumn<>("Match score");
		scoreCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Sequence>(cellData.getValue().getSequence()));
		scoreCol.setCellFactory(new Callback<TableColumn<Taxonomy, Sequence>,TableCell<Taxonomy, Sequence>>()
		{
			@Override
			public TableCell<Taxonomy, Sequence> call(TableColumn<Taxonomy, Sequence> arg0) 
			{
				final TableCell<Taxonomy, Sequence> cell = new TableCell<Taxonomy, Sequence>()
				{
					@Override
					public void updateItem(final Sequence item, boolean empty)
					{
						super.updateItem(item, empty);
						//if(!empty)
							if (!empty && item.getMatchScore()==-1)
								this.setText("No DNA sequence was found"); 
							else if(!empty)
								this.setText(""+item.getMatchScore());
					}
				};
				return cell;
			}

		});
		resTable.getColumns().addAll(IDCol,nameCol,scoreCol);
		//resTable.getItems().removeAll(resTable.getItems());
		//resultList = Result.orthology;
		//itemsToBeCompared = new ArrayList<Taxonomy>();
		/*setter = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{

				for(int i=0;i<itemsToBeCompared.size();i++)
				{
					Taxonomy item = itemsToBeCompared.get(i);
					int index = Vars.findInOrthology(item.getTaxID());
					if(index>-1)
					{
						item.setSequence(Vars.compare(resultList.get(index).getGeneID()));
						if(item.getSequence().getDNA().equals("bad dna"))
							item.getSequence().setMatchScore(-1);
						else item.getSequence().setMatchScore(Vars.userSequence.compare(item.getSequence()));
						resTable.getItems().add(item);
					}
				}



			}

		});*/

	}

	public void setItems(ArrayList<Taxonomy> itemsToBeCompared)
	{
		//this.itemsToBeCompared.addAll(itemsToBeCompared);
		resTable.getItems().addAll(itemsToBeCompared);
		//System.out.println("Thread started...");
		//setter.start();

	}

}
