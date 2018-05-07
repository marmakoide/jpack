/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import org.marmakoide.numeric.Vector;

/**
 * Generates a weight vector to compute the mean of a set of samples.
 * 
 * @see org.marmakoide.es.Strategy
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-08
 */

public interface MeanWeightsGenerator {
  public void generate(Vector outWeights);
}
