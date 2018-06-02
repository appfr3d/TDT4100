# Farkle #

## Regler ##
Bruker standard scoring fra wikipedia
* 6 terninger
* I starten av turen kaster spilleren alle terningene
* Etter hvert kast må en eller flere terninger med poeng settes til side settes til side
* Hvis man får poeng med alle 6 terningene får man "hot dice" og kan kaste alle tergningene igjen og bygge videre på poengene fra den runden
* Poengkombinasjoner må skje på samme kast, og kan ikke kombineres med senere kast (kast1: to 1ere, kast2: en 1er er ikke 1000 poeng, men heller lik 3*100=300 poeng)
* Hvis et kast ikke gir noen terninger som gir poeng, så har man "farkled" og mister alle poengene for den runden
* Førstemann til 10 000 poeng

## Poeng ##
* 1ere gir 100
* 5ere gir 50
* Tre 1ere gir 1000
* Tre 2ere gir 200
* Tre 3ere gir 300
* Tre 4ere gir 400
* Tre 5ere gir 500
* Tre 6ere gir 600

All info ligger [her](https://en.wikipedia.org/wiki/Farkle)

## Oppsett ##
FarkleGameMaster (Kjør denne klassen for å spille)
* Holder styr på et spill med Farkle
* Har en Farkle variabel
* Implementerer ScoreListener
	* Altså: Funksjon scoresDidChange(), som caller checkGameOver()
* Observerer på Farkle
	* Hvis noen får poeng skal denne varsles
* Styrer spillet ved å få input fra spillere

Farkle
* Er observable, man kan lytte på scoren til spillerne
* Holder styr på spillere, poengsummer, hvem sin tur det er og terninger
* Regner ut poengsummer utifra terningkast

ScoreListener
* Interface med funksjonen scoresDidChange

Combinations
* Finner ut kombinasjoner som gir poeng ut ifra terningkast

Dice
* Funksjon rollDie() som returnerer et random tall mellom 1 og 6

Player
* Har et navn
* Har en poengsum

