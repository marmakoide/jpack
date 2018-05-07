/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import java.io.PrintStream;

/**
 * An object that logs information from an optimization run.
 * 
 * At each iteration, the current number of evaluations performed and the
 * current best fitness is printed to a text stream. Some general informations
 * on the underlaying strategy and the stopping criteria are also displayed.
 */

public class OptimizerLogger implements OptimizerListener {
  private final PrintStream mStream;

  // --- Constructors ---------------------------------------------------------

  /**
   * Create an OptimizerLogger logging to the standard output
   */
  public OptimizerLogger() {
    this.mStream = System.out;
  }

  /**
   * Create an OptimizerLogger
   * 
   * @param inStream
   *          The output stream to log to
   */
  public OptimizerLogger(final PrintStream inStream) {
    this.mStream = inStream;
  }

  // --- OptimizerListener interface implementation ---------------------------

  @Override
  public void onStart(final Optimizer inOptim) {
    final Strategy lStrategy = inOptim.getStrategy();

    this.mStream.printf("# N=%d mu=%d lambda=%d seed=%d\n", lStrategy.getN(),
        lStrategy.getMu(), lStrategy.getLambda(), inOptim.getSeed());
  }

  @Override
  public void onUpdate(final Optimizer inOptim) {
    this.mStream.printf("%d %f\n", inOptim.getNbEvaluations(), inOptim
        .getStrategy().getBestPoint().getFitness());
  }

  @Override
  public void onStop(final Optimizer inOptim) {
    this.mStream.append("# stopping criteria=").println(
        inOptim.getStrategy().getStopCriterionId());
  }
}
