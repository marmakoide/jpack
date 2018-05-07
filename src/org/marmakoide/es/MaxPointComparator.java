/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import java.util.Comparator;

class MaxPointComparator implements Comparator<Point> {
  @Override
  public int compare(final Point inA, final Point inB) {
    return inA.getFitness() < inB.getFitness() ? 1 : -1;
  }

  @Override
  public boolean equals(final Object inObj) {
    return inObj instanceof MaxPointComparator;
  }
}
