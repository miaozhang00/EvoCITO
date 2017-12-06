package process;

import java.math.BigInteger;
import java.util.ArrayList;

public class Factorial {
	
	BigInteger n;
	
	// 记录从1! -> n!的值
	ArrayList<BigInteger> sub_fts;
	
	public Factorial(BigInteger n) {
		this.n = n;
		this.sub_fts = new ArrayList<BigInteger>();
		
		initial();
	}
	
	private void initial() {
		initialSubFactorials(n);
		
		System.out.println(sub_fts);
	}
	
	// 递归方法，将1! -> n!的值加入sub_fts
	private BigInteger initialSubFactorials(BigInteger bi_n) {
		BigInteger bi_1 = new BigInteger("1");
		if (bi_n.equals(new BigInteger("1"))) {
			sub_fts.add(bi_1);
			return bi_n;
		} else {
			BigInteger bi_i = bi_n.multiply(initialSubFactorials(bi_n.subtract(bi_1)));
			sub_fts.add(bi_i);
			return bi_i;
		}
	}
	
	public BigInteger getSubFactorial(int i) {
		return sub_fts.get(i);
	}
	
	public static void main(String[] args) {
		Factorial factorial = new Factorial(new BigInteger("10"));
		factorial.getSubFactorial(0);
	}
	
}
