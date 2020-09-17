package com.samujjwaal.hw1;

import com.samujjwaal.hw1.simulations.SaaSSimulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteSimulations {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteSimulations.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Executing all Hw1 Simulations");
        logger.info("Software as a Service Model Simulation");
        new SaaSSimulation();
    }
}
