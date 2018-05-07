/* Copyright (c) 2012-2018 Alexandre Devert
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.mlp;

import java.util.Arrays;

import org.marmakoide.mlp.tfuncs.Tanh;

/**
 * Represents a MLP definition.
 * 
 * @author Alexandre Devert
 * @version 1.0
 * @since 2012-05-04
 */

public class Template {
  private final static TransferFunction DEFAULT_TFUNC = new Tanh();

  private final int[] mLayersSize; // Hold the size of all layers : in.,
                                   // hidden, out.
  private final TransferFunction mTFunc;

  // --- Constructors ---------------------------------------------------------

  /**
   * Generic constructor for a MLP definition
   * 
   * @param inTFunc
   *          transfer function of the represented MLP
   * @param inNbInputs
   *          number of inputs of the represented MLP
   * @param inLayersSize
   *          size of each hidden layers
   * @param inNbOutputs
   *          number of outputs of the represented MLP
   */
  public Template(final TransferFunction inTFunc, final int inNbInputs,
      final int[] inLayersSize, final int inNbOutputs) {
    this.mLayersSize = new int[inLayersSize.length + 2];
    this.mLayersSize[0] = inNbInputs;
    this.mLayersSize[this.mLayersSize.length - 1] = inNbOutputs;
    System
        .arraycopy(inLayersSize, 0, this.mLayersSize, 1, inLayersSize.length);
    this.mTFunc = inTFunc;
    this.sanityCheck();
  }

  /**
   * Generic constructor for a MLP definition, with 'tanh' as default transfer
   * function.
   * 
   * @param inNbInputs
   *          number of inputs of the represented MLP
   * @param inLayersSize
   *          size of each hidden layers
   * @param inNbOutputs
   *          number of outputs of the represented MLP
   */
  public Template(final int inNbInputs, final int[] inLayersSize,
      final int inNbOutputs) {
    this(Template.DEFAULT_TFUNC, inNbInputs, inLayersSize, inNbOutputs);
  }

  /**
   * Convenience constructor for a MLP definition
   * <p>
   * For instance, to define a MLP with 2 inputs, 1 output, and 5 hidden
   * neurons, you can do
   * 
   * <pre>
   * {
   *   &#064;code Template lMLPTmpl = new Template(new Tanh(), 2, 5, 1);
   * }
   * </pre>
   * 
   * @param inTFunc
   *          transfer function of the represented MLP
   * @param inLayersSize
   *          size of each layer. The first layer is considered as as the input
   *          layer, while the last layer is considedred as the output layer.
   */
  public Template(final TransferFunction inTFunc, final int... inLayersSize) {
    if (inLayersSize.length < 2) {
      throw new IllegalArgumentException("At least 2 size should be given");
    }
    this.mLayersSize = Arrays.copyOf(inLayersSize, inLayersSize.length);
    this.mTFunc = inTFunc;
    this.sanityCheck();
  }

  /**
   * Convenience constructor for a MLP definition, with 'tanh' as default
   * transfer function.
   * <p>
   * For instance, to define a MLP with 2 inputs, 1 output, and 5 hidden
   * neurons, you can do
   * 
   * <pre>
   * {
   *   &#064;code Template lMLPTmpl = new Template(2, 5, 1);
   * }
   * </pre>
   * 
   * @param inLayersSize
   *          size of each layer. The first layer is considered as as the input
   *          layer, while the last layer is considedred as the output layer.
   */
  public Template(final int... inLayersSize) {
    this(Template.DEFAULT_TFUNC, inLayersSize);
  }

  /**
   * Copy contructor
   * 
   * @param inTemplate
   *          The template to copy
   */
  public Template(final Template inTemplate) {
    this.mTFunc = inTemplate.mTFunc;
    this.mLayersSize = inTemplate.mLayersSize;
  }

  // --- Accessors ------------------------------------------------------------

  /**
   * Returns the number of inputs of the represented MLP
   * 
   * @return The number of inputs of the represented MLP
   */
  public int getNbInputs() {
    return this.mLayersSize[0];
  }

  /**
   * Returns the number of outputs of the represented MLP
   * 
   * @return The number of outputs of the represented MLP
   */
  public int getNbOutputs() {
    return this.mLayersSize[this.mLayersSize.length - 1];
  }

  /**
   * Returns the size of the i-th hidden layer of the represented MLP
   * 
   * @param i
   *          the index of a hidden layer
   * @return The size of the i-th hidden layer of the represented MLP
   */
  public int getLayerSize(final int i) {
    if ((i < 0) || (i >= (this.mLayersSize.length - 1))) {
      throw new IndexOutOfBoundsException();
    }

    return this.mLayersSize[i + 1];
  }

  /**
   * Returns the number of hidden layers of the represented MLP
   * 
   * @return The number of hidden layers of the represented MLP
   */
  public int getNbLayers() {
    return this.mLayersSize.length - 2;
  }

  /**
   * Returns the transfer function for the represented MLP
   * 
   * @return The transfer function for the represented MLP
   */
  public TransferFunction getTransferFunction() {
    return this.mTFunc;
  }

  /**
   * Computes the number of weights of the represented MLP
   * 
   * @return The number of weights of the represented MLP
   */
  public int getNbWeights() {
    int lSum = -this.mLayersSize[0];
    int lPrevSize = 0;

    for (final int lSize : this.mLayersSize) {
      lSum += (lPrevSize + 1) * lSize;
      lPrevSize = lSize;
    }

    return lSum;
  }

  // --- Internals ------------------------------------------------------------

  private void sanityCheck() {
    if (this.mTFunc == null) {
      throw new NullPointerException(
          "Null reference passed as transfer function");
    }

    for (final int lSize : this.mLayersSize) {
      if (lSize < 1) {
        throw new IllegalArgumentException(
            "A layer should have a size of 1 at least");
      }
    }
  }

  // --- Java Object API support ----------------------------------------------

  @Override
  public boolean equals(final Object inObj) {
    // Casting
    if (!(inObj instanceof Template)) {
      return false;
    }

    final Template inTemplate = (Template) inObj;

    // Test equality
    return (this.mTFunc.equals(inTemplate.mTFunc))
        && Arrays.equals(this.mLayersSize, inTemplate.mLayersSize);
  }
}
