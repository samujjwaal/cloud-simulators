package com.samujjwaal.hw1.utils;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for various util methods in class DataCenterUtils
 * to verify the creation of datacenter components from config files
 */

class DataCenterUtilsTest {

    static DataCenterUtils dcUtils;
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
        assertNotNull(configHost);
        configDatacenter = new LoadDataCenterConfig(simulationModel,simulNo,index);
        assertNotNull(configDatacenter);
        configVM = new LoadVmConfig(simulationModel,simulNo,index);
        assertNotNull(configVM);
        configCloudlet = new LoadCloudletConfig(simulationModel,simulNo,index);
        assertNotNull(configCloudlet);

        dcUtils = new DataCenterUtils(configHost,configDatacenter,configVM,configCloudlet);
        assertNotNull(dcUtils);

    }

    @Test
    void testCreateHost() throws IllegalAccessException, InstantiationException {
        Host host = dcUtils.createHost();
        assertNotNull(host);
        assertEquals(host.getNumberOfPes(),configHost.numberOfPE);
    }

    @Test
    void testCreateHostList() throws IllegalAccessException, InstantiationException {
        List<Host> list = dcUtils.createHostList();
        assertNotNull(list);
        assertEquals(list.size(),configDatacenter.numberOfHosts);
    }

    @Test
    void testCreateVm(){
        Vm vm = dcUtils.createVm();
        assertNotNull(vm);
        assertEquals(vm.getNumberOfPes(),configVM.numberOfPE);
    }

    @Test
    void testCreateVmList(){
        List<Vm> list = dcUtils.createVmList();
        assertNotNull(list);
        assertEquals(list.size(),configDatacenter.numberOfVms);
    }

    @Test
    void testCreateCloudlet(){
        Cloudlet cloudlet = dcUtils.createCloudlet();
        assertNotNull(cloudlet);
        assertEquals(cloudlet.getNumberOfPes(),configCloudlet.numberOfPE);
    }

    @Test
    void testCreateCloudletList(){
        List<Cloudlet> list = dcUtils.createCloudletList();
        assertNotNull(list);
        assertEquals(list.size(),configDatacenter.numberOfCloudlets);
    }
}