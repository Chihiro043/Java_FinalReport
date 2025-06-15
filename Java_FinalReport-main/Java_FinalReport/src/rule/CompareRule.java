package rule;
import java.util.List;
import model.Card;
import model.Player;

//處理牌型比較邏輯：對子 > 同花 > 單張，再比數字總和大小
public class CompareRule {

    /**
     * 比較兩名玩家的牌型勝負
     * @return >0 表示 p1 贏，<0 表示 p2 贏，=0 平手
     */
    public static int compare(Player p1, Player p2) {
        Card c1a = p1.getVisibleCard();
        Card c1b = p1.getHiddenCard();
        Card c2a = p2.getVisibleCard();
        Card c2b = p2.getHiddenCard();

        int type1 = getTypeScore(c1a, c1b);
        int type2 = getTypeScore(c2a, c2b);
        if (type1 != type2) return Integer.compare(type1, type2);

        int sum1 = c1a.getNumber() + c1b.getNumber();
        int sum2 = c2a.getNumber() + c2b.getNumber();
        return Integer.compare(sum1, sum2);
    }

    //回傳牌型分數：對子=3，同花=2，雜牌=1
    private static int getTypeScore(Card c1, Card c2) {
        if (c1.getNumber() == c2.getNumber()) return 3; // 對子
        if (c1.getSuit().equals(c2.getSuit())) return 2; // 同花
        return 1; // 雜牌
    }

    //回傳牌型名稱（可用於顯示用途）
    public static String getTypeName(Card c1, Card c2) {
        if (c1.getNumber() == c2.getNumber()) return "對子";
        if (c1.getSuit().equals(c2.getSuit())) return "同花";
        return "雜牌";
    }
    
    /**
     * 找出最強者，如果出現平手就回傳 null
     */
    public static Player findStrongestPlayer(List<Player> players) {
        if (players == null || players.isEmpty()) return null;

        Player best = players.get(0);
        boolean tie = false;

        for (int i = 1; i < players.size(); i++) {
            Player challenger = players.get(i);
            int result = compare(best, challenger);

            if (result < 0) {
                best = challenger;
                tie = false; // 有人贏就重設
            } else if (result == 0) {
                tie = true; // 有平手情況
            }
        }

        return tie ? null : best;
    }

}
