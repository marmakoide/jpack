/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp.tfuncs;

import org.marmakoide.mlp.TransferFunction;
import org.marmakoide.numeric.UnaryFunctor;
import org.marmakoide.numeric.Vector;
import org.marmakoide.numeric.VectorMath;

/**
 * The tanh transfer function.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-05-07
 */

public final class Tanh implements TransferFunction {
  private final class DerivativeFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return 1.0 - (inU * inU);
    }
  }

  @Override
  public void transform(final Vector inU) {
    VectorMath.tanh(inU);
  }

  @Override
  public void transform(final Vector inU, final Vector inUd) {
    VectorMath.tanh(inU);
    inUd.broadcastedCopy(new DerivativeFunctor(), inU);
  }

  // --- Java Object API support ----------------------------------------------

  @Override
  public boolean equals(final Object inObj) {
    return (inObj instanceof Tanh);
  }
}
