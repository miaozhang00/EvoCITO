package toolkits;

import global.GlobalTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StupComplexity {
	
	Set<SourceClassInfo> 	sci_set;
	
	List<SourceClassInfo> 	sci_list;
	
	int size_sci;
	
	public StupComplexity() {
		this.sci_set 	= new HashSet<SourceClassInfo>();
		this.sci_list 	= new ArrayList<SourceClassInfo>();
	}
	
	public SourceClassInfo newSourceClassInfo(
			String 	className, 
			boolean b_interface,
			boolean b_abstract) {
		SourceClassInfo sci = new SourceClassInfo(className, b_interface, b_abstract);
		this.sci_set.add(sci);
		this.sci_list.add(sci);
		this.size_sci ++;
		return sci;
	}
	
	public SourceClassInfo newSourceClassInfo(
			String className) {
		SourceClassInfo sci = new SourceClassInfo(className, false, false);
		this.sci_set.add(sci);
		this.sci_list.add(sci);
		this.size_sci ++;
		return sci;
	}
	
	public SourceClassInfo getSourceClassInfo(
			String className) {
		for (SourceClassInfo sci : sci_set) {
			if (sci.className().equals(className)) {
				return sci;
			}
		}
		return null;
	}
	
	public List<SourceClassInfo> getListOfSourceClassInfo() {
		return sci_list;
	}
	
	public int getSizeOfSourceClassInfo() {
		return size_sci;
	}
	
	public int getMaxSizeOfAttrDeps() {
		int max = 0;
		for (SourceClassInfo sci : sci_set) {
			if (sci.getMaxSizeOfAttrs() > max) {
				max = sci.getMaxSizeOfAttrs();
			}
		}
		return max;
	}
	
	public int getMaxSizeOfMethodDeps() {
		int max = 0;
		for (SourceClassInfo sci : sci_set) {
			if (sci.getMaxSizeOfMethods() > max) {
				max = sci.getMaxSizeOfMethods();
			}
		}
		return max;
	}
	
	// 对StupComplexity中的SourceClassInfo进行排序
	public void sort(Comparator<SourceClassInfo> comparator) {
		Collections.sort(sci_list, comparator);
	}
	
	public String toDump(String dumpType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.STUP_COMPLEXITY + "\n");
		sb.append(GlobalTag.MAX_SIZE_ATTRS + "\t" + getMaxSizeOfAttrDeps() + "\n");
		sb.append(GlobalTag.MAX_SIZE_METHODS + "\t" + getMaxSizeOfMethodDeps() + "\n");
		
		for (SourceClassInfo sci : sci_set) {
			sb.append("\n");
			sb.append(sci.toDump(dumpType));
		}
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		
		
		return sb.toString();
	}
	
}
