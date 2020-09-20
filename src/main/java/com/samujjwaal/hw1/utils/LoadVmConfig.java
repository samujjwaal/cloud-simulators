package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadVmConfig {
    Config conf = ConfigFactory.load("SaaSSim");
    long  mips = conf.getLong("vm1.mips");
    int  numberOfPE = conf.getInt("vm1.numberOfPE");
    int  ram = conf.getInt("vm1.ram");
    int  bw = conf.getInt("vm1.bw");
    int  size = conf.getInt("vm1.size");
    String  vmm = conf.getString("vm1.vmm");
}
