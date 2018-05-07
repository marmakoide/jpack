/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp.tfuncs;

import org.marmakoide.mlp.TransferFunction;
import org.marmakoide.numeric.UnaryFunctor;
import org.marmakoide.numeric.Vector;

/**
 * The sigmoid transfer function.
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-05-07
 */

public final class Sigmoid implements TransferFunction {
  private final class SigmoidFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return 1.0 / (1.0 + Math.exp(-inU));
    }
  }

  private final class DerivativeFunctor implements UnaryFunctor {
    @Override
    public double getImage(final double inU) {
      return inU * (1.0 - inU);
    }
  }

  @Override
  public void transform(final Vector inU) {
    inU.broadcast(new SigmoidFunctor());
  }

  @Override
  public void transform(final Vector inU, final Vector inUd) {
    inU.broadcast(new SigmoidFunctor());
    inUd.broadcastedCopy(new DerivativeFunctor(), inU);
  }

  // --- Java Object API support ----------------------------------------------

  @Override
  public boolean equals(final Object inObj) {
    return (inObj instanceof Sigmoid);
  }
}
