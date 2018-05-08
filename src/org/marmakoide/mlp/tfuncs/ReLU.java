/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp.tfuncs;

import org.marmakoide.mlp.TransferFunction;
import org.marmakoide.numeric.UnaryFunctor;
import org.marmakoide.numeric.Vector;

/**
 * The ReLU transfer function.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2018-05-08
 */

public final class ReLU implements TransferFunction {
  private final class ReLUFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return Math.max(0.0, inU);
    }
  }

  private final class DerivativeFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return inU < 0.0 ? 0.0 : 1.0;
    }
  }

  @Override
  public void transform(final Vector inU) {
    inU.broadcast(new ReLUFunctor());
  }

  @Override
  public void transform(final Vector inU, final Vector inUd) {
    inU.broadcast(new ReLUFunctor());
    inUd.broadcast(new DerivativeFunctor());
  }

  // --- Java Object API support ----------------------------------------------

  @Override
  public boolean equals(final Object inObj) {
    return (inObj instanceof ReLU);
  }
}
