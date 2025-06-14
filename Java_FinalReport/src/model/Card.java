package model;
import model.Card;

public class Card {
    private String suit;
    private int number; //本遊戲只用 A-10

    public Card(String suit, int number) {
        this.suit = suit;
        this.number = number;
    }

    public String getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    public String toString() {
        return suit + number;
    }

    // 用於比大小（同類型比數字）
    public int compareTo(Card other) {
        return Integer.compare(this.number, other.number);
    }
}
