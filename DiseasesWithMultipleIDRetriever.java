import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/** 
 * The DiseasesWithMultipleIDRetriever program retrieves the disease concepts
 * with more than one CUI (identifier from UMLS Metathesaurus)
 * 
 * 
 * @author Kalpana Raja
 *
 */
public class DiseasesWithMultipleIDRetriever {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;

		String arg1 = args[0]; //input_file -- diseases lexicon
		String arg2 = args[1]; //output_file
		
		ArrayList<String> diseasesLexicon = new ArrayList<String>();
		Set<String> diseases = new LinkedHashSet<String>();
		
		try {
			FileInputStream fis = new FileInputStream(arg1);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg2);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				String[] arrLine = line.split("\t");
				String disease = arrLine[0].toLowerCase();
				diseases.add(disease);
				diseasesLexicon.add(disease + "\t" + arrLine[1]);

				count++;
				//if(count==10) break;
				//if(count%1000==0) System.out.println(count);
			}
			
			bw.append("disease\tCUI_TUIs\tCUI_TUIs_count");
			bw.append("\n");
			for(String eachDisease : diseases) {
				Set<String> diseaseCUI_TUI = new LinkedHashSet<String>();
				for(String each : diseasesLexicon) {
					if(!each.startsWith(eachDisease+"\t")) continue; 
						
					String[] arr = each.split("\t");
					diseaseCUI_TUI.add(arr[1]);
				}
				
				if(diseaseCUI_TUI.size()>1) {
					String cui_tuiS="";
					for(String eachID : diseaseCUI_TUI) {
						if(cui_tuiS.isEmpty()) cui_tuiS = eachID;
						else cui_tuiS = cui_tuiS + "|" + eachID;
					}
					
					bw.append(eachDisease + "\t" + cui_tuiS + "\t" + diseaseCUI_TUI.size());
					bw.append("\n");
				}
			}
			
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
