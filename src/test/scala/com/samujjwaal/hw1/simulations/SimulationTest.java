package com.samujjwaal.hw1.simulations;

import com.samujjwaal.hw1.utils.*;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.vms.Vm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test to verify the proper simulation of a simple datacenter
 * created using components by parsing config file specifications
 */

class SimulationTest {

    static DataCenterUtils dcUtil;
    static LoadHostConfig configHost;
    static LoadDataCenterConfig configDatacenter;
    static LoadVmConfig configVM;
    static LoadCloudletConfig configCloudlet;

    @BeforeAll
    static void init() {
        String simulationModel = "RoundRobin";
        int simulNo = 1;
        int index = 1;
        configHost = new LoadHostConfig(simulationModel, simulNo,index);
        configDatacenter = new LoadDataCenterConfig(simulationModel,simulNo,index);
        configVM = new LoadVmConfig(simulationModel,simulNo,index);
        configCloudlet = new LoadCloudletConfig(simulationModel,simulNo,index);

        dcUtil = new DataCenterUtils(configHost,configDatacenter,configVM,configCloudlet);
    }

    @Test
    void testSimulationExecution(){
        // create instance object of CloudSim class
        CloudSim simulation = new CloudSim();

        // create a new datacenter for simulation
        Datacenter dc = dcUtil.createDatacenter(simulation);
        assertNotNull(dc);

        DatacenterBroker broker = new DatacenterBrokerSimple(simulation);

        List<Vm> vmList = dcUtil.createVmList();
        assertNotNull(vmList);
        broker.submitVmList(vmList);

        List<Cloudlet> cloudletList = dcUtil.createCloudletList();
        assertNotNull(cloudletList);
        broker.submitCloudletList(cloudletList);

        // execute simulation
        simulation.start();

        assertFalse(simulation.isRunning());

        assertEquals(broker.getVmCreatedList().size(),vmList.size());
        assertEquals(broker.getCloudletFinishedList().size(),cloudletList.size());

    }


}