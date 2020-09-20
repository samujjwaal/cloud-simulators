package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadCloudletConfig {
    Config conf = ConfigFactory.load("SaaSSim");
    long  length = conf.getLong("cloudLet1.length");
    int  numberOfPE = conf.getInt("cloudLet1.numberOfPE");
    int  size = conf.getInt("cloudLet1.size");
}
