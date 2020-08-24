package atm.interfaces.clients;

import atm.interfaces.dto.TakeCashResult;

public interface ATMClient {
    TakeCashResult takeCash(Integer amount);

    Integer getBalance();
}
