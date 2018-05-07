/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

import org.marmakoide.numeric.Vector;

/**
 * A candidate solution and its fitness.
 * 
 * @see org.marmakoide.es.Strategy
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-08
 */

public final class Point {
  private final Vector mX;
  private final Vector mZ;
  private double mFitness;

  Point(final int inN) {
    this.mX = new Vector(inN);
    this.mZ = new Vector(inN);
  }

  Point(final Vector inX, final Vector inZ) {
    this.mX = inX;
    this.mZ = inZ;
  }

  /**
   * Returns the coordinates of the point in the search space
   * 
   * @return the coordinates of the point
   */
  public final Vector getX() {
    return this.mX;
  }

  /**
   * Returns the coordinates of the point in the sampling space
   * 
   * @return the coordinates of the point
   */
  public final Vector getZ() {
    return this.mZ;
  }

  /**
   * Returns the fitness of the point
   * 
   * @return the fitness of the point
   */
  public final double getFitness() {
    return this.mFitness;
  }

  /**
   * Set the fitness of the point
   * 
   * @param inFitness
   *          the fitness of the point
   */
  public final void setFitness(final double inFitness) {
    this.mFitness = inFitness;
  }

  void copy(final Point inPoint) {
    this.mX.copy(inPoint.mX);
    this.mZ.copy(inPoint.mZ);
    this.mFitness = inPoint.mFitness;
  }
}
