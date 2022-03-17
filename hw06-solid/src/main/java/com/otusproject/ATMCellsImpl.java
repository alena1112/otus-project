package com.otusproject;

import java.util.*;

public class ATMCellsImpl implements ATMCells {
    private Map<Banknote, Cell> cells = new HashMap<>();

    public ATMCellsImpl() {
        Arrays.stream(Banknote.values()).forEach(banknote -> this.cells.put(banknote, new CellImpl(banknote)));
    }

    @Override
    public boolean putCell(Cell... cells) {
        Arrays.stream(cells).forEach(cell ->
                getCellByBanknote(cell.getBanknote()).addAmount(cell.getAmount())
        );
        return true;
    }

    @Override
    public int getBalanceAmount() {
        int result = 0;
        for (Cell cell : cells.values()) {
            result += cell.getBalanceAmount();
        }
        return result;
    }

    @Override
    public void addAmountByBanknote(Banknote banknote, int amount) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        getCellByBanknote(banknote).addAmount(amount);
    }

    @Override
    public void getAmountByBanknote(Banknote banknote, int amount) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        getCellByBanknote(banknote).getAmount(amount);
    }

    @Override
    public int getAmountByBanknote(Banknote banknote) {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote must not be null");
        }
        return getCellByBanknote(banknote).getAmount();
    }

    @Override
    public ATMCells clone() {
        ATMCellsImpl clone = new ATMCellsImpl();
        clone.cells = new HashMap<>();
        for (Map.Entry<Banknote, Cell> entry: this.cells.entrySet()) {
            clone.cells.put(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }

    private Cell getCellByBanknote(Banknote banknote) {
        return cells.get(banknote);
    }
}
