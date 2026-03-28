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

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Verifies that the FailureInjector correctly terminates Hazelcast nodes.
 */
public class FailureInjectionTest {
    private HazelcastInstance master;
    private HazelcastInstance worker;

    @Before
    public void setup() {
        Config config = new Config();
        master = Hazelcast.newHazelcastInstance(config);
        worker = Hazelcast.newHazelcastInstance(config);
        HzObjectCollection.getHzObjectCollection().getHazelcastInstances().add(master);
        HzObjectCollection.getHzObjectCollection().getHazelcastInstances().add(worker);
    }

    @After
    public void tearDown() {
        Hazelcast.shutdownAll();
        HzObjectCollection.getHzObjectCollection().getHazelcastInstances().clear();
    }

    @Test
    public void testNodeFailureInjection() throws InterruptedException {
        FailureModel model = new FailureModel(FailureModel.FailureType.NODE_FAILURE, 1.0); // 1s MTBF
        FailureInjector injector = new FailureInjector(model);
        
        int initialSize = Hazelcast.getAllHazelcastInstances().size();
        assertTrue("Should have 2 instances", initialSize >= 2);

        injector.start();

        // Wait for failure to be injected (MTBF is 1s, so wait longer)
        Thread.sleep(3000);

        int finalSize = Hazelcast.getAllHazelcastInstances().size();
        System.out.println("Initial nodes: " + initialSize + ", Final nodes: " + finalSize);

        assertTrue("Number of nodes should have decreased after failure injection", finalSize < initialSize);
        
        injector.stop();
    }
}
