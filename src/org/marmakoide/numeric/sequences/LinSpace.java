/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric.sequences;

import org.marmakoide.numeric.Sequence;

public class LinSpace implements Sequence {
  double mStart;
  double mStep;
  int mSize;
  int mI;

  public LinSpace(final double inStart, final double inStop, final int inSize,
      final boolean inEndPoints) {
    this._setup(inStart, inStop, inSize, inEndPoints);
  }

  public LinSpace(final double inStart, final double inStop, final int inSize) {
    this._setup(inStart, inStop, inSize, true);
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
      final int inSize, final boolean inEndPoints) {
    this.mStart = inStart;
    this.mStep = (inStop - inStart) / (inSize - (inEndPoints ? 1 : 0));
    this.mSize = inSize;
  }
}
