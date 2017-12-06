package toolkits.hierarchy;

import global.GlobalTag;

import java.util.HashSet;
import java.util.Set;

/*
 * ������̳���Ϣ
 */
public class ClassNode {
	
	String className;
	
	// �ڵ��̼���
	Set<ClassNode> successors;
	
	// �ڵ�ǰ�̼���
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
	
	// �����ĵ�1�㸸��
	public Set<ClassNode> getDirectPredecessors() {
		return predecessors;
	}
	
	// �����ĵ�1������
	public Set<ClassNode> getDirectSuccessors() {
		return successors;
	}
	
	// ��������������
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
		
		// ������ǰ�̣���1�㣩
		sb.append(GlobalTag.PREDECESSOR);
		for (ClassNode pred : predecessors) {
			sb.append("\t" + pred.className());
		}
		sb.append("\n");
		
		// �����ĺ�̣���1�㣩
		sb.append(GlobalTag.SUCCESSOR);
		for (ClassNode succ : successors) {
			sb.append("\t" + succ.className());
		}
		sb.append("\n");
		
		/*// �����ĺ�̣����У�
		sb.append(GlobalTag.SUCCESSOR_ALL);
		for (ClassNode succ : getAllSuccessors()) {
			sb.append("\t" + succ.className());
		}
		sb.append("\n");*/
		
		return sb.toString();
	}
	
}