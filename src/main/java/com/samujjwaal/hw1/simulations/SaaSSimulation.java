package com.samujjwaal.hw1.simulations;

import com.samujjwaal.hw1.utils.*;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SaaSSimulation {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

//    private final CloudSim simulation;
//    private DatacenterBroker broker0;
//    private List<Vm> vmList;
//    private List<Cloudlet> cloudletList;
//    private Datacenter datacenter0;

    public SaaSSimulation(){
        logger.info("Initiating SaaS Simulation");

        CloudSim simulation = new CloudSim();

        LoadHostConfig host0 = new LoadHostConfig();

        LoadDataCenterConfig datacenter0 = new LoadDataCenterConfig();

        Datacenter dc = DataCenterUtils.createDatacenter(host0, datacenter0,simulation);

        DatacenterBroker broker0 = new DatacenterBrokerSimple(simulation,"SaaS_Simulation_Broker");


        LoadVmConfig vmSpec = new LoadVmConfig();

        Vm vm = DataCenterUtils.createVm(vmSpec);

        broker0.submitVm(vm);


        LoadCloudletConfig cloudletSpec = new LoadCloudletConfig();

        Cloudlet cloudlet = DataCenterUtils.createCloudlet(cloudletSpec, new UtilizationModelFull());

        broker0.submitCloudlet(cloudlet);


        simulation.start();

        final List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(finishedCloudlets).build();


    }


}
