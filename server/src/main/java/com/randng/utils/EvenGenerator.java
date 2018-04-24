package com.randng.utils;

import java.util.ArrayList;
import java.util.List;

public class EvenGenerator {

    // see http://docs.cntd.ru/document/gost-r-iso-28640-2012
    // Комбинация значений A, C и M обеспечивает генерацию
    // положительных целых чисел из интервала от 1 до 2^31-2.
    private static final int A = 2100005341;
    private static final int C = 0;
    private static final int M = Integer.MAX_VALUE; // 2^31-1

    private static final int maxValue = Integer.MAX_VALUE - 1; // 2^31-2

    private int seed;

    private double mean;
    private double dev;
    private double theorMean;
    private double theorDev;

    public EvenGenerator(int seed) {
        this.seed = seed;
    }

    // Генерация псевдослучайных чисел
    // линейным конгруэнтным методом.
    // Для большей случайности конечное число составляется
    // из старших битов двух сгенерированых чисел.
    private int generateOne() {

        int firstPart;
        int secondPart;

        seed = Integer.remainderUnsigned((A*seed + C), M);
        firstPart = seed & 0xFFFF0000;
        seed = Integer.remainderUnsigned((A*seed + C), M);
        secondPart = seed >>> 16;

        seed = firstPart | secondPart;
        return seed;
    }

    private List<Integer> generateMany(int num) {

        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            numbers.add(generateOne());
        }

        return numbers;
    }

    public List<Integer> generateManyFromTo(int num, int from, int to) {

        if (from > to) {
            int temp = from;
            from = to;
            to = temp;
        }

        int temp;

        mean = 0;
        dev = 0;

        List<Integer> numbers = generateMany(num);
        List<Integer> tempNumbers = new ArrayList<>();

        for (Integer n : numbers) {
            temp = (n % (to - from) + from);
            mean += temp;
            tempNumbers.add(temp);
        }

        mean = mean / num;

        for (Integer n : tempNumbers) {
            dev += (n - mean) * (n - mean);
        }

        dev = dev / num; //дисперсия
        dev = Math.sqrt(dev); // среднеквадратическое отклонение

        return tempNumbers;
    }

    // Генерация данных нормального распределения,
    // необходимых для построения гистограммы
    public List<Float> getDistribution(int num, int from, int to, int columnCount, List<Integer> numbers) {

        if (from > to) {
            int temp = from;
            from = to;
            to = temp;
        }

        theorMean = (to - from) / 2 + from;
        theorDev = (theorMean - from) / 2;

        List<Float> tempNumbers = new ArrayList<>();

        float size = (to - from) / columnCount;
        int count;

        for (int i = 0; i < columnCount; i++) {

            float start = from + size * i;
            float end = start + size;

            count = 0;

            for (Integer n : numbers) {
                if (n >= start && n < end) {
                    count++;
                }
            }

            tempNumbers.add( ((float)count / (float)num) / size );
        }

        return tempNumbers;
    }

    public float getTheorDistribution(int from, int to) {

        if (from > to) {
            int temp = from;
            from = to;
            to = temp;
        }

        return 1.0f / (to - from);
    }

    // Приведение значения к виду 0 < x < 1
    public float getOneStandard() {
        return (float) generateOne() / maxValue;
    }

    public double getMean() {
        return mean;
    }

    public double getDev() {
        return dev;
    }

    public double getTheorMean() {
        return theorMean;
    }

    public double getTheorDev() {
        return theorDev;
    }
}
