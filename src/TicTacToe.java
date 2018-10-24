import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToe {

	private static final int XSIZE = 3;
	private static final int YSIZE = 3;
	private static int ROUND = 0;

	public static void main(String[] args) {
		/*
		 * char[] beispiel = new char[XSIZE*YSIZE];
		 * 
		 * for(int i = 0; i < XSIZE*YSIZE; i++){ beispiel[i] = '_'; }
		 * 
		 * boolean isEmpty = (beispiel[2] == '_'); if(isEmpty == true) {
		 * beispiel[2] = 'X'; } beispiel[4] = 'X';
		 * 
		 * /// for(int i = 0, j = 0; i < YSIZE*XSIZE; i++){ j++;
		 * System.out.print("["+beispiel[i]+"]"); if(j == 3){
		 * System.out.println(""); j = 0; } } /////
		 */

		System.out.println("Willkommen beim TicTacToe von Halvar Kelm!");

		Scanner sc = new Scanner(System.in);
		int choise = 0;
		do {
			System.out
					.println("Wenn Sie mit zwei Spielern spielen möchten, geben Sie 1 an. Wenn Sie gegen den PC spielen möchten, die 2.");
			choise = sc.nextInt();
		} while ((choise != 1) && (choise != 2));
		if (choise == 1) {
			PvP();
		} else {
			PvE();
		}
	}

	private static int PvP() {
		clear();
		Scanner sc = new Scanner(System.in);
		char[][] field = new char[3][3];
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				field[i][j] = '_';
			}
		}

		boolean XTurn = true;
		boolean draw = false;
		int win = 0;

		do {
			printScreen(field);
			if (XTurn) {
				System.out.println("Spieler 1 mit X ist dran");
			} else {
				System.out.println("Spieler 2 mit O ist dran");
			}
			boolean correctInput = false;
			int YNumber = -1;
			int XNumber = -1;
			do {
				System.out
						.println("Bitte geben Sie ihr Feld ein. Nummer zuerst und dann Buchstabe (Bsp. 1A)");
				String input = sc.next();
				YNumber = Character.getNumericValue(input.charAt(0)) - 1;
				char letter = input.toUpperCase().charAt(1);
				XNumber = ((Character.getNumericValue(letter)) - 10);
				if ((0 <= YNumber && YNumber < YSIZE)
						&& (0 <= XNumber && XNumber < XSIZE)) {
					if (isFree(field, YNumber, XNumber)) {
						correctInput = true;
					} else {
						clear();
						System.out
								.println("Feld ist nicht leer. Bitte wähle ein anderes Feld.");
						printScreen(field);
					}
				}
			} while (!correctInput);
			// put new value in field and check for win
			if (XTurn) {
				field[YNumber][XNumber] = 'X';
			} else {
				field[YNumber][XNumber] = 'O';
			}
			XTurn = !XTurn;
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
		return 0;
	}

	private static int PvE() {
		Scanner sc = new Scanner(System.in);
		int choise = 0;
		do {
			System.out.println("Bitte wählen Sie den Schwierigkeitsgrad:");
			System.out.println("1. einfach\n2. schwer");
			choise = sc.nextInt();
			if (choise == 2) {
				String sureChoice = "";
				do {
					System.out
							.println("Sind Sie sich wirklich sicher, dass Sie das richtige gewählt haben? Das ist ihre letzte Chance umzukehren...");
					System.out.println("J. Ja\nN. Nein");
					sureChoice = sc.next();
					if (sureChoice.toLowerCase().equals("j")) {
						choise = 2;
					} else {
						choise = 3;
					}
				} while ((sureChoice.toLowerCase().contains("j"))
						&& (sureChoice.toLowerCase().contains("n")));
			}
		} while ((choise != 1) && (choise != 2));

		if (choise == 1) {
			easyPC();
		} else {
			hardPC();
		}

		return 0;
	}

	private static void clear() {
		// clear screen
	}

	private static void easyPC() {
		Scanner sc = new Scanner(System.in);
		String sureChoice = "";
		do {
			System.out
					.println("Sie sind X. Der PC ist O.\nMöchten Sie anfangen?");
			System.out.println("J. Ja\nN. Nein");
			sureChoice = sc.next();
		} while (!(sureChoice.toLowerCase().equals("j"))
				&& !(sureChoice.toLowerCase().equals("n")));
		clear();
		char[][] field = new char[3][3];
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				field[i][j] = '_';
			}
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
				int YNumber = -1;
				int XNumber = -1;
				do {
					System.out
							.println("Bitte geben Sie ihr Feld ein. Nummer zuerst und dann Buchstabe (Bsp. 1A)");
					String input = sc.next();
					YNumber = Character.getNumericValue(input.charAt(0)) - 1;
					char letter = input.toUpperCase().charAt(1);
					XNumber = ((Character.getNumericValue(letter)) - 10);
					if ((0 <= YNumber && YNumber < YSIZE)
							&& (0 <= XNumber && XNumber < XSIZE)) {
						if (isFree(field, YNumber, XNumber)) {
							correctInput = true;
						} else {
							clear();
							System.out
									.println("Feld ist nicht leer. Bitte wähle ein anderes Feld.");
							printScreen(field);
						}
					}
				} while (!correctInput);
				// put new value in field and check for win
				if (YourTurn) {
					field[YNumber][XNumber] = 'X';
				}
				YourTurn = !YourTurn;
			} else {
				int freeFields = 0;
				for (int i = 0; i < XSIZE; i++) {
					for (int j = 0; j < YSIZE; j++) {
						if (field[i][j] == '_') {
							freeFields++;
						}
					}
				}
				int[] XValues = new int[freeFields];
				int[] YValues = new int[freeFields];
				int k = 0;
				for (int i = 0; i < XSIZE; i++) {
					for (int j = 0; j < YSIZE; j++) {
						if (field[i][j] == '_') {
							XValues[k] = i;
							YValues[k] = j;
							k++;
						}
					}
				}
				int randomNum = ThreadLocalRandom.current().nextInt(0,
						freeFields + 1);
				field[XValues[randomNum / 2]][YValues[randomNum / 2]] = 'O';
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

	private static void hardPC() {
		// MACHINE LEARNING ALGORITHM
		/*
		 * Use single dimensional array as field. Berechne eine Stellung als
		 * ((feld1 * 3) + feld2)*3 => Eine Zahl Habe ein Array aus Zügen, dass
		 * für eine bestimmte Stellung einen bestimmten Zug ausgiebt Habe ein
		 * Array mit der Historie, Historie[t] ist eine Stellung Advantages: -
		 * Something new - +Points - Interessting solution Disadvantages: - Hard
		 * to implement
		 */
		// ODER:
		/*
		 * Use two dimensional array as field. Handle different cases to never
		 * lose. Advantages: - Something new - +Points - Interessting solution
		 * Disadvantages: - Hard to implement
		 */
	}

	private static boolean isFree(char field[][], int YNumber, int XNumber) {
		try {
			if (field[YNumber][XNumber] == '_') {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private static void printScreen(char[][] field) {
		System.out.println("ROUND " + ROUND++);
		System.out.println("------------");
		System.out.println("   A    B    C");
		int[] lines = new int[XSIZE];
		for (int i = 0, j = 1; i < XSIZE; i++, j++) {
			lines[i] = j;
		}
		for (int i = 0; i < XSIZE; i++) {
			System.out.print(lines[i]);
			for (int j = 0; j < YSIZE; j++) {
				System.out.printf(" [%c] ", field[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.println("------------");
	}

	private static int checkForWin(char field[][]) {
		/*
		 * WIN CASES: - ALL OF A ROW - ALL OF A COLUMN - LEFT UPPER CORNER TO
		 * RIGHT LOWER - TOP RIGHT CORNER TO LEFT LOWER
		 */

		// Check for Vertical and Horizontal
		boolean win = true;
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < YSIZE; i++) {
				win = true;
				char Check = ' ';
				if (k == 0) {
					Check = field[i][0];
				} else {
					Check = field[0][i];
				}
				for (int j = 0; j < XSIZE; j++) {
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

					if (Check == 'X') {
						return 1;
					} else if (Check == 'O') {
						return 2;
					}
				}
			}
		}

		// Check for diagonal win
		for (int k = 0; k < 2; k++) {
			win = true;
			char Check = ' ';
			if (k == 0) {
				Check = field[0][0];
				int i = 0, j = 0;
				while (i + 1 < XSIZE && j + 1 < YSIZE) {
					char next = field[i + 1][j + 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j++;
				}
			} else {
				Check = field[0][YSIZE - 1];
				int i = 0, j = YSIZE - 1;
				while (i + 1 < XSIZE && j - 1 >= 0) {
					char next = field[i + 1][j - 1];
					if (next != Check) {
						win = false;
					}
					Check = next;
					i++;
					j--;
				}
			}

			if (win == true && Check == 'X') {
				return 1;
			} else if (win == true && Check == 'O') {
				return 2;
			}
		}
		return 0;
	}

	private static boolean checkForDraw(char field[][]) {
		int freeFields = 0;
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				if (field[i][j] == '_') {
					freeFields++;
				}
			}
		}
		if (freeFields == 0) {
			return true;
		}
		return false;
	}

}