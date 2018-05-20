package Entities;

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

		int clusterNum = 0, matchNumber=0, t=0;
                                 /*           CHECK IF HIDDEN REPEATS OF 2 CLUSTERS ARE EQUAL        */		
		for(int i=0;i<Math.min((double)clusters.size(), (double)(toCompareDNA.clusters).size());++i) {//Go over all the clusters and check their hidden repeat
			if(!(clusters.get(clusterNum).getHiddenRepeat().equals(toCompareDNA.clusters.get(clusterNum).getHiddenRepeat()))) 
				clusterNum++;
			
		                     	/*            ALIGN 2 CLUSTERS AND COMPARE THEM          */
			
			else {//Clusters hidden repeats are equal, add if if there is a match percentage between the cluster and the hidden repeat
				t++;
				String cluster1 = dna.substring(clusters.get(clusterNum).getStart(), clusters.get(clusterNum).getEnd());
				String cluster2 = toCompareDNA.dna.substring(toCompareDNA.clusters.get(clusterNum).getStart(), clusters.get(clusterNum).getEnd());
			
				
				seqAlign.calcOptimalAlignment(cluster1, cluster2);
				cluster1 = seqAlign.seq1Aligned;
				cluster2 = seqAlign.seq2Aligned;

				int temp = 0;
				//The 2 clusters are aligned and ready to be compared
				for(int j=0;j<Math.min((double)cluster1.length(), (double)cluster2.length());++j) 
					if((cluster1.charAt(j)) == (cluster2.charAt(j))) {
						matchNumber++;
						temp++;
					}
				System.out.println("cluster 1: " + cluster1 );
				System.out.println("cluster 2: " + cluster2 );
				System.out.println("cluster size: " + cluster1.length());
				System.out.println("matchNumber: " + temp);
				clusterNum++;
			}//else
		}
		System.out.println("matchnum: " + matchNumber + "    minSize: " + Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length()));
		System.out.println("timesRight: " + t + "   clusterNum: " + toCompareDNA.clusters.size());
		System.out.println("DNA Length: " + toCompareDNA.dna.length());
		//return ((double)matchNumber/Math.min((double)DNA.length(), (double)(toCompareDNA.getDNA()).length()))*100;
		return ((double)matchNumber/(Math.min((double)dna.length(), (double)(toCompareDNA.getDNA()).length())))*100;
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
