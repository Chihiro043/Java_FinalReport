package rule;
import java.util.Random;

public class DebtRule {

    private static final int MIN_DEBT = 100000;
    private static final int MAX_DEBT = 30000000;
    private static final int STEP = 10000;
    private static final Random random = new Random();

    //隨機產生一筆合法債務金額（100000 ~ 30000000 間的 10000 倍數）
    public static int generateValidDebt() {
        int range = (MAX_DEBT - MIN_DEBT) / STEP + 1;
        return MIN_DEBT + random.nextInt(range) * STEP;
    }

    public static boolean isValidDebt(int amount) {
        return amount >= MIN_DEBT && amount <= MAX_DEBT && amount % STEP == 0;
    }
} 
