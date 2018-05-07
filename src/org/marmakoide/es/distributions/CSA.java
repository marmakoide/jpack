/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es.distributions;

import org.marmakoide.es.Distribution;
import org.marmakoide.es.StopCriterionId;
import org.marmakoide.es.Strategy;
import org.marmakoide.numeric.Matrix;
import org.marmakoide.numeric.Vector;

/*Implements an isotropic gaussian distribution, with Cumulative Step Length
 * Adaption heuristic for the step-size control.
 * 
 * The stopping criterion is satisfied when the step-length is bellow the CPU
 * floating point precision.
 * 
 * The full algorithm is described in : Evolution Strategies with Cumulative
 * Step Length Adaption on the Noisy Parabolic Ridge Dirk V. Arnold &
 * Hans-Georg Beyer */

public class CSA implements Distribution {
  // State variables
  private double mC; // Cumulation parameter
  private double mDampening; // Adaption dampening
  private double mSigma; // Mutation strength
  private Vector mSigmaPath; // Evolution path for sigma

  // Control variables
  private double mSigmaInit;
  private double mSigmaStop;

  // --- Constructor ----------------------------------------------------------

  public CSA() {
    this.setSigma(1.0, 1e-12);
  }

  // --- Setup API ------------------------------------------------------------

  public void setSigma(final double inSigmaInit, final double inSigmaStop) {
    if (this.mSigmaInit < 0.0) {
      throw new IllegalArgumentException("sigmaInit inferior to 0.0");
    }

    if (this.mSigmaStop < 0.0) {
      throw new IllegalArgumentException("sigmaStop inferior to 0.0");
    }

    if (this.mSigmaInit < this.mSigmaStop) {
      throw new IllegalArgumentException("sigmaInit inferior to sigmaStop");
    }

    this.mSigmaInit = inSigmaInit;
    this.mSigmaStop = inSigmaStop;
  }

  // --- Accessors ------------------------------------------------------------

  public double getSigma() {
    return this.mSigma;
  }

  // --- Distribution interface implementation --------------------------------

  @Override
  public void setup(final int inN) {
    this.mC = 1.0 / Math.sqrt(inN);
    this.mDampening = 1.0 / (2.0 * inN * Math.sqrt(inN));
    this.mSigmaPath = new Vector(inN);
  }

  @Override
  public void start(final Strategy inStrategy) {
    this.mSigma = this.mSigmaInit;
    this.mSigmaPath.fill(0.0);
  }

  @Override
  public void update(final Strategy inStrategy) {
    // Constants
    final double N = inStrategy.getN();
    final double mu = inStrategy.getMu();

    // Cumulate sigma evolution path
    this.mSigmaPath.scale(1.0 - this.mC).scaledAdd(
        Math.sqrt(mu * this.mC * (2.0 - this.mC)), inStrategy.getZMean());

    // Adapt sigma
    this.mSigma *= Math.exp(this.mDampening
        * (Math.sqrt(this.mSigmaPath.squareSum()) - N));
  }

  @Override
  public void samplePoint(final Strategy inStrategy, final Vector outX,
      final Vector outZ) {
    // Generate Z
    outZ.fillAsRandNorm(inStrategy.getRandom());

    // Compute X
    outX.scaledCopy(this.mSigma, outZ).add(inStrategy.getXMean());
  }

  @Override
  public void sampleCloud(final Strategy inStrategy, final Matrix outX,
      final Matrix outZ) {
    // Generate Z
    outZ.fillAsRandNorm(inStrategy.getRandom());

    // Compute X
    outX.scaledCopy(this.mSigma, outZ).colWise().add(inStrategy.getXMean());
  }

  @Override
  public StopCriterionId stop(final Strategy inStrategy) {
    if (this.mSigma <= this.mSigmaStop) {
      return StopCriterionId.LowSigma;
    }

    return StopCriterionId.None;
  }
}
