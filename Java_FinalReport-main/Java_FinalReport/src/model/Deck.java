package model;
import java.util.*;

public class Deck {
    private List<Card> cards;
    private static final String[] SUITS = {"♠", "♥", "♦", "♣"};

    public Deck() {
        cards = new ArrayList<>();
        for (String suit : SUITS) {
            for (int i = 1; i <= 10; i++) {
                cards.add(new Card(suit, i));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in deck.");
        }
        return cards.remove(0);
    }
}
