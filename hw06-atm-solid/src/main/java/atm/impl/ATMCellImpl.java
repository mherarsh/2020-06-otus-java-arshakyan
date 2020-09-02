package atm.impl;

import atm.interfaces.hardware.ATMCell;
import atm.interfaces.hardware.ATMCellType;

public class ATMCellImpl implements ATMCell {
    private final ATMCellType cellType;
    private Integer banknotesCount = 0;

    public ATMCellImpl(ATMCellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public ATMCellType cellType() {
        return cellType;
    }

    @Override
    public Integer nominal() {
        return cellType.nominal();
    }

    @Override
    public Integer balance() {
        return cellType.nominal() * banknotesCount;
    }

    @Override
    public void addBanknotes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("The count of banknotes must be positive");
        }

        banknotesCount += count;
    }

    @Override
    public void takeBanknotes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("The count of banknotes must be positive");
        }

        if(count > banknotesCount){
            throw new IllegalArgumentException("Not enough banknotes");
        }

        banknotesCount -= count;
    }
}
