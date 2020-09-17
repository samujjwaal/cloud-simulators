package com.samujjwaal.hw1.utils;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataCenterUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static Host createHost(LoadHostConfig host) {
        final List<Pe> peList = new ArrayList<>(host.numberOfPE);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < host.numberOfPE; i++) {
            //Uses a PeProvisionerSimple by default to provision PEs for VMs
            peList.add(new PeSimple(host.mips));
        }
        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        return new HostSimple(host.ram, host.bw, host.storage, peList);
    }

    public static Datacenter createDatacenter(LoadHostConfig hosts, LoadDataCenterConfig datacenter, CloudSim simulation) {
        final List<Host> hostList = new ArrayList<>(datacenter.numberOfHosts);
        for(int i = 0; i < datacenter.numberOfHosts; i++) {
            Host host = createHost(hosts);
            hostList.add(host);
        }

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        Datacenter dc = new DatacenterSimple(simulation, hostList);
        dc.getCharacteristics().setArchitecture(datacenter.arch).setOs(datacenter.os);
        return dc;
    }
}
