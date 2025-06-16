package game;

import model.*;
import rule.*;
import java.util.*;

public class Game {

    private static final int TOTAL_ROUNDS = 10;
    public static void startGame() {
        System.out.println(GameUtils.RED+"債務整理大會－雙重印地安撲克"+GameUtils.RESET);
        
        // 規則展示
        InstructionView.showInstructions();
        System.out.print("（請按下 ENTER 繼續）");
        new Scanner(System.in).nextLine();
        
        // 1. 初始化角色與玩家（自訂名稱）
        List<Player> players = initializePlayers();
     
        // 2. 顯示玩家抽到的角色並設定名稱
        System.out.println("\n由系統已隨機分配角色（A~D)");
        Scanner sc = new Scanner(System.in);
        for (Player p : players) {
            if (p.isUserControlled()) {
                System.out.print("你抽到的是角色 " + p.getId() + "，請輸入名稱：");
                p.setName(sc.nextLine());
            } else {
                p.setName("電腦" + p.getId());
            }
        }

        // 3. 設定債務（含 C、D 調換邏輯）
        for (Player p : players) {
            int debt = DebtRule.getDebtForRole(p.getId());
            p.setDeclaredAmount(debt);
            p.setDebt(debt);
        }

        System.out.println("\n本場角色分配與債務金額如下：");
        for (Player p : players) {
            System.out.printf("→ [%s] %s：債務 %d 元，初始籌碼單價：%d 元\n",
                p.getId(), p.getName(), p.getDebt(), p.getDebt() / 10);
        }
        System.out.println();


        // 4. 出千設定
        Map<String, Boolean> userCheatingChoices = new HashMap<>();
        for (Player p : players) {
            if (p.isUserControlled()) {
                System.out.print("玩家" + p.getName() + " 是否出千？(y/n)：");
                String input = sc.nextLine().trim().toLowerCase();
                userCheatingChoices.put(p.getId(), input.equals("y"));
            }
        }
        CheatingRule.setupCheatingStatus(players, userCheatingChoices);

        // 5. 特殊說服與反抗邏輯
        Player a = findById(players, "A");
        Player b = findById(players, "B");
        Player c = findById(players, "C");
        Player d = findById(players, "D");

        if (a.isUserControlled()) {
            System.out.print("A 是否選擇反抗 B（出千）？(y/n)：");
            boolean resist = sc.nextLine().trim().equalsIgnoreCase("y");
            CheatingRule.applyResist(a, b, resist);
        }
        else {
            CheatingRule.applyResist(a, b, false); // 電腦 A 自動服從 B
        }

        if (c.isUserControlled()) {
            System.out.print("你是 C，是否要說服 D 一起出千？(y/n)：");
            boolean persuade = sc.nextLine().trim().equalsIgnoreCase("y");
            CheatingRule.applyPersuasion(c, d, persuade);
        }

        // 6. 若合作出千成功，進行申報金額調換
        CheatingRule.swapDeclaredAmountIfCooperative(c, d);

        // 7. 初始化籌碼
        for (Player p : players) {
            ChipRule.initializeChips(p);
        }

        // 8. 顯示角色卡片資訊
        for (Player p : players) {
            if (p.isUserControlled()) {
                PlayerProfile.printRoleCard(p, players);
            }
        }

        // 9. 遊戲回合流程（10 輪，莊家輪替）
        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            int dealerIndex = (round - 1) % players.size();
            Round.run(players, round, dealerIndex);
        }

        // 10. 結算與排名
        List<Player> ranked = GameUtils.sortPlayersByChipValue(players);

        System.out.println("\n遊戲結算：依照籌碼總價值進行排序");
        for (int i = 0; i < ranked.size(); i++) {
            Player p = ranked.get(i);
            int value = ChipRule.getCurrentValue(p);
            System.out.printf("%d. %s（角色 %s）：%d 元%n", i + 1, p.getName(), p.getId(), value);
        }

        // 11. 劇情演出（可依排名擴充）
        showEndingDialogue(ranked);
    }

    private static List<Player> initializePlayers() {
        // 1. 建立兩位使用者與兩位電腦角色（先給暫時 ID）
        List<Player> userPlayers = new ArrayList<>();
        List<Player> aiPlayers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            userPlayers.add(new Player("?", "P" + (i + 1), 0, true));
            aiPlayers.add(new Player("?", "AI" + (i + 1), 0, false));
        }

        // 2. 隨機決定玩家使用哪一派（AB 或 CD）
        boolean userIsAB = new Random().nextBoolean();

        // 3. 使用 FactionAssigner 指派角色 ID
        FactionAssigner.assignPlayerFaction(userPlayers, aiPlayers, userIsAB);

        // 4. 合併為一個完整列表並回傳
        List<Player> all = new ArrayList<>();
        all.addAll(userPlayers);
        all.addAll(aiPlayers);

        System.out.println("系統已隨機將你分配至「" + (userIsAB ? "A+B 派（支配者&棋子）" : "C+D 派（脫序者）") + "」");
        return all;
    }


    private static Player findById(List<Player> list, String id) {
        return list.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    private static void showEndingDialogue(List<Player> ranked) {
        Player first = ranked.get(0);
        Player last = ranked.get(ranked.size() - 1);

        System.out.println(first.getId() + "（" + first.getName() + "）：" + "這就是勝利者的姿態...!!");
        System.out.println(last.getId() + "（" + last.getName() + "）：" + "竟然變成敗犬了嘛!?!?");
        System.out.println("\n-\n這場賭局落幕，但你還能承受下一場嗎？");
    }
}
