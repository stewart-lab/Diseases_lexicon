import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/** 
 * The DiseasesWithMultipleCUICompiler program combines the disease concepts
 * and synonyms together. It replaced multiple CUIs for the same disease 
 * concept with a customized CUI that starts with the letter 'M'. The letter 'M'
 * in the customized CUI denotes "modified  CUI".
 * 
 * 
 * @author Kalpana Raja
 *
 */
public class DiseasesWithMultipleCUICompiler {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		String line="";
		int count=0;
		
		//get unique id
		long number = 00000000;

		String arg1 = args[0]; //input_file -- diseases lexicon
		String arg2 = args[1]; //input_file -- diseases with multiple CUIs
		String arg3 = args[2]; //output_file
		
		ArrayList<String> diseasesAndSynonyms = new ArrayList<String>();
		Set<String> diseasesWithMultipleCUI = new LinkedHashSet<String>();
		Set<String> cuis = new LinkedHashSet<String>();
		
		try {
			FileInputStream fis0 = new FileInputStream(arg1);
			InputStreamReader isr0 = new InputStreamReader(fis0,"UTF-8");
		    BufferedReader br0 = new BufferedReader(isr0);
		    while((line = br0.readLine()) != null) {
		    	diseasesAndSynonyms.add(line);
		    }
		    
			FileInputStream fis = new FileInputStream(arg2);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		    BufferedReader br = new BufferedReader(isr);
		         
		    FileOutputStream fos = new FileOutputStream(arg3);
		    OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
		    BufferedWriter bw = new BufferedWriter(osr);
		       
			while((line = br.readLine()) != null) {
				if(line.startsWith("disease\tCUI_TUIs")) continue;
                diseasesWithMultipleCUI.add(line);
			}	
			
			Set<String> allCUIs = new LinkedHashSet<String>();
			for(String eachRecord : diseasesWithMultipleCUI) {
				Set<String> synonyms = new LinkedHashSet<String>();
				Set<String> tuis = new LinkedHashSet<String>();
				
				String[] arrLine = eachRecord.split("\t");
				if(cuis.contains(arrLine[1])) continue;
				
				cuis.add(arrLine[1]);
				String[] arrCUIs = arrLine[1].split("\\|"); //CUIs from current row
				Set<String> a1 = new LinkedHashSet<String>(Arrays.asList(arrCUIs));
				Set<String> a3 = new LinkedHashSet<String>(a1);
				a3.retainAll(allCUIs);
				if(a3.size()>0) continue;
				
				//get all related IDs
				for(String eachCUI : arrCUIs) {
					for(String eachRow : diseasesWithMultipleCUI) {
						if(eachRow.equals(eachRecord)) continue; //same record
						if(!eachRow.contains(eachCUI)) continue; 
						
						String[] arr = eachRow.split("\t");
						if(arr[1].equals(arrLine[1])) continue; //same ID group
						
						String[] rowCUIs = arr[1].split("\\|");
						Set<String> a2 = new LinkedHashSet<String>(Arrays.asList(rowCUIs)); 
						a2.retainAll(a1);
						if(a2.size()>0) {
							for(String eachRowCUI : rowCUIs) {
								a1.add(eachRowCUI);
							}
						}
					}
				}
				
				//get synonyms and TUI
				for(String eachCUI : a1) {
					//get all synonyms matching IDs
					for(String eachSynonym : diseasesAndSynonyms) {
						if(!eachSynonym.contains("\t"+eachCUI)) continue;
						
						String[] arr = eachSynonym.split("\t");
						synonyms.add(arr[0]);
					}
					
					//get all TUI
					String[] arrSubCUIs = eachCUI.split(";");
					for(String eachSubCUI : arrSubCUIs) {
						String tui = eachSubCUI.substring(eachSubCUI.indexOf("_"));
						tuis.add(tui);
					}
				}
				
				//generate unique ID
				number = number+1;
				String diseaseCUI = "M".concat(String.format("%08d", number));
				String diseaseCUI_tui="";
				for(String eachTUI : tuis) {
					if(diseaseCUI_tui.isEmpty()) diseaseCUI_tui = diseaseCUI + eachTUI;
					else diseaseCUI_tui = diseaseCUI_tui + ";" + diseaseCUI + eachTUI;
				}
				
				//assign drugs and synonyms with new ID 
				for(String eachSynonym : synonyms) {
					bw.append(eachSynonym + "\t" + diseaseCUI_tui);
					bw.append("\n");
				}
				
				//all IDs addressed
				for(String each : a1) {
					allCUIs.add(each);
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
