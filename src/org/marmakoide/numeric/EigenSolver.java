/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric;

public final class EigenSolver {
  private final Vector mTmp;

  public EigenSolver(final int inN) {
    this.mTmp = new Vector(inN);
  }

  public final void solve(final Matrix inM, final Matrix outVectors,
      final Vector outValues) {
    outVectors.copy(inM);
    MatrixAlgos.Householder(outVectors, outValues, this.mTmp);
    MatrixAlgos.QL(outVectors, outValues, this.mTmp);
  }
}
