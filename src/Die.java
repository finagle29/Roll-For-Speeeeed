import java.util.Random;

class Die {

	private int sides;
	private static Random r = new Random();

	private Die(int n) {

		if (n > 0)
			sides = n;
		else
			throw new IllegalArgumentException("Dice must have at least one side!");
	}

	public Die() {
		this(6);
	}

	int roll() {
		return 1 + r.nextInt(sides);
	}

}
