package com.otusproject;

import java.util.TreeSet;

public class ATMImpl implements ATM {
    private ATMCells atmCells = new ATMCellsImpl();

    @Override
    public boolean putMoney(Cell... cells) {
        return atmCells.putCell(cells);
    }

    @Override
    public int getMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }

        ATMCells copyOfAllMoney = atmCells.clone();
        ATMCells map = calculate(amount, Banknote.HUNDRED.getValue(), new ATMCellsImpl(), copyOfAllMoney);

        if (map != null) {
            this.atmCells = copyOfAllMoney;
        }

        return map != null ? map.getBalanceAmount() : 0;
    }

    @Override
    public int getBalanceAmount() {
        return atmCells.getBalanceAmount();
    }

    private ATMCells calculate(int sum, int minValue, ATMCells result, ATMCells allMoney) {
        TreeSet<Banknote> orderedValues = Banknote.getOrderedValues();
        for (Banknote banknote: orderedValues) {
            int value = banknote.getValue();
            if (minValue >= value && sum >= value) {
                if (allMoney.getAmountByBanknote(banknote) > 0) {
                    result.addAmountByBanknote(banknote, 1);
                    allMoney.getAmountByBanknote(banknote, 1);
                    return calculate(sum - value, value, result, allMoney);
                } else if (banknote == Banknote.ONE) {
                    return null;
                } else {
                    Banknote higher = orderedValues.higher(banknote);
                    return calculate(sum, higher != null ? higher.getValue() : Banknote.ONE.getValue(), result, allMoney);
                }
            } else if (sum == 0) {
                return result;
            }
        }
        return null;
    }
}
