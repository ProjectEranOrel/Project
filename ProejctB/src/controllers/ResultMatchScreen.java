package controllers;


import java.util.ArrayList;

import entities.Sequence;
import entities.Taxonomy;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;


public class ResultMatchScreen 
{
	@FXML
	public TableView<Taxonomy> resTable;


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
						if(!empty)
							if (item.getMatchScore()==-1)
								this.setText("No DNA sequence was found"); 
							else 
								this.setText(""+item.getMatchScore());

					}
				};
				return cell;
			}

		});
		resTable.getColumns().addAll(IDCol,nameCol,scoreCol);
	}

	public void setItems(ArrayList<Taxonomy> items)
	{
		resTable.getItems().setAll(items);
	}

}
