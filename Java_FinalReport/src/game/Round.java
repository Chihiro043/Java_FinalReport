package game;

import model.*;
import rule.*;
import java.util.*;
import java.util.Scanner;

public class Round {

    public static void run(List<Player> players, int roundNum,int dealerIndex) {
        System.out.println("\n========== 第 " + roundNum + " 輪開始 ==========");

        // 1. 更新每位玩家的回合狀態
        System.out.println("[更新] 本回合初始資訊");
        for (Player p : players) {
            p.setCurrentRound(roundNum);
            p.resetForNewRound();
        }
        for (Player p : players) {
            if (p.isUserControlled()) {
                System.out.println("\n玩家 [" + p.getName() + "] 狀態總覽：");
                p.showInfo();
            }
        }
        
        // 2. 繳交出場費（ante）
        System.out.println("[繳費] 每人繳交 1 枚籌碼作為參賽費");
        List<Chip> pot = new ArrayList<>();
        for (Player p : players) {
            List<Chip> ante = ChipRule.payAnte(p);
            pot.addAll(ante);
        }

        // 3. 洗牌並發牌
        System.out.println("[發牌] 荷官洗牌並發放每人兩張卡（明+暗）");
        Deck deck = new Deck();
        for (Player p : players) p.setVisibleCard(deck.drawCard());
        for (Player p : players) p.setHiddenCard(deck.drawCard());

        // 4. 顯示所有玩家卡牌視角（GameView）
        System.out.println("[看牌] 顯示當前可見手牌");
        for (Player p : players) {
            if (p.isUserControlled()) {
            	 System.out.println("\n【" + p.getName() + " 的視角】");
                GameView.showCardView(p, players);
                System.out.print("（按下 Enter 切換下一位玩家視角）");
                new Scanner(System.in).nextLine();
            }
        }

        // 5. 下注階段
        Player dealer = players.get(dealerIndex);
        System.out.println("本輪莊家為：" + dealer.getName()+"開始下注:");
        System.out.println("[下注] 輪流進行下注");
        Map<Player, List<Chip>> playerBets = new HashMap<>();
        for (Player p : players) {
            List<Chip> bet = p.isUserControlled()
                ? askUserBet(p)
                : PCcontroll.decideBet(p, GameUtils.buildContext(p, players, roundNum));
            playerBets.put(p, bet);
            pot.addAll(bet);
        }
        //檢查是否只有莊家下注
        List<Player> activeBettors = new ArrayList<>();
        for (Map.Entry<Player, List<Chip>> entry : playerBets.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                activeBettors.add(entry.getKey());
            }
        }

        if (activeBettors.size() == 1 && activeBettors.get(0).equals(dealer)) {
            System.out.println("無人跟注，莊家 " + dealer.getName() + " 自動獲勝");
            ChipRule.rewardWinner(dealer, pot);
            return;
        }
        System.out.println("\n本輪下注統計：");
        for (Map.Entry<Player, List<Chip>> entry : playerBets.entrySet()) {
            Player p = entry.getKey();
            int amount = ChipRule.totalValue(entry.getValue());
            if (amount > 0) {
                System.out.println("→ " + p.getName() + " 下注 " + entry.getValue().size() + " 枚（共值 " + amount + " 元）");
            } else {
                System.out.println("→ " + p.getName() + " 棄牌");
            }
        }
        
        //6. 揭露手牌
        System.out.println("\n 所有玩家的手牌揭曉：");
        for (Player p : players) {
            GameView.showSinglePlayerHand(p);  // 顯示每人明牌與暗牌
        }

        // 7. 比牌
        System.out.println("[比牌] 開始比牌，決定勝負");
        Player winner = CompareRule.findStrongestPlayer(players);
        if (winner != null) {
            System.out.println("本輪勝者：" + winner.getName() + " 獲得本輪所有籌碼");
            ChipRule.rewardWinner(winner, pot);
        } else {
            System.out.println("本輪為平手，退還各自下注籌碼");
            ChipRule.rewardDraw(pot, players);
        }
        
        //7. 揭露手牌
        System.out.println("\n 所有玩家的手牌揭曉：");
        for (Player p : players) {
            GameView.showSinglePlayerHand(p);  // 顯示每人明牌與暗牌
        }


        System.out.println("========== 第 " + roundNum + " 輪結束 ==========\n");
        System.out.print("請按下 ENTER 繼續下一輪...");
        new Scanner(System.in).nextLine();
    }

    private static List<Chip> askUserBet(Player user) {
        Scanner scanner= new Scanner(System.in);
        int num = 0;
        while (true) {
            System.out.print(user.getName() + "，請輸入本輪下注籌碼數量（1～5）：");
            try {
                num = Integer.parseInt(scanner.nextLine());
                if (ChipRule.canBet(user, num)) break;
                System.out.println(" 數量非法/籌碼不足，請重新輸入。");
            } catch (Exception e) {
                System.out.println("請輸入合法整數！");
            }
        }
        return ChipRule.bet(user, num);
    }
}
