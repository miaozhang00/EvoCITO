package data;

import global.GlobalTag;

import java.util.Properties;
import java.util.Set;

import toolkits.StupComplexity;
import toolkits.hierarchy.ClassHierarchyGraph;
import toolkits.pso.PSOData;

/*
 * ����������
 * �����¼��������ʱ��������������
 */
public class DataCenter {

	Properties config;
	
	Set<String> myAppClasses;
	
	/*
	 * projectName	��Ŀ����
	 * processType	��������
	 * dumpType		�������
	 * inputFile	�����ļ�
	 */
	String projectName;
	String processType;
	String dumpType;
	String inputFile;
	
	StupComplexity stupComplexity;
	
	ClassHierarchyGraph classHierarchyGraph;
	
	PSOData psoData;
	
	/*
	 * projectName	��Ŀ����
	 * processType	��������
	 * dumpType		�������
	 * inputFile	�����ļ�
	 */
	public DataCenter(Properties config) {
		
		this.config 		= config;
		this.projectName 	= config.getProperty(GlobalTag.PROJECT_NAME);
		this.processType 	= config.getProperty(GlobalTag.PROCESS_TYPE);
		this.dumpType 		= config.getProperty(GlobalTag.DUMP_TYPE);
		this.inputFile 		= config.getProperty(GlobalTag.INPUT_FILE);
		
		this.stupComplexity = new StupComplexity();
		this.classHierarchyGraph = new ClassHierarchyGraph();
	}

	public String get(String type) {
		if (GlobalTag.WORK_SPACE.equals(type)) {
			return this.config.getProperty(GlobalTag.WORK_SPACE);
		} else if (GlobalTag.CLASS_PATH.equals(type)) {
			return this.config.getProperty(GlobalTag.CLASS_PATH);
		} else if (GlobalTag.PROJECT_NAME.equals(type)) {
			return projectName;
		} else if (GlobalTag.PROCESS_TYPE.equals(type)) {
			return processType;
		} else if (GlobalTag.DUMP_TYPE.equals(type)) {
			return dumpType;
		} else if (GlobalTag.INPUT_FILE.equals(type)) {
			return inputFile;
		} else {
			return null;
		}
	}
	
	public ClassHierarchyGraph getClassHierarchyGraph() {
		return classHierarchyGraph;
	}
	
	public StupComplexity getStupComplexity() {
		return stupComplexity;
	}
	
	public PSOData getPSOData() {
		return psoData;
	}
	
	public Set<String> getMyAppClasses() {
		return this.myAppClasses;
	}
	
	public void setMyAppClasses(Set<String> myAppClasses) {
		this.myAppClasses = myAppClasses;
	}
	
}
