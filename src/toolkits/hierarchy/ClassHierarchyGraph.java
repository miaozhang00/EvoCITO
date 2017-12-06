package toolkits.hierarchy;

import global.GlobalTag;

import java.util.HashSet;
import java.util.Set;

/*
 * 所有类继承信息
 * 为每个Class生成一个ClassNode，记录它们的继承关系
 */
public class ClassHierarchyGraph {

	Set<ClassNode> classNodes;
	
	public ClassHierarchyGraph() {
		this.classNodes = new HashSet<ClassNode>();
	}
	
	public Set<ClassNode> getClassNodes() {
		return classNodes;
	}
	
	public ClassNode getClassNode(String className) {
		for (ClassNode cn : classNodes) {
			if (cn.className().equals(className)) {
				return cn;
			}
		}
		
		ClassNode cn = new ClassNode(className);
		this.classNodes.add(cn);
		return cn;
	}
	
	public String toDump() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.CLASS_HIERARCHY_GRAPH + "\n");
		
		for (ClassNode cn : classNodes) {
			sb.append("\n");
			sb.append(cn.toDump());
		}
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		
		return sb.toString();
	}
	
}
