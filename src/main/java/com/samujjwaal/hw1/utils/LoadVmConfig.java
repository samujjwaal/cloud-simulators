package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * A util class to parse VM specifications from config file
 */

public class LoadVmConfig {
    Config conf;
    double  mips;
    int  numberOfPE, ram, bw, size;
    String  vmm;

    public LoadVmConfig(String simulationType,int simulNum, int index) {
        conf = ConfigFactory.load(simulationType);
        String configItem = "simulation"+simulNum+"."+"vm"+index+".";
        mips = conf.getInt(configItem + "mips");
        numberOfPE = conf.getInt(configItem + "numberOfPE");
        ram = conf.getInt(configItem + "ram");
        bw = conf.getInt(configItem + "bw");
        size = conf.getInt(configItem + "size");
        vmm = conf.getString(configItem + "vmm");
    }
}
