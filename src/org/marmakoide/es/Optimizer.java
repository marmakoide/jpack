/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import java.util.LinkedList;
import java.util.List;

import org.marmakoide.numeric.Vector;

/**
 * Manages an optimization run.
 * <p>
 * An optimizer manages an optimization run, handling most of the low-level
 * details for you, and protecting you from some common mistakes. You might use
 * this instead of directly using a Strategy instance directly. You can
 * implement the OptimizerListener interface to monitor an optimization run to
 * your convinience.
 * 
 * @see org.marmakoide.es.Strategy
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-08
 */

public class Optimizer {
  public final static int DEFAULT_MAX_NB_RETRY = 32;

  private long mSeed;
  private int mNbEvals;
  private int mMaxNbEvals;
  private final int mMaxNbRetry;
  private final Evaluator mEvaluator;
  private final Strategy mStrategy;
  private final Vector mX;

  private final List<OptimizerListener> mListeners;

  // --- Constructor ----------------------------------------------------------

  public Optimizer(final Evaluator inEvaluator, final Distribution inDistrib) {
    if (inEvaluator == null) {
      throw new NullPointerException("Null reference passed as evaluator");
    }

    if (inDistrib == null) {
      throw new NullPointerException("Null reference passed as distribution");
    }

    final int N = inEvaluator.getDimension();

    this.mEvaluator = inEvaluator;

    this.mStrategy = new Strategy(N);
    this.mStrategy.setDistribution(inDistrib);
    this.mMaxNbRetry = Optimizer.DEFAULT_MAX_NB_RETRY;

    this.mX = new Vector(N);
    this.mListeners = new LinkedList<OptimizerListener>();
  }

  // --- Accessors ------------------------------------------------------------

  /**
   * Return the initial seed for the run's random number generation
   * 
   * @return the seed for the run's random number generation
   */
  public long getSeed() {
    return this.mSeed;
  }

  /**
   * Return the number of evaluations computed during the current run
   * 
   * @return the number of evaluations computed so far
   */
  public int getNbEvaluations() {
    return this.mNbEvals;
  }

  /**
   * Return the maximum number of evaluations allowed.
   * 
   * @return the maximum number of evaluations allowed. If no limits is set
   *         then 0 is returned
   */
  public int getNbMaxEvaluations() {
    return this.mMaxNbEvals;
  }

  /**
   * Return the maximum number of retry for a candidate solution
   * 
   * An evaluator might be unable to compute the fitness of a given solution.
   * One possible strategy is to sample a different solution and retry. To
   * avoid an infinite loop, the number of retry is capped.
   * 
   * @return the maximum number of retry for a candidate solution
   */
  public int getMaxNbRetry() {
    return this.mMaxNbRetry;
  }

  /**
   * Sets the maximum number of retry for a candidate solution
   * 
   * An evaluator might be unable to compute the fitness of a given solution.
   * One possible strategy is to sample a different solution and retry. To
   * avoid an infinite loop, the number of retry is capped.
   * 
   * @param inNbEvals
   *          the maximum number of retry for a candidate solution
   */
  public void setNbMaxEvaluations(final int inNbEvals) {
    if (inNbEvals < 0) {
      throw new IllegalArgumentException("Negative number of evaluations");
    }
    this.mMaxNbEvals = inNbEvals;
  }

  /**
   * Returns the strategy managed by the optimizer.
   * 
   * @return the strategy managed by the optimizer
   */
  public Strategy getStrategy() {
    return this.mStrategy;
  }

  // --- Optimization management ----------------------------------------------

  /**
   * Perform an optimization run, with a trully random seed
   */
  public void run() {
    this.run(Double.doubleToRawLongBits(Math.random()));
  }

  /**
   * Perform an optimization run, with a user provided random seed
   */
  public void run(final long inSeed) {
    this.mSeed = inSeed;
    this.mNbEvals = 0;

    // Initialize
    this.mStrategy.getRandom().setSeed(this.mSeed);
    this.mEvaluator.setInitialVector(this.mStrategy.getRandom(), this.mX);

    // Iterate
    this.mStrategy.start();
    this.fireOnStart();
    do {
      // Sample
      this.mStrategy.sampleCloud();

      // Evaluate
      for (final Point lPoint : this.mStrategy.getPoints()) {
        int lNbTrials = 0;
        boolean lFailed = true;
        do {
          this.mX.copy(lPoint.getX());

          try {
            lPoint.setFitness(this.mEvaluator.getFitness(this.mX));
            lFailed = false;
          } catch (final EvaluationException e) {
            this.mStrategy.samplePoint(lPoint);
          }

          lNbTrials += 1;
          this.mNbEvals += 1;
        } while ((lFailed) && (lNbTrials < this.mMaxNbRetry));

        if (lNbTrials >= this.mMaxNbRetry) {
          ;
        }
      }

      // Update
      this.mStrategy.update();
      this.fireOnUpdate();

      // Check max eval
      if ((this.mMaxNbEvals > 0) && (this.mNbEvals >= this.mMaxNbEvals)) {
        break;
      }
    } while (!this.mStrategy.stop());

    // Job done
    this.fireOnStop();
  }

  // --- Listeners management -------------------------------------------------

  /**
   * Add a listener to the optimizer
   * 
   * @param inListener
   *          the listener to add
   */
  public void addListener(final OptimizerListener inListener) {
    if (inListener == null) {
      throw new NullPointerException("Null reference passed as a listerner");
    }

    this.mListeners.add(inListener);
  }

  /**
   * Remove a listener, previously added to the optimizer
   * 
   * @param inListener
   *          the listener to remover
   */
  public void removeListener(final OptimizerListener inListener) {
    this.mListeners.remove(inListener);
  }

  private void fireOnStart() {
    for (final OptimizerListener lListener : this.mListeners) {
      lListener.onStart(this);
    }
  }

  private void fireOnUpdate() {
    for (final OptimizerListener lListener : this.mListeners) {
      lListener.onUpdate(this);
    }
  }

  private void fireOnStop() {
    for (final OptimizerListener lListener : this.mListeners) {
      lListener.onStop(this);
    }
  }
}
