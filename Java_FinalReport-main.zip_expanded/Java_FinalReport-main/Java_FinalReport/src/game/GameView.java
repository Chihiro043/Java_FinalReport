package game;

import model.Player;
import model.Card;
import rule.CheatingRule;

import java.util.List;

//僅負責顯示玩家視角知卡牌情況，根據出千狀態與角色規則
public class GameView {

    /**
     * 顯示「viewer」目前能看到哪些角色的牌（明牌＋暗牌）
     * - 明牌：他人可見、自己不可見（下注完畢，比牌階段才可視）
     * - 暗牌：需判定是否可偷看
     */
	public static void showCardView(Player viewer, List<Player> allPlayers) {
	    System.out.println("\n🎴 你目前可見的手牌資訊：");
	    for (Player target : allPlayers) {
	        String id = target.getId();
	        String name = target.getName();

	        boolean isSelf = id.equals(viewer.getId());

	        // 明牌顯示：自己無法看到自己的明牌
	        String visibleText = isSelf
	            ? "（放在額頭，看不到）"
	            : target.getVisibleCard() != null
	                ? target.getVisibleCard().toString()
	                : "未發";

	        // 暗牌顯示：自己或出千者可看見
	        boolean canSeeHidden = isSelf || CheatingRule.canSeeHiddenCard(viewer, target);
	        String hiddenText = canSeeHidden
	            ? (target.getHiddenCard() != null ? target.getHiddenCard().toString() : "未發")
	            : "??";

	        System.out.printf("[%s] %s\n", id, name);
	        System.out.println("  明牌：" + visibleText);
	        System.out.println("  暗牌：" + hiddenText);

	        if (!isSelf && !canSeeHidden) {
	            System.out.println("  👁 無法偷看此人的暗牌");
	        } 
	    }
	}


    //單一玩家完整手牌檢視（僅用於全開視角）
    public static void showSinglePlayerHand(Player player) {
        System.out.println("\n[" + player.getId() + "] " + player.getName());
        System.out.println("  明牌：" + (player.getVisibleCard() != null ? player.getVisibleCard() : "未發"));
        System.out.println("  暗牌：" + (player.getHiddenCard() != null ? player.getHiddenCard() : "未發"));
    }
}
