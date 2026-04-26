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

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Partition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.cloudbus.cloudsim.compatibility.hazelcast.keys.HzCloudletKey;
import org.cloudbus.cloudsim.compatibility.hazelcast.keys.HzVmKey;

import static org.junit.Assert.assertEquals;

/**
 * Verifies that VMs and their associated Cloudlets map to the same partition.
 */
public class PartitionLocalityTest {
    private static HazelcastInstance hz;

    @BeforeClass
    public static void setup() {
        Config config = new Config();
        hz = Hazelcast.newHazelcastInstance(config);
    }

    @AfterClass
    public static void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void testCoLocation() {
        int vmId = 42;
        int cloudletId = 101;

        HzVmKey vmKey = new HzVmKey(vmId);
        HzCloudletKey cloudletKey = new HzCloudletKey(cloudletId, vmId);

        Partition vmPartition = hz.getPartitionService().getPartition(vmKey);
        Partition cloudletPartition = hz.getPartitionService().getPartition(cloudletKey);

        System.out.println("VM Partition: " + vmPartition.getPartitionId());
        System.out.println("Cloudlet Partition: " + cloudletPartition.getPartitionId());

        assertEquals("VM and Cloudlet should be in the same partition",
                vmPartition.getPartitionId(), cloudletPartition.getPartitionId());
    }
}
