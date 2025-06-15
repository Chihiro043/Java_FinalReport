package model;

import java.util.*;

public class PlayerProfile {
    private static final Map<String, String> storyMap = new HashMap<>();
    private static final Map<String, String> ruleMap = new HashMap<>();

    static {
        storyMap.put("A", "角色 A\n" +
        		  "我明明是XX企業的千金阿，怎麼會被賭局搞成這樣，一直被踩在腳下，像條被遺棄的敗犬。\n" +
                  "今天又被拖著參與賭局了，又是強迫我配合出千，又是把我當成用完即拋棋子，這樣的生活到底甚麼時候可以到頭......。\n" +
                  "沉重的賭債像鐐銬，實在是，讓我動彈不得，就配合吧......會結束的......會結束吧。\n" +
                  "-\n" +
                  "桌子對面的好像是出了名的賭徒瘋子阿......好像有點，燃起了希望，也許我還能翻身......嗎?");
        storyMap.put("B", "角色 B\n" +
        		  "這場賭局不過是我賺外快的遊戲罷了，有這種蠢狗打pass，穩操勝券的好嗎。\n" +
                  "連著三年，故意借錢來參與債務整理大會，反正每年都是我最贏，今年一定也是穩的啦，謝謝主辦送錢啦哈哈!!!\n" +
                  "-\n" +
                  "X的怎麼這種賭徒XX抽到一桌!!不會出事吧......");
        storyMap.put("C", "角色 C\n" +
                "賭博，就是瘋狂的遊戲！欠債算什麼？人生規劃算什麼?\n"+
                "「讓我們瘋狂豪賭吧！」這才是賭博的真諦~!!");
        storyMap.put("D", "角色 D\n" +
        		  "X的，被這種瘋子搞到要來參與債務整理大會，還分到同一桌，真的有夠有病......\n" +
                  "-\n" +
                  "這女人腦子還好嘛!?想出這種......算了，就順著她吧，反正對面看起來，呵呵......\n" +
                  "-\n" +
                  "「你們這些笨蛋，都是被我騙的！」我贏了被看不起？笑話！別想小看我，敢來賭？我陪你們玩到底，絕不服輸！");

        ruleMap.put("A",
        "【A（保守）】\n" +
        "觀望，除非手牌夠強或有資訊優勢，否則主動大額下注。\n" +"陣營互動：與 B 陣營共享視角資訊。");
        ruleMap.put("B",
        "【B（壓迫）】\n" +
        "即使手牌普通，也可主動下注，尤其出千時可以大額壓注以強化威脅力。\n" + "陣營互動：與 A 陣營共享視角資訊。");
         ruleMap.put("C",
        "【C（誘導）】\n" +
        "下注取決於資訊掌握程度，可誘導敵方下注後再反擊。\n" + "陣營互動：出千會與 D 陣營共享視角資訊，交換債務申報金額，方便佈局。");
         ruleMap.put("D",
        "【D（操作）】\n" +
        "藉由出千來獲取視角優勢後再下注，手牌差會 bluff。\n" + " 陣營互動：與 C 陣營共享視角資訊，交換債務申報金額，配合策略。");
        
    }

    public static String getIntroStory(String role) {
        return storyMap.getOrDefault(role, "-");
    }

    public static String getPersonalRule(String role) {
        return ruleMap.getOrDefault(role, "-");
    }

    /**
     * 顯示完整角色卡片資訊（分段Enter切換）
     */
    public static void printRoleCard(Player self, List<Player> allPlayers) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n＜角色背景＞（按 Enter 查看）");
        scanner.nextLine();
        System.out.println(getIntroStory(self.getId()));

        System.out.println("\n＜債務與陣營資訊＞（按 Enter 查看）");
        scanner.nextLine();
        System.out.println("角色 ID：" + self.getId());
        System.out.println("顯示名稱：" + self.getName());
        System.out.println("初始債務金額：" + self.getDebt());

        int chipValue = self.getDebt() / 10;
        System.out.println("單枚籌碼價值：" + chipValue + " 元");
        System.out.println("持有籌碼數：" + self.getChipCount());

        String faction;
        String factionName;
        if (self.getId().equals("A") || self.getId().equals("B")) {
            faction = "A+B 派";
            factionName = "支配者＆旗子";
        } else {
            faction = "C+D 派";
            factionName = "脫序者";
        }
        System.out.println("所屬陣營：" + faction + "（" + factionName + "）");

        System.out.println("隊友角色故事：");
        for (Player p : allPlayers) {
            if (isSameFaction(self, p) && !p.getId().equals(self.getId())) {
                System.out.println("→ " + p.getName() + "：" + getIntroStory(p.getId()));
            }
        }

        System.out.println("\n【出千規則】（按 Enter 查看）");
        scanner.nextLine();
        System.out.println(getPersonalRule(self.getId()));
    }

    /**
     * 簡易判斷是否同陣營（AB為一隊，CD為一隊）
     */
    private static boolean isSameFaction(Player p1, Player p2) {
        return (p1.getId().equals("A") || p1.getId().equals("B")) &&
               (p2.getId().equals("A") || p2.getId().equals("B"))
            || (p1.getId().equals("C") || p1.getId().equals("D")) &&
               (p2.getId().equals("C") || p2.getId().equals("D"));
    }
}
