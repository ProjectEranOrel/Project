package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Entities.Lineage;
import Entities.Result;
import Entities.Taxonomy;
import Entities.Vars;

public class ParseSourceCode {

	private static final String son = "<UL COMPACT>";
	private static final String father = "</UL>";
	private static final String notExpandable = "square";

	public static void main(String args[]) {
		//getLineage("9443");
		getSons("9443");
	}
	public static ArrayList<Taxonomy> getLineage(String taxID) { // First page3

		URLConnection conn;
		ArrayList<Taxonomy> taxList = new ArrayList<Taxonomy>();
		Taxonomy tax;
		Taxonomy taxSelected = new Taxonomy();
		try {

			URL url = new URL(Vars.taxonomyBrowser + taxID);
			conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			for(int i=0;i<122;i++) //CHANGE this works for now, but rather get something more general!
				br.readLine();

			String lineage = br.readLine();
			String searchedTaxInfo = br.readLine();

			/*              GET INFO ABOUT THE ORGANISM WE'RE SPECTATING          */
			//TAXID
			taxSelected.setLink(searchedTaxInfo.substring(searchedTaxInfo.indexOf("id=") + 3, searchedTaxInfo.indexOf("&lvl")));
			//ORGANISM
			taxSelected.setOrganism(searchedTaxInfo.substring(searchedTaxInfo.indexOf("<STRONG>") + 8, searchedTaxInfo.indexOf("</STRONG>")));
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
				tax.setExpandAble(true);
				if(Vars.userResult == null)
					Vars.userResult = new Result();
				taxList.add(tax);
			}
			for(int j=0;j<taxList.size();++j)
				System.out.println(" Organism: " + taxList.get(j).getOrganism()
						+ " TaxID: " + taxList.get(j).getTaxID());

		}catch(Exception e) {e.printStackTrace();}
		return taxList;
	}






	public static Taxonomy getSons(String taxID) {
		URLConnection conn;
		Taxonomy root = new Taxonomy();
		root.setOrganism("TESTORGA");
		root.setTaxID("TESTTAX");
		root.setExpandAble(true);
		Taxonomy currentTax = root;
		// CHECK FIRST IF EXPANDABLE, IF NOT, RETURN INFO!
		//root.set
		try {
			/// DO PREV AND CURR TAX! REPARSE!
			URL url = new URL(Vars.taxonomyBrowser + taxID);
			conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine="";
			String temp="";
			while((inputLine = br.readLine())!=null)
				temp+=inputLine + "\n";
			inputLine = temp;
			int startIndex = inputLine.indexOf("</em>") + 6;//We start now!
			int endIndex = inputLine.indexOf(("<script type=\"text/javascript\">") , (inputLine.indexOf("<script type=\"text/javascript\">")+1));//<script type="text/javascript"> shows twice
			inputLine = inputLine.substring(startIndex, endIndex+12);
			String[] lines = inputLine.split("\\r?\\n");


			for(int i=0;i<lines.length-1;i++) {// -1 to ignore <script type line
				/* SON */
				if(lines[i].contains(son)){			//		System.out.println("son");
				currentTax.addToSons(new Taxonomy());
				currentTax = currentTax.getSons().get(currentTax.getSons().size()-1);
				}// end if

				/* FATHER */
				else if(lines[i].contains(father)) {	//				System.out.println("father");
				int l=0;
				while(lines[i].contains(father)) {
					currentTax = currentTax.ancestor;i++;
				}
				if(lines[i].contains("<script t"))
					break;
				currentTax.ancestor.addToSons(new Taxonomy());
				currentTax = currentTax.ancestor.getSons().get(currentTax.ancestor.getSons().size()-1);
				--i;

				}// end else if
				else {
					/*      EXPANDABLE      */
					if(lines[i].substring(lines[i].indexOf("="), lines[i].indexOf(">")).contains(notExpandable))
						currentTax.setExpandAble(false);
					else
						currentTax.setExpandAble(true);
					/*      TAX ID         */
					currentTax.setTaxID(lines[i].substring((lines[i].indexOf("id="))+3, lines[i].indexOf("&lvl")));
					/*      ORGANISM      */
					currentTax.setOrganism(lines[i].substring((lines[i].indexOf("<STRONG>")) + 8, lines[i].indexOf("</STRONG>")));
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



	public static void parseGeneSearchSourceCode() {
		URLConnection conn;
		try {
			URL url = new URL(Vars.searchLink + Vars.searchWord);
			conn = url.openConnection();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
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
						System.out.println("No Location!");
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
				Result.resultsList.add(result);result = new Result();
			}//for
			if(resultsInPage>0) {
				result.print();
				Result.resultsList.add(result);
			}

			br.close();
		} catch (IOException e) {e.printStackTrace();System.out.println("Link might be broken");}
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


}
