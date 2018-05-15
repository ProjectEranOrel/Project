package Entities;

import java.util.ArrayList;

public class Taxonomy {
		private ArrayList<Taxonomy> sons = new ArrayList<Taxonomy>();
		private String link;
		private String taxID;
		private String organism;
		private boolean isExpandAble;
		public Taxonomy ancestor;

		
		
		
		public ArrayList<Taxonomy> getSons() {
			return sons;
		}
		public void addToSons(Taxonomy tax) {
			tax.ancestor = this;
			sons.add(tax);
		}
		
		public void setTaxID(String taxID) {
			this.taxID = taxID;
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

		
		public String getOrganism() {
			return organism;
		}

		public void setOrganism(String organism) {
			this.organism = organism;
		}
		public boolean isExpandAble() {
			return isExpandAble;
		}
		public void setExpandAble(boolean isExpandAble) {
			this.isExpandAble = isExpandAble;
		}
}
