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

import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Injects failures into the simulation cluster.
 */
public class FailureInjector {
    private static final Logger logger = Logger.getLogger(FailureInjector.class.getName());
    private final FailureModel model;
    private final ScheduledExecutorService executor;
    private final ExponentialDistribution interArrivalTime;
    private final Random random = new Random();

    public FailureInjector(FailureModel model) {
        this.model = model;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        // MTBF is the mean of the exponential distribution for failure arrivals
        this.interArrivalTime = new ExponentialDistribution(model.getMtbf());
    }

    /**
     * Starts the failure injection process.
     */
    public void start() {
        logger.info("Starting Failure Injection [Type: " + model.getType() + ", MTBF: " + model.getMtbf() + "]");
        scheduleNextFailure();
    }

    private void scheduleNextFailure() {
        double delayInSeconds = interArrivalTime.sample();
        long delayMillis = (long) (delayInSeconds * 1000);

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    injectFailure();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error during failure injection", e);
                }
                scheduleNextFailure();
            }
        }, delayMillis, TimeUnit.MILLISECONDS);
    }

    private void injectFailure() {
        if (model.getType() == FailureModel.FailureType.NODE_FAILURE) {
            failNode();
        } else if (model.getType() == FailureModel.FailureType.VM_FAILURE) {
            failVm();
        }
    }

    private void failNode() {
        List<HazelcastInstance> instances = HzObjectCollection.getHzObjectCollection().getHazelcastInstances();
        if (instances.size() <= 1) {
            logger.warning("Only one node left. Skipping node failure to prevent simulation crash.");
            return;
        }

        // Don't kill the first instance (master) if possible, or pick randomly
        int index = random.nextInt(instances.size());
        if (index == 0 && instances.size() > 1) {
             index = 1 + random.nextInt(instances.size() - 1);
        }

        HazelcastInstance instance = instances.get(index);
        Log.printLine("CRITICAL: Injecting NODE_FAILURE on instance: " + instance.getName());
        instance.getLifecycleService().terminate();
        // Note: Hazelcast takes some time to detect node loss in the cluster.
    }

    private void failVm() {
        // Logic to mark a VM as failed in the distributed collection
        // This would require a status field in HzVm being monitored
        Log.printLine("Injected VM_FAILURE (Simulated status update)");
    }

    public void stop() {
        executor.shutdownNow();
    }
}
