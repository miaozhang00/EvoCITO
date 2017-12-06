package ga;

import java.util.LinkedList;
import java.util.List;

import toolkits.SourceClassInfo;
import toolkits.StupComplexity;

public class ConnectDataCenter {

	StupComplexity 	stupComplexity;
	
	public ConnectDataCenter(StupComplexity stupComplexity){
		this.stupComplexity = stupComplexity;
	}
	
	//public Chromosome getChromosFromDC(){
		//Chromosome a = new Chromosome();
		//return a;
	//}
	
	public List<SourceClassInfo> getSCIfromDC(){
		
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for(SourceClassInfo sci : sci_list){
			sci_initial.add(sci);
		}
		return sci_initial;
	}	
	
}
