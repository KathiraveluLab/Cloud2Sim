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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModelFull;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.hazelcast.keys.HzCloudletKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates and submits cloudlets to the distributed collection based on a distribution.
 */
public class WorkloadGenerator {
    private static final Logger logger = Logger.getLogger(WorkloadGenerator.class.getName());
    private final DistributionProvider provider;
    private final ScheduledExecutorService executor;
    private final HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    private int cloudletIdCounter = 10000; // Start high to avoid collision

    public WorkloadGenerator(DistributionProvider provider) {
        this.provider = provider;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts generating a fixed number of cloudlets at dynamic intervals.
     * @param userId owner of the cloudlets
     * @param totalCount total number of cloudlets to generate
     */
    public void start(final int userId, final int totalCount) {
        logger.info("Starting dynamic workload generation for " + totalCount + " cloudlets...");
        scheduleNext(userId, 0, totalCount);
    }

    private void scheduleNext(final int userId, final int currentCount, final int totalCount) {
        if (currentCount >= totalCount) {
            logger.info("Dynamic workload generation completed.");
            executor.shutdown();
            return;
        }

        double delayInSeconds = provider.getNext();
        // Convert simulation-time delay to real-time (1 simulation second = 1 real second for demo purposes)
        // In a real simulator, this would be scaled or driven by the simulation clock.
        long delayMillis = (long) (delayInSeconds * 1000);

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                submitCloudlet(userId);
                scheduleNext(userId, currentCount + 1, totalCount);
            }
        }, delayMillis, TimeUnit.MILLISECONDS);
    }

    private void submitCloudlet(int userId) {
        int id = cloudletIdCounter++;
        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;

        HzCloudlet cloudlet = new HzCloudlet(id, length, pesNumber, fileSize, outputSize,
                new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
        cloudlet.setUserId(userId);

        logger.log(Level.INFO, "Submitting Dynamic Cloudlet ID: {0}", id);
        hzObjectCollection.getUserCloudletList().put(new HzCloudletKey(id, -1), cloudlet);
    }

    public void stop() {
        executor.shutdownNow();
    }
}
