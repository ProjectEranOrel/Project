package entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.SequenceAlignment;

public class Sequence {

	public String dna = "";
	public ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	SequenceAlignment seqAlign = new SequenceAlignment();
	private double matchScore;

	public Sequence(String dna) {
		this.dna = dna;
	}

	public Sequence() {	}
	/**
	 * This function compares 2 sequences by checking their clusters and comparing each character in location k to another character in location k
	 * iff both of them belong to the same cluster family. If they are equal, score++, else continue
	 * @param toCompareDNA Sequence we should compare to the user's dna
	 * @return The score of the comparison
	 */
	public double compare(Sequence toCompareDNA) {
		BufferedWriter pr = null;
		try {
			pr = new BufferedWriter(new FileWriter(new File("test"+ Vars.i++  +".txt"), false));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			@SuppressWarnings("unused")
			int userClusterNum = 0, toCompareClusterNum = 0, startIndex=0, endIndex=0, matchNumber=0, compares =0;
			String cluster1="", cluster2="";
			int count = 0;
			/*           CHECK IF HIDDEN REPEATS OF 2 CLUSTERS ARE EQUAL        */		
			while(toCompareClusterNum<toCompareDNA.clusters.size() && userClusterNum<clusters.size() ) {//Go over all the clusters and check their hidden repeat

				/*WRITE, DELETE LATER */
				pr.write(clusters.get(userClusterNum).getDnaCluster());
				pr.newLine();

				pr.write(toCompareDNA.clusters.get(toCompareClusterNum).getDnaCluster());
				pr.newLine();

				pr.write(clusters.get(userClusterNum).getHiddenRepeat() + " " + toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat());
				pr.newLine();


				/*WRITE, DELETE LATER */

				if(!(Vars.isInSameFamily(clusters.get(userClusterNum).getHiddenRepeat(),toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat()))) 
				{

					if( clusters.get(userClusterNum).getEnd() == toCompareDNA.clusters.get(toCompareClusterNum).getEnd()) 
					{
						System.out.println("ERROR1 "+count);
						userClusterNum++;toCompareClusterNum++;
					}
					else if(clusters.get(userClusterNum).getEnd()>toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					{
						System.out.println("ERROR2 "+count);
						toCompareClusterNum++;
					}
					else if(clusters.get(userClusterNum).getEnd()<toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					{
						System.out.println("ERROR3 "+count);
						userClusterNum++;
					}
					count++;
					continue;
				}



				if(toCompareDNA.clusters.get(toCompareClusterNum).getStart() < clusters.get(userClusterNum).getStart())
					startIndex = clusters.get(userClusterNum).getStart();
				else
					startIndex = toCompareDNA.clusters.get(toCompareClusterNum).getStart();

				if(toCompareDNA.clusters.get(toCompareClusterNum).getEnd() > clusters.get(userClusterNum).getEnd())
					endIndex = clusters.get(userClusterNum).getEnd();
				else
					endIndex = toCompareDNA.clusters.get(toCompareClusterNum).getEnd();



				/*            ALIGN 2 CLUSTERS AND COMPARE THEM          */
				//Clusters hidden repeats are equal, add if if there is a match percentage between the cluster and the hidden repeat
				cluster1 = dna.substring(startIndex, endIndex);
				cluster2 = toCompareDNA.dna.substring(startIndex, endIndex);

				seqAlign.calcOptimalAlignment(cluster1, cluster2);

				cluster1 = seqAlign.seq1Aligned;
				cluster2 = seqAlign.seq2Aligned;


				//The 2 clusters are aligned and ready to be compared
				for(int j=0;j<Math.min((double)cluster1.length(), (double)cluster2.length());j++) 
				{
					compares++;
					if((cluster1.charAt(j)) == (cluster2.charAt(j))) 
						matchNumber++;
				}
				if( clusters.get(userClusterNum).getEnd() == toCompareDNA.clusters.get(toCompareClusterNum).getEnd()) {
					userClusterNum++;
					toCompareClusterNum++;
				}
				else if(clusters.get(userClusterNum).getEnd()>toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					toCompareClusterNum++;
				else if(clusters.get(userClusterNum).getEnd()<toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					userClusterNum++;
				count++;
			}
			System.out.println("mn="+matchNumber);
			System.out.println("len="+toCompareDNA.getDNA().length());
			matchScore = ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100;
			if(matchScore>100)
				matchScore = 100;
			pr.write("\nScore:" + ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100);
			return matchScore;
		}
		catch (Exception e) {
			e.printStackTrace();
			return matchScore;
		}finally 
		{
			try {
				pr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}



	public String getDNA() {
		return dna;
	}
	public void setDNA(String DNA) {
		this.dna = DNA;
	}

	public double getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(double matchScore) {
		this.matchScore = matchScore;
	}



}
