package atm.impl.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMCellTypeImplTest {
    @Test
    void getNominal50() {
        assertEquals(ATMCellTypeImpl.N50.nominal(), 50);
    }

    @Test
    void getNominal100() {
        assertEquals(ATMCellTypeImpl.N100.nominal(), 100);
    }

    @Test
    void getNominal500() {
        assertEquals(ATMCellTypeImpl.N500.nominal(), 500);
    }

    @Test
    void getNominal1000() {
        assertEquals(ATMCellTypeImpl.N1000.nominal(), 1000);
    }

    @Test
    void getNominal2000() {
        assertEquals(ATMCellTypeImpl.N2000.nominal(), 2000);
    }

    @Test
    void getNominal5000() {
        assertEquals(ATMCellTypeImpl.N5000.nominal(), 5000);
    }
}