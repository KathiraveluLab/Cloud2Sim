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

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Provides inter-arrival times following an Exponential distribution
 * (which results in a Poisson process for arrivals).
 */
public class PoissonDistributionProvider implements DistributionProvider {
    private final ExponentialDistribution distribution;

    /**
     * @param lambda average arrival rate (arrivals per unit time).
     */
    public PoissonDistributionProvider(double lambda) {
        // Inter-arrival time for Poisson process with rate lambda is Exponential with mean 1/lambda.
        this.distribution = new ExponentialDistribution(1.0 / lambda);
    }

    @Override
    public double getNext() {
        return distribution.sample();
    }
}
