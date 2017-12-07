package global;

public class GlobalTag {

    // 分析的项目
    public final static String PROJECT_NAME = "project_name";
    public final static String PROJECT_CITO = "cito";
    public final static String PROJECT_ATM = "ATMExample";

    // 分析类的类型
    public final static String PROCESS_TYPE = "process_type";
    public final static String PROCESS_ALL = "all"; // 分析所有的类
    public final static String PROCESS_APPLICATIONS = "app"; // 分析应用类

    // 本文分析的应用类
    public final static String MY_APP_CLASSES = "myAppClasses";
    public final static String AP_ATMExample = "ap_ATMExample";

    // 输入的类型
    public final static String INPUT_FILE = "input_file";
    public final static String CLASSES_FILE = "classes";
    public final static String ATTRIBUTE_FILE = "attribute";
    public final static String METHOD_FILE = "method";

    // 转储的类型
    public final static String DUMP_TYPE = "dump_type";
    public final static String DUMP_ALL = "all"; // 输出所有信息
    public final static String DUMP_NUMBER = "num"; // 输出使用数量

    public final static String LOC_CONFIG = "./config";
    public final static String LOC_DUMP = "./dump";

    public final static String FILE_CONFIG = "config";

    public final static String CONFIG = "config";

    public final static String WORK_SPACE = "workspace";
    public final static String CLASS_PATH = "classpath";

    public final static String CLASS_HIERARCHY_GRAPH = "class_hierarchy_graph";
    public final static String CLASS_HIERARCHY_GRAPH_XML = "class_hierarchy_graph_xml";

    public final static String CLASS_NODE = "class_node";
    public final static String SUCCESSOR = "successor";
    public final static String PREDECESSOR = "predecessor";
    public final static String SUCCESSOR_ALL = "succ_all";
    public final static String PREDECESSOR_all = "pred_all";

    public final static String STUP_COMPLEXITY = "stup_complexity";
    public final static String SIZE = "size";

    public final static String METHOD_NAME = "method_name";
    public final static String LINE_NUMBER = "line_number";
    public final static String INVOKE_NAME = "invoke_name";
    public final static String VARIABLE_NAME = "variable_name";
    public final static String IS_SUCC = "is_succ";

    public final static String STUP_COMPLEXITY_XML = "stup_complexity_xml";

    public final static String MAX_SIZE_ATTRS = "max_size_attrs";
    public final static String MAX_SIZE_METHODS = "max_size_methods";

    public final static String PSO_DATA = "pso_data";
    public final static String ITERATIONS = "iterations";
    public final static String PARTICAL_NUM = "paritcal_num";
    public final static String ITERATION = "ite";
    public final static String PARTICAL = "par";

    public final static String NAME = "name";
    public final static String SOURCE_CLASS = "source_class";
    public final static String TARGET_CLASS = "target_class";

    public final static String SIZE_DEP = "size_dep";
    public final static String SIZE_DEP_ATTR = "size_dep_attr";
    public final static String SIZE_DEP_METHOD = "size_dep_method";

    public final static String DEPENDENCY = "dep";

    public final static String TYPE_DEP = "type_dep";
    public final static String DEP_ATTR = "dep_attr";;
    public final static String DEP_METHOD = "dep_method";
    public final static String DEP_AAI = "dep_aai";

    // Type of dependency
    public final static String DEP_AGGREGATION = "dep_aggregation";
    public final static String DEP_ASSOCIATION = "dep_association";
    public final static String DEP_INHERIT = "dep_inherit";

    // Type of AttributeDependency
    public final static String ATTR_DEP_TYPE_ARG = "ad_arg"; // 源类方法参数
    public final static String ATTR_DEP_TYPE_RET = "ad_ret"; // 源类方法返回值
    public final static String ATTR_DEP_TYPE_MEM = "ad_mem"; // 源类成员变量

    // Type of MethodDependency
    public final static String METHOD_DEP_TYPE_INVOKE = "md_invoke"; // 源类方法调用

    public final static String SEPARATE_16 = "****************";
    public final static String SEPARATE_24 = "************************";
    public final static String SEPARATE_32 = "********************************";
    public final static String SEPARATE_64 = "****************************************************************";

}
