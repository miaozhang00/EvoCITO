package toolkits.hierarchy;

import global.GlobalTag;

import java.util.HashSet;
import java.util.Set;

/*
 * 单个类继承信息
 */
public class ClassNode {
	
	String className;
	
	// 节点后继集合
	Set<ClassNode> successors;
	
	// 节点前继集合
	Set<ClassNode> predecessors;
	
	public ClassNode(String className) {
		this.className 		= className;
		this.successors 	= new HashSet<ClassNode>();
		this.predecessors 	= new HashSet<ClassNode>();
	}
	
	public String className() {
		return className;
	}
	
	public void addPredecessor(ClassNode predecessor) {
		this.predecessors.add(predecessor);
	}
	
	public void addSuccessor(ClassNode successor) {
		this.successors.add(successor);
	}
	
	// 获得类的第1层父类
	public Set<ClassNode> getDirectPredecessors() {
		return predecessors;
	}
	
	// 获得类的第1层子类
	public Set<ClassNode> getDirectSuccessors() {
		return successors;
	}
	
	// 获得类的所有子类
	public Set<ClassNode> getAllSuccessors() {
		Set<ClassNode> all = new HashSet<ClassNode>();
		all.addAll(successors);
		for (ClassNode succ : successors) {
			all.addAll(succ.getAllSuccessors());
		}
		return all;
	}
	
	public String toDump() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GlobalTag.CLASS_NODE + "\t" + className + "\n");
		
		// 输出类的前继（第1层）
		sb.append(GlobalTag.PREDECESSOR);
		for (ClassNode pred : predecessors) {
			sb.append("\t" + pred.className());
		}
		sb.append("\n");
		
		// 输出类的后继（第1层）
		sb.append(GlobalTag.SUCCESSOR);
		for (ClassNode succ : successors) {
			sb.append("\t" + succ.className());
		}
		sb.append("\n");
		
		/*// 输出类的后继（所有）
		sb.append(GlobalTag.SUCCESSOR_ALL);
		for (ClassNode succ : getAllSuccessors()) {
			sb.append("\t" + succ.className());
		}
		sb.append("\n");*/
		
		return sb.toString();
	}
	
}