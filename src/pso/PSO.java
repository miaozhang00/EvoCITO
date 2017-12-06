package pso;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import process.StupComplexityFitness;
import process.StupComplexityFitnessCalculator;

import toolkits.SourceClassInfo;
import toolkits.pso.PSOData;

public class PSO {

	// 配置、记录PSO信息
	PSOData psoData;
	
	// 种群数目
	private int particleNumber;

	// 迭代次数
	private int iterations;

	// 位置区间
	private BigInteger WMIN;
	private BigInteger WMAX;

	// 速度区间
	private BigInteger VMAX;

	// 位置
	private BigInteger x[];

	// 速度
	private BigInteger v[];

	// 适应度值
	private BigDecimal fit[];
	
	// 属性适应度值
//	private BigDecimal fit_attr[];
	
	// 方法适应度值
//	private BigDecimal fit_method[];

	// 单个粒子最优位置
	private BigInteger xpbest[];

	// 单个粒子最优适应度
	private BigDecimal pbest[];

	// 种群粒子最优位置
	private BigInteger xgbest;

	// 种群粒子最优适应度
	private BigDecimal gbest;
	
	// 速度惯性权重
	private BigDecimal w;
	
	// 速度参数
	private int k;
	private BigDecimal C1;
	private BigDecimal C2;
	
	// 适应度计算器
	IFitnessCalculator fitnessCalculator;

	public PSO(PSOData psoData) {
		this.psoData = psoData;
		
		this.particleNumber = psoData.getParticleNumber();
		this.iterations 	= psoData.getIterations();
		this.WMIN 			= psoData.getWMIN();
		this.WMAX 			= psoData.getWMAX();
		this.VMAX 			= psoData.getVMAX();
		this.fitnessCalculator 		= psoData.getFitness();
		
		this.x 				= new BigInteger[particleNumber];
		this.v 				= new BigInteger[particleNumber];
		for (int i = 0; i < particleNumber; i++) {
			x[i] = new BigDecimal(WMAX).multiply(new BigDecimal(Math.random())).toBigInteger().add(WMIN);
			
//			System.out.println(i + "\t" + x[i]);
			psoData.addPosition(i, 0, x[i]);
			
			v[i] = new BigDecimal(VMAX).multiply(new BigDecimal(Math.random())).toBigInteger();
		}
		
		this.fit 			= new BigDecimal[particleNumber];
//		this.fit_attr 		= new BigDecimal[particleNumber];
//		this.fit_method 	= new BigDecimal[particleNumber];
		this.pbest 			= new BigDecimal[particleNumber];
		this.xpbest 		= new BigInteger[particleNumber];
		for (int i = 0; i < particleNumber; i++) {
			pbest[i] 	= new BigDecimal("10000");
			xpbest[i] 	= new BigInteger("0");
		}
		this.gbest 			= new BigDecimal("10000");
		this.xgbest 		= new BigInteger("0");

		this.k = 1;
		this.C1 = new BigDecimal("2");
		this.C2 = new BigDecimal("2");
	}

	// 粒子群计算
	public void doPSO() {
		
		for ( ; k <= iterations; k++) {
			w = new BigDecimal(0.9 - k * (0.9 - 0.4) / iterations);
			
			
//			System.out.print(k + "\t");
			// 适应度计算
			{
				for (int i = 0; i < particleNumber; i++) {
					
					// 计算单个粒子在该位置的适应度
					IFitness iFit 	= fitnessCalculator.computeFitness(x[i]);
					StupComplexityFitness scFit = (StupComplexityFitness) iFit;
					fit[i] 			= scFit.getFitness();
//					fit_attr[i] 	= scFit.getAttributeFitness();
//					fit_method[i] 	= scFit.getMethodFitness();
					
//					System.out.print(i + "\t" + x[i] + "\t" + fit[i] + "\t");
					
					// 记录位置和适应度值
					psoData.addPosition(i, k, x[i]);
					psoData.addFitness(i, k, fit[i]);
					
					// 单个粒子最优适应度，最优位置
					if (fit[i].compareTo(pbest[i]) == -1) {
						pbest[i] 	= fit[i];
						xpbest[i] 	= x[i];
					}
					
					// 种群粒子最优适应度，最优位置
					if (pbest[i].compareTo(gbest) == -1) {
						gbest 	= pbest[i];
						xgbest 	= xpbest[i];
					}
					
//					System.out.print(gbest + "\t" + xgbest + "\t");
				}
			}
//			System.out.print("\n");
			
			
			// 速度计算和位置计算
			{
				for (int i = 0; i < particleNumber; i++) {
					v[i] = w.multiply(new BigDecimal(v[i]))
							.add(C1.multiply(new BigDecimal(Math.random())).multiply(new BigDecimal(xpbest[i].subtract(x[i]))))
							.add(C2.multiply(new BigDecimal(Math.random())).multiply(new BigDecimal(xgbest.subtract(x[i]))))
							.toBigInteger();
					
					if (v[i].compareTo(VMAX) == 1) {
						v[i] = VMAX;
					}
					
					x[i] = x[i].add(v[i]);
					if (x[i].compareTo(WMAX) == 1) {
						x[i] = WMAX;
					} else if (x[i].compareTo(WMIN) == -1) {
						x[i] = WMIN;
					}
				}
			}
		}
		
		System.out.println("gbest: \t" + gbest);
		System.out.println("xgbest: \t" + xgbest);
		
		System.out.println("size of AttrDependencies" + ((StupComplexityFitnessCalculator) fitnessCalculator).getNumberOfAttrDependency(xgbest));
		System.out.println("size of MethodDependencies" + ((StupComplexityFitnessCalculator) fitnessCalculator).getNumberOfMethodDependency(xgbest));
		System.out.println("size of AllDependencies" + ((StupComplexityFitnessCalculator) fitnessCalculator).getNumberOfAllDependency2(xgbest));
		
		BigDecimal fit_attr 	= ((StupComplexityFitnessCalculator) fitnessCalculator).computeAttributeFitness(xgbest);
		BigDecimal fit_method 	= ((StupComplexityFitnessCalculator) fitnessCalculator).computeMethodFitness(xgbest);
		
		System.out.println("fit_attr: \t" + fit_attr);
		System.out.println("fit_method: \t" + fit_method);
		
		List<SourceClassInfo> scis = null;
		scis = ((StupComplexityFitnessCalculator) fitnessCalculator).decode(xgbest);
		for (SourceClassInfo sci : scis) {
			System.out.println(sci.className());			
		}

	}
	
	/*public static void main(String[] args) {
		// 参数：种群数，迭代数，位置区间最小值，位置区间最大值，速度最大值
		PSO pso = new PSO(50, 5, new BigInteger("1"), new BigInteger("100"), new BigInteger("3"), null);
		pso.doPSO();
	}*/

}
