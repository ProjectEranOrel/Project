package Entities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Vars {
	public static final String genBankLink = "ftp://ftp.ncbi.nlm.nih.gov/genomes/";
	public static final String searchLink = "https://www.ncbi.nlm.nih.gov/gene/?Term=";
	// NEED TO BE DONE
	public static final String getGIWithTaxidLink = "https://www.ncbi.nlm.nih.gov/nuccore/?term=txid";
	public static final String taxonomyBrowser = "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=";
	private static final String orthologyLink = "https://www.ncbi.nlm.nih.gov/gene/?Term=ortholog_gene_";
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
	public static ArrayList<Node> nodesList;


	public static String getOrthologyLink(String taxID) {
		return orthologyLink+taxID+"[group]";
	}

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

	public static Sequence compare(String geneID) {
		Sequence seq = setSequence(geneID);
		seq.setMatchScore(Vars.userSequence.compare(seq));
		return seq;
	}

	@SuppressWarnings("resource")
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
				sequence.clusters.add(new Cluster(Integer.parseInt(start), Integer.parseInt(end), 
						sequence.dna.substring(Integer.parseInt(start), Integer.parseInt(end))));
				start = end;
			}
		}catch(Exception e) {e.printStackTrace(); sequence = null;}
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

	public static File trimNodesFile() {
		File file = null;
		try {
			file = new File("nodes.dmp");
			if(!file.isFile()) {
				setTaxDmpFile();
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			PrintWriter writer = new PrintWriter("nodes.txt", "UTF-8");
			String str = "", strToWrite="";
			int i=0;
			while((str = br.readLine()) != null) 
				strToWrite+=str.substring(0, str.indexOf("|", str.indexOf("|")+1)) + "\n";


			writer.write(strToWrite);
			file.delete();

		}catch(Exception e) {e.printStackTrace();}
		return file;
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
	public static void setNodesList(){
		ArrayList<Node> nodesList = new ArrayList<Node>();

		try {
			FileReader fr = new FileReader(new File("nodes.dmp"));
			BufferedReader br = new BufferedReader(fr);
			String str, son, father;
			while((str = br.readLine())!=null) {
				son = str.substring(0, str.indexOf("\t"));
				str = str.substring(son.length()+3);
				father = str.substring(0, str.indexOf("\t"));
				nodesList.add(new Node(son, father));
			}
		}catch (Exception e) {e.printStackTrace();}
	}

}
