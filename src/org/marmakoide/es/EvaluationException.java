/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es;

/**
 * Thrown when the fitness for a given candidate solution can not be computed
 * 
 * @see org.marmakoide.es.Evaluator
 * 
 * @author Alexandre Devert <marmakoide@yahoo.fr>
 * @version 1.0
 * @since 2012-02-08
 */

public class EvaluationException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public EvaluationException() {
  }
}
