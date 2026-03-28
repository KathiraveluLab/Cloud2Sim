/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.workload;

import org.apache.commons.math3.distribution.WeibullDistribution;

/**
 * Provides inter-arrival times following a Weibull distribution.
 */
public class WeibullDistributionProvider implements DistributionProvider {
    private final WeibullDistribution distribution;

    /**
     * @param alpha shape parameter.
     * @param beta scale parameter.
     */
    public WeibullDistributionProvider(double alpha, double beta) {
        this.distribution = new WeibullDistribution(alpha, beta);
    }

    @Override
    public double getNext() {
        return distribution.sample();
    }
}
