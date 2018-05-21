package com.randng.dto;

import java.util.List;

public class NormalData {

    public List<Double> manyNormal;
    public List<Double> distribution;
    public List<Double> theorDistribution;
    public double mean;
    public double dev;
    public double theorMean;
    public double theorDev;
    public double minValue;
    public double maxValue;
    public double x2;
    public double theorX2;
    public boolean testTheor;

    public NormalData(List<Double> manyNormal,
                      List<Double> distribution,
                      List<Double> theorDistribution,
                      double mean,
                      double dev,
                      double theorMean,
                      double theorDev,
                      double minValue,
                      double maxValue,
                      double x2,
                      double theorX2,
                      boolean testTheor) {
        this.manyNormal = manyNormal;
        this.distribution = distribution;
        this.theorDistribution = theorDistribution;
        this.mean = mean;
        this.dev = dev;
        this.theorMean = theorMean;
        this.theorDev = theorDev;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.x2 = x2;
        this.theorX2 = theorX2;
        this.testTheor = testTheor;
    }
}
