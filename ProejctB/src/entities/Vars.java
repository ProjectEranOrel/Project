package entities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Vars {

	public static final String genBankLink = "ftp://ftp.ncbi.nlm.nih.gov/genomes/";
	public static final String searchLink = "https://www.ncbi.nlm.nih.gov/gene/?Term=";
	public static final String orthoSearchLink = "https://www.ncbi.nlm.nih.gov/gene/";
	// NEED TO BE DONE
	public static final String getGIWithTaxidLink = "https://www.ncbi.nlm.nih.gov/nuccore/?term=txid";
	public static final String taxonomyBrowser = "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=";
	// NEED TO BE DONE
	public static String searchWord = null;
	public static Sequence userSequence = null;
	public static Sequence foundSequence;
	public static ArrayList<Integer> clustersIndexes = new ArrayList<Integer>();
	private static File userDNAFile = null;//Get a pointer to the user's DNA file
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
	public static String[] nodesArray = null;
	public static String[][] families = new String[20][6];
	public static int i=0;

	//Vars.root = func(userResult.getTaxID());

	public static void setFamilies() {
		families[0][0] = "AGT";families[0][3] = "ATG";
		families[0][1] = "GAT";families[0][4] = "GTA";
		families[0][2] = "TGA";families[0][5] = "TAG";

		families[1][0] = "AGC";families[1][3] = "ACG";
		families[1][1] = "GAC";families[1][4] = "GCA";
		families[1][2] = "CGA";families[1][5] = "CAG";

		families[2][0] = "ACT";families[2][3] = "ATC";
		families[2][1] = "CAT";families[2][4] = "CTA";
		families[2][2] = "ACT";families[2][5] = "ATC";

		families[3][0] = "GTC";families[3][3] = "GCT";
		families[3][1] = "TGC";families[3][4] = "TCG";
		families[3][2] = "CGT";families[3][5] = "CTG";
		//cc
		families[4][0] = "CGC";families[4][1] = "CCG";
		families[4][2] = "GCC";

		families[5][0] = "CTC";families[5][1] = "CCT";
		families[5][2] = "TCC";

		families[6][0] = "CAC";families[6][1] = "CCA";
		families[6][2] = "ACC";
		//gg
		families[7][0] = "GAG";families[7][1] = "GGA";
		families[7][2] = "AGG";

		families[8][0] = "GTG";families[8][1] = "GGT";
		families[8][2] = "TGG";

		families[9][0] = "GCG";families[9][1] = "GGC";
		families[9][2] = "CGG";
		//aa
		families[10][0] = "AGA";families[10][1] = "AAG";
		families[10][2] = "AGG";

		families[11][0] = "ATA";families[11][1] = "AAT";
		families[11][2] = "TAA";

		families[12][0] = "ACA";families[12][1] = "AAC";
		families[12][2] = "CAA";
		//tt
		families[13][0] = "TCT";families[13][1] = "TTC";
		families[13][2] = "CTT";

		families[14][0] = "TAT";families[14][1] = "TTA";
		families[14][2] = "ATT";

		families[15][0] = "TGT";families[15][1] = "TTG";
		families[15][2] = "GTT";

		families[16][0]="TTT";
		families[17][0]="AAA";
		families[18][0]="GGG";
		families[19][0]="CCC";

	}

	public static boolean isInSameFamily(String hidden1, String hidden2) {
		int hidden1Family = 0;
		loop:
			for(int row=0;row<20;row++)
				for(int col=0;col<6;col++)
					if(families[row][col] == null) break;
					else if(hidden1.equals(families[row][col])) {
						hidden1Family = row;break loop;
					}
		for(int i=0;i<6;++i) 
			if(hidden2.equals(families[hidden1Family][i])) 
				return true;

		return false;
	}

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

	public static int findInOrthology(String taxID)
	{
		ArrayList<Result> resultList = Result.orthology;
		for(int i=0;i<resultList.size();i++)
			if(resultList.get(i).getTaxID().equals(taxID))
				return i;
		return -1;
	}

	public static void setUserDNAFile(File file) {
		userDNAFile = file;
	}

	public static File getUserDNAFile() {
		return userDNAFile;
	}

	public static File getDNAByGI(String gi)
	{
		System.out.println("lelelel");
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
			/*String st = br.readLine();
			br.close();
			//System.out.println("accession number: "+st);
			int index = st.indexOf(".");
			if(index>-1)
				cmdArray[2] = st.substring(0,index);
			else cmdArray[2] = st;
			cmdArray[1] = "getFasta.pl";*/
			String st;
			while((st=br.readLine())!=null)//We don't know in what line the accession number is found
			{
				System.out.println(st);
				int index = st.indexOf("_");
				//Making sure we found the accession number
				if(index>-1 && isAccessionNumber(st, index))
				{
					cmdArray[2] = st.substring(index-2, index+7);
					break;
				}
			}
			br.close();
			cmdArray[1] = "getFasta.pl";
			System.out.println("Accession number: "+cmdArray[2]);
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



	private static boolean isAccessionNumber(String st,int index)
	{
		if(!(Character.isLetter(st.charAt(index-1)) && Character.isLetter(st.charAt(index-2)) && st.charAt(index-3)==' '))
			return false;
		for(int i=1;i<7;i++)
		{
			if(st.length()==index+i)//It's too short
				return false;
			System.out.println(st.charAt(index+i));
			if(!Character.isDigit(st.charAt(index+i)))
				return false;
		}
		return true;
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

	public static Sequence compare(String geneID) {
		Sequence seq = setSequence(geneID);
		seq.setMatchScore(Vars.userSequence.compare(seq));
		return seq;
	}

	@SuppressWarnings("resource")
	public static Sequence setSequence(String geneID) {
		System.out.println("setSequence");
		FileReader fr;
		BufferedReader br;
		File dnaFile;
		Sequence sequence = new Sequence();
		try {
			if(geneID.equals("userDNA")) 
				dnaFile = Vars.userDNAFile;

			else
				dnaFile = getDNAByGI(geneID);
			fr = new FileReader(dnaFile);
			br = new BufferedReader(fr);

			if(!isErrorDNA(dnaFile)) {
				System.out.println("bad sequence! Bad GI");
				sequence.setDNA("bad dna");return sequence;
			} 
			String x;

			do
				if((x = br.readLine()) == null)
					{System.out.println("damn");  return null;}
			while(!isLineDNA(x));//Get to the row where the DNA sequence begins

			/*        DNA          */
			String string = "";
			while((string=br.readLine())!=null) 
				sequence.dna+= string;

			/*        Clusters       */
			fr = new FileReader(blackBox(dnaFile.getPath()));
			br = new BufferedReader(fr);



			String start=br.readLine()/*=0*/, end;
			while((end = br.readLine()) != null) {
				sequence.clusters.add(new Cluster(Integer.parseInt(start), Integer.parseInt(end), 
						sequence.dna.substring(Integer.parseInt(start), Integer.parseInt(end))));
				start = end;
			}
		}catch(Exception e) {e.printStackTrace(); sequence = null;}
		return sequence;
	}

	public static boolean isLineDNA(String line) {
		if(!line.matches("[AGCT]+") || !line.matches("[agct]+"))
			return false;
		return true;
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


	public static void setTaxDmpFile() {
		downloadFile("taxdmp.zip","ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/taxdmp.zip");
		extractFromZip("nodes.dmp", "taxdmp.zip", true);
	}


	public static void downloadFile(String fileName, String fileUrl){
		try {
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try {
				in = new BufferedInputStream(new URL(fileUrl).openStream());
				fout = new FileOutputStream(fileName);

				byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fout.write(data, 0, count);
				}
			} finally {
				if (in != null)
					in.close();
				if (fout != null)
					fout.close();
			}
		}catch(Exception e) {e.printStackTrace();}
	}

	public static void extractFromZip(String fileToBeExtracted, String zipPackage, boolean toDelete) {
		try {
			OutputStream out = new FileOutputStream(fileToBeExtracted);
			FileInputStream fileInputStream = new FileInputStream(zipPackage);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream );
			ZipInputStream zin = new ZipInputStream(bufferedInputStream);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.getName().equals(fileToBeExtracted)) {
					byte[] buffer = new byte[9000];
					int len;
					while ((len = zin.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					break;
				}
			} 
			zin.close();
			if(toDelete) {
				File file = new File(zipPackage);
				file.delete();
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	public static void setNodesArray(){
		ArrayList<Node> nodesList = new ArrayList<Node>();
		BufferedReader br = null;
		int max = 0;
		try {
			FileReader fr = new FileReader(new File("nodes.dmp"));
			br = new BufferedReader(fr);
			String str, son, father;
			while((str = br.readLine())!=null) {
				son = str.substring(0, str.indexOf("\t"));
				str = str.substring(son.length()+3);
				father = str.substring(0, str.indexOf("\t"));
				nodesList.add(new Node(son, father));
				if(Integer.parseInt(son)>=Integer.parseInt(father)) {
					if(Integer.parseInt(son)>max)
						max=Integer.parseInt(son);}
				else
					if(Integer.parseInt(father)>max)
						max = Integer.parseInt(father);
			}
			if(max == 0) {
				System.out.println("File is empty or non existent!"); return;}
			System.out.println(max);
			nodesArray = new String[max+1];
			Arrays.fill(nodesArray, null);
			for(int i=0;i<nodesList.size();++i)
				nodesArray[Integer.parseInt(nodesList.get(i).getSonTaxID())] = nodesList.get(i).getFatherTaxID();

		}catch (Exception e) {e.printStackTrace();}
		finally {try {br.close();} catch (IOException e) {e.printStackTrace();}}
	}

	/*	public static ArrayList<String> findAncestors(String taxID)
	{
		if(nodesArray == null)
			Vars.setNodesArray();
		//ArrayList<Node> nodesList = Vars.nodesList;
		ArrayList<String> ancestors = new ArrayList<String>();
		//String currAncestor = parentColumn.get(sonColumn.indexOf(taxID));
		int index = getSonIndex(Vars.nodesList,taxID);
		if(index==-1) return null;
		String currAncestor = Vars.nodesList.get(index).getFatherTaxID();
		String nextAncestor = null;
		ancestors.add(currAncestor);
		while(true)
		{
			//nextAncestor = parentColumn.get(sonColumn.indexOf(currAncestor));
			index = getSonIndex(Vars.nodesList,currAncestor);
			if(index==-1) return ancestors;
			nextAncestor = Vars.nodesList.get(index).getFatherTaxID();
			if(currAncestor.equals(nextAncestor)) break;
			ancestors.add(nextAncestor);
			currAncestor = nextAncestor;
		}
		return ancestors;
	}*/


}