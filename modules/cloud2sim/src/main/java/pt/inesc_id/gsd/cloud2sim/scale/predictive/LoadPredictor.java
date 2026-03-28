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

import java.util.LinkedList;

/**
 * Utility to maintain recent load history and predict future load based on trends.
 */
public class LoadPredictor {
    private final int windowSize;
    private final LinkedList<Double> loadHistory;

    public LoadPredictor(int windowSize) {
        this.windowSize = windowSize;
        this.loadHistory = new LinkedList<>();
    }

    /**
     * Adds a new load observation.
     * @param load current load (0.0 to 1.0)
     */
    public synchronized void addObservation(double load) {
        if (loadHistory.size() >= windowSize) {
            loadHistory.removeFirst();
        }
        loadHistory.addLast(load);
    }

    /**
     * Predicts the load after a certain number of steps using simple linear regression trend.
     * @param stepsToLookAhead how many intervals into the future to predict
     * @return predicted load
     */
    public synchronized double predictFutureLoad(int stepsToLookAhead) {
        if (loadHistory.size() < 2) {
            return loadHistory.isEmpty() ? 0.0 : loadHistory.getLast();
        }

        // Simple Linear Regression: y = mx + c
        int n = loadHistory.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i;
            double y = loadHistory.get(i);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double denominator = (n * sumX2 - sumX * sumX);
        if (denominator == 0) return loadHistory.getLast();

        double m = (n * sumXY - sumX * sumY) / denominator;
        double c = (sumY - m * sumX) / n;

        // Predict for context index (n - 1 + stepsToLookAhead)
        return m * (n - 1 + stepsToLookAhead) + c;
    }

    public synchronized double getCurrentAverage() {
        if (loadHistory.isEmpty()) return 0.0;
        double sum = 0;
        for (Double d : loadHistory) sum += d;
        return sum / loadHistory.size();
    }
}
