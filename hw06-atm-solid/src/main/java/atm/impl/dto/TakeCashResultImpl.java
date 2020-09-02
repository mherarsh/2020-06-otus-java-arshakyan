package atm.impl.dto;

import atm.interfaces.dto.TakeCashResult;
import atm.interfaces.hardware.ATMCellType;

import java.util.HashMap;
import java.util.Map;

public class TakeCashResultImpl implements TakeCashResult {
    private final Map<ATMCellType, Integer> banknotesMap;
    private final int amount;

    public TakeCashResultImpl(Map<ATMCellType, Integer> banknotesMap) {
        this.banknotesMap = banknotesMap;
        this.amount = calculateAmount();
    }

    @Override
    public Map<ATMCellType, Integer> banknotes() {
        return new HashMap<>() {{
            putAll(banknotesMap);
        }};
    }

    @Override
    public Integer amount() {
        return amount;
    }

    private Integer calculateAmount() {
        return banknotesMap.entrySet().stream()
                .map(x -> x.getKey().nominal() * x.getValue())
                .reduce(0, Integer::sum);
    }
}
