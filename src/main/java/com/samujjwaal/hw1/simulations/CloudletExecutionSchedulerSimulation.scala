package com.samujjwaal.hw1.simulations

import java.util

import com.samujjwaal.hw1.utils._
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.slf4j.{Logger, LoggerFactory}

/**
 * A simple simulation showing Time Shared and Space Shared VM Scheduling
 * in a simple datacenter with 1 host, 1 VM and 2 cloudlets for execution.
 */
class CloudletExecutionSchedulerSimulation(val simulationNo: Int, val simulationModel: String, val vmScheduler: VmScheduler, val cloudletScheduler: CloudletScheduler) {

  //Define a static logger variable so that it references the Logger instance
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  logger.info("Initiating " + simulationModel + " Vm Execution Simulation\n")

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

  // create a new datacenter for simulation
  dcUtil.createDatacenter(simulation, vmScheduler)

  // Creates a broker that is a software acting on behalf a cloud customer to manage VMs and Cloudlets
  val broker = new DatacenterBrokerSimple(simulation)

  logger.info("Creating VM & submitting to datacenter broker")
  // create a vm from config
  val vm: Vm = dcUtil.createVm(cloudletScheduler)
  // submit vm to broker
  broker.submitVm(vm)

  logger.info("Creating list of Cloudlets & submitting to datacenter broker")
  // create list of cloudlets from config
  val cloudletList: util.List[Cloudlet] = dcUtil.createCloudletList
  // submit cloudlet list to broker
  broker.submitCloudletList(cloudletList)

  // execute simulation
  simulation.start

  val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList
  new CloudletsTableBuilder(finishedCloudlets).build()

  System.out.println("\n")

  logger.info("End of execution of " + simulationModel + " Vm Execution Simulation\n\n")
}