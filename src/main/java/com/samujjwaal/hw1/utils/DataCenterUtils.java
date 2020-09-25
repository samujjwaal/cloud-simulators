package com.samujjwaal.hw1.utils;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
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
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

        logger.info("Datacenter specifications loaded from the configuration file are:");
        logger.info("Number of Hosts: " + configDatacenter.numberOfHosts);
        logger.info("Number of Vms: " + configDatacenter.numberOfVms);
        logger.info("Number of Cloudlets: " + configDatacenter.numberOfCloudlets + "\n");
    }

    public List<Pe> createPeList() {
        final List<Pe> list = new ArrayList<>();
        IntStream.range(0, configHost.numberOfPE)
                .forEach(i -> {
                    list.add(new PeSimple(configHost.mips));
                    logger.info("PE " + i + " added to host");
                });
        return list;
    }

    public Host createHost(boolean activateHost) {
        //List of Host's CPUs (Processing Elements, PEs)
        final List<Pe> peList = createPeList();
        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        return new HostSimple(configHost.ram, configHost.bw, configHost.storage, peList, activateHost);
    }

    public List<Host> createHostList(boolean activateHosts) {
        final List<Host> list = new ArrayList<>();
        logger.info("Adding hosts " + configDatacenter.numberOfHosts + " to the datacenter");
        IntStream.range(0, configDatacenter.numberOfHosts)
                .forEach(i -> {
                    logger.info("Creating host " + i + " and adding PEs ");
                    list.add(createHost(activateHosts));
                    logger.info("Host " + i + " added to datacenter");
                });
        System.out.println();
        return list;
    }

    public Datacenter createDatacenter(CloudSim simulation, VmAllocationPolicy vmPolicy, boolean activateHosts) {
        //List of Datacenter's Hosts
        final List<Host> hostList = createHostList(activateHosts);

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        Datacenter dc = new DatacenterSimple(simulation, hostList, vmPolicy);
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

    public Datacenter createDatacenter(CloudSim simulation, VmAllocationPolicy vmPolicy) {
        return createDatacenter(simulation, vmPolicy, true);
    }

    public Datacenter createDatacenter(CloudSim simulation, boolean activateHosts) {
        return createDatacenter(simulation, new VmAllocationPolicySimple(), activateHosts);
    }

    public Datacenter createDatacenter(CloudSim simulation) {
        return createDatacenter(simulation, true);
    }

    public Vm createVm() {
        // return new vm instance
        return new VmSimple(configVM.mips, configVM.numberOfPE)
                .setRam(configVM.ram)
                .setBw(configVM.bw)
                .setSize(configVM.size);
    }

    public List<Vm> createVmList() {
        final List<Vm> list = new ArrayList<>();
        logger.info("Provisioning " + configDatacenter.numberOfVms + " Vms for allocation to datacenter hosts\n");
        IntStream.range(0, configDatacenter.numberOfVms)
                .forEach(i -> list.add(createVm()));
        return list;
    }

    public Cloudlet createCloudlet(UtilizationModel utilizationModel) {
        // return new cloudlet instance
        return new CloudletSimple(configCloudlet.length, configCloudlet.numberOfPE, utilizationModel)
                .setSizes(configCloudlet.size);
    }

    public Cloudlet createCloudlet() {
        return createCloudlet(new UtilizationModelFull());
    }

    public List<Cloudlet> createCloudletList(UtilizationModel utilizationModel) {
        final List<Cloudlet> list = new ArrayList<>();
        logger.info("Received " + configDatacenter.numberOfCloudlets + " cloudlets for execution in the datacenter\n");
        IntStream.range(0, configDatacenter.numberOfVms)
                .forEach(i -> list.add(createCloudlet(utilizationModel)));
        return list;
    }

    public List<Cloudlet> createCloudletList() {
        return createCloudletList(new UtilizationModelFull());
    }


    public float executionCost(List<Cloudlet> cloudlets) {
        float cost = 0.0f;
        for (Cloudlet cl : cloudlets) {
            cost += cl.getTotalCost();
            logger.info("Cost to execute cloudlet " + cl.getId() + " = " + (float) cl.getTotalCost());
        }
        return cost;
    }
}
