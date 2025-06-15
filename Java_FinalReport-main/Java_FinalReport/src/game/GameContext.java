package game;

import model.Player;

// 存放電腦下注策略用的回合即時資訊。
public class GameContext {
    public int roundNumber;         // 當前回合數（由 Game 控制）
    public int myChipCount;         // 玩家剩餘籌碼數
    public int myTotalValue;        // 玩家目前持有籌碼總價值
    public int opponentBet;         // 當前對手下注數
    public int myCardScore;         // 自己手牌評分（0~10）
    public int enemyCardScore;      // 對手手牌評分（若可見）
    public boolean hasCheatInfo;    // 是否已出千並成功看到對手暗牌
    public int rank;                // 當前總價值名次（1為最高）
    public Player self;             // 指向自身 Player 實例

    public GameContext(Player self) {
        this.self = self;
    }


    public GameContext(Player self, int roundNumber, int myChipCount, int myTotalValue,
                       int myCardScore, int enemyCardScore, boolean hasCheatInfo,
                       int opponentBet, int rank) {
        this.self = self;
        this.roundNumber = roundNumber;
        this.myChipCount = myChipCount;
        this.myTotalValue = myTotalValue;
        this.myCardScore = myCardScore;
        this.enemyCardScore = enemyCardScore;
        this.hasCheatInfo = hasCheatInfo;
        this.opponentBet = opponentBet;
        this.rank = rank;
    }
}
