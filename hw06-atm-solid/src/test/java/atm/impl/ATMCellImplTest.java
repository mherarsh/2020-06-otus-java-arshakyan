package atm.impl;

import atm.impl.enums.ATMCellTypeImpl;
import atm.interfaces.hardware.ATMCell;
import atm.interfaces.hardware.ATMCellType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMCellImplTest {
    private final ATMCellType cellType = ATMCellTypeImpl.N50;
    private final ATMCell atmCell = new ATMCellImpl(cellType);

    @Test
    void cellType() {
        assertEquals(cellType, atmCell.cellType());
    }

    @Test
    void nominal() {
        assertEquals(cellType.nominal(), atmCell.nominal());
    }

    @Test
    void balance() {
        var banknotesCount = 10;
        atmCell.addBanknotes(banknotesCount);

        assertEquals(atmCell.balance(), cellType.nominal() * banknotesCount);
    }

    @Test
    void addBanknotes() {
        var banknotesCount = 10;
        atmCell.addBanknotes(banknotesCount);

        assertEquals(atmCell.balance(), cellType.nominal() * banknotesCount);
    }

    @Test
    void takeBanknotes() {
        var banknotesCount = 10;
        atmCell.addBanknotes(banknotesCount);

        var takeCount = 3;
        atmCell.takeBanknotes(takeCount);

        banknotesCount -= takeCount;

        assertEquals(atmCell.balance(), cellType.nominal() * banknotesCount);
    }
}