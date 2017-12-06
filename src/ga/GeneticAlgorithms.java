package ga;

import java.math.BigDecimal;
import java.util.List;

import data.DataCenter;
import toolkits.SourceClassInfo;

public class GeneticAlgorithms {

	private int SizeOfPops;							//种群大小
	private List<SourceClassInfo> listOfGA;			//种群信息
	private double pc;			    				//交叉率
	private double pm;			    				//变异率
	private int GenerationOfPops;   				//运行代数
		
	public GeneticAlgorithms(){
		
	}
	
	public GeneticAlgorithms(int SizeOfPops, List<SourceClassInfo> listOfGA, double pc, double pm, int GenerationOfPops){
		this.SizeOfPops = SizeOfPops;
		this.listOfGA = listOfGA; 
		this.pc = pc;
		this.pm = pm;
		this.GenerationOfPops = GenerationOfPops;
	}
	
	public void generateClassListBasedGA(int SizeOfPops, List<SourceClassInfo> listOfGA){
		
		Populations initialPops = new Populations(SizeOfPops, listOfGA, false);
		
		int generations = 0;
		Populations Pops_evolver = initialPops;
				
		while(generations < GenerationOfPops){
			System.out.println("---------Generation "+generations+"--------------");
			Populations Pops_evolved = Pops_evolver;
			//System.out.println("Pos_evolved_size"+Pops_evolved.getSizeOfPopulations());//
			Populations newPops = Pops_evolver.evolvePops(Pops_evolved);
			//System.out.println(newPops.getBestFitness(newPops));
			Pops_evolver = newPops;
			//System.out.println("Pos_evolver_size"+Pops_evolver.getSizeOfPopulations());//
			System.out.println(Pops_evolver.getBestFitness(Pops_evolver));
			generations++;
		}
		
		Chromosome global_bestchromos = Pops_evolver.getBest(Pops_evolver);
		BigDecimal global_bestfitness = Pops_evolver.getBestFitness(Pops_evolver);
		
		System.out.println("global_best_fitness" + global_bestchromos.calculateFitness(global_bestchromos.getListOfSCI()));
		System.out.println("fit_attr" + global_bestchromos.calculateAttributeFitness(global_bestchromos.getListOfSCI()));
		System.out.println("fit_method" + global_bestchromos.calculateMethodFitness(global_bestchromos.getListOfSCI()));
		
		System.out.println("size of AttrDependencies" + global_bestchromos.getNumberOfAttrDependency());
		System.out.println("size of MethodDependencies" + global_bestchromos.getNumberOfMethodDependency());
		
		System.out.println("---- CITO ------");
		global_bestchromos.print();
		
	}
}
