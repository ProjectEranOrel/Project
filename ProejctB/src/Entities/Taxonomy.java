package Entities;

import java.util.ArrayList;
import javafx.scene.control.*;
public class Taxonomy {
		private ArrayList<Taxonomy> sons = new ArrayList<Taxonomy>();
		private String link;
		private String taxID;
		private String organism;
		private boolean isExpandable;
		public Taxonomy ancestor;
		
		

		
		public Taxonomy() {
			super();
		}
		
		public Taxonomy(ArrayList<Taxonomy> sons, String link, String taxID, String organism, boolean isExpandable,
				Taxonomy ancestor) 
		{
			super();
			this.sons = sons;
			this.link = link;
			this.taxID = taxID;
			this.organism = organism;
			this.isExpandable = isExpandable;
			this.ancestor = ancestor;
		}
		public Taxonomy(String taxID, String organism, boolean isExpandable) 
		{
			super();
			this.taxID = taxID;
			this.organism = organism;
			this.isExpandable = isExpandable;
		}
		
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
		
		public boolean isExpandable() {
			return isExpandable;
		}
		public void setExpandable(boolean isExpandable) {
			this.isExpandable = isExpandable;
		}

		

		
}
