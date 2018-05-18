package controllers;

import java.util.Collection;

import Entities.Taxonomy;
import Entities.Vars;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ResultMatchScreen 
{
	public TextArea selectedFasta,inputFasta;
	public TableView<Taxonomy> selectedTable;
	public static Collection<Taxonomy> extraEntires;
	@SuppressWarnings("unchecked")
	public void initialize()
	{
		TableColumn<Taxonomy,String> selectedIDcol = new TableColumn<>("Taxonomy ID");
		selectedIDcol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getTaxID()));

		TableColumn<Taxonomy, String> selectedNameCol = new TableColumn<>("Organism name");
		selectedNameCol.setCellValueFactory((TableColumn.CellDataFeatures<Taxonomy, String> param) -> 
		new ReadOnlyStringWrapper(param.getValue().getOrganism()));
		
		TableColumn<Taxonomy, Number> matchScoreCol = new TableColumn<>("Match score");
		matchScoreCol.setCellValueFactory(cellData -> 
		new ReadOnlyDoubleWrapper(cellData.getValue().getSequence().getMatchScore()));
		
		selectedTable.getColumns().addAll(selectedIDcol,selectedNameCol,matchScoreCol);
		inputFasta.setText(Vars.userSequence.getDNA());
		ObservableList<Taxonomy> selectedData = TreeScreenController.selectedData;
		selectedTable.getItems().addAll(selectedData);
		selectedTable.getSortOrder().add(matchScoreCol);
	}
	public void onClickSelectedTable()
	{
		Taxonomy chosen = selectedTable.getSelectionModel().getSelectedItem();
		if(chosen!=null)
			selectedFasta.setText(chosen.getSequence().getDNA());
	}
	public void addEntries(Collection<Taxonomy> entires)
	{
		selectedTable.getItems().addAll(entires);
		selectedTable.setItems(selectedTable.getItems());
	}
}
