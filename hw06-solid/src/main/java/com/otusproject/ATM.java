package com.otusproject;

public interface ATM {
    boolean putMoney(Cell... cells);

    int getMoney(int amount);

    int getBalanceAmount();
}
