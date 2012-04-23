package com.xebia.fitnesse.restfulservice;

import java.math.BigDecimal;

public class MatchingHelper {

	public boolean matchEquals(String s1, String s2) {
		return s1.equals(s2);
	}

	public boolean matchCurrency(String s1, String s2) {
		BigDecimal bd1 = new BigDecimal(s1);
		BigDecimal bd2 = new BigDecimal(s2);

		bd1.setScale(2);
		bd2.setScale(2);

		return bd1.compareTo(bd2) == 0;
	}
	
	public boolean matchSummation(String values, String sum) {
		// TODO: implement
		return false;
	}
}
