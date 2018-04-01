package com.randng.utils;

import java.util.ArrayList;
import java.util.List;

public class NormalGenerator {

    private boolean ready = false;

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
                tempNumbers.add(0.0);
            } else {
                tempNumbers.add(sum / count);
            }
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
