package pso;

import java.math.BigInteger;

public interface IFitnessCalculator {
	
	public IFitness computeFitness(BigInteger x);
	
}
