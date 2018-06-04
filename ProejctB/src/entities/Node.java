package entities;

public class Node {
	private String sonTaxID;
	private String fatherTaxID;
	
	public Node(String sonTaxID, String fatherTaxID) {
		this.sonTaxID = sonTaxID;
		this.fatherTaxID = fatherTaxID;
	}

	public String getSonTaxID() {
		return sonTaxID;
	}

	public void setSonTaxID(String sonTaxID) {
		this.sonTaxID = sonTaxID;
	}

	public String getFatherTaxID() {
		return fatherTaxID;
	}

	public void setFatherTaxID(String fatherTaxID) {
		this.fatherTaxID = fatherTaxID;
	}
	 
	
}



