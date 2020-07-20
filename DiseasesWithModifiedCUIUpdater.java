import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** 
 * The DiseasesWithModifiedCUIUpdater program replaces the disease concepts
 * with more than one CUI (identifier from UMLS Metathesaurus) with the
 * new customized CUI.
 * 
 * 
 * @author Kalpana Raja
 *
 */
public class DiseasesWithModifiedCUIUpdater {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;

		String arg1 = args[0]; //input_file -- diseases with modified ID
		String arg2 = args[1]; //input_file -- diseases lexicon
		String arg3 = args[2]; //output_file
		
		ArrayList<String> diseasesWithModifiedCUI = new ArrayList<String>();
		HashMap<String, String> diseasesAndModifiedCUI = new LinkedHashMap<String, String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	String[] arrLine = line.split("\t");
		    	diseasesWithModifiedCUI.add(arrLine[0]);
		    	diseasesAndModifiedCUI.put(arrLine[0], arrLine[1]);
		    }
			
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
		    bw.append("diseases_and_synonyms\tcui_or_modified_cui");
		    bw.append("\n");
		    while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				
				if(diseasesWithModifiedCUI.contains(arrLine[0])) {
					String diseaseModifiedCUI = diseasesAndModifiedCUI.get(arrLine[0]);
					bw.append(arrLine[0] + "\t" + diseaseModifiedCUI);
					bw.append("\n");
				}
				else {
					bw.append(line);
					bw.append("\n");
				}
				
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
