package Entities;

import java.util.ArrayList;

public class Result {
	private String geneID = "";
	private String description = "";
	private String location = "";
	private String aliases = "";
	private String mim = "";
	private String results = "";
	private String name = "";
	private String nameGenID = name + "\n" + geneID;
	private String orgName;
	public ArrayList<Lineage> lineage = new ArrayList<Lineage>();
	private int number; // row number in table
	private String TaxID;
	
	
	public static ArrayList<Result> resultsList = new ArrayList<Result>();

	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getGeneID() {
		return geneID;
	}
	public void setGeneID(String nameGenID) {
		this.geneID = nameGenID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public String getMim() {
		return mim;
	}
	public void setMim(String mim) {
		this.mim = mim;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameGenID() {
		return nameGenID;
	}
	public void setNameGenID(String nameGenID) {
		this.nameGenID = nameGenID;
	}
	public void print() {
		System.out.println("\nnamegen " + nameGenID + "\ndes " + description + "\nloc " + location + "\nali " + aliases + "\nmim " +mim);
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getTaxID() {
		return TaxID;
	}
	public void setTaxID(String taxID) {
		TaxID = taxID;
	}


}
