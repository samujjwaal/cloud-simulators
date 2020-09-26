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
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
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
        List<Pe> list = new ArrayList<>();
        IntStream.range(0, configHost.numberOfPE)
                .forEach(i -> {
                    list.add(new PeSimple(configHost.mips));
                    logger.info("PE " + i + " added to host");
                });
        return list;
    }


    public Host createHost(boolean activateHost, VmScheduler vmScheduler) throws IllegalAccessException, InstantiationException {
        //List of Host's CPUs (Processing Elements, PEs)
        List<Pe> peList = createPeList();
        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        // Need to create a new instance of VmScheduler for each host. Each host must use is own instance of a VmScheduler
        VmScheduler scheduler = vmScheduler.getClass().newInstance();

        return new HostSimple(configHost.ram, configHost.bw, configHost.storage, peList, activateHost)
                .setVmScheduler(scheduler);
    }

    public List<Host> createHostList(boolean activateHosts, VmScheduler vmScheduler) {
        List<Host> list = new ArrayList<>();
        logger.info("Adding " + configDatacenter.numberOfHosts + " hosts to the datacenter");
        IntStream.range(0, configDatacenter.numberOfHosts)
                .forEach(i -> {
                    logger.info("Creating host " + i + " and adding PEs ");
                    try {
                        list.add(createHost(activateHosts, vmScheduler));
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    logger.info("Host " + i + " added to datacenter");
                });
        System.out.println();
        return list;
    }

    public Datacenter createDatacenter(CloudSim simulation, VmAllocationPolicy vmAllocPolicy,VmScheduler vmScheduler, boolean activateHosts) {
        //List of Datacenter's Hosts
        List<Host> hostList = createHostList(activateHosts,vmScheduler);

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        Datacenter dc = new DatacenterSimple(simulation, hostList, vmAllocPolicy);
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

    public Datacenter createDatacenter(CloudSim simulation, VmAllocationPolicy vmAllocPolicy, boolean activateHosts) {
        return createDatacenter(simulation, vmAllocPolicy, new VmSchedulerSpaceShared(),activateHosts);
    }

    public Datacenter createDatacenter(CloudSim simulation, VmAllocationPolicy vmAllocPolicy) {
        return createDatacenter(simulation, vmAllocPolicy, new VmSchedulerSpaceShared(), true);
    }

    public Datacenter createDatacenter(CloudSim simulation, VmScheduler vmScheduler) {
        return createDatacenter(simulation, new VmAllocationPolicySimple(), vmScheduler, true);
    }

    public Datacenter createDatacenter(CloudSim simulation, boolean activateHosts) {
        return createDatacenter(simulation, new VmAllocationPolicySimple(), new VmSchedulerSpaceShared(),activateHosts);
    }

    public Datacenter createDatacenter(CloudSim simulation) {
        return createDatacenter(simulation, true);
    }

    public Vm createVm(CloudletScheduler cloudletScheduler) {
        // return new vm instance
        return new VmSimple(configVM.mips, configVM.numberOfPE, cloudletScheduler)
                .setRam(configVM.ram)
                .setBw(configVM.bw)
                .setSize(configVM.size);
    }

    public List<Vm> createVmList(CloudletScheduler cloudletScheduler) {
        List<Vm> list = new ArrayList<>();
        logger.info("Provisioning " + configDatacenter.numberOfVms + " Vms for allocation to datacenter hosts\n");
        IntStream.range(0, configDatacenter.numberOfVms)
                .forEach(i -> list.add(createVm(cloudletScheduler)));
        return list;
    }

    public List<Vm> createVmList(){
        return createVmList(new CloudletSchedulerTimeShared());
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
        List<Cloudlet> list = new ArrayList<>();
        logger.info("Received " + configDatacenter.numberOfCloudlets + " cloudlets for execution in the datacenter\n");
        IntStream.range(0, configDatacenter.numberOfCloudlets)
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
