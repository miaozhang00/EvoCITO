package ria;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import toolkits.SourceClassInfo;

public class RandomIterativeAlgorithms {

	private int GenerationOfOrders; //���д���
	private List<SourceClassInfo> listOfRIA; //��Ⱥ��Ϣ
	private double T = 100.0; //�˻��ʼ�¶�
	private double a; //����ϵ��
	
	private double finalT = 0.0; //��ֹ�¶�
	
	private List<SourceClassInfo> globalBestOrder;  //���Ž�
	private BigDecimal globalBestfitness = new BigDecimal("10000000");//������Ӧ��ֵ
	private List<SourceClassInfo> localBestOrder;	//ÿ�����Ž�
	private BigDecimal localBestfitness;			 //ÿ��������Ӧ��ֵ
	
	
	public RandomIterativeAlgorithms(){
		
	}
	
	public RandomIterativeAlgorithms(int GenerationOfOrders, List<SourceClassInfo> listOfRIA, double T, double a){
		this.GenerationOfOrders = GenerationOfOrders;
		this.listOfRIA = listOfRIA;
		this.T = T;
		this.a = a;
		
	}
	
	public void generateClassListBasedonRIA(List<SourceClassInfo> listOfRIA){
		
		int generations = 0;
		while(generations < GenerationOfOrders){
			System.out.println();
			System.out.println("-------Generations"+generations+"-------");
			List<SourceClassInfo> initialOrder = initialOrder(listOfRIA);
			List<SourceClassInfo> riaOrder = doRIA(initialOrder);
			
			 if(calculateFitness(riaOrder).compareTo(calculateFitness(initialOrder)) == -1){
				 localBestOrder = riaOrder;
				 localBestfitness = calculateFitness(riaOrder);
			 }
			 else{
				 localBestOrder = initialOrder;
				 localBestfitness = calculateFitness(initialOrder);
			 }
			//
			//System.out.println("--- Compare to Best Order ---");
			System.out.println("** loaclBestOrder.fitness"+localBestfitness);
			System.out.println("** globalBestOrder.fitness"+globalBestfitness);
			//
			
			 if(localBestfitness.compareTo(globalBestfitness) == -1){
				 globalBestOrder = localBestOrder;
				 globalBestfitness = localBestfitness;
			 }
			 generations ++;
		}
		
		System.out.println();
		System.out.println();
		System.out.println("RIA bestfitness" + globalBestfitness);
		System.out.println("---- RIA  CITO ------");
		print(globalBestOrder);
		
	}
	
	/*
	 * ��ʼ������
	 */
	public List<SourceClassInfo> initialOrder(List<SourceClassInfo> listOfRIA){
		
		List<SourceClassInfo> sci_temp = new LinkedList<SourceClassInfo>();
		for(SourceClassInfo sci : listOfRIA){
			sci_temp.add(sci);
		}
		
		int n = 0;
		List<SourceClassInfo> order = new LinkedList<SourceClassInfo>();
		Random random = new Random();
		while(sci_temp.size()!=0){
			n = random.nextInt(sci_temp.size());
			order.add(sci_temp.get(n));
			sci_temp.remove(n);			
		}
		
		//
		//System.out.println("-- RIA initial ---");
		//printwithNo(order);
		System.out.println("**RIA initial fitness***  "+ calculateFitness(order));
		//
		
		return order;
	}
	
	/*
	 * ִ�������������
	 * �����ɽ�㷨��ģ���˻�
	 * �Ƿ���Ҫ������ֹ�¶ȣ�
	 */
	public List<SourceClassInfo> doRIA(List<SourceClassInfo> order){
		
		//��������sortOrder��¼���������������и����λ��
		int[] sortOrder = new int[order.size()];
		for(int a=0; a <= sortOrder.length-1; a++){
		  sortOrder[a] = a;	
		}	
		
		int k = 0; 									//ÿ�αȽϵ���
		loop:for(int j=0; j <= order.size()-2; j++){		//j֮ǰ����ȷ��������
			k = j+1;
			for(int i=0; i <= j; i++){				//p ��ǰ�Ƚϵ���
				double g = gain(i,j,k,order);		//����gain(i,j,k)				
				if(g>0){					
				if(Math.random()/100 < 1-Math.exp(-g/T) ){					
					if(T > finalT)
					{						
						SortOrder(i,k,sortOrder);   //��i j��������k֮��
						T = a * T;                  //����										
						break;						//����������ѭ��������ֱ�Ӻ���һ��kֵ�Ƚ�
					}	                    						
					if(T < finalT){
						break loop;
					}
				}					
				}
			}
		}
				
		//�������
		/*
		System.out.println("final sortOrder");
		for(int b=0; b <= sortOrder.length-1; b++){
			System.out.print(sortOrder[b]+" ");
			
		}
		System.out.println();
		*/
			
		List<SourceClassInfo> riaOrder = new LinkedList<SourceClassInfo>();
		for(int i=0; i <= sortOrder.length-1; i++){
			riaOrder.add(order.get(sortOrder[i]));
		}
		
		//
		//System.out.println("-- RIA random iterative ---");
		//print(riaOrder);
		System.out.println("**RIA random iterative fitness***  "+ calculateFitness(riaOrder));
		//
		
		return riaOrder;
	}
	
	/*
	 * ��ij��������k֮��
	 */
	public void SortOrder(int i, int k, int[] order){
		
		int temp = 0;
		for(int j=k-1; j >= i; j--){
			temp = order[k];
			order[k] = order[j];
			order[j] = temp;
			k--;
		}
	}
	
	
	
	/*
	 * ����gain(i,j,k)
	 */
	public double gain(int i, int j, int k, List<SourceClassInfo> order){    //k=j+1
		double g = 0.0;
		if(i==0 && j==0 && k==1){
			return g = SCplx(0,1,order)-SCplx(1,0,order);
		}
		else{
			double sumik = 0.0;
			for(int s=0; s <= i; s++){
				sumik = sumik + SCplx(s,k,order)-SCplx(k,s,order);
			}
			return g = gain(0,j-1,k-1,order)+ sumik;
		}
	}
	
	/*
	 * ����SCplx
	 */
	public double SCplx(int i, int k, List<SourceClassInfo> order){
		int max_size_attrs = getMaxSizeAttrDeps(order);
		int max_size_methods = getMaxSizeMethodDeps(order);
		
		SourceClassInfo sci_i = order.get(i);
		String name_k = order.get(k).className();
		
		double sizeOfAttr = sci_i.getSizeOfAttrDeps(name_k);
		double sizeOfMethod = sci_i.getSizeOfMethodDeps(name_k);
		
		double _sizeOfAttr = sizeOfAttr / max_size_attrs;
		double _sizeOfMethod = sizeOfMethod / max_size_methods;
		
		double scplx = Math.sqrt(_sizeOfAttr*_sizeOfAttr/2 + _sizeOfMethod*_sizeOfMethod/2);
		return scplx;
		
	}
	
	public int getMaxSizeAttrDeps(List<SourceClassInfo> order){
		
		int max_size_attrs = 0;
		for(SourceClassInfo sci : order){
			if (sci.getMaxSizeOfAttrs() > max_size_attrs) {
				max_size_attrs = sci.getMaxSizeOfAttrs();
			}
		}
		return max_size_attrs;
	}
	
	public int getMaxSizeMethodDeps(List<SourceClassInfo> order){
		int max_size_methods = 0;
		for (SourceClassInfo sci : order) {
			if (sci.getMaxSizeOfMethods() > max_size_methods) {
				max_size_methods = sci.getMaxSizeOfMethods();
			}
		}
		return max_size_methods;
	}
	
	/*
	 * ��ȡ��Ӧ�Ⱥ����ķ���
	 */	
	public BigDecimal calculateFitness(List<SourceClassInfo> order){
		
		int max_size_attrs = getMaxSizeAttrDeps(order);
		int max_size_methods = getMaxSizeMethodDeps(order);
		
		BigDecimal fit = new BigDecimal("0");
		
		//����������ļ���
		Set<String> classNames = new HashSet<String>();
		
		for(SourceClassInfo sci : order){
			String className = sci.className();
			classNames.add(className);
			
			//Ŀ���༯��
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
										
					double complex = Math.sqrt(_sizeOfAttr*_sizeOfAttr/2 + _sizeOfMethod*_sizeOfMethod/2);					
					fit = fit.add(new BigDecimal(complex));
				}
			}
			
		}
		
		return fit;
	}
	
	public void print(List<SourceClassInfo> order){
		
		for (SourceClassInfo sci : order) {
			System.out.println(sci.className());
		}
	}
	
	public void printwithNo(List<SourceClassInfo> order){
		int i=0;
		for (SourceClassInfo sci : order) {
			System.out.println(i+" "+sci.className());
			i++;
		}
	}
}
