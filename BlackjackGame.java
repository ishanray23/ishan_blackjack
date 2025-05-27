import java.util.*;

public class BlackjackGame {
    public static void main(String[] args) {
    
      int cores = Runtime.getRuntime().availableProcessors();
      System.out.println("********************************************************************");
      System.out.println("************************WELCOME TO BLACKJAVA************************");
      System.out.println("*****A java-based reproduction of the classic game of blackjack!****");
      System.out.println("********************************************************************");
      System.out.println("*******JAVA VIRTUAL MACHINE SPECIFICATIONS**************************");
      System.out.println("*******This program uses " + cores + " cores.***********************************");
      System.out.println("********************************************************************");
      System.out.println("*****Programmed by Ishan Ray, finished development 5.23.25**********");
        new Game().play();
    }
}

class Game {
    private final Scanner input = new Scanner(System.in);
    private final Deck deck = new Deck();
    private int bank = 500;

    public void play() {
        System.out.println("Welcome to Blackjack!");
        while (true) {
            deck.shuffle();

            if (bank <= 0) {
                System.out.println("You’re broke! Game over.");
                break;
            }

            System.out.println("Bank: $" + bank);
            System.out.print("Enter your bet: ");
            int bet = input.nextInt();
            if (bet > bank || bet <= 0) {
                System.out.println("Invalid bet.");
                continue;
            }

            List<Hand> hands = new ArrayList<>();
            List<Integer> bets = new ArrayList<>();
            List<String> handNames = new ArrayList<>();

            Hand initial = new Hand();
            initial.add(deck.deal());
            initial.add(deck.deal());
            hands.add(initial);
            bets.add(bet);
            handNames.add("Hand 1");

            Hand dealer = new Hand();
            dealer.add(deck.deal());
            dealer.add(deck.deal());

            System.out.println("Dealer shows: " + dealer.cards.get(0));

            for (int i = 0; i < hands.size(); i++) {
                Hand current = hands.get(i);
                int currentBet = bets.get(i);
                String name = handNames.get(i);

                while (true) {
                    System.out.println(name + ": " + current + " (Total: " + current.total() + ")");
                    boolean canSplit = current.cards.size() == 2 &&
                                       current.cards.get(0).getPointValue() == current.cards.get(1).getPointValue() &&
                                       bank >= currentBet &&
                                       hands.size() < 4;

                    System.out.print("Choose (hit / stand / double" + (canSplit ? " / split" : "") + "): ");
                    String move = input.next().toLowerCase();

                    if (move.equals("split") && canSplit) {
                        bank -= currentBet;
                        Hand h1 = new Hand();
                        Hand h2 = new Hand();
                        h1.add(current.cards.get(0));
                        h2.add(current.cards.get(1));
                        h1.add(deck.deal());
                        h2.add(deck.deal());

                        hands.remove(i);
                        bets.remove(i);
                        handNames.remove(i);

                        hands.add(i, h2);
                        hands.add(i, h1);
                        bets.add(i, currentBet);
                        bets.add(i + 1, currentBet);
                        handNames.add(i, name + "a");
                        handNames.add(i + 1, name + "b");

                        i--;
                        break;
                    } else if (move.equals("double")) {
                        if (bank >= currentBet) {
                            bank -= currentBet;
                            currentBet *= 2;
                            current.add(deck.deal());
                            System.out.println(name + " after double: " + current + " (Total: " + current.total() + ")");
                            bets.set(i, currentBet);
                            break;
                        } else {
                            System.out.println("Not enough bank to double.");
                        }
                    } else if (move.equals("hit")) {
                        current.add(deck.deal());
                        if (current.total() > 21) {
                            System.out.println(name + " busts: " + current + " (Total: " + current.total() + ")");
                            break;
                        }
                    } else if (move.equals("stand")) {
                        break;
                    }
                }
            }

            System.out.println("Dealer reveals: " + dealer + " (Total: " + dealer.total() + ")");
            while (dealer.total() < 17) {
                dealer.add(deck.deal());
                System.out.println("Dealer hits: " + dealer + " (Total: " + dealer.total() + ")");
            }

            for (int i = 0; i < hands.size(); i++) {
                Hand hand = hands.get(i);
                int currentBet = bets.get(i);
                String name = handNames.get(i);
                int pTotal = hand.total();
                int dTotal = dealer.total();

                if (pTotal > 21) {
                    System.out.println(name + " busts. You lose $" + currentBet);
                } else if (dTotal > 21 || pTotal > dTotal) {
                    System.out.println(name + " wins! You earn $" + currentBet);
                    bank += currentBet;
                } else if (pTotal == dTotal) {
                    System.out.println(name + " pushes.");
                } else {
                    System.out.println(name + " loses. You lose $" + currentBet);
                    bank -= currentBet;
                }
            }

            System.out.print("Play another round? (yes/no): ");
            if (!input.next().equalsIgnoreCase("yes")) break;
        }
    }
}

class Card {
    private final int suit;
    private final int value;

    public Card() {
        this.suit = (int)(Math.random() * 4) + 1;
        int r = (int)(Math.random() * 13) + 1;
        if (r == 1) this.value = 11;
        else if (r >= 11) this.value = 10;
        else this.value = r;
    }

    public int getPointValue() {
        return value;
    }

    @Override
    public String toString() {
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int v = (value == 11) ? 0 : (value == 10 ? 9 : value - 1);
        return values[v] + " of " + suits[suit - 1];
    }
}

class Deck {
    private final Stack<Card> cards = new Stack<>();

    public Deck() {
        for (int i = 0; i < 52; i++) cards.push(new Card());
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) return new Card();
        return cards.pop();
    }
}

class Hand {
    public final List<Card> cards = new ArrayList<>();

    public void add(Card card) {
        cards.add(card);
    }

    public int total() {
        int sum = 0, aces = 0;
        for (Card card : cards) {
            int val = card.getPointValue();
            if (val == 11) aces++;
            sum += val;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
