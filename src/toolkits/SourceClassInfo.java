package toolkits;

import global.GlobalTag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import toolkits.dependency.IDependency;

public class SourceClassInfo {
	
	// 源类名
	String className;
	
	// 接口
	boolean b_interface;
	
	// 抽象类
	boolean b_abstract;
	
	// 属性依赖信息，每个目标类组成一个集合
	Map<String, Set<IDependency>> attrDeps;
	
	// 方法依赖信息，每个目标类组成一个集合
	Map<String, Set<IDependency>> methodDeps;
	
	// aai(Aggregation, Association, Inherit)依赖信息，每个目标类组成一个集合
	Map<String, Set<IDependency>> aaiDeps;
	
	int size_max_attrDeps;
	int size_max_methodDeps;
	
	int size_attrDep;
	int size_methodDep;
	int size_aaiDep;
	int size_Dep;
	
	public SourceClassInfo(
			String className, 
			boolean b_interface,
			boolean b_abstract
			) {
		this.className		= className;
		this.b_interface 	= b_interface;
		this.b_abstract 	= b_abstract;
		
		this.attrDeps 		= new HashMap<String, Set<IDependency>>();
		this.methodDeps		= new HashMap<String, Set<IDependency>>();
		this.aaiDeps 		= new HashMap<String, Set<IDependency>>();
		
		this.size_max_attrDeps 		= 0;
		this.size_max_methodDeps	= 0; 
		
		this.size_attrDep 	= 0;
		this.size_methodDep = 0;
		this.size_aaiDep	= 0;
		this.size_Dep 		= 0;
		
//		this.b_evaluate 	= false;
	}
	
	public String className() {
		return this.className;
	}
	
	public Set<String> getAttrClasses() {
		return this.attrDeps.keySet();
	}
	
	public Set<String> getMethodClasses() {
		return this.methodDeps.keySet();
	}
	
	public Set<IDependency> getAttrDependencies(String className) {
		return this.attrDeps.get(className);
	}
	
	public Set<IDependency> getMethodDependencies(String className) {
		return this.methodDeps.get(className);
	}
	
//	public void doEvaluate() {
//		this.b_evaluate = true;
//	}
//	
//	public boolean isEvaluate() {
//		return b_evaluate;
//	}
	
	// 添加属性依赖
	public void newAttributeDependency(IDependency iDep) {
		if (null == iDep) {
			return;
		} else {
			String targetClassName = iDep.getChildClassName();
			Set<IDependency> iDeps = null;
			
			if (attrDeps.keySet().contains(targetClassName)) {
				iDeps = attrDeps.get(targetClassName);
				iDeps.add(iDep);
			} else {
				iDeps = new HashSet<IDependency>();
				iDeps.add(iDep);
				attrDeps.put(targetClassName, iDeps);
			}
			
			if (iDeps.size() > size_max_attrDeps) {
				size_max_attrDeps = iDeps.size();
			}
			
			this.size_attrDep++;
			this.size_Dep++;
		}
	}
	
	public void newAttributeDependencies(Set<IDependency> iDeps) {
		for (IDependency iDep : iDeps) {
			newAttributeDependency(iDep);
		}
	}
	
	// 添加方法依赖
	public void newMethodDependency(IDependency iDep) {
		if (null == iDep) {
			return;
		} else {
			String targetClassName = iDep.getChildClassName();
			Set<IDependency> iDeps = null;
			
			if (methodDeps.keySet().contains(targetClassName)) {
				iDeps = methodDeps.get(targetClassName);
				iDeps.add(iDep);
			} else {
				iDeps = new HashSet<IDependency>();
				iDeps.add(iDep);
				methodDeps.put(targetClassName, iDeps);
			}
			
			if (iDeps.size() > size_max_methodDeps) {
				size_max_methodDeps = iDeps.size();
			}
			
			this.size_methodDep++;
			this.size_Dep++;
		}
	}
	
	public void newMethodDependencies(Set<IDependency> iDeps) {
		for (IDependency iDep : iDeps) {
			newMethodDependency(iDep);
		}
	}
	
	public void newAAIDependence(IDependency iDep) {
		if (null == iDep) {
			return;
		} else {
			String targetClassName = iDep.getChildClassName();
			Set<IDependency> iDeps = null;
			
			if (aaiDeps.keySet().contains(targetClassName)) {
				iDeps = aaiDeps.get(targetClassName);
				iDeps.add(iDep);
			} else {
				iDeps = new HashSet<IDependency>();
				iDeps.add(iDep);
				aaiDeps.put(targetClassName, iDeps);
			}
			
			this.size_aaiDep++;
			this.size_Dep++;
		}
		
	}
	
	public int getSizeOfAttrDeps() {
		return size_attrDep;
	}
	
	public int getSizeOfMethodDeps() {
		return size_methodDep;
	}
	
	public int getSizeOfAAIDeps() {
		return size_aaiDep;
	}
	
	public int getSizeOfDeps() {
		return size_Dep;
	}
	
	public int getSizeOfAttrDeps(String targetClassName) {
		boolean b = attrDeps.containsKey(targetClassName)?true:false;
		if (b) {
			Set<IDependency> deps = attrDeps.get(targetClassName);
			return deps.size();
		} else {
			return 0;
		}
	}
	
	public int getSizeOfMethodDeps(String targetClassName) {
		boolean b = methodDeps.containsKey(targetClassName)?true:false;
		if (b) {
			Set<IDependency> deps = methodDeps.get(targetClassName);
			return deps.size();
		} else {
			return 0;
		}
	}
	
	public int getSizeOfAAIDeps(String targetClassName) {
		boolean b = aaiDeps.containsKey(targetClassName)?true:false;
		if (b) {
			Set<IDependency> deps = aaiDeps.get(targetClassName);
			return deps.size();
		} else {
			return 0;
		}
	}
	
	public int getMaxSizeOfAttrs() {
		return this.size_max_attrDeps;
	}
	
	public int getMaxSizeOfMethods() {
		return this.size_max_methodDeps;
	}
	
	public String toDump(String dumpType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GlobalTag.SOURCE_CLASS + "\t" + className + "\t" + size_Dep + "\n");
		
		{
			sb.append(GlobalTag.DEP_ATTR + "\t" + size_attrDep + "\n");
			Set<String> keys = attrDeps.keySet();
			for (String target : keys) {
				int size = getSizeOfAttrDeps(target);
				sb.append("\t" + GlobalTag.TARGET_CLASS + "\t" + target 
						+ "\t" + size + "\n");
			
				if (GlobalTag.DUMP_ALL.equals(dumpType)) {
					Set<IDependency> iDeps = attrDeps.get(target);
					for (IDependency iDep : iDeps) {
						sb.append("\t\t" + iDep.toDump());
					}
				}
			}
		}
		
		{
			sb.append(GlobalTag.DEP_METHOD + "\t" + size_methodDep + "\n");
			Set<String> keys = methodDeps.keySet();
			for (String target : keys) {
				int size = getSizeOfMethodDeps(target);
				sb.append("\t" + GlobalTag.TARGET_CLASS + "\t" + target 
						+ "\t" + size + "\n");
				
				if (GlobalTag.DUMP_ALL.equals(dumpType)) {
					Set<IDependency> iDeps = methodDeps.get(target);
					for (IDependency iDep : iDeps) {
						sb.append("\t\t" + iDep.toDump());
					}
				}
			}
		}
		
		/*{
			sb.append(GlobalTag.AAI_DEP + "\t" + size_aaiDep + "\n");
			Set<String> keys = aaiDeps.keySet();
			for (String target : keys) {
				int size = getSizeOfAAIDeps(target);
				sb.append("\t" + GlobalTag.TARGET_CLASS + "\t" + target 
						+ "\t" + size + "\n");
				
				if (GlobalTag.DUMP_ALL.equals(dumpType)) {
					Set<IDependency> iDeps = aaiDeps.get(target);
					for (IDependency iDep : iDeps) {
						sb.append("\t\t" + iDep.toDump());
					}
				}
			}
		}*/
		
		return sb.toString();
	}
	
}
