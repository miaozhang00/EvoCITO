package process;

import java.math.BigDecimal;

import pso.IFitness;

public class StupComplexityFitness implements IFitness {

    BigDecimal fit = new BigDecimal("0");
    // BigDecimal fit_attr = new BigDecimal("0");
    // BigDecimal fit_method = new BigDecimal("0");

    public StupComplexityFitness(BigDecimal fit
    // BigDecimal fit,
    // BigDecimal fit_attr,
    // BigDecimal fit_method
    ) {
        this.fit = fit;
        // this.fit_attr = fit_attr;
        // this.fit_method = fit_method;
    }

    public BigDecimal getFitness() {
        return this.fit;
    }

    // public BigDecimal getAttributeFitness() {
    // return this.fit_attr;
    // }
    //
    // public BigDecimal getMethodFitness() {
    // return this.fit_method;
    // }

}
