package com.randng.controllers;

import com.randng.dto.EvenData;
import com.randng.dto.NormalData;
import com.randng.utils.EvenGenerator;
import com.randng.utils.NormalGenerator;
import com.randng.utils.PearsonTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/")
public class MainController {

    @GetMapping(path = "even", params = { "seed", "num", "from", "to", "column", "sLevel" })
    public ResponseEntity<EvenData> getEvenData(@RequestParam final int seed,
                                                @RequestParam final int num,
                                                @RequestParam final int from,
                                                @RequestParam final int to,
                                                @RequestParam final int column,
                                                @RequestParam final double sLevel) {

        EvenGenerator g = new EvenGenerator(seed);

        List<Integer> manyEvenFromTo = g.generateManyFromTo(num, from, to);
        List<Float> distribution = g.getDistribution(num, from, to, column, manyEvenFromTo);
        float theorDistribution = g.getTheorDistribution(from, to);
        double m = g.getMean();
        double d = g.getDev();
        double theorMean = g.getTheorMean();
        double theorDev = g.getTheorDev();
        PearsonTest pt = new PearsonTest();
        double x2 = pt.computeX2(distribution, theorDistribution);
        double theorX2 = pt.getTheorX2(column - 1, sLevel);
        boolean testTheor = pt.compareX2(x2, column - 1, sLevel);

        EvenData data = new EvenData(manyEvenFromTo,
                distribution,
                theorDistribution,
                m,
                d,
                theorMean,
                theorDev,
                x2,
                theorX2,
                testTheor);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(path = "normal", params = { "seed", "num", "mean", "dev", "column", "sLevel" })
    public ResponseEntity<NormalData> getNormalData(@RequestParam final int seed,
                                                    @RequestParam final int num,
                                                    @RequestParam final float mean,
                                                    @RequestParam final float dev,
                                                    @RequestParam final int column,
                                                    @RequestParam final double sLevel) {

        NormalGenerator g = new NormalGenerator(seed);

        List<Double> manyNormal = g.generateMany(num, mean, dev);
        double m = g.getMean();
        double d = g.getDev();
        double minValue = g.getMinValue();
        double maxValue = g.getMaxValue();
        List<Double> distribution = g.getDistribution(m, d, column, manyNormal);
        List<Double> theorDistribution = g.getTheorDistribution(mean, dev, column, manyNormal);
        List<Double> splittedDistribution = g.getSplittedDistribution(column, distribution);
        List<Double> splittedTheorDistribution = g.getSplittedDistribution(column, theorDistribution);
        PearsonTest pt = new PearsonTest();
        double x2 = pt.computeX2(splittedDistribution, splittedTheorDistribution);
        double theorX2 = pt.getTheorX2(column - 3, sLevel);
        boolean testTheor = pt.compareX2(x2, column - 3, sLevel);

        NormalData data = new NormalData(manyNormal,
                distribution,
                theorDistribution,
                m,
                d,
                mean,
                dev,
                minValue,
                maxValue,
                x2,
                theorX2,
                testTheor);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
