package atm.interfaces.hardware;

public interface ATMCell {
    ATMCellType cellType();
    Integer nominal();
    Integer balance();
    void addBanknotes(int count);
    void takeBanknotes(int count);
}
