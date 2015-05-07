import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

class Player {

	private String name;
	private Card pc;
	public Hand hand;
	int HP;
	private int maxHP = 0;
	private int baseSp = 0;
	private int turnSp = 0;
	int speed = 0;
	private boolean shielded = false;
	private int handSize;

	public Player() {
		name = "";
		hand = new Hand(0);
	}
	/**
	 * This constructor is used for initial setup
	 * 
	 * @param name the name of the player
	 * @param p the pcDeck used for the game
	 * @param d the Deck used for the game
	 * @param dice  the Die array used for the game
	 * @param in the Scanner used for the game
	 */
	public void setup(String name, pcDeck p, Deck d, Die[] dice, Scanner in) {
		this.name = name;
		hand.add(p.deal());
		hand.add(p.deal());

		hand.display();
		int i = Integer.parseInt(JOptionPane.showInputDialog("Choose a player card (1 or 2): "));
		while ((i != 1) && (i != 2)) {
			hand.display();
			i = Integer.parseInt(JOptionPane.showInputDialog("Please choose card 1 or card 2 as your player card."));
		}
		pc = hand.get(i - 1);
		hand.clear();
		hand.fill(d, (pc.suit == 3) ? 5 : 3);
		if (pc.suit == Card.CLUBS) {
			Set<Card> s = new HashSet<Card>(hand);
			switch (hand.size() - s.size()) {
			case 1:
				hand.add(d.deal());
				break;
			case 2:
				hand.fill(d, 3);
				break;
			}
		}
		handSize = hand.size();
		roll4baseSp(dice, in);
		roll4hp(dice, in);
	}

	/**
	 * This constructor is used for resets.
	 * 
	 * @param p  the pcDeck used for the game
	 * @param d the Deck used for the game
	 * @param dice the Die array used for the game
	 * @param h the player's current hand
	 * @param in the Scanner used for the game
	 */
	private Player(pcDeck p, Deck d, Die[] dice, Hand h, Scanner in) {
		this();
		hand.add(p.deal());
		hand.add(p.deal());

		hand.display();
		int i = Integer.parseInt(JOptionPane.showInputDialog("Choose a player card (1 or 2): "));
		while ((i != 1) && (i != 2)) {
			hand.display();
			i = Integer.parseInt(JOptionPane.showInputDialog("Please choose card 1 or card 2 as your player card."));
		}
		pc = hand.get(i - 1);
		hand.clear();
		hand.fill(d, (pc.suit == 3) ? 5 : 3);
		handSize = hand.size();
		if (handSize > h.size())
			h.fill(d, handSize - h.size());
		hand = (Hand) h.clone();
		roll4baseSp(dice, in);
		roll4hp(dice, in);
	}

	private void roll4hp(Die[] dice, Scanner in) {
		for (int c = 0; c < ((pc.suit == Card.HEARTS) ? 6 : 5); c++)
			maxHP += 2 * dice[c].roll();
		HP = maxHP;
		JOptionPane.showMessageDialog(null, "HP: " + HP + "/" + maxHP);
		if ((pc.value == Card.QUEEN) || (pc.value == Card.KING)) {
			
			char ans = JOptionPane.showInputDialog("Would you like to reroll (y/n)?").charAt(0);
			if ((ans == 'y') || (ans == 'Y')) {
				maxHP = 0;
				for (int c = 0; c < ((pc.suit == Card.HEARTS) ? 6 : 5); c++)
					maxHP += 2 * dice[c].roll();
				HP = maxHP;
				JOptionPane.showMessageDialog(null, "HP: " + HP + "/" + maxHP);
			}
		}
	}

	private void roll4baseSp(Die[] dice, Scanner in) {
		JOptionPane.showMessageDialog(null, "Rolling for speeeeed...");
		for (int c = 0; c < ((pc.suit == Card.DIAMONDS) ? 5 : 4); c++)
			baseSp += dice[c].roll();
		JOptionPane.showMessageDialog(null, "Your current base speed is " + baseSp);
		if ((pc.value == Card.QUEEN) || (pc.value == Card.JACK)) {
			char ans = JOptionPane.showInputDialog("Would you like to reroll (y/n)?").charAt(0);
			if ((ans == 'y') || (ans == 'Y')) {
				baseSp = 0;
				JOptionPane.showMessageDialog(null, "Rolling for speeeeed...");
				for (int c = 0; c < ((pc.suit == Card.DIAMONDS) ? 5 : 4); c++)
					baseSp += dice[c].roll();
				JOptionPane.showMessageDialog(null, "Your current base speed is now " + baseSp);
			}
		}
	}

	private void makeEqualTo(Player copy) {
		pc = copy.pc.clone();
		hand = (Hand) copy.hand.clone();
		maxHP = copy.maxHP;
		baseSp = copy.baseSp;
	}

	void rollForSpeeeeed(Die[] dice) {
		turnSp = 0;
		JOptionPane.showMessageDialog(null, "Roll for Speeeed!");
		for (int c = 0; c < 2; c++)
			turnSp += dice[c].roll();
		speed = turnSp + baseSp;
	}

	@Override
	public String toString() {
		return name + "\t" + pc.toString() + "\nHP: " + HP + "/" + maxHP + "\nspeed: " + speed + "\n" + hand.toString();
	}

	void display() {
		System.out.println(this.toString());
	}

	void takeTurn(Player opponent, Die[] dice, Scanner in, pcDeck pcdeck, Deck deck, Deck discard) {
		returnState r;
		JOptionPane.showMessageDialog(null, "Your hand:");
		hand.display();
		char c = JOptionPane.showInputDialog("Attack or Effect (a/e)").charAt(0);
		while ((c != 'a') && (c != 'e') && (c != 'A') && (c != 'E')) {
			hand.display();
			c = JOptionPane.showInputDialog("Please choose to either [A]ttack or [E]ffect.").charAt(0);
		}
		switch (c) {
		case 'a':
		case 'A':
			r = this.attack(opponent, dice, in, pcdeck, deck, discard);
			break;
		default:
			r = this.effect(opponent, in, deck, discard);
		}
		if (r.discardNeeded) {
			this.discard(deck, discard, hand, r.choice);
			this.replenish(deck, discard, hand);
		}
	}

	private returnState attack(Player opponent, Die[] dice, Scanner in, pcDeck pcdeck, Deck deck, Deck discard) {
		hand.shuffle();
		System.out.println(opponent.name + ", choose a card from " + name + "'s hand (1-" + hand.size() + ")");
		int choice = in.nextInt() - 1;
		while ((choice < 0) || (choice >= hand.size())) {
			System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
			choice = in.nextInt() - 1;
		}
		System.out.println(hand.get(choice).toString());
		switch (hand.get(choice).value) {
		case Card.ACE:
		case 3:
		case 4:
		case 5:
		case 7:
			this.damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 8:
		case 9:
			if (pc.value == Card.JACK)
				this.damage(Card.ACE, opponent, dice, in);
			else
				this.damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 6:
			if (pc.value == Card.KING)
				this.damage(Card.ACE, opponent, dice, in);
			else
				this.damage(hand.get(choice).value, opponent, dice, in);
			break;
		case 2:
			switch (pc.suit) {
			case Card.CLUBS:
				shielded = true;
				break;
			case Card.DIAMONDS:
				System.out.print("Rolling to drain the average of 2d6... ");
				int r1 = dice[0].roll();
				int r2 = dice[1].roll();
				System.out.println(r1 + " " + r2);
				int drain = ((r1 + r2) / 2) + ((r1 + r2) % 2);
				System.out.println(drain + " was drained!");
				opponent.HP -= drain;
				HP += drain;
				break;
			case Card.HEARTS:
				Card[] handArray = (Card[]) hand.toArray();
				for (Card c : handArray) {
					deck.add(c);
					hand.remove(c);
				}
				handArray = (Card[]) opponent.hand.toArray();
				for (Card c : handArray) {
					deck.add(c);
					opponent.hand.remove(c);
				}
				deck.shuffle();
				hand.fill(deck, 4);
				opponent.hand.fill(deck, 4);
				System.out.println("Both players hands were shuffled into the deck and new 4 card hands were drawn");
				return new returnState(choice, false);
			case Card.SPADES:
				System.out.print("Rolling d6 to increase speed... ");
				r1 = dice[0].roll();
				System.out.println(r1);
				baseSp += r1;
			}
			break;
		case 10:
			System.out.println("Who shall you reset? " + name + " or " + opponent.name + "?");// choose who to reset
			String choyce = in.next();
			while (!choyce.equalsIgnoreCase(name) && !choyce.equalsIgnoreCase(opponent.name)) {
				System.out.println("You must choose one of the two players currently in play!");
				choyce = in.next();
			}
			if (choyce.equalsIgnoreCase(name)) {
				Player tempPlayer = new Player(pcdeck, deck, dice, hand, in);
				this.makeEqualTo(tempPlayer);
				return new returnState(choice, false);
			} else {
				System.out.print("Rolling for hit... ");
				int roll = dice[0].roll();
				System.out.println(roll);
				if ((roll * 10) > speed) {
					Player tempPlayer = new Player(pcdeck, deck, dice, opponent.hand, in);
					opponent.makeEqualTo(tempPlayer);
				} else
					System.out.println("Miss");
			}
			break;
		}
		return new returnState(choice, true);
	}

	private void damage(int chupa, Player opponent, Die[] dice, Scanner in) {
		if (opponent.shielded) {
			System.out.println(opponent.name + " blocked the attack!");
			opponent.shielded = false;
		} else if ((chupa >= 3) && (chupa <= 9)) {
			System.out.print("Rolling for hit... ");
			int roll = dice[0].roll();
			System.out.println(roll);
			if ((roll * 10) > speed) {
				System.out.println(opponent.name + " takes " + chupa + " damage");
				if (opponent.HP > chupa)
					opponent.HP -= chupa;
				else
					opponent.HP = 0;
			} else
				System.out.println("Miss");
		} else if (chupa == Card.ACE) {
			System.out.print("Rolling 2d6 for damage... ");
			int r1 = dice[0].roll();
			int r2 = dice[1].roll();
			System.out.println(r1 + " " + r2);
			if (opponent.HP > (r1 + r2))
				opponent.HP -= (r1 + r2);
			else
				opponent.HP = 0;
		}
	}

	private returnState effect(Player opponent, Scanner in, Deck deck, Deck discard) {
		System.out.println("Choose a card to effect play (1-" + hand.size() + ")");
		int choice = in.nextInt() - 1;
		while ((choice < 0) || (choice >= hand.size())) {
			System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
			choice = in.nextInt() - 1;
		}
		Card choice1 = hand.get(choice).clone();
		this.discard(deck, discard, hand, choice1);
		Card choice2 = null;
		System.out.println(choice1.toString());
		if ((choice1.suit != pc.suit) && hand.contains(new Card(14, choice1.suit))) {
			System.out.println("Would you like to effect play another card? (Y/n)");
			char c = in.next().charAt(0);
			while ((c != 'y') && (c != 'n') && (c != 'Y') && (c != 'N')) {
				hand.display();
				System.out.println("Please answer [Y]es or [n]o");
				c = in.next().charAt(0);
			}
			if ((c == 'Y') || (c == 'y')) {
				System.out.println("Choose a card to effect play (1-" + hand.size() + ")");
				choice = in.nextInt() - 1;
				while ((choice < 0) || (choice >= hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
					choice = in.nextInt() - 1;
				}
				choice2 = hand.get(choice).clone();
				this.discard(deck, discard, hand, choice2);
			}
		}
		this.replenish(deck, discard, hand);
		int strength = 1 + ((choice1.suit == pc.suit) ? 1 : 0) + ((choice2 == null) ? 0 : 1);
		Card discardChoice = null;
		switch (choice1.suit) {
		case Card.CLUBS:
			if (strength == 1) {
				System.out.println(opponent.name + " takes " + 4 + " damage");
				if (opponent.HP > 4)
					opponent.HP -= 4;
				else
					opponent.HP = 0;
			} else {
				System.out.println(opponent.name + " takes " + 6 + " damage");
				if (opponent.HP > 6)
					opponent.HP -= 6;
				else
					opponent.HP = 0;
			}
			opponent.shielded = false;
			break;
		case Card.DIAMONDS:
			if (strength == 1) {
				opponent.hand.shuffle();
				System.out.println(name + ", choose a card from opponent's hand (1-" + opponent.hand.size() + ")");
				choice = in.nextInt() - 1;
				while ((choice < 0) || (choice >= opponent.hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + opponent.hand.size() + ")");
					choice = in.nextInt() - 1;
				}
				choice2 = opponent.hand.get(choice).clone();

				hand.shuffle();
				System.out.println(opponent.name + ", choose a card from opponent's (1-" + hand.size() + ")");
				choice = in.nextInt() - 1;
				while ((choice < 0) || (choice >= hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
					choice = in.nextInt() - 1;
				}
				opponent.hand.add(hand.get(choice));
				hand.remove(choice);
				hand.add(choice2);
				choice2 = null;
			} else {
				hand.display();
				System.out.println("Choose first card to give to opponent (1-" + hand.size() + ")");
				int[] choices = { in.nextInt() - 1, 0 };
				while ((choices[0] < 0) || (choices[0] >= hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
					choices[0] = in.nextInt() - 1;
				}
				System.out.println("Choose second card to give to opponent (1-" + hand.size() + ")");
				choices[1] = in.nextInt() - 1;
				while ((choices[1] < 0) || (choices[1] >= hand.size()) || (choices[1] == choices[0])) {
					System.out.println("You must choose a valid card index (between 1 and " + hand.size() + " and not " + choices[0] + " )");
					choices[1] = in.nextInt() - 1;
				}
				Card trade11 = hand.get(choices[0]).clone();
				Card trade12 = hand.get(choices[1]).clone();
				hand.remove(trade11);
				hand.remove(trade12);

				opponent.hand.shuffle();
				System.out.println("Choose first card to take from opponent (1-" + opponent.hand.size() + ")");
				int[] choices2 = { in.nextInt() - 1, 0 };
				while ((choices2[0] < 0) || (choices2[0] >= opponent.hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + opponent.hand.size() + ")");
					choices2[0] = in.nextInt() - 1;
				}
				System.out.println("Choose second card to give to opponent (1-" + opponent.hand.size() + ")");
				choices2[1] = in.nextInt() - 1;
				while ((choices2[1] < 0) || (choices2[1] >= opponent.hand.size()) || (choices2[1] == choices2[0])) {
					System.out.println("You must choose a valid card index (between 1 and " + opponent.hand.size() + " and not " + choices2[0] + " )");
					choices2[1] = in.nextInt() - 1;
				}
				Card trade21 = opponent.hand.get(choices2[0]).clone();
				Card trade22 = opponent.hand.get(choices2[1]).clone();
				opponent.hand.remove(trade21);
				opponent.hand.remove(trade22);
				hand.add(trade21);
				hand.add(trade22);
				opponent.hand.add(trade11);
				opponent.hand.add(trade12);
			}
			break;
		case Card.HEARTS:
			if (strength == 1) {
				System.out.println(name + " is healed for " + 4 + " damage");
				if ((maxHP - HP) > 4)
					HP += 4;
				else
					HP = maxHP;
			} else {
				System.out.println(name + " is healed for " + 6 + " damage");
				if ((maxHP - HP) > 6)
					HP += 6;
				else
					HP = maxHP;
			}
			break;
		case Card.SPADES:
			if (strength == 1) {
				hand.display();
				System.out.println("Choose a card to discard (1-" + hand.size() + ")");
				choice = in.nextInt() - 1;
				while ((choice < 0) || (choice >= hand.size())) {
					System.out
							.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
					choice = in.nextInt() - 1;
				}
				discardChoice = hand.get(choice).clone();
				this.discard(deck, discard, hand, discardChoice);
				this.replenish(deck, discard, hand);
			} else {
				this.replenish(deck, discard, hand, true);
				hand.display();
				System.out.println("Choose a card to discard (1-" + hand.size() + ")");
				choice = in.nextInt() - 1;
				while ((choice < 0) || (choice >= hand.size())) {
					System.out.println("You must choose a valid card index (between 1 and " + hand.size() + ")");
					choice = in.nextInt() - 1;
				}
				discardChoice = hand.get(choice).clone();
				this.discard(deck, discard, hand, discardChoice);
			}
			break;
		}
		return new returnState(0, false);
	}

	private class returnState {
		int choice;
		boolean discardNeeded;

		returnState(int c, boolean d) {
			choice = c;
			discardNeeded = d;
		}

	}

	private void discard(Deck deck, Deck discard, Hand h, int choice) {
		discard.add(h.get(choice));
		h.remove(choice);
	}

	private void replenish(Deck deck, Deck discard, Hand h) {
		if (deck.isEmpty()) {
			deck.addAll(discard);
			discard.clear();
			deck.shuffle();
		}
		while (hand.size() < handSize)
			h.add(deck.deal());
	}

	private void replenish(Deck deck, Deck discard, Hand h, boolean fooFlag) {
		if (deck.isEmpty()) {
			deck.addAll(discard);
			discard.clear();
			deck.shuffle();
		}
		if (fooFlag)
			h.add(deck.deal());
	}

	private void discard(Deck deck, Deck discard, Hand h, Card choice) {
		discard.add(choice);
		h.remove(choice);
	}
}
