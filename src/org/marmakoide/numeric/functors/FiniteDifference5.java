/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric.functors;

import org.marmakoide.numeric.UnaryFunctor;

/**
 * Implements a 5 points stencil finite difference as a functor.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-02-08
 */

public final class FiniteDifference5 implements UnaryFunctor {
  private final double DEFAULT_EPSILON = 1e-15;

  private final double mEps;
  private final UnaryFunctor mFunc;

  public FiniteDifference5(final UnaryFunctor inFunc) {
    this.mEps = this.DEFAULT_EPSILON;
    this.mFunc = inFunc;
  }

  public FiniteDifference5(final UnaryFunctor inFunc, final double inEps) {
    this.mEps = inEps;
    this.mFunc = inFunc;
  }

  public double getEpsilon() {
    return this.mEps;
  }

  @Override
  public double getImage(final double inU) {
    return ((this.mFunc.getImage(inU - (2 * this.mEps)) - this.mFunc
        .getImage(inU + (2 * this.mEps))) + (8 * (this.mFunc.getImage(inU
        - this.mEps) - this.mFunc.getImage(inU + this.mEps))))
        / (12 * this.mEps);
  }
}
