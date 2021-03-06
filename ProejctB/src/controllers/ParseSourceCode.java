package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import entities.Result;
import entities.Taxonomy;
import entities.Vars;

public class ParseSourceCode {

	private static final String son = "<UL COMPACT>";
	private static final String father = "</UL>";
	private static final String notExpandable = "square";


	
	/**
	 * This function parses the source code of the tax id's lineage into an arraylist of it's lineage
	 * @param taxID the taxonomy ID of the node we are currently working on
	 * @return returns an arraylist of all the lineage of the node
	 */
	public static ArrayList<Taxonomy> getLineage(String taxID) { 
		//URLConnection conn;
		ArrayList<Taxonomy> taxList = new ArrayList<Taxonomy>();
		Taxonomy tax;
		Taxonomy taxSelected = new Taxonomy();
		try {
			BufferedReader br = getBufferedReader(Vars.taxonomyBrowser + taxID);
			String lineage = null;
			while((lineage = br.readLine())!=null)
				if(lineage.contains("lineage")) break;

			if(!lineage.contains("STRONG")) 
				return getLineageModifier(br);



			/*              GET INFO ABOUT THE ORGANISM WE'RE SPECTATING          */
			//TAXID
			taxSelected.setLink(lineage.substring(lineage.indexOf("id=", lineage.indexOf("</STRONG>")) + 3,
					lineage.indexOf("&lvl",lineage.indexOf("</STRONG>"))));
			//ORGANISM
			taxSelected.setOrganism(lineage.substring(lineage.indexOf(">", lineage.indexOf("ALT="))+1, 
					lineage.indexOf("<", lineage.indexOf("ALT="))));
			/*              GET INFO ABOUT THE ORGANISM WE'RE SPECTATING          */

			int index = 3;//First ahref is irrelevant
			while(lineage.indexOf("HREF") != -1) {
				tax = new Taxonomy();
				lineage = lineage.substring(index);
				index = lineage.indexOf("HREF");
				if(index == -1)
					break;
				//TAXID
				lineage = lineage.substring(index);
				tax.setLink(lineage.substring(lineage.indexOf("id=") + 3, lineage.indexOf("&lvl")));
				//ORGANISM
				index = lineage.indexOf("ALT");
				lineage = lineage.substring(index);
				index = lineage.indexOf(">") + 1;//Skip the '>'
				tax.setOrganism(lineage.substring(index, lineage.indexOf("<")));
				tax.setExpandable(true);
				/*				if(Vars.userResult == null)
					Vars.userResult = new Result();*/
				taxList.add(tax);
				int nextTax;
				boolean isDone = false;
				for(int i=0;i<Result.orthology.size()&&!isDone;++i) {
					nextTax = Integer.parseInt(Result.orthology.get(i).getTaxID());
					while(Vars.nodesArray[nextTax] != null) 
						if(Vars.nodesArray[nextTax].equals(tax.getTaxID()) && !tax.getOrganism().contains("*")) {
							tax.setOrganism("*"+tax.getOrganism()+"*");isDone=true;break;
						}
						else {
							nextTax = Integer.parseInt(Vars.nodesArray[nextTax]);
							if(nextTax ==  Integer.parseInt(Vars.nodesArray[nextTax])) break;
						}
				}//for
			}//outer while
			taxList.set(0, taxSelected);

		}catch(Exception e) {e.printStackTrace();}

		return taxList;
	}



/**
	 * This function parses the source code of the tax id's lineage into an arraylist of it's lineage
	 * @param taxID the taxonomy ID of the node we are currently working on
	 * @return returns an arraylist of all the lineage of the node
 */
	public static ArrayList<Taxonomy> getLineageModifier(BufferedReader br){
	
		ArrayList<Taxonomy> taxList = new ArrayList<Taxonomy>();
		Taxonomy tax;

		try {	
			String lineage = br.readLine();
			int index = 3;//First ahref is irrelevant
			while(lineage.indexOf("href") != -1) {
				tax = new Taxonomy();
				index = lineage.indexOf("href");
				if(index == -1)
					break;
				//TAXID
				lineage = lineage.substring(index);
				tax.setLink(lineage.substring(lineage.indexOf("id=") + 3, lineage.indexOf("&amp", lineage.indexOf("id="))));
				//ORGANISM
				index = lineage.indexOf("TITLE=");
				lineage = lineage.substring(index);
				index = lineage.indexOf(">") + 1;//Skip the '>'
				tax.setOrganism(lineage.substring(index, lineage.indexOf("<")));
				tax.setExpandable(true);
				/*				if(Vars.userResult == null)
					Vars.userResult = new Result();*/
				taxList.add(tax);
				int nextTax;
				boolean isDone = false;
				for(int i=0;i<Result.orthology.size()&&!isDone;++i) {
					nextTax = Integer.parseInt(Result.orthology.get(i).getTaxID());
					while(Vars.nodesArray[nextTax] != null) 
						if(Vars.nodesArray[nextTax].equals(tax.getTaxID()) && !tax.getOrganism().contains("*")) {
							tax.setOrganism("*"+tax.getOrganism()+"*");isDone=true;break;
						}
						else {
							nextTax = Integer.parseInt(Vars.nodesArray[nextTax]);
							if(nextTax ==  Integer.parseInt(Vars.nodesArray[nextTax])) break;
						}
				}//for

			}
			return taxList;

		}catch(Exception e) {e.printStackTrace(); return null;}
	}

	
	/**
	 * This function gets the sons of a given node
	 * @param taxonomy taxonomy information of the node
	 * @return returns the taxonomy of the sons
	 */
	public static Taxonomy getSons(Taxonomy taxonomy) {
		Taxonomy root = taxonomy;
		String taxID=root.getTaxID();
		root.setExpandable(true);
		Taxonomy currentTax = root;

		try {
			BufferedReader br = getBufferedReader(Vars.taxonomyBrowser + taxID);

			String inputLine="";
			String temp="";
			while((inputLine = br.readLine())!=null)
				temp+=inputLine + "\n";
			inputLine = temp;
			int startIndex = inputLine.indexOf("</em>") + 6;//We start now!
			int endIndex = inputLine.indexOf(("<script type=\"text/javascript\">") , (inputLine.indexOf("<script type=\"text/javascript\">")+1));//<script type="text/javascript"> shows twice
			inputLine = inputLine.substring(startIndex, endIndex+12);
			String[] lines = inputLine.split("\\r?\\n");


			for(int i=0;i<lines.length;i++) {// -1 to ignore <script type line
				/* SON */

				if(lines[i].contains(son)){	
					currentTax.addToSons(new Taxonomy());
					currentTax = currentTax.getSons().get(currentTax.getSons().size()-1);
				}// end if

				/* FATHER */
				else if(lines[i].contains(father)) {
					if(lines[i].contains("id=")) //last line
						if(!(lines[i].contains("&nbsp"))) {//Its the end!
							currentTax.setTaxID(lines[i].substring((lines[i].indexOf("id="))+3, lines[i].indexOf("&lvl")));
							String org = lines[i].substring((lines[i].indexOf("<STRONG>")) + 8, lines[i].indexOf("</STRONG>")) +
									lines[i].substring((lines[i].indexOf("</A>")) + 4, lines[i].indexOf("</UL>"));	
							currentTax.setOrganism(org);
							break;
						}

					while(lines[i].contains(father)) {
						currentTax = currentTax.ancestor;i++;
					}

					currentTax.ancestor.addToSons(new Taxonomy());
					currentTax = currentTax.ancestor.getSons().get(currentTax.ancestor.getSons().size()-1);
					--i;

				}// end else if
				else {
					/*      EXPANDABLE      */
					if(lines[i].substring(lines[i].indexOf("="), lines[i].indexOf(">")).contains(notExpandable))
						currentTax.setExpandable(false);
					else
						currentTax.setExpandable(true);
					/*      TAX ID         */
					currentTax.setTaxID(lines[i].substring((lines[i].indexOf("id="))+3, lines[i].indexOf("&lvl")));
					/*      ORGANISM      */

					String org = lines[i].substring((lines[i].indexOf("<STRONG>")) + 8, lines[i].indexOf("</STRONG>")) +
							lines[i].substring((lines[i].indexOf("</A>")) + 4, lines[i].indexOf("&nbsp"));	
					currentTax.setOrganism(org);

					int nextTax;
					boolean isDone = false;
					for(int j=0;j<Result.orthology.size()&&!isDone;++j) {
						nextTax = Integer.parseInt(Result.orthology.get(j).getTaxID());
						while(Vars.nodesArray[nextTax] != null) 
							if(Vars.nodesArray[nextTax].equals(currentTax.getTaxID()) && !currentTax.getOrganism().contains("*")) {
								currentTax.setOrganism("*"+currentTax.getOrganism()+"*");isDone=true;break;
							}
							else {
								nextTax = Integer.parseInt(Vars.nodesArray[nextTax]);
								if(nextTax ==  Integer.parseInt(Vars.nodesArray[nextTax])) break;
							}
					}//for



					if((!(lines[i+1].equals(father)))&&(!(lines[i+1].equals(son)))) {
						currentTax.ancestor.addToSons(new Taxonomy());
						currentTax = currentTax.ancestor.getSons().get(currentTax.ancestor.getSons().size()-1);	
					}
				}

			}
		}catch(Exception e) {e.printStackTrace();}	
		return root;
	}
	public boolean isExpandable(String expand) {
		if(expand.equals("square"))
			return false;
		return true;
	}


/**
 * This function parses https://www.ncbi.nlm.nih.gov/gene/?Term= 
 */
	public static void parseGeneSearchSourceCode() {
		try {

			BufferedReader br = getBufferedReader(Vars.searchLink + Vars.searchWord);
			String inputLine;
			while ((inputLine = br.readLine()) != null) 
				if(inputLine.contains("gene-id"))
					break;


			String temp="";
			int i=0, linkuidLocation=0, geneidLocation=0; // locations are from indexOf, to get the second, third etc.
			Result result = new Result();
			int resultsInPage=20;

			for(;resultsInPage>0;--resultsInPage) {
				/*         NAME        */
				if(i>=inputLine.length()) break;
				i = inputLine.indexOf("link_uid", linkuidLocation+1);
				linkuidLocation = i;
				i+=10;
				while(inputLine.charAt(i) !='>') ++i; if(i>=inputLine.length()) break;
				++i;//for the '>'
				temp = getStr(i,inputLine,'<'); if(temp.equals("IndexOutOfBounds")) break;
				i+=temp.length();
				result.setName(temp);
				temp = "";
				/*              ID      */
				if(i>=inputLine.length()) break;
				i = inputLine.indexOf("gene-id", geneidLocation+1);
				geneidLocation = i;
				i+=13;
				temp = getStr(i, inputLine, '<');if(temp.equals("IndexOutOfBounds")) break;
				i+=temp.length();
				result.setGeneID(temp);
				result.setNameGenID(result.getName() + " " + result.getGeneID());
				temp = "";
				/*            DESCRIPTION     */       	
				String str;
				if(i>=inputLine.length()) break;
				while(!(inputLine.charAt(i-1)=='>' && inputLine.charAt(i)!='<')) {  ++i; if(i>=inputLine.length()) break;}
				str = getStr(i, inputLine, '<');if(temp.equals("IndexOutOfBounds")) break;
				temp = str;
				i+=temp.length();
				if(temp.contains("[")) {
					while (!temp.contains("]")) {
						while(!(inputLine.charAt(i-1)=='>' && inputLine.charAt(i)!='<')) {  ++i; if(i>=inputLine.length()) break;}
						str = getStr(i, inputLine, '<');if(temp.equals("IndexOutOfBounds")) break;
						temp += str;
						i+=str.length();
					}
				}
				result.setDescription(temp);
				temp="";
				/*           Location              */
				if(i>=inputLine.length()) break;
				while(!(inputLine.charAt(i-1)=='>' && inputLine.charAt(i)!='<')) {
					if(inputLine.charAt(i-1)=='-' && inputLine.charAt(i)=='o' && inputLine.charAt(i+1)=='m' && inputLine.charAt(i+2)=='i') {//Means no aliases, cause we reached the mim
						result.setLocation("none");break;
					}
					++i;
					if(i>=inputLine.length())
						break;
				}
				if(!result.getLocation().equals("none")) {
					str = getStr(i, inputLine, '<');if(temp.equals("IndexOutOfBounds")) break;
					result.setLocation(str);
					i+=str.length();
					str="";
				} else result.setLocation("");
				/*            Aliases               */
				if(i>=inputLine.length()) break;	
				while(!(inputLine.charAt(i-1)=='>' && inputLine.charAt(i)!='<')) {
					if(inputLine.charAt(i-1)=='-' && inputLine.charAt(i)=='o' && inputLine.charAt(i+1)=='m' && inputLine.charAt(i+2)=='i') {//Means no aliases, cause we reached the mim
						result.setAliases("none");break;
					}
					++i;
					if(i>=inputLine.length()) break;
				}
				if(!result.getAliases().equals("none")) {
					str = getStr(i, inputLine, '<');if(temp.equals("IndexOutOfBounds")) break;
					result.setAliases(str);
					i+=str.length();
					str="";
				}
				else 
					result.setAliases(" ");
				/*            MIM               */
				if(inputLine.charAt(inputLine.indexOf("-omim", linkuidLocation+1) + 7) == '<')  result.setMim("none"); 
				if(!result.getMim().equals("none")) {
					if(i>=inputLine.length()) break;
					while(!(inputLine.charAt(i-1)=='>' && inputLine.charAt(i)!='<'))
					{
						++i;
						if(i>=inputLine.length()) { 
							result.setMim("none");
							break;
						}
					}
				}
				if(!result.getMim().equals("none")) {
					str = getStr(i, inputLine, '<');
					if(temp.equals("IndexOutOfBounds"))  result.setMim("none");
					if(!result.getMim().equals("none"))
						result.setMim(str);
					else
						result.setMim(" ");
					i+=str.length();
				}
				else 
					result.setMim("");
				Result.orthology.add(result);result = new Result();
			}//for
			if(resultsInPage>0) {
				result.print();
				Result.orthology.add(result);
			}

			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	public static String getStr(int i, String inputLine, char notEqual) {
		String str = "";
		if(i>=inputLine.length()) 
			return "IndexOutOfBounds";
		while(inputLine.charAt(i)!=notEqual) {
			str+=inputLine.charAt(i);
			++i;
			if(i>=inputLine.length())
				return "IndexOutOfBounds";
		}
		return str;

	}
	
	
	/**
	 * This function checks if the DMP.zip file should be updated
	 */
	public static void checkTaxDMPUpdate() {//Update later maybe!
		try {
			BufferedReader br = getBufferedReader("ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/");
			String str = "", oldDate, newDate;
			while(!str.contains("taxdmp.zip")) 
				str = br.readLine();
			
			newDate = str.substring(str.indexOf("\"",str.indexOf("MB\","+5))+1, str.indexOf(":")-4);
			File file;
			if(!(Files.exists(Paths.get("C:\\Users\\Orel\\git\\Project\\ProejctB\\taxdmpdate.txt")))) { 
				PrintWriter writer = new PrintWriter("taxdmpdate.txt", "UTF-8");
				writer.write(newDate);
				writer.close();
				return;
			}
			else 
				file = new File("taxdmpdate.txt");
			FileReader fr = new FileReader(file);
			BufferedReader brdmp = new BufferedReader(fr);
			oldDate = brdmp.readLine();
			brdmp.close();
			String[] oldDateArr = oldDate.split("."), newDateArr = newDate.split(".");//DD-MM-YYYY
			boolean toDownload = false;
			if(Integer.parseInt(newDateArr[2]) > Integer.parseInt(oldDateArr[2])) 
				toDownload=true;

			else if(Integer.parseInt(newDateArr[1]) > Integer.parseInt(oldDateArr[1])) 
				toDownload=true;

			else if(Integer.parseInt(newDateArr[0]) > Integer.parseInt(oldDateArr[0])) 
				toDownload=true;
			if(!toDownload)
				return;
			//FTPClient client = new FTPClient();
			//client.connect("ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/");
		}catch(Exception e) {e.printStackTrace();}
	}

	
	

	@SuppressWarnings("finally")
	public static BufferedReader getBufferedReader(String link) {
		URLConnection conn;
		BufferedReader br = null;
		try {
			URL url = new URL(link);
			conn = url.openConnection();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}catch(Exception e) {e.printStackTrace();}
		finally 
		{
			return br;
		}
	}


}
