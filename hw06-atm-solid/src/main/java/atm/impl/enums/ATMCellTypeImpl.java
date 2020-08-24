package atm.impl.enums;

import atm.interfaces.hardware.ATMCellType;

public enum ATMCellTypeImpl implements ATMCellType {
    N50(50),
    N100(100),
    N500(500),
    N1000(1000),
    N2000(2000),
    N5000(5000);

    private final int nominal;

    ATMCellTypeImpl(int nominal) {
        this.nominal = nominal;
    }

    @Override
    public Integer nominal() {
        return nominal;
    }
}
