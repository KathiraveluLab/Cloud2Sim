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
 * A partition-aware key for Cloudlets.
 * Uses the vmId as the partition key to ensure co-location.
 */
public class HzCloudletKey implements Serializable, PartitionAware<Integer> {
    private final int cloudletId;
    private final int vmId;

    public HzCloudletKey(int cloudletId, int vmId) {
        this.cloudletId = cloudletId;
        this.vmId = vmId;
    }

    public int getCloudletId() {
        return cloudletId;
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
        HzCloudletKey that = (HzCloudletKey) o;
        return cloudletId == that.cloudletId;
    }

    @Override
    public int hashCode() {
        return cloudletId;
    }

    @Override
    public String toString() {
        return "HzCloudletKey{cloudletId=" + cloudletId + ", vmId=" + vmId + "}";
    }
}
