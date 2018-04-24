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

    public EvenData(List<Integer> manyEvenFromTo,
                    List<Float> distribution,
                    float theorDistribution,
                    double mean,
                    double dev,
                    double theorMean,
                    double theorDev) {
        this.manyEvenFromTo = manyEvenFromTo;
        this.distribution = distribution;
        this.theorDistribution = theorDistribution;
        this.mean = mean;
        this.dev = dev;
        this.theorMean = theorMean;
        this.theorDev = theorDev;
    }
}
