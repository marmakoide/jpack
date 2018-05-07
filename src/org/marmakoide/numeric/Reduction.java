/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric;

public interface Reduction {
  public double init(double inValue);

  public double accumulate(double inAcc, double inValue);
}
