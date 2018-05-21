package com.randng.dto;

import java.util.List;

public class EvenData {

    public List<Integer> manyEvenFromTo;
    public List<Float> distribution;
    public float theorDistribution;
    public double mean;
    public double dev;
    public double theorMean;
    public double theorDev;
    public double x2;
    public double theorX2;
    public boolean testTheor;

    public EvenData(List<Integer> manyEvenFromTo,
                    List<Float> distribution,
                    float theorDistribution,
                    double mean,
                    double dev,
                    double theorMean,
                    double theorDev,
                    double x2,
                    double theorX2,
                    boolean testTheor) {
        this.manyEvenFromTo = manyEvenFromTo;
        this.distribution = distribution;
        this.theorDistribution = theorDistribution;
        this.mean = mean;
        this.dev = dev;
        this.theorMean = theorMean;
        this.theorDev = theorDev;
        this.x2 = x2;
        this.theorX2 = theorX2;
        this.testTheor = testTheor;
    }
}
