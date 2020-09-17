package com.samujjwaal.hw1.simulations;

import com.samujjwaal.hw1.utils.DataCenterUtils;
import com.samujjwaal.hw1.utils.LoadDataCenterConfig;
import com.samujjwaal.hw1.utils.LoadHostConfig;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.vms.Vm;
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

        System.out.println(dc.getHostList().toString());
        System.out.println(dc.getCharacteristics().toString());

    }


}
