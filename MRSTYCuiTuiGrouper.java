import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** 
 * The MRSTYCuiTuiGrouper program implements an application 
 * that groups TUIs that are paired with the same CUI in the 
 * cui_tui.txt file. 
 * 
 * @author Kalpana Raja and Troy Cao
 *
 */
public class MRSTYCuiTuiGrouper {
	
	public static void main(String[] args) throws IOException {

		String arg1 = args[0]; //input file -- mrsty.cuitui2017AB.txt
		String arg2 = args[1]; //output file -- mrsty.cuituiGroup2017AB.txt

		BufferedReader in = new BufferedReader(new FileReader(arg1));
		PrintWriter pwr = new PrintWriter(arg2);
		ArrayList<String> cuiTuiGroup = new ArrayList<String>();
		String precui="", cuilist="";
		
		while(in.ready()){
			String line = in.readLine();
			String[] splits=line.split("\t");
			String cui=splits[0].substring(0, splits[0].indexOf("_"));
			if(cuilist.isEmpty()) { 
				cuilist=splits[0]; //cui_tui
			}
			else { 
				if(cuilist.startsWith(cui)) { 
					cuilist=cuilist.concat(";").concat(splits[0]); 
				}
				else {
					//System.out.println(cuilist);
					cuiTuiGroup.add(cuilist);
					cuilist="";
					cuilist=splits[0];
				}
			}
		}
		in.close();
		
		for(String eachCuiTui : cuiTuiGroup) {
			pwr.println(eachCuiTui);
		}
		pwr.close();
		
		System.out.println("Total number of records: "+cuiTuiGroup.size());
	}
}
