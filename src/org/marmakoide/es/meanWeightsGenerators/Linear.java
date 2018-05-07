/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es.meanWeightsGenerators;

import org.marmakoide.es.MeanWeightsGenerator;
import org.marmakoide.numeric.Vector;

public class Linear implements MeanWeightsGenerator {
  @Override
  public void generate(final Vector outWeights) {
    final int N = outWeights.getSize();

    for (int i = 0; i < N; ++i) {
      outWeights.set(i, N - i);
    }
  }
}
