/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.predictive;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Verifies that the LoadPredictor correctly identifies trends and projects future load.
 */
public class PredictiveScalingTest {

    @Test
    public void testLinearTrendPrediction() {
        LoadPredictor predictor = new LoadPredictor(10);
        
        // Feed a clear upward trend: 0.1, 0.2, 0.3, 0.4, 0.5
        for (int i = 1; i <= 5; i++) {
            predictor.addObservation(i * 0.1);
        }

        // Current last observation is 0.5 at index 4.
        // Predict 3 steps ahead (index 7).
        // m = 0.1, c = 0.1 (y = 0.1x + 0.1)
        // at x=7, y = 0.1 * 7 + 0.1 = 0.8
        
        double predictedLoad = predictor.predictFutureLoad(3);
        System.out.println("Predicted Load: " + predictedLoad);

        // Allow some precision margin
        assertEquals(0.8, predictedLoad, 0.01);
    }

    @Test
    public void testDownwardTrendPrediction() {
        LoadPredictor predictor = new LoadPredictor(10);
        
        // Feed a clear downward trend: 0.9, 0.8, 0.7, 0.6
        predictor.addObservation(0.9);
        predictor.addObservation(0.8);
        predictor.addObservation(0.7);
        predictor.addObservation(0.6);

        // Prediction should be lower than 0.6
        double predictedLoad = predictor.predictFutureLoad(2);
        assertTrue("Predicted load should be lower on downward trend", predictedLoad < 0.6);
        assertEquals(0.4, predictedLoad, 0.01);
    }
}
