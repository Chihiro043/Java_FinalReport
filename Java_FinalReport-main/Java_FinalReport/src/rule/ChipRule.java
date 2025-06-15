package rule;

import model.Chip;
import model.Player;
import java.util.*;

//管理籌碼初始化、下注、來源統計、分配等邏輯。
public class ChipRule {

    private static final int MAX_BET_PER_ROUND = 5;

    // ========== 初始化邏輯 ==========

    //初始化玩家籌碼：固定 10 枚，面額 = 債務 / 10。
    public static void initializeChips(Player player) {
        player.getChips().clear();
        int value = player.getDebt() / 10;
        for (int i = 0; i < 10; i++) {
            player.getChips().add(new Chip(player.getId(), value));
        }
    }

    // ========== 下注邏輯 ==========

    // 每局強制繳交出場費（ante）：1 枚籌碼。
    public static List<Chip> payAnte(Player player) {
        return bet(player, 1);
    }

    //是否可下注指定枚數（最多 5，且不超過手上籌碼）。
    public static boolean canBet(Player player, int num) {
        return num > 0 && num <= MAX_BET_PER_ROUND && num <= player.getChips().size();
    }

    // 是否符合 All-in：持有籌碼 <= 5，且 > 0。
    public static boolean canAllIn(Player player) {
        int count = player.getChips().size();
        return count > 0 && count <= MAX_BET_PER_ROUND;
    }

    // 執行下注：從玩家籌碼中取出 num 枚。
    public static List<Chip> bet(Player player, int num) {
        List<Chip> out = new ArrayList<>();
        if (!canBet(player, num)) return out;
        Iterator<Chip> it = player.getChips().iterator();
        while (it.hasNext() && out.size() < num) {
            out.add(it.next());
            it.remove();
        }
        return out;
    }

    // ========== 特定來源下注 ==========

    //從指定角色來源下注 num 枚籌碼。
    public static List<Chip> betFrom(Player player, int num, String ownerId) {
        List<Chip> taken = new ArrayList<>();
        Iterator<Chip> it = player.getChips().iterator();
        while (it.hasNext() && taken.size() < num) {
            Chip c = it.next();
            if (c.getOwnerId().equals(ownerId)) {
                taken.add(c);
                it.remove();
            }
        }
        return taken;
    }

    // ========== 籌碼統計 ==========

    //計算一組籌碼總面額。
    public static int totalValue(List<Chip> chips) {
        return chips.stream().mapToInt(Chip::getValue).sum();
    }

    //計算玩家手中所有籌碼的總價值。
    public static int getCurrentValue(Player player) {
        return totalValue(player.getChips());
    }

    // 是否擁有某角色的籌碼。
    public static boolean hasChipFrom(Player player, String ownerId) {
        return player.getChips().stream().anyMatch(c -> c.getOwnerId().equals(ownerId));
    }

    //計算某來源籌碼的「數量」。
    public static int countChipFrom(Player player, String ownerId) {
        return (int) player.getChips().stream()
                .filter(c -> c.getOwnerId().equals(ownerId))
                .count();
    }
    
    // 計算來自某位角色的籌碼總價值。
    public static int totalValueFrom(Player player, String ownerId) {
        return player.getChips().stream()
                .filter(c -> c.getOwnerId().equals(ownerId))
                .mapToInt(Chip::getValue)
                .sum();
    }


    // 回傳來源角色分佈：{A=3, B=2, ...}
    public static Map<String, Integer> sourceBreakdown(Player player) {
        Map<String, Integer> map = new HashMap<>();
        for (Chip chip : player.getChips()) {
            map.merge(chip.getOwnerId(), 1, Integer::sum);
        }
        return map;
    }

    // ========== 勝利分配 ==========

    //勝者獲得獎池中所有籌碼，並列印來源明細與總價值。
    public static void rewardWinner(Player winner, List<Chip> pot) {
        if (winner == null || pot == null || pot.isEmpty()) return;

        winner.getChips().addAll(pot);

        System.out.println("\n🏆 " + winner.getName() + " 獲得本輪勝利");
        
        // 統計來源
        Map<String, List<Chip>> sourceMap = new HashMap<>();
        for (Chip chip : pot) {
            sourceMap.computeIfAbsent(chip.getOwnerId(), k -> new ArrayList<>()).add(chip);
        }

        int total = 0;
        for (Map.Entry<String, List<Chip>> entry : sourceMap.entrySet()) {
            String owner = entry.getKey();
            List<Chip> chips = entry.getValue();
            int count = chips.size();
            int value = totalValue(chips);
            total += value;
            System.out.println("→ 來自 " + owner + " 的籌碼：" + count + " 枚，總價值：" + value + " 元");
        }

        System.out.println("💰 本輪獲得籌碼總價值：" + total + " 元\n");

        pot.clear();
        
        
    }
 // 平手時退還所有下注籌碼給原持有人
    public static void rewardDraw(List<Chip> pot, List<Player> players) {
        if (pot == null || pot.isEmpty()) return;

        System.out.println("平手：所有下注籌碼已退還原玩家。");

        for (Chip chip : pot) {
            for (Player p : players) {
                if (p.getId().equals(chip.getOwnerId())) {
                    p.getChips().add(chip);
                    break;
                }
            }
        }
        pot.clear(); // 清空桌面籌碼池
    }


}
