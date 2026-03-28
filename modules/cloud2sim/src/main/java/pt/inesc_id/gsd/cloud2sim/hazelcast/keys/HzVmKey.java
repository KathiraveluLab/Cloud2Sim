/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast.keys;

import com.hazelcast.core.PartitionAware;
import java.io.Serializable;

/**
 * A partition-aware key for VMs.
 */
public class HzVmKey implements Serializable, PartitionAware<Integer> {
    private final int vmId;

    public HzVmKey(int vmId) {
        this.vmId = vmId;
    }

    public int getVmId() {
        return vmId;
    }

    @Override
    public Integer getPartitionKey() {
        return vmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HzVmKey hzVmKey = (HzVmKey) o;
        return vmId == hzVmKey.vmId;
    }

    @Override
    public int hashCode() {
        return vmId;
    }

    @Override
    public String toString() {
        return "HzVmKey{vmId=" + vmId + "}";
    }
}
