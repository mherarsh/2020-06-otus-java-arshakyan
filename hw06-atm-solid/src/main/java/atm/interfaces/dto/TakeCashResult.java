package atm.interfaces.dto;

import atm.interfaces.hardware.ATMCellType;

import java.util.Map;

public interface TakeCashResult {
    Map<ATMCellType, Integer> banknotes();
    Integer amount();
}
