package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import Entities.Result;
import Entities.Taxonomy;
import Entities.Vars;

public class ParseSourceCode {
	public static void main(String args[]) {
		parseTaxonomyBrowserSourceCode("9606");
	}
	public static void parseTaxonomyBrowserSourceCode(String taxID) { // First page3
		
		URLConnection conn;
		try {
			
		URL url = new URL(Vars.taxonomyBrowser + taxID);
		conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String currLine="", prevLine;
/*		while ((prevLine = br.readLine()) != null) {
			if(currLine.contains("</table>") && prevLine.contains("<A HREF"))
				System.out.println(currLine);
			currLine = br.readLine();
			if(prevLine.contains("</table>") && currLine.contains("<A HREF"))
				System.out.println(currLine);
		}*/
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		for(int i=0;i<122;i++) {//CHANGE this works for now, but rather get something more general!
			br.readLine();
		}
			String lineage = br.readLine();
			System.out.println(lineage);
			String searchedTaxInfo = br.readLine();
			Taxonomy searchedTax = new Taxonomy();
			//TAXID
			searchedTax.setLink(searchedTaxInfo.substring(searchedTaxInfo.indexOf("id=") + 3, searchedTaxInfo.indexOf("&lvl")));
			//ORGANISM
			searchedTax.setOrganism(searchedTaxInfo.substring(searchedTaxInfo.indexOf("<STRONG>") + 8, searchedTaxInfo.indexOf("</STRONG>")));
			
			
			int index = 3;//First ahref is irrelevant
			lineage = lineage.substring(index);
			while(lineage.indexOf("HREF") != -1) {
				lineage = lineage.substring(index);
				index = lineage.indexOf("HREF");
				if(index == -1)
					break;
				Taxonomy tax = new Taxonomy();
				//TAXID
				lineage = lineage.substring(index);
				tax.setLink(lineage.substring(lineage.indexOf("id=") + 3, lineage.indexOf("&lvl")));
				//ORGANISM
				index = lineage.indexOf("ALT");
				lineage.substring(index);
				index = lineage.indexOf(">") + 1;//Skip the '>'
				tax.setOrganism(lineage.substring(index, lineage.indexOf("<", index)));
				searchedTax.addToAncestorsList(tax);
			}
			System.out.println("done while");

		
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public static void parseSearchSourceCode() {
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
