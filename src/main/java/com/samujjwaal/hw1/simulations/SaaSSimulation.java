package com.samujjwaal.hw1.simulations;

import com.samujjwaal.hw1.utils.*;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A simple simulation showing execution of a simple datacenter running
 * on Software as a Service(SaaS) model.
 * At the end of execution of cloudlet(s), it outputs the cost of execution.
 */

public class SaaSSimulation {

    public SaaSSimulation(int simulationNo) {
        //Define a static logger variable so that it references the Logger instance
        Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
        logger.info("Initiating SaaS Simulation\n");

        String simulationModel = "SaaSSim";
        int index = 1;

        // create instance object of CloudSim class
        CloudSim simulation = new CloudSim();

        // create instances of classes for parsing config files and get simulation specifications
        LoadHostConfig hostSpec = new LoadHostConfig(simulationModel, simulationNo, index);
        LoadDataCenterConfig dcSpec = new LoadDataCenterConfig(simulationModel, simulationNo, index);
        LoadVmConfig vmSpec = new LoadVmConfig(simulationModel, simulationNo, index);
        LoadCloudletConfig cloudletSpec = new LoadCloudletConfig(simulationModel, simulationNo, index);

        logger.info("Parsed datacenter specifications from config\n");

        // create instance of util class for datacenter operations
        DataCenterUtils dcUtil = new DataCenterUtils(hostSpec, dcSpec, vmSpec, cloudletSpec);

        // create a new datacenter for simulation
        dcUtil.createDatacenter(simulation);

        // Creates a broker that is a software acting on behalf a cloud customer to manage VMs and Cloudlets
        DatacenterBroker broker0 = new DatacenterBrokerSimple(simulation);

        logger.info("Creating list of VMs & submitting to datacenter broker");
        // create list of vms from config
        List<Vm> vmList = dcUtil.createVmList();
        // submit vm list to broker
        broker0.submitVmList(vmList);


        logger.info("Creating list of Cloudlets & submitting to datacenter broker");
        // create list of cloudlets from config
        List<Cloudlet> cloudletList = dcUtil.createCloudletList();
        // submit cloudlet list to broker
        broker0.submitCloudletList(cloudletList);

        // execute simulation
        simulation.start();

        final List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();

        new CloudletsTableBuilder(finishedCloudlets).build();

        System.out.println("Total cost of simulation = " + DataCenterUtils.executionCost(finishedCloudlets));
        System.out.println("\n");

        logger.info("End of execution of SaaS Simulation\n\n");
    }
}