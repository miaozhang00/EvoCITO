package process;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import pso.PSO;
import ria.RandomIterativeAlgorithms;
import compare.DependencyDESCComparator;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;
import toolkits.pso.PSOData;
import data.DataCenter;
import ga.ConnectDataCenter;
import ga.GeneticAlgorithms;

public class Processor {
	
	DataCenter dc;
	
	StupComplexity stupComplexity;
	
	Factorial factorial;
	
	PSO pso;
	public Processor() {
		
	}
	public Processor(DataCenter dc) {
		this.dc = dc;
		this.stupComplexity = dc.getStupComplexity();
	}

	public void processRIA(){
		
		//设置退火初始温度
		double T = 100.0;
		//设置降温系数
		double a = 0.95;
		//设置运行代数
		int GenerationOfOrders = 100;
		
		System.out.println("random iterative algorithms start: ...");
		long time_s = System.currentTimeMillis();
		
		ConnectDataCenter cdc = new ConnectDataCenter(stupComplexity);
		List<SourceClassInfo> listOfRIA = cdc.getSCIfromDC();
		
		RandomIterativeAlgorithms ria = new RandomIterativeAlgorithms(GenerationOfOrders,listOfRIA, T, a);
		ria.generateClassListBasedonRIA(listOfRIA);
		
		long time_e = System.currentTimeMillis();
		System.out.println("random iterative algorithms finished, "	+ (time_e - time_s) + "ms total.");
		
	}
	public void processGA(){
		
		//设置种群大小
		int SizeOfPops = stupComplexity.getSizeOfSourceClassInfo();
		//设置交叉率
		double pc = 0.5;
		//设置变异率
		double pm = 0.015;
		//设置运行代数
		int GenerationOfPops = 100;
		
		System.out.println("genetic algorithms start: ...");
		long time_s = System.currentTimeMillis();
		
		ConnectDataCenter cdc = new ConnectDataCenter(stupComplexity);
		List<SourceClassInfo> listOfGA = cdc.getSCIfromDC();
		
		
		GeneticAlgorithms ga = new GeneticAlgorithms(SizeOfPops, listOfGA, pc, pm, GenerationOfPops);
		ga.generateClassListBasedGA(SizeOfPops, listOfGA);
		
		long time_e = System.currentTimeMillis();
		System.out.println("genetic algorithms finished, "	+ (time_e - time_s) + "ms total.");
		
	}
	public void process() {
		
		int size_sci = stupComplexity.getSizeOfSourceClassInfo();
		
		// 按照SourceClass中Dependency的数量由多到少进行排序（DESC）
		{
			this.stupComplexity.sort(new DependencyDESCComparator());
		}
		
		// 计算区间位置
		{
			this.factorial = new Factorial(BigInteger.valueOf(size_sci));
		}
		
		// 基于粒子群优化的类测试序生成
		{
			generateClassListBasedPSO(size_sci);
		}
		
	}
	
	// 基于粒子群优化的类测试序生成
	public void generateClassListBasedPSO(int sizeOfClass) {
		int particleNumber 	= 100;
		int iterations		= 500;
		BigInteger WMIN		= new BigInteger("0");
		BigInteger WMAX		= this.factorial.getSubFactorial(sizeOfClass - 1).subtract(new BigInteger("1"));
		BigInteger VMAX 	= this.factorial.getSubFactorial(sizeOfClass - 2).subtract(new BigInteger("1"));
		StupComplexityFitnessCalculator scf 	= new StupComplexityFitnessCalculator(stupComplexity, factorial);
		List<SourceClassInfo> list = scf.getSCIfromDC();
		System.out.println("---getSCIfromDC---" + list.size());
		for(SourceClassInfo sci : list){
			System.out.println("sci.name----" + sci.className());
		}
		PSOData psoData 	= new PSOData(particleNumber, iterations, WMIN, WMAX, VMAX, scf);
		
		System.out.println("WMIN: " + WMIN);
		System.out.println("WMAX: " + WMAX);
		
		System.out.println("pso start: ...");
		long time_s = System.currentTimeMillis();
		
		this.pso = new PSO(psoData);
		this.pso.doPSO();
		
		long time_e = System.currentTimeMillis();
		System.out.println("pso finished, "	+ (time_e - time_s) + "ms total.");
	}
	
	//获取需要用到的所有的类信息
	public List<SourceClassInfo> getSCIfromDC(){
		
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for(SourceClassInfo sci : sci_list){
			sci_initial.add(sci);
		}
		return sci_initial;
	}
}
