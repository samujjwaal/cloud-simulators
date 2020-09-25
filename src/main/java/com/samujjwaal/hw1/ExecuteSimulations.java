package com.samujjwaal.hw1;

import com.samujjwaal.hw1.simulations.RoundRobinVmAllocation;
import com.samujjwaal.hw1.simulations.SaaSSimulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteSimulations {
    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(ExecuteSimulations.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("<------- Executing all Hw1 Simulations ------->\n\n");

        logger.info("<><><> Simulating Software as a Service Model <><><>\n\n");

        logger.info("------- SaaS simulation 1 -------\n");
        new SaaSSimulation(1);
        logger.info("------- SaaS simulation 2 -------\n");
        new SaaSSimulation(2);
        logger.info("End of all SaaS simulations\n\n");

        logger.info("<><><> Round Robin Vm Allocation Simulation <><><>\n");
        new RoundRobinVmAllocation(1);
    }
}
