/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Initiator for Hazelcast-based distributed simulation nodes.
 * This class allows Hazelcast nodes to be started independently of the simulator.
 */
public class HzInitiator {
    public static void main(String[] args) {
        System.out.println("Starting Hazelcast Initiator instance...");
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        System.out.println("Hazelcast Initiator node is up and running: " + hazelcastInstance.getName());
    }
}
