/* Copyright (c) 2012 Alexandre Devert <marmakoide@yahoo.fr>
 * 
 * JPack is free software; you can redistribute it and/or modify it under the
 * terms of the MIT license. See LICENSE for details. */

package org.marmakoide.es.distributions;

import org.marmakoide.es.Strategy;

/*Common constants for the different CMA-ES flavours. */

public final class CMAConstants {
  private final double muW;
  private final double cSigma;
  private final double dSigma;
  private final double cc;
  private final double c1;
  private final double cMu;
  private final double chiN; // Expectation of ||N(0,I)|| == norm(randn(N,1))

  private static final double sqr(final double inX) {
    return inX * inX;
  }

  public CMAConstants(final Strategy inStrategy, final boolean inSeparable) {
    final double N = inStrategy.getN();

    this.chiN = Math.sqrt(N)
        * ((1.0 - (1.0 / (4.0 * N))) + (1.0 / (21.0 * N * N)));
    this.muW = 1.0 / inStrategy.getMeanWeights().squareSum();
    this.cSigma = (this.muW + 2.0) / (this.muW + N + 5.0);
    this.dSigma = 1.0
        + (2.0 * Math.max(0.0, Math.sqrt((this.muW - 1.0) / (N + 1.0)) - 1.0))
        + this.cSigma;
    this.cc = (4.0 + (this.muW / N)) / (N + 4.0 + ((2.0 * this.muW) / N));

    final double c1Tmp = 2.0 / (CMAConstants.sqr(N + 1.3) + this.muW);
    final double cMuTmp = (2.0 * ((this.muW - 2.0) + (1.0 / this.muW)))
        / (CMAConstants.sqr(N + 2.0) + this.muW);

    if (inSeparable) {
      this.cMu = cMuTmp;
      this.c1 = c1Tmp;
    } else {
      final double k = (N + 1.5) / 3.0;
      this.cMu = Math.min(1.0 - c1Tmp, k * cMuTmp);
      this.c1 = Math.min(1.0, k * c1Tmp);
    }
  }

  // --- Accessors ------------------------------------------------------------

  public final double muW() {
    return this.muW;
  }

  public final double cSigma() {
    return this.cSigma;
  }

  public final double dSigma() {
    return this.dSigma;
  }

  public final double cc() {
    return this.cc;
  }

  public final double c1() {
    return this.c1;
  }

  public final double cMu() {
    return this.cMu;
  }

  public final double chiN() {
    return this.chiN;
  }
}
