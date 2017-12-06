package ga;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import data.DataCenter;
import mgr.Manager;
import process.Processor;
import process.StupComplexityFitnessCalculator;
import soot.SootClass;
import soot.SystemContext;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;

public class Chromosome implements Comparable<Chromosome>,Cloneable {

	/*
	 * 种群信息，包括各种类和类间关系
	 */
	private List<SourceClassInfo> listOfGA;			 
	
	/*
	 * 染色体表示
	 */
	List<SourceClassInfo> chromos;
	
	/*
	 * 染色体大小，即类的总数
	 */
	int sizeOfChromos;
	
	/*
	 * 各代适应度值
	 */
	BigDecimal fit;
	double fitattr;
	BigDecimal fit_attr;
	BigDecimal fit_method;
	
	/*
	 * 染色体简化表示，仅包含类名
	 */
	List<String> scs_chromos;

	public Chromosome(){
	}
	
	public Chromosome(List<SourceClassInfo> listOfGA, boolean isChromos){
		
		this.listOfGA = listOfGA;
		this.sizeOfChromos = listOfGA.size();
		if(isChromos == false){
			this.chromos = initialChromos(listOfGA);
		}else this.chromos = listOfGA;
	}
	
	public Chromosome(Chromosome chromos){
		this.listOfGA = chromos.listOfGA;
		this.sizeOfChromos = chromos.sizeOfChromos;
		this.chromos = chromos.getListOfSCI();
	}
	
	public Chromosome(List<SourceClassInfo> partOfList){
		this.listOfGA = partOfList;
		this.chromos = partOfList;
	}
	
	/*
	 * 初始化染色体
	 */	
	public List<SourceClassInfo> initialChromos(List<SourceClassInfo> listOfGA){
			
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		                                          
		for(SourceClassInfo sci : listOfGA){
			sci_initial.add(sci);
		}
		
		
		int n=0;
		List<String> scs_chromos = new ArrayList<String>();
		List<SourceClassInfo> chromos = new LinkedList<SourceClassInfo>();
		Random random = new Random();
		do{
		n = random.nextInt(sci_initial.size());
		scs_chromos.add(sci_initial.get(n).className());
		chromos.add(sci_initial.get(n));
		sci_initial.remove(n);
		}while(sci_initial.size()!=0);
				
		return chromos;
	}
	
	/*
	 * 获取该染色体的List<SourceClassInfo>表现形式
	 */	
	public List<SourceClassInfo> getListOfSCI(){
			
		return this.chromos;
	}
	
	public void setGene(int index, SourceClassInfo gene){
		
		this.chromos.set(index, gene);
	}
	
	public SourceClassInfo getGene(int index){
		
		return this.getListOfSCI().get(index);
	}

	public void setGeneName(int index, String geneName){
		
		this.scs_chromos.add(index, geneName);
	}
	
	public String getGeneName(int index){
		if(this.scs_chromos !=null)
		return this.scs_chromos.get(index);
		else return this.getListOfSCI().get(index).className();
	}
	
	public int getMaxSizeAttrDeps(){
		
		int max_size_attrs = 0;
		for (SourceClassInfo sci : this.getListOfSCI()) {
			if (sci.getMaxSizeOfAttrs() > max_size_attrs) {
				max_size_attrs = sci.getMaxSizeOfAttrs();
			}
		}		
		return max_size_attrs;
	}
	
	public int getMaxSizeMethodDeps(){
		
		int max_size_method = 0;
		for (SourceClassInfo sci : this.getListOfSCI()) {
			if (sci.getMaxSizeOfMethods() > max_size_method) {
				max_size_method = sci.getMaxSizeOfMethods();
			}
		}
		return max_size_method;
	}

	/*
	 * 获取适应度函数的方法
	 */	
	public BigDecimal calculateFitness(List<SourceClassInfo> chromos){
		
		int max_size_attrs = getMaxSizeAttrDeps();
		int max_size_methods = getMaxSizeMethodDeps();
		
		BigDecimal fit = new BigDecimal("0");
		
		//评估过的类的集合
		Set<String> classNames = new HashSet<String>();
		
		for(SourceClassInfo sci : chromos){
			String className = sci.className();
			classNames.add(className);
			
			//目标类集合
			Set<String> depClasses = new HashSet<String>();
			Set<String> attrClasses = sci.getAttrClasses();
			Set<String> methodClasses = sci.getMethodClasses();
			
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for(String depClass : depClasses){
				if(!classNames.contains(depClass)){
					double sizeOfAttr = sci.getSizeOfAttrDeps(depClass);
					double sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
					
					double _sizeOfAttr = sizeOfAttr / max_size_attrs;
					double _sizeOfMethod = sizeOfMethod / max_size_methods;
					
					//double complex = Math.sqrt(_sizeOfAttr*_sizeOfAttr/2 + _sizeOfMethod*_sizeOfMethod/2);
					double complex = Math.sqrt(_sizeOfAttr*_sizeOfAttr);
					//double complex = Math.sqrt(_sizeOfMethod*_sizeOfMethod);
					fit = fit.add(new BigDecimal(complex));
				}
			}
			
		}
		
		return fit;
	}

	public double calculateAttrFitness(List<SourceClassInfo> chromos){
		
		int max_size_attrs = getMaxSizeAttrDeps();
		
		double fitattr = 0.0;
		
		//评估过的类的集合
		Set<String> classNames = new HashSet<String>();
		
		for(SourceClassInfo sci : chromos){
			String className = sci.className();
			classNames.add(className);
			
			//目标类集合
			Set<String> depClasses = new HashSet<String>();
			Set<String> attrClasses = sci.getAttrClasses();
			Set<String> methodClasses = sci.getMethodClasses();
			
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for(String depClass : depClasses){
				if(!classNames.contains(depClass)){
					double sizeOfAttr = sci.getSizeOfAttrDeps(depClass);				
					double _sizeOfAttr = sizeOfAttr / max_size_attrs;
					//double complex = _sizeOfAttr * _sizeOfAttr;
					fitattr = fitattr + _sizeOfAttr;
				}
			}
			
		}		
		return fitattr;		
	}
	/*
	 * 获取属性适应度
	 */	
	public BigDecimal calculateAttributeFitness(List<SourceClassInfo> chromos){

		BigDecimal fit_attr = new BigDecimal("0");
		
		//评估过的类的集合
		Set<String> classNames = new HashSet<String>();
		
		for(SourceClassInfo sci : chromos){
			String className = sci.className();
			classNames.add(className);
			
			//目标类集合
			Set<String> depClasses = new HashSet<String>();
			Set<String> attrClasses = sci.getAttrClasses();
			Set<String> methodClasses = sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for(String depClass : depClasses){
				if(!classNames.contains(depClass)){
					double sizeOfAttr = sci.getSizeOfAttrDeps(depClass);
					fit_attr = fit_attr.add(new BigDecimal(sizeOfAttr));
				}
			}
	
		}
		return fit_attr;	
	}

	/*
	 * 获取方法适应度
	 */	
	public BigDecimal calculateMethodFitness(List<SourceClassInfo> chromos){
		
		BigDecimal fit_method = new BigDecimal("0");
		
		//评估过的类集合
		Set<String> classNames = new HashSet<String>();
		
		for(SourceClassInfo sci : chromos){
			String className = sci.className();
			classNames.add(className);
			
			//目标类集合
			Set<String> depClasses = new HashSet<String>();
			Set<String> attrClasses = sci.getAttrClasses();
			Set<String> methodClasses = sci.getMethodClasses();
			depClasses.addAll(attrClasses);
			depClasses.addAll(methodClasses);
			
			for(String depClass : depClasses){
				if(!classNames.contains(depClass)){
					double sizeOfMethod = sci.getSizeOfMethodDeps(depClass);
					fit_method = fit_method.add(new BigDecimal(sizeOfMethod));
				}
			}
		}
		return fit_method;	
	}
	
	/*
	 * 获取属性依赖个数
	 */	
	public int getNumberOfAttrDependency(){		
		
		int NumberOfAttrDependency = 0;
		for(SourceClassInfo sci : this.getListOfSCI()){
			NumberOfAttrDependency = NumberOfAttrDependency + NumberOfAttrDeps(sci);
		}
		
		return NumberOfAttrDependency;	
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

	/*
	 * 获取方法依赖个数
	 */	
	public int getNumberOfMethodDependency(){
		
		int NumberOfMethodDependency = 0;
		for(SourceClassInfo sci : this.getListOfSCI()){
			NumberOfMethodDependency = NumberOfMethodDependency + NumberOfMethodDeps(sci);
		}
		
		return NumberOfMethodDependency;
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
	
	/*
	 * 获取染色体大小，即类的数目
	 */	
	public int getSizeOfChromos(){
		
		return sizeOfChromos;		
	}

	public boolean containsGene(String gene){
		
		return scs_chromos.contains(gene);
	}
	
	public void print(){

		for (SourceClassInfo sci : this.getListOfSCI()) {
			System.out.println(sci.className());
		}
	}
		
	public int compareTo(Chromosome chrom2) {

		BigDecimal fitness1 = this.calculateFitness(this.getListOfSCI());
		BigDecimal fitness2 = chrom2.calculateFitness(chrom2.getListOfSCI());	
		
		//double fit1 = this.calculateAttrFitness(this.getListOfSCI());
		//double fit2 = chrom2.calculateAttrFitness(chrom2.getListOfSCI());
		
		return fitness1.compareTo(fitness2);
		/*
		if (fit1 < fit2) {
			return 1;
		} else if (fit1 > fit2) {
			return -1;
		} else {
			return 0;
		}*/
	}
	
	public Object clone(){
		Chromosome o = null;
		try{
			o = (Chromosome) super.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		return o;
	}
}
