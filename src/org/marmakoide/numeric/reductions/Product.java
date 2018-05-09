/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric.reductions;

import org.marmakoide.numeric.Reduction;

public final class Product implements Reduction {
  @Override
  public double init(final double inValue) {
    return inValue;
  }

  @Override
  public double accumulate(final double inAcc, final double inValue) {
    return inAcc * inValue;
  }
}
