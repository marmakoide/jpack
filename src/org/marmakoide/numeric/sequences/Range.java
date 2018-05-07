/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric.sequences;

import org.marmakoide.numeric.Sequence;

public class Range implements Sequence {
  double mStart;
  double mStep;
  int mSize;
  int mI;

  public Range(final double inStop) {
    this._setup(0.0, inStop, 0.0 < inStop ? 1.0 : -1.0);
  }

  public Range(final double inStart, final double inStop) {
    this._setup(inStart, inStop, inStart < inStop ? 1.0 : -1.0);
  }

  public Range(final double inStart, final double inStop, final double inStep) {
    this._setup(inStart, inStop, inStep);
  }

  @Override
  public int getSize() {
    return this.mSize;
  }

  @Override
  public void init() {
    this.mI = 0;
  }

  @Override
  public double next() {
    final double lRet = this.mStart + (this.mStep * this.mI);
    this.mI += 1;
    return lRet;
  }

  private void _setup(final double inStart, final double inStop,
      final double inStep) {
    this.mStart = inStart;
    this.mStep = inStep;
    this.mSize = (int) Math.floor((inStop - inStart) / inStep);
  }
}
