package Entities;

import java.util.ArrayList;

public class Taxonomy {
		private ArrayList<Taxonomy> lineage = new ArrayList<Taxonomy>();
		private String link;
		private Result result;
		private String taxID;
		private String organism;
		private boolean isExpandAble;
		public Taxonomy next, prev;

		
		
		public Taxonomy() {
			result = new Result();
		}
		
		public ArrayList<Taxonomy> getLineage() {
			return lineage;
		}
		public void setLineage(ArrayList<Taxonomy> lineage) {
			this.lineage = lineage;
		}
		public Result getResult() {
			return result;
		}
		public void setResult(Result result) {
			this.result = result;
		}

		
		public void setLink(String taxID) {
			this.link = "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" + taxID + "&lvl=3&lin=f&keep=1&srchmode=1&unlock";
			this.taxID = taxID;
		}
		
		public String getTaxID() {
			return taxID;
		}
		
		public String getLink() {
			return link;
		}

		public ArrayList<Taxonomy> getAncestorsList() {
			return lineage;
		}

		public void setAncestorsList(ArrayList<Taxonomy> ancestorsList) {
			this.lineage = ancestorsList;
		}

		public void addToAncestorsList(Taxonomy tax) {
			lineage.add(tax);
		}
		
		public String getOrganism() {
			return organism;
		}

		public void setOrganism(String organism) {
			this.organism = organism;
		}
		public boolean isExpandAble() {
			return isExpandAble;
		}
		public void setIsExpandAble(boolean isExpandAble) {
			this.isExpandAble = isExpandAble;
		}
}
