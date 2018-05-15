import java.io.*; import java.util.*;

import org.biojava.bio.*; import org.biojava.bio.seq.db.*; import org.biojava.bio.seq.io.*; import org.biojava.bio.symbol.*;

public class ReadFasta {

 /**
  * The program takes two args: the first is the file name of the Fasta file.
  * The second is the name of the Alphabet. Acceptable names are DNA RNA or PROTEIN.
  */
 public static void main(String[] args) {

   try {
     //setup file input
     String filename = "gbbct149";
     BufferedInputStream is =
         new BufferedInputStream(new FileInputStream(filename));

     //get the appropriate Alphabet
     Alphabet alpha = AlphabetManager.alphabetForName("gbbct149");

     //get a SequenceDB of all sequences in the file
     @SuppressWarnings("deprecation")
	SequenceDB db = SeqIOTools.readFasta(is, alpha);
   }
   catch (BioException ex) {
     //not in fasta format or wrong alphabet
     ex.printStackTrace();
   }catch (NoSuchElementException ex) {
     //no fasta sequences in the file
     ex.printStackTrace();
   }catch (FileNotFoundException ex) {
     //problem reading file
     ex.printStackTrace();
   }
 }

}