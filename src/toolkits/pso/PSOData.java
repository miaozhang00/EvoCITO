package toolkits.pso;

import global.GlobalTag;

import java.math.BigDecimal;
import java.math.BigInteger;

import process.StupComplexityFitnessCalculator;

public class PSOData {

	/*
	 * ��Ⱥ��Ŀ
	 */
	int particleNumber;
	
	/*
	 * ��������
	 */
	int iterations;
	
	/*
	 * ������Сֵ
	 */
	BigInteger WMIN;
	
	/*
	 * �������ֵ
	 */
	BigInteger WMAX;
	
	/*
	 * �ٶ����ֵ
	 */
	BigInteger VMAX;
	
	/*
	 * ��Ӧ��ֵ������
	 */
	StupComplexityFitnessCalculator scf;
	
	/*
	 * ����λ��
	 */
	BigInteger pos[][];
	
	/*
	 * ������Ӧ��ֵ
	 */
	BigDecimal fit[][];
	
	public PSOData(
			int particleNumber,
			int iterations,
			BigInteger WMIN,
			BigInteger WMAX,
			BigInteger VMAX,
			StupComplexityFitnessCalculator scf
			) {
		this.particleNumber = particleNumber;
		this.iterations 	= iterations;
		this.WMIN 			= WMIN;
		this.WMAX 			= WMAX;
		this.VMAX 			= VMAX;
		this.scf 			= scf;
		
		this.pos 	= new BigInteger[particleNumber][iterations+1];
		this.fit	= new BigDecimal[particleNumber][iterations+1];
	}
	
	public int getParticleNumber() {
		return this.particleNumber;
	}
	
	public int getIterations() {
		return this.iterations;
	}
	
	public BigInteger getWMIN() {
		return this.WMIN;
	}
	
	public BigInteger getWMAX() {
		return this.WMAX;
	}
	
	public BigInteger getVMAX() {
		return this.VMAX;
	}
	
	public StupComplexityFitnessCalculator getFitness() {
		return this.scf;
	}
	
	public void addPosition(int particial, int iteration, BigInteger position) {
		this.pos[particial][iteration] = position;
	}
	
	public void addFitness(int particial, int iteration, BigDecimal fitness) {
		this.fit[particial][iteration] = fitness;
	}
	
	public String toDump() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.PSO_DATA + "\n");
		
		sb.append(GlobalTag.PARTICAL_NUM + "\t" + particleNumber + "\n");
		sb.append(GlobalTag.ITERATIONS + "\t" + iterations + "\n");
		
		for (int ite=0; ite<=iterations; ++ite) {
			
			sb.append(GlobalTag.ITERATION + "\t" + ite);
			
			for (int partical=0; partical<particleNumber; ++partical) {
				BigInteger position	= pos[partical][ite];
				BigDecimal fitness 	= fit[partical][ite];
				
				sb.append("\t" + GlobalTag.PARTICAL + "_" + partical
						+ "\t" + position + "\t" + fitness);
			}
			
			sb.append("\n");
		}
		
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		sb.append(GlobalTag.SEPARATE_64 + "\n");
		
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
