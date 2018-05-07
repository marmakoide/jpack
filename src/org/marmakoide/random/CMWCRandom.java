/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.random;

import java.util.Random;

/**
 * A drop-in replacement for the standard pseudo-random number generator.
 * 
 * This implementation is significantly faster than the standard pseudo-random
 * number generator, while having (much) better properties, regarding the
 * statistics of its output and the cycle length. Note that this implementation
 * is not thread-safe. It is a straight implementation of the CMWC generator
 * from George Marsaglia, with state size of 1024
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-11
 */

public class CMWCRandom extends Random {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final static int ARRAY_SIZE = 1024; // period approx 2^32794
  private final static long MULTIPLIER = 5555698L;
  private final static long INITIAL_CARRY = 362436L;

  private long[] mArray;
  private long mMultiplier;
  private long mCarry;
  private int mIndex;

  public CMWCRandom() {
    super(Double.doubleToRawLongBits(Math.random()));
  }

  public CMWCRandom(final long inSeed) {
    super(inSeed);
  }

  private void setup() {
    if (this.mArray == null) {
      this.mArray = new long[CMWCRandom.ARRAY_SIZE];
      this.mMultiplier = CMWCRandom.MULTIPLIER;
    }
  }

  private void reset() {
    this.mCarry = CMWCRandom.INITIAL_CARRY;
    this.mIndex = this.mArray.length - 1;
  }

  @Override
  protected int next(final int inBits) {
    final long t = (this.mMultiplier * this.mArray[this.mIndex]) + this.mCarry;

    final long div32 = t >>> 32;
    this.mCarry = div32
        + ((t & 0xffffffffL) >= (0xffffffffL - div32) ? 1L : 0L);
    this.mArray[this.mIndex] = (0xfffffffeL - (t & 0xffffffffL)
        - ((this.mCarry - div32) << 32) - this.mCarry) & 0xffffffffL;

    final long lRet = this.mArray[this.mIndex];
    this.mIndex = (this.mIndex + 1) & (this.mArray.length - 1);
    return (int) (lRet >>> (32 - inBits));
  }

  @Override
  public void setSeed(final long inSeed) {
    this.setup();

    // Seed the state of the RNG with a 32 bits XorShift generator
    // See Marsaglia, George "Xorshift RNGs", Journal of Statistical Software
    long x = 123456789L;
    long y = 362436069L;
    long z = 521288629L;
    long w = inSeed;

    for (int i = 0; i < this.mArray.length; ++i) {
      final long t = x ^ (x << 11);
      x = y;
      y = z;
      z = w;
      w = w ^ (w >> 19) ^ (t ^ (t >> 8));
      this.mArray[i] = w;
    }

    this.reset();
  }
}
