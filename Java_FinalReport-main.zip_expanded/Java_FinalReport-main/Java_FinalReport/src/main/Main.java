package main;

import game.Game;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("歡迎來到《狂賭之淵：雙重印地安撲克 - 敗犬 or 主人》");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("請按下 ENTER 開始遊戲，或輸入 0 離開：");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("0")) {
                System.out.println("遊戲結束，感謝遊玩！");
                break;
            } else {
                Game.startGame();  
                System.out.println("遊戲結束，是否再來一局？");
            }
        }

        sc.close();
    }
}
