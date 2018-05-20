package Entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Vars {
	public static final String genBankLink = "ftp://ftp.ncbi.nlm.nih.gov/genomes/";
	public static final String searchLink = "https://www.ncbi.nlm.nih.gov/gene/?Term=";
	// NEED TO BE DONE
	public static final String getGIWithTaxidLink = "https://www.ncbi.nlm.nih.gov/nuccore/?term=txid";
	public static final String taxonomyBrowser = "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=";
	// NEED TO BE DONE
	public static String searchWord = null;
	public static Sequence userSequence = null;
	public static Sequence foundSequence;
	public static ArrayList<Integer> clustersIndexes = new ArrayList<Integer>();
	private static File userDNAFile;//Get a pointer to the user's DNA file
	public static boolean isUserDNA = true;//This flag will say whether it's the user's DNA that we're looking for or a comparison DNA
	public static String desktopPath;
	public static String noResultsString = "The following term was not found in Gene";
	public static String firstScreenTitle = "First";
	public static String getResultsScreenTitle = "Results";
	public static String resultsScreenScreenTitle = "Results";
	public static String searchScreenTitle = "Search";
	public static File resultsFile;
	public static Result userResult;//The entry the user chose
	public static Taxonomy root = null;


	//Vars.root = func(userResult.getTaxID());
	@SuppressWarnings({ "resource", "finally" })
	public static String readDNAFile() {
		BufferedReader br = null;
		FileReader fr = null;
		String dna="";
		try {
			fr = new FileReader(userDNAFile);
			br = new BufferedReader(fr);

			br.readLine();//Skip first row - garbage there
			while((dna+=br.readLine())!=null);
		} catch (IOException e) {e.printStackTrace();}
		finally {
			return dna;
		}
	}

	public static void setUserDNAFile(File file) {
		userDNAFile = file;
	}

	public static File getUserDNAFile() {
		return userDNAFile;
	}

	public static File getDNAByGI(String gi)
	{
		String[] cmdArray = new String[3];
		cmdArray[0] = "C:\\Perl64\\bin\\perl";
		cmdArray[1] = "getacc.pl";
		cmdArray[2] = gi;
		try {
			Runtime.getRuntime().exec(cmdArray).waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = null;
		try {
			file = new File("acc_num.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st = br.readLine();
			br.close();
			int index = st.indexOf(".");
			cmdArray[1] = "getFasta.pl";
			cmdArray[2] = st.substring(0,index);
			Runtime.getRuntime().exec(cmdArray).waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			//file.delete();
		}
		return new File("dna.txt");
	}

	public static File blackBox(String filePath)
	{
		ProcessBuilder launcher = new ProcessBuilder("main.exe",filePath);
		try {
			Process p = launcher.start();
			p.waitFor();//Waiting for the process to finish running
		} 
		catch (IOException | InterruptedException  e) 
		{
			e.printStackTrace();
			System.out.println("Please check if the executable is located in your desktop");
		}
		return new File("clusters.txt");
	}

	public static Sequence setSequence(String geneID) {
		FileReader fr;
		BufferedReader br;
		Sequence sequence = new Sequence();
		try {
			File dnaFile = getDNAByGI(geneID);
			fr = new FileReader(dnaFile);
			br = new BufferedReader(fr);

			if(!isErrorDNA(dnaFile)) {
				System.out.println("bad sequence! Bad GI");
				sequence.setDNA("bad dna");return sequence;
			}
			br.readLine();//First line is junk
			/*        DNA          */
			String string = "";
			while((string=br.readLine())!=null) 
				sequence.dna+= string;

			/*        Clusters       */
			fr = new FileReader(blackBox(dnaFile.getPath()));

			br = new BufferedReader(fr);
			String start=br.readLine()/*=0*/, end;
			while((end = br.readLine()) != null) {
				if(end.equals("3306"))
					System.out.println("DAMN BOY");
				sequence.clusters.add(new Cluster(Integer.parseInt(start), Integer.parseInt(end), 
						sequence.dna.substring(Integer.parseInt(start), Integer.parseInt(end))));
				start = end;
			}
			/*			for(int i=0;i<sequence.clusters.size();i++)
				System.out.println(sequence.clusters.get(i).getDnaCluster() + "        hidden:" + sequence.clusters.get(i).getHiddenRepeat());
			 */		}catch(Exception e) {e.printStackTrace(); sequence = null;}
		System.out.println("cluster size: " + sequence.clusters.size());
		return sequence;
	}

	@SuppressWarnings("resource")
	public static boolean isErrorDNA(File dnaFile) {
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(dnaFile); 
			br = new BufferedReader(fr);
			String currLine = "";
			while((currLine = br.readLine()) != null)
				if(currLine.toLowerCase().contains("error"))
					return false;
			return true;
		} catch (IOException e) {e.printStackTrace(); return false;}

	}


}
