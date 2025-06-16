package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String id;               // A, B, C, D
    private String name;             // 玩家可自訂顯示名稱
    private int debt;                // 初始賭債金額（不可變動）
    private List<Chip> chips;        // 當前持有籌碼清單
    private Card forheadCard;        // 明牌
    private Card hiddenCard;         // 暗牌
    private boolean isUserControlled;// 是否為使用者控制
    private boolean isCheating;      // 是否選擇出千
    private int currentRound;        // 當前回合數（由 Game 控制遞增）

    public Player(String id, String name, int debt, boolean isUserControlled) {
        this.id = id;
        this.name = name;
        this.debt = debt;
        this.isUserControlled = isUserControlled;
        this.chips = new ArrayList<>();
        this.isCheating = false;
        this.currentRound = 1;
    }

    // ===== 基本屬性存取 =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDebt() { return debt; }

    public boolean isUserControlled() { return isUserControlled; }

    public boolean isCheating() { return isCheating; }
    public void setCheating(boolean isCheating) { this.isCheating = isCheating; }

    public int getCurrentRound() { return currentRound; }
    public void setCurrentRound(int round) { this.currentRound = round; }

    // ===== 籌碼管理 =====
    public List<Chip> getChips() { return chips; }

    public int getChipCount() {
        return chips.size();
    }

    public void setChips(List<Chip> newChips) {
        chips.clear();
        chips.addAll(newChips);
    }

    public int getTotalChipValue() {
        int sum = 0;
        for (Chip c : chips) {
            sum += c.getValue();
        }
        return sum;
    }

    public Map<String, Integer> getChipSourceSummary() {
        Map<String, Integer> sourceCount = new HashMap<>();
        for (Chip c : chips) {
            sourceCount.merge(c.getOwnerId(), 1, Integer::sum);
        }
        return sourceCount;
    }

    // ===== 手牌管理 =====
    public Card getVisibleCard() { return forheadCard; }
    public void setVisibleCard(Card visibleCard) { this.forheadCard = visibleCard; }

    public Card getHiddenCard() { return hiddenCard; }
    public void setHiddenCard(Card hiddenCard) { this.hiddenCard = hiddenCard; }

 // ===== 出千申報金額管理（可虛報）=====
    private int declaredAmount;

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getDeclaredAmount() {
        return declaredAmount > 0 ? declaredAmount : debt;
    }

    public void setDeclaredAmount(int declaredAmount) {
        this.declaredAmount = declaredAmount;
    }
    
    // ===== 展示用方法 =====
    public void showInfo() {
        System.out.println("【" + name + " / " + id + "】");
        System.out.println("回合數：第 " + currentRound + " 輪");
        System.out.println("賭債金額：" + debt);
        System.out.println("籌碼數量：" + getChipCount());
        System.out.println("總籌碼價值：" + getTotalChipValue());

        System.out.println("來源統計：");
        for (Map.Entry<String, Integer> entry : getChipSourceSummary().entrySet()) {
            System.out.println("→ 來自 " + entry.getKey() + " 的籌碼：" + entry.getValue() + " 枚");
        }

        System.out.println("明牌：" + (forheadCard != null ? "（放在額頭，看不到）" : "未發"));
        System.out.println("暗牌：" + (hiddenCard != null ? "hiddenCard" : "未發"));
    }
    
    public void resetForNewRound() {
    	forheadCard = null;
        hiddenCard = null;
        isCheating = false;
        // 如果有特殊記錄也可以清掉
    }
}
