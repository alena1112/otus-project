package com.otusproject;

public interface Cell {
    Banknote getBanknote();

    int getAmount();

    void addAmount(int amount);

    void getAmount(int amount);

    int getBalanceAmount();

    Cell clone();
}
