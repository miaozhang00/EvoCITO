package ga;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import toolkits.SourceClassInfo;

//public class FitnessComparator implements Comparator<Chromosome> {
public class FitnessComparator implements Comparable<Chromosome> {	
	/*
	public int compare(Chromosome chromos1, Chromosome chromos2){
		
		BigDecimal fitness1 = chromos1.getFitness();
		BigDecimal fitness2 = chromos2.getFitness();		
		return fitness1.compareTo(fitness2);
	}
	*/
	
	public int compare(Chromosome chrom1, Chromosome chrom2){
		
		System.out.println("chrom1-----");
		System.out.println(chrom1.getSizeOfChromos());
		for(int i=0; i < chrom1.getSizeOfChromos(); i++){
			System.out.println(chrom1.getGeneName(i));
		}
		//chrom1.print();

		System.out.println("chrom2-----");
		//chrom2.print();
		BigDecimal fitness1 = chrom1.calculateFitness(chrom1.getListOfSCI());
		System.out.println(fitness1);
		BigDecimal fitness2 = chrom2.calculateFitness(chrom2.getListOfSCI());
		
		if(fitness1.compareTo(fitness2)== -1){
			return -1;
		}else if(fitness1.compareTo(fitness2)== 1){
			return 1;
		}else{
			return 0;
		}
	}

	public int compareTo(Chromosome o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
/*测试代码
System.out.println("newPopulations's number" + newPopulations.getSizeOfPopulations());
for(int i=0; i < newPopulations.getSizeOfPopulations(); i++){
	Chromosome chrom = newPopulations.getChromos(i);
	List<SourceClassInfo> a = chrom.getListOfSCI();
	for(SourceClassInfo sci : a){
		System.out.println(sci.className());
	}
	System.out.println("------newPopulations---chrom------"+i);
	//chrom.getListOfSCI();
	//chrom.print();		
}
*/
/*测试代码
System.out.println("sortpopulations-----" + sortpopulations.size());

for( Chromosome ch : sortpopulations){
	System.out.println("这是sortpopulations的一个染色体个体------");
	List<SourceClassInfo> a = ch.getListOfSCI();
	for(SourceClassInfo sci : a){
		System.out.println(sci.className());
	}
	
}
	 System.out.println("------sort---chrom------");
*/
/*测试代码
System.out.println("sortpopulations-----" + sortpopulations.size());
int i=1;
for( Chromosome ch : sortpopulations){
	System.out.println("这是sortpopulations的第"+i+"个染色体个体------");
	i++;
	System.out.println("该染色体方法依赖数目为"+ch.getNumberOfMethodDependency(ch.getListOfSCI()));
	System.out.println("该染色体属性依赖数目为"+ch.getNumberOfAttrDependency());
	System.out.println("该染色体属性复杂度为"+ch.calculateAttributeFitness(ch.getListOfSCI()));
	System.out.println("该染色体方法复杂度为"+ch.calculateMethodFitness(ch.getListOfSCI()));
	System.out.println("该染色体总体复杂度为"+ch.calculateFitness(ch.getListOfSCI()));
	System.out.println(ch.getFitness());
	System.out.println("----该染色体的所有类-----");
	List<SourceClassInfo> a = ch.getListOfSCI();
	for(SourceClassInfo sci : a){
		System.out.println(sci.className());
	}
	
}
*/
/*Processor50
System.out.println("---getSCIfromDC---" + listOfGA.size());
for(SourceClassInfo sci2 : listOfGA){
	System.out.println("sci.name----" + sci2.className());
}
*/