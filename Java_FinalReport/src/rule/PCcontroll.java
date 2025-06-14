package rule;

import model.Player;
import model.Chip;
import game.GameContext;
import java.util.List;

/**
 * 管理 AI 控制邏輯，包括下注策略與判斷。
 */
public class PCcontroll {

    /**
     * 根據角色執行對應的下注策略。
     * @param self 電腦控制之角色
     * @param ctx 當前回合資訊
     * @return 下的籌碼列表（空代表 fold）
     */
    public static List<Chip> decideBet(Player self, GameContext ctx) {
        int bet = 0;

        switch (self.getId()) {
            case "A":
                bet = strategyA(ctx);
                break;
            case "B":
                bet = strategyB(ctx);
                break;
            case "C":
                bet = strategyC(ctx);
                break;
            case "D":
                bet = strategyD(ctx);
                break;
        }

        if (bet > 0) {
            return ChipRule.bet(self, bet);
        } else {
            System.out.println(self.getName() + " 選擇棄牌");
            return List.of(); // Java 11+ 的空集合語法
        }
    }

    private static int strategyA(GameContext ctx) {
        if (ctx.roundNumber <= 4) {
            return 1; // 被動下注
        } else if (ctx.hasCheatInfo && ctx.myCardScore >= 6) {
            return Math.min(2, ctx.myChipCount);
        } else if (ctx.myChipCount <= 2 && ctx.myCardScore >= 5) {
            return Math.min(5, ctx.myChipCount); // All-in
        }
        return 0;
    }

    private static int strategyB(GameContext ctx) {
        if (ctx.myCardScore >= 7 && ctx.hasCheatInfo) {
            return Math.min(2, ctx.myChipCount);
        } else if (ctx.myCardScore >= 6) {
            return 1;
        } else if (ChipRule.hasChipFrom(ctx.self, "A")) {
            return Math.min(1, ctx.myChipCount); // 消耗奴隸籌碼
        }
        return 0;
    }

    private static int strategyC(GameContext ctx) {
        if (ctx.roundNumber <= 3 && ctx.myCardScore <= 3) {
            return 1;
        } else if (ctx.myCardScore >= 7 || (ctx.hasCheatInfo && ctx.myCardScore >= ctx.enemyCardScore)) {
            return Math.min(2, ctx.myChipCount);
        } else if (ctx.opponentBet >= 2 && ctx.myChipCount >= ctx.opponentBet) {
            return ctx.opponentBet; // 跟注
        }
        return 0;
    }

    private static int strategyD(GameContext ctx) {
        if (ctx.myCardScore >= 6 || (ctx.hasCheatInfo && ctx.myCardScore >= ctx.enemyCardScore)) {
            return Math.min(3, ctx.myChipCount);
        } else if (ctx.rank == 1 && ctx.roundNumber >= 7 && ctx.myChipCount >= 1) {
            return 1;
        } else if (ctx.hasCheatInfo && ctx.enemyCardScore <= 3) {
            return Math.min(2, ctx.myChipCount);
        }
        return 0;
    }
}
