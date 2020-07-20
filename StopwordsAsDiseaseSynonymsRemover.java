import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/** 
 * The StopwordsAsDiseaseSynonymsRemover program removes the disease concepts
 * matching with English stopwords
 * 
 * 
 * @author Kalpana Raja
 *
 */
public class StopwordsAsDiseaseSynonymsRemover {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;

		String arg1 = args[0]; //input_file -- stopwords
		String arg2 = args[1]; //input_file -- disease lexicon
		String arg3 = args[2]; //output_file
		
		ArrayList<String> stopwords = new ArrayList<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		    while((line = br.readLine()) != null) {
		    	stopwords.add(line.toLowerCase().trim());
		    }
			
			FileInputStream fis0 = new FileInputStream(arg2);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br0.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(stopwords.contains(arrLine[0].toLowerCase().trim())) continue;
				
				bw.append(line);
				bw.append("\n");

				count++;
				//if(count==10) break;
				//if(count%1000==0) System.out.println(count);
			}
			br0.close();
			br.close();
			bw.close();
		} catch(IOException e) {
			System.err.println(e);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time in milliseconds: " + elapsedTime);
	}
	
}
