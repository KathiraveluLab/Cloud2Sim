/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.main.dynamic;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.applications.main.statics.SimulationEngine;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudSim;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.workload.DistributionProvider;
import pt.inesc_id.gsd.cloud2sim.workload.PoissonDistributionProvider;
import pt.inesc_id.gsd.cloud2sim.workload.WeibullDistributionProvider;

import java.util.Calendar;

/**
 * An example simulator that uses Dynamic Workload Generation.
 * This demonstrates how cloudlets arrive over time, triggering adaptive scaling.
 */
public class DynamicSimulator {
    public static void main(String[] args) {
        Log.printLine("Starting Dynamic Workload Simulation...");

        try {
            // 1. Initialize CloudSim
            int numUser = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            HzCloudSim.init(numUser, calendar, traceFlag);

            // 2. Create Datacenters (assumed created by Cloud2SimEngine or manually)
            // For this example, we'll use the static ones defined in the engine.

            // 3. Create Broker
            HzDatacenterBroker broker = SimulationEngine.createBroker("DynamicBroker");
            int userId = broker.getId();

            // 4. Create VMs
            SimulationEngine.createVM(userId);

            // 5. Setup Dynamic Workload
            String distType = "poisson"; // In a real app, read from properties
            double lambda = 0.5;
            DistributionProvider provider;

            if ("weibull".equalsIgnoreCase(distType)) {
                provider = new WeibullDistributionProvider(1.5, 2.0);
            } else {
                provider = new PoissonDistributionProvider(lambda);
            }

            int cloudletCount = 50;
            SimulationEngine.createDynamicWorkload(userId, provider, cloudletCount);

            // 6. Start Simulation
            Cloud2SimEngine.startSimulation();

            // 7. Shutdown (in a real dynamic scenario, this might be handled by heartbeats)
            // For this demo, we'll wait 30 seconds to see arrivals
            Thread.sleep(30000);
            Cloud2SimEngine.stopSimulation();

            Log.printLine("Dynamic Workload Simulation finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }
}
