package controllers;


import java.util.ArrayList;
import entities.Cluster;
import entities.Sequence;
import entities.Vars;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SearchScreenController 
{
	@FXML
	public ComboBox<String> matchType,searchType;
	@FXML
	public Text firstMatchText, bestMatchText;
	@FXML
	public static CheckBox firstMatchCheckBox;
	@FXML
	public CheckBox bestMatchCheckBox;
	@FXML
	public static TextField fromTextField;
	@FXML
	public static TextField toTextField;
	public ArrayList<Integer> clustersIndexes = new ArrayList<Integer>();



	public void initialize()
	{
		firstMatchText.setVisible(false);
		bestMatchText.setVisible(false);
	}

	public void onFirstMatchEntered() {
		firstMatchText.setVisible(true);
	}

	public void onBestMatchEntered() {
		bestMatchText.setVisible(true);
	}
	public void onFirstMatchExited() {
		firstMatchText.setVisible(false);
		bestMatchText.setVisible(false);
	}

	public void onBestMatchExited() {
		firstMatchText.setVisible(false);
		bestMatchText.setVisible(false);
	}

	public void onFirstMatchCheckBox() {
		firstMatchCheckBox.setSelected(true);
		bestMatchCheckBox.setSelected(false);
	}
	public void onBestMatchCheckBox() {
		firstMatchCheckBox.setSelected(false);
		bestMatchCheckBox.setSelected(true);
	}

	public static void onSearch() {

		double maxScore = 0, score=0;
		/*		//Run a loop to look for dnas in the db, get them and run them through the black box and get File pointer
		ArrayList<SequenceAlignment> alignedClustersList = new ArrayList<SequenceAlignment>();
		for(int i=0;i<Math.min((double)Main.clustersIndexes.size(), (double)clustersIndexes.size());++i) {
			ADD TO THE SEQUENCEALIGNMENT INDEX VARIABLES TO KNOW WHICH CLUSTER IT IS REPRESENTING
			alignedClustersList.add(i, new SequenceAlignment());
			alignedClustersList.get(i).calcOptimalAlignment(Main.userSequence,Main.);//Use the indexes on both files and align each cluster against the other
		 */		
		System.out.println("lel");
		Sequence toCompareDNA = null;
		//SAYING WE HAVE ANOTHER SEQUENCE BY NOW(toCompareDNA)
		//TEST HOW TO ADD CLUSTERS
		toCompareDNA = new Sequence("abcbacacbcabcabacabcabcacbacabcacbacabcabcacba");
		toCompareDNA.clusters.add(new Cluster(0,3, toCompareDNA.getDNA().substring(0,3)));//The correct form! ~~~~~~~~~~~~~~~~~ !

		/**          INCORRECT !!!!!!!           **/
		toCompareDNA.clusters.add(new Cluster(4,9, "bca"));
		toCompareDNA.clusters.add(new Cluster(10,17, "cba"));
		toCompareDNA.clusters.add(new Cluster(18,30, "cbc"));
		toCompareDNA.clusters.add(new Cluster(31,35, "aaa"));
		toCompareDNA.clusters.add(new Cluster(36,45, "aba"));

		Vars.userSequence = new Sequence("abcacabcabcabcabcacbacabcacbacbacabcacbabacbca");
		Vars.userSequence.clusters.add(new Cluster(0,3, "abc"));
		Vars.userSequence.clusters.add(new Cluster(4,9, "ccc"));
		Vars.userSequence.clusters.add(new Cluster(10,17, "cba"));
		Vars.userSequence.clusters.add(new Cluster(18,30, "aba"));
		Vars.userSequence.clusters.add(new Cluster(31,35, "aaa"));
		Vars.userSequence.clusters.add(new Cluster(36,45, "aba"));
		//TEST CLUSTERS
		/* CHANGE - USE THE BBOX TO CREATE THE CLUSTERS AND SEQUENCES*/
		//First match
		score = toCompareDNA.getMatchScore();
		if(firstMatchCheckBox.isSelected()) {
			if(score>Integer.parseInt(fromTextField.getText()) && score<Integer.parseInt(toTextField.getText())) {
				System.out.println("Sequence FOUND!");
				Vars.foundSequence = toCompareDNA;
			}
			else
				System.out.println("sequence no found");
		}else if(score>maxScore) maxScore=score;

	}
}