package rule;

import model.Player;
import java.util.*;

// 玩家先選擇一組陣營（AB 或 CD）
public class FactionAssigner {

    private static final List<String> AB_ROLES = new ArrayList<>(Arrays.asList("A", "B"));
    private static final List<String> CD_ROLES = new ArrayList<>(Arrays.asList("C", "D"));

    /**
     * 玩家先被指定為 AB 或 CD 派，再分配角色
     * @param userPlayers 使用者玩家（2 人）
     * @param aiPlayers 電腦玩家（2 人）
     * @param userIsAB true 表示玩家為 AB 派；false 表示玩家為 CD 派
     */
    public static void assignPlayerFaction(List<Player> userPlayers, List<Player> aiPlayers, boolean userIsAB) {
        if (userPlayers.size() != 2 || aiPlayers.size() != 2) {
            throw new IllegalArgumentException("使用者與電腦玩家數量必須各為 2。");
        }

        List<String> userRoles = new ArrayList<>(userIsAB ? AB_ROLES : CD_ROLES);
        List<String> aiRoles = new ArrayList<>(userIsAB ? CD_ROLES : AB_ROLES);

        Collections.shuffle(userRoles);
        Collections.shuffle(aiRoles);

        for (int i = 0; i < 2; i++) {
            userPlayers.get(i).setId(userRoles.get(i));
            aiPlayers.get(i).setId(aiRoles.get(i));
        }
    }

}
