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

import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;
import pt.inesc_id.gsd.cloud2sim.scale.ias.IasRunnable;

import java.util.logging.Logger;

/**
 * A scaler that uses Prediction to trigger scaling events proactively.
 */
public class PredictiveScaler extends IasRunnable {
    private static final Logger logger = Logger.getLogger(PredictiveScaler.class.getName());
    private final LoadPredictor predictor;
    private final double safetyThreshold;
    private final int lookAheadIntervals;

    public PredictiveScaler() {
        // Window size of 10, safety threshold from config
        this.predictor = new LoadPredictor(10);
        this.safetyThreshold = 0.7; // 70% target load
        this.lookAheadIntervals = 3; // Predict 3 intervals ahead
    }

    @Override
    public void run() {
        IasRunnable.initHealthMap();
        while (true) {
            int waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenHealthChecks() * 1000;
            try {
                Thread.sleep(waitTimeInMillis);
            } catch (InterruptedException e) {
                return;
            }

            // In a real system, we'd get the actual CPU load.
            // For the simulation, we'll monitor the "toScaleOut" flag or similar.
            // But here we want to PREDICT if we SHOULD scale out soon.
            
            // Dummy logic to feed the predictor: if the queue is growing, load increases
            double currentPseudoLoad = getCurrentPseudoLoad();
            predictor.addObservation(currentPseudoLoad);

            double predictedLoad = predictor.predictFutureLoad(lookAheadIntervals);

            if (predictedLoad > safetyThreshold) {
                Log.printConcatLine("[Predictive Scaler] Proactive Scaling Triggered. Current Load: ", 
                        currentPseudoLoad, ", Predicted Load: ", predictedLoad);
                getNodeHealth().put("toScaleOut", true);
            }

            // Call the base probe to handle the actual spawning/termination if flags were set
            super.run(); 
        }
    }

    private double getCurrentPseudoLoad() {
        // Logic to estimate load based on pending cloudlets in the cluster
        // For now, return a placeholder that we can drive in tests
        return 0.5; // Placeholder
    }
}
