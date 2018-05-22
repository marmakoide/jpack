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

	private static double integerSum(int inN) {
		double lDblN = inN;
		return (lDblN * (lDblN + 1)) / 2.;
	}

	private static double integerSumRange(int inFrom, int inTo) {
		return integerSum(inTo) - integerSum(inFrom);
	}

	private static double integerSquareSum(int inN) {
		double lDblN = inN;
		return (lDblN * (lDblN + 1) * (2 * lDblN + 1)) / 6.;
	}

	private static double integerSquareSumRange(int inFrom, int inTo) {
		return integerSquareSum(inTo) - integerSquareSum(inFrom);
	}

	// --- Test on full vector --------------------------------------------------

	@Test
	public void testVectorSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));

		double lExpectedValue = integerSum(sSequenceSize);
		assertEquals(lU.sum(), lExpectedValue, 2 * (Math.ulp(lExpectedValue) - lExpectedValue));
	}

	@Test
	public void testVectorSquareSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));

		double lExpectedValue = integerSquareSum(sSequenceSize);
		assertEquals(lU.squareSum(), lExpectedValue, 2 * (Math.ulp(lExpectedValue) - lExpectedValue));
	}

	@Test
	public void testVectorAbsSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		for(int i = 0; i < sSequenceSize; i += 2)
			lU.set(i, -lU.get(i));

		double lExpectedValue = integerSum(sSequenceSize);
		assertEquals(lU.absSum(), lExpectedValue, 2 * (Math.ulp(lExpectedValue) - lExpectedValue));
	}

	// --- Test on vector slice -------------------------------------------------

	@Test
	public void testVectorSliceSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		
		int lA = lU.getSize() / 4;
		int lB = (3 * lU.getSize()) / 4;

		// Front slice
		Vector lVFront = lU.slice(0, lA);
		double lExpectedValueFront = integerSumRange(0, lA);
		assertEquals(lVFront.sum(), lExpectedValueFront, 2 * (Math.ulp(lExpectedValueFront) - lExpectedValueFront));

		// Middle slice
		Vector lVMiddle = lU.slice(lA, lB);
		double lExpectedValueMiddle = integerSumRange(lA, lB);
		assertEquals(lVMiddle.sum(), lExpectedValueMiddle, 2 * (Math.ulp(lExpectedValueMiddle) - lExpectedValueMiddle));

		// Back slice
		Vector lVBack = lU.slice(lB, lU.getSize());
		double lExpectedValueBack = integerSumRange(lB, lU.getSize());
		assertEquals(lVBack.sum(), lExpectedValueBack, 2 * (Math.ulp(lExpectedValueBack) - lExpectedValueBack));
	}

	@Test
	public void testVectorSliceSquareSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		
		int lA = lU.getSize() / 4;
		int lB = (3 * lU.getSize()) / 4;

		// Front slice
		Vector lVFront = lU.slice(0, lA);
		double lExpectedValueFront = integerSquareSumRange(0, lA);
		assertEquals(lVFront.squareSum(), lExpectedValueFront, 2 * (Math.ulp(lExpectedValueFront) - lExpectedValueFront));

		// Middle slice
		Vector lVMiddle = lU.slice(lA, lB);
		double lExpectedValueMiddle = integerSquareSumRange(lA, lB);
		assertEquals(lVMiddle.squareSum(), lExpectedValueMiddle, 2 * (Math.ulp(lExpectedValueMiddle) - lExpectedValueMiddle));

		// Back slice
		Vector lVBack = lU.slice(lB, lU.getSize());
		double lExpectedValueBack = integerSquareSumRange(lB, lU.getSize());
		assertEquals(lVBack.squareSum(), lExpectedValueBack, 2 * (Math.ulp(lExpectedValueBack) - lExpectedValueBack));
	}

	@Test
	public void testVectorSliceAbsSum() {
		Vector lU = Vector.fromSequence(new Range(1., (double)(sSequenceSize + 1)));
		
		int lA = lU.getSize() / 4;
		int lB = (3 * lU.getSize()) / 4;

		// Front slice
		Vector lVFront = lU.slice(0, lA);
		double lExpectedValueFront = integerSumRange(0, lA);
		assertEquals(lVFront.absSum(), lExpectedValueFront, 2 * (Math.ulp(lExpectedValueFront) - lExpectedValueFront));

		// Middle slice
		Vector lVMiddle = lU.slice(lA, lB);
		double lExpectedValueMiddle = integerSumRange(lA, lB);
		assertEquals(lVMiddle.absSum(), lExpectedValueMiddle, 2 * (Math.ulp(lExpectedValueMiddle) - lExpectedValueMiddle));

		// Back slice
		Vector lVBack = lU.slice(lB, lU.getSize());
		double lExpectedValueBack = integerSumRange(lB, lU.getSize());
		assertEquals(lVBack.absSum(), lExpectedValueBack, 2 * (Math.ulp(lExpectedValueBack) - lExpectedValueBack));
	}
}
