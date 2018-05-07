/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric.functors;

import org.marmakoide.numeric.BinaryFunctor;

/**
 * The scaled sum function as an arity 2 functor.
 * <p>
 * This function computes <code>u + k * v</code>, where k is a constant.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-02-08
 */

public final class ScaledAdd implements BinaryFunctor {
  private final double mScale;

  public ScaledAdd(final double inScale) {
    this.mScale = inScale;
  }

  @Override
  public double getImage(final double inU, final double inV) {
    return inU + (this.mScale * inV);
  }
}
