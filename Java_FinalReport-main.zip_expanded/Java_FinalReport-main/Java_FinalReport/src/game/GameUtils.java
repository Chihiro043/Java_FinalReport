package game;

import model.Card;
import model.Player;
import rule.CheatingRule;
import rule.ChipRule;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class GameUtils {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    public static void printColor(String text, String color) {
        System.out.println(color + text + RESET);
    }

    public static void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public static void clearPlayerInfoAsGarbage(Player player) {
        Random rand = new Random();
        String chars = "@#%&*!?ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        System.out.println("\n【已結束視角，以下為亂碼資訊保護】");

        // 假裝亂碼債務與籌碼資訊
        System.out.print("債務金額：");
        printGarbageLine(1, chars, rand);
        System.out.print("籌碼總數：");
        printGarbageLine(1, chars, rand);

        // 假裝亂碼明牌與暗牌
        System.out.println("明牌：");
        printGarbageLine(1, chars, rand);
        System.out.println("暗牌：");
        printGarbageLine(1, chars, rand);

        System.out.println("\n【請將裝置交給下一位玩家】");
    }

    private static void printGarbageLine(int lines, String chars, Random rand) {
        for (int i = 0; i < lines; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 20; j++) {
                sb.append(chars.charAt(rand.nextInt(chars.length())));
            }
            System.out.println("　" + sb);
        }
    }

    //構建電腦控制之角色所需的即時資訊（GameContext）。
    public static GameContext buildContext(Player self, List<Player> allPlayers, int roundNumber) {
        GameContext ctx = new GameContext(self);
        ctx.roundNumber = roundNumber;
        ctx.myChipCount = self.getChipCount();
        ctx.myTotalValue = self.getTotalChipValue();
        ctx.myCardScore = evaluateCardScore(self.getVisibleCard(), self.getHiddenCard());
        ctx.rank = calculateRank(self, allPlayers);

        // 尋找一位主對手（非自己）做為出千參考目標
        Player opponent = allPlayers.stream()
            .filter(p -> !p.getId().equals(self.getId()))
            .findFirst()
            .orElse(null);

        ctx.hasCheatInfo = opponent != null && CheatingRule.canSeeHiddenCard(self, opponent);
        ctx.enemyCardScore = ctx.hasCheatInfo ? evaluateCardScore(opponent.getVisibleCard(), opponent.getHiddenCard()) : 0;
        ctx.opponentBet = 1; 
        return ctx;
    }

    //根據玩家籌碼總價值對所有玩家排序後回傳名次
    public static int calculateRank(Player target, List<Player> all) {
        List<Player> sorted = all.stream()
            .sorted(Comparator.comparingInt(Player::getTotalChipValue).reversed())
            .toList();
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getId().equals(target.getId())) {
                return i + 1;
            }
        }
        return all.size();
    }
    public static List<Player> sortPlayersByChipValue(List<Player> players) {
        return players.stream()
            .sorted(Comparator.comparingInt(Player::getTotalChipValue).reversed())
            .toList();
    }


    //將兩張牌轉換為 0~10 的強度分數：（對子>同花>散牌）
    public static int evaluateCardScore(Card open, Card hidden) {
        if (open == null || hidden == null) return 0;

        if (open.getNumber() == hidden.getNumber()) {
            return 10;
        } else if (open.getSuit().equals(hidden.getSuit())) {
            return 7 + Math.max(open.getNumber(), hidden.getNumber()) / 3; // 同花
        } else {
            return Math.max(open.getNumber(), hidden.getNumber());
        }
    }
  
    //是否屬於 AB 派（支配者）
    public static boolean isABFaction(String id) {
        return id.equals("A") || id.equals("B");
    }

    //是否屬於 CD 派（脫序者）
    public static boolean isCDFaction(String id) {
        return id.equals("C") || id.equals("D");
    }

    //判斷兩個角色是否同一陣營
    public static boolean isSameFaction(Player a, Player b) {
        return (isABFaction(a.getId()) && isABFaction(b.getId())) ||
               (isCDFaction(a.getId()) && isCDFaction(b.getId()));
    }
}
