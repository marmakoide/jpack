/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.marmakoide.numeric.Vector;
import org.marmakoide.numeric.sequences.Range;

/**
 * Testing for reduction operators
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2018-05-21
 */

public class ReductionTest {
	private final static int sSequenceSize = 1000;

	// --- Helper functions -----------------------------------------------------

	private static double integerSum(int n) {
		double l = n;
		return .5 * l * (l + 1);
	}

	// --- Tests ----------------------------------------------------------------

	@Test
	public void testVectorSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		double lExpectedValue = integerSum(sSequenceSize);
		assertEquals(lU.sum(), lExpectedValue, 2 * (Math.ulp(lExpectedValue) - lExpectedValue));
	}
}
