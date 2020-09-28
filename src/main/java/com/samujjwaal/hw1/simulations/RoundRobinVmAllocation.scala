package com.samujjwaal.hw1.simulations

import java.util
import java.util.Comparator

import com.samujjwaal.hw1.utils._
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRoundRobin
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.slf4j.{Logger, LoggerFactory}

/**
 * A simulation that shows the usage of the VmAllocationPolicyRoundRobin,
 * that cyclically assigns VMs to Hosts.
 * This policy places a VM into a Host and moves to the next Host.
 *
 * All hosts are not powered-on when created. As VMs need to be placed,
 * Hosts are activated on demand (as can be checked in the logs).
 */
class RoundRobinVmAllocation(val simulationNo: Int) {

  //Define a static logger variable so that it references the Logger instance
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  logger.info("Initiating Round Robin Vm Allocation Simulation\n")

  val simulationModel = "RoundRobin"
  val index = 1

  // create instance object of CloudSim class
  val simulation = new CloudSim

  // create instances of classes for parsing config files and get simulation specifications
  val hostSpec = new LoadHostConfig(simulationModel, simulationNo, index)
  val dcSpec = new LoadDataCenterConfig(simulationModel, simulationNo, index)
  val vmSpec = new LoadVmConfig(simulationModel, simulationNo, index)
  val cloudletSpec = new LoadCloudletConfig(simulationModel, simulationNo, index)

  logger.info("Parsed datacenter specifications from config\n")

  // create instance of util class for datacenter operations
  val dcUtil = new DataCenterUtils(hostSpec, dcSpec, vmSpec, cloudletSpec)

  // create a new datacenter for simulation, Hosts are not turned on creation
  dcUtil.createDatacenter(simulation, new VmAllocationPolicyRoundRobin, false)

  // Creates a broker that is a software acting on behalf a cloud customer to manage VMs and Cloudlets
  val broker = new DatacenterBrokerSimple(simulation)

  logger.info("Creating list of VMs & submitting to datacenter broker")
  // create list of vms from config
  val vmList: util.List[Vm] = dcUtil.createVmList
  // submit vm list to broker
  broker.submitVmList(vmList)

  logger.info("Creating list of Cloudlets & submitting to datacenter broker")
  // create list of cloudlets from config
  val cloudletList: util.List[Cloudlet] = dcUtil.createCloudletList
  // submit cloudlet list to broker
  broker.submitCloudletList(cloudletList)

  // execute simulation
  simulation.start

  val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList
  finishedCloudlets.sort(Comparator.comparingLong((cloudlet: Cloudlet) => cloudlet.getVm.getId))
  new CloudletsTableBuilder(finishedCloudlets).build()
  System.out.println("\n")

  logger.info("End of execution of Round Robin Vm Allocation Simulation\n\n")
}