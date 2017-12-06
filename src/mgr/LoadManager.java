package mgr;

import global.GlobalTag;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import toolkits.SourceClassInfo;
import toolkits.StupComplexity;
import toolkits.dependency.SimpleAttributeDependency;
import toolkits.dependency.SimpleMethodDependency;

import data.DataCenter;

public class LoadManager {

	DataCenter dc;

	LocationManager locMgr;
	
	public LoadManager(DataCenter dc, LocationManager locMgr) {
		this.dc 	= dc;
		this.locMgr = locMgr;
	}
	
	public void load(String loadType) {
		if (GlobalTag.MY_APP_CLASSES.equals(loadType)) {
//			loadMyAppClasses();
		} else if (GlobalTag.INPUT_FILE.equals(loadType)) {
			loadInputFile();
		}
	}

	/*public void loadMyAppClasses() {
		String loc 		= GlobalTag.LOC_CONFIG;
		String fileName = GlobalTag.AP_ATMExample;
		
		BufferedReader br;
		try {
			Set<String> myAppClasses = new HashSet<String>();
			
			br = new BufferedReader(new FileReader(loc + "/" + fileName));
			String data = br.readLine();
			while( data!=null){  
				System.out.println(data);  
			    data = br.readLine();
			    myAppClasses.add(data);
			} 
			
			if (myAppClasses.size() != 0) {
				System.out.println("11111");
				this.dc.setMyAppClasses(myAppClasses);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}*/
	
	public void loadInputFile() {
		
		Map<String, String> appClasses = new HashMap<String, String>();
		loadClassesFile(appClasses);
		loadAttributeFile(appClasses);
		loadMethodFile(appClasses);
	}
	
	public void loadClassesFile(Map<String, String> appClasses) {
		String loc 		= locMgr.getLoc(GlobalTag.INPUT_FILE);
		String fileName	= locMgr.getFileName(GlobalTag.CLASSES_FILE);
		
		StupComplexity sc = dc.getStupComplexity();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(loc + "/" + fileName));
			
			String line = null;
			do {
				line = br.readLine();
			    
			    if (line == null) { continue; }
			    
			    String[] strs 	= line.split("\t");
			    String index 	= strs[0];
//			    String appClass = strs[1];
			    String appClass = line;
			    
			    appClasses.put(index, appClass);
			    
			    sc.newSourceClassInfo(appClass);
//			    sc.newSourceClassInfo(index);
			} while (line != null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public void loadAttributeFile(Map<String, String> appClasses) {
		String loc 		= locMgr.getLoc(GlobalTag.INPUT_FILE);
		String fileName	= locMgr.getFileName(GlobalTag.ATTRIBUTE_FILE);
		
		StupComplexity sc = dc.getStupComplexity();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(loc + "/" + fileName));
			
			String line = null;
			do {
				line = br.readLine();
			    
			    if (line == null) { continue; }
			    
			    String[] strs 	= line.split("\t");
			    String from 		= strs[0];
			    String to 			= strs[1];
			    int sizeOfAttrs 	= Integer.valueOf(strs[2]);
			    
			    String sourceClass = appClasses.get(from);
			    String targetClass = appClasses.get(to);
			    
			    SourceClassInfo sci = sc.getSourceClassInfo(sourceClass);
//			    SourceClassInfo sci = sc.getSourceClassInfo(from);
			    for (int i=0; i<sizeOfAttrs; i++) {
			    	sci.newAttributeDependency(new SimpleAttributeDependency(sourceClass, targetClass));
//			    	sci.newAttributeDependency(new SimpleAttributeDependency(from, to));
			    }
			    
			} while (line != null);
			
//			System.out.println(sc.toDump(GlobalTag.DUMP_NUMBER));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void loadMethodFile(Map<String, String> appClasses) {
		String loc 		= locMgr.getLoc(GlobalTag.INPUT_FILE);
		String fileName	= locMgr.getFileName(GlobalTag.METHOD_FILE);
		
		StupComplexity sc = dc.getStupComplexity();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(loc + "/" + fileName));
			
			String line = null;
			do {
				line = br.readLine();
			    
			    if (line == null) { continue; }
			    
			    String[] strs 	= line.split("\t");
			    String from 		= strs[0];
			    String to 			= strs[1];
			    int sizeOfAttrs 	= Integer.valueOf(strs[2]);
			    
			    String sourceClass = appClasses.get(from);
			    String targetClass = appClasses.get(to);
			    
			    SourceClassInfo sci = sc.getSourceClassInfo(sourceClass);
//			    SourceClassInfo sci = sc.getSourceClassInfo(from);
			    for (int i=0; i<sizeOfAttrs; i++) {
			    	sci.newMethodDependency(new SimpleMethodDependency(sourceClass, targetClass));
//			    	sci.newMethodDependency(new SimpleMethodDependency(from, to));
			    }
			    
			} while (line != null);
			
			System.out.println(sc.toDump(GlobalTag.DUMP_NUMBER));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/*public Properties load(String loadType) {
		Properties properties = new Properties();
		
		String loc 	= locMgr.getLoc(loadType);
		String fileName = locMgr.getFileName(loadType);
		
		try {
			properties.load(new FileInputStream(loc + "/" + fileName));
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return properties;
	}*/
		
}
