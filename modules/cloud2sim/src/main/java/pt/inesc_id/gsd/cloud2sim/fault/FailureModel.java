/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.fault;

/**
 * Defines the parameters for a failure injection scenario.
 */
public class FailureModel {
    public enum FailureType {
        NODE_FAILURE,
        VM_FAILURE
    }

    private final FailureType type;
    private final double mtbf; // Mean Time Between Failures (in simulation seconds)

    public FailureModel(FailureType type, double mtbf) {
        this.type = type;
        this.mtbf = mtbf;
    }

    public FailureType getType() {
        return type;
    }

    public double getMtbf() {
        return mtbf;
    }
}
