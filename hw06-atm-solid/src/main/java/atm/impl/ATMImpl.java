package atm.impl;

import atm.impl.dto.TakeCashResultImpl;
import atm.interfaces.hardware.ATM;
import atm.interfaces.hardware.ATMCell;
import atm.interfaces.hardware.ATMCellType;
import atm.interfaces.dto.TakeCashResult;

import java.util.*;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {
    private final String invalidAmountMessage;
    private final Map<ATMCellType, ATMCell> atmCells = new HashMap<>();

    public ATMImpl(List<ATMCellType> atmCellTypes) {
        if (atmCellTypes == null || atmCellTypes.isEmpty()) {
            throw new IllegalArgumentException("ATM cell types is null or empty");
        }

        createAtmCells(atmCellTypes);
        invalidAmountMessage = getInvalidAmountMessage();
    }

    @Override
    public TakeCashResult takeCash(Integer amount) {
        if (!isValidAmount(amount)) {
            throw new IllegalArgumentException(invalidAmountMessage);
        }

        if (amount > getBalance()) {
            throw new IllegalArgumentException("Not enough cash to pay out");
        }

        return new TakeCashResultImpl(getBanknotesMap(amount));
    }

    @Override
    public Integer getBalance() {
        return atmCells.values().stream()
                .map(ATMCell::balance)
                .reduce(0, Integer::sum);
    }

    @Override
    public void addBanknote(ATMCellType atmCellType, Integer count) {
        if (!atmCells.containsKey(atmCellType)) {
            throw new IllegalArgumentException("Cell with nominal " + atmCellType.nominal() + " not found");
        }

        atmCells.get(atmCellType).addBanknotes(count);
    }

    private void createAtmCells(List<ATMCellType> atmCellTypes) {
        atmCellTypes.forEach(cellType -> atmCells.put(cellType, new ATMCellImpl(cellType)));
    }

    private Map<ATMCellType, Integer> getBanknotesMap(Integer amount) {
        var needAmount = amount;
        var banknotesMap = new HashMap<ATMCellType, Integer>();

        ATMCell[] positiveSells = getPositiveCellsSortedByNominal();

        for (var atmCell : positiveSells) {
            var banknotesCount = needAmount / atmCell.nominal();
            if (banknotesCount > 0) {
                atmCell.takeBanknotes(banknotesCount);
                banknotesMap.put(atmCell.cellType(), banknotesCount);
                needAmount -= atmCell.nominal() * banknotesCount;

                if (needAmount == 0) {
                    return banknotesMap;
                }
            }
        }

        throw new IllegalArgumentException("Not enough cash to pay out");
    }

    private ATMCell[] getPositiveCellsSortedByNominal() {
        return atmCells.values().stream()
                .filter(x -> x.balance() > 0)
                .sorted(Comparator.comparing(ATMCell::nominal).reversed())
                .toArray(ATMCell[]::new);
    }

    private String getInvalidAmountMessage() {
        var atmBanknotesString = atmCells.keySet().stream()
                .sorted(Comparator.comparing(ATMCellType::nominal))
                .map(x -> x.nominal().toString())
                .collect(Collectors.joining(", "));

        return "Amount must be positive and multiples of the following nominal : " + atmBanknotesString;
    }

    private boolean isValidAmount(Integer amount) {
        if (amount <= 0) {
            return false;
        }

        for (var cell : atmCells.entrySet()) {
            if (amount % cell.getKey().nominal() == 0) {
                return true;
            }
        }

        return false;
    }
}
