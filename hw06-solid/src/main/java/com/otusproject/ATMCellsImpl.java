package com.otusproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ATMCellsImpl implements ATMCells {
    private List<Cell> cells = new ArrayList<>();

    public ATMCellsImpl() {
        Arrays.stream(Banknote.values()).forEach(banknote -> this.cells.add(new CellImpl(banknote)));
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
        for (Cell cell : cells) {
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
        clone.cells = new ArrayList<>();
        for (Cell cell: this.cells) {
            clone.cells.add(cell.clone());
        }
        return clone;
    }

    private Cell getCellByBanknote(Banknote banknote) {
        return cells.stream()
                .filter(cell -> banknote.equals(cell.getBanknote()))
                .findAny().get();
    }
}
