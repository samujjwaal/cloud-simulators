package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadHostConfig {
    Config conf = ConfigFactory.load("SaaSSim");
    int  ram = conf.getInt("host1.ram");
    int  storage = conf.getInt("host1.storage");
    int  bw = conf.getInt("host1.bw");
    int  numberOfPE = conf.getInt("host1.numberOfPE");
    int  mips = conf.getInt("host1.mips");
}
