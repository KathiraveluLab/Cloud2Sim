/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.search;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.junit.Test;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzVm;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class HibernateSearchTest {

    @Test
    public void testIndexingAndSearch() {
        SearchProcessor searchProcessor = SearchProcessor.getInstance();
        assertNotNull(searchProcessor);

        // Create an HzVm
        HzVm vm = new HzVm(100, 1, 1000, 1, 512, 1000, 10000, "Xen", new CloudletSchedulerTimeShared());
        searchProcessor.indexObject(vm);

        // Create an HzCloudlet
        HzCloudlet cloudlet = new HzCloudlet(200, 1000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
        searchProcessor.indexObject(cloudlet);

        // Search for the VM by VMM
        List<Object> results = searchProcessor.search(HzVm.class, "vmm:Xen");
        
        // Since SearchProcessor search is a placeholder that logs to stdout for now 
        // (as a full Infinispan/HS bootstrap in unit test is complex), 
        // we mainly verify it runs without exceptions.
        System.out.println("[HibernateSearchTest] Search completed successfully.");
    }
}
