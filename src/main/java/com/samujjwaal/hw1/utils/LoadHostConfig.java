package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * A util class to parse host specifications from config file
 */

public class LoadHostConfig {

    Config conf;
    int  ram, storage, bw, numberOfPE, mips;

    public LoadHostConfig(String simulationType,int simulNum, int index) {
        conf = ConfigFactory.load(simulationType);
        String configItem = "simulation"+simulNum+"."+"host"+index+".";
        ram = conf.getInt(configItem + "ram");
        storage = conf.getInt(configItem + "storage");
        bw = conf.getInt(configItem + "bw");
        numberOfPE = conf.getInt(configItem + "numberOfPE");
        mips = conf.getInt(configItem + "mips");
    }
}
