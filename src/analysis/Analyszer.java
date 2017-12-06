package analysis;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Body;
import soot.Local;
import soot.RefType;
import soot.SootClass;
import soot.SootDriver;
import soot.SootField;
import soot.SootMethod;
import soot.SystemContext;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JimpleLocalBox;
import soot.tagkit.LineNumberTag;
import soot.util.Chain;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;
import toolkits.dependency.AttributeDependency;
import toolkits.dependency.IDependency;
import toolkits.dependency.MethodDependency;
import toolkits.dependency.SourceClassAttribute;
import toolkits.dependency.SourceClassMethod;
import toolkits.dependency.TargetClass;
import toolkits.hierarchy.ClassHierarchyGraph;
import toolkits.hierarchy.ClassNode;
import global.GlobalTag;
import data.DataCenter;

/*
 * ������
 * ��������ݽ��з���
 */
public class Analyszer {
	
	DataCenter dc;
	
	SootDriver sootDriver;
	
	SystemContext system;

	ClassHierarchyGraph classHierarchyGraph;
	
	StupComplexity stupComplexity;
	
	String processType;
	
//	Set<String> myAppClasses;
	
//	Set<SootClass> myAppSootClasses;
	
	Collection<String> appCNs;
	
	SourceClassInfo curSCI;
	
	public Analyszer(DataCenter dc) {
		this.dc = dc;
		this.classHierarchyGraph 	= dc.getClassHierarchyGraph();
		this.stupComplexity 		= dc.getStupComplexity();
		this.processType 			= dc.get(GlobalTag.PROCESS_TYPE);
//		this.myAppClasses 			= dc.getMyAppClasses();
		
		
	}
	
	/*
	 * ��ʼ��
	 */
	public void initial() {
		initialSoot();
		
	}
	
	/*
	 * ��ʼ��Soot
	 */
	public void initialSoot() {
		String class_path = dc.get(GlobalTag.CLASS_PATH);
		String work_space = dc.get(GlobalTag.WORK_SPACE);
		
		this.sootDriver = SootDriver.getInstance();
		this.sootDriver.prosessDir(class_path, work_space);
		this.system = SystemContext.getInstance();
	}
	
	/*
	 * ����myAppSootClasses
	 */
	/*public void genMyAppSootClasses() {
		if (this.myAppClasses != null) {
			this.myAppSootClasses = new HashSet<SootClass>();
		} else {
			System.out.println("22222");
			return;
		}
		
		
		Collection<SootClass> scs = system.getApplicationClasses();
		for (SootClass sc : scs) {
			String cn = sc.getName();
			if (this.myAppClasses.contains(cn)) {
				this.myAppSootClasses.add(sc);
			}
		}
	}*/
	
	/*
	 * ����ClassHierarchyGraph
	 */
	public void genClassHierarchyGraph() {
		Collection<SootClass> scs = system.getApplicationClasses();
//		Collection<SootClass> scs = this.myAppSootClasses;
		
		System.out.println("size of app classes: " + scs.size());
		
		
		
		
		
		
		for (SootClass sc : scs) {
			String className = sc.getName();
			ClassNode cn = classHierarchyGraph.getClassNode(className);
			
			// �����̳й�ϵ
			{
				SootClass superClass = sc.getSuperclass();
				if (superClass.isApplicationClass()) {
					ClassNode superCN = classHierarchyGraph.getClassNode(superClass.getName());
					superCN.addSuccessor(cn);
					cn.addPredecessor(superCN);
				}
			}
			
			// ��ӽӿڼ̳й�ϵ
			{
				Chain<SootClass> interfaces = sc.getInterfaces();
				for (SootClass sc_interface : interfaces) {
					if (sc_interface.isApplicationClass()) {
						ClassNode interfaceCN = classHierarchyGraph.getClassNode(sc_interface.getName());
						interfaceCN.addSuccessor(cn);
						cn.addPredecessor(interfaceCN);
					}
				}
			}
			
		}
		
//		System.out.println(classHierarchyGraph.toDump());
	}
	
	/*
	 * ������Ŀ
	 */
	public void analysis() {
		
		initial();
		
//		genMyAppSootClasses();
		//genClassHierarchyGraph();
		
		Collection<SootClass> scs = system.getApplicationClasses();
		System.out.println("analysis------" + scs.size());//����
//		Collection<SootClass> scs = this.myAppSootClasses;
		this.appCNs = getClassNames(scs);
		for (SootClass sc : scs) {
			System.out.println(sc.getName());//����
			/*1124test
			 * 
			 */
			System.out.println("1124(�s�n�t)���ÿ����ķ���" + sc.getName());//����
			Collection<SootMethod> sms = sc.getMethods();
			for (SootMethod sm : sms) {
				System.out.println(sm.getName());
			}
			System.out.println("1124(�s�n�t)���ÿ�������" + sc.getName());//����
			Chain<SootField> fields = sc.getFields();
			for (SootField sootField : fields) {
				System.out.println(sootField.getName());
				System.out.println(sootField.getType().toString());
			}
			/*1124test
			 * 
			 */
			analysis(sc);
		}
		
		//int NumOfDependency = getNumberOfDependency();
		//System.out.println("size of app dependencies:" + NumOfDependency);	//new
//		System.out.println(stupComplexity.toDump(GlobalTag.DUMP_ALL));
		
	}
	
	/*
	 * ����SootClass
	 */
	public void analysis(SootClass sc) {
		
		String className = sc.getName();
		boolean b_interface = sc.isInterface();
		boolean b_abstract 	= sc.isAbstract();
		
		this.curSCI = stupComplexity.newSourceClassInfo(className, b_interface, b_abstract);
		
		// ����Դ���Ա����������MEM��Ա����
		Chain<SootField> fields = sc.getFields();
		for (SootField sootField : fields) {
			Set<IDependency> iDeps = genAttributeDependencies(className, sootField, GlobalTag.ATTR_DEP_TYPE_MEM);
			this.curSCI.newAttributeDependencies(iDeps);
		}
		
		// ����Դ���Ա����������INVOKE���������Լ�ARG��Ա���ԡ�RET��Ա����
		Collection<SootMethod> sms = sc.getMethods();
		for (SootMethod sm : sms) {
			if (sm.isConcrete()) {
				analysis(className, sm);
			}
		}
	
	}
	
	/*
	 * ����SootMethod
	 */
	public void analysis(String cn, SootMethod sm) {
		
		String methodName = sm.getName();
		Body body = sm.retrieveActiveBody();
		
		// ����ARG��Ա����
		{
			List<?> types = sm.getParameterTypes();
			int size_arg = types.size();
			
			for (int i = 0; i < size_arg; ++i) {
				Object type = types.get(i);
				Local local = body.getParameterLocal(i);
				
				Set<IDependency> iDeps = genAttributeDependencies(cn, methodName, (Type) type, local.toString(), GlobalTag.ATTR_DEP_TYPE_ARG);
				this.curSCI.newAttributeDependencies(iDeps);
			}
		}
		
		// ����RET��Ա����
		{
			Type type = sm.getReturnType();
			
			Set<IDependency> iDeps = genAttributeDependencies(cn, methodName, type, GlobalTag.ATTR_DEP_TYPE_RET);
			this.curSCI.newAttributeDependencies(iDeps);
		}
		
		// ����INVOKE��������
		{
			Collection<Unit> units = body.getUnits();
			for (Unit unit : units) {
				Stmt stmt = (Stmt) unit;
				analysis(cn, methodName, stmt);
			}
		}
		
	}
	
	/*
	 * ����Stmt
	 */
	public void analysis(String cn, String mn, Stmt stmt) {
		LineNumberTag lnTag = (LineNumberTag) stmt.getTag("LineNumberTag");
		int lineNumber = (lnTag == null)?0:lnTag.getLineNumber();
		
		Set<IDependency> iDeps = genMethodDependencies(cn, mn, lineNumber, stmt, GlobalTag.METHOD_DEP_TYPE_INVOKE);
		this.curSCI.newMethodDependencies(iDeps);
	}
	
	public Set<IDependency> genAttributeDependencies(String scn, SootField sf, String attr_type) {
		Set<IDependency> attrDeps = new HashSet<IDependency>();
		if ( sf == null || !(sf.getType() instanceof RefType) ) {
			return attrDeps;
		}
		
		RefType type 	= (RefType) sf.getType();
		String varName 	= sf.getName();
		SootClass sc_sf	= type.getSootClass();
		String targetClassName = sc_sf.getName();
		
		ClassNode targetCN = classHierarchyGraph.getClassNode(targetClassName);
		if (null == targetCN) {
			return attrDeps;
		}
		
		if (GlobalTag.PROCESS_APPLICATIONS.equals(processType)
				&& !appCNs.contains(targetClassName)) {
			return attrDeps;
		} 
		
		// ���Դ����Ŀ���ࣨ�ӿڣ�����������
		{
			SourceClassAttribute dParent = new SourceClassAttribute(scn, null, 0, varName);
			TargetClass dChild = new TargetClass(targetClassName);
			attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, false));
		}
		
		// ���Դ����Ŀ���ࣨ�ӿڣ��������������
		{
			Set<ClassNode> succs_target = targetCN.getAllSuccessors();
			for (ClassNode succ_target : succs_target) {
				SourceClassAttribute dParent = new SourceClassAttribute(scn, null, 0, varName);
				TargetClass dChild = new TargetClass(succ_target.className());
				attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, true));
			}
		}
		
		return attrDeps;
	}
	
	public Set<IDependency> genAttributeDependencies(String scn, String mn, Type type, String varName, String attr_type) {
		Set<IDependency> attrDeps = new HashSet<IDependency>();
		if ( !(type instanceof RefType) ) {
			return attrDeps;
		}
		
		RefType retType = (RefType) type;
		SootClass sc_sf	= retType.getSootClass();
		String targetClassName = sc_sf.getName();
		
		ClassNode targetCN = classHierarchyGraph.getClassNode(targetClassName);
		if (null == targetCN) {
			return attrDeps;
		}
		
		if (GlobalTag.PROCESS_APPLICATIONS.equals(processType)
				&& !appCNs.contains(targetClassName)) {
			return attrDeps;
		} 
		
		// ���Դ����Ŀ���ࣨ�ӿڣ�����������
		{
			SourceClassAttribute dParent = new SourceClassAttribute(scn, null, 0, varName);
			TargetClass dChild = new TargetClass(targetClassName);
			attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, false));
		}
		
		// ���Դ����Ŀ���ࣨ�ӿڣ��������������
		{
			Set<ClassNode> succs_target = targetCN.getAllSuccessors();
			for (ClassNode succ_target : succs_target) {
				SourceClassAttribute dParent = new SourceClassAttribute(scn, null, 0, varName);
				TargetClass dChild = new TargetClass(succ_target.className());
				attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, true));
			}
		}
		
		return attrDeps;
	}
	
	public Set<IDependency> genAttributeDependencies(String scn, String mn, Type type, String attr_type) {
		Set<IDependency> attrDeps = new HashSet<IDependency>();
		if ( !(type instanceof RefType) ) {
			return attrDeps;
		}
		
		RefType retType = (RefType) type;
		SootClass sc_sf	= retType.getSootClass();
		String targetClassName = sc_sf.getName();
		
		ClassNode targetCN = classHierarchyGraph.getClassNode(targetClassName);
		if (null == targetCN) {
			return attrDeps;
		}
		
		if (GlobalTag.PROCESS_APPLICATIONS.equals(processType)
				&& !appCNs.contains(targetClassName)) {
			return attrDeps;
		} 
		
		// ���Դ����Ŀ���ࣨ�ӿڣ�����������
		{
			SourceClassAttribute dParent = new SourceClassAttribute(scn, mn, 0, null);
			TargetClass dChild = new TargetClass(targetClassName);
			attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, false));
		}
		
		// ���Դ����Ŀ���ࣨ�ӿڣ��������������
		{
			Set<ClassNode> succs_target = targetCN.getAllSuccessors();
			for (ClassNode succ_target : succs_target) {
				SourceClassAttribute dParent = new SourceClassAttribute(scn, mn, 0, null);
				TargetClass dChild = new TargetClass(succ_target.className());
				attrDeps.add(new AttributeDependency(dParent, dChild, attr_type, true));
			}
		}
		
		return attrDeps;
	}
	
	public Set<IDependency> genMethodDependencies(String scn, String smn, int sln, Stmt stmt, String attr_type) {
		
		Set<IDependency> methodDeps = new HashSet<IDependency>();
		if (!(stmt instanceof JInvokeStmt)) {
			return methodDeps;
		}
		
		InvokeExpr ie = stmt.getInvokeExpr();
		SootMethod sm = ie.getMethod();
		String in = sm.getName();
		
		SourceClassMethod dParent = new SourceClassMethod(scn, smn, sln, in);
		
		List<?> useboxes = stmt.getUseBoxes();
		for (Object usebox : useboxes) {
			if (usebox instanceof JimpleLocalBox) {
				Value value = ((JimpleLocalBox) usebox).getValue();
				Type type = value.getType();
				
				String targetClassName = type.toString();
				
				ClassNode targetCN = classHierarchyGraph.getClassNode(targetClassName);
				if (null == targetCN) {
					return methodDeps;
				}
				
				if (GlobalTag.PROCESS_APPLICATIONS.equals(processType) 
						&& !appCNs.contains(targetClassName)) {
					return methodDeps;
				}
				
				// ���Դ����Ŀ���ࣨ�ӿڣ�����������
				{
					TargetClass dChild = new TargetClass(targetClassName);
					methodDeps.add(new MethodDependency(dParent, dChild, attr_type, false));
				}
				
				// ���Դ����Ŀ���ࣨ�ӿڣ��������������
				{
					Set<ClassNode> succs_target = targetCN.getAllSuccessors();
					for (ClassNode succ_target : succs_target) {
						TargetClass dChild = new TargetClass(succ_target.className());
						methodDeps.add(new MethodDependency(dParent, dChild, attr_type, true));
					}
				}
			}
		}
		
		return methodDeps;
		
	}
	
	public Collection<String> getClassNames(Collection<SootClass> scs) {
		Collection<String> classNames = new HashSet<String>();
		for (SootClass sc : scs) {
			classNames.add(sc.getName());
		}
		return classNames;
	}
	
	public boolean contains(Collection<String> strs, String s) {
		for (String str : strs) {
			if (str.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public int getNumberOfDependency(){
	//-------new of mew----
	int NumberOfDependency = 0;
	Collection<SootClass> scs = system.getApplicationClasses();
			
	for(int i=1;i<= scs.size();i++){		
			
		for(SootClass sc : scs){
			SourceClassInfo sci = stupComplexity.newSourceClassInfo(sc.getName());
			System.out.println(sc.getName());			
			System.out.println(NumberOfAttrDeps(sci));
			System.out.println(NumberOfMethodDeps(sci));
			NumberOfDependency = NumberOfDependency + NumberOfDeps(sci);
			//System.out.println(sc.getName()); 
			//System.out.println(sci.getSizeOfDeps());
		}
					}		
	
	return NumberOfDependency;
					//-------new of mew-------
	}		
	public int NumberOfAttrDeps(SourceClassInfo sci){
		
			String className = sci.className();
			
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> attrClasses 	= sci.getAttrClasses();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			int sumOfAttr =0;
			for (String depClass : depClasses) {
				int sizeOfAttr 	= sci.getSizeOfAttrDeps(depClass);
				sumOfAttr = sumOfAttr + sizeOfAttr;
				int sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
			}
			return sumOfAttr;
		}
	
	public int NumberOfMethodDeps(SourceClassInfo sci){
		
		String className = sci.className();
		
		Set<String> depClasses 		= new HashSet<String>();
		Set<String> attrClasses 	= sci.getAttrClasses();
		Set<String> methodClasses 	= sci.getMethodClasses();
		depClasses.addAll(attrClasses);
		depClasses.addAll(methodClasses);
		
		int sumOfMethod =0;
		for (String depClass : depClasses) {
			int sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
			sumOfMethod = sumOfMethod + sizeOfMethod;
		}
		return sumOfMethod;
	}

	public int NumberOfDeps(SourceClassInfo sci){
		return NumberOfAttrDeps(sci)+ NumberOfMethodDeps(sci);
	}

}
