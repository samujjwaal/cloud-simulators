package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadCloudletConfig {
    Config conf;
    int  length, numberOfPE, size;

    public LoadCloudletConfig(String simulationType,int simulNum, int index) {
        conf = ConfigFactory.load(simulationType);
        String configItem = "simulation"+simulNum+"."+"cloudlet"+index+".";
        length = conf.getInt(configItem + "length");
        numberOfPE = conf.getInt(configItem + "numberOfPE");
        size = conf.getInt(configItem + "size");
    }
}
