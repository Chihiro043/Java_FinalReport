package rule;

import model.Player;
import java.util.*;

/**
 * 處理出千狀態、視角與角色可見性。
 * 所有出千選擇應在遊戲一開始就設定，之後不可更改。
 */
public class CheatingRule {

    private static final Map<String, Boolean> cheatMap = new HashMap<>();

    // === 初始化階段 ===

    // 設定所有玩家是否出千（只能設定一次）
    public static void setupCheatingStatus(List<Player> allPlayers, Map<String, Boolean> userCheatingChoices) {
        for (Player p : allPlayers) {
            String id = p.getId();
            boolean cheat = p.isUserControlled()
                ? userCheatingChoices.getOrDefault(id, false)
                : true; // 電腦強制出千

            cheatMap.put(id, cheat);
            p.setCheating(cheat);
        }
    }

    // A 對抗 B 的服從或反目
    public static void applyResist(Player a, Player b, boolean aChoosesToResist) {
        if (a.getId().equals("A") && b.getId().equals("B")) {
            cheatMap.put("A", aChoosesToResist);
            a.setCheating(aChoosesToResist);
            System.out.println(aChoosesToResist
                ? "[反擊選擇] A 選擇反目出千，將反制 B。"
                : "[服從選擇] A 選擇服從，繼續受 B 控制。");
        }
    }

    // C 嘗試說服 D 合作出千（成功則交換申報金額）
    public static void applyPersuasion(Player sourceC, Player targetD, boolean dAccepts) {
        if (sourceC.getId().equals("C") && targetD.getId().equals("D")) {
            cheatMap.put("D", dAccepts);
            targetD.setCheating(dAccepts);
            System.out.println(dAccepts
                ? "[說服成功] C 說服 D 出千，建立共享視角，調換申報之債務金額。"
                : "[說服失敗] D 拒絕出千，無法共享視角，不調換申報之債務金額。");
        }
    }

    // === 狀態查詢與邏輯判定 ===

    public static boolean isCheating(Player player) {
        return cheatMap.getOrDefault(player.getId(), false);
    }

    // CheatingRule.java

    public static boolean canSeeHiddenCard(Player viewer, Player target) {
        if (viewer.getId().equals(target.getId())) return true;
    
        if (!isCheating(viewer)) return false;
    
        String v = viewer.getId();
        String t = target.getId();
    
        // 雙向合作（C <-> D）
        if ((v.equals("C") && t.equals("D")) || (v.equals("D") && t.equals("C"))) return true;
    
        // B 偷看 A，但 A 沒有反目（還是棋子）
        if (v.equals("B") && t.equals("A") && !isCheating(target)) return true;
    
        return false;
    }


    public static boolean isDetectedByViewer(Player target, Player viewer) {
        String t = target.getId();
        String v = viewer.getId();
        return (v.equals("B") && t.equals("A")) || (v.equals("C") && t.equals("D"));
    }

    public static boolean isInCooperativeCheating(Player p1, Player p2) {
        String id1 = p1.getId();
        String id2 = p2.getId();

        if (!isCheating(p1) || !isCheating(p2)) return false;

        return (id1.equals("C") && id2.equals("D")) || (id1.equals("D") && id2.equals("C"));
    }

    // === 進階行為處理 ===

    // 調換申報金額（僅限 C 與 D 合作時）
    public static void swapDeclaredAmountIfCooperative(Player p1, Player p2) {
        if (isInCooperativeCheating(p1, p2)) {
            int tmp = p1.getDeclaredAmount();
            p1.setDeclaredAmount(p2.getDeclaredAmount());
            p2.setDeclaredAmount(tmp);
            System.out.printf("[交換申報] %s 與 %s 調換申報金額%n", p1.getId(), p2.getId());
        }
    }

    // 虛假申報:出千者會使用「調換後的」申報金額（應由 Game 在合作後即設定完成）
    public static int getDeclaredAmountWithCheating(Player player) {
        return player.getDeclaredAmount();
    }

}
