import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** 
 * The MRCONSOPreferredVocabularyGenerator program implements
 * an application that extracts concepts from MRCONSO.RRF that 
 * are in English and are labeled preferred. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */

public class MRCONSOPreferredVocabularyGenerator {
	
	public static void main(String[] args) throws IOException {

		String arg1 = args[0]; //input file -- MRCONSO.RRF
		String arg2 = args[1]; //output file -- mrconso.preferredVocabularies2017AB

		BufferedReader in = new BufferedReader(new FileReader(arg1));
		PrintWriter pwr = new PrintWriter(arg2);
		
		int count=0, count1=0;
		while(in.ready()){
			String line = in.readLine();
			String[] arrLine = line.split("\\|");
			if(arrLine[1].trim().equals("ENG") && arrLine[6].equals("Y")) {
				pwr.append(line);
				pwr.append("\n");
				count1++;
			}
			
			count++;
			if(count%1000==0) System.out.println(count);
		}
		
		System.out.println("\n\nFiltered records: "+count1);
		in.close();
		pwr.close();
	}
}
