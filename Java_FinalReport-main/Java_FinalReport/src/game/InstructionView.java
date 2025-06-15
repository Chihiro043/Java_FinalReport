package game;

public class InstructionView {

    public static void showInstructions() {
        System.out.println("♠");
        System.out.println("你身負鉅額債務，走投無路下只能參與債務整理大會。");
        System.out.println("此次賭局遊戲為「雙重印地安撲克」，四名參賽者一組──"+GameUtils.RED);
        System.out.println(GameUtils.RED +"這是一場圍繞權力階級、忠誠與背叛、出千與偽裝的心理博弈，");
        System.out.println( GameUtils.RED +"你必須靠智慧與膽識，擊敗對手，奪回自由。" + GameUtils.RED) ;
        System.out.println("遊戲規則與操作說明");
        System.out.println("♦");
        System.out.println("------------------------------------");
        System.out.println("遊戲名稱：狂賭之淵：雙重印地安撲克 - 敗犬 or 主人");
        System.out.println("玩家人數：2人（2 名玩家 + 2 名電腦）");
        System.out.println("每人初始債務：10萬～3000萬（將影響籌碼面額）");
        System.out.println("每輪流程：");
        System.out.println("  1. 每人繳交出場費（1 枚籌碼）");
        System.out.println("  2. 每人發放 2 張手牌（明牌 + 暗牌）");
        System.out.println("  3. 顯示自己視角所見的卡牌（視出千情況可偷看）");
        System.out.println("  4. 輪流下注（1～5 枚，最多 all-in，也可棄牌不跟注）");
        System.out.println("  5. 所有人比牌，勝者獲得全部下注籌碼");
        System.out.println("  *若平手則退還所有人籌碼");
        System.out.println("出千說明：");
        System.out.println("  - 玩家可選擇是否出千，電腦預設皆出千");
        System.out.println("  - 出千有各自規則，視角色卡牌）");
        System.out.println("勝負方式：");
        System.out.println("  - 進行 10 輪後，依據持有籌碼總價值進行排名");
        System.out.println("------------------------------------");
        System.out.println("操作提示：");
        System.out.println("  - ENTER 鍵可切換視角、繼續下一輪");
        System.out.println("  - y/n 輸入用於決定是否出千等選項");
        System.out.println("  - 數字輸入代表下注籌碼數量（1～5,0）");
        System.out.println("====================================");
        System.out.println("♣");
        System.out.println(GameUtils.RED+"\n※ 選擇出千將冒更高風險，但可能獲得關鍵優勢。" +GameUtils.RED);
        System.out.println(GameUtils.RED+ "※ 若輸掉所有籌碼，將成為完全的敗犬。" +GameUtils.RED);
        System.out.println("===============================\n" ;
        System.out.println("♥");
    }
}
