package entities;
import java.util.ArrayList;
public class Taxonomy {
		private ArrayList<Taxonomy> sons = new ArrayList<Taxonomy>();
		private String link;//USELESS
		private String taxID;
		public String geneID;
		private String organism;
		private boolean isExpandable;
		public Taxonomy ancestor;
		private Sequence sequence;//To use the compare method
		
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
			setSequence(new Sequence());
			
		}
		public Taxonomy(String taxID, String organism) 
		{
			super();
			this.taxID = taxID;
			this.organism = organism;
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

		public Sequence getSequence() {
			return sequence;
		}

		public void setSequence(Sequence sequence) {
			this.sequence = sequence;
		}		
}
