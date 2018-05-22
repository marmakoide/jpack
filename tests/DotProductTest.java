/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.marmakoide.numeric.Vector;
import org.marmakoide.numeric.sequences.Range;

/**
 * Testing for dot product
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2018-05-21
 */

public class DotProductTest {
	private final static int sSequenceSize = 1000;

	// --- Helper functions -----------------------------------------------------

	private static double integerSquareSum(int inN) {
		double lDblN = inN;
		return (lDblN * (lDblN + 1) * (2 * lDblN + 1)) / 6.;
	}

	private static double integerSquareSumRange(int inFrom, int inTo) {
		return integerSquareSum(inTo) - integerSquareSum(inFrom);
	}

	// --- Test on full vector --------------------------------------------------

	@Test
	public void testVectorDotProduct() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));

		double lExpectedValue = integerSquareSum(sSequenceSize);
		assertEquals(lU.dot(lU), lExpectedValue, 2 * (Math.ulp(lExpectedValue) - lExpectedValue));
	}

	// --- Test on vector slice -------------------------------------------------

	@Test
	public void testVectorSliceDotProduct() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		
		int lA = lU.getSize() / 4;
		int lB = (3 * lU.getSize()) / 4;

		// Front slice
		Vector lVFront = lU.slice(0, lA);
		double lExpectedValueFront = integerSquareSumRange(0, lA);
		assertEquals(lVFront.dot(lVFront), lExpectedValueFront, 2 * (Math.ulp(lExpectedValueFront) - lExpectedValueFront));

		// Middle slice
		Vector lVMiddle = lU.slice(lA, lB);
		double lExpectedValueMiddle = integerSquareSumRange(lA, lB);
		assertEquals(lVMiddle.dot(lVMiddle), lExpectedValueMiddle, 2 * (Math.ulp(lExpectedValueMiddle) - lExpectedValueMiddle));

		// Back slice
		Vector lVBack = lU.slice(lB, lU.getSize());
		double lExpectedValueBack = integerSquareSumRange(lB, lU.getSize());
		assertEquals(lVBack.dot(lVBack), lExpectedValueBack, 2 * (Math.ulp(lExpectedValueBack) - lExpectedValueBack));
	}
}
