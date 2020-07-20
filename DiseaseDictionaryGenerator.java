import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * The DiseaseDictionaryGenerator program implements an application
 * that locates all preferred English UMLS Metathesaurus concepts that 
 * belong to the category of diseases and groups them.
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */
public class DiseaseDictionaryGenerator {
	
	public static void main(String[] args) {
		String line="";
		int count=0;

		String arg1 = args[0]; //input file -- umls.metathesaurus.vocabulary2017AB
		String arg2 = args[1]; //output file -- mrconso.diseaseVocabularies2017AB
		
		//generate dictionary
		try {
			FileReader fr = new FileReader(arg1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(arg2);
			BufferedWriter bw = new BufferedWriter(fw);
			while((line = br.readLine())!=null) {
				//disease related semantic types
				if(line.trim().contains("T020") || line.trim().contains("T190") || 
						line.trim().contains("T049") || line.trim().contains("T019") ||
						line.trim().contains("T047") || line.trim().contains("T050") || 
						line.trim().contains("T033") || line.trim().contains("T037") || 
						line.trim().contains("T048") || line.trim().contains("T191") ||
						line.trim().contains("T046") || line.trim().contains("T184")) {
					bw.append(line);
					bw.append("\n");
					//System.out.println(line);
					count++;
				}
			}
			
			System.out.println("\n\nTotal number of records: "+count);
			
			br.close();
			bw.close();
		} catch (IOException ie) {
			System.err.println(ie);
		}
		
		//evaluate dictionary
		getStatistics(arg2);
	}
	
	/**
	 * Method to obtain the number of diseases in disease lexicon. 
	 *
	 */
	public static void getStatistics(String arg2) {
		String line="";
		int count1=0, count2=0, count3=0, count4=0, count5=0, count6=0, 
		count7=0, count8=0, count9=0, count10=0, count11=0, count12=0;
		try {
			FileReader fr = new FileReader(arg2);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null) {
				//disease related semantic types
				if(line.trim().contains("T020")) { count1++; }
				else if(line.trim().contains("T190")) { count2++; }
				else if(line.trim().contains("T049")) { count3++; }
				else if(line.trim().contains("T019")) { count4++; }
				else if(line.trim().contains("T047")) { count5++; }
				else if(line.trim().contains("T050")) { count6++; }
				else if(line.trim().contains("T033")) { count7++; }
				else if(line.trim().contains("T037")) { count8++; }
				else if(line.trim().contains("T048")) { count9++; }
				else if(line.trim().contains("T191")) { count10++; }
				else if(line.trim().contains("T046")) { count11++; }
				else if(line.trim().contains("T184")) { count12++; }
			}
	
			System.out.println("T020 - Acquired Abnormality: "+count1);
			System.out.println("T190 - Anatomical Abnormality: "+count2);
			System.out.println("T049 - Cell or Molecular Dysfunction: "+count3);
			System.out.println("T019 - Congenital Abnormality: "+count4);
			System.out.println("T047 - Disease or Syndrome: "+count5);
			System.out.println("T050 - Experimental Model of Disease: "+count6);
			System.out.println("T033 - Finding: "+count7);
			System.out.println("T037 - Injury or Poisoning: "+count8);
			System.out.println("T048 - Mental or Behavioral Dysfunction: "+count9);
			System.out.println("T191 - Neoplastic Process: "+count10);
			System.out.println("T046 - Pathologic Function: "+count11);
			System.out.println("T184 - Sign or Symptom: "+count12);
	
			br.close();
		} catch (IOException ie) {
			System.err.println(ie);
		}
	}
}

