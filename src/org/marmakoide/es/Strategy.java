/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.marmakoide.numeric.Matrix;
import org.marmakoide.numeric.Vector;
import org.marmakoide.random.CMWCRandom;

/**
 * A (mu,lambda) evolution strategy.
 * <p>
 * A Strategy is intended to be used as following.
 * <ol>
 * <li>Setup : set the strategy as you wish
 * <li>
 * <ol>
 * Iterations : optimize a solution for a given problem
 * <li> <code>start()</code> to initializa the iterations
 * <li> <code>sampleCloud()</code> to generate a new population. You have to
 * assign the fitness of each point by yourself.
 * <li> <code>update</code> to update the population
 * <li> <code>stop()</code> to check if more iterations can be done
 * </ol>
 * </ol>
 * 
 * The idea is that you have to write by yourself the optimization loop. The
 * motivation for this is flexibility, say, distributing fitness evaluations
 * over several threads or computers. For an easier to use, managed
 * optimization loop, use the <code>Optimizer</code> class.
 * 
 * @see org.marmakoide.es.Optimizer
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-08
 */

public class Strategy {
  private final int mN;
  private int mMu;
  private int mLambda;
  private int mPrevLambda;
  private int mNbUpdates;
  private int mNbUpdatesBestFitnessStalled;
  private int mNbUpdatesBestFitnessStalledLimit;

  private Distribution mDistribution;

  private final Random mRandom;

  private Vector mMeanWeights;
  private boolean mMeanWeightsSetupDone;
  private MeanWeightsGenerator mMeanWeightsGenerator;

  private Matrix mX;
  private Matrix mZ;
  private final Vector mXMean;
  private final Vector mZMean;

  private Point[] mPoints;
  private final Point mBestPoint;
  private final Comparator<Point> mPointComparator;

  private StopCriterionId mStopCriterionId;

  /**
   * Create an Evolution Strategy instance
   * 
   * @param inN
   *          Dimension of the search space
   */
  public Strategy(final int inN) {
    if (inN <= 0) {
      throw new IllegalArgumentException("N inferior to 1");
    }

    this.mN = inN;
    this.mRandom = new CMWCRandom();

    // Allocations independent from population size
    this.mXMean = new Vector(this.mN);
    this.mZMean = new Vector(this.mN);
    this.mBestPoint = new Point(this.mN);

    // Default fitness order is for minimization
    this.mPointComparator = new MinPointComparator();

    // Default setting for mean weights
    this.setMeanWeightsGenerator(new org.marmakoide.es.meanWeightsGenerators.Log());

    // Default setting for mu & lambda
    final int lDefaultLambda = (int) Math
        .ceil(4.0 + (3.0 * Math.log(this.mN)));
    this.setMuLambda(lDefaultLambda / 2, lDefaultLambda);
    this.mPrevLambda = 0;
  }

  // --- Accessors ------------------------------------------------------------

  /**
   * Return the dimension of the search space
   * 
   * @return the dimension of the search space
   */
  public int getN() {
    return this.mN;
  }

  /**
   * Return the number of points selected to renew the population during an
   * update ('mu' in the Evolution Strategy jargon)
   * 
   * @return the number of points selected to renew the population during an
   *         update
   */
  public int getMu() {
    return this.mMu;
  }

  /**
   * Return the population size ('lambda' in the Evolution Strategy jargon)
   * 
   * @return the population size
   */
  public int getLambda() {
    return this.mLambda;
  }

  /**
   * Return the number of iterations, (or optimization steps, or epochs, same
   * thing) performed since the last call to start()
   * 
   * @return the number of iterations
   */
  public int getNbUpdates() {
    return this.mNbUpdates;
  }

  public Vector getMeanWeights() {
    return this.mMeanWeights;
  }

  /**
   * Return the pseudo-random number generator used by this instance.
   * <p>
   * Note that the actual instance might not be, and should not be assumed to
   * be, an instance of java.util.Random, but it can be an unspecified
   * specialization of it.
   * 
   * @return the pseudo-random number generator used by this instance
   */
  public Random getRandom() {
    return this.mRandom;
  }

  /**
   * Return the instance of Distribution used by the Strategy instance
   * 
   * @return an instance of Distribution
   */
  public Distribution getDistribution() {
    return this.mDistribution;
  }

  /**
   * Set the instance of Distribution used by the Strategy instance.
   * 
   * @param inDistribution
   *          An instance of Distribution
   */
  public void setDistribution(final Distribution inDistribution) {
    this.mDistribution = inDistribution;
    this.mDistribution.setup(this.mN);
  }

  /**
   * Return the current population.
   * <p>
   * Reading the points state is ok. However, keeping references to it or
   * changing their state have an unspecified and potentially hazardous effect.
   * 
   * @return the current population.
   */
  public Point[] getPoints() {
    return this.mPoints;
  }

  /**
   * Return the best point seen since the lastest call to start().
   * 
   * @return the best point seen since the lastest call to start().
   */
  public Point getBestPoint() {
    return this.mBestPoint;
  }

  public Vector getXMean() {
    return this.mXMean;
  }

  public Vector getZMean() {
    return this.mZMean;
  }

  /**
   * Return an enum value specifying the reason for the strategy to stop.
   * 
   * @see #stop()
   * @return an enum value specifying the reason for the strategy to stop.
   */
  public StopCriterionId getStopCriterionId() {
    return this.mStopCriterionId;
  }

  // --- Setup API ------------------------------------------------------------

  public void setMeanWeightsGenerator(final MeanWeightsGenerator inGenerator) {
    this.mMeanWeightsSetupDone = false;
    this.mMeanWeightsGenerator = inGenerator;
  }

  /**
   * Set the population size.
   * 
   * @param inMu
   *          Number of points used to generate a new population
   * @param inLambda
   *          Size of the population
   */
  public void setMuLambda(final int inMu, final int inLambda) {
    if (inLambda < 1) {
      throw new IllegalArgumentException("lambda inferior to 1");
    }

    if (inMu < 1) {
      throw new IllegalArgumentException("mu inferior to 1");
    }

    if (inMu > inLambda) {
      throw new IllegalArgumentException("mu superior to lambda");
    }

    this.mMu = inMu;
    this.mLambda = inLambda;
    this.mNbUpdatesBestFitnessStalledLimit = (int) Math.ceil(10 + Math
        .floor((30.0 * this.mN) / this.mLambda));
    this.mMeanWeightsSetupDone = false;
  }

  // --- Iterations API -------------------------------------------------------

  /**
   * Initialization step.
   * 
   * Initialize the point's population.
   */
  public void start() {
    // Check if a distribution object is set
    if (this.mDistribution == null) {
      throw new DistributionNotSetException();
    }

    // Setup population if required
    if (this.mPrevLambda != this.mLambda) {
      this.setupPopulation(this.mLambda);
      this.mPrevLambda = this.mLambda;
    }

    // Generate the weights to compute the distribution center
    if (!this.mMeanWeightsSetupDone) {
      this.setupMeanWeights();
      this.mMeanWeightsSetupDone = true;
    }

    // Start the distribution
    this.mDistribution.start(this);

    // Job done
    this.mNbUpdates = 0;
    this.mNbUpdatesBestFitnessStalled = 0;
    this.mStopCriterionId = StopCriterionId.None;
  }

  /**
   * Sampling step.
   * 
   * Generate a whole new population.
   * 
   * start() should be called before starting to call sampleCloud().
   */
  public void sampleCloud() {
    this.mDistribution.sampleCloud(this, this.mX, this.mZ);
  }

  /**
   * Generate a given point.
   * 
   * If a given point have coordinates which are not valid for your
   * optimization problem, you can resample that point to get a new one.
   * 
   * start() should be called before starting to call samplePoint().
   */
  public void samplePoint(final Point inPoint) {
    this.mDistribution.samplePoint(this, inPoint.getX(), inPoint.getZ());
  }

  /**
   * Update step.
   * 
   * Update the point's population and the best point seen so far.
   * <p>
   * start() should be called before starting to call update().
   * <p>
   * sampleCloud() should be called before each call, then the fitness for the
   * whole population have to be assigned.
   */
  public void update() {
    // Sort points according to their fitnesses
    Arrays.sort(this.mPoints, 0, this.mLambda, this.mPointComparator);

    // Update best point ever
    final Point lPoint = this.mPoints[0];
    if ((this.mNbUpdates == 0)
        || (this.mPointComparator.compare(this.mBestPoint, lPoint) == 1)) {
      this.mBestPoint.copy(lPoint);
      this.mNbUpdatesBestFitnessStalled += 1;
    } else {
      this.mNbUpdatesBestFitnessStalled = 0;
    }

    // Update xMean
    this.mXMean.fill(0.0);
    for (int i = 0; i < this.mMu; ++i) {
      this.mXMean.scaledAdd(this.mMeanWeights.get(i), this.mPoints[i].getX());
    }

    // Update zMean
    this.mZMean.fill(0.0);
    for (int i = 0; i < this.mMu; ++i) {
      this.mZMean.scaledAdd(this.mMeanWeights.get(i), this.mPoints[i].getZ());
    }

    // Update the distribution
    this.mDistribution.update(this);

    // Job done
    this.mNbUpdates += 1;
  }

  /**
   * Indicate whether further update can be done or not.
   * 
   * @see #getStopCriterionId()
   * @return true if a stopping criterion is fullfilled, false otherwise
   */
  public boolean stop() {
    if (this.mStopCriterionId == StopCriterionId.None) {
      if (this.mNbUpdatesBestFitnessStalled > this.mNbUpdatesBestFitnessStalledLimit) {
        this.mStopCriterionId = StopCriterionId.BestFitnessStall;
      } else {
        this.mStopCriterionId = this.mDistribution.stop(this);
      }
    }

    return this.mStopCriterionId != StopCriterionId.None;
  }

  // --- Internals
  // -------------------------------------------------------------

  private void setupPopulation(final int inPopSize) {
    this.mMeanWeights = new Vector(this.mMu);
    this.mX = new Matrix(this.mN, inPopSize);
    this.mZ = new Matrix(this.mN, inPopSize);

    if ((this.mPoints == null) || (this.mPoints.length < inPopSize)) {
      this.mPoints = new Point[inPopSize];
    }

    for (int i = 0; i < inPopSize; ++i) {
      this.mPoints[i] = new Point(this.mX.col(i), this.mZ.col(i));
    }
  }

  private void setupMeanWeights() {
    this.mMeanWeightsGenerator.generate(this.mMeanWeights);
    this.mMeanWeights.invScale(this.mMeanWeights.sum());
  }
}
