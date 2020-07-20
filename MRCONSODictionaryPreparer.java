import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/** 
 * The MRCONSODictionaryPreparer program implements an
 * application that constructs a list of records that each
 * include the name of a concept, a CUI, a TUI, and the 
 * semantic type.  
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */
public class MRCONSODictionaryPreparer {
	
	public static void main(String[] args) throws IOException {
		
		long startTime = System.nanoTime();
		
		String arg1 = args[0]; //input -- mrsty.cuituiGroup2017AB.txt
		String arg2 = args[1]; //input -- semgroup_tui2013.txt
		String arg3 = args[2]; //input -- mrconso.preferredVocabularies2017AB.txt
		String arg4 = args[3]; //output -- umls.metathesaurus.vocabulary2017AB
		String arg5 = args[4]; //output -- missedVocabulary2017AB.txt
		
		BufferedReader in1 = new BufferedReader(new FileReader(arg1));
		ArrayList<String> mrsemType = new ArrayList<String>();
		while(in1.ready()){
			String line = in1.readLine();
			mrsemType.add(line);
		}
		in1.close();
		
		BufferedReader in2 = new BufferedReader(new FileReader(arg2));
		ArrayList<String> semgrp = new ArrayList<String>();
		while(in2.ready()){
			String line = in2.readLine();
			semgrp.add(line);
		}
		in2.close();
		
		BufferedReader in = new BufferedReader(new FileReader(arg3));
		PrintWriter pwr = new PrintWriter(arg4);
		PrintWriter pwr1 = new PrintWriter(arg5);
		
		HashSet<String> terms = new HashSet<String>();
		HashMap<String,String> norms = new HashMap<String,String>();
		int count=0, count1=0;
		String cui="", cui_tui="", semGroup="";
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\\|");
			String term = splits[14];
			cui = splits[0];
			
			//remove ending semantic tag such as (disorder) or [Ambiguity] for better matching
			if(term.endsWith(")")&&term.matches(".+\\s+\\(\\S+\\)"))  term=term.substring(0, term.indexOf("("));
			else if(term.endsWith("]")&&term.matches(".+\\s+\\[\\S+\\]")) term=term.substring(0, term.indexOf("["));
		
			if(term.startsWith("\\(") && term.endsWith("\\]")) continue; 
			else if(term.startsWith("\\[") && term.endsWith("\\]")) continue;
			else {
				//normalized tokens
				String norm = term.toLowerCase(); 
				if(norm.contains(" ")) norm = norm.replaceAll(" ", "\t");
				if(norm.contains(",")) norm = norm.replaceAll(",", " ");
				if(norm.contains("-")) norm = norm.replaceAll("-", " ");
				if(norm.contains("\\(")) norm = norm.replaceAll("\\(", "");
				if(norm.contains("\\)")) norm = norm.replaceAll("\\)", "");
			
				//get TUI
				for(String mr : mrsemType) {
					if(mr.startsWith(cui)) {
						cui_tui = mr;
						break;
					}
				}
			
				//get sem group
				ArrayList<String> cuiTuiList=null; 
				if(cui_tui.contains(";")) {
					String[] arrcui_tui = cui_tui.split(";");
					cuiTuiList = new ArrayList<String>(Arrays.asList(arrcui_tui));
				}
				if(!cui_tui.contains(";")){
					cuiTuiList = new ArrayList<String>();
					cuiTuiList.add(cui_tui);
				}
				semGroup="";
				for(String eachCuiTui : cuiTuiList) {
					String[] arrCuiTui = eachCuiTui.split("_");
					if(arrCuiTui.length>1) {
						String tui = arrCuiTui[1];
						for(String se : semgrp) {
							if(se.contains("|"+tui+"|")) {
								String[] arrSe = se.split("\\|");
								if(semGroup.isEmpty()) {
									semGroup = arrSe[0];
								}
								else if (!semGroup.contains(arrSe[0])){
									semGroup = semGroup.concat(";").concat(arrSe[0]);
								}
							}
						}
					}
					else {
						pwr1.println("CUI_TUI_List"+cuiTuiList);
					}
				}
				
				term = term.trim();
				//System.out.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
				if(!norm.isEmpty() && !term.isEmpty() && !semGroup.isEmpty() && !cui_tui.isEmpty()) {
					pwr.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
					//System.out.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
				}
				else {
					pwr1.println(norm+"|"+term+"||"+semGroup+"|"+cui_tui);
				}	
				norm = term = semGroup = cui_tui = "";
			
				count++;
				//if(count==100) break;
				if(count%1000==0) System.out.println(count);
				
				//count1++;
				//if(count1==50) break;
			}	
		}
		in.close();
		pwr.close();
		pwr1.close();
		System.out.println("Total number of records: "+count);
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Process time: "+duration);
	}
}
