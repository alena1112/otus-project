package com.otusproject;

public interface ATMCells {
    boolean putCell(Cell... cells);

    int getBalanceAmount();

    void addAmountByBanknote(Banknote banknote, int amount);

    void getAmountByBanknote(Banknote banknote, int amount);

    int getAmountByBanknote(Banknote banknote);

    ATMCells clone();
}

