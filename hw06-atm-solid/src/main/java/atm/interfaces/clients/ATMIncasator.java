package atm.interfaces.clients;

import atm.interfaces.hardware.ATMCellType;

public interface ATMIncasator {
    void addBanknote(ATMCellType atmCellType, Integer count);

    Integer getBalance();
}
