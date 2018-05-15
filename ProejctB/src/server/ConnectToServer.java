package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

public class ConnectToServer{
	public static void main(String[] args) {
		connectToURL();
		URL url = null;
		try {
			url = new URL("ftp://ftp.ncbi.nlm.nih.gov/genbank/gbbct1.seq.gz");
			File file = new File("C:\\Users\\Orel\\Desktop\\Test.gz");
			download(url, file);
		} catch (IOException e) {	e.printStackTrace();}

	}

	static java.net.URLConnection conn = null;
	private static String directory = "C:\\Users\\Orel\\Desktop";
	
	public static  void connectToURL() {
		URL url ;
		try {
			url = new URL("ftp://ftp.ncbi.nlm.nih.gov/genbank/gbbct1.seq.gz");
			conn = url.openConnection(); 
		}catch(Exception e) {}
		
		System.out.println("Connected URL");

	}
	
	public static void readFromURL() {
        try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	            System.out.println(inputLine);
	        in.close();
			}//try
        
        catch (IOException e) {e.printStackTrace();}
	}
	
	public static void download(URL link, File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(link.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
            fis.write(buffer, 0, count);
        
        System.out.println("here");
        fis.close();
        bis.close();
        System.out.println("Done");
	}
}
