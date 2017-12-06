package driver;

import global.GlobalTag;
import mgr.Manager;

/*
 * 锟斤拷锟斤拷
 * 锟斤拷锟斤拷锟斤拷锟绞街达拷锟�
 */
public class Driver {

	/*
	 * 锟斤拷锟矫癸拷锟斤拷锟斤拷
	 */
	public static void main(String[] args) {
		
		for(int i=1; i<=30; i++){//new
		System.out.println();//new
		System.out.println("*******第"+i+"次实验***********");//new
		//new
			
		Manager mgr = new Manager();
		
		//mgr.load(GlobalTag.INPUT_FILE);
		
		
		long time1 = System.currentTimeMillis();
		mgr.analyze();
		long time2 = System.currentTimeMillis();
		System.out.println("analyze: " + (time2-time1) + "ms");
	   
		
		
		long time3 = System.currentTimeMillis();
		mgr.process();
		long time31 = System.currentTimeMillis();
		System.out.println("processPSO: " + (time31-time3) + "ms");
		
		
		/*
		long time4 = System.currentTimeMillis();
		mgr.processGA();
		long time41 = System.currentTimeMillis();
		System.out.println("processGA: " + (time41-time4) + "ms");
		
		
		
		long time34 = System.currentTimeMillis();
		mgr.processRIA();
		long time43 = System.currentTimeMillis();
		System.out.println("processRIA: " + (time43-time34) + "ms");
		*/
		
		long time5 = System.currentTimeMillis();
		mgr.dump(GlobalTag.STUP_COMPLEXITY);
		mgr.dump(GlobalTag.CLASS_HIERARCHY_GRAPH);
		long time6 = System.currentTimeMillis();
		System.out.println("dump: " + (time6-time5) + "ms");
		//System.out.println("JSA dinished time"+ time6);//new
		}//new
		
		//		mgr.dump(GlobalTag.PSO_DATA);
	}
	
}
