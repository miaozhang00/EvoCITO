package toolkits.dependency;

public class MethodDependency implements IDependency {

	SourceClassMethod 	dParent;
	TargetClass			dChild;
	
	String 	type;
	
	boolean b_succ;
	
	public MethodDependency(
			SourceClassMethod 	dParent, 
			TargetClass 		dChild,
			String 				type, 
			boolean 			b_succ
			) {
		this.dParent 	= dParent;
		this.dChild 	= dChild;
		this.type 		= type;
		this.b_succ 	= b_succ;
	}
	
	public String getParentClassName() {
		return this.dParent.className();
	}

	public String getParentMethodName() {
		return this.dParent.methodName();
	}
	
	public int getParentLineNumber() {
		return this.dParent.lineNumber();
	}
	
	public String getParentInvokeName() {
		return this.dParent.invokeName();
	}
	
	public String getChildClassName() {
		return this.dChild.className();
	}

	public String getType() {
		return this.type;
	}
	
	public boolean isSucc() {
		return b_succ;
	}

	public String toDump() {
		StringBuilder sb 	= new StringBuilder();
		String dp_class 	= dParent.className();
		String dp_method	= dParent.methodName();
		int dp_ln 			= dParent.lineNumber();
		String dp_in 		= dParent.invokeName();
		String dc_class 	= dChild.className();
		
		sb.append(type + "\t" + dp_class + "\t" + dp_method 
				+ "\t" + dp_ln + "\t" + dp_in + "\t" + dc_class + "\t" + b_succ + "\n");
		
		return sb.toString();
	}

}
