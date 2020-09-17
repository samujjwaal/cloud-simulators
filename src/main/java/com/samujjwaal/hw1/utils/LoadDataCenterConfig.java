package com.samujjwaal.hw1.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadDataCenterConfig {
    Config conf = ConfigFactory.load("SaaSSim");
    int  numberOfHosts = conf.getInt("dataCenter1.numberOfHosts");
    String  arch = conf.getString("dataCenter1.arch");
    String  os = conf.getString("dataCenter1.os");
    String  vmm = conf.getString("dataCenter1.vmm");
}
