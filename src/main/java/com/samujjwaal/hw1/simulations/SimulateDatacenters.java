package com.samujjwaal.hw1.simulations;

import com.samujjwaal.hw1.utils.*;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Simulation for Task 5: showing execution of cloudlets in 3 datacenters running
 * on combination of SaaS, IaaS and PaaS models.
 * At the end of execution of cloudlet(s), it outputs the cost of execution.
 */

public class SimulateDatacenters {
    CloudSim simulation;
    Datacenter dc1,dc2,dc3;
    DatacenterBroker broker;

    public SimulateDatacenters() {
        Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

        logger.info("Initiating Final Simulation\n");
        String configFile1 = "Default";
        String configFile2 = "DataCenterSimulation";

        // create instance object of CloudSim class
        simulation = new CloudSim();

        // create instances of classes for parsing config files and get datacenter specifications
        iaasAndDefaultSpecs defaultSpecs = new iaasAndDefaultSpecs(configFile1,1);
        iaasAndDefaultSpecs iaasSpecs = new iaasAndDefaultSpecs(configFile2,1);
        paasSpecs paasSpecs = new paasSpecs(configFile2,2);

        logger.info("Parsed datacenter specifications from config\n");
        // create instance of util class for each datacenter
        DataCenterUtils iaasUtil = new DataCenterUtils(iaasSpecs.getHostConfig(), iaasSpecs.getDatacenterConfig(),
                iaasSpecs.getVMConfig(), iaasSpecs.getCloudletConfig());
        DataCenterUtils paasUtil = new DataCenterUtils(defaultSpecs.getHostConfig(), defaultSpecs.getDatacenterConfig(),
                defaultSpecs.getVMConfig(),paasSpecs.getCloudletConfig());
        DataCenterUtils saasUtil = new DataCenterUtils(defaultSpecs.getHostConfig(), defaultSpecs.getDatacenterConfig(),
                defaultSpecs.getVMConfig(),defaultSpecs.getCloudletConfig());

        // create a new IaaS datacenter instance for simulation
        dc1 = iaasUtil.createDatacenter(simulation, new VmSchedulerSpaceShared());
        // create a new PaaS datacenter instance for simulation
        dc2 = paasUtil.createDatacenter(simulation);
        // create a new SaaS datacenter instance for simulation
        dc3 = saasUtil.createDatacenter(simulation);

        broker = new DatacenterBrokerSimple(simulation);

        // Creates the network topology from a brite file
        connectDataCenters();

        logger.info("Creating list of VMs & submitting to datacenter broker");
        // create and submit Vms to broker
        List<Vm> vmList = new ArrayList<>();
        vmList.addAll(iaasUtil.createVmList());
        vmList.addAll(paasUtil.createVmList());
        vmList.addAll(saasUtil.createVmList());
        broker.submitVmList(vmList);

        logger.info("Creating list of Cloudlets & submitting to datacenter broker");
        // create and submit Cloudlets to broker
        List<Cloudlet> cloudletList = new ArrayList<>();
        cloudletList.addAll(iaasUtil.createCloudletList());
        cloudletList.addAll(paasUtil.createCloudletList());
        cloudletList.addAll(saasUtil.createCloudletList());
        broker.submitCloudletList(cloudletList);

        simulation.start();
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        finishedCloudlets.sort(Comparator.comparingLong(Identifiable::getId));
        new CloudletsTableBuilder(finishedCloudlets).build();

        System.out.println("\nTotal cost of executing cloudlets = "+ DataCenterUtils.executionCost(finishedCloudlets));
        System.out.println("\n");

        logger.info("End of execution of Final Simulation\n\n");
    }

    //Map CloudSim entities to BRITE entities
    private void connectDataCenters() {
        //load the network topology file
        NetworkTopology networkTopology = BriteNetworkTopology.getInstance("topology.brite");
        simulation.setNetworkTopology(networkTopology);

        //Datacenter1 mapped to BRITE node 0
        networkTopology.mapNode(dc1.getId(), 0);

        //Datacenter2 mapped to BRITE node 2
        networkTopology.mapNode(dc2.getId(), 2);

        //Datacenter3 mapped to BRITE node 3
        networkTopology.mapNode(dc3.getId(), 3);

        //Broker mapped to BRITE node 4
        networkTopology.mapNode(broker.getId(), 4);
    }
}

/**
 * Class to parse config files for IaaS model and SaaS model
 */

class iaasAndDefaultSpecs {
    private final LoadHostConfig configHost;
    private final LoadDataCenterConfig configDatacenter;
    private final LoadVmConfig configVM;
    private final LoadCloudletConfig configCloudlet;

    public iaasAndDefaultSpecs(String configFile, int simulationNo) {
        this.configHost = new LoadHostConfig(configFile, simulationNo, 1);
        this.configDatacenter = new LoadDataCenterConfig(configFile, simulationNo, 1);
        this.configVM = new LoadVmConfig(configFile, simulationNo, 1);
        this.configCloudlet = new LoadCloudletConfig(configFile, simulationNo, 1);
    }

    public LoadHostConfig getHostConfig(){
        return configHost;
    }

    public LoadDataCenterConfig getDatacenterConfig() {
        return configDatacenter;
    }

    public LoadVmConfig getVMConfig() {
        return configVM;
    }

    public LoadCloudletConfig getCloudletConfig() {
        return configCloudlet;
    }
}

/**
 * Class to parse config files for PaaS model
 */

class paasSpecs{

    private final LoadCloudletConfig configCloudlet;

    public paasSpecs(String configFile, int simulationNo) {
        this.configCloudlet = new LoadCloudletConfig(configFile, simulationNo, 1);
    }

    public LoadCloudletConfig getCloudletConfig() {
        return configCloudlet;
    }
}
