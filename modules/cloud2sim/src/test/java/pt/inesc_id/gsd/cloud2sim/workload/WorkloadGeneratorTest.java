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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic validation for distribution providers.
 */
public class WorkloadGeneratorTest {

    @Test
    public void testPoissonArrivals() {
        double lambda = 10.0; // 10 arrivals per second
        PoissonDistributionProvider provider = new PoissonDistributionProvider(lambda);

        double sum = 0;
        int samples = 1000;
        for (int i = 0; i < samples; i++) {
            sum += provider.getNext();
        }

        double averageInterArrivalTime = sum / samples;
        double expectedMean = 1.0 / lambda;

        System.out.println("Expected Mean: " + expectedMean);
        System.out.println("Actual Mean: " + averageInterArrivalTime);

        // Allow 10% margin for randomness
        assertEquals(expectedMean, averageInterArrivalTime, expectedMean * 0.1);
    }
}
