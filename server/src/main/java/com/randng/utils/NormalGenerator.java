package com.randng.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NormalGenerator {

    private boolean ready = false;
    private int STEP_SIZE = 10;

    private double second;
    private double minValue = Double.MAX_VALUE;
    private double maxValue = Double.MIN_VALUE;
    private double mean;
    private double dev;

    // Для обеспечения независимости пар генерируемых
    // нормально распределенных чисел
    // используются 2 различных генератора
    // равномерно распределенных чисел
    private EvenGenerator firstGenerator;
    private EvenGenerator secondGenerator;

    public NormalGenerator(int seed) {
        firstGenerator = new EvenGenerator(seed);
        secondGenerator = new EvenGenerator(seed & 0xF0F0F0F0);
    }

    // Генерация нормально распределенных чисел
    // по методу Бокса-Мюллера
    private double generateOne(double m, double d) {

        if (ready) {
            ready = false;
            return second;
        } else {
            float firstEven = firstGenerator.getOneStandard();
            float secondEven = secondGenerator.getOneStandard();

            double first = Math.sqrt((-2) * Math.log(1 - firstEven)) *
                    Math.cos(2 * Math.PI * secondEven) * d + m;
            checkMin(first);
            checkMax(first);

            second = Math.sqrt((-2) * Math.log(1 - firstEven)) *
                    Math.sin(2 * Math.PI * secondEven) * d + m;
            checkMin(second);
            checkMax(second);

            ready = true;

            return first;
        }
    }

    private void checkMin(double val) {
        if (val < minValue) {
            minValue = val;
        }
    }

    private void checkMax(double val) {
        if (val > maxValue) {
            maxValue = val;
        }
    }

    public List<Double> generateMany(int num, double m, double d) {

        double temp;

        mean = 0;
        dev = 0;

        List<Double> numbers = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            temp = generateOne(m, d);
            mean += temp;
            numbers.add(temp);
        }

        mean = mean / num;

        for (Double n : numbers) {
            dev += (n - mean) * (n - mean);
        }

        dev = dev / num; //дисперсия
        dev = Math.sqrt(dev); // среднеквадратическое отклонение

        return numbers;
    }

    // Генерация данных равномерного распределения,
    // необходимых для построения гистограммы
    public List<Double> getDistribution(double m, double d, int columnCount, List<Double> numbers) {

        double temp;
        double sum;
        int count;

        double size = (maxValue - minValue) / columnCount;

        List<Double> tempNumbers = new ArrayList<>();

        for (int i = 0; i < columnCount; i++) {

            double start = minValue + size * i;
            double end = start + size;

            sum = 0;
            count = 0;

            for (Double n : numbers) {
                if (n >= start && n <= end) {
                    count++;
                    temp = Math.exp(-((n - m) * (n - m)) / (2 * d * d)) /
                            (d * Math.sqrt(2 * Math.PI));
                    sum += temp;
                }
            }

            if (count == 0) {
                for(int k = 0; k < STEP_SIZE; k++) {
                    tempNumbers.add(0.0);
                }
            } else {
                for(int k = 0; k < STEP_SIZE; k++) {
                    tempNumbers.add(sum / count);
                }
            }
        }

        return tempNumbers;
    }

    public List<Double> getTheorDistribution(double m, double d, int columnCount, List<Double> numbers) {

        double temp;
        List<Double> vars = new ArrayList<>(numbers);
        Collections.sort(vars);
        List<Double> tempNumbers = new ArrayList<>();

        for (double current = minValue;
             current < maxValue;
             current += (maxValue - minValue) / (STEP_SIZE * columnCount)) {
            temp = Math.exp(-((current - m) * (current - m)) / (2 * d * d)) /
                    (d * Math.sqrt(2 * Math.PI));
            tempNumbers.add(temp);
        }

        return tempNumbers;
    }

    public List<Double> getSplittedDistribution(int columnCount, List<Double> numbers) {

        double sum;
        int count;

        int size = STEP_SIZE;

        List<Double> tempNumbers = new ArrayList<>();

        for (int i = 0; i < columnCount; i++) {

            int start = size * i;
            int end = start + size;

            sum = 0;
            count = 0;

            List<Double> vars = numbers.subList(start, end);

            for (Double n : vars) {
                count++;
                sum += n;
            }

            tempNumbers.add(sum / count);
        }

        return tempNumbers;
    }

    public double getMean() {
        return mean;
    }

    public double getDev() {
        return dev;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
