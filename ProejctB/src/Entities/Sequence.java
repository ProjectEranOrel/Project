package Entities;

import java.util.ArrayList;

import controllers.SequenceAlignment;

public class Sequence {

	private String DNA;
	public ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	SequenceAlignment seqAlign = new SequenceAlignment();
	private double matchScore;
	
	public Sequence(String DNA) {
		this.DNA = DNA;
	}

	public Sequence() {	}

	public void compare(Sequence toCompareDNA) {

		int clusterNum = 0, matchNumber=0;
                                 /*           CHECK IF HIDDEN REPEATS OF 2 CLUSTERS ARE EQUAL        */
		System.out.println("sizes : "  + (double)clusters.size() + "  "  +  (double)(toCompareDNA.clusters).size());
		
		for(int i=0;i<Math.min((double)clusters.size(), (double)(toCompareDNA.clusters).size());++i) {//Go over all the clusters and check their hidden repeat
			if(!(clusters.get(clusterNum).getHiddenRepeat().equals(toCompareDNA.clusters.get(clusterNum).getHiddenRepeat()))
					|| (i==clusters.get(clusterNum).getEnd())) 
				clusterNum++;
			
		                     	/*            ALIGN 2 CLUSTERS AND COMPARE THEM          */
			
			else {//Clusters hidden repeats are equal, add if if there is a match percentage between the cluster and the hidden repeat
				String cluster1 = DNA.substring(clusters.get(clusterNum).getStart(), clusters.get(clusterNum).getEnd());
				String cluster2 = toCompareDNA.DNA.substring(toCompareDNA.clusters.get(clusterNum).getStart(), clusters.get(clusterNum).getEnd());

				seqAlign.calcOptimalAlignment(cluster1, cluster2);

				cluster1 = seqAlign.seq1Aligned;
				cluster2 = seqAlign.seq2Aligned;
				
				System.out.println("cluster1: " + cluster1 + " cluster2: " + cluster2);
				//The 2 clusters are aligned and ready to be compared
				for(int j=0;j<Math.min((double)cluster1.length(), (double)cluster2.length());++j) 
					if((cluster1.charAt(j)) == (cluster2.charAt(j)))
						matchNumber++;
				
				clusterNum++;
			}//else
		}
		System.out.println("Its good: "  + ((double)matchNumber/Math.max((double)DNA.length(), (double)(toCompareDNA.getDNA()).length()))*100);
		//return ((double)matchNumber/Math.min((double)DNA.length(), (double)(toCompareDNA.getDNA()).length()))*100;
		toCompareDNA.matchScore = ((double)matchNumber/Math.min((double)DNA.length(), (double)(toCompareDNA.getDNA()).length()))*100;
	}

	public String getDNA() {
		return DNA;
	}
	public void setDNA(String DNA) {
		this.DNA = DNA;
	}

	public double getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(double matchScore) {
		this.matchScore = matchScore;
	}



}
