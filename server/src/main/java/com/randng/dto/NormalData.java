package com.randng.dto;

import java.util.List;

public class NormalData {

    public List<Double> manyNormal;
    public List<Double> distribution;
    public List<Double> theorDistribution;
    public double mean;
    public double dev;
    public double minValue;
    public double maxValue;

    public NormalData(List<Double> manyNormal,
                      List<Double> distribution,
                      List<Double> theorDistribution,
                      double mean,
                      double dev,
                      double minValue,
                      double maxValue) {
        this.manyNormal = manyNormal;
        this.distribution = distribution;
        this.theorDistribution = theorDistribution;
        this.mean = mean;
        this.dev = dev;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
