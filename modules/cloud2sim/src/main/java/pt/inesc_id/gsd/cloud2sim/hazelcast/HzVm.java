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

import org.hibernate.search.annotations.*;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import pt.inesc_id.gsd.cloud2sim.hazelcast.keys.HzVmKey;

/**
 * Extending VM to use in a distributed environment with hazelcast.
 */
@Indexed
public class HzVm extends Vm {
    @DocumentId
    @Override
    public int getId() {
        return super.getId();
    }

    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @Override
    public int getUserId() {
        return super.getUserId();
    }

    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @Override
    public int getRam() {
        return super.getRam();
    }

    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @Override
    public double getMips() {
        return super.getMips();
    }

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    @Override
    public String getVmm() {
        return super.getVmm();
    }
    private HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    public HzVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm,
                CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }

    /**
     * Sets the host that runs this VM.
     *
     * @param host Host running the VM
     * @pre host != $null
     * @post $none
     */
    public void setHost(Host host) {
        if (host != null) {
            super.setHost(host);
            hzObjectCollection.getHostForVm().put(new HzVmKey(this.getId()), host.getId());
        }
    }

    /**
     * Gets the id of the host that the vm is assigned to.
     * @return hostId.
     */
    public int getHostId() {
        int id = -1;
        if (hzObjectCollection.getHostForVm().get(new HzVmKey(getId()))!= null) {
            id = hzObjectCollection.getHostForVm().get(new HzVmKey(getId()));
        }
        return id;
    }
}
