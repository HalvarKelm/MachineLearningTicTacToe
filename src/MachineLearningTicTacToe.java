import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/*
 * 
 *  Author: Halvar Kelm
 * 
 * 	This program is using machine learning to get the best field for any TicTacToe possibility
 */

public class MachineLearningTicTacToe {

	// TicTacToe size
	private static final int SIZE = 3;

	// amount of possible situations
	private static final int POSSIBILITIES = 19682;

	// saves the corresponding action
	static int[] round = new int[POSSIBILITIES];

	// how often the ML alg should play against a random player
	static int possibilityCheck = 800;
	
	public static void main(String[] args) {
		
	}

	//train the algorithm
	public void train() {

		//get for every field the best choice
		for(int i = 0; i < round.length; i++) {
			round[i] = getBestPossibility(calcField(i), false);
		}
		
		//handle some special cases
		round[0] = 0;
		round[6579] = 8;
		round[8019] = 8;
		round[13205] = 1;
		round[1557] = 1;
	}

	//test the game
	private int playGame(boolean output) {

		//initialize field
		int[] field = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++) {
			field[i] = 0;
		}

		// a list to store the fields of a match
		ArrayList<int[]> history = new ArrayList<int[]>();

		boolean play = false;
		int randomNum = (int) (Math.random() * 2) + 1;

		if (randomNum == 2) {
			play = true;
		}

		int win = 0;
		boolean draw = false;
		while (win == 0 && draw == false) {

			if (play == true) {
				field[getRightField(field)] = 1;
				play = !play;
			} else {
				field[checkRandomField(field)] = 2;
				play = !play;
			}

			int[] currentField = new int[SIZE * SIZE];
			for (int i = 0; i < field.length; i++) {
				currentField[i] = field[i];
			}
			history.add(currentField);
			win = checkForWin(field);
			if (win == 0) {
				draw = checkForDraw(field);
			}
		}
		int endwin = 0;
		if (win == 1) {
			endwin = 1;
		} else if (draw == true) {
			endwin = 0;
		} else {
			endwin = 2;
			if (output == true) {
				System.out.println("A GAME WAS LOST!");
				for (int i = 0; i < history.size(); i++) {
					printScreen(history.get(i));
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return endwin;

	}

	//play 100000 times against a random Player
	private double test(boolean output) {
		System.out.println("Playing 100000 games against Random Player");
		int wongames = 0;
		for (int i = 0; i < 100000; i++) {
			int game = playGame(output);
			if (game != 2) {
				wongames++;
			}
		}
		System.out.println("End winrate is = " + (wongames / 100000.0) * 100);
		return (wongames / 100000.0) * 100;
	}

	//check the win percentage for all the possible places
	private int getBestPossibility(int[] field, boolean output) {
		int[] gotField = new int[SIZE * SIZE];
		for (int i = 0; i < gotField.length; i++) {
			gotField[i] = field[i];
		}

		// check all the possibilities of the current choice in history
		if (possibilities(gotField) != null) {
			int[] possibilities = possibilities(gotField);
			
			// save the win chances
			double[] WinChancesForPossibilities = new double[possibilities.length];
			double[] LooseChancesForPossibilities = new double[possibilities.length];

			for (int j = 0; j < possibilities.length; j++) {

				//handle in case you won
				gotField[possibilities[j]] = 1;
				if (checkForWin(gotField) == 1) {
					gotField[possibilities[j]] = 0;
					return possibilities[j];
				}
				gotField[possibilities[j]] = 0;
				
				// play every possibility 100 times and see which one gets the best win
				// percentage
				double wongames = 0;
				double lostgames = 0;
				int won = 0;
				gotField[possibilities[j]] = 1;
				for (int k = 0; k < possibilityCheck; k++) {
					// play it out and get win and rounds
					won = rematch(gotField);
					if (won != 2) {
						wongames++;
					} else if (won == 2) {
						lostgames++;
					}
				}

				LooseChancesForPossibilities[j] = (lostgames / possibilityCheck) * 100;
				WinChancesForPossibilities[j] = (wongames / possibilityCheck) * 100;
				WinChancesForPossibilities[j] -= LooseChancesForPossibilities[j];
				if (output == true) {
					System.out.println(
							"CHOICE: " + possibilities[j] + " LOOSEPERCENT:" + LooseChancesForPossibilities[j]);
					System.out.println("CHOICE: " + possibilities[j] + " WINPERCENT:" + WinChancesForPossibilities[j]
							+ "\n---------");
				}
				gotField[possibilities[j]] = 0;
			}

			//check if you need to block
			for (int i = 0; i < possibilities.length; i++) {
				gotField[possibilities[i]] = 2;
				if (checkForWin(gotField) == 2) {
					gotField[possibilities[i]] = 0;
					return possibilities[i];
				}
				gotField[possibilities[i]] = 0;
			}

			//get best possibility
			double[] bestPossibility = { -1000.0, -1000.0 };
			for (int k = 0; k < possibilities.length; k++) {
				if (WinChancesForPossibilities[k] > bestPossibility[1]) {
					bestPossibility[0] = possibilities[k];
					bestPossibility[1] = WinChancesForPossibilities[k];
				}
			}
			return (int) bestPossibility[0];
		}
		return -1;
	}

	//play a game against a random player
	private int rematch(int[] field) {
		int[] playField = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++) {
			playField[i] = field[i];
		}

		boolean play = true;

		if (count(1, playField) < count(2, playField)) {
			play = true;
		} else if (count(1, playField) == count(2, playField)) {
			if (2 == ((int) (Math.random() * 2) + 1)) {
				play = false;
			}
		}
		int win = 0;
		boolean draw = false;

		draw = checkForDraw(playField);
		win = checkForWin(playField);
		while (win == 0 && draw == false) {
			if (play == true) {
				playField[checkRandomField(playField)] = 1;
				play = !play;
			} else {
				playField[checkRandomField(playField)] = 2;
				play = !play;
			}
			win = checkForWin(playField);
			if (win == 0) {
				draw = checkForDraw(playField);
			}
		}
		int endwin = 0;
		if (win == 1) {
			endwin = 1;
		} else if (draw == true) {
			endwin = 0;
		} else {
			endwin = 2;
		}
		return endwin;
	}

	//get free places in a field
	private int[] possibilities(int[] field) {
		int freeFields = 0;
		int[] Values = null;
		for (int i = 0; i < SIZE * SIZE; i++) {
			if (field[i] == 0) {
				freeFields++;
			}
		}
		if (freeFields != 0) {
			Values = new int[freeFields];
			int k = 0;
			for (int i = 0; i < SIZE * SIZE; i++) {
				if (field[i] == 0) {
					Values[k] = i;
					k++;
				}
			}
		}
		return Values;
	}

	//let the ML alg play against a human
	private void playAgainstHuman() {
		Scanner sc = new Scanner(System.in);
		String sureChoice = "";
		do {
			System.out.println("Sie sind X. Der PC ist O.\nMöchten Sie anfangen?");
			System.out.println("J. Ja\nN. Nein");
			sureChoice = sc.next();
		} while (!(sureChoice.toLowerCase().equals("j")) && !(sureChoice.toLowerCase().equals("n")));
		int[] field = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++) {
			field[i] = 0;
		}

		boolean YourTurn = false;
		if (sureChoice.toLowerCase().equals("j")) {
			YourTurn = true;
		}
		boolean draw = false;
		int win = 0;

		do {
			printScreen(field);
			if (YourTurn) {
				System.out.println("Du bist an der Reihe.");

				boolean correctInput = false;
				System.out.println("Bitte geben Sie ihr Feld ein. Nummer zuerst und dann Buchstabe (Bsp. 1A)");
				int input = sc.nextInt();
				int Number = input;
				// put new value in field and check for win
				if (YourTurn) {
					field[Number] = 2;
				}
				YourTurn = !YourTurn;
			} else {
				field[getRightField(field)] = 1;
				YourTurn = !YourTurn;
			}
			win = checkForWin(field);
			draw = checkForDraw(field);
		} while (win == 0 && draw == false);
		printScreen(field);
		if (win == 1) {
			System.out.print("Spieler 1 mit X hat gewonnen.");
		} else if (win == 2) {
			System.out.print("Spieler 2 mit O hat gewonnen.");
		} else {
			System.out.println("DRAW!");
		}
	}

	//calculate the Field form a Number
	public int[] calcField(int fieldNr) {
		int[] field = new int[SIZE * SIZE];
		for (int i = 0; i < SIZE * SIZE; i++) {
			field[i] = 0;
		}
		int remaining = fieldNr;
		int result = fieldNr;
		int i = SIZE * SIZE - 1;
		while (result != 0) {
			result = remaining / 3;
			int rest = remaining % 3;
			field[i] = rest;
			remaining = result;
			i--;
		}
		return field;
	}
	
	//initialize the different possibilities with random numbers
	public void init() {
		for (int i = 0; i < POSSIBILITIES; i++) {
			int[] field = calcField(i);
			int check = checkRandomField(field);
			if (check != -1) {
				round[i] = checkRandomField(field);
			}
		}
	}
	
	//calculate the Number form a Field
	public int calcNumber(int[] field) {
		int finalInt = 0;
		int[] threes = { 1, 3, 9, 27, 81, 243, 729, 2187, 6561 };
		for (int i = 0, j = SIZE * SIZE - 1; i < SIZE * SIZE; i++, j--) {
			finalInt += field[i] * threes[j];
		}
		return finalInt;
	}

	//return how many times a value is present in a field
	private int count(int i, int[] field) {
		int j = 0;
		for (int k = 0; k < SIZE * SIZE; k++) {
			if (field[k] == i) {
				j++;
			}
		}
		return j;
	}

	//get the corresponding reaction to a field
	public int getRightField(int[] field) {
		int fieldNr = calcNumber(field);
		return round[fieldNr];
	}

	//get a random reaction to a field
	private int checkRandomField(int[] field) {
		int[] freeFields = possibilities(field);
		if (freeFields != null) {
			int randomNum = (int) ((Math.random() * freeFields.length));
			return freeFields[randomNum];
		}
		return -1;
	}

	//print field to screen
	public void printScreen(int[] field) {
		System.out.println("-------------------");
		for (int i = 0, j = 0; i < SIZE * SIZE; i++) {
			j++;
			System.out.print("[" + field[i] + "]");
			if (j == 3) {
				System.out.println("");
				j = 0;
			}
		}
	}

	//check weather the game was a draw
	private boolean checkForDraw(int[] inputField) {
		int freeFields = 0;
		for (int i = 0; i < SIZE * SIZE; i++) {
			if (inputField[i] == 0) {
				freeFields++;
			}
		}
		if (freeFields > 0) {
			return false;
		} else {
			return true;
		}
	}

	//check weather the game is won or not
	private int checkForWin(int[] InputField) {

		// Inputfield to 2dimensional array
		int[][] field = new int[SIZE][SIZE];
		int x = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				field[i][j] = InputField[x++];
			}
		}

		// Check for Vertical and Horizontal
		boolean win = true;
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < SIZE; i++) {
				win = true;
				int Check = -1;
				if (k == 0) {
					Check = field[i][0];
				} else {
					Check = field[0][i];
				}
				for (int j = 0; j < SIZE; j++) {
					if (k == 0) {
						if (field[i][j] != Check) {
							win = false;
							break;
						}
					} else {
						if (field[j][i] != Check) {
							win = false;
							break;
						}
					}
				}
				if (win == true) {

					if (Check == 1) {
						return 1;
					} else if (Check == 2) {
						return 2;
					}
				}
			}
		}

		// Check for diagonal win
		for (int k = 0; k < 2; k++) {
			win = true;
			int Check = -1;
			if (k == 0) {
				Check = field[0][0];
				int i = 0, j = 0;
				while (i + 1 < SIZE && j + 1 < SIZE) {
					int next = field[i + 1][j + 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j++;
				}
			} else {
				Check = field[0][SIZE - 1];
				int i = 0, j = SIZE - 1;
				while (i + 1 < SIZE && j - 1 >= 0) {
					int next = field[i + 1][j - 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j--;
				}
			}

			if (win == true && Check == 1) {
				return 1;
			} else if (win == true && Check == 2) {
				return 2;
			}
		}
		return 0;
	}
}