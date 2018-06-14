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

	public double compare(Sequence toCompareDNA) {
		PrintWriter pr = null;
		try {
			pr = new PrintWriter("test"+ Vars.i++  +".txt", "UTF-8");
			@SuppressWarnings("unused")
			int userClusterNum = 0, toCompareClusterNum = 0, startIndex=0, endIndex=0, matchNumber=0, compares =0;
			String cluster1="", cluster2="";
			/*           CHECK IF HIDDEN REPEATS OF 2 CLUSTERS ARE EQUAL        */		
			if(toCompareClusterNum<toCompareDNA.clusters.size() && userClusterNum<clusters.size())
				pr = new PrintWriter("test"+ Vars.i++  +".txt", "UTF-8");

			while(toCompareClusterNum<toCompareDNA.clusters.size() && userClusterNum<clusters.size() ) {//Go over all the clusters and check their hidden repeat
				/*WRITE, DELETE LATER */
				pr.write(clusters.get(userClusterNum).getDnaCluster() + "\n" + toCompareDNA.clusters.get(toCompareClusterNum).getDnaCluster() + "\n" +
						clusters.get(userClusterNum).getHiddenRepeat() + "	" + toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat() + "\n");
				/*WRITE, DELETE LATER */
				
				/*if(!(clusters.get(userClusterNum).getHiddenRepeat().equals(toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat()))) {*/
					if(!(Vars.isInSameFamily(clusters.get(userClusterNum).getHiddenRepeat(),toCompareDNA.clusters.get(toCompareClusterNum).getHiddenRepeat()))) {
					if( clusters.get(userClusterNum).getEnd() == toCompareDNA.clusters.get(toCompareClusterNum).getEnd()) {
						userClusterNum++;toCompareClusterNum++;
					}
					else if(clusters.get(userClusterNum).getEnd()>toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
						toCompareClusterNum++;
					else if(clusters.get(userClusterNum).getEnd()<toCompareDNA.clusters.get(toCompareClusterNum).getEnd())
						userClusterNum++;
					continue;
				}//IF to continue



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

			System.out.println("lel");
			pr.write("\nScore:" + ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100);
			return ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return matchScore;
		}finally {
			pr.close();
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
