package Farkle;

import java.util.ArrayList;
import java.util.List;

public class Combinations {

	private List<ArrayList<Integer>> posibleCombinations;
	
	public Combinations(List<Integer> dice) {
		posibleCombinations = new ArrayList<ArrayList<Integer>>();
		
		System.out.println("");
		
		// Liste som skal holde antall enere på plass 0, antall toere på plass1, ..., antall seksere på plass 5
		List<Integer> numEyes = new ArrayList<Integer>(6);
		
		// Legger til antall enere, toere, ..., seksere på riktig plass
		for (int i = 1; i <= 6; i++) {
			Integer num = 0;
			
			for (int j = 0; j < dice.size(); j++) if (dice.get(j) == i) num++;
			
			numEyes.add(num);
		}
		
		// Går gjennom listen igjen, og finner mulige poengsummer (bare de beste per tall (1-6) per nå)
		for (int i = 0; i < 6; i++) {
			ArrayList<Integer> diceUsed = new ArrayList<Integer>(6);
			
			// Enere og femmere gir alltid poeng
			if (i == 0 || i == 4) 			for (int j=0; j<numEyes.get(i); j++) diceUsed.add(i+1);
			
			// Endre tall gir poeng hvis de er 3 av samme i ett kast
			else if (numEyes.get(i) >= 3) 	for (int j=0; j<3; j++) 				diceUsed.add(i+1);
			
			// Lagre de kombinasjonene du har funnet ut om dette tallet (1-6)
			if (!diceUsed.isEmpty()) posibleCombinations.add(diceUsed);
		}
	}

	public List<ArrayList<Integer>> getPosibleCombinations() {
		return posibleCombinations;
	}
	
}
