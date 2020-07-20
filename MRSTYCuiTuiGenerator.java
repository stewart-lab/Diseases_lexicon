import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** 
 * The MRSTYCuiTuiGenerator program implements an application that 
 * extracts and groups information regarding the CUI, TUI, and 
 * semantic type of each concept in MRSTY.RRF. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */
public class MRSTYCuiTuiGenerator {
	
	public static void main(String[] args) throws IOException {

		String arg1 = args[0]; //input file -- MRSTY.RRF
		String arg2 = args[1]; //output file -- mrsty.cuitui2017AB.txt

		BufferedReader in = new BufferedReader(new FileReader(arg1));
		PrintWriter pwr = new PrintWriter(arg2);
		ArrayList<String> terms = new ArrayList<String>();
		int count=0;
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\\|");
			String cui = splits[0];
			String tui = splits[1];
			String type = splits[3];
			String cui_tui = cui.concat("_").concat(tui);
			
			terms.add(cui_tui.concat("\t").concat(type));
			
			count++;
			if(count%1000==0) System.out.println(count);
		}
		in.close();
		
		for (String t : terms) {
			pwr.println(t); 
		}
		pwr.close();
		System.out.println("Total number of records: "+count);
	}
}
