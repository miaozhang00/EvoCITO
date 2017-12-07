package mgr;

import java.io.File;

import data.DataCenter;

import global.GlobalTag;

/*
 * 路径管理类
 */
public class LocationManager {

    DataCenter dc;

    String locProject;
    String processType;
    String dumpType;
    String input;

    public LocationManager(DataCenter dc) {
        this.dc = dc;
        this.locProject = createFolder(GlobalTag.LOC_DUMP, dc.get(GlobalTag.PROJECT_NAME));
        this.processType = dc.get(GlobalTag.PROCESS_TYPE);
        this.dumpType = dc.get(GlobalTag.DUMP_TYPE);
        this.input = dc.get(GlobalTag.INPUT_FILE);
    }

    public String getLoc(String type) {
        if (GlobalTag.PROJECT_NAME.equals(type)) {
            return locProject;
        } else if (GlobalTag.CONFIG.equals(type)) {
            return GlobalTag.LOC_CONFIG;
        } else if (GlobalTag.INPUT_FILE.equals(type)) {
            return GlobalTag.LOC_CONFIG + "/" + dc.get(GlobalTag.INPUT_FILE);
        } else {
            return null;
        }
    }

    public String getFileName(String type) {
        if (GlobalTag.CONFIG.equals(type)) {
            return GlobalTag.FILE_CONFIG;
        } else if (GlobalTag.STUP_COMPLEXITY.equals(type)) {
            return GlobalTag.STUP_COMPLEXITY + "_" + processType + "_" + dumpType;
        } else if (GlobalTag.STUP_COMPLEXITY_XML.equals(type)) {
            return GlobalTag.STUP_COMPLEXITY + "_" + processType + "_" + dumpType;
        } else if (GlobalTag.CLASS_HIERARCHY_GRAPH.equals(type)) {
            return GlobalTag.CLASS_HIERARCHY_GRAPH;
        } else if (GlobalTag.CLASS_HIERARCHY_GRAPH_XML.equals(type)) {
            return GlobalTag.CLASS_HIERARCHY_GRAPH;
        } else if (GlobalTag.INPUT_FILE.equals(type)) {
            return this.input;
        } else if (GlobalTag.CLASSES_FILE.equals(type)) {
            return GlobalTag.CLASSES_FILE;
        } else if (GlobalTag.ATTRIBUTE_FILE.equals(type)) {
            return GlobalTag.ATTRIBUTE_FILE;
        } else if (GlobalTag.METHOD_FILE.equals(type)) {
            return GlobalTag.METHOD_FILE;
        }

        return null;
    }

    public String createFolder(String folderName) {
        if (GlobalTag.STUP_COMPLEXITY.equals(folderName)) {
            return createFolder(locProject, GlobalTag.STUP_COMPLEXITY);
        } else if (GlobalTag.CLASS_HIERARCHY_GRAPH.equals(folderName)) {
            return createFolder(locProject, GlobalTag.CLASS_HIERARCHY_GRAPH);
        } else {
            return null;
        }
    }

    public String createFolder(String locParent, String folderName) {
        String locFolder = locParent + "/" + folderName;
        File file = new File(locFolder);
        file.mkdir();
        return locFolder;
    }
}
