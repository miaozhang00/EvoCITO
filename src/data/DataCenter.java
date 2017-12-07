package data;

import global.GlobalTag;

import java.util.Properties;
import java.util.Set;

import toolkits.StupComplexity;
import toolkits.hierarchy.ClassHierarchyGraph;
import toolkits.pso.PSOData;

/*
 * 数据中心类
 * 负责记录程序运行时产生的所有数据
 */
public class DataCenter {

    Properties config;

    Set<String> myAppClasses;

    /*
     * projectName 项目名称 processType 处理类型 dumpType 输出类型 inputFile 输入文件
     */
    String projectName;
    String processType;
    String dumpType;
    String inputFile;

    StupComplexity stupComplexity;

    ClassHierarchyGraph classHierarchyGraph;

    PSOData psoData;

    /*
     * projectName 项目名称 processType 处理类型 dumpType 输出类型 inputFile 输入文件
     */
    public DataCenter(Properties config) {

        this.config = config;
        this.projectName = config.getProperty(GlobalTag.PROJECT_NAME);
        this.processType = config.getProperty(GlobalTag.PROCESS_TYPE);
        this.dumpType = config.getProperty(GlobalTag.DUMP_TYPE);
        this.inputFile = config.getProperty(GlobalTag.INPUT_FILE);

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
