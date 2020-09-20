package com.samujjwaal.hw1.utils;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataCenterUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static Host createHost(LoadHostConfig configHost) {
        final List<Pe> peList = new ArrayList<>(configHost.numberOfPE);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < configHost.numberOfPE; i++) {
            //Uses a PeProvisionerSimple by default to provision PEs for VMs
            peList.add(new PeSimple(configHost.mips));
        }
        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        return new HostSimple(configHost.ram, configHost.bw, configHost.storage, peList);
    }

    public static Datacenter createDatacenter(LoadHostConfig configHost, LoadDataCenterConfig configDatacenter, CloudSim simulation) {
        final List<Host> hostList = new ArrayList<>(configDatacenter.numberOfHosts);
        for(int i = 0; i < configDatacenter.numberOfHosts; i++) {
            Host host = createHost(configHost);
            hostList.add(host);
        }

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        Datacenter dc = new DatacenterSimple(simulation, hostList);
        dc.getCharacteristics().setArchitecture(configDatacenter.arch).setOs(configDatacenter.os);
        return dc;
    }

    public static Vm createVm(LoadVmConfig configVM){
        Vm vm = new VmSimple(configVM.mips,configVM.numberOfPE);
        vm.setRam(configVM.ram).setBw(configVM.bw).setSize(configVM.size);
        return vm;
    }

    public static Cloudlet createCloudlet(LoadCloudletConfig configCloudlet, UtilizationModel utilizationModel){
        Cloudlet cloudlet = new CloudletSimple(configCloudlet.length,configCloudlet.numberOfPE, utilizationModel);
        cloudlet.setSizes(configCloudlet.size);
        return cloudlet;
    }
}
