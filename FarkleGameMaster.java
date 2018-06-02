package Farkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FarkleGameMaster implements ScoreListener {

	private Farkle farkle;
	private Scanner scanner;
	
	public FarkleGameMaster(Player...players) {
		farkle = new Farkle(players);
		scanner = new Scanner(System.in);
		farkle.addScoreListener(this);
	}

	public void startGame() {
		System.out.println("***FARKLE***\n\n");
		String name = farkle.getCurrentPlayerName();
		System.out.println("\n\n" + name + ", det er din tur! \n");
		continueTurn();
	}
	
	public void startTurn() {
		farkle.nextTurn();
		String name = farkle.getCurrentPlayerName();
		System.out.println(name + ", det er din tur! \n");
		continueTurn();
	}
	
	public void continueTurn() {
		List<Integer> theThrow = farkle.rollDice();
		List<ArrayList<Integer>> combos = farkle.calculateCombinations(theThrow);
		
		// Print "Du kastet dette:"
		// 1 1 1 3 4 5
		System.out.print("Du kastet dette: ");
		for (int i = 0; i < theThrow.size(); i++) System.out.print(theThrow.get(i) + " ");
		System.out.print("\n");

		if (combos.isEmpty()) {
			System.out.println("Farkle! Du får 0 poeng denne runden!\n");
		} else chooseCombination(combos);
	}
	
	public void endTurn() {
		farkle.endTurn();
	}
	
	private void printCombinations(List<ArrayList<Integer>> combos, List<Integer> chosen) {
		// "Hvikle kombinasjoner vil du beholde? (12 for alle)"
		// 1: 1			: 100 poeng
		// 2: 444		: 400 poeng  <- bruk getStringFromCombination(combo) for å få komboene som string
		// 3: 5			: 50 poeng
		
		String p = "Hvilke kombinasjoner vil du beholde?";
		if (combos.size() > 1) p += "(1-" + combos.size() + ")";
		
		System.out.println(p);
		for (int i = 0; i < combos.size(); i++) {
			String s = "";
			if (chosen.contains(i)) s += "* "; 
			s +=  (i+1) + ": " + farkle.getStringFromCombination(combos.get(i)) + "\t: ";
			s += farkle.getScoreForCombination(combos.get(i)) + " poeng\n";
			
			System.out.print(s);
		}
	}
	
	private void printScoreboard() {
		System.out.println("\n\n- - - Scoreboard - - -");
		for (int i = 0; i < farkle.getPlayerCount(); i++) {
			System.out.println(farkle.getPlayer(i).getName() + "\t: " + farkle.getPlayer(i).getScore() + " poeng");
		}
		System.out.print("\n\n");
	}
	
	
	// Kanskje den vanskeligste funksjonen å forstå, sså her er litt ekstra kommentarer:
	
	// Lagrer plassen (aka indexen) til de komboene av terninger som man vil legge til siden
	// Disse indexene legges inn i chosenComboIndexes
	// Ber Farkle-klassen summere opp scoren for disse valgte komboene midlretidig
	// Spør spilleren om den vil kaste de resterende terningene eller ikke
	// Hvis ja; starter funksjonen på nytt
	// Hvis nei; slutter denne spillerens sin runde
	public void chooseCombination(List<ArrayList<Integer>> combos) {
		List<Integer> chosenComboIndexes = new ArrayList<Integer>();
		
		// få ett brukerinput (enten 1 eller 2 i dette tilfellet)
		
		boolean moreCombos = true;
		
		do {
			printCombinations(combos, chosenComboIndexes);
			
			// Velger en kombinasjon
			int choice = 0;
			do {
				System.out.print("\nVelg en kombinasjon av terninger du vil sette til side: ");
				choice = scanner.nextInt();
			} while (choice < 1 || choice > combos.size());
			
			choice--;
			
			// Om den ikke er valgt fra før, legg til i listen, er den valgt, fjern den
			int index = chosenComboIndexes.indexOf(choice);
			if (index == -1) chosenComboIndexes.add(choice);
			else chosenComboIndexes.remove(index);
			
			if (chosenComboIndexes.size() < combos.size()) {
				// Finn ut om brukeren vil velge en kombinasjon til
				String YN = "";
				do {
					System.out.print("\nVil du velge en kombinasjon til? (Y/N): ");
					YN = scanner.next();
				} while (!YN.toUpperCase().equals("Y") && !YN.toUpperCase().equals("N"));
				
				// Hvis ja, så skal do-while loopen kjøre igjen
				moreCombos = YN.toUpperCase().equals("Y");
			} else moreCombos = false;
			
			// Hvis chosenComboIndexes er tom mo man også gjøre det på nytt
		} while (moreCombos || chosenComboIndexes.isEmpty());
		
		
		List<ArrayList<Integer>> chosenCombos = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < chosenComboIndexes.size(); i++) {
			// legg de valgte kombinasjonene inn i en ny liste
			chosenCombos.add(combos.get(chosenComboIndexes.get(i)));
		}
		
		// legg til poeng for de valgte kombinasjonene
		// husk å ta bort antall terninger som kombinasjonene er lagd opp av
		farkle.addScoreForChoosenCombination(chosenCombos);

		
		String YN = "";
		do {
			System.out.print("\nDu har nå tjent opp " + farkle.getTurnScore() + ", vil du fortsette å kaste? (Y/N): ");
			YN = scanner.next();
		} while (!YN.toUpperCase().equals("Y") && !YN.toUpperCase().equals("N"));
		
		// Print "Kast videre?"
		// Hvis ja : continueTurn()
		// Hvis nei: endTurn()
		if (YN.toUpperCase().equals("Y")) continueTurn();
		else endTurn();
		
		// ikke bare sett endTurn() inn i gameloopen, fordi hvis man har "Farkled" så skal man ikke adde opp scoren
	}
	
	
	
	public boolean checkGameOver() {
		for (Player player : farkle.getPlayers()) if (player.getScore() >= 10000) return true;
		return false;
	}
	
	public void closeScanner() {
		scanner.close();
	}
	
	@Override
	public void scoresDidChange(int playerNum) {
		// Print ut at du har fått noen mere poeng ellerns
		
		// sjekk om game over?
		if (checkGameOver()) {
			// game over!!
			System.out.println("\n\nGame over! " + farkle.getPlayer(playerNum).getName() + " du vant!");
		}
	}
	
	
	public static void main(String[] args) {
		Player alfred = new Player("Alfred");
		Player jonas = new Player("Jonas");
		Player conrad = new Player("Conrad");
		Player jakob = new Player("Jakob");
		Player martin = new Player("Martin");
		
		FarkleGameMaster master = new FarkleGameMaster(alfred, jonas, conrad, jakob, martin);

		
		
		// Start spillet
		master.startGame();
		master.printScoreboard();
		
		// Game-loop, så lenge det ikke er game-over
		while (!master.checkGameOver()) {
			master.startTurn();
			master.printScoreboard();
		}
		
		master.closeScanner();
		
//		Scanner scanner = new Scanner(System.in);
//		scanner.hasNextInt();
//		scanner.close();
		
	}
	

}
