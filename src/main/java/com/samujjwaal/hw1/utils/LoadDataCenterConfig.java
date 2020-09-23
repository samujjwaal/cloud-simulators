package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadDataCenterConfig {
    Config conf;
    int numberOfHosts;
    String arch, os, vmm;
    double costPerSecond ,costPerMem, costPerStorage, costPerBw;

    public LoadDataCenterConfig(String simulationType,int simulNum, int index) {
        conf = ConfigFactory.load(simulationType);
        String configItem = "simulation"+simulNum+"."+"dataCenter"+index+".";
        numberOfHosts = conf.getInt(configItem + "numberOfHosts");
        arch = conf.getString(configItem + "arch");
        os = conf.getString(configItem + "os");
        vmm = conf.getString(configItem + "vmm");
        costPerSecond = conf.getDouble(configItem + "costPerSecond");
        costPerMem = conf.getDouble(configItem + "costPerMem");
        costPerStorage = conf.getDouble(configItem + "costPerStorage");
        costPerBw = conf.getDouble(configItem + "costPerStorage");
    }
}
