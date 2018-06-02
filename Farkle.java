package Farkle;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class Farkle {

	private List<Player> playerList;
	private Collection<ScoreListener> listenerList;
	private int turn;
	private int turnScore;
	private int diceLeft;
	
	public Farkle(Player...players) {
		// Legg til spillerene våre 
		playerList = new ArrayList<Player>();
		for (Player player : players) {
			playerList.add(player);
		}
		
		listenerList = new ArrayList<ScoreListener>();
		
		// player1 har plass 0, aka turn må starte å være 0
		turn = 0;
		
		// man starter på 0 score hver runde
		turnScore = 0;
		
		// man starter med 6 terninger
		diceLeft = 6;
	}
	
	// Returnerer player på plass n
	// Thrower IllegalArgumentException om n er ulovlig
	public Player getPlayer(int n) {
		if (n < 0 || n >= playerList.size()) throw new IllegalArgumentException("n si not in bounds of playerList");
		return playerList.get(n);
	}
	
	// Returnerer alle spillerne
	public Collection<Player> getPlayers() {
		return playerList;
	}
	
	
	// Returnerer antall spillere som er med
	public int getPlayerCount() {
		return playerList.size();
	}
	
	// Hent navnet til den som er som det er turen til
	public String getCurrentPlayerName() {
		return playerList.get(turn).getName();
	}
	
	// Hent turnScore
	public int getTurnScore() {
		return turnScore;
	}
	
	// Summer opp turnScore ut ifra valgt(e) kombinasjon(er)
	public void addScoreForChoosenCombination(List<ArrayList<Integer>> combos) {
		// Gå gjennom kombinasjonene og summer opp scoren, og tell ned antall terninger igjen
		for (int i = 0; i < combos.size(); i++) {
			turnScore += getScoreForCombination(combos.get(i));
			diceLeft -= combos.get(i).size();
		}
		
		// Resett diceLeft til 6 hvis det er en "hot dice"
		if (diceLeft == 0) diceLeft = 6;
	}
	
	// Starter en ny runde
	public void nextTurn() {
		// Endrer turn og nullstiller turnScore
		turn = (turn+1)%getPlayerCount();
		turnScore = 0;
		diceLeft = 6;
	}
	
	// Slutter runden
	public void endTurn() {
		// Lagre poeng
		playerList.get(turn).addScore(turnScore);
		
		// Oppdater lyttere
		fireScoresDidChange();
	}
	
	// Ruller n antall terninger
	// Litt forvirrende, men 'die' er entall, og 'dice' er flertall
	public List<Integer> rollDice() { // int n) {
		// if (n < 1 || n > 6) throw new IllegalArgumentException("n is not between 1 and 6");
		List<Integer> dice = new ArrayList<Integer>();
		Dice die = new Dice();
		for (int i = 0; i < diceLeft; i++) {
			dice.add((Integer) die.rollDie());
		}
		return dice;
	}
	
	// Bruker Combinationsobjektet til å få forskjellige kombinasjoner som gir poeng
	public List<ArrayList<Integer>> calculateCombinations(List<Integer> dice) {
		Combinations combinations = new Combinations(dice);
		return combinations.getPosibleCombinations();
	}
	
	// Returnerer summen på en tre-på-rad kombo basert på hvor mange øyne terningen viser
	private int getThreeInARowScore(int eyes) {
		switch (eyes) {
			case 1: return 1000;
			case 2: return 200;
			case 3: return 300;
			case 4: return 400;
			case 5: return 500;
			case 6: return 600;
			default: throw new IllegalArgumentException("Eyes must be between 1 and 6");
		}
	}
	
	// Returnerer en score for en gitt kombinasjon
	// combination er en liste med tall av en type (enten 1, eller 2, ... , eller 6)
	public int getScoreForCombination(List<Integer> combo) {
		// sjekk om combination er på riktig format
		for (int i = 0; i < combo.size(); i++) if (combo.get(0) != combo.get(i)) {
			throw new IllegalArgumentException("Feil i combo, alle må være av samme tall");
		}
		
		// 3-på-rad 
		if (combo.size() == 3) return getThreeInARowScore(combo.get(0));
		
		// 3-på-rad og flere enere eller femmere
		else if (combo.size() > 3) {
			int threeInARow = getThreeInARowScore(combo.get(0));
			int singles = 0;
			if (combo.get(0)==1) singles = 100*(combo.size() - 3);
			else if (combo.get(0)==5) singles = 50*(combo.size() - 3);
			// ((combo.get(0)==1) ? 1000 : 200) * (combo.size() - 3);
			return threeInARow + singles;
		}
		
		// bare enere eller femmere
		else if (combo.size() < 3) {
			if (combo.get(0)==1) return (int)100*combo.size();
			else if (combo.get(0)==5) return (int)50*combo.size();
		}
		
		// Skal ikke bli kallt noen gang, men må være her som en sikkerhet
		return 0;
	}
	
	
	// Gir en string for en combo, f.eks. komboen 111 blir til "111"
	public String getStringFromCombination(List<Integer> combo) {
		String s = "";
		for (int i = 0; i < combo.size(); i++) s += combo.get(i);
		return s;
	}
	
	
	// Observable funksjoner
	private void fireScoresDidChange() {
		for (ScoreListener listener : listenerList) {
			listener.scoresDidChange(turn);
		}
	}
	
	public void addScoreListener(ScoreListener listener) {
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}
	
	public void removeScoreListener(ScoreListener listener) {
		listenerList.remove(listener);
	}

}
