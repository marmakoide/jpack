/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric;

/**
 * An interface for binary functions, working on double values.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-02-08
 */

public interface BinaryFunctor {
  public double getImage(double inU, double inV);
}
