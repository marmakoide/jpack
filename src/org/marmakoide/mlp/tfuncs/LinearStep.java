/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp.tfuncs;

import org.marmakoide.mlp.TransferFunction;
import org.marmakoide.numeric.UnaryFunctor;
import org.marmakoide.numeric.Vector;

/**
 * The linear step transfer function.
 * <p>
 * This function returns
 * <ul>
 * <li>-1 if input < -1
 * <li>identity if -1 < input < 1
 * <li>1 if input > 1
 * </ul>
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-05-07
 */

public final class LinearStep implements TransferFunction {
  private final class LinearStepFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return Math.abs(inU) < 1.0 ? inU : Math.signum(inU);
    }
  }

  private final class RectangleFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return Math.abs(inU) < 1.0 ? 1.0 : 0.0;
    }
  }

  @Override
  public void transform(final Vector inU) {
    inU.broadcast(new LinearStepFunctor());
  }

  @Override
  public void transform(final Vector inU, final Vector inUd) {
    inU.broadcast(new LinearStepFunctor());
    inUd.broadcast(new RectangleFunctor());
  }

  // --- Java Object API support ----------------------------------------------

  @Override
  public boolean equals(final Object inObj) {
    return (inObj instanceof LinearStep);
  }
}
