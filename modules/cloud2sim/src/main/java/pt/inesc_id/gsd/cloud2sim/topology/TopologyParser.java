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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * TopologyParser loads and parses JSON topology files.
 */
public class TopologyParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static TopologyModel.Topology parse(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), TopologyModel.Topology.class);
    }

    public static TopologyModel.Topology parse(File file) throws IOException {
        return mapper.readValue(file, TopologyModel.Topology.class);
    }
}
