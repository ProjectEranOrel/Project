package entities;

public class Lineage {
	private String taxID;
	private String organism;//Name of the organism
	private String link;
	private Result result;
	private boolean isExpandAble;
	public Taxonomy tree;
	
	
	
	
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public boolean isExpandAble() {
		return isExpandAble;
	}
	public void setExpandAble(boolean isExpandAble) {
		this.isExpandAble = isExpandAble;
	}
	public String getTaxID() {
		return taxID;
	}
	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}

}
