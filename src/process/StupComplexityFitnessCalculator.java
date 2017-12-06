package process;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pso.IFitness;
import pso.IFitnessCalculator;
import soot.SootClass;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;

public class StupComplexityFitnessCalculator implements IFitnessCalculator {

	StupComplexity 	stupComplexity;
	
	Factorial 		factorial;
	
	public StupComplexityFitnessCalculator(StupComplexity stupComplexity, Factorial factorial) {
		this.stupComplexity = stupComplexity;
		this.factorial 		= factorial;
	}
	
	/*
	 * 计算适应度值
	 * x 表示位置
	 */
//	public BigDecimal computeFitness(BigInteger position) {
	
	public List<SourceClassInfo> getSCIfromDC(){
		
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for(SourceClassInfo sci : sci_list){
			sci_initial.add(sci);
		}
		return sci_initial;
	}
	
	public IFitness computeFitness(BigInteger position) {
		
		int max_size_attrs 		= stupComplexity.getMaxSizeOfAttrDeps();
		int max_size_methods	= stupComplexity.getMaxSizeOfMethodDeps();
		
		BigDecimal fit 			= new BigDecimal("0");
//		BigDecimal fit_attr 	= new BigDecimal("0");
//		BigDecimal fit_method 	= new BigDecimal("0");
		
		List<SourceClassInfo> sci_list = decode(position);
		
		// 评估过的类集合
		Set<String> classNames = new HashSet<String>();
		
		for (SourceClassInfo sci : sci_list) {
			String className = sci.className();
			classNames.add(className);

			// 目标类集合
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> attrClasses 	= sci.getAttrClasses();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for (String depClass : depClasses) {
				if (!classNames.contains(depClass)) {
					double sizeOfAttr 	= sci.getSizeOfAttrDeps(depClass);
					double sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
					
					//System.out.println(sci.className());//new
					//System.out.println(sizeOfAttr);//new
					
					
					
					double _sizeOfAttr 		= sizeOfAttr / max_size_attrs;
					double _sizeOfMethod 	= sizeOfMethod / max_size_methods;
					
//					double complex	= Math.sqrt(sizeOfAttr*sizeOfAttr/2 + sizeOfMethod*sizeOfMethod/2);
					double complex	= Math.sqrt(_sizeOfAttr*_sizeOfAttr/2 + _sizeOfMethod*_sizeOfMethod/2);
					fit = fit.add(new BigDecimal(complex));
					
//					fit_attr = fit_attr.add(new BigDecimal(sizeOfAttr));
//					fit_method = fit_method.add(new BigDecimal(sizeOfMethod));
				}
			}
		}
		
//		return new StupComplexityFitness(fit, fit_attr, fit_method);
		return new StupComplexityFitness(fit);
	}

	public BigDecimal computeAttributeFitness(BigInteger position) {
		BigDecimal fit_attr 	= new BigDecimal("0");
		
		List<SourceClassInfo> sci_list = decode(position);
		
		// 评估过的类集合
		Set<String> classNames = new HashSet<String>();
		
		for (SourceClassInfo sci : sci_list) {
			String className = sci.className();
			classNames.add(className);

			// 目标类集合
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> attrClasses 	= sci.getAttrClasses();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for (String depClass : depClasses) {
				if (!classNames.contains(depClass)) {
					double sizeOfAttr 	= sci.getSizeOfAttrDeps(depClass);
					fit_attr = fit_attr.add(new BigDecimal(sizeOfAttr));
				}
			}
		}
		
		return fit_attr;
	}
	
	public BigDecimal computeMethodFitness(BigInteger position) {
		
		BigDecimal fit_method 	= new BigDecimal("0");
		
		List<SourceClassInfo> sci_list = decode(position);
		
		// 评估过的类集合
		Set<String> classNames = new HashSet<String>();
		
		for (SourceClassInfo sci : sci_list) {
			String className = sci.className();
			classNames.add(className);

			// 目标类集合
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> attrClasses 	= sci.getAttrClasses();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for (String depClass : depClasses) {
				if (!classNames.contains(depClass)) {
					double sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
					fit_method = fit_method.add(new BigDecimal(sizeOfMethod));
				}
			}
		}
		
		return fit_method;
	}
	
	public List<SourceClassInfo> decode(BigInteger position) {
		
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for (SourceClassInfo sci : sci_list) {
			sci_initial.add(sci);
		}
		
		List<SourceClassInfo> sci_results = new ArrayList<SourceClassInfo>();
		while (sci_initial.size() > 1) {
			BigInteger index = position.divide(factorial.getSubFactorial(sci_initial.size()-2));
			BigInteger mod = position.mod(factorial.getSubFactorial(sci_initial.size()-2));
			
			SourceClassInfo sci = sci_initial.remove(index.intValue());
			sci_results.add(sci);
			position = mod;
		}
		
		SourceClassInfo sci = sci_initial.remove(0);
		sci_results.add(sci);
		
		return sci_results;
	}
	
	public int getNumberOfAllDependency2(BigInteger position){
		int NumberOfAllDependency = 0;
		List<SourceClassInfo> sci_list = decode(position);
		for(SourceClassInfo sci : sci_list){
			
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(methodClasses);
			
			for (String depClass : depClasses) {
				
				/*
				if((sci.getSizeOfAttrDeps(depClass))== 0 && sci.getSizeOfMethodDeps(depClass) != 0)				 
					NumberOfAllDependency ++;
				if((sci.getSizeOfAttrDeps(depClass))!= 0 && sci.getSizeOfMethodDeps(depClass) == 0)
					NumberOfAllDependency ++;
				if((sci.getSizeOfAttrDeps(depClass))!= 0 && sci.getSizeOfMethodDeps(depClass) != 0)
					NumberOfAllDependency ++;					
				 */
				if(!(sci.getSizeOfAttrDeps(depClass) == 0 && sci.getSizeOfMethodDeps(depClass) == 0))
					NumberOfAllDependency ++;
			}			
		}
		return NumberOfAllDependency;
	}
	
	public int getNumberOfAllDependency(BigInteger position){
		int NumberOfAllDependency = 0;
		List<SourceClassInfo> sci_list = decode(position);
		for(SourceClassInfo sci : sci_list){
			NumberOfAllDependency += NumberOfAttrDeps(sci);
			
			Set<String> depClasses 		= new HashSet<String>();
			Set<String> methodClasses 	= sci.getMethodClasses();
			depClasses.addAll(methodClasses);
			
			for (String depClass : depClasses) {
				if(sci.getSizeOfAttrDeps(depClass) != 0)
					NumberOfAllDependency += sci.getSizeOfMethodDeps(depClass);
			}			
		}
		return NumberOfAllDependency;
	}
	
	public int getNumberOfAttrDependency(BigInteger position){
		
		int NumberOfAttrDependency = 0;
		List<SourceClassInfo> sci_list = decode(position);
		for(SourceClassInfo sci : sci_list){
			NumberOfAttrDependency = NumberOfAttrDependency + NumberOfAttrDeps(sci);
		}
		
		return NumberOfAttrDependency;
		}		

	public int getNumberOfMethodDependency(BigInteger position){
		
		int NumberOfMethodDependency = 0;
		List<SourceClassInfo> sci_list = decode(position);
		for(SourceClassInfo sci : sci_list){
			NumberOfMethodDependency = NumberOfMethodDependency + NumberOfMethodDeps(sci);
		}
		
		return NumberOfMethodDependency;
	}
	
	public int NumberOfAttrDeps(SourceClassInfo sci){
		
		String className = sci.className();
		
		Set<String> depClasses 		= new HashSet<String>();
		Set<String> attrClasses 	= sci.getAttrClasses();	
		depClasses.addAll(attrClasses);
	
		
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
		Set<String> methodClasses 	= sci.getMethodClasses();
		depClasses.addAll(methodClasses);
		
		int sumOfMethod =0;
		for (String depClass : depClasses) {
			int sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
			sumOfMethod = sumOfMethod + sizeOfMethod;
		}
		return sumOfMethod;
	}
	
	public static void main(String[] args) {
		
		int i = 4;
		System.out.println(Math.sqrt(i));
	
		double d = 0.4;
		BigDecimal bdd = new BigDecimal(d);
		BigDecimal bd = new BigDecimal("0");
//		bd.add(bdd);
		bdd.add(bd);
		
		BigDecimal b1 = new BigDecimal("1");
		BigDecimal b2 = new BigDecimal("2");
		b1 = b1.add(b2);
		System.out.println(b1);
		
		System.out.println(bdd);
		System.out.println(bd);
		
	}
	
}
