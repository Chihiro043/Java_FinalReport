package game;

public class InstructionView {

    public static void showInstructions() {
        System.out.println("遊戲規則與操作說明");
        System.out.println("------------------------------------");
        System.out.println("遊戲名稱：狂賭之淵：雙重印地安撲克 - 敗犬 or 主人");
        System.out.println("玩家人數：4 人（最多 2 名玩家 + 2 名電腦）");
        System.out.println("每人初始債務：10萬～3000萬（將影響籌碼面額）");
        System.out.println("每輪流程：");
        System.out.println("  1. 每人繳交出場費（1 枚籌碼）");
        System.out.println("  2. 每人發放 2 張手牌（明牌 + 暗牌）");
        System.out.println("  3. 顯示自己視角所見的卡牌（視出千情況可偷看）");
        System.out.println("  4. 輪流下注（1～5 枚，最多 all-in，也可棄牌不跟注）");
        System.out.println("  5. 所有人比牌，勝者獲得全部下注籌碼");
        System.out.println("  6. 若平手則所有人失去籌碼（或退還，依規則）");
        System.out.println("出千說明：");
        System.out.println("  - 玩家可選擇是否出千，電腦預設皆出千");
        System.out.println("  - 出千可偷看對方暗牌（依角色視角）");
        System.out.println("  - 特殊角色間可交換資訊與調換申報債務（C 與 D）");
        System.out.println("勝負方式：");
        System.out.println("  - 進行 10 輪後，依據持有籌碼總價值進行排名");
        System.out.println("------------------------------------");
        System.out.println("操作提示：");
        System.out.println("  - ENTER 鍵可切換視角、繼續下一輪");
        System.out.println("  - y/n 輸入用於決定是否出千等選項");
        System.out.println("  - 數字輸入代表下注籌碼數量（1～5）");
        System.out.println("====================================");
    }
}
