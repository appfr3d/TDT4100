package Farkle;

import java.util.Random;

public class Dice {

	// Returnerer ett tall mellom 1 og 6
	public int rollDie() {
		Random ran = new Random();
		return ran.nextInt(6) + 1;
	}

}
