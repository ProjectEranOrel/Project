package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;



import Entities.Taxonomy;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.application.*;
public class ResultMatchScreen 
{
	public TextArea selectedFasta;
	public TextArea inputFasta;
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
		/*matchScoreCol.setCellValueFactory(cellData -> 
		new ReadOnlyDoubleWrapper(cellData.getValue().getSequence().getMatchScore()));*/

		selectedTable.getColumns().addAll(selectedIDcol,selectedNameCol,matchScoreCol);
		/*inputFasta.setText(Vars.userSequence.getDNA());
		ObservableList<Taxonomy> selectedData = TreeScreenController.selectedData;
		selectedTable.getItems().addAll(selectedData);
		selectedTable.getSortOrder().add(matchScoreCol);*/
		Thread t1 = new Thread(new Runnable() {
			@Override public void run() {
				while(true)
				{
					int num = new Random().nextInt(200);
					ArrayList<Taxonomy> data = new ArrayList<Taxonomy>();
					data.add(new Taxonomy("GX88"+num,"Solgaleo"+num,false));
					Platform.runLater(new Runnable() 
					{
						@Override public void run() 
						{
							addEntries(data);
						}
					});

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t1.start();
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
