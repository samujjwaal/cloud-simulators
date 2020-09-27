package com.samujjwaal.hw1;

import com.samujjwaal.hw1.simulations.CloudletExecutionSchedulerSimulation;
import com.samujjwaal.hw1.simulations.RoundRobinVmAllocation;
import com.samujjwaal.hw1.simulations.SaaSSimulation;
import com.samujjwaal.hw1.simulations.SimulateDatacenters;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteSimulations {
    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(ExecuteSimulations.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("<------- Executing all Hw1 Simulations ------->\n\n");

        logger.info("<><><> Simulating Vm Scheduler Execution<><><>\n\n");
        logger.info("------- Time Shared VM Scheduling Simulation -------\n");
        new CloudletExecutionSchedulerSimulation(1, "TimeShared", new VmSchedulerTimeShared(), new CloudletSchedulerTimeShared());
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        logger.info("------- Space Shared VM Scheduling Simulation -------\n");
        new CloudletExecutionSchedulerSimulation(1,"SpaceShared", new VmSchedulerSpaceShared(), new CloudletSchedulerSpaceShared());
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");

        logger.info("<><><> Round Robin Vm Allocation Simulation <><><>\n\n");
        new RoundRobinVmAllocation(1);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");

        logger.info("<><><> Simulating Software as a Service Model <><><>\n\n");
        logger.info("------- SaaS simulation 1 -------\n");
        new SaaSSimulation(1);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        logger.info("------- SaaS simulation 2 -------\n");
        new SaaSSimulation(2);
        logger.info("End of all SaaS simulations\n\n");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");

        logger.info("<><><> Simulating 3 datacenters for Task 5 <><><>\n\n");
        new SimulateDatacenters();
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        logger.info("<------- End of Hw1 Simulations ------->");
        }
}