package ga;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import data.DataCenter;
import toolkits.SourceClassInfo;
import toolkits.StupComplexity;

public class Populations  {

	private List<Chromosome> populations;			 //��Ⱥ��ʾ
	private List<SourceClassInfo> listOfGA;			 //��Ⱥ��Ϣ
	private int POPNUM;                              //��Ⱥ��С
	private double pc = 0.5;                         //�������
	private double pm = 0.015;                       //�������
	private int tournamentSize = 5;                  //������ѡ�����
	private boolean elitism = true;                  //�Ƿ�����һ�������Ÿ���
	
	public Populations(){
		this.populations = new ArrayList<Chromosome>();
		
	}
	public Populations(List<Chromosome> populations){
		this.populations = populations;
	}
	
	public Populations(int populationSize, List<SourceClassInfo> listOfGA, boolean initialise){
		this.POPNUM = populationSize;
		this.listOfGA = listOfGA;
		if(!initialise){
			initialPops(populationSize, listOfGA);
	
		}		
	}

	public Populations(int populationSize, Chromosome chromos, boolean initialise){
		this.POPNUM = populationSize;
		this.listOfGA = chromos.getListOfSCI();
		if(!initialise){
			initialPops(populationSize, listOfGA);
			
		}		
	}
	
	/**
	 * ��ʼ��һ��Ⱦɫ��
	 * һ����Ⱥ
	 */
	public void initialPops(int POPNUM, List<SourceClassInfo> listOfGA){
		
		this.populations = new ArrayList<Chromosome>();
		for(int i=0; i<POPNUM; i++){	
			Chromosome chromos = new Chromosome(listOfGA,false);
			
			populations.add(chromos);
		}
		
	}
	
   /**
    * ÿ����Ⱥ����
    */
	public Populations evolvePops(Populations oldPopulations){
				
		Populations newPopulations = new Populations();
		POPNUM = oldPopulations.getSizeOfPopulations();
		
		//������һ������������		
		int offset = 0;
		if(elitism){						
			newPopulations.setChromos(0,oldPopulations.getBest(oldPopulations));			
			offset = 1;
		}
		//System.out.println("��һ��������ֵ"+newPopulations.getBestFitness(newPopulations));	
		//ȷ����Ⱥ�н���ĸ�����
		int crossnum = (int)(POPNUM * pc);
		if(crossnum%2!=0){
			crossnum++;                      
		}
		
		//ѡ�����ڽ���ĸ��岢ִ�н������
		List<Chromosome> sortpopulations = sortPops(oldPopulations);				
		for(int i=0; i < crossnum; i++){
			Chromosome parent1 = sortpopulations.get(i);								
			i++;			
			Chromosome parent2 = tournamentSelection(sortpopulations);
						
			List<Chromosome> children = crossover(parent1, parent2);	
			newPopulations.setChromos(offset, children.get(0));
			//Test
			//System.out.println("----------��"+i+"�齻����-------");//
			//System.out.println("�������one"+children.get(0).calculateFitness(children.get(0).getListOfSCI()));//
			//System.out.println("�������two"+children.get(1).calculateFitness(children.get(1).getListOfSCI()));//
			offset++;
			newPopulations.setChromos(offset, children.get(1));
			offset++;
		}
			//System.out.println("����������ֵ"+newPopulations.getBestFitness(newPopulations));			
		
		//ȷ����Ⱥ�б���ĸ�����
		int mutatenum = (int)(POPNUM * pm);
		
		//ѡ�����ڱ���ĸ��岢ִ�б������
		for(int j=0; j < mutatenum; j++){
			
			Chromosome parent = sortpopulations.get(sortpopulations.size()-1-j);						
			Chromosome child = mutate(parent);
			newPopulations.setChromos(offset, child);
			offset++;
		}
		  //System.out.println("����������ֵ"+newPopulations.getBestFitness(newPopulations));
		 // newPopulations = FullOrEmpty(oldPopulations, newPopulations);
		  //System.out.println("======�´���Ⱥ����====");//
		 // int count = 0;//
		 // for(Chromosome newc :newPopulations.getListOfChromosome()){
			 // System.out.println("�ø�����Ӧ��Ϊ"+ newc.calculateFitness(newc.getListOfSCI()));
			 // count++;
		//  }
		 // System.out.println("�´���Ⱥ������Ϊ"+newPopulations.getSizeOfPopulations());
		  //return newPopulations;
		return FullOrEmpty(oldPopulations, newPopulations);
	}

	/**
	 * ������ѡ��
	 * ÿ��5������
	 */
	/*
	public Chromosome tournamentSelection(Populations population){
		Populations tournament = new Populations(tournamentSize,population.getChromos(0),false);
		for(int i=0; i < tournamentSize; i++){
			int randomId = (int)(Math.random() * population.getSizeOfPopulations());
			tournament.setChromos(i,population.getChromos(randomId));
		}
		Chromosome selected = tournament.getBest(tournament);
		return selected;
	}
	*/
	public Chromosome tournamentSelection(List<Chromosome> list_chromos){
		Populations tournament = new Populations();
		for(int i=0; i < tournamentSize; i++){
			int randomId = (int)(Math.random()* list_chromos.size());
			tournament.setChromos(i, list_chromos.get(randomId));
		}
		Chromosome selected = tournament.getAttrBest(tournament);		
		return selected;
	}
	
	/**
	 * �������
	 * �������Ϊpc
	 */
	public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2){
		
		//children�������ɵ������Ӵ�Ⱦɫ��
		List<Chromosome> children = new ArrayList<Chromosome>();
		
		//������ɽ����
		Random random = new Random();
		int startpos, endpos = 0;
		startpos = random.nextInt(parent1.getSizeOfChromos());
		
		do{
			endpos = random.nextInt(parent2.getSizeOfChromos());   
		}while(endpos == startpos);
		if(startpos > endpos){
			int temp = startpos;
			startpos = endpos;
			endpos = temp;
		}
		
		//��ȡ��������
		SourceClassInfo gene_start_p1 = parent1.getGene(startpos);
		SourceClassInfo gene_end_p1 = parent1.getGene(endpos);
		SourceClassInfo gene_start_p2 = parent2.getGene(startpos);
		SourceClassInfo gene_end_p2 = parent2.getGene(endpos);
		
		//�����µ��Ӵ�����		
		List<SourceClassInfo> child1_list = new ArrayList<SourceClassInfo>();
		List<SourceClassInfo> child2_list = new ArrayList<SourceClassInfo>();
		
		for(int i=0; i < parent1.getSizeOfChromos(); i++){
			if(!(parent1.getGeneName(i).equals(parent2.getGeneName(startpos)))
					&& !(parent1.getGeneName(i).equals(parent2.getGeneName(endpos))) ){
			child1_list.add(parent1.getGene(i));			
			}
		}
		child1_list.add(startpos, gene_start_p2);
		child1_list.add(endpos, gene_end_p2);
		Chromosome child1 = new Chromosome(child1_list,true);		
		//System.out.println("");//

		for(int i=0; i < parent2.getSizeOfChromos(); i++){
			if(!(parent2.getGeneName(i).equals(parent1.getGeneName(startpos)))
					&& !(parent2.getGeneName(i).equals(parent1.getGeneName(endpos))) ){
			child2_list.add(parent2.getGene(i));			
			}
		}		
		child2_list.add(startpos, gene_start_p1);
		child2_list.add(endpos, gene_end_p1);
		Chromosome child2 = new Chromosome(child2_list,true);		
		//Chromosome child2 = new Chromosome(child2_list);
		//child2.setGene(startpos, gene_start_p1);
		//child2.setGene(endpos, gene_end_p1);
		
		children.add(child1);
		children.add(child2);
		
		return children;
	}
	
	/**
	 * �������
	 * �������Ϊpm
	 */
	public Chromosome mutate(Chromosome parent){
				
		//������ɽ����
		Random random = new Random();
		int startpos, endpos = 0;
		startpos = random.nextInt(parent.getSizeOfChromos());
		do{
			endpos = random.nextInt(parent.getSizeOfChromos());   
		}while(endpos == startpos);
		if(startpos > endpos){
			int temp = startpos;
			startpos = endpos;
			endpos = temp;
		}
		
		//��ȡ��������
		SourceClassInfo gene_start = parent.getGene(startpos);
		SourceClassInfo gene_end = parent.getGene(endpos);
		
		//�����µ��Ӵ�����		
		List<SourceClassInfo> child_list = new ArrayList<SourceClassInfo>();
		for(int i=0; i < parent.getSizeOfChromos(); i++){
			if(i != startpos && i != endpos ){
				child_list.add(parent.getGene(i));			
				}
			}		
		//Chromosome child = new Chromosome(child_list,true);		
		//child.setGene(startpos, gene_end);
		//child.setGene(endpos, gene_start);
		
		child_list.add(startpos, gene_end);
		child_list.add(endpos, gene_start);
		Chromosome child = new Chromosome(child_list,true);		
		
		return child;
	}
	
	/**
	 * �ж���Ⱥ�����ǿ�
	 * ������ɾ�����ָ��壻
	 * ���գ����Ӹ�����������
	 */
	public Populations FullOrEmpty(Populations oldPopulations, Populations newPopulations){

		int numofnew = newPopulations.getSizeOfPopulations();
		int numofold = oldPopulations.getSizeOfPopulations();
		
		if(numofnew > numofold){
			for(int i=0; i < (numofnew - numofold); i++){
				int random = (int)(Math.random() * (numofold-1))+1;
				newPopulations.removeChromos(random);				
			}
		}
		if(numofnew < numofold){
			for(int i=1; i <= (numofold - numofnew); i++){
				int ranId = (int)(Math.random()* oldPopulations.getSizeOfPopulations());
				newPopulations.setChromos(0,sortPops(oldPopulations).get(ranId));				
			}
		}
	
		return newPopulations;
		
	}
	
	/**
	 * ���ݸ�����Ӧ��ֵ������Ⱥ���������������Ӧ�ȴ�С����
	 */
	 public List<Chromosome> sortPops(Populations population){
		 
		 List<Chromosome> sortpopulations = population.getListOfChromosome();				     
		 Collections.sort(sortpopulations);
		 return sortpopulations;
	 }

	/**
	 * Ѱ�����Ÿ��壬�������¼����
	 */
	 public Chromosome getBest(Populations population){
		Chromosome bestchromos = new Chromosome();
		List<Chromosome> sortpopulations = sortPops(population);
		bestchromos = sortpopulations.get(0);
		return bestchromos;
	}
	 
	 public Chromosome getAttrBest(Populations population){
		 Chromosome best_attr_chromos = new Chromosome();
		 List<Chromosome> sortpopulations = sortPops(population);
		 best_attr_chromos = sortpopulations.get(0);
		 return best_attr_chromos;
	 }
	
	 /**
	  * Ѱ��������Ӧ��ֵ
	  */
	 public BigDecimal getBestFitness(Populations population){
		 Chromosome best_chromos = sortPops(population).get(0);
		 BigDecimal best_fitness = best_chromos.calculateFitness(best_chromos.getListOfSCI());
		 return best_fitness;
	 }
	 
	 public double getBestAttrFitness(Populations population){
		 Chromosome best_attr_chromos = sortPops(population).get(0);
		 double best_attr_fitness = best_attr_chromos.calculateAttrFitness(best_attr_chromos.getListOfSCI());
		 return best_attr_fitness;
	 }
	/**
	 * Ѱ�������壬�������¼����
	 */
	public Chromosome getWorst(Populations population){
		Chromosome worstchromos = new Chromosome();
		List<Chromosome> sortpopulations = sortPops(population);
		worstchromos = sortpopulations.get(getSizeOfPopulations()-1);
		return worstchromos;
	}	
	
	/**
	 * Ѱ�������Ӧ��ֵ
	 */
	public BigDecimal getWorstFitness(Populations population){
		Chromosome worst_chromos = sortPops(population).get(getSizeOfPopulations()-1);
		BigDecimal worst_fitness = worst_chromos.calculateFitness(worst_chromos.getListOfSCI());
		return worst_fitness;
	}
	
	public void setChromos(int offset, Chromosome chromos){

			this.populations.add(offset, chromos);
		//this.getListOfChromosome().add(offset, chromos);
	}
	
	public Chromosome getChromos(int offset){
		
		return this.populations.get(offset);
	}
	
	public void removeChromos(int offset){
		
		this.populations.remove(offset);
	}
	
	public int getSizeOfPopulations(){
		
		return this.populations.size();
	}

	public List<Chromosome> getListOfChromosome(){
		List<Chromosome> list_chrom = new ArrayList<Chromosome>();
		//return this.populations;
		for(int i=0; i<this.getSizeOfPopulations();i++){
			Chromosome chrom = this.getChromos(i);
			list_chrom.add(i,chrom);
		}
		return list_chrom;
	}
	
}
