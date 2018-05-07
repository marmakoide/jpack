/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.numeric;

import java.util.Arrays;
import java.util.Random;

import org.marmakoide.numeric.functors.Add;
import org.marmakoide.numeric.functors.Division;
import org.marmakoide.numeric.functors.InvScale;
import org.marmakoide.numeric.functors.Product;
import org.marmakoide.numeric.functors.Scale;
import org.marmakoide.numeric.functors.Sub;
import org.marmakoide.numeric.reductions.AbsSum;
import org.marmakoide.numeric.reductions.Max;
import org.marmakoide.numeric.reductions.Min;
import org.marmakoide.numeric.reductions.SquareSum;
import org.marmakoide.numeric.reductions.Sum;

/**
 * A row-wise view of a matrix.
 * <p>
 * This object allows to perform row-wise operations on a given matrix. For
 * instance, a call to <code>scaledAdd</code> will have the same effect as
 * calling <code>scaledAdd</code> on each row of the matrix. Using a view just
 * allow to do this with less code and better performances.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-02-08
 */

public final class RowWiseMatrixProxy {
  private final Matrix mMatrix;

  RowWiseMatrixProxy(final Matrix inMatrix) {
    this.mMatrix = inMatrix;
  }

  // --- Arithmetic -----------------------------------------------------------

  public final Matrix fill(final double inValue) {
    return this.mMatrix.fill(inValue);
  }

  public final Matrix fillFromValues(final double... inValues) {
    final int uBegInc = this.mMatrix.getPitch();
    final int uLen = this.mMatrix.getCols();

    final double[] lA = this.mMatrix.getData();

    for (int i = this.mMatrix.getRows(), uBeg = this.mMatrix.getOffset(), v = 0; i != 0; --i, uBeg += uBegInc, ++v) {
      Arrays.fill(lA, uBeg, uBeg + uLen, inValues[v]);
    }

    return this.mMatrix;
  }

  public final Matrix fillAsRandNorm(final Random inRandom) {
    return this.mMatrix.fillAsRandNorm(inRandom);
  }

  // --- Broadcasting ---------------------------------------------------------

  public final Matrix broadcastOnCols(final BinaryFunctor inFunc,
      final Vector inVector) {
    final int uBegInc = this.mMatrix.getPitch();

    final int vBeg = inVector.getOffset();
    final int vInc = inVector.getStride();

    final double[] lA = this.mMatrix.getData();
    final double[] lB = inVector.getData();

    for (int i = this.mMatrix.getCols(), uBeg = this.mMatrix.getOffset(), v = vBeg; i != 0; --i, uBeg += uBegInc, v += vInc) {
      final double lValue = lB[v];
      for (int j = this.mMatrix.getRows(), u = uBeg; j != 0; --j, ++u) {
        lA[u] = inFunc.getImage(lA[u], lValue);
      }
    }

    return this.mMatrix;
  }

  public final Matrix broadcastOnRows(final BinaryFunctor inFunc,
      final Vector inVector) {
    final int uBegInc = this.mMatrix.getPitch();

    final int vBeg = inVector.getOffset();
    final int vInc = inVector.getStride();

    final double[] lA = this.mMatrix.getData();
    final double[] lB = inVector.getData();

    for (int i = this.mMatrix.getCols(), uBeg = this.mMatrix.getOffset(); i != 0; --i, uBeg += uBegInc) {
      for (int j = this.mMatrix.getRows(), u = uBeg, v = vBeg; j != 0; --j, ++u, v += vInc) {
        lA[u] = inFunc.getImage(lA[u], lB[v]);
      }
    }

    return this.mMatrix;
  }

  public final Matrix copy(final Vector inVector) {
    final int uBegInc = this.mMatrix.getPitch();
    final int uLen = this.mMatrix.getCols();

    final int vBeg = inVector.getOffset();
    final int vInc = inVector.getStride();

    final double[] lA = this.mMatrix.getData();
    final double[] lB = inVector.getData();

    for (int i = this.mMatrix.getRows(), uBeg = this.mMatrix.getOffset(), v = vBeg; i != 0; --i, uBeg += uBegInc, v += vInc) {
      Arrays.fill(lA, uBeg, uBeg + uLen, lB[v]);
    }

    return this.mMatrix;
  }

  public final Matrix scaledCopy(final double inValue, final Vector inVector) {
    return this.copy(inVector).broadcast(new Scale(inValue));
  }

  public final Matrix invScaledCopy(final double inValue, final Vector inVector) {
    return this.copy(inVector).broadcast(new InvScale(inValue));
  }

  public final Matrix scale(final double inValue) {
    return this.mMatrix.broadcast(new Scale(inValue));
  }

  public final Matrix scale(final Vector inVector) {
    return this.broadcastOnRows(new Product(), inVector);
  }

  public final Matrix invScale(final double inValue) {
    return this.mMatrix.broadcast(new InvScale(inValue));
  }

  public final Matrix invScale(final Vector inVector) {
    return this.broadcastOnRows(new Division(), inVector);
  }

  public final Matrix add(final Vector inVector) {
    return this.broadcastOnCols(new Add(), inVector);
  }

  public final Matrix sub(final Vector inVector) {
    return this.broadcastOnCols(new Sub(), inVector);
  }

  public final Matrix convolve(final Vector inVector) {
    return this.broadcastOnCols(new Product(), inVector);
  }

  public final Matrix scaledAdd(final double inValue, final Vector inVector) {
    final int uBegInc = this.mMatrix.getPitch();

    final int vBeg = inVector.getOffset();
    final int vInc = inVector.getStride();

    final double[] lA = this.mMatrix.getData();
    final double[] lB = inVector.getData();

    for (int i = this.mMatrix.getCols(), uBeg = this.mMatrix.getOffset(), v = vBeg; i != 0; --i, uBeg += uBegInc, v += vInc) {
      final double lValue = inValue * lB[v];
      for (int j = this.mMatrix.getRows(), u = uBeg; j != 0; --j, ++u) {
        lA[u] += lValue;
      }
    }

    return this.mMatrix;
  }

  // --- Reductions -----------------------------------------------------------

  public final Vector sum() {
    return this.mMatrix.reduce(new Sum(), false);
  }

  public final Vector sum(final Vector outResult) {
    return this.mMatrix.reduce(new Sum(), false, outResult);
  }

  public final Vector squareSum() {
    return this.mMatrix.reduce(new SquareSum(), false);
  }

  public final Vector squareSum(final Vector outResult) {
    return this.mMatrix.reduce(new SquareSum(), false, outResult);
  }

  public final Vector absSum() {
    return this.mMatrix.reduce(new AbsSum(), false);
  }

  public final Vector absSum(final Vector outResult) {
    return this.mMatrix.reduce(new AbsSum(), false, outResult);
  }

  public final Vector min() {
    return this.mMatrix.reduce(new Min(), false);
  }

  public final Vector min(final Vector outResult) {
    return this.mMatrix.reduce(new Min(), false, outResult);
  }

  public final Vector max() {
    return this.mMatrix.reduce(new Max(), false);
  }

  public final Vector max(final Vector outResult) {
    return this.mMatrix.reduce(new Max(), false, outResult);
  }
}
