/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp;

import org.marmakoide.numeric.Vector;

/**
 * An interface for neuron transfer functions.
 * 
 * A transfer function operate on vectors rather than single values for
 * performance purposes
 * <ul>
 * <li>Many common transfer function have a computationaly cheap derivative if
 * if we want in the same time the value of the function for a given input.
 * Returning 2 values at once in Java is not possible, passing by reference
 * natives types is not possible either. By using vectors as input, call by
 * reference is possible.
 * <li>Amortize the cost of repeatingly calling the <code>transform</code>
 * </ul>
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-05-04
 */

public interface TransferFunction {
  /**
   * Transforms the value hold in the input vector
   * 
   * @param inU
   *          the vector of values to transform
   */
  public void transform(Vector inU);

  /**
   * Transforms the value hold in the input vector and provide derivatives in a
   * separate vector
   * 
   * @param inU
   *          the vector of values to transform
   * @param inUd
   *          the vector receiving the derivative of the transform
   */
  public void transform(Vector inU, Vector inUd);
}
