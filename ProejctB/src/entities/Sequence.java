package entities;

import java.io.PrintWriter;
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
		try {
			@SuppressWarnings("unused")
			int userClusterNum = 0, toCompareClusterNum = 0, startIndex=0, endIndex=0, matchNumber=0, compares =0;
			String cluster1="", cluster2="";
			/*           CHECK IF HIDDEN REPEATS OF 2 CLUSTERS ARE EQUAL        */		

			while(toCompareClusterNum<toCompareDNA.clusters.size() && userClusterNum<clusters.size() ) {//Go over all the clusters and check their hidden repeat

				
					if(!(Vars.isInSameFamily(clusters.get(userClusterNum).getHiddenRepeat(),toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat()))) {
					if( clusters.get(userClusterNum).getEnd() == toCompareDNA.clusters.get(toCompareClusterNum).getEnd()) {
						userClusterNum++;toCompareClusterNum++;
					}
					else if(clusters.get(userClusterNum).getEnd()>toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
						toCompareClusterNum++;
					else if(clusters.get(userClusterNum).getEnd()<toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
						userClusterNum++;
					continue;
				}



				if(toCompareDNA.clusters.get(toCompareClusterNum).getStart() < clusters.get(userClusterNum).getStart())
					startIndex = toCompareDNA.clusters.get(toCompareClusterNum).getStart();
				else
					startIndex = clusters.get(userClusterNum).getStart();

				if(toCompareDNA.clusters.get(toCompareClusterNum).getEnd() > clusters.get(userClusterNum).getEnd())
					endIndex = toCompareDNA.clusters.get(toCompareClusterNum).getEnd();
				else
					endIndex = clusters.get(toCompareClusterNum).getEnd();



				/*            ALIGN 2 CLUSTERS AND COMPARE THEM          */

				//Clusters hidden repeats are equal, add if if there is a match percentage between the cluster and the hidden repeat

				cluster1 = dna.substring(startIndex, endIndex);
				cluster2 = toCompareDNA.dna.substring(startIndex, endIndex);

				seqAlign.calcOptimalAlignment(cluster1, cluster2);
				
				cluster1 = seqAlign.seq1Aligned;
				cluster2 = seqAlign.seq2Aligned;



				if( clusters.get(userClusterNum).getEnd() == toCompareDNA.clusters.get(toCompareClusterNum).getEnd()) {
					userClusterNum++;
					toCompareClusterNum++;
				}
				else if(clusters.get(userClusterNum).getEnd()>toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					toCompareClusterNum++;
				else if(clusters.get(userClusterNum).getEnd()<toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
					userClusterNum++;

				//The 2 clusters are aligned and ready to be compared
				for(int j=0;j<Math.min((double)cluster1.length(), (double)cluster2.length());) {
					compares++;
					if((cluster1.charAt(j)) == (cluster2.charAt(j))) 
						matchNumber++;
					if(++j>=Math.min((double)cluster1.length(), (double)cluster2.length()) && ((toCompareClusterNum>=toCompareDNA.clusters.size() || userClusterNum>=clusters.size()))) 
						if((cluster1.charAt(j-1)) == (cluster2.charAt(j-1))) 
							matchNumber++;	
				}
			}

			return ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return matchScore;
		}finally {
		}
		
		//return ((double)matchNumber/(double)compares)*100;

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
