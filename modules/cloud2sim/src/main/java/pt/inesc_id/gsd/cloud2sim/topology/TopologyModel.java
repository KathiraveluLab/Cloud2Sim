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

import java.util.List;

/**
 * POJO models for mapping JSON topology descriptions.
 */
public class TopologyModel {

    public static class Topology {
        public List<DatacenterModel> datacenters;
        public List<VmModel> vms;
        public List<CloudletModel> cloudlets;
    }

    public static class DatacenterModel {
        public String name;
        public String arch;
        public String os;
        public String vmm;
        public double timeZone;
        public double cost;
        public double costPerMem;
        public double costPerStorage;
        public double costPerBw;
        public List<HostModel> hosts;
    }

    public static class HostModel {
        public int id;
        public int ram;
        public long storage;
        public int bw;
        public int mips;
        public int pes;
    }

    public static class VmModel {
        public int id;
        public int userId;
        public double mips;
        public int pes;
        public int ram;
        public long bw;
        public long size;
        public String vmm;
    }

    public static class CloudletModel {
        public int id;
        public long length;
        public int pes;
        public long fileSize;
        public long outputSize;
    }
}
