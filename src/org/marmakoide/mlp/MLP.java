/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp;

import org.marmakoide.numeric.Vector;

/**
 * An helper class to handle MLPs as a single class.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-05-04
 */

public class MLP {
  private final Template mTemplate;
  private ForwardPass mFwdPass;
  private Vector mWeights;

  // --- Constructors ---------------------------------------------------------

  /**
   * Convenience constructor for a MLP.
   * <p>
   * For instance, to create a MLP with 2 inputs, 1 output, and 5 hidden
   * neurons, tanh transfer function, you can do
   * 
   * <pre>
   * {
   *   &#064;code MLP lMLP = new MLP(new Tanh(), 2, 5, 1);
   * }
   * </pre>
   */
  public MLP(final TransferFunction inTFunc, final int... inLayersSize) {
    this.mTemplate = new Template(inTFunc, inLayersSize);
    this.setup();
  }

  /**
   * Convenience constructor for a MLP, with 'tanh' as default transfer
   * function.
   * <p>
   * For instance, to create a MLP with 2 inputs, 1 output, and 5 hidden
   * neurons, you can do
   * 
   * <pre>
   * {
   *   &#064;code MLP lMLP = new MLP(2, 5, 1);
   * }
   * </pre>
   */
  public MLP(final int... inLayersSize) {
    this.mTemplate = new Template(inLayersSize);
    this.setup();
  }

  /**
   * Create a new MLP, using a given template.
   * 
   * @param inTemplate
   *          The template which defines the MLP
   */
  public MLP(final Template inTemplate) {
    this.mTemplate = inTemplate;
    this.setup();
  }

  // --- Accessors ------------------------------------------------------------

  /**
   * Returns the template of the MLP.
   * 
   * @return The template of the MLP.
   */
  public Template getTemplate() {
    return this.mTemplate;
  }

  /**
   * Returns the weights of the MLP as a Vector.
   * 
   * The weights are exposed in a 'flattened' form. The actual meaning of each
   * weight is under the responsability of the Template object.
   * 
   * @return The weights of the MLP as a Vector.
   */
  public Vector getWeights() {
    return this.mWeights;
  }

  // --- Operations -----------------------------------------------------------

  /**
   * Compute the output of a MLP for a given vector.
   * 
   * This method creates a new instance of Vector.
   * 
   * @param inU
   *          the input vector.
   * @return The output of the MLP, as a new Vector instance.
   */
  public Vector transform(final Vector inU) {
    return this.mFwdPass.process(inU);
  }

  /**
   * Compute the output of a MLP for a given vector.
   * 
   * This method does not create a new instance of Vector.
   * 
   * @param inU
   *          the input vector.
   * @param outV
   *          the output vector.
   * @return outV.
   */
  public Vector transform(final Vector inU, final Vector outV) {
    return this.mFwdPass.process(inU, outV);
  }

  // --- Internals ------------------------------------------------------------

  private void setup() {
    this.mWeights = new Vector(this.mTemplate.getNbWeights());
    this.mFwdPass = new ForwardPass(this.mTemplate);
    this.mFwdPass.bindWeights(this.mWeights);
  }
}
