package toolkits.dependency;

public class AttributeDependency implements IDependency {
	
	SourceClassAttribute 	dParent;
	TargetClass				dChild;
	
	String type;
	
	boolean b_succ;
	
	public AttributeDependency(
			SourceClassAttribute 	dParent, 
			TargetClass 			dChild,
			String 					type,
			boolean 				b_succ) {
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
	
	public String getParentVariableName() {
		return this.dParent.variableName();
	}
	
	public String getChildClassName() {
		return this.dChild.className();
	}

	public String getType() {
		return this.type;
	}

	public boolean isSucc() {
		return this.b_succ;
	}
	
	public String toDump() {
		StringBuilder sb 	= new StringBuilder();
		String dp_class 	= dParent.className();
		String dp_method	= dParent.methodName();
		int dp_ln 			= dParent.lineNumber();
		String dp_vn 		= dParent.variableName();
		String dc_class 	= dChild.className();
		
		sb.append(type + "\t" + dp_class + "\t" + dp_method 
				+ "\t" + dp_ln + "\t" + dp_vn + "\t" + dc_class + "\t" + b_succ + "\n");
		
		return sb.toString();
	}
}
