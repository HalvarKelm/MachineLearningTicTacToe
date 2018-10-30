import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TicTacToe {

	// size of field
	private static final int XSIZE = 3;
	private static final int YSIZE = 3;

	// track the number of played rounds
	private static int ROUND = 0;

	public static void main(String[] args) {
		System.out.println("Willkommen beim TicTacToe von Halvar Kelm!");

		// Scanner to get input
		Scanner sc = new Scanner(System.in);

		// track the choice of the user
		int choice = 0;
		String choiceString = "";

		// repeat till the player doesn't want to play anymore
		do {
			// get correct input from user
			do {
				System.out.println("Wenn Sie mit zwei Spielern spielen möchten, geben Sie 1 an.\nWenn Sie gegen den PC spielen möchten, die 2.");
				choice = sc.nextInt();
			} while ((choice != 1) && (choice != 2));

			// handle choice between 1 and 2
			if (choice == 1) {
				PvP();
			} else {
				PvE();
			}

			// give the user the choice to play again
			System.out.println("Wollen Sie noch einmal spielen?");
			System.out.println("J. Ja\nN. Nein");
			choiceString = sc.next();

		} while (choiceString.toLowerCase().contains("j"));
		// end the program
		sc.close();
		return;
	}
	
	// handle a Player vs Player game
	private static int PvP() {

		// handle user input
		Scanner sc = new Scanner(System.in);

		// Initialize the field
		char[][] field = new char[3][3];
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				field[i][j] = '_';
			}
		}

		// declare and initialize variables
		boolean XTurn = true;
		boolean draw = false;
		int win = 0;

		// play till someone won or it is a draw
		do {
			printScreen(field);
			if (XTurn) {
				System.out.println("Spieler 1 mit X ist dran");
			} else {
				System.out.println("Spieler 2 mit O ist dran");
			}

			// store user input
			boolean correctInput = false;
			int YNumber = -1;
			int XNumber = -1;

			// get correct input
			do {

				System.out.println("Bitte geben Sie ihr Feld ein. Buchstabe zuerst und dann Nummer (Bsp. A1)");

				// handle the input and get the two values
				String input = sc.next();
				YNumber = Character.getNumericValue(input.charAt(1)) - 1; 
				char letter = input.toUpperCase().charAt(0);
				XNumber = ((Character.getNumericValue(letter)) - 10);

				// check if the input field is clear
				if ((0 <= YNumber && YNumber < YSIZE) && (0 <= XNumber && XNumber < XSIZE)) {
					if (isFree(field, YNumber, XNumber)) {
						correctInput = true;
					} else {
						System.out.println("Feld ist nicht leer. Bitte wähle ein anderes Feld.");
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

			// check for win and draw
			win = checkForWin(field);
			draw = checkForDraw(field);
		} while (win == 0 && draw == false);

		printScreen(field);

		// handle win and draw cases
		if (win == 1) {
			System.out.println("Spieler 1 mit X hat gewonnen.");
		} else if (win == 2) {
			System.out.println("Spieler 2 mit O hat gewonnen.");
		} else {
			System.out.println("DRAW!");
		}
		ROUND = 0;
		return 0;
	}

	// handle player vs enemy
	private static int PvE() {
		
		//handle user input
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		
		//check for correct input
		do {
			System.out.println("Bitte wählen Sie den Schwierigkeitsgrad:");
			System.out.println("1. einfach\n2. schwer");
			choice = sc.nextInt();
		} while ((choice != 1) && (choice != 2));

		//setup object of the machine learning tic tac toe
		MachineLearningTicTacToe ml = new MachineLearningTicTacToe();
		if (choice == 2) {
			
			//initialize and train the machine learning algorithm
			System.out.println("Training \"machine learning algorithm\"...");
			ml.init();
			ml.train();
		}

		//check weather the user wants to start or not
		String sureChoice = "";
		do {
			System.out.println("Sie sind X. Der PC ist O.\nMöchten Sie anfangen?");
			System.out.println("J. Ja\nN. Nein");
			sureChoice = sc.next();

		} while (!(sureChoice.toLowerCase().equals("j")) && !(sureChoice.toLowerCase().equals("n")));
		
		//setup play field
		char[][] field = new char[3][3];
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				field[i][j] = '_';
			}
		}

		//check weather player starts or not
		boolean YourTurn = false;
		if (sureChoice.toLowerCase().equals("j")) {
			YourTurn = true;
		}
		boolean draw = false;
		int win = 0;

		// play till someone won or it is a draw
		do {
			printScreen(field);
			
			//handle user playing
			if (YourTurn) {
				System.out.println("Du bist an der Reihe.");
				
				// store user input
				boolean correctInput = false;
				int YNumber = -1;
				int XNumber = -1;
				
				// get correct input
				do {

					System.out.println("Bitte geben Sie ihr Feld ein. Buchstabe zuerst und dann Nummer (Bsp. A1)");

					// handle the input and get the two values
					String input = sc.next();
					YNumber = Character.getNumericValue(input.charAt(1)) - 1;
					char letter = input.toUpperCase().charAt(0);
					XNumber = ((Character.getNumericValue(letter)) - 10);

					// check if the input field is clear
					if ((0 <= YNumber && YNumber < YSIZE) && (0 <= XNumber && XNumber < XSIZE)) {
						if (isFree(field, YNumber, XNumber)) {
							correctInput = true;
						} else {
							System.out.println("Feld ist nicht leer. Bitte wähle ein anderes Feld.");
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
				
				//handle random choosing pc
				if (choice == 1) {
					
					//store all places with no user input
					int freeFields = 0;
					for (int i = 0; i < XSIZE; i++) {
						for (int j = 0; j < YSIZE; j++) {
							if (field[i][j] == '_') {
								freeFields++;
							}
						}
					}
					
					//save value pairs of X and Y values
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
					
					//check a random on of these pair values
					int randomNum = ThreadLocalRandom.current().nextInt(0, freeFields + 1);
					field[XValues[randomNum / 2]][YValues[randomNum / 2]] = 'O';
				} else {
					
					//create new 1 dimensional array
					int[] playField = new int[field.length];
					
					//translate 2D array to 1D array
					playField = toOneDimensionalArray(field);
					
					//get best choice from machine learning algorithm
					int choiceField = ml.getRightField(playField);
					
					//place value in 2D array
					toTwoDimensionalArray(choiceField, field);
				}
				YourTurn = !YourTurn;
			}
			
			//check or win and draw
			win = checkForWin(field);
			draw = checkForDraw(field);
			
		} while (win == 0 && draw == false);
		
		// handle win and draw cases
		printScreen(field);
		if (win == 1) {
			System.out.println("Spieler 1 mit X hat gewonnen.");
		} else if (win == 2) {
			System.out.println("Spieler 2 mit O hat gewonnen.");
		} else {
			System.out.println("DRAW!");
		}
		ROUND = 0;
		return 0;
	}

	//get a value from a one dimensional array and put it in the corresponding place in the two dimensional array
	private static void toTwoDimensionalArray(int k, char[][] field) {
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				if (k == 0) {
					field[i][j] = 'O';
				}
				k--;
			}
		}
	}

	//get a two dimensional array and translate it to a one dimensional array
	private static int[] toOneDimensionalArray(char[][] field) {
		int[] playField = new int[XSIZE * YSIZE];
		printScreen(field);
		int k = 0;
		for (int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				switch (field[i][j]) {
				case 'X':
					playField[k++] = 2;
					break;
				case 'O':
					playField[k++] = 1;
					break;
				default:
					playField[k++] = 0;
					break;
				}
			}
		}
		return playField;
	}

	//check if a field in a 2D array is free
	private static boolean isFree(char field[][], int YNumber, int XNumber) {
		try {
			if (field[YNumber][XNumber] == '_') {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	//print field to screen
	private static void printScreen(char[][] field) {
		System.out.println("ROUND " + ROUND++);
		System.out.println("------------");
		System.out.println("   A    B    C");
		
		//print values next to lines
		int[] lines = new int[XSIZE];
		for (int i = 0, j = 1; i < XSIZE; i++, j++) {
			lines[i] = j;
		}
		
		//print field
		for (int i = 0; i < XSIZE; i++) {
			System.out.print(lines[i]);
			for (int j = 0; j < YSIZE; j++) {
				System.out.printf(" [%c] ", field[i][j]);
			}
			
			System.out.printf("\n");
		}
		System.out.println("------------");
	}

	//check if a player won the game
	private static int checkForWin(char field[][]) {

		// Check for vertical and horizontal cases
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

		// Check for diagonal win cases
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

	//check weather the game is completely full and no one won
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