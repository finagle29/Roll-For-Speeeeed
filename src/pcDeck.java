class pcDeck extends Deck {

	private static final long serialVersionUID = 200411594381390783L;

	public pcDeck() {
		for (int val = 11; val <= 13; val++)
			for (int suit = 0; suit <= 3; suit++)
				this.add(new Card(val, suit));
	}

}
