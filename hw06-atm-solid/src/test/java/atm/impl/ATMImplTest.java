package atm.impl;

import atm.impl.enums.ATMCellTypeImpl;
import atm.interfaces.clients.ATMClient;
import atm.interfaces.clients.ATMIncasator;
import atm.interfaces.dto.TakeCashResult;
import atm.interfaces.hardware.ATM;
import atm.interfaces.hardware.ATMCellType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ATMImplTest {
    private final ATM atm = new ATMImpl(new ArrayList<>() {{
        add(ATMCellTypeImpl.N50);
        add(ATMCellTypeImpl.N100);
        add(ATMCellTypeImpl.N500);
        add(ATMCellTypeImpl.N1000);
        add(ATMCellTypeImpl.N2000);
        add(ATMCellTypeImpl.N5000);
    }});

    @Test
    void takeCashOk() {
        addBanknotes(atm, new HashMap<>() {{
            put(ATMCellTypeImpl.N50, 5);
            put(ATMCellTypeImpl.N100, 5);
            put(ATMCellTypeImpl.N1000, 5);
            put(ATMCellTypeImpl.N2000, 5);
            put(ATMCellTypeImpl.N5000, 5);
        }});

        var takeCashResult = takeCash(atm, 8350);

        assertEquals(takeCashResult.amount(), 8350);
    }

    @Test
    void takeCashNotEnough() {
        assertThrows(IllegalArgumentException.class, () -> {
            var takeCashResult = takeCash(atm, 8350);

            assertEquals(takeCashResult.amount(), 8350);
        });
    }

    @Test
    void takeCashBanknotesOK() {
        addBanknotes(atm, new HashMap<>() {{
            put(ATMCellTypeImpl.N50, 5);
            put(ATMCellTypeImpl.N100, 5);
            put(ATMCellTypeImpl.N1000, 5);
            put(ATMCellTypeImpl.N2000, 5);
            put(ATMCellTypeImpl.N5000, 5);
        }});

        var takeCashResult = takeCash(atm, 8350);

        assertEquals(
                takeCashResult.banknotes(),
                new HashMap<>() {{
                    put(ATMCellTypeImpl.N50, 1);
                    put(ATMCellTypeImpl.N100, 3);
                    put(ATMCellTypeImpl.N1000, 1);
                    put(ATMCellTypeImpl.N2000, 1);
                    put(ATMCellTypeImpl.N5000, 1);
                }});
    }

    @Test
    void getTotalSum() {
        var balance = addBanknotes(atm, new HashMap<>() {{
            put(ATMCellTypeImpl.N50, 1);
            put(ATMCellTypeImpl.N100, 1);
            put(ATMCellTypeImpl.N500, 1);
        }});

        assertEquals(balance, 650);
    }

    private TakeCashResult takeCash(ATMClient client, int amount) {
        return client.takeCash(amount);
    }

    private Integer addBanknotes(ATMIncasator incasator, Map<ATMCellType, Integer> banknotes) {
        if (incasator == null) {
            throw new NullPointerException("Incasator is null");
        }

        if (banknotes == null || banknotes.isEmpty()) {
            return null;
        }

        banknotes.forEach(incasator::addBanknote);

        return incasator.getBalance();
    }
}