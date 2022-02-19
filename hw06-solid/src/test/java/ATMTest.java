import com.otusproject.ATM;
import com.otusproject.ATMImpl;
import com.otusproject.Banknote;
import com.otusproject.CellImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ATMTest {
    private ATM atm;

    @BeforeEach
    public void init() {
        atm = new ATMImpl();
        atm.putMoney(
                new CellImpl(Banknote.ONE, 1000),
                new CellImpl(Banknote.FIVE, 500),
                new CellImpl(Banknote.TEN, 100),
                new CellImpl(Banknote.FIFTY, 50),
                new CellImpl(Banknote.HUNDRED, 10)
        );
    }

    @Test
    public void testPutMoney() {
        atm.putMoney(
                new CellImpl(Banknote.TEN, 9),
                new CellImpl(Banknote.HUNDRED, 1)
        );
        assertEquals(8000 + 90 + 100, atm.getBalanceAmount());
    }

    @Test
    public void testGetMoney() {
        int sum = 101;
        int balanceAmount = atm.getMoney(sum);
        assertEquals(sum, balanceAmount);

        sum = 10000;
        balanceAmount = atm.getMoney(sum);
        assertEquals(balanceAmount, 0);
    }

    @Test
    public void testGetBalanceAmount() {
        assertEquals(8000, atm.getBalanceAmount());
    }
}
