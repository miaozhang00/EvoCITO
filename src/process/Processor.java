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
		
		//�����˻��ʼ�¶�
		double T = 100.0;
		//���ý���ϵ��
		double a = 0.95;
		//�������д���
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
		
		//������Ⱥ��С
		int SizeOfPops = stupComplexity.getSizeOfSourceClassInfo();
		//���ý�����
		double pc = 0.5;
		//���ñ�����
		double pm = 0.015;
		//�������д���
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
		
		// ����SourceClass��Dependency�������ɶൽ�ٽ�������DESC��
		{
			this.stupComplexity.sort(new DependencyDESCComparator());
		}
		
		// ��������λ��
		{
			this.factorial = new Factorial(BigInteger.valueOf(size_sci));
		}
		
		// ��������Ⱥ�Ż��������������
		{
			generateClassListBasedPSO(size_sci);
		}
		
	}
	
	// ��������Ⱥ�Ż��������������
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
	
	//��ȡ��Ҫ�õ������е�����Ϣ
	public List<SourceClassInfo> getSCIfromDC(){
		
		List<SourceClassInfo> sci_initial = new LinkedList<SourceClassInfo>();
		
		List<SourceClassInfo> sci_list = stupComplexity.getListOfSourceClassInfo();
		for(SourceClassInfo sci : sci_list){
			sci_initial.add(sci);
		}
		return sci_initial;
	}
}
