package Farkle;

public class Player {

	private String name;
	private int score;
	
	public Player(String name) {
		this.name = name;
		this.score = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
	
	public void addScore(int score) {
		if (score < 0) throw new IllegalArgumentException("Score must be positive");
		this.score += score;
	}

}
