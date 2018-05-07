/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

/**
 * Thrown when the distribution of a Stragegy is not set
 * 
 * @see org.marmakoide.es.Strategy
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-05-03
 */

public class DistributionNotSetException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DistributionNotSetException() {
  }
}
