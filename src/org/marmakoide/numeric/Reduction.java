/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric;

public interface Reduction {
  public double init(double inValue);

  public double accumulate(double inAcc, double inValue);
}
