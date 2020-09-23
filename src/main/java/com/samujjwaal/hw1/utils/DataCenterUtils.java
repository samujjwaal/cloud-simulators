package com.samujjwaal.hw1.utils;

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
    //Define a static logger variable so that it references the Logger instance
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final LoadHostConfig configHost;
    private final LoadDataCenterConfig configDatacenter;
    private final LoadVmConfig configVM;
    private final LoadCloudletConfig configCloudlet;

    public DataCenterUtils(LoadHostConfig configHost, LoadDataCenterConfig configDatacenter, LoadVmConfig configVM, LoadCloudletConfig configCloudlet) {
        this.configHost = configHost;
        this.configDatacenter = configDatacenter;
        this.configVM = configVM;
        this.configCloudlet = configCloudlet;
    }

    public Host createHost() {
        final List<Pe> peList = new ArrayList<>(configHost.numberOfPE);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < configHost.numberOfPE; i++) {
            //Uses a PeProvisionerSimple by default to provision PEs for VMs
            peList.add(new PeSimple(configHost.mips));
            logger.info("PE "+i+" added to host");
        }
        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        return new HostSimple(configHost.ram, configHost.bw, configHost.storage, peList);
    }

    public Datacenter createDatacenter(CloudSim simulation) {
        //List of Datacenter's Hosts
        final List<Host> hostList = new ArrayList<>(configDatacenter.numberOfHosts);
        for(int i = 0; i < configDatacenter.numberOfHosts; i++) {
            logger.info("Creating host "+i + " and adding PEs ");
            Host host = createHost();
            hostList.add(host);
            logger.info("Host "+i+" added to datacenter");
        }

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        Datacenter dc = new DatacenterSimple(simulation, hostList);
        // set characteristics of the datacenter
        dc.getCharacteristics()
                .setArchitecture(configDatacenter.arch)
                .setOs(configDatacenter.os)
                .setVmm(configDatacenter.vmm)
                .setCostPerSecond(configDatacenter.costPerSecond)
                .setCostPerMem(configDatacenter.costPerMem)
                .setCostPerStorage(configDatacenter.costPerStorage)
                .setCostPerBw(configDatacenter.costPerBw);
        return dc;
    }

    public Vm createVm(){
        // return new vm instance
        return new VmSimple(configVM.mips,configVM.numberOfPE)
                .setRam(configVM.ram)
                .setBw(configVM.bw)
                .setSize(configVM.size);
    }

    public Cloudlet createCloudlet(UtilizationModel utilizationModel){
        // return new cloudlet instance
        return new CloudletSimple(configCloudlet.length,configCloudlet.numberOfPE, utilizationModel)
                .setSizes(configCloudlet.size);
    }

    public float executionCost(List<Cloudlet> cloudlets){
        float cost = 0.0f;
        for (Cloudlet cl : cloudlets){
            cost += cl.getTotalCost();
            logger.info("Cost to execute cloudlet "+cl.getId()+" = "+(float)cl.getTotalCost());
        }
        return cost;
    }
}
