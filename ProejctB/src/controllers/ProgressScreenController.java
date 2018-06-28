package controllers;

import java.util.ArrayList;

import entities.Result;
import entities.Taxonomy;
import entities.Vars;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
/*hggfhgh**/




public class ProgressScreenController {
	public ProgressBar pb = new ProgressBar(0.0);
	//public ProgressIndicator pi = new ProgressIndicator();
	ArrayList<Taxonomy> itemsToBeCompared = new ArrayList<Taxonomy>();
	ArrayList<Taxonomy> alreadyCompared = new ArrayList<Taxonomy>();
	Stage stage;
	private int i;

	public void initialize()
	{
		ArrayList<Result> resultList = Result.orthology;
		Task<Void> task = new Task<Void>()
		{

			@Override
			protected Void call() throws Exception 
			{
				updateProgress(0, itemsToBeCompared.size()-1);
				if(Vars.userSequence==null)
					Vars.userSequence = Vars.setSequence(Vars.userResult.getGeneID());
				for(i=0;i<itemsToBeCompared.size();i++)
				{
					Taxonomy item = itemsToBeCompared.get(i);
					int index = Vars.findInOrthology(item.getTaxID());
					if(index>-1)
					{
						item.setSequence(Vars.setSequence(resultList.get(index).getGeneID()));
						//item.setSequence(Vars.compare(resultList.get(index).getGeneID()));
						if(item.getSequence().getDNA().equals("bad dna"))
							item.getSequence().setMatchScore(-1);
						else 
						{ 
							item.getSequence().setMatchScore(Vars.userSequence.compare(item.getSequence())); 
							System.out.println(item.getSequence().getMatchScore());
						}
						alreadyCompared.add(item);
					}
					updateProgress(i, itemsToBeCompared.size()-1);	
				}
				Platform.runLater(new Runnable() {
					@Override public void run() 
					{
						try 
						{
							stage.close();
							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ResultMatchScreen.fxml"));
							Parent root1 = (Parent) fxmlLoader.load();
							ResultMatchScreen controller = fxmlLoader.getController();
							controller.setItems(new ArrayList<Taxonomy>(alreadyCompared));
							Stage stage = new Stage();
							stage.setScene(new Scene(root1));
							stage.show();
						} 
						catch(Exception e) 
						{
							e.printStackTrace();
						}
					}
				});
				return null;
			}

		};
		pb.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
	}

}
