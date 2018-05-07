/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es.meanWeightsGenerators;

import org.marmakoide.es.MeanWeightsGenerator;
import org.marmakoide.numeric.Vector;

public class Equal implements MeanWeightsGenerator {
  @Override
  public void generate(final Vector outWeights) {
    outWeights.fill(1.0);
  }
}
