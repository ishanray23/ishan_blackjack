Game Motivation:
[Start Game] 
     v
[Display Bank & Ask for Bet] 
     v
[Deal 2 Cards to Player and Dealer (1 Hidden)] 
     v
[Player Action Loop (for each hand)]
     |
     |--> [Hit] --> [Add Card] --> [Check Bust] --> (loop/exit)
     |--> [Stand] --> [Next Hand]
     |--> [Double] --> [Double Bet, Add One Card, End Turn]
     |--> [Split] --> [Create Two Hands from Pair, Add One Card to Each, Restart Loop]
     v
[Dealer Reveals Hidden Card]
     |
[Dealer Hits Until Total ≥ 17]
     |
[Compare Hands, Adjust Bank (Win/Loss/Push)]
     |
[Ask to Play Again]
     |
[Yes] -----------------------------+
     |                             |
     +----> [Shuffle & Restart Round]
     |
[No]
     |
[End Game]


Requirements:
A console-based Blackjack game.
Player always starts with $500.
Player can bet, hit, stand, double, and split (multiple times).
Dealer follows standard rules (hit ≤ 16, stand ≥ 17).
Bank updates after each hand.
Code should handle aces correctly (soft/hard total).

Components of Program, Structure:
Card class: Represents a card with a random suit and value.
Deck class: Holds 52 cards and shuffles/deals them.
Hand class: Represents a player's or dealer's hand.
Game class: Controls the game loop, user input, logic.
Main method: Starts the game.

Work Steps:
Implement Card class with toString and value rules.
Implement Deck class to manage card shuffling and dealing.
Implement Hand class to track cards and calculate totals.
Implement Game class to manage game flow and betting.
Add support for hit, stand, double, and split (recursive).
Test win/loss/draw conditions and bank management.
Add loop for multiple hands per session and restart option.

Possible work method:


Card Class
Each card has:
A random suit (1–4: Clubs, Diamonds, Hearts, Spades).
A value (1 = Ace (11 pts), 2–10 = 2–10, 11–13 = J/Q/K = 10 pts).
toString() returns a readable name like "K of Hearts".

Deck Class
Stores a stack of cards.
Can shuffle using Collections.shuffle().
deal() returns the top card (or creates a new one if empty).

Hand Class
Tracks a list of cards.
Calculates the total, adjusting for soft/hard Aces:
Initially, Ace = 11.
If total > 21 and Ace exists, subtract 10 (Ace becomes 1).
Used by both the player and dealer.

Game Class
Starts game and manages flow.
Player is asked to bet, and two cards are dealt to each side.
Player can:
hit to take another card.
stand to end their turn.
double to double their bet and take one more card.
split if the two cards are the same value.
After player decisions:
Dealer reveals second card.
Dealer hits until 17+.
Outcomes are compared, and bank is adjusted.
Game loops unless player exits or bank = 0.
