package com.otusproject;

public class CellImpl implements Cell {
    private final Banknote banknote;
    private int amount;

    public CellImpl(Banknote banknote) {
        this.banknote = banknote;
    }

    public CellImpl(Banknote banknote, int amount) {
        this.banknote = banknote;
        this.amount = amount;
    }

    @Override
    public Banknote getBanknote() {
        return banknote;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void addAmount(int amount) {
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        this.amount += amount;
    }

    @Override
    public void getAmount(int amount) {
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        this.amount -= amount;
    }

    @Override
    public int getBalanceAmount() {
        return banknote.getValue() * amount;
    }

    @Override
    public CellImpl clone() {
        return new CellImpl(banknote, amount);
    }
}
