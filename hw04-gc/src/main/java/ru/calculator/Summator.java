package ru.calculator;

public class Summator {
    private static final int MAX_SIZE = 6_600_000;

    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private int[] listValues = new int[MAX_SIZE];
    private int currentSize;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues[currentSize] = data.getValue();
        currentSize++;
        if (currentSize % MAX_SIZE == 0) {
            listValues = new int[MAX_SIZE];
            currentSize = 0;
        }
        sum += data.getValue();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + currentSize;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
