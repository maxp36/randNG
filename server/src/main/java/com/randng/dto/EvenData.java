package com.randng.dto;

import java.util.List;

public class EvenData {

    public List<Integer> manyEvenFromTo;
    public List<Float> distribution;
    public float theorDistribution;
    public double mean;
    public double dev;

    public EvenData(List<Integer> manyEvenFromTo,
                    List<Float> distribution,
                    float theorDistribution,
                    double mean,
                    double dev) {
        this.manyEvenFromTo = manyEvenFromTo;
        this.distribution = distribution;
        this.theorDistribution = theorDistribution;
        this.mean = mean;
        this.dev = dev;
    }
}
