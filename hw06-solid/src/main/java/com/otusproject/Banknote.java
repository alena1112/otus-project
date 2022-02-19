package com.otusproject;

import java.util.Arrays;
import java.util.TreeSet;

public enum Banknote {
    ONE(1),
    FIVE(5),
    TEN(10),
    FIFTY(50),
    HUNDRED(100);

    private final int value;
    private static TreeSet<Banknote> orderedBanknotes;

    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TreeSet<Banknote> getOrderedValues() {
        if (orderedBanknotes == null) {
            orderedBanknotes = new TreeSet<>((b1, b2) -> b2.getValue() - b1.getValue());
            orderedBanknotes.addAll(Arrays.asList(Banknote.values()));
        }
        return orderedBanknotes;
    }
}
