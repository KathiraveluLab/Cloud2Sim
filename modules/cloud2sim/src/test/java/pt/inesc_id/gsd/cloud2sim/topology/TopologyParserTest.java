/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.topology;

import org.junit.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TopologyParserTest {

    @Test
    public void testParseTopology() throws IOException {
        String json = "{\n" +
                "  \"vms\": [\n" +
                "    { \"id\": 0, \"userId\": 0, \"mips\": 1000, \"pes\": 1, \"ram\": 512, \"bw\": 1000, \"size\": 10000, \"vmm\": \"Xen\" }\n" +
                "  ],\n" +
                "  \"cloudlets\": [\n" +
                "    { \"id\": 0, \"length\": 40000, \"pes\": 1, \"fileSize\": 300, \"outputSize\": 300 }\n" +
                "  ]\n" +
                "}";
        
        File tempFile = File.createTempFile("topology", ".json");
        java.nio.file.Files.write(tempFile.toPath(), json.getBytes());

        TopologyModel.Topology topology = TopologyParser.parse(tempFile.getAbsolutePath());
        
        assertNotNull(topology);
        assertNotNull(topology.vms);
        assertEquals(1, topology.vms.size());
        assertEquals(512, topology.vms.get(0).ram);
        
        assertNotNull(topology.cloudlets);
        assertEquals(1, topology.cloudlets.size());
        assertEquals(40000, topology.cloudlets.get(0).length);

        tempFile.delete();
    }
}
