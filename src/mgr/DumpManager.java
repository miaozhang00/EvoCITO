package mgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.DataCenter;

import global.GlobalTag;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;
import toolkits.dependency.AttributeDependency;
import toolkits.dependency.IDependency;
import toolkits.dependency.MethodDependency;
import toolkits.dependency.SimpleAttributeDependency;
import toolkits.dependency.SimpleMethodDependency;
import toolkits.hierarchy.ClassHierarchyGraph;
import toolkits.hierarchy.ClassNode;

public class DumpManager {

	DataCenter dc;
	
	LocationManager locMgr;
	
	String dumpType;
	
	File file;
	FileWriter fileWriter;
	
	private DocumentBuilderFactory	documentBuilderFactory;
	private DocumentBuilder			documentBuilder;
	
	private DOMSource		domSource;
	private PrintWriter		printWriter;
	private StreamResult	streamResult;
	
	private Document		doc;
	private Element			eeeRoot;
	
	public DumpManager(DataCenter dc, LocationManager locMgr) {
		this.dc 		= dc;
		this.locMgr 	= locMgr;
		this.dumpType 	= dc.get(GlobalTag.DUMP_TYPE);
		
		try {
			this.documentBuilderFactory	= DocumentBuilderFactory.newInstance();
			this.documentBuilder			= documentBuilderFactory.newDocumentBuilder();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	public void dump(String dumpObject) {
		if (GlobalTag.STUP_COMPLEXITY.equals(dumpObject)) {
			
			String loc 	= locMgr.createFolder(GlobalTag.STUP_COMPLEXITY);
			StupComplexity sc = this.dc.getStupComplexity();
			dump(loc, sc);
			dumpXML(loc, sc);
			
		} else if (GlobalTag.CLASS_HIERARCHY_GRAPH.equals(dumpObject)) {
			
			String loc 	= locMgr.createFolder(GlobalTag.CLASS_HIERARCHY_GRAPH);
			ClassHierarchyGraph chg = this.dc.getClassHierarchyGraph();
			dump(loc, chg);
			dumpXML(loc, chg);
			
		} else if (GlobalTag.PSO_DATA.equals(dumpObject)) {
//			PSOData psoData = this.dc.getPSOData();
//			dump(psoData);
		}
	}
	
	private void dumpXML(String loc, StupComplexity stupComplexity) {
		String file_name	= locMgr.getFileName(GlobalTag.STUP_COMPLEXITY_XML);
		String name = GlobalTag.STUP_COMPLEXITY;
		
		initial(name);
		writeDoc(stupComplexity);
		dump(loc, file_name);
	}
	
	private void dumpXML(String loc, ClassHierarchyGraph chg) {
		String file_name 	= locMgr.getFileName(GlobalTag.CLASS_HIERARCHY_GRAPH_XML);
		String name = GlobalTag.CLASS_HIERARCHY_GRAPH;
		
		initial(name);
		writeDoc(chg);
		dump(loc, file_name);
	}
	
	public void initial(String type) {
		doc = (Document) documentBuilder.newDocument();	
		
		eeeRoot = doc.createElement(type);
		
		doc.appendChild(eeeRoot);
	}
	
	public void dump(String folderLoc, String fileName) {
		try {
			TransformerFactory	transformerFactory	= TransformerFactory.newInstance();
			Transformer			transformer			= transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			domSource		= new DOMSource(doc);
			printWriter		= new PrintWriter(new FileOutputStream(folderLoc + "/" + fileName + ".xml"));
			streamResult	= new StreamResult(printWriter);
			
			transformer.transform(domSource, streamResult);
			printWriter.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void writeDoc(StupComplexity stupComplexity) {
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for (SourceClassInfo sci : sci_list) {
			writeDoc(sci);
		}
	}
	
	public void writeDoc(ClassHierarchyGraph classHierarchyGraph) {
		Set<ClassNode> nodes = classHierarchyGraph.getClassNodes();
		for (ClassNode cn : nodes) {
			writeDoc(cn);
		}
	}
	
	public void writeDoc(SourceClassInfo sci) {
		
		// 添加源类
		Element eeeSourceClass = addElement(eeeRoot, GlobalTag.SOURCE_CLASS);
		
		// 添加源类名称
		{
			String sourceClassName = sci.className();
			addAttr(eeeSourceClass, GlobalTag.NAME, sourceClassName);
		}
		
		// 添加依赖数目
		{
			int sizeOfDeps = sci.getSizeOfDeps();
			addAttr(eeeSourceClass, GlobalTag.SIZE_DEP, sizeOfDeps);
		}
		
		// 添加属性依赖信息
		{
			Element eeeAttr = addElement(eeeSourceClass, GlobalTag.DEP_ATTR);
			
			// 添加属性依赖数目
			{
				int sizeOfDepAttrs = sci.getSizeOfAttrDeps();
				addAttr(eeeAttr, GlobalTag.SIZE_DEP_ATTR, sizeOfDepAttrs);
			}
			
			// 添加属性依赖
			{
				Set<String> attr_classes = sci.getAttrClasses();
				for (String attr_class : attr_classes) {

					// 添加目标类信息
					Element eeeTargetClass = addElement(eeeAttr, GlobalTag.TARGET_CLASS);
					
					// 添加目标类名称
					{
						addAttr(eeeTargetClass, GlobalTag.NAME, attr_class);
					}
					
					Set<IDependency> deps = sci.getAttrDependencies(attr_class);
					
					// 添加属性依赖数目
					{
						int size = deps.size();
						addAttr(eeeTargetClass, GlobalTag.SIZE_DEP_ATTR, size);
					}
					
					// 添加属性依赖
					for (IDependency dep : deps) {
						writeDoc(eeeTargetClass, dep);
					}
				}
			}
		}
		
		// 添加方法依赖信息
		{
			Element eeeMethod = addElement(eeeSourceClass, GlobalTag.DEP_METHOD);
			
			// 添加方法依赖数目
			{
				int sizeOfDepMethods = sci.getSizeOfMethodDeps();
				addAttr(eeeMethod, GlobalTag.SIZE_DEP_METHOD, sizeOfDepMethods);
			}
			
			// 添加属性依赖
			{
				Set<String> method_classes = sci.getMethodClasses();
				for (String method_class : method_classes) {

					// 添加目标类信息
					Element eeeTargetClass = addElement(eeeMethod, GlobalTag.TARGET_CLASS);

					// 添加目标类名称
					{
						addAttr(eeeTargetClass, GlobalTag.NAME, method_class);
					}

					Set<IDependency> deps = sci.getMethodDependencies(method_class);

					// 添加属性依赖数目
					{
						int size = deps.size();
						addAttr(eeeTargetClass, GlobalTag.SIZE_DEP_METHOD, size);
					}

					// 添加属性依赖
					for (IDependency dep : deps) {
						writeDoc(eeeTargetClass, dep);
					}
				}
			}
		}
		
	}
	
	public void writeDoc(Element eeeParent, IDependency iDep) {
		if (iDep instanceof AttributeDependency) {
			writeDoc(eeeParent, (AttributeDependency) iDep);
		} else if (iDep instanceof MethodDependency) {
			writeDoc(eeeParent, (MethodDependency) iDep);
		} else if (iDep instanceof SimpleAttributeDependency) {
			writeDoc(eeeParent, (SimpleAttributeDependency) iDep);
		} else if (iDep instanceof SimpleMethodDependency) {
			writeDoc(eeeParent, (SimpleMethodDependency) iDep);
		}
	}
	
	public void writeDoc(Element eeeParent, AttributeDependency dep) {
		
		Element eeeDep 	= addElement(eeeParent, GlobalTag.DEPENDENCY);
		
		String 	type			= dep.getType();
		String 	pc_methodName 	= dep.getParentMethodName();
		int 	pc_lineNumber 	= dep.getParentLineNumber();
		String	pc_varName 		= dep.getParentVariableName();
		boolean b_succ 			= dep.isSucc();
		
		addAttr(eeeDep, GlobalTag.TYPE_DEP, 		type);
		addAttr(eeeDep, GlobalTag.METHOD_NAME, 		pc_methodName);
		addAttr(eeeDep, GlobalTag.LINE_NUMBER, 		pc_lineNumber);
		addAttr(eeeDep, GlobalTag.VARIABLE_NAME, 	pc_varName);
		addAttr(eeeDep, GlobalTag.IS_SUCC, 			b_succ);
		
	}
	
	public void writeDoc(Element eeeParent, MethodDependency dep) {
		
		Element eeeDep 	= addElement(eeeParent, GlobalTag.DEPENDENCY);
		
		String 	type			= dep.getType();
		String 	pc_methodName 	= dep.getParentMethodName();
		int 	pc_lineNumber 	= dep.getParentLineNumber();
		String	pc_invokeName	= dep.getParentInvokeName();
		boolean b_succ 			= dep.isSucc();
		
		addAttr(eeeDep, GlobalTag.TYPE_DEP, 		type);
		addAttr(eeeDep, GlobalTag.METHOD_NAME, 		pc_methodName);
		addAttr(eeeDep, GlobalTag.LINE_NUMBER, 		pc_lineNumber);
		addAttr(eeeDep, GlobalTag.INVOKE_NAME, 	pc_invokeName);
		addAttr(eeeDep, GlobalTag.IS_SUCC, 			b_succ);
	
	}
	
	public void writeDoc(Element eeeParent, SimpleAttributeDependency dep) {
		return;
	}
	
	public void writeDoc(Element eeeParent, SimpleMethodDependency dep) {
		return;
	}
	
	public void writeDoc(ClassNode cn) {
		Element eeeCN = addElement(eeeRoot, GlobalTag.CLASS_NODE);
		
		String name = cn.className();
		addAttr(eeeCN, GlobalTag.NAME, name);
		
		Set<ClassNode> preds = cn.getDirectPredecessors();
		for (ClassNode pred : preds) {
			name = pred.className();
			Element eeePred = addElement(eeeCN, GlobalTag.PREDECESSOR);
			addAttr(eeePred, GlobalTag.NAME, name);
		}
		
		Set<ClassNode> susss = cn.getDirectSuccessors();
		for (ClassNode succ : susss) {
			name = succ.className();
			Element eeePred = addElement(eeeCN, GlobalTag.SUCCESSOR);
			addAttr(eeePred, GlobalTag.NAME, name);
		}
		
	}
	
	private void dump(String loc, StupComplexity stupComplexity) {
		String name	= locMgr.getFileName(GlobalTag.STUP_COMPLEXITY);
		
		file = new File(loc + "/" + name);
		fileWriter = null;
		
		try {
			file.createNewFile();
			fileWriter = new FileWriter(file);
			fileWriter.write(stupComplexity.toDump(dumpType));
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void dump(String loc, ClassHierarchyGraph classHierarchyGraph) {
//		String loc 	= locMgr.createFolder(GlobalTag.CLASS_HIERARCHY_GRAPH);
		String name = locMgr.getFileName(GlobalTag.CLASS_HIERARCHY_GRAPH);
		
		file = new File(loc + "/" + name);
		fileWriter = null;
		
		try {
			file.createNewFile();
			fileWriter = new FileWriter(file);
			fileWriter.write(classHierarchyGraph.toDump());
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Element addElement(Element parent, String child) {
//		System.out.println(child);
		Element eee = doc.createElement(child);
		parent.appendChild(eee);
		return eee;
	}
	
	private Element addElement(Element parent, int child) {
		Element eee = doc.createElement("_" + String.valueOf(child));
		parent.appendChild(eee);
		return eee;
	}
	
	private Element addElement(Element parent, int child, String value) {
		Element eee = addElement(parent, child);
		eee.appendChild(doc.createTextNode(value));
		return eee;
	}
	
	private void addAttr(Element eee, String name, String value) {
		eee.setAttribute(name, value);
	}
	
	private void addAttr(Element eee, String name, int value) {
		eee.setAttribute(name, String.valueOf(value));
	}
	
	private void addAttr(Element eee, String name, boolean value) {
		String _value = null;
		if (value == true) {
			_value = "0";
		} else {
			_value = "1";
		}
		eee.setAttribute(name, _value);
	}
	
}
