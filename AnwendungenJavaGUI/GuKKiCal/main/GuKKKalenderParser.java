package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Karl-Heinz Gödderz (GuKKDevel)
 *
 */
public class GuKKKalenderParser {
	
	protected String iCalName;

	public GuKKKalenderParser( )  throws Exception {
		// TODO Automatisch generierter Konstruktorstub
	}

	public static void main(String[] args) throws Exception 
	{
		try 
		{
		GuKKKalenderParser Kalender = new GuKKKalenderParser();
		
		Kalender.einlesen ("/home/programmieren/Git-Repositories/TestRepository/IgnoreForGit/iCalender/TestKalender.ics");

		}
		finally 
		{
			
		}
		
	}
	/**
     * @param inPath
     *            Pfad der Eingabedatei für den Kalender als String
     *  
     * @throws IOException
     */	
	
	public boolean einlesen (String inPath) throws IOException {
		BufferedReader ein = null;
		String zeile = null;
		String protokollZeile = "";
		int einruecken = 0;
		boolean fandBEGIN = false;
		boolean fandEND = false;
		
		try 
		{
			ein = new BufferedReader(new InputStreamReader(new FileInputStream(inPath), "UTF-8"));
			while ((zeile = ein.readLine()) != null) 
			{
				fandBEGIN = false;
				fandEND= false;
//				System.out.println(zeile.substring(0));
				
				if (zeile.substring(0, 6).equals("BEGIN:")) 
				{
//					System.out.println("BEGIN gefunden");
					fandBEGIN = true;			
				}
				else if (zeile.substring(0, 4).equals("END:"))
				{
//					System.out.println("END gefunden");
					fandEND = true;
				}
				
				if (fandEND)
				{
					einruecken--;
				}
				protokollZeile = "";
				for (int i = 0; i < einruecken; i++) {
		            protokollZeile = protokollZeile + "    ";
		        } 			
				protokollZeile = protokollZeile + zeile;
				protokollieren (protokollZeile);
				if (fandBEGIN) 
				{
					einruecken++;
					fandBEGIN = false;
				}
			}
		
		}
		finally {
			if (ein != null) 
			{
				ein.close();
			}
		}
		return true;
		
	}

	private void protokollieren (String textZeile)
	{
		
		System.out.println(textZeile);
	}




}
